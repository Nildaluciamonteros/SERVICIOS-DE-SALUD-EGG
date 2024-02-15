/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Nico
 */
@Controller
@RequestMapping("/paciente")
public class PacienteControlador {

    @GetMapping("/")
    public String paciente() {
        return "paciente.html";
    }
}
