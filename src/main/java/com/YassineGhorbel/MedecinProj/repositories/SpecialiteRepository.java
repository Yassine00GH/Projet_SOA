package com.YassineGhorbel.MedecinProj.repositories;

import com.YassineGhorbel.MedecinProj.entities.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@RepositoryRestResource(path = "specialites")
@CrossOrigin(origins = "http://localhost:4200")
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
}
