/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Rol;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	public String index(ModelMap modelo)
	{
		return "index.html";

	}
	
	@PreAuthorize("hasAnyRole('ROLE_PACIENTE','ROLE_ADMIN','ROLE_PROFESIONAL')")
	@GetMapping("/inicio")
	public String inicio(HttpSession session)
	{
		Usuario logeado = (Usuario) session.getAttribute("usuarioSession");
		if (logeado.getRol() == Rol.ADMIN)
		{
			return "redirect:/admin/dashboard";
		}
		return "inicio.html";

	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, ModelMap modelo)
	{
		if (error != null)
		{
			modelo.put("error", "Usuario o contraseña invalidos!");
		}
		return "login.html";
	}
	
	@GetMapping("/registro")
	public String registro(ModelMap modelo)
	{
		return "registro.html";
	}
	
	@PostMapping("/registro")
	public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono,
			@RequestParam String email, @RequestParam String password, @RequestParam String password2,
			@RequestParam MultipartFile archivo, ModelMap modelo)
	{
		try
		{
			usuarioServicio.registrar(nombre,apellido,telefono,email,password,password2,archivo);
			modelo.put("exito", "Usuario registrado correctamente");
			return "index.html";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("telefono", telefono);
			modelo.put("email", email);
			modelo.put("password",password);
			modelo.put("password2",password2);
			return "usuario_registro.html";
		}

	}
}
