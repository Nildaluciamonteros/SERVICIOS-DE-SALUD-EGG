/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.ObraSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ALEXIS.R.L
 */
@Repository
public interface ObraSocialRepositorio extends JpaRepository<ObraSocial, String>{
    @Query("SELECT o FROM ObraSocial o WHERE o.nombre = :nombre")
    public ObraSocial buscarPorNombre(@Param("nombre") String nombre);
    
}
