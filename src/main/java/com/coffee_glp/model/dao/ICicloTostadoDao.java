package com.coffee_glp.model.dao;

import com.coffee_glp.model.entities.CicloTostado;
import com.coffee_glp.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICicloTostadoDao extends JpaRepository<CicloTostado, Long> {
    List<CicloTostado> findByCliente(Usuario cliente);
}
