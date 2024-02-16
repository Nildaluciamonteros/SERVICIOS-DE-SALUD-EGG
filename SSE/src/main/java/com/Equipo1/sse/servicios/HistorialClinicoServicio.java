/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Ficha;
import com.Equipo1.sse.entidades.HistorialClinico;
import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.repositorios.HistorialClinicoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aldan
 */
@Service
public class HistorialClinicoServicio {

    @Autowired
    private HistorialClinicoRepositorio historialClinicoRepositorio;

    public void crearHistorial(Paciente paciente) {
        HistorialClinico historial = new HistorialClinico();

        historial.setPaciente(paciente);
        historial.setFichas(new ArrayList());
        historialClinicoRepositorio.save(historial);

    }

    public void agregarFicha(Ficha ficha, String id) {
        if (ficha != null) {

            Optional<HistorialClinico> respuesta = historialClinicoRepositorio.findById(id);

            if (respuesta.isPresent()) {
                HistorialClinico historial = respuesta.get();
                List<Ficha> fichas = historial.getFichas();
                fichas.add(ficha);
                historial.setFichas(fichas);
                historialClinicoRepositorio.save(historial);
            }

        }
    }

    public HistorialClinico buscarHistorialPorPaciente(Paciente paciente) {
        return historialClinicoRepositorio.buscarPorPaciente(paciente);
    }

    public List<Ficha> leerHistorial(HistorialClinico historial) {
        return historial.getFichas();
    }

    public HistorialClinico getOne(String id) {
        return historialClinicoRepositorio.getOne(id);
    }
    public List<HistorialClinico> listarHistorial(){
        return historialClinicoRepositorio.findAll();
    }
}
