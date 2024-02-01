/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Nico
 */
@Controller
@RequestMapping("/")
public class PortalControlador
{
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@GetMapping("/")
	public String inicio(ModelMap modelo)
	{
		return "inicio.html";
	}
	
	@GetMapping("/login")
	public String login(ModelMap modelo)
	{
		return "login.html";
	}
	
	@GetMapping("/registro")
	public String registro(ModelMap modelo)
	{
		return "registro.html";
	}
}
