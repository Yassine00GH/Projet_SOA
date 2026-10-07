package com.YassineGhorbel.MedecinProj.repositories;

import com.YassineGhorbel.MedecinProj.entities.Specialite;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource(path = "specialites")
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    @Override
    @EntityGraph(attributePaths = "medecins")
    List<Specialite> findAll();

    @Override
    @EntityGraph(attributePaths = "medecins")
    Optional<Specialite> findById(Long id);
}
