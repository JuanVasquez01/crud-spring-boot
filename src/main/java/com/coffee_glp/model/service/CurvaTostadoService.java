package com.coffee_glp.model.service;

import com.coffee_glp.model.dao.ICurvaTostadoDao;
import com.coffee_glp.model.entities.CurvaTostado;
import org.springframework.stereotype.Service;

@Service
public class CurvaTostadoService {

    private final ICurvaTostadoDao curvaTostadoDao;

    public CurvaTostadoService(ICurvaTostadoDao curvaTostadoDao) {
        this.curvaTostadoDao = curvaTostadoDao;
    }

    public void guardarCurva(CurvaTostado curvaTostado) {
        curvaTostadoDao.save(curvaTostado);
    }
}