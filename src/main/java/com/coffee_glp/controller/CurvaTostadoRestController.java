package com.coffee_glp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/curvas")
public class CurvaTostadoRestController {

    @PostMapping
    public String crearCurva() {
        return "Curva de tostado creada";
    }
}