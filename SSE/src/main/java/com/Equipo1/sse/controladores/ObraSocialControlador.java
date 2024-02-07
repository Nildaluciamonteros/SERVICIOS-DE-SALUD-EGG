package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.ObraSocial;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ObraSocialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ALEXIS.R.L
 */
@Controller
@RequestMapping("/obraSocial")
public class ObraSocialControlador {

    @Autowired
    private ObraSocialServicio obraSocialServicio;
    
    @GetMapping("/registrar") //localhost:8080/obraSocioal/registrar
    public String registrar(){
        return "obraSocial_form.html";
    }
    
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        
        try {
            obraSocialServicio.registrar(nombre);
            
            modelo.put("exito", "La obra Socual fue registrada correctamente!");
        } catch (MiException ex) {
                      
            modelo.put("error", ex.getMessage());
            return "obraSocial_form.html";
        }
        
        return "obraSocial.html";        
    }
    
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<ObraSocial> OS = obraSocialServicio.listarObraSociales();
        modelo.put("obraSociales", OS);

        return "obraSocial_lista.html";
    }
    
    @GetMapping("/obraSocial/{id}/eliminar")
	public String eliminarUsuario(@PathVariable String id, ModelMap modelo)
	{
		ObraSocial OS = obraSocialServicio.getOne(id);
		if(OS == null)
		{
			modelo.put("error", "La Obra Social no se encuentra");
		}
		else
		{
			obraSocialServicio.eliminarObraSocial(id);
			modelo.put("exito", "La Obra Social se eliminó");
		}
		return "redirect:/admin/obraSocial";
	}
}
