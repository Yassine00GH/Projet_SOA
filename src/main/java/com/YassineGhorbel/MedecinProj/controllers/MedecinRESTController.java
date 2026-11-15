package com.YassineGhorbel.MedecinProj.controllers;

import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.services.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class MedecinRESTController {

    private final MedecinService medecinService;

    @RequestMapping(path = {"", "/all"}, method = RequestMethod.GET)
    public List<Medecin> getAllMedecin() {
        return medecinService.getAllMedecin();
    }

    @RequestMapping(value = {"/{id}", "/getbyid/{id}"}, method = RequestMethod.GET)
    public Medecin getMedecinById(@PathVariable("id") Long id) {
        return medecinService.getMedecin(id);
    }

    @RequestMapping(path = {"", "/addmed", "/addprod"}, method = RequestMethod.POST)
    public Medecin createMedecin(@RequestBody Medecin medecin) {
        return medecinService.saveMedecin(medecin);
    }

    @RequestMapping(path = {"", "/updatemed", "/updateprod"}, method = RequestMethod.PUT)
    public Medecin updateMedecin(@RequestBody Medecin medecin) {
        return medecinService.updateMedecin(medecin);
    }

    @RequestMapping(value = {"/{id}", "/updatemed/{id}", "/updateprod/{id}"}, method = RequestMethod.PUT)
    public Medecin updateMedecin(@PathVariable("id") Long id, @RequestBody Medecin medecin) {
        medecin.setId(id);
        return medecinService.updateMedecin(medecin);
    }

    @RequestMapping(value = {"/{id}", "/delmed/{id}", "/delprod/{id}"}, method = RequestMethod.DELETE)
    public void deleteMedecin(@PathVariable("id") Long id) {
        medecinService.deleteMedecinById(id);
    }

    @RequestMapping(value = "/medsByName/{nom}", method = RequestMethod.GET)
    public List<Medecin> findByNomContains(@PathVariable("nom") String nom) {
        return medecinService.findByNomContains(nom);
    }

    @RequestMapping(value = "/MedecinSpec/{id}", method = RequestMethod.GET)
    public List<Medecin> getMedecinSpecById(@PathVariable("id") Long id) {
        return medecinService.findBySpecialiteId(id);
    }
}
