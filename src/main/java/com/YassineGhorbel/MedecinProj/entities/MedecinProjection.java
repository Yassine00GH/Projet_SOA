package com.YassineGhorbel.MedecinProj.entities;


import org.springframework.data.rest.core.config.Projection;

@Projection(name = "nom", types = {Medecin.class})
public interface MedecinProjection {
    public String getNom();
}
