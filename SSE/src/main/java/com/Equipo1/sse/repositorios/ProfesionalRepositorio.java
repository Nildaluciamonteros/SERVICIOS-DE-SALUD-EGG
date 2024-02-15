
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.Profesional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aldan
 */
@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional, String> {
    
    @Query("SELECT p FROM Usuario p WHERE p.email = :email")
    public Profesional buscarPorEmail(@Param("email") String email);

    @Query("SELECT p FROM Usuario p WHERE p.especialidad = LOWER(:#{#especialidad})")
    public List<Profesional> buscarPorEspecialidad(@Param("especialidad") String especialidad);
    
    @Query("SELECT p FROM Usuario p WHERE p.reputacion = :reputacion")
	public List<Profesional> buscarProfesionalPorReputacion(@Param("reputacion") Integer reputacion);
}

