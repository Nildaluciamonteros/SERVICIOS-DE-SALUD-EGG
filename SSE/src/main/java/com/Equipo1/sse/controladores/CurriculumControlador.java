/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Rol;
import com.Equipo1.sse.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Nico
 */
@Controller
@RequestMapping("/curriculum")
public class CurriculumControlador
{
	@Autowired
	UsuarioServicio usuarioServicio;
	
	@GetMapping("/cv/{id}")
	public ResponseEntity<byte[]> curriculumProfesional(@PathVariable String id)
	{
		Usuario usuario = usuarioServicio.getOne(id);
		if(usuario instanceof Profesional && usuario.getRol() == Rol.PROFESIONAL)
		{
			Profesional profesional = (Profesional) usuario;

			byte[] pdf = profesional.getCurriculum().getContenido();
			if(pdf != null)
			{
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);

				return new ResponseEntity<>(pdf,headers,HttpStatus.OK);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
}
