package com.example.helmes_challenge.benefit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParentalBenefitRepository extends JpaRepository<ParentalBenefit, UUID> {
}
