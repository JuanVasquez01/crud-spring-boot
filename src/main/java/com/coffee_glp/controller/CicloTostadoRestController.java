package com.coffee_glp.controller;

import com.coffee_glp.model.dao.ICicloTostadoDao;
import com.coffee_glp.model.entities.CicloTostado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciclos")
public class CicloTostadoRestController {

    @Autowired
    private ICicloTostadoDao cicloTostadoDao;

    @PostMapping
    public void registrarCiclo(@RequestBody CicloTostado cicloTostado) {
        cicloTostadoDao.save(cicloTostado);
    }

    @GetMapping
    public List<CicloTostado> listarCiclos() {
        return cicloTostadoDao.findAll();
    }
}