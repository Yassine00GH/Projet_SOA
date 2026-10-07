package com.YassineGhorbel.MedecinProj;

import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.repositories.MedecinRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class MedecinProjApplicationTests {

	@Autowired
	private MedecinRepository medecinRepository;

	@Test
	public void testCreateMedecin() {
		Medecin medecin = new Medecin(null, "ahmed", "ghorbel", "ahmedghorbel62@gmail.com",
				"21320877", new Date(), null);
		Medecin savedMedecin = medecinRepository.save(medecin);
	}

	@Test
	public void testFindMedecin() {
		Medecin medecin = medecinRepository.findById(1L).get();
		System.out.println(medecin);
	}

	@Test
	public void testUpdateMedecin() {
		Medecin medecin = medecinRepository.findById(1L).get();
		medecin.setPrenom("yassineghorbel");
		medecin.setNom("ghorbel");
		Medecin savedMedecin = medecinRepository.save(medecin);
		System.out.println(medecin);
	}

	@Test
	public void testDeleteMedecin() {
		medecinRepository.deleteById(1L);
		System.out.println("Medecin deleted");
	}

	@Test
	public void testFindAllMedecin() {
		List<Medecin> medecins = medecinRepository.findAll();
		for (Medecin medecin : medecins) {
			System.out.println(medecin);
		}
	}

	@Test
	public void testFindMedecinByNom() {
		List<Medecin> medecin = medecinRepository.findByNom("yassine");
		for (Medecin medecin1 : medecin) {
			System.out.println(medecin1);
		}
	}

	@Test
	public void testFindMedecinByNomContains() {
		List<Medecin> medecin = medecinRepository.findByNomContains("a");
		for (Medecin medecin1 : medecin) {
			System.out.println(medecin1);
		}
	}

	@Test
	public void testFindByNomPrenom() {
		List<Medecin> medecins = medecinRepository.findByNomPrenom("ne", "bel");
		afficherMedecins(medecins);
	}

	@Test
	public void testFindBySpecialite() {
		Specialite specialite = new Specialite();
		specialite.setId(1L);
		List<Medecin> medecins = medecinRepository.findBySpecialite(specialite);
		afficherMedecins(medecins);
	}

	@Test
	public void testFindBySpecialiteId() {
		List<Medecin> medecins = medecinRepository.findBySpecialiteId(1L);
		afficherMedecins(medecins);
	}

	@Test
	public void testFindByOrderByNomAsc() {
		List<Medecin> medecins = medecinRepository.findByOrderByNomAsc();
		afficherMedecins(medecins);
	}

	@Test
	public void testTrierMedecinsNomsPrenoms() {
		List<Medecin> medecins = medecinRepository.trierMedecinsNomsPrenoms();
		afficherMedecins(medecins);
	}

	private void afficherMedecins(List<Medecin> medecins) {
		System.out.println("Nombre de medecins trouves : " + medecins.size());
		if (medecins.isEmpty()) {
			System.out.println("Aucun medecin trouve.");
			return;
		}
		medecins.forEach(System.out::println);
	}

}
