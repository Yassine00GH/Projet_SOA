package com.YassineGhorbel.MedecinProj;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.YassineGhorbel.MedecinProj.security.SecParams;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class SecurityJWTTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    private String generateToken(String username, String... roles) {
        return JWT.create()
                .withSubject(username)
                .withArrayClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecParams.EXP_TIME))
                .sign(Algorithm.HMAC256(SecParams.SECRET));
    }

    @Test
    void testAtelier15MedecinApiRoleRestrictions() throws Exception {
        String userToken = generateToken("yassine", "USER");
        String adminToken = generateToken("admin", "ADMIN", "USER");

        // Unauthenticated cannot consult /api/all (403 Forbidden)
        mockMvc.perform(get("/api/all"))
                .andExpect(status().isForbidden());

        // USER can consult /api/all
        mockMvc.perform(get("/api/all")
                        .header("Authorization", SecParams.PREFIX + userToken))
                .andExpect(status().isOk());

        String newMedecinJson = """
                {
                    "nom": "Ghorbel",
                    "prenom": "Yassine",
                    "email": "yassine@medecin.tn",
                    "telephone": "23320877"
                }
                """;

        // USER cannot add a medecin (403 Forbidden)
        mockMvc.perform(post("/api/addmed")
                        .header("Authorization", SecParams.PREFIX + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMedecinJson))
                .andExpect(status().isForbidden());

        // ADMIN can add a medecin (200 OK)
        mockMvc.perform(post("/api/addmed")
                        .header("Authorization", SecParams.PREFIX + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMedecinJson))
                .andExpect(status().isOk());
    }

    @Test
    void testAtelier16bisCorsPreflightAndAuthorizationHeader() throws Exception {
        String adminToken = generateToken("admin", "ADMIN", "USER");

        mockMvc.perform(options("/api/all")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(options("/api/addmed")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(options("/api/updatemed")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "PUT")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(options("/api/delmed/1")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "DELETE")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(options("/rest/specialites")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(get("/api/all")
                        .header("Origin", "http://localhost:4200")
                        .header("Authorization", SecParams.PREFIX + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(get("/rest/specialites")
                        .header("Origin", "http://localhost:4200")
                        .header("Authorization", SecParams.PREFIX + adminToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

        mockMvc.perform(get("/api/medsByName/Ghor")
                        .header("Origin", "http://localhost:4200")
                        .header("Authorization", SecParams.PREFIX + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/MedecinSpec/1")
                        .header("Origin", "http://localhost:4200")
                        .header("Authorization", SecParams.PREFIX + adminToken))
                .andExpect(status().isOk());
    }
}
