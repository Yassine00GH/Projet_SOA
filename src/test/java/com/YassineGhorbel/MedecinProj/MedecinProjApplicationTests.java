package com.YassineGhorbel.MedecinProj;

import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.controllers.MedecinRESTController;
import com.YassineGhorbel.MedecinProj.repositories.MedecinRepository;
import com.YassineGhorbel.MedecinProj.repositories.SpecialiteRepository;
import com.YassineGhorbel.MedecinProj.services.MedecinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class MedecinProjApplicationTests {

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @Autowired
    private MedecinService medecinService;

    private Medecin medecin;
    private Specialite specialite;

    @BeforeEach
    void setUp() {
        medecinRepository.deleteAll();
        specialiteRepository.deleteAll();

        specialite = new Specialite();
        specialite.setNom("Cardiologie");
        specialite.setDescription("Santé cardiovasculaire");
        specialite = specialiteRepository.save(specialite);

        medecin = new Medecin(null, "ghorbel", "yassine", "yassine@example.com",
                "23320877", new Date(), specialite);
        medecin = medecinRepository.save(medecin);
    }

    @Test
    void testCreateMedecin() {
        Medecin nouveauMedecin = new Medecin(null, "benali", "ahmed", "ahmed@example.com",
                "21320877", new Date(), specialite);
        nouveauMedecin.setId(9L);

        Medecin savedMedecin = medecinService.saveMedecin(nouveauMedecin);

        assertNotNull(savedMedecin.getId());
        assertNotEquals(9L, savedMedecin.getId());
        assertEquals("ahmed", savedMedecin.getPrenom());
    }

    @Test
    void testFindMedecin() {
        Medecin foundMedecin = medecinRepository.findById(medecin.getId()).orElseThrow();

        assertEquals("ghorbel", foundMedecin.getNom());
    }

    @Test
    void testUpdateMedecin() {
        Specialite specialiteReçue = new Specialite(specialite.getId(), "Nom ignoré", "Description ignorée", null);
        Medecin medecinReçu = new Medecin(medecin.getId(), "ghorbel", "yassine-modifie",
                "yassine@example.com", "23320877", new Date(), specialiteReçue);

        Medecin updatedMedecin = medecinService.updateMedecin(medecinReçu);

        assertEquals("yassine-modifie", updatedMedecin.getPrenom());
        assertEquals("Cardiologie", updatedMedecin.getSpecialite().getNom());
    }

    @Test
    void testPutMedecinByPathIdAcceptsIsoDateAndNestedSpecialite() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new MedecinRESTController(medecinService)).build();
        String requestBody = """
                {
                  "id": 999,
                  "nom": "ghorbel",
                  "prenom": "updated-over-http",
                  "email": "yassine@example.com",
                  "telephone": "23320877",
                  "dateDiplome": "2025-10-25T00:00:00.000Z",
                  "specialite": {"id": %d, "nom": "Nom ignoré", "description": "Description ignorée"}
                }
                """.formatted(specialite.getId());

        mockMvc.perform(put("/api/{id}", medecin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Medecin updatedMedecin = medecinRepository.findById(medecin.getId()).orElseThrow();
        assertEquals("updated-over-http", updatedMedecin.getPrenom());
        assertNotNull(updatedMedecin.getDateDiplome());
        assertEquals("Cardiologie", updatedMedecin.getSpecialite().getNom());
        assertEquals(1, medecinRepository.count());
    }

    @Test
    void testPutMedecinAtCollectionUrlUsesBodyId() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new MedecinRESTController(medecinService)).build();
        String requestBody = """
                {
                  "id": %d,
                  "nom": "ghorbel",
                  "prenom": "updated-from-current-angular",
                  "email": "yassine@example.com",
                  "telephone": "23320877",
                  "dateDiplome": "2025-10-25T00:00:00.000Z",
                  "specialite": {"id": %d}
                }
                """.formatted(medecin.getId(), specialite.getId());

        mockMvc.perform(put("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Medecin updatedMedecin = medecinRepository.findById(medecin.getId()).orElseThrow();
        assertEquals("updated-from-current-angular", updatedMedecin.getPrenom());
    }

    @Test
    void testUpdateMedecinWithoutIdReturnsBadRequest() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> medecinService.updateMedecin(new Medecin()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testUpdateMissingMedecinReturnsNotFound() {
        Medecin missingMedecin = new Medecin();
        missingMedecin.setId(-1L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> medecinService.updateMedecin(missingMedecin));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDeleteMedecin() {
        Long id = medecin.getId();

        medecinRepository.deleteById(id);

        assertFalse(medecinRepository.findById(id).isPresent());
    }

    @Test
    void testFindAllMedecin() {
        assertEquals(1, medecinRepository.findAll().size());
    }

    @Test
    void testFindMedecinByNom() {
        assertEquals(1, medecinRepository.findByNom("ghorbel").size());
    }

    @Test
    void testFindMedecinByNomContains() {
        assertEquals(1, medecinRepository.findByNomContains("or").size());
    }

    @Test
    void testFindByNomPrenom() {
        assertEquals(1, medecinRepository.findByNomPrenom("ghorbel", "yassine").size());
        assertTrue(medecinRepository.findByNomPrenom("ghorbel", "autre").isEmpty());
    }

    @Test
    void testFindBySpecialite() {
        assertEquals(1, medecinRepository.findBySpecialite(specialite).size());
    }

    @Test
    void testFindBySpecialiteId() {
        assertEquals(1, medecinRepository.findBySpecialiteId(specialite.getId()).size());
    }

    @Test
    void testFindByOrderByNomAsc() {
        Medecin autreMedecin = new Medecin(null, "ahmed", "amine", "amine@example.com",
                "12345678", new Date(), specialite);
        medecinRepository.save(autreMedecin);

        List<Medecin> medecins = medecinRepository.findByOrderByNomAsc();

        assertEquals("ahmed", medecins.get(0).getNom());
    }

    @Test
    void testTrierMedecinsNomsPrenoms() {
        Medecin autreMedecin = new Medecin(null, "ghorbel", "amina", "amina@example.com",
                "12345678", new Date(), specialite);
        medecinRepository.save(autreMedecin);

        List<Medecin> medecins = medecinRepository.trierMedecinsNomsPrenoms();

        assertEquals("yassine", medecins.get(0).getPrenom());
        assertEquals("amina", medecins.get(1).getPrenom());
    }

}
