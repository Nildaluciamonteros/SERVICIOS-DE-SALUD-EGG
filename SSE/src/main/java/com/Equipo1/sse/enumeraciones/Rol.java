/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Equipo1.sse.enumeraciones;

/**
 *
 * @author Nico
 */
public enum Rol
{
	PACIENTE,PROFESIONAL,ADMIN;
	
	public static Rol buscar(String val)
	{
		for (Rol v : values())
		{
			if (v.name().equals(val))
			{
				return v;
			}
		}
		return null;
	}
}
