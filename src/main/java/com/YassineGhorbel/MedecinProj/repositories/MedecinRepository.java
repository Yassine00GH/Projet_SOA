package com.YassineGhorbel.MedecinProj.repositories;


import com.YassineGhorbel.MedecinProj.entities.Medecin;
import com.YassineGhorbel.MedecinProj.entities.Specialite;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RepositoryRestResource(path = "rest")
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    List<Medecin> findByNom(String nom);
    List<Medecin> findByNomContains(String nom);
    @Query("select m from Medecin m where m.nom like concat('%', ?1) and m.prenom > ?2")
    List<Medecin> findByNomPrenom(String nom, String prenom);

    @Query("select m from Medecin m where m.specialite = ?1")
    List<Medecin> findBySpecialite(Specialite specialite);

    List<Medecin> findBySpecialiteId(Long id);

    List<Medecin> findByOrderByNomAsc();

    @Query("select m from Medecin m order by m.nom asc, m.prenom desc")
    List<Medecin> trierMedecinsNomsPrenoms();
}
