package com.coffee_glp.model.dao;

import com.coffee_glp.model.entities.CurvaTostado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICurvaTostadoDao extends JpaRepository<CurvaTostado, Long> {
    List<CurvaTostado> findByCicloId(Long cicloId);

    @Query("SELECT c FROM CurvaTostado c WHERE c.cicloId = ?1 ORDER BY c.timestamp DESC")
    List<CurvaTostado> findByCicloIdOrderByTimestampDesc(Long cicloId);
}
