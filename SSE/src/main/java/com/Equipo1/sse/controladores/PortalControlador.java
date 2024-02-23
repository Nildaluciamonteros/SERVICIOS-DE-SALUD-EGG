/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Recuperacion;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.enumeraciones.Rol;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ObraSocialServicio;
import com.Equipo1.sse.servicios.ProfesionalServicio;
import com.Equipo1.sse.servicios.RecuperacionServicio;
import com.Equipo1.sse.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

	@Autowired
	private ProfesionalServicio profesionalServicio;

	@Autowired
	private RecuperacionServicio recuperacionServicio;

	@Autowired
	private JavaMailSender mailSender;

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
			return "redirect:/profesional/dashboard";
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
		modelo.put("obrasSociales", obraSocialServicio.listarObraSociales());
		return "registro.html";
	}
	
	@GetMapping("/faqs")
	public String faqs()
	{
		return "faq.html";
	}
	
	@GetMapping("/contactanos")
	public String contactanos()
	{
		return "contactanos.html";
	}

	@PostMapping("/registro")
	public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono,
			@RequestParam String email, String obraSocial, String numAfiliado, @RequestParam String password,
			@RequestParam String password2,
			MultipartFile archivo, ModelMap modelo)
	{
		try
		{
			usuarioServicio.registrar(nombre, apellido, telefono, email, obraSocial, numAfiliado,
					password, password2, archivo);
			modelo.put("exito", "Usuario registrado correctamente");
			return "redirect:/login";
		} catch (MiException ex)
		{
			modelo.put("obrasSociales", obraSocialServicio.listarObraSociales());
			modelo.put("obraSocial",obraSocial);
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

	@GetMapping("/profesionales")
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

	@GetMapping("/profesionales/{especialidad}")
	public String especialidad(@PathVariable String especialidad, ModelMap modelo)
	{
		List<Profesional> profesionales = profesionalServicio.listarProfesionalesPorEspecialidad(especialidad);
		modelo.put("profesionales", profesionales);
		return "especialidad.html";
	}

	@PreAuthorize("hasAnyRole('ROLE_PACIENTE','ROLE_ADMIN','ROLE_PROFESIONAL')")
	@GetMapping("/perfil")
	public String perfil(ModelMap modelo, HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
		Paciente paciente = null;
		Profesional profesional = null;
		if (usuario instanceof Paciente)
		{
			paciente = (Paciente) usuario;
			modelo.put("usuario", paciente);
		} else if (usuario instanceof Profesional)
		{
			profesional = (Profesional) usuario;
			modelo.put("usuario", profesional);
		} else
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

	@PreAuthorize("hasAnyRole('ROLE_PACIENTE','ROLE_ADMIN','ROLE_PROFESIONAL')")
	@PostMapping("/perfil/{id}")
	public String actualizar(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String telefono, @RequestParam String email, @RequestParam String obraSocial,
			@RequestParam String numAfiliado,
			@RequestParam String password, @RequestParam String password2, MultipartFile archivo, HttpSession session,
			ModelMap modelo, Authentication authentication)
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
			return "redirect:/inicio";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			modelo.put("usuario", usuario);
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
	}

	public void enviarCorreo(String desde, String para, String asunto, String mensaje)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(desde);
		message.setTo(para);
		message.setSubject(asunto);
		message.setText(mensaje);
		mailSender.send(message); //método Send(envio), propio de Java Mail
	}

	@GetMapping("/recuperarClave")
	public String recuperarClave(ModelMap modelo)
	{
		return "recuperar_form.html";
	}

	@PostMapping("/recuperarClave")
	public String recuperoClave(@RequestParam String email, ModelMap modelo, HttpSession session)
	{
		Usuario usuario = usuarioServicio.buscarPorEmail(email);
		try
		{
			Recuperacion recu = recuperacionServicio.registrar(usuario.getId());
			enviarCorreo("soporte@sse.com", usuario.getEmail(), "SSE - Recuperar clave", "Puede recuperar su clave con este link: localhost:8080/" + recu.getId() + "/recuperarClave");
			modelo.put("mensaje", "Se ha enviado un mensaje con un enlace para recuperar la clave, vence en una hora.");
		} catch (MiException ex)
		{
			modelo.put("mensaje", ex.getMessage());
		}
		return "mensaje.html";
	}

	@GetMapping("/{id}/recuperarClave")
	public String cambiarClave(@PathVariable String id, ModelMap modelo)
	{
		Recuperacion recu = recuperacionServicio.getOne(id);
		Usuario usuario = usuarioServicio.getOne(recu.getUsuarioId());
		if (recu.getFechaLimite().after(new Date()))
		{
			modelo.put("id", usuario.getId());
			return "cambiar_clave.html";
		} else
		{
			modelo.put("mensaje", "La fecha limite ha pasado.");
			return "mensaje.html";
		}
	}

	@PostMapping("/cambiarClave")
	public String cambioClave(@RequestParam String id, @RequestParam String password, @RequestParam String password2, ModelMap modelo)
	{
		try
		{
			usuarioServicio.cambiarClave(id, password, password2);
			modelo.put("exito","Ha cambiado la clave");
			return "redirect:/login";
		} catch (MiException ex)
		{
			modelo.put("mensaje", ex.getMessage());
			return "mensaje.html";
		}
	}
}
