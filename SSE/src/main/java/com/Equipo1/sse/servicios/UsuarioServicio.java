/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Imagen;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.enumeraciones.Rol;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
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
 * @author Nico
 */
@Service
public class UsuarioServicio implements UserDetailsService
{

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private ImagenServicio imagenServicio;

	@Transactional
	public void registrar(String nombre, String apellido, String telefono,
			String email, String obraSocial, String numAfiliado, String password, String password2, MultipartFile archivo) throws MiException
	{
		if(obraSocial == null)
		{
			obraSocial = "Particular";
		}
		validar(nombre,apellido,telefono,email, numAfiliado,password,password2);

		Usuario usuario = new Usuario();

		usuario.setNombre(nombre);
		usuario.setApellido(apellido);
		usuario.setTelefono(telefono);
		usuario.setEmail(email);
		usuario.setObraSocial(obraSocial);
		usuario.setNumAfiliado(numAfiliado);
		usuario.setPassword(new BCryptPasswordEncoder().encode(password));
		usuario.setRol(Rol.PACIENTE);
		Imagen imagen = imagenServicio.guardar(archivo);
		usuario.setImagen(imagen);

		usuarioRepositorio.save(usuario);
	}
	
	@Transactional
	public void actualizar(String idUsuario, String nombre, String apellido,
			String telefono, String email, String obraSocial, String numAfiliado, String password, String password2, MultipartFile archivo) throws MiException
	{
		boolean claveVacia = (password == null || password.isEmpty() && password2 == null || password2.isEmpty());
		if(obraSocial == null)
		{
			obraSocial = "Particular";
		}
		if(claveVacia)
		{
			validar(nombre,apellido,telefono,email, numAfiliado, "123456", "123456");
		}
		else
		{
			validar(nombre,apellido,telefono,email, numAfiliado,password,password2);
		}
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
		if(respuesta.isPresent())
		{
			Usuario usuario = respuesta.get();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setTelefono(telefono);
			usuario.setEmail(email);
			usuario.setObraSocial(obraSocial);
			usuario.setNumAfiliado(numAfiliado);
			if(!claveVacia)
			{
				usuario.setPassword(new BCryptPasswordEncoder().encode(password));
			}
			if(archivo != null && !archivo.isEmpty())
			{
				String idImagen = null;
				if(usuario.getImagen().getId() != null)
				{
					idImagen = usuario.getImagen().getId();
				}
				Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
				usuario.setImagen(imagen);
			}
			usuarioRepositorio.save(usuario);
		}
	}
	
	public void cambiarRol(String id)
	{
		Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
		
		if(respuesta.isPresent())
		{
			Usuario usuario = respuesta.get();
			if(usuario.getRol().equals(Rol.PACIENTE))
			{
				usuario.setRol(Rol.ADMIN);
			}
			else if(usuario.getRol().equals(Rol.ADMIN))
			{
				usuario.setRol(Rol.PACIENTE);
			}
			usuarioRepositorio.save(usuario);
		}
	}
	
	public List<Usuario> listarUsuarios()
	{
		return usuarioRepositorio.findAll();
	}
	
	public List<Profesional> listarProfesionalesPorEspecialidad(String especialidad)
	{
		return usuarioRepositorio.buscarPorEspecialidad(especialidad);
	}
	
	public List<Profesional> listarProfesionales()
	{
		return usuarioRepositorio.buscarTodosLosProfesionales();
	}

	private void validar(String nombre, String apellido, String telefono,
			String email, String numAfiliado, String password, String password2) throws MiException
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
		if (numAfiliado == null || numAfiliado.isEmpty())
		{
			throw new MiException("El numero de afiliado no puede ser nulo o estar vacio");
		}
		if (password == null || password.isEmpty() || password.length() <= 5)
		{
			throw new MiException("La contraseña no puede ser nula o estar vacia, y debe tener más de 5 digitos");
		}
		if (password2 == null || password2.isEmpty() || !password.equals(password2))
		{
			throw new MiException("Las contraseñas ingresadas deben ser iguales");
		}
	}
	public Usuario getOne(String id)
	{
		return usuarioRepositorio.getOne(id);
	}
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	{
		Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

		if (usuario != null)
		{
			List<GrantedAuthority> permisos = new ArrayList();
			GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

			permisos.add(p);

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			HttpSession session = attr.getRequest().getSession();

			session.setAttribute("usuarioSession", usuario);

			return new User(usuario.getEmail(), usuario.getPassword(), permisos);
		} else
		{
			return null;
		}
	}
}
