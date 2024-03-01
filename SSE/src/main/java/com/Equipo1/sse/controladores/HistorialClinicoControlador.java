package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.Ficha;
import com.Equipo1.sse.entidades.HistorialClinico;
import com.Equipo1.sse.entidades.ObraSocial;
import com.Equipo1.sse.entidades.Paciente;
import com.Equipo1.sse.entidades.Profesional;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.FichaServicio;
import com.Equipo1.sse.servicios.HistorialClinicoServicio;
import com.Equipo1.sse.servicios.ObraSocialServicio;
import com.Equipo1.sse.servicios.ProfesionalServicio;
import com.Equipo1.sse.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ALEXIS.R.L
 */
@Controller
@RequestMapping("/historialClinico")
public class HistorialClinicoControlador {

    @Autowired
    private HistorialClinicoServicio historialClinicoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private FichaServicio fichaServicio;
    @Autowired
    private ObraSocialServicio obraSocialServicio;
    @Autowired
    private ProfesionalServicio profesionalServicio; 
    
    @GetMapping("/")
    public String lista(ModelMap modelo) {
        List<HistorialClinico> historiales = historialClinicoServicio.listarHistorial();
        modelo.put("historiales", historiales);
        return "historialClinico.html";
    }

    @GetMapping("/buscar/{id}")
    public String buscar(@PathVariable String id, ModelMap modelo) throws MiException {
        Paciente paciente = (Paciente) usuarioServicio.getOne(id);
        if (paciente == null) {
            modelo.put("error", "El paciente no se encuentra");
        } else {
            HistorialClinico historial = historialClinicoServicio.buscarHistorialPorPaciente(paciente);
            if (historial == null) {
                modelo.put("error", "El historial no se encuentra");
            } else {
                List<Ficha> fichas = historialClinicoServicio.leerHistorial(historial);
                modelo.put("fichas", fichas);
            }
        }
        return "historialClinico_buscar.html";
    }
    @GetMapping("/agregar")
    public String agregar(){
        return "historialClinico_form.html";
    }
    
    
    @PostMapping("/agregar/{id}")
    public String agrego(@PathVariable String id, @RequestParam String diagnostico, 
            @RequestParam String obraSocial,@RequestParam String idProfesional, ModelMap modelo){
        try {
            ObraSocial OS = obraSocialServicio.buscarObraSocial(obraSocial);
            Profesional profesional = profesionalServicio.getOne(idProfesional);
            Ficha ficha = fichaServicio.crearFicha(diagnostico, OS, profesional);
            historialClinicoServicio.agregarFicha(ficha, diagnostico);
            modelo.put("exito", "La ficha fue agrgada correctamente!");
        } catch (MiException ex) {
            modelo.put("obraSocial",obraSocial);
            modelo.put("diagnostico",diagnostico);
            modelo.put("idProfesional",idProfesional);
            modelo.put("error", ex.getMessage());
            return "historialClinico_form.html";
        }
        
        return "redirect:/buscar/"+id;
    }
}
