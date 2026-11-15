package com.YassineGhorbel.MedecinProj.controllers;

import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.repositories.MedecinRepository;
import com.YassineGhorbel.MedecinProj.repositories.SpecialiteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/specialites")
public class SpecialiteRESTController {
    private final SpecialiteRepository specialiteRepository;
    private final MedecinRepository medecinRepository;

    public SpecialiteRESTController(SpecialiteRepository specialiteRepository, MedecinRepository medecinRepository) {
        this.specialiteRepository = specialiteRepository;
        this.medecinRepository = medecinRepository;
    }

    @GetMapping
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Specialite getSpecialiteById(@PathVariable Long id) {
        return specialiteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialite introuvable"));
    }

    @PostMapping
    public ResponseEntity<Specialite> createSpecialite(@RequestBody Specialite specialite) {
        validateName(specialite.getNom());
        String name = specialite.getNom().trim();
        if (specialiteRepository.existsByNomIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette specialite existe deja");
        }
        specialite.setId(null);
        specialite.setNom(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialiteRepository.save(specialite));
    }

    @PutMapping("/{id}")
    public Specialite updateSpecialite(@PathVariable Long id, @RequestBody Specialite specialite) {
        validateName(specialite.getNom());
        Specialite existing = getSpecialiteById(id);
        String name = specialite.getNom().trim();
        if (!existing.getNom().equalsIgnoreCase(name) && specialiteRepository.existsByNomIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette specialite existe deja");
        }
        existing.setNom(name);
        existing.setDescription(specialite.getDescription());
        return specialiteRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecialite(@PathVariable Long id) {
        getSpecialiteById(id);
        if (medecinRepository.existsBySpecialite_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Impossible de supprimer une specialite attribuee a un medecin");
        }
        specialiteRepository.deleteById(id);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nom de la specialite est obligatoire");
        }
    }
}
