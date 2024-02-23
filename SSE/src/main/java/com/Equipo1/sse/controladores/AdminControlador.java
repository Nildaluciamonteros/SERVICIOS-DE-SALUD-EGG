/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Horario;
import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.enumeraciones.Rol;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ObraSocialServicio;
import com.Equipo1.sse.servicios.ProfesionalServicio;
import com.Equipo1.sse.servicios.UsuarioServicio;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProfesionalServicio profesionalServicio;

    @Autowired
    private ObraSocialServicio obraSocialServicio;

    @GetMapping("/dashboard")
    public String index(ModelMap modelo) {
        return "panelAdmin.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        List<String> roles = new ArrayList();
        for (Rol e : Rol.values()) {
            roles.add(e.name());
        }
        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("roles", roles);

        return "usuario_lista.html";
    }

    @PostMapping("/usuarios/buscar")
    public String buscarUsuario(ModelMap modelo, @RequestParam String nombre) {
        List<Usuario> usuarios = usuarioServicio.buscarPorNombre(nombre);
        if (usuarios == null || usuarios.size() > 0) {
            modelo.addAttribute("usuarios", usuarios);
        } else {
            modelo.put("error", "No se encuentra ningun usuario con ese nombre.");
            usuarios = usuarioServicio.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
        }

        return "usuario_buscar.html";
    }

    @GetMapping("/usuarios/{id}/darBaja")
    public String darBajaUsuario(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.getOne(id);
        if (usuario == null) {
            modelo.put("error", "El usuario no se encuentra");
        } else {
            usuarioServicio.darBajaUsuario(id);
            modelo.put("exito", "El usuario se eliminó");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/{id}/darAlta")
    public String darAltaUsuario(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.getOne(id);
        try {
            usuarioServicio.darAltaUsuario(id);
            modelo.put("exito", "El usuario se dio de alta");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/{id}/modificar")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {

        Usuario usuario = (Usuario) usuarioServicio.getOne(id);
        Paciente paciente = null;
        Profesional profesional = null;
        if (usuario instanceof Paciente) {
            paciente = (Paciente) usuario;
            modelo.put("usuario", paciente);
        } else if (usuario instanceof Profesional) {
            profesional = (Profesional) usuario;
            modelo.put("usuario", profesional);
        } else {
            modelo.put("usuario", usuario);
        }
        if (usuario instanceof Profesional) {
            modelo.put("especialidades", Especialidades.values());
        }
        if (usuario instanceof Paciente) {
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
            MultipartFile archivo, HttpSession session, ModelMap modelo) {
        try {
            usuarioServicio.actualizar(id, nombre, apellido,
                    telefono, email, obraSocial, numAfiliado, password, password2, archivo);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "redirect:/admin/usuarios";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            Usuario usuario = (Usuario) usuarioServicio.getOne(id);
            if (usuario instanceof Profesional) {
                modelo.put("especialidades", Especialidades.values());
            }
            if (usuario instanceof Paciente) {
                modelo.put("obrasSociales", obraSocialServicio.listarObraSociales());
            }
            modelo.put("usuario", usuario);

            return "usuario_modificar.html";
        }
    }

    @GetMapping("/registrarProfesional")
    public String registrarProfesional() {
        return "registrar_profesional.html";
    }

    @PostMapping("/registrarProfesional")
    public String registroProfesional(@RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String telefono,
            @RequestParam String email, @RequestParam String password, @RequestParam String especialidad, @RequestParam String password2,
            ModelMap modelo, @RequestParam Double valorConsulta, @RequestParam Integer horasI, @RequestParam Integer horasF, @RequestParam String[] diasSemana) {
        try {
			profesionalServicio.registrarProfesional(nombre, apellido, telefono, email, password, password2, especialidad, valorConsulta, horasI, horasF, diasSemana);
            modelo.put("exito", "Usuario registrado correctamente");
            return "redirect:/login";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("email", email);
            modelo.put("password", password);
            modelo.put("password2", password2);
            modelo.put("especialidad", especialidad);
            return "registrar_profesional.html";
        }
    }
	@GetMapping("/generarProfesionales")
	public String generarProfesionales(ModelMap modelo)
	{
		String[] apellidos = {"García","Rodríguez","Martínez","López","Fernández","González","Pérez","Sánchez","Ramírez","Torres","Flores","Gutiérrez","Díaz","Vázquez","Romero","Navarro","Muñoz","Ruiz","Castillo","Ramos","Álvarez","Morales","Ortega","Jiménez","Moreno","Herrera","Molina","Castro","Soto","Silva","Medina","León","Prieto","Mendoza","Delgado","Herrera","Aguilar","Nieto","Ortiz","Cárdenas","Guerrero","Rivera","Ríos","Cabrera","Ponce","Reyes","Miranda","Peña","Salazar","Vargas","Guzmán","Núñez","Valencia","Zamora","Cervantes","Del Valle","Quintero","Peralta","Benítez","Gallego","Roldán","Serrano","Vidal","Ibarra","Rosales","Espinoza","Franco","Lugo","Escobar","Tovar","Gómez","Calderón","Aguayo","Del Río","Cortés","Lozano","Rosario","Ochoa","Vela","Sandoval","Bautista","Espinosa","Parra","Rojas","Monroy","Galindo","Rangel","Huerta","Solís","Osorio","Chávez","Aguilar","Bernal","Palacios","Avila","Cisneros","Montes","Rivas","Contreras","Maldonado"};
		String[] nombres = {"Alejandro","Sofía","Juan","Valentina","Carlos","María","Andrés","Camila","Javier","Isabella","Luis","Valeria","Diego","Ana","José","Gabriela","Miguel","Natalia","Daniel","Laura","Pablo","Andrea","Fernando","Paula","Antonio","Daniela","Manuel","Sara","Francisco","Julia","Javier","Marta","Jorge","Elena","Raúl","Patricia","Roberto","Clara","Rubén","Lucía","Sergio","Martina","Oscar","Adriana","David","Inés","Ignacio","Victoria","Germán","Marina","Emilio","Carmen","Mateo","Beatriz","Guillermo","Lorena","Ricardo","Isabel","Gabriel","Esther","Emilio","Natalia","Iván","Lorena","Ángel","Claudia","Víctor","Miriam","Álvaro","Lorena","Raúl","Lorena","Sergio","Belén","Juan Pablo","Adriana","Enrique","Marisol","Jorge","Margarita","Pedro","Marta","Fernando","Lorena","Ricardo","Leticia","Xavier","Laura","Diego","Carolina","Raúl","Paloma","José Luis","Luciana","Alfonso","Rocío","Ángel","Elena","Marcos","Violeta"};
		String[] caracteristicas = {"2273","2325","11","221"};
		String[] especialidades = {"Clinica", "Pediatria", "Ginecologia", "Cardiologia"};
		String[] dominios = {"hotmail.com", "gmail.com", "live.com", "yahoo.com.ar"};
		String[] diasSemana = {"lunes","martes","miércoles","jueves","viernes","sàbado","domingo"};
		Integer cantidad = 100;
		for(int i = 0; i < cantidad; i++)
		{
			String nombre = nombres[(((int)(Math.random())) * nombres.length)];
			String apellido = apellidos[(((int)(Math.random())) * apellidos.length)];
			Integer edad = ((int)(Math.random()) * 40) + 25;
			String caracteristica = caracteristicas[(((int)(Math.random())) * caracteristicas.length)];
			String numero = String.valueOf(((int)(Math.random() * 10))) + String.valueOf(((int)(Math.random() * 10))) + String.valueOf(((int)(Math.random() * 10))) + String.valueOf(((int)(Math.random() * 10))) + String.valueOf(((int)(Math.random() * 10))) + String.valueOf(((int)(Math.random() * 10)));
			String telefono = caracteristica + "" + numero;
			String especialidad = especialidades[(((int)(Math.random())) * especialidades.length)];
			Double valorConsulta = (Math.random() * 100) + 50;
			String dominio = dominios[(((int)(Math.random())) * dominios.length)];
			String email = quitarTildes(nombre) + quitarTildes(apellido) + "@" + dominio;
			Integer horasI = (int)(Math.random() * 14);
			Integer horasF = horasI + 8;
			String password = "123123";
			String password2 = password;
			
			try {
				profesionalServicio.registrarProfesional(nombre, apellido, telefono, email, password, password2, especialidad, valorConsulta, horasI, horasF, diasSemana);
				modelo.put("exito", "Usuario registrado correctamente");
			}
			catch (MiException ex)
			{
				modelo.put("error", ex.getMessage());
			}
		}
		return "panelAdmin.html";
	}
	
	public String quitarTildes(String textoConTildes)
	{
        return Normalizer.normalize(textoConTildes, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
