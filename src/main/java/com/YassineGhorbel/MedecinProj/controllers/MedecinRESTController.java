package com.YassineGhorbel.MedecinProj.controllers;


import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.services.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@AllArgsConstructor
public class MedecinRESTController {

    private final MedecinService medecinService ;


    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping()
    List<Medecin> getAllMedecin(){
        return medecinService.getAllMedecin();
    }
    //@RequestMapping(value ="/{id}",method = RequestMethod.GET)
    @GetMapping("/{id}")
    public Medecin getMedecinById(@PathVariable Long id){
        return medecinService.getMedecin(id);
    }

    //@RequestMapping(method = RequestMethod.POST)
    @PostMapping()
    public Medecin createMedecin(@RequestBody Medecin medecin){
        return  medecinService.saveMedecin(medecin);
    }

    //@RequestMapping(method = RequestMethod.PUT)
    @PutMapping()
    public Medecin updateMedecin(@RequestBody Medecin medecin){
        return medecinService.updateMedecin(medecin);
    }

    //@RequestMapping(value ="/{id}",method = RequestMethod.DELETE)
    @DeleteMapping(value ="/{id}")
    public void deleteMedecin(@PathVariable("id") Long id){
        medecinService.deleteMedecinById(id);
    }

    //@RequestMapping(value ="/api/{id}",method = RequestMethod.GET)
    @GetMapping("/MedecinSpec/{id}")
    public List<Medecin> getMedecinSpecById(@PathVariable("id") Long id)
    {
        return medecinService.findBySpecialiteId(id);
    }


}
