/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Paciente;
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
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminControlador
{

	@Autowired
	private UsuarioServicio usuarioServicio;

	@Autowired
	private ObraSocialServicio obraSocialServicio;

	@GetMapping("/dashboard")
	public String index(ModelMap modelo)
	{
		return "panelAdmin.html";
	}

	@GetMapping("/usuarios")
	public String listar(ModelMap modelo)
	{
		List<Usuario> usuarios = usuarioServicio.listarUsuarios();
		List<String> roles = new ArrayList();
		for (Rol e : Rol.values())
		{
			roles.add(e.name());
		}
		modelo.addAttribute("usuarios", usuarios);
		modelo.addAttribute("roles", roles);

		return "usuario_lista.html";
	}

	@PostMapping("/usuarios/buscar")
	public String buscarUsuario(ModelMap modelo, @RequestParam String nombre)
	{
		List<Usuario> usuarios = usuarioServicio.buscarPorNombre(nombre);
		if (usuarios == null || usuarios.size() > 0)
		{
			modelo.addAttribute("usuarios", usuarios);
		} else
		{
			modelo.put("error", "No se encuentra ningun usuario con ese nombre.");
			usuarios = usuarioServicio.listarUsuarios();
			modelo.addAttribute("usuarios", usuarios);
		}

		return "usuario_buscar.html";
	}
        
	@GetMapping("/usuarios/{id}/darBaja")
	public String darBajaUsuario(@PathVariable String id, ModelMap modelo)
	{
		Usuario usuario = usuarioServicio.getOne(id);
		if (usuario == null)
		{
			modelo.put("error", "El usuario no se encuentra");
		} else
		{
			usuarioServicio.darBajaUsuario(id);
			modelo.put("exito", "El usuario se eliminó");
		}
		return "redirect:/admin/usuarios";
	}
          
	

	@GetMapping("/usuarios/{id}/modificar")
	public String modificarUsuario(@PathVariable String id, ModelMap modelo)
	{

		Usuario usuario = (Usuario) usuarioServicio.getOne(id);
		Paciente paciente = null;
		Profesional profesional = null;
		if(usuario instanceof Paciente)
		{
			paciente = (Paciente)usuario;
			modelo.put("usuario", paciente);
		}
		else if(usuario instanceof Profesional)
		{
			profesional = (Profesional)usuario;
			modelo.put("usuario", profesional);
		}
		else
		{
			modelo.put("usuario", usuario);
		}
		if (usuario instanceof Profesional)
		{
			modelo.put("especialidades", Especialidades.values());
		}
		if (usuario instanceof Paciente)
		{
			modelo.put("obrasSociales", obraSocialServicio.listarObraSociales());
		}
		return "usuario_modificar.html";
	}

	@PostMapping("/usuarios/{id}/modificar")
	public String actualizar(@PathVariable String id, @RequestParam String nombre,
			@RequestParam String apellido, @RequestParam String telefono,
			String numAfiliado,
			@RequestParam String email, String obraSocial,
			@RequestParam String password, @RequestParam String password2,
			MultipartFile archivo, HttpSession session, ModelMap modelo)
	{
		try
		{
			usuarioServicio.actualizar(id, nombre, apellido,
					telefono, email, obraSocial, numAfiliado, password, password2, archivo);
			modelo.put("exito", "Usuario actualizado correctamente");
			return "inicio.html";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			Usuario usuario = (Usuario) usuarioServicio.getOne(id);
			if (usuario instanceof Profesional)
			{
				modelo.put("especialidades", Especialidades.values());
			}
			if (usuario instanceof Paciente)
			{
				modelo.put("obrasSociales", obraSocialServicio.listarObraSociales());
			}
			modelo.put("usuario", usuario);

			return "usuario_modificar.html";
		}
	}
	
	@GetMapping("/registrarProfesional")
	public String registrarProfesional()
	{
		return "registro_profesional.html";
	}
	@PostMapping("/registrarProfesional")
	public String registroProfesional(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono,
			@RequestParam String email, @RequestParam String password, @RequestParam String password2,ModelMap modelo)
	{
		try
		{
			usuarioServicio.registrarProfesional(nombre, apellido, telefono, email, password, password2);
			modelo.put("exito", "Usuario registrado correctamente");
			return "redirect:/login";
		}
		catch (MiException ex)
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
}
