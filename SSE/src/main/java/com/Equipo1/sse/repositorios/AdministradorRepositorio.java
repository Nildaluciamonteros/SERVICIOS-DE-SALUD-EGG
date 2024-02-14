/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Nico
 */
@Repository
public interface AdministradorRepositorio extends JpaRepository<Administrador,String>
{
	@Query("SELECT a FROM Administrador a WHERE a.email = :email")
    public Administrador buscarPorEmail(@Param("email") String email);
}
