/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.repositorios;

import com.Equipo1.sse.entidades.Imagen;
import com.Equipo1.sse.excepciones.MiException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nico
 */
@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String>
{
	
}

