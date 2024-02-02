/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Imagen;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nico
 */
@Service
public class ImagenServicio
{
	@Autowired
	private ImagenRepositorio imagenRepositorio;
	
	public Imagen guardar(MultipartFile archivo) throws MiException
	{
		if(archivo != null)
		{
			try
			{
				Imagen imagen = new Imagen();
				imagen.setMime(archivo.getContentType());
				imagen.setNombre(archivo.getName());
				imagen.setContenido(archivo.getBytes());
				return imagenRepositorio.save(imagen);
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		return null;
	}
	public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException
	{
		if(archivo != null)
		{
			try
			{
				Imagen imagen = new Imagen();
				if(idImagen != null || idImagen.isEmpty())
				{
					Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
					if(respuesta.isPresent())
					{
						imagen = respuesta.get();
					}
				}
				imagen.setMime(archivo.getContentType());
				imagen.setNombre(archivo.getName());
				imagen.setContenido(archivo.getBytes());
				return imagenRepositorio.save(imagen);
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		return null;
	}
}