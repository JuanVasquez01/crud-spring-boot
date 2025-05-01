package com.coffee_glp.model.service;

import com.coffee_glp.model.entities.Clientes;

import java.util.List;

public interface IClienteService {
    List<Clientes> listar();
    Clientes findById(Long id);
    void crear(Clientes cliente);
    void delete(Clientes cliente);
}