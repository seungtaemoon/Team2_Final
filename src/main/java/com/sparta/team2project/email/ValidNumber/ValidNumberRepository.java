package com.sparta.team2project.email.ValidNumber;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidNumberRepository extends JpaRepository<ValidNumber, Long> {
    Optional<ValidNumber> findByEmail(String email);
}
