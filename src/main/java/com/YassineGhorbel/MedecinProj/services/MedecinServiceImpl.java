package com.YassineGhorbel.MedecinProj.services;

import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.repositories.MedecinRepository;
import com.YassineGhorbel.MedecinProj.repositories.SpecialiteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MedecinServiceImpl implements MedecinService{

    private final MedecinRepository medecinRepository;
    private final SpecialiteRepository specialiteRepository;



    public MedecinServiceImpl(MedecinRepository medecinRepository, SpecialiteRepository specialiteRepository) {
        this.medecinRepository = medecinRepository;
        this.specialiteRepository = specialiteRepository;
    }


    @Override
    public Medecin saveMedecin(Medecin medecin) {
        medecin.setId(null);
        return medecinRepository.save(medecin);
    }

    @Override
    public Medecin updateMedecin(Medecin medecin) {
        if (medecin.getSpecialite() != null) {
            Long specialiteId = medecin.getSpecialite().getId();
            if (specialiteId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'identifiant de la spécialité est obligatoire");
            }
            Specialite specialite = specialiteRepository.findById(specialiteId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spécialité introuvable"));
            medecin.setSpecialite(specialite);
        }
        if (medecin.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'identifiant du médecin est obligatoire");
        }
        if (!medecinRepository.existsById(medecin.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable");
        }
        return medecinRepository.save(medecin);
    }

    @Override
    public void deleteMedecin(Medecin medecin) {
        medecinRepository.delete(medecin);
    }

    @Override
    public void deleteMedecinById(Long id) {
        if (!medecinRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable");
        }
        medecinRepository.deleteById(id);
    }

    @Override
    public Medecin getMedecin(Long id) {
        return medecinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin introuvable"));
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
