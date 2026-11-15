package com.YassineGhorbel.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.YassineGhorbel.users.entities.User;
import com.YassineGhorbel.users.entities.VerificationToken;
import com.YassineGhorbel.users.repos.UserRepository;
import com.YassineGhorbel.users.repos.VerificationTokenRepository;
import com.YassineGhorbel.users.security.SecParams;
import com.YassineGhorbel.users.util.EmailSender;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class UsersMicroserviceApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @MockitoBean
    private EmailSender emailSender;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        String loginJson = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andReturn();
        return result.getResponse().getHeader("Authorization");
    }

    @Test
    void testSuccessfulLoginGeneratesValidJWT() throws Exception {
        String token = loginAndGetToken("yassine", "123");
        assertNotNull(token);

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecParams.SECRET)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        assertEquals("yassine", decodedJWT.getSubject());
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        assertTrue(roles.contains("USER"));
    }

    @Test
    void testAllUsersEndpointRestrictedToAdmin() throws Exception {
        String userToken = loginAndGetToken("yassine", "123");
        String adminToken = loginAndGetToken("admin", "123");

        mockMvc.perform(get("/all")
                        .header("Authorization", SecParams.PREFIX + userToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/all")
                        .header("Authorization", SecParams.PREFIX + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void testInscriptionVerificationEmailAndDisabledUserLogin() throws Exception {
        String registerJson = """
                {
                    "username": "nouveauUser",
                    "password": "secret123",
                    "email": "nouveau@example.com"
                }
                """;

        mockMvc.perform(post("/register")
                        .header("Origin", "http://localhost:4200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
                .andExpect(jsonPath("$.username").value("nouveauUser"))
                .andExpect(jsonPath("$.email").value("nouveau@example.com"))
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.roles[0].role").value("USER"));

        verify(emailSender).sendEmail(eq("nouveau@example.com"), anyString());

        mockMvc.perform(post("/register")
                        .header("Origin", "http://localhost:4200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("USER_EMAIL_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("email déjà existant!"));

        mockMvc.perform(post("/login")
                        .header("Origin", "http://localhost:4200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"nouveauUser\",\"password\":\"secret123\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCause").value("disabled"))
                .andExpect(jsonPath("$.message").value("L'utilisateur est désactivé !"));

        mockMvc.perform(get("/verifyEmail/000000")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("INVALID_TOKEN"));

        User expiredUser = new User(null, "expiredUser", "pass", false, "expired@example.com", null);
        expiredUser = userRepository.save(expiredUser);
        VerificationToken expiredToken = new VerificationToken("999999", expiredUser);
        expiredToken.setExpirationTime(new Date(System.currentTimeMillis() - 60_000));
        verificationTokenRepository.save(expiredToken);

        mockMvc.perform(get("/verifyEmail/999999")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("EXPIRED_TOKEN"));

        assertNull(verificationTokenRepository.findByToken("999999"));

        User createdUser = userRepository.findByEmail("nouveau@example.com").orElseThrow();
        VerificationToken validToken = verificationTokenRepository.findAll().stream()
                .filter(t -> t.getUser().getUser_id().equals(createdUser.getUser_id()))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/verifyEmail/" + validToken.getToken())
                        .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
                .andExpect(jsonPath("$.enabled").value(true));

        String token = loginAndGetToken("nouveauUser", "secret123");
        assertNotNull(token);
    }
}