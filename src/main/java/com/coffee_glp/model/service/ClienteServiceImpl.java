package com.coffee_glp.model.service;

import com.coffee_glp.model.dao.IClienteDao;
import com.coffee_glp.model.entities.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    public List<Clientes> listar() {
        return (List<Clientes>) clienteDao.findAll();
    }

    @Override
    public Clientes findById(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    public void crear(Clientes cliente) {
        clienteDao.save(cliente);
    }

    @Override
    public void delete(Clientes cliente) {
        clienteDao.delete(cliente);
    }
}