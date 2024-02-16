/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.HistorialClinico;
import com.Equipo1.sse.entidades.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aldan
 */
@Repository
public interface HistorialClinicoRepositorio extends JpaRepository<HistorialClinico, String> {
    
    @Query("SELECT h FROM HistorialClinico h WHERE h.paciente = :paciente")
    public HistorialClinico buscarPorPaciente(@Param("paciente") Paciente paciente);
}
