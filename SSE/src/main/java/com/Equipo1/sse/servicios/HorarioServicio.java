/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Horario;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.HorarioRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Nico
 */
@Service
public class HorarioServicio
{

	@Autowired
	private HorarioRepositorio horarioRepositorio;

	@Transactional
	public Horario registrar(Integer horasD, Integer minutosD, Integer horasH, Integer minutosH, Integer diaI, Integer diaF) throws MiException
	{
		validar(horasD, minutosD, horasH, minutosH, diaI);
		Horario horario = new Horario();
		horario.setHorasDesde(horasD);
		horario.setMinutosDesde(minutosD);
		horario.setHorasHasta(horasH);
		horario.setMinutosHasta(minutosH);
		Boolean[] dias = new Boolean[7];
		if (diaF == null)
		{
			for (int i = 0; i < 7; i++)
			{
				dias[i] = (i == diaI);
			}
		} else
		{
			if (diaF < 1 || diaF > 7)
			{
				throw new MiException("El día de fin no puede estar fuera del rango 1-7");
			}
			for (int i = diaI - 1; i < diaF; i++)
			{
				dias[i] = true;
			}
		}
		horario.setDias(dias);
		horarioRepositorio.save(horario);
		return horario;
	}

	@Transactional
	public void actualizar(String id, Integer horasD, Integer minutosD, Integer horasH, Integer minutosH, Integer diaI, Integer diaF) throws MiException
	{
		validar(horasD, minutosD, horasH, minutosH, diaI);
		Optional<Horario> respuesta = horarioRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Horario horario = (Horario) respuesta.get();
			horario.setHorasDesde(horasD);
			horario.setMinutosDesde(minutosD);
			horario.setHorasHasta(horasH);
			horario.setMinutosHasta(minutosH);
			Boolean[] dias = new Boolean[7];
			if (diaF == null)
			{
				for (int i = 0; i < 7; i++)
				{
					dias[i] = (i == diaI);
				}
			} else
			{
				if (diaF < 1 || diaF > 7)
				{
					throw new MiException("El día de fin no puede estar fuera del rango 1-7");
				}
				for (int i = diaI - 1; i < diaF; i++)
				{
					dias[i] = true;
				}
			}
			horario.setDias(dias);
			horarioRepositorio.save(horario);
		}
	}

	public List<Horario> listar()
	{
		return horarioRepositorio.findAll();
	}

	public Horario getOne(String id)
	{
		return horarioRepositorio.getOne(id);
	}

	public void quitar(String id) throws MiException
	{
		Optional<Horario> respuesta = horarioRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Horario horario = (Horario) respuesta.get();
			horarioRepositorio.delete(horario);
		}
		else
		{
			throw new MiException("No se encontró el horario");
		}
	}

	public void validar(Integer horasD, Integer minutosD, Integer horasH, Integer minutosH, Integer diaI) throws MiException
	{
		if (horasD == null || horasD < 0 || horasD > 24)
		{
			throw new MiException("Las horas no pueden ser nulas o estar fuera del rango 0-23");
		}

		if (minutosD == null || minutosD < 0 || minutosD > 59)
		{
			throw new MiException("Los minutos no pueden ser nulos o estar fuera del rango 0-59");
		}

		if (horasH == null || horasH < 0 || horasH > 24)
		{
			throw new MiException("Las horas no pueden ser nulas o estar fuera del rango 0-23");
		}
		if (minutosH == null || minutosH < 0 || minutosH > 59)
		{
			throw new MiException("Los minutos no pueden ser nulos o estar fuera del rango 0-59");
		}
		if (diaI == null || diaI < 1 || diaI > 7)
		{
			throw new MiException("El día de inicio/unico no puede ser nulo, o estar fuera del rango 1-7");
		}
	}
}
