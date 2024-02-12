/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Curriculum;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.CurriculumRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nico
 */
@Service
public class CurriculumServicio
{
	@Autowired
	private CurriculumRepositorio curriculumRepositorio;
	
	public Curriculum guardar(MultipartFile archivo) throws MiException
	{
		if(archivo != null)
		{
			try
			{
				Curriculum curriculum = new Curriculum();
				curriculum.setMime(archivo.getContentType());
				curriculum.setNombre(archivo.getName());
				curriculum.setContenido(archivo.getBytes());
				return curriculumRepositorio.save(curriculum);
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		return null;
	}
	public Curriculum actualizar(MultipartFile archivo, String idCurriculum) throws MiException
	{
		if(archivo != null)
		{
			try
			{
				Curriculum curriculum = new Curriculum();
				if(idCurriculum != null || idCurriculum.isEmpty())
				{
					Optional<Curriculum> respuesta = curriculumRepositorio.findById(idCurriculum);
					if(respuesta.isPresent())
					{
						curriculum = respuesta.get();
					}
				}
				curriculum.setMime(archivo.getContentType());
				curriculum.setNombre(archivo.getName());
				curriculum.setContenido(archivo.getBytes());
				return curriculumRepositorio.save(curriculum);
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		return null;
	}
}
