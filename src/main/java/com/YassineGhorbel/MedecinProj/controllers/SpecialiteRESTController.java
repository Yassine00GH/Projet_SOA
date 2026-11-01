package com.YassineGhorbel.MedecinProj.controllers;

import com.YassineGhorbel.MedecinProj.entities.Specialite;
import com.YassineGhorbel.MedecinProj.repositories.SpecialiteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/specialites")
@CrossOrigin("*")
public class SpecialiteRESTController {
    private final SpecialiteRepository specialiteRepository;

    public SpecialiteRESTController(SpecialiteRepository specialiteRepository) {
        this.specialiteRepository = specialiteRepository;
    }

    @GetMapping
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Specialite getSpecialiteById(@PathVariable Long id) {
        return specialiteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spécialité introuvable"));
    }
}
