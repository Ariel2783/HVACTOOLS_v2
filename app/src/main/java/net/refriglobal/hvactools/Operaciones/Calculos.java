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

    private static double factorFriccion;
    public Double getFactorFriccion() { return factorFriccion; }

    private static double presionVelocidad;
    public Double getPresionVelocidad() { return presionVelocidad; }

    private static double perdidaFriccion;
    public Double getPerdidaFriccion() { return perdidaFriccion; }

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
        //areaDiametroEqv en ft^2, se multiplica por 144 para su conversion en plg^2.
        double diametroHidraulico = (4*areaDiametroEqv*144)/(2*Math.PI*(diametro/2)); //diametro en plg.
        numReynolds = (diametroHidraulico * velocidadFluido)/(720*viscocidadCinematica);
        /*referencia de ecuacion de NumRe de ASHRAE Handbook-Fundamentals 2009, Duct Desing Chapter 21, pg 21.6
        * ecuacion(20)*/
    }

    public void calculoFactorFriccion(double diametro)
    {
        double rugosidad = 0.0005; //unidad de rugosidad ft
        factorFriccion = 0.25/(Math.pow((Math.log10((1/(3.7*((diametro/12)/rugosidad))) + (5.74/Math.pow(numReynolds, 0.9)))) ,2));
        /*Referencia de ecuacion, P.K. Swamee y A.K. Jain (1976), Libro Mecanica de fluidos, Robert L. Mott
        * 4ª edicion.*/
    }

    public void calculoPresionVelocidad(double densidadAire)
    {
        presionVelocidad = densidadAire*Math.pow((velocidadDiametroEqv/1097),2);
        /*referencia de ecuacion, ASHRAE Handbook-Fundamentals 2009, Duct Desing Chapter 21, pg 21.2
         * ecuacion(8)*/
    }

    public void calculoPerdidaFriccion(double diametro)
    {
        double diametroHidraulico = (4*areaDiametroEqv*144)/(2*Math.PI*(diametro/2)); //diametro en plg.
        perdidaFriccion = (12*factorFriccion*100/diametroHidraulico)*presionVelocidad;
        /*referencia de ecuacion, ASHRAE Handbook-Fundamentals 2009, Duct Desing Chapter 21, pg 21.6
         * ecuacion(18)*/
    }

}

