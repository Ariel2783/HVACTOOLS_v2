package net.refriglobal.hvactools.ClasificacionListas;

import java.io.Flushable;

public class ClasificacionListaVelocidad
{
    public int velocidad;
    public double perdida;
    public double cfm;

    public ClasificacionListaVelocidad(int vel, double perd, double flujo)
    {
        velocidad = vel;
        perdida = perd;
        cfm = flujo;
    }
}
