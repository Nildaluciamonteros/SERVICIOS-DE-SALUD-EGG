/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.servicios.TurnoServicio;
import com.Equipo1.sse.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Nico
 */
@Controller
@RequestMapping("/profesional")
public class ProfesionalControlador
{
	@Autowired
	private TurnoServicio turnoServicio;
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@GetMapping("/")
	public String profesional()
	{
		return "profesional.html";
	}
}
