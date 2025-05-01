package com.coffee_glp.model.dao;

import com.coffee_glp.model.entities.CicloTostado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICicloTostadoDao extends JpaRepository<CicloTostado, Long> {
}