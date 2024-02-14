/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.entidades;
import com.Equipo1.sse.enumeraciones.Rol;
import javax.persistence.Entity;

/**
 *
 * @author Nico
 */
@Entity
public class Administrador extends Usuario
{
	public Administrador()
	{
		super();
		setRol(Rol.ADMIN);
	}
}
