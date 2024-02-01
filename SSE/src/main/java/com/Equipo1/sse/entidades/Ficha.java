package com.Equipo1.sse.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Data @AllArgsConstructor 
public class Ficha implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String diagnostico;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private String obraSocial;
}
