package com.coffee_glp.repository;

import com.coffee_glp.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    Usuario findByCorreo(String correo);
    Usuario findByResetToken(String resetToken);
}
