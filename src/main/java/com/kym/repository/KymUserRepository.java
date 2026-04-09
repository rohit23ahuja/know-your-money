package com.kym.repository;

import com.kym.entity.KymUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KymUserRepository extends JpaRepository<KymUser, Long> {
    Optional<KymUser> findByUsername(String username);
}
