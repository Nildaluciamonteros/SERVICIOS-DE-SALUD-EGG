package com.Equipo1.sse.entidades;

import com.Equipo1.sse.enumeraciones.Rol;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity @Data @AllArgsConstructor 

public class Usuario implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;

    private String apellido;

    private String telefono;

    private String email;

    private String password;
	
	@Enumerated(EnumType.STRING)
	private Rol rol;
    
    @OneToOne
    private Imagen imagen;

    public Usuario() {
    }
    
    
}
