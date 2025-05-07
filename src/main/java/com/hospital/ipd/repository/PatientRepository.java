package com.hospital.ipd.repository;

import com.hospital.ipd.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Patient findByEmail(String email);

}
