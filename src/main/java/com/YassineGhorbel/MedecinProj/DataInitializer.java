package com.YassineGhorbel.MedecinProj;

import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.repositories.SpecialiteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpecialiteRepository specialiteRepository;

    public DataInitializer(SpecialiteRepository specialiteRepository) {
        this.specialiteRepository = specialiteRepository;
    }

    @Override
    public void run(String... args) {
        if (specialiteRepository.count() == 0) {
            specialiteRepository.saveAll(List.of(
                    new Specialite(null, "Cardiologie", "Coeur et systeme cardiovasculaire", null),
                    new Specialite(null, "Dermatologie", "Peau et maladies cutanees", null),
                    new Specialite(null, "Medecine generale", "Soins et suivi general", null)
            ));
        }
    }
}
