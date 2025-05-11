package com.coffee_glp.model.dao;

import com.coffee_glp.model.entities.CicloTostado;
import com.coffee_glp.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICicloTostadoDao extends JpaRepository<CicloTostado, Long> {
    List<CicloTostado> findByCliente(Usuario cliente);

    @Query("SELECT c.status FROM CicloTostado c WHERE c.id = ?1")
    CicloTostado.CicloStatus findStatusById(Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CicloTostado c WHERE c.id = ?1")
    boolean existsById(Long id);
}
