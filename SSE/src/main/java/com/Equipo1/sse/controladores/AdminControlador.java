/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.controladores;

import com.Equipo1.sse.entidades.ObraSocial;
import com.Equipo1.sse.entidades.Usuario;
import com.Equipo1.sse.excepciones.MiException;
import com.Equipo1.sse.servicios.ObraSocialServicio;
import com.Equipo1.sse.servicios.UsuarioServicio;
import java.util.List;
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
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ObraSocialServicio obraSocialServicio;

    @GetMapping("/dashboard")
    public String index(ModelMap modelo) {
        return "panelAdmin.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_lista.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, HttpSession session, Authentication authentication) {
        Usuario usuarioSession = (Usuario) session.getAttribute("usuarioSession");
        usuarioServicio.cambiarRol(id);
        Usuario editado = usuarioServicio.getOne(id);
        if (!editado.equals(usuarioSession)) {

        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {

        Usuario usuario = (Usuario) usuarioServicio.getOne(id);
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String actualizar(@PathVariable String id, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String telefono,
            @RequestParam String numAfiliado,
            @RequestParam String email, @RequestParam String obraSocial,
            @RequestParam String password, @RequestParam String password2,
            @RequestParam MultipartFile archivo, HttpSession session, ModelMap modelo) {
        try {
            ObraSocial OS = obraSocialServicio.buscarObraSocial(obraSocial);
            usuarioServicio.actualizar(id, nombre, apellido,
                    telefono, email, OS, numAfiliado, password, password2, archivo);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "inicio.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            Usuario usuario = (Usuario) usuarioServicio.getOne(id);
            modelo.put("usuario", usuario);

            return "usuario_modificar.html";
        }
    }
}
