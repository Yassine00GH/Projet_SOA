package com.YassineGhorbel.MedecinProj.services;

import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.repositories.MedecinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedecinServiceImpl implements MedecinService{

    private final MedecinRepository medecinRepository;



    public MedecinServiceImpl(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }


    @Override
    public Medecin saveMedecin(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    @Override
    public Medecin updateMedecin(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    @Override
    public void deleteMedecin(Medecin medecin) {
        medecinRepository.delete(medecin);
    }

    @Override
    public void deleteMedecinById(Long id) {
        medecinRepository.deleteById(id);
    }

    @Override
    public Medecin getMedecin(Long id) {
        return medecinRepository.findById(id).get();
    }

    @Override
    public List<Medecin> getAllMedecin() {
        return medecinRepository.findAll();
    }

    @Override
    public List<Medecin> findByNom(String nom) {
        return medecinRepository.findByNom(nom);
    }

    @Override
    public List<Medecin> findByNomContains(String nom) {
        return medecinRepository.findByNomContains(nom);
    }

    @Override
    public List<Medecin> findByNomPrenom(String nom, String prenom) {
        return medecinRepository.findByNomPrenom(nom, prenom);
    }

    @Override
    public List<Medecin> findBySpecialite(Specialite specialite) {
        return medecinRepository.findBySpecialite(specialite);
    }

    @Override
    public List<Medecin> findBySpecialiteId(Long id) {
        return medecinRepository.findBySpecialiteId(id);
    }

    @Override
    public List<Medecin> findByOrderByNomAsc() {
        return medecinRepository.findByOrderByNomAsc();
    }

    @Override
    public List<Medecin> trierMedecinsNomsPrenoms() {
        return medecinRepository.trierMedecinsNomsPrenoms();
    }
}
