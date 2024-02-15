/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ProfesionalServicio;
import com.Equipo1.sse.servicios.TurnoServicio;
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
	private TurnoServicio turnoServicio;
	
	@Autowired
	private ProfesionalServicio profesionalServicio;
	
	@GetMapping("/")
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
			String especialidad, String matricula, HttpSession session, ModelMap modelo, Authentication authentication)
	{
		try
		{
			Profesional usuarioSession = (Profesional) session.getAttribute("usuarioSession");
			Profesional editado = (Profesional) profesionalServicio.getOne(id);
			if (!editado.equals(usuarioSession))
			{
				profesionalServicio.actualizarProfesional(id, nombre, apellido, telefono, email,

						password, password2, valorConsulta, especialidad, matricula, imagen, curriculum);
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
}
