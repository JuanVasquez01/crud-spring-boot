package com.coffee_glp.controller;

import com.coffee_glp.model.entities.Clientes;
import com.coffee_glp.model.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping
    public List<Clientes> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public Clientes obtenerPorId(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @PostMapping
    public void crear(@RequestBody Clientes cliente) {
        clienteService.crear(cliente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Clientes cliente = clienteService.findById(id);
        if (cliente != null) {
            clienteService.delete(cliente);
        }
    }
}