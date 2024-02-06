/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.Turno;
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
public interface TurnoRepositorio extends JpaRepository<Turno, String> {
    
    @Query("SELECT t FROM Turno t WHERE t.paciente.numAfiliado = :numAfiliado")
	public List<Turno> buscarPorNumeroAfiliado(@Param("numAfiliado") String numAfiliado);
}
