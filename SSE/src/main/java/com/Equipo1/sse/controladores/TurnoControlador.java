package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Administrador;
import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.entidades.Turno;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ProfesionalServicio;
import com.Equipo1.sse.servicios.TurnoServicio;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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

/**
 *
 * @author ALEXIS.R.L & Nico
 */
@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PACIENTE','ROLE_PROFESIONAL')")
@RequestMapping("/turnos")
public class TurnoControlador {

    @Autowired
    private TurnoServicio turnoServicio;
    @Autowired
    private ProfesionalServicio profesionalServicio;

    @GetMapping("/registrados")
    public String turnos(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        if (usuario instanceof Paciente) {
            List<Turno> turnos = turnoServicio.buscarPorNumeroAfiliado(((Paciente) usuario).getNumAfiliado());
            modelo.put("turnos", turnos);
        } else if (usuario instanceof Profesional) {
            List<Turno> turnos = ((Profesional) usuario).getTurnos();
            modelo.put("turnos", turnos);
        } else {
            List<Turno> turnos = turnoServicio.listarTurnos();
            modelo.put("turnos", turnos);
        }
        return "turnos.html";
    }

    @GetMapping("/buscar/{id}")
    public String turnos(@PathVariable String id, ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        if (usuario instanceof Paciente) {
            Profesional profesional = profesionalServicio.getOne(id);
            List<Turno> turnos = profesional.getTurnos();
            modelo.put("turnos", turnos);
        }
        return "turnos.html";/* Se corrige el .html */
    }

    @PostMapping("/sacarTurno")
    public String sacarTurno(@RequestParam String idTurno, ModelMap modelo, HttpSession session,
            HttpServletRequest request) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
            if (usuario instanceof Paciente) {
                Turno turno = turnoServicio.getOne(idTurno);
                if (turno.getPaciente() == null) {
                    turnoServicio.modificarTurno(turno.getId(), turno.getFecha(), (Paciente) usuario);
                    modelo.put("exito", "El turno se saco con exito");
                } else {
                    throw new MiException("El turno no se encuentra disponible");
                }
            }
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESIONAL')")
    @GetMapping("/abrirTurnos")
    public String abrirTurnos(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        if (usuario instanceof Profesional || usuario instanceof Administrador) {
            return "turno_form.html";
        } else {
            return "redirect:/turnos";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN','ROLE_PROFESIONAL')")
    @PostMapping("/abrirTurnos")
    public String generarTurnos(ModelMap modelo, HttpSession session) throws MiException {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
            if (usuario instanceof Profesional) {
                Date ahora = new Date();
                ahora.setHours(0);
                ahora.setMinutes(0);
                ahora.setSeconds(0);
                ahora.setDate(ahora.getDate() + 1);
                for (int j = 0; j < 365; j++) {
                    for (int i = 0; i < 24; i++) {
                        Date nuevo = new Date();
                        nuevo.setTime(ahora.getTime());
                        nuevo.setHours(ahora.getHours() + i);
                        nuevo.setDate(ahora.getDate() + j);
                        Turno turno = turnoServicio.crearTurno(nuevo, null);
                        profesionalServicio.agregarTurno(usuario.getId(), turno);
                    }
                }
            }
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/turnos";
    }
}
