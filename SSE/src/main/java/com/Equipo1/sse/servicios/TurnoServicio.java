/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.entidades.Turno;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.TurnoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aldan
 */
@Service
public class TurnoServicio
{

	@Autowired
	TurnoRepositorio turnoRepositorio;

	@Transactional
	public void crearTurno(Date fecha, Paciente paciente) throws MiException
	{
		Turno turno = new Turno();
		
		validar(fecha, paciente);
		turno.setFecha(fecha);
		turno.setPaciente(paciente);

		turnoRepositorio.save(turno);
	}

	public List<Turno> listarTurnos()
	{
		List<Turno> turnos = new ArrayList();

		turnos = turnoRepositorio.findAll();

		return turnos;
	}

	public void modificarEditorial(String id, Date fecha, Paciente paciente) throws MiException
	{
		validar(fecha, paciente);
		Optional<Turno> respuesta = turnoRepositorio.findById(id);

		if (respuesta.isPresent())
		{
			Turno turno = respuesta.get();

			turno.setFecha(fecha);
			turno.setPaciente(paciente);

			turnoRepositorio.save(turno);
		}
	}

	public Turno getOne(String id)
	{
		return turnoRepositorio.getOne(id);
	}

	private void validar(Date fecha, Paciente paciente) throws MiException
	{
		if (fecha == null)
		{
			throw new MiException("La fecha no puede ser nula");
		}
		if (paciente == null)
		{
			throw new MiException("El paciente no puede ser nulo");
		}
	}
}
