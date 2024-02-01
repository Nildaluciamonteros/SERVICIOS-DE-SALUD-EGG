package com.Equipo1.sse.entidades;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Data @AllArgsConstructor 
public class HistorialClinico {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @OneToMany
    private List<Ficha> fichas;
    
    @OneToOne
    private Paciente paciente;
}
