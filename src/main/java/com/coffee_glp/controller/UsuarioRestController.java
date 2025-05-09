package com.coffee_glp.controller;

import com.coffee_glp.model.entities.Usuario;
import com.coffee_glp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("USER"); // Valor predeterminado
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().isEmpty()) {
            usuario.setCorreo("default@correo.com"); // Valor predeterminado
        }
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre de usuario no puede estar vacío.");
        }

        Optional<Usuario> usuario = Optional.ofNullable(usuarioRepository.findByUsername(username));
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }
    }
}