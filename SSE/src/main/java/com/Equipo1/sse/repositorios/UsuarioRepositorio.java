/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Nico
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,String>
{
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
	
	@Query("SELECT u FROM Usuario u WHERE u.especialidad = LOWER(:#{#especialidad})")
	public List<Profesional> buscarPorEspecialidad(@Param("especialidad") String especialidad);
	
	@Query("SELECT u FROM Usuario u WHERE u.rol = 'profesional'")
	public List<Profesional> buscarTodosLosProfesionales();
	
	@Query("SELECT u FROM Usuario u WHERE u.nombre LIKE :nombre")
	public List<Usuario> buscarPorNombre(@Param("nombre") String nombre);
	
	
}
