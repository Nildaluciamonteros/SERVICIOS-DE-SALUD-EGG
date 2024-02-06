/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.ObraSocial;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.enumeraciones.Rol;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ObraSocialServicio;
import com.Equipo1.sse.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@Autowired
	private ObraSocialServicio obraSocialServicio;

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
                if (logeado.getRol() == Rol.PROFESIONAL)
		{
			return "redirect:/profesional/";
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
			@RequestParam String email, @RequestParam String obraSocial, @RequestParam String numAfiliado, @RequestParam String password, @RequestParam String password2,
			@RequestParam MultipartFile archivo, ModelMap modelo)
	{
		try
		{
			usuarioServicio.registrar(nombre, apellido, telefono, email, obraSocial, numAfiliado,
					password, password2, archivo);
			modelo.put("exito", "Usuario registrado correctamente");
			return "inicio.html";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("telefono", telefono);
			modelo.put("email", email);
			modelo.put("password", password);
			modelo.put("password2", password2);
			return "registro.html";
		}
	}

	@GetMapping("/especialidades")
	public String especialidades(ModelMap modelo)
	{
		List<String> especialidades = new ArrayList();
		for (Especialidades e : Especialidades.values())
		{
			especialidades.add(e.name());
		}
		modelo.put("especialidades", especialidades);
		return "especialidades.html";
	}

	@GetMapping("/especialidades/{especialidad}")
	public String especialidad(@PathVariable String especialidad, ModelMap modelo)
	{
		List<Profesional> profesionales = usuarioServicio.listarProfesionalesPorEspecialidad(especialidad);
		modelo.put("profesionales", profesionales);
		return "especialidad.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_PACIENTE','ROLE_ADMIN','ROLE_PROFESIONAL')")
	@GetMapping("/perfil")
	public String perfil(ModelMap modelo, HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
		modelo.put("usuario", usuario);
		return "perfil_modificar.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_PACIENTE','ROLE_ADMIN','ROLE_PROFESIONAL')")
	@PostMapping("/perfil/{id}")
	public String actualizar(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String telefono, @RequestParam String email, @RequestParam String obraSocial,
			@RequestParam String numAfiliado,
			@RequestParam String password, @RequestParam String password2, @RequestParam MultipartFile archivo, HttpSession session, ModelMap modelo, Authentication authentication)
	{
		try
		{
			Usuario usuarioSession = (Usuario) session.getAttribute("usuarioSession");
			Usuario editado = usuarioServicio.getOne(id);
			if (!editado.equals(usuarioSession))
			{
				usuarioServicio.actualizar(id, nombre, apellido, telefono, email, obraSocial, numAfiliado,
						password, password2, archivo);
			}
			modelo.put("exito", "Usuario actualizado correctamente");
			return "inicio.html";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			modelo.put("usuario", usuario);

			return "perfil_modificar.html";
		}
	}
}
