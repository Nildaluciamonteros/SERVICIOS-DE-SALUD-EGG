package com.Equipo1.sse.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity @Data @AllArgsConstructor 

public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;

    private String apellido;

    private String telefono;

    private String email;

    private String password;
    
    @OneToOne
    private Imagen imagen;

    public Usuario() {
    }
    
    
}
