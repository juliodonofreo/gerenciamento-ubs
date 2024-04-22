package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Dependent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DependentRepository extends JpaRepository<Dependent, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_dependent d WHERE d.companion_id  = :companion_id")
    public List<Dependent> findByCompanion(Long companion_id);
}
