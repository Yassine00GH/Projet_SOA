package com.YassineGhorbel.MedecinProj.services;

import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;

import java.util.List;

public interface MedecinService {
    Medecin saveMedecin(Medecin medecin);
    Medecin updateMedecin(Medecin medecin);
    void deleteMedecin(Medecin medecin);
    void deleteMedecinById(Long id);
    Medecin getMedecin(Long id);
    List<Medecin> getAllMedecin();
    List<Medecin> findByNom(String nom);
    List<Medecin> findByNomContains(String nom);
    List<Medecin> findByNomPrenom(String nom, String prenom);
    List<Medecin> findBySpecialite(Specialite specialite);
    List<Medecin> findBySpecialiteId(Long id);
    List<Medecin> findByOrderByNomAsc();
    List<Medecin> trierMedecinsNomsPrenoms();
}
