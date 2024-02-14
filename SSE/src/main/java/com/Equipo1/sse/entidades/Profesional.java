package com.Equipo1.sse.entidades;

import com.Equipo1.sse.enumeraciones.Especialidades;
import com.Equipo1.sse.enumeraciones.Rol;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Data @AllArgsConstructor 
public class Profesional extends Usuario {
    
    @OneToMany
    private List<Turno> turnos;
    
    @Enumerated(EnumType.STRING)
    private Especialidades especialidad;

    private Double valorConsulta;
	
	private String matricula;

    private Integer reputacion;
	
	private Curriculum curriculum;

    public Profesional() {
        super();
		setRol(Rol.PROFESIONAL);
    }
    
    
}
