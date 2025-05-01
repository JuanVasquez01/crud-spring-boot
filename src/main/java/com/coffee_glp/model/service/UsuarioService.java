package com.coffee_glp.model.service;

import com.coffee_glp.model.dao.IUsuarioDao;
import com.coffee_glp.model.entities.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final IUsuarioDao usuarioDao;

    public UsuarioService(IUsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Cacheable("usuarios")
    public Usuario findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }
}