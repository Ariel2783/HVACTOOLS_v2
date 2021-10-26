package net.refriglobal.hvactools.Operaciones;

import net.refriglobal.hvactools.DimDucto_Temporal;

public class Calculos
{
    private static double areaDiametroEqv;
    public Double getAreaDiametroEqv() { return areaDiametroEqv; }

    private static double velocidadDiametroEqv;
    public Double getVelocidadDiametro() { return velocidadDiametroEqv; }

    private static double numReynolds;
    public Double getNumeroReynolds() { return numReynolds; }

    public void calculoArea(double diaEqv)
    {
        areaDiametroEqv = (Math.PI * Math.pow(diaEqv/2,2))/144; //dia en plg, area en ft².
    }

    public void calculoVelDiaEqv(double flujoUsuario)
    {
        velocidadDiametroEqv = flujoUsuario/areaDiametroEqv;
    }

    public void calculoNumeroReynolds(double velocidadFluido, double diametro, double viscocidadCinematica)
    {
        numReynolds = ((velocidadFluido/60)*(diametro/12))/(viscocidadCinematica); //velocidad en FPM y diametro en plg.
    }

}

