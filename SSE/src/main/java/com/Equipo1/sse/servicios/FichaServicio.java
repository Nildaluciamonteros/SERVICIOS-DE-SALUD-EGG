/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Ficha;
import com.Equipo1.sse.entidades.ObraSocial;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.FichaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aldan
 */
@Service
public class FichaServicio {

    @Autowired
    private FichaRepositorio fichaRepositorio;

    @Transactional
    public Ficha crearFicha(String diagnostico, ObraSocial obraSocial, Profesional profesional) throws MiException {
        Ficha ficha = new Ficha();

        validar(diagnostico, profesional);
        ficha.setFecha(new Date());
        ficha.setDiagnostico(diagnostico);
        ficha.setObraSocial(obraSocial);
        ficha.setProfesional(profesional);

        fichaRepositorio.save(ficha);
        return ficha;
    }

    public List<Ficha> listarFichas() {
        List<Ficha> fichas = new ArrayList();

        fichas = fichaRepositorio.findAll();

        return fichas;
    }

    public List<Ficha> buscarPorFecha(Date fecha) {
        return fichaRepositorio.buscarPorFecha(fecha);
    }

    public void modificarFicha(String id, Date fecha, ObraSocial obraSocial, String diagnostico, Profesional profesional) throws MiException {
        validar(diagnostico, profesional);
        Optional<Ficha> respuesta = fichaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Ficha ficha = respuesta.get();

            ficha.setDiagnostico(diagnostico);
            ficha.setObraSocial(obraSocial);

            fichaRepositorio.save(ficha);
        }
    }

    public Ficha getOne(String id) {
        return fichaRepositorio.getOne(id);
    }

    private void validar(String diagnostico, Profesional profesional) throws MiException {

        if (profesional == null) {
            throw new MiException("El profesional no puede ser nulo");
        }
        if (diagnostico == null || diagnostico.isEmpty()) {
            throw new MiException("El diagnostico no puede ser nulo");
        }

    }
}
