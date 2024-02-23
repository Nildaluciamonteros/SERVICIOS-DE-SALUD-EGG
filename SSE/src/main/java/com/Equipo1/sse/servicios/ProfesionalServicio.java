/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Curriculum;
import com.Equipo1.sse.entidades.Horario;
import com.Equipo1.sse.entidades.Imagen;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Turno;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Aldan
 */
@Service
public class ProfesionalServicio implements UserDetailsService
{

	@Autowired
	private CurriculumServicio curriculumServicio;

	@Autowired
	private HorarioServicio horarioServicio;

	@Autowired
	private ImagenServicio imagenServicio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Transactional
	public void registrarProfesional(String nombre, String apellido, String telefono,
			String email, String password, String password2, String especialidad, Double valorConsulta,
			Integer horasI, Integer horasF, String[] diasSemana) throws MiException
	{
		validarProfesional(nombre, apellido, telefono, email, password, password2, especialidad, valorConsulta, horasI, horasF, diasSemana);
		// Crear Usuario
		Profesional usuario = new Profesional();
		List<Horario> horarios = new ArrayList();
		Horario horario = new Horario();
		Boolean[] dias =
		{
			false, false, false, false, false, false, false
		};

		for (String dia : diasSemana)
		{
			switch (dia)
			{
				case "lunes":
					dias[0] = true;
					break;
				case "martes":
					dias[1] = true;
					break;
				case "miércoles":
					dias[2] = true;
					break;
				case "jueves":
					dias[3] = true;
					break;
				case "viernes":
					dias[4] = true;
					break;
				case "sábado":
					dias[5] = true;
					break;
				case "domingo":
					dias[6] = true;
					break;
			}
		}
		horario.setDias(dias);
		horario.setHorasDesde(horasI);
		horario.setMinutosDesde(0);
		horario.setHorasHasta(horasF);
		horario.setMinutosHasta(0);

		usuario.setNombre(nombre);
		usuario.setApellido(apellido);
		usuario.setTelefono(telefono);
		usuario.setEmail(email);
		usuario.setPassword(new BCryptPasswordEncoder().encode(password));
		Especialidades espe = Especialidades.buscar(especialidad);
		usuario.setEspecialidad(espe);
		usuario.setHorarios(horarios);
		usuario.setValorConsulta(valorConsulta);
		usuario.setActivado(Boolean.TRUE);
		
		usuarioRepositorio.save(usuario);
	}

	@Transactional
	public void actualizarProfesional(String idUsuario, String nombre, String apellido,
			String telefono, String email, String password, String password2,
			Double valorConsulta, String especialidad, String matricula,
			MultipartFile archImagen, MultipartFile archCurriculum, Integer horasI, Integer horasF, String[] diasSemana) throws MiException
	{
		boolean claveVacia = (password == null || password.isEmpty() && password2 == null || password2.isEmpty());
		// Validar datos
		if (claveVacia)
		{
			validarProfesional(nombre, apellido, telefono, email, "123456", "123456", especialidad, valorConsulta, horasI, horasF, diasSemana);
		} else
		{
			validarProfesional(nombre, apellido, telefono, email, password, password2, especialidad, valorConsulta, horasI, horasF, diasSemana);
		}
		// Buscar usuario
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
		if (respuesta.isPresent())
		{
			List<Horario> horarios = new ArrayList();
			Horario horario = new Horario();
			Boolean[] dias =
			{
				false, false, false, false, false, false, false
			};

			for (String dia : diasSemana)
			{
				switch (dia)
				{
					case "lunes":
						dias[0] = true;
						break;
					case "martes":
						dias[1] = true;
						break;
					case "miércoles":
						dias[2] = true;
						break;
					case "jueves":
						dias[3] = true;
						break;
					case "viernes":
						dias[4] = true;
						break;
					case "sábado":
						dias[5] = true;
						break;
					case "domingo":
						dias[6] = true;
						break;
				}
			}
			horario.setDias(dias);
			horario.setHorasDesde(horasI);
			horario.setMinutosDesde(0);
			horario.setHorasHasta(horasF);
			horario.setMinutosHasta(0);
			Profesional profesional = (Profesional) respuesta.get();
			profesional.setNombre(nombre);
			profesional.setApellido(apellido);
			profesional.setTelefono(telefono);
			profesional.setEmail(email);
			Especialidades espe = Especialidades.buscar(especialidad);
			profesional.setEspecialidad(espe);
			profesional.setValorConsulta(valorConsulta);
			profesional.setHorarios(horarios);

			if (!claveVacia)
			{
				profesional.setPassword(new BCryptPasswordEncoder().encode(password));
			}
			if (archImagen != null && !archImagen.isEmpty())
			{
				String idImagen = null;
				if (profesional.getImagen().getId() != null)
				{
					idImagen = profesional.getImagen().getId();
				}
				Imagen imagen = imagenServicio.actualizar(archImagen, idImagen);
				profesional.setImagen(imagen);
			}
			if (archCurriculum != null && !archCurriculum.isEmpty())
			{
				String idCurriculum = null;
				if (profesional.getCurriculum().getId() != null)
				{
					idCurriculum = profesional.getCurriculum().getId();
				}
				Curriculum curriculum = curriculumServicio.actualizar(archCurriculum, idCurriculum);
				profesional.setCurriculum(curriculum);
			}
			usuarioRepositorio.save(profesional);
		}
	}

	private void validarProfesional(String nombre, String apellido, String telefono,
			String email, String password, String password2,
			String especialidad, Double valorConsulta, Integer horasI, Integer horasF, String[] dias) throws MiException
	{
		if (nombre == null || nombre.isEmpty())
		{
			throw new MiException("El nombre no puede ser nulo o estar vacio");
		}
		if (apellido == null || apellido.isEmpty())
		{
			throw new MiException("El apellido no puede ser nulo o estar vacio");
		}
		if (telefono == null || telefono.isEmpty() || !telefono.matches("[0-9]+"))
		{
			throw new MiException("El telefono no puede contener caracteres que no sean numeros, ser nulo o estar vacio");
		}
		if (email == null || email.isEmpty())
		{
			throw new MiException("El email no puede ser nulo o estar vacio");
		}
		if (password == null || password.isEmpty() || password.length() <= 5)
		{
			throw new MiException("La contraseña no puede ser nula o estar vacia, y debe tener minimo 6 digitos");
		}
		if (password2 == null || password2.isEmpty() || !password.equals(password2))
		{
			throw new MiException("Las contraseñas ingresadas deben ser iguales");
		}
		if (especialidad == null || especialidad.isEmpty() || Especialidades.buscar(especialidad) != null)
		{
			throw new MiException("La especialidad ingresada no es válida");
		}
		if (valorConsulta == null || valorConsulta <= 0)
		{
			throw new MiException("La consulta ingresada no es válida");
		}
		if (horasI == null || horasI < 0 || horasI > 23)
		{
			throw new MiException("La horas de ingreso ingresada no es válida");
		}
		if (horasF == null || horasF < 0 || horasF > 23)
		{
			throw new MiException("La horas de salida ingresada no es válida");
		}
		if (dias == null || dias.length <= 0)
		{
			throw new MiException("Tiene que ingresar los días que va a atender");
		}
	}

	public void cambiarEspecialidad(String idProfesional, String especialidad)
	{
		Especialidades esp = Especialidades.buscar(especialidad);
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idProfesional);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			if (profesional instanceof Profesional)
			{
				((Profesional) profesional).setEspecialidad(Especialidades.buscar(especialidad));
			}
		}
	}

	public void darBajaProfesional(String idProfesional)
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idProfesional);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			profesional.setActivado(false);
			usuarioRepositorio.save(profesional);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	{
		Profesional profesional = (Profesional) usuarioRepositorio.buscarPorEmail(email);

		if (profesional != null)
		{
			List<GrantedAuthority> permisos = new ArrayList();
			GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + profesional.getRol().toString());

			permisos.add(p);

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			HttpSession session = attr.getRequest().getSession();

			session.setAttribute("usuarioSession", profesional);

			return new User(profesional.getEmail(), profesional.getPassword(), permisos);
		} else
		{
			return null;
		}
	}

	public List<Profesional> listarProfesionalesPorEspecialidad(String especialidad)
	{
		return usuarioRepositorio.buscarPorEspecialidad(especialidad);
	}

	public Profesional getOne(String id)
	{
		return (Profesional) usuarioRepositorio.getOne(id);
	}

	public void agregarTurno(String idProfesional, Turno turno) throws MiException
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idProfesional);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			List<Turno> turnos = profesional.getTurnos();
			turnos.add(turno);
			usuarioRepositorio.save(profesional);
		} else
		{
			throw new MiException("No se encontró el profesional");
		}
	}

	public List<Horario> listarHorarios(String id)
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			return profesional.getHorarios();
		} else
		{
			return null;
		}
	}

	public void agregarHorario(String id, Horario horario) throws MiException
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			List<Horario> horarios = profesional.getHorarios();
			horarios.add(horario);
			usuarioRepositorio.save(profesional);
		} else
		{
			throw new MiException("No se encontró el profesional");
		}
	}

	public void actualizarHorario(String id, Horario horario) throws MiException
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			List<Horario> horarios = profesional.getHorarios();
			horarios.add(horario);
			usuarioRepositorio.save(profesional);
		} else
		{
			throw new MiException("No se encontró el profesional");
		}
	}

	public void quitarHorario(String idProfesional, String idHorario) throws MiException
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idProfesional);
		if (respuesta.isPresent())
		{
			Profesional profesional = (Profesional) respuesta.get();
			List<Horario> horarios = profesional.getHorarios();
			Iterator<Horario> horarioIt = horarios.iterator();
			boolean encontrado = false;
			while (horarioIt.hasNext() && !encontrado)
			{
				Horario horario = horarioIt.next();
				if (horario.getId().equals(idHorario))
				{
					horarioServicio.quitar(idHorario);
					horarioIt.remove();
					encontrado = true;
				}
			}
			if (!encontrado)
			{
				throw new MiException("No se encontró el horario");
			}
			profesional.setHorarios(horarios);
			usuarioRepositorio.save(profesional);
		} else
		{
			throw new MiException("No se encontró el profesional");
		}
	}
}
