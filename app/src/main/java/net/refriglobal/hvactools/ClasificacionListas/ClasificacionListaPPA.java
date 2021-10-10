package net.refriglobal.hvactools.ClasificacionListas;

public class ClasificacionListaPPA
{
    public int cfm;
    public double diametro;
    public double perdida;

    public ClasificacionListaPPA(double dia, int flujo, double perd)
    {
        diametro = dia;
        cfm = flujo;
        perdida = perd;
    }
}

