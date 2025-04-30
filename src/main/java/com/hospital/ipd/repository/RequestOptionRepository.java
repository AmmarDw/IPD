package com.hospital.ipd.repository;

import com.hospital.ipd.model.RequestOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOptionRepository extends JpaRepository<RequestOption,Integer> {
}
