/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Horario;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.HorarioServicio;
import com.Equipo1.sse.servicios.ProfesionalServicio;
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
@PreAuthorize("hasRole('ROLE_PROFESIONAL')")
@RequestMapping("/profesional")
public class ProfesionalControlador
{

	@Autowired
	private ProfesionalServicio profesionalServicio;
	
	@Autowired
	private HorarioServicio horarioServicio;
	
	@GetMapping("/dashboard")
	public String profesional()
	{
		return "panelProfesional.html";
	}

	@GetMapping("/perfil")
	public String perfil(ModelMap modelo, HttpSession session)
	{
		Profesional usuario = (Profesional) session.getAttribute("usuarioSession");
		modelo.put("usuario", usuario);
		return "usuario_modificar.html";
	}

	@PostMapping("/perfil/{id}")
	public String actualizar(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String telefono, @RequestParam String email, @RequestParam Double valorConsulta,
			@RequestParam String password, @RequestParam String password2, MultipartFile imagen, MultipartFile curriculum,
			@RequestParam String especialidad, @RequestParam String matricula, @RequestParam Integer horasI,
			@RequestParam Integer horasF, @RequestParam String lunes, @RequestParam String martes, @RequestParam String miercoles, @RequestParam String jueves,
			@RequestParam String viernes, @RequestParam String sabado, @RequestParam String domingo, HttpSession session, ModelMap modelo, Authentication authentication)
	{
		try
		{
			Profesional usuarioSession = (Profesional) session.getAttribute("usuarioSession");
			Profesional editado = (Profesional) profesionalServicio.getOne(id);
			if (!editado.equals(usuarioSession))
			{
				profesionalServicio.actualizarProfesional(id, nombre, apellido, telefono, email,
						password, password2, valorConsulta, especialidad, matricula, imagen, curriculum,horasI,horasF,
						lunes, martes, miercoles, jueves, viernes, sabado, domingo);
			}
			modelo.put("exito", "Usuario actualizado correctamente");
			return "redirect:/profesional";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());

			Profesional profesional = (Profesional) session.getAttribute("usuarioSession");

			modelo.put("especialidades", Especialidades.values());
			return "usuario_modificar.html";
		}
	}

	@GetMapping("/horario")
	public String horario(HttpSession session, ModelMap modelo)
	{
		Profesional usuario = (Profesional) session.getAttribute("usuarioSession");
		modelo.put("horarios", profesionalServicio.listarHorarios(usuario.getId()));
		return "horario.html";
	}

	@GetMapping("/horario/agregar")
	public String agregarHorario()
	{
		return "horario_form.html";
	}

	@PostMapping("/horario/agregar")
	public String agregoHorario(Integer horasD, Integer minutosD, Integer horasH, Integer minutosH, Integer diaI, Integer diaF, HttpSession session, ModelMap modelo)
	{
		try
		{
			Horario horario = horarioServicio.registrar(horasD,minutosD,horasH,minutosH,diaI,diaF);
			Profesional usuario = (Profesional) session.getAttribute("usuarioSession");
			profesionalServicio.agregarHorario(usuario.getId(),horario);
			modelo.put("exito","El horario se agregó correctamente");
			return "redirect:/profesional/horario";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			modelo.put("horasD",horasD);
			modelo.put("minutosD",minutosD);
			modelo.put("horasH",horasH);
			modelo.put("minutosH",minutosH);
			modelo.put("diaI",diaI);
			if(diaF != null)
			{
				modelo.put("diaF",diaF);
			}
			return "horario_form.html";
		}
	}
	
	@GetMapping("/horario/{id}/modificar")
	public String modificarHorario(@PathVariable String id)
	{
		Horario horario = horarioServicio.getOne(id);
		
		return "horario_modificar.html";
	}
	
	@PostMapping("/horario/{id}/modificar")
	public String modificoHorario(@PathVariable String id, Integer horasD, Integer minutosD, Integer horasH, Integer minutosH, Integer diaI,Integer diaF, HttpSession session, ModelMap modelo)
	{
		try
		{
			horarioServicio.actualizar(id, horasD, minutosD, horasH, minutosH, diaI,diaF);
			modelo.put("exito","El horario se agregó correctamente");
			return "redirect:/profesional/horario";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			return "horario_modificar.html";
		}
	}
	
	@PostMapping("/horario/{id}/quitar")
	public String quitarHorario(@PathVariable String id, ModelMap modelo, HttpSession session)
	{
		try
		{
			Profesional usuario = (Profesional) session.getAttribute("usuarioSession");
			profesionalServicio.quitarHorario(usuario.getId(), id);
			modelo.put("exito","El horario se quitó correctamente");
			return "redirect:/profesional/horario";
		} catch (MiException ex)
		{
			modelo.put("error", ex.getMessage());
			return "horario_form.html";
		}
	}
}
