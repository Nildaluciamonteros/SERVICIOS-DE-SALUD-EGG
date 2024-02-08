/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.entidades.Turno;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.servicios.TurnoServicio;
import java.util.List;
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
@RequestMapping("/paciente")
public class PacienteControlador
{
	@Autowired
	private TurnoServicio turnoServicio;
	
	@GetMapping("/")
	public String paciente()
	{
		return "paciente.html";
	}
	
	@GetMapping("/turnos")
	public String turnos(ModelMap modelo, HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
		if(usuario instanceof Paciente)
		{
			List<Turno> turnos = turnoServicio.buscarPorNumeroAfiliado(((Paciente)usuario).getNumAfiliado());
			modelo.put("turnos", turnos);
		}
		return "turnos.hmtl";
	}
}
