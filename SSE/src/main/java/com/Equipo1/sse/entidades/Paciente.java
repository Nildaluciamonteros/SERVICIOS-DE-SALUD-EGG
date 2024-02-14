package com.Equipo1.sse.entidades;

import com.Equipo1.sse.enumeraciones.Rol;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Data @AllArgsConstructor
public class Paciente extends Usuario {

    @OneToOne
    private ObraSocial obraSocial;

    private String numAfiliado;

    public Paciente() {
        super();
		setRol(Rol.PACIENTE);
    }

    
}
