package com.coffee_glp.model.dao;

import com.coffee_glp.model.entities.Clientes;
import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends CrudRepository<Clientes, Long> {
}