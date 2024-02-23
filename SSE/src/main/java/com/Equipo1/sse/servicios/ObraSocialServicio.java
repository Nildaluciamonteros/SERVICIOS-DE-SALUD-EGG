/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.ObraSocial;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.ObraSocialRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ALEXIS.R.L
 */
@Service
public class ObraSocialServicio
{

	@Autowired
	private ObraSocialRepositorio obraSocialRepositorio;

	@Transactional
	public void registrar(String nombre) throws MiException
	{
		validar(nombre);
		ObraSocial obraSocial = new ObraSocial();
		obraSocial.setNombre(nombre);
		obraSocial.setActivado(true);
		obraSocialRepositorio.save(obraSocial);
	}

	@Transactional
	public void actualizar(String idOS, String nombre) throws MiException
	{
		validar(nombre);
		Optional<ObraSocial> respuesta = obraSocialRepositorio.findById(idOS);
		if (respuesta.isPresent())
		{
			ObraSocial obraSocial = respuesta.get();
			obraSocial.setNombre(nombre);
			obraSocial.setActivado(true);
			obraSocialRepositorio.save(obraSocial);
		}
	}

	public List<ObraSocial> listarObraSociales()
	{
		return obraSocialRepositorio.findAll();
	}

	public ObraSocial buscarObraSocial(String nombre)
	{
		return obraSocialRepositorio.buscarPorNombre(nombre);
	}
	
	public void darBaja(String idOS) throws MiException
	{
		Optional<ObraSocial> respuesta = obraSocialRepositorio.findById(idOS);
		if (respuesta.isPresent())
		{
			ObraSocial obraSocial = respuesta.get();
			if(obraSocial.getActivado() != null)
			{
				obraSocial.setActivado(!obraSocial.getActivado());
			}
			obraSocialRepositorio.save(obraSocial);
		}
		else
		{
			throw new MiException("No se encontró la obra social");
		}
	}

	private void validar(String nombre) throws MiException
	{
		if (nombre == null || nombre.isEmpty())
		{
			throw new MiException("El nombre no puede ser nulo o estar vacio");
		}
	}

	public ObraSocial getOne(String id)
	{
		return obraSocialRepositorio.getOne(id);
	}

	public void eliminarObraSocial(String idOS)
	{
		Optional<ObraSocial> respuesta = obraSocialRepositorio.findById(idOS);
		if (respuesta.isPresent())
		{
			ObraSocial obraSocial = respuesta.get();
			obraSocialRepositorio.delete(obraSocial);
		}
	}
}
