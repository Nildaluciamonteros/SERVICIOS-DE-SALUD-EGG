/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Hora;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.HoraRepositorio;
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
public class HoraServicio
{
	@Autowired
	HoraRepositorio horaRepositorio;
	
	@Transactional
	public Hora registrar(Integer horas, Integer minutos) throws MiException
	{
		validar(horas,minutos);
		Hora hora = new Hora();
		hora.setHoras(horas);
		hora.setMinutos(minutos);
		horaRepositorio.save(hora);
		return hora;
	}
	
	@Transactional
	public void actualizar(String id, Integer horas, Integer minutos) throws MiException
	{
		validar(horas,minutos);
		Optional<Hora> respuesta = horaRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Hora hora = respuesta.get();
			hora.setHoras(horas);
			hora.setMinutos(minutos);
			horaRepositorio.save(hora);
		}
	}
	
	public void quitar(String id)
	{
		Optional<Hora> respuesta = horaRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Hora hora = respuesta.get();
			horaRepositorio.delete(hora);
		}
	}

	private void validar(Integer horas, Integer minutos) throws MiException
	{
		if(horas == null || horas < 0 || horas > 23)
		{
			throw new MiException("Las horas de no pueden estar fuera del rango de 0-23");
		}
		if(minutos == null || minutos < 0 || minutos > 59)
		{
			throw new MiException("Los minutos no pueden estar fuera del rango de 0-59");
		}
	}
	
	public Hora getOne(String id)
	{
		return horaRepositorio.getOne(id);
	}
	
	public List<Hora> listar()
	{
		return horaRepositorio.findAll();
	}
}
