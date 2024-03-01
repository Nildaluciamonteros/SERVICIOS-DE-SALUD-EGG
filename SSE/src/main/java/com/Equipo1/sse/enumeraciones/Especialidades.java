package com.Equipo1.sse.enumeraciones;

public enum Especialidades
{

	Clinica, Pediatria, Ginecologia, Cardiologia;

	public static Especialidades buscar(String val)
	{
		for (Especialidades v : values())
		{
			if (v.name().toLowerCase().equals(val.toLowerCase()))
			{
				return v;
			}
		}
		return null;
	}
}