/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.Ficha;
import java.util.Date;
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
public interface FichaRepositorio extends JpaRepository<Ficha, String>{
    
     @Query("SELECT f FROM Ficha f WHERE f.fecha = :fecha")
	public List<Ficha> buscarPorFecha(@Param("fecha") Date fecha);
}
