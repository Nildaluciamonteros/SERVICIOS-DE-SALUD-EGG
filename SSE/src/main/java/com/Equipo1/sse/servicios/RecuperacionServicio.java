/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Recuperacion;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.RecuperacionRepositorio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Nico
 */
@Service
public class RecuperacionServicio
{
	@Autowired
	private RecuperacionRepositorio recuperacionRepositorio;
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	public Recuperacion registrar(String idUsuario) throws MiException
	{
		validar(idUsuario);
		Recuperacion recu = new Recuperacion();
		Date limite = new Date();
		limite.setTime(limite.getTime() + (1000 * 60 * 60));
		recu.setFechaLimite(limite);
		recu.setUsuarioId(idUsuario);
		recuperacionRepositorio.save(recu);
		return recu;
	}
	
	public Recuperacion getOne(String id)
	{
		return recuperacionRepositorio.getOne(id);
	}

	private void validar(String idUsuario) throws MiException
	{
		// Si el id es nulo o vacio
		if(idUsuario == null || idUsuario.isEmpty())
		{
			throw new MiException("El id del usuario no puede ser nulo o estar vacio.");
		}
		// Busco el usuario
		Usuario usu = usuarioServicio.getOne(idUsuario);
		// Si no se encontró significa que no existe en la base de datos
		if(usu == null)
		{
			throw new MiException("El usuario no existe.");
		}
		// Si está dado de baja
		if(usu.getActivado() == null || !usu.getActivado())
		{
			throw new MiException("El usuario está dado de baja.");
		}
		// Obtengo el campo recuperacionPendiente del usuario
		Recuperacion pend = usu.getRecuperacionPendiente();
		// Si no es nula, quiere decir que hay una recuperacion pendiente registrada
		if(pend != null)
		{
			// Obtengo la fechaLimite
			Date limite = pend.getFechaLimite();
			// Si la fecha limite está despues de ahora
			if(limite.after(new Date()))
			{
				throw new MiException("Ya una recuperación pendiente hasta las " + limite.getHours() + ":" + limite.getMinutes());
			}
			else
			{
				// Doy de baja la recuperación
				recuperacionRepositorio.delete(pend);
				usuarioServicio.bajaRecuperacion(usu.getId());
			}
		}
	}
}
