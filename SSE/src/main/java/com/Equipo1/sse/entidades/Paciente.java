package com.Equipo1.sse.entidades;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Data @AllArgsConstructor 
public class Paciente extends Usuario {

    private Boolean obraSocial;

    private String numAfiliado;

    public Paciente() {
        super();
    }

    
}
