/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Nico
 */
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Hora implements Comparable<Hora>
{
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
	private Integer horas;
	private Integer minutos;

	@Override
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		if(!(x instanceof Hora))
		{
			return false;
		}
		Hora h = (Hora)x;
		return (this.getHoras() == h.getHoras() && this.getMinutos() == h.getMinutos());
	}
	
	@Override
	public int compareTo(Hora x)
	{
		if((this.getHoras() < x.getHoras()) || (this.getHoras() == x.getHoras() && this.getMinutos() < x.getMinutos()))
		{
			return -1;
		}
		else if(this.getHoras() == x.getHoras() && this.getMinutos() == x.getMinutos())
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
}
