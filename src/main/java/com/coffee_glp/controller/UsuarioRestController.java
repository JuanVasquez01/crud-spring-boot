package com.coffee_glp.controller;

import com.coffee_glp.model.dao.IUsuarioDao;
import com.coffee_glp.model.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @Autowired
    private IUsuarioDao usuarioDao;

    @PostMapping
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña no puede estar vacía");
        }
        usuarioDao.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}