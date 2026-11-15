package com.YassineGhorbel.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.YassineGhorbel.users.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}

