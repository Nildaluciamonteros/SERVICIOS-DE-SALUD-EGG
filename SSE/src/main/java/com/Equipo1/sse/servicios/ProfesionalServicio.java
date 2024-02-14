/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.servicios;

import com.Equipo1.sse.entidades.Curriculum;
import com.Equipo1.sse.entidades.Imagen;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.repositorios.ProfesionalRepositorio;
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
 * @author Aldan
 */
@Service
public class ProfesionalServicio implements UserDetailsService {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;


    @Autowired
    private CurriculumServicio curriculumServicio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void actualizarProfesional(String idUsuario, String nombre, String apellido,
            String telefono, String email, String password, String password2,
            Double valorConsulta, String especialidad, String matricula,
            MultipartFile archImagen, MultipartFile archCurriculum) throws MiException {
        boolean claveVacia = (password == null || password.isEmpty() && password2 == null || password2.isEmpty());
        // Validar datos
        if (claveVacia) {
            validarProfesional(nombre, apellido, telefono, email, "123456", "123456", especialidad);
        } else {
            validarProfesional(nombre, apellido, telefono, email, password, password2, especialidad);
        }
        // Buscar usuario
        Optional<Profesional> respuesta = profesionalRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Profesional profesional = (Profesional) respuesta.get();
            profesional.setNombre(nombre);
            profesional.setApellido(apellido);
            profesional.setTelefono(telefono);
            profesional.setEmail(email);
            Especialidades espe = Especialidades.buscar(especialidad);
            profesional.setEspecialidad(espe);
            profesional.setValorConsulta(valorConsulta);

            if (!claveVacia) {
                profesional.setPassword(new BCryptPasswordEncoder().encode(password));
            }
            if (archImagen != null && !archImagen.isEmpty()) {
                String idImagen = null;
                if (profesional.getImagen().getId() != null) {
                    idImagen = profesional.getImagen().getId();
                }
                Imagen imagen = imagenServicio.actualizar(archImagen, idImagen);
                profesional.setImagen(imagen);
            }
            if (archCurriculum != null && !archCurriculum.isEmpty()) {
                String idCurriculum = null;
                if (profesional.getCurriculum().getId() != null) {
                    idCurriculum = profesional.getCurriculum().getId();
                }
                Curriculum curriculum = curriculumServicio.actualizar(archCurriculum, idCurriculum);
                profesional.setCurriculum(curriculum);
            }
            profesionalRepositorio.save(profesional);
        }
    }

    private void validarProfesional(String nombre, String apellido, String telefono,
            String email, String password, String password2,
            String especialidad) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo o estar vacio");
        }
        if (telefono == null || telefono.isEmpty() || !telefono.matches("[0-9]+")) {
            throw new MiException("El telefono no puede contener caracteres que no sean numeros, ser nulo o estar vacio");
        }
        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        }
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("La contraseña no puede ser nula o estar vacia, y debe tener minimo 6 digitos");
        }
        if (password2 == null || password2.isEmpty() || !password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        if (especialidad == null || especialidad.isEmpty() || Especialidades.buscar(especialidad) != null) {
            throw new MiException("La especialidad ingresada no es válida");
        }
    }
    
    public void cambiarEspecialidad(String idProfesional, String especialidad) {
        Especialidades esp = Especialidades.buscar(especialidad);
        Optional<Profesional> respuesta = profesionalRepositorio.findById(idProfesional);
        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            if (profesional instanceof Profesional) {
                ((Profesional) profesional).setEspecialidad(Especialidades.buscar(especialidad));
            }
        }
    }

    public void darBajaProfesional(String idProfesional) {
        Optional<Profesional> respuesta = profesionalRepositorio.findById(idProfesional);
        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setActivado(false);
            profesionalRepositorio.save(profesional);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profesional profesional = (Profesional) profesionalRepositorio.buscarPorEmail(email);

        if (profesional != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + profesional.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession();

            session.setAttribute("usuarioSession", profesional);

            return new User(profesional.getEmail(), profesional.getPassword(), permisos);
        } else {
            return null;
        }
    }
    public List<Profesional> listarProfesionalesPorEspecialidad(String especialidad)
	{
		return profesionalRepositorio.buscarPorEspecialidad(especialidad);
	}
	
    public Profesional getOne(String id)
	{
		return profesionalRepositorio.getOne(id);
	}

}
