package net.refriglobal.hvactools.Operaciones;

import net.refriglobal.hvactools.ClasificacionListas.LadoRectangular;
import net.refriglobal.hvactools.ClasificacionListas.Listas;

import java.util.List;
import java.util.Locale;

public class DimRectangular
{
    private static double ladoB;
    public Double getLadoB() { return ladoB; }
    public void setLadoB(double lado_B) { ladoB = lado_B; }

    private static double ladoA;
    public Double getLadoA() { return ladoA; }
    public void setLadoA(double lado_A) { ladoA = lado_A; }
    //TODO: 20220508: CONTINUAR con el dimensionamiento desde el lado B.

    public DimRectangular()
    {
        ladoA = -1;
        ladoB = -1;
    }

    public void dimensionLadoBBiseccion(double ladoA)
    {
        Interpolaciones setGetInter = new Interpolaciones();
        double diaEqv = setGetInter.getDiametroEqvFinal();
        double valorFinal = 0;
        double valorInicial = 0;
        final double error = 0.001;

        //Busqueda del intervalo.
        int i = 0;
        while (true)
        {
            //2009 ASHRAE Handbook Fundamentals, CHAPTER 21, pg - 21.7, eq - 25
            valorFinal = (1.30*Math.pow( (ladoA*(i+1) ), 0.625) / Math.pow( (ladoA+(i+1)) ,0.25)) - diaEqv;

            if (valorFinal > 0)
            {
                valorFinal = i+1;
                break;
            }

            else
                valorInicial = i+1;

            i++;
        }

        //Metodo de biseccion.
        while (true)
        {
            //paso 1
            double puntoMedio = (valorFinal + valorInicial) / 2;

            //Paso 2
            //Evaluando valor inicial
            double xi = (1.30*Math.pow( (ladoA*valorInicial ), 0.625) / Math.pow( (ladoA+valorInicial) ,0.25)) - diaEqv;

            //Evaluando valor final
            double xs = (1.30*Math.pow( (ladoA*valorFinal ), 0.625) / Math.pow( (ladoA+valorFinal) ,0.25)) - diaEqv;

            //Evaluando punto medio
            double xm = (1.30*Math.pow( (ladoA*puntoMedio ), 0.625) / Math.pow( (ladoA+puntoMedio) ,0.25)) - diaEqv;

            //Paso 3
            if ( (xi*xm) > 0)
            {
                valorInicial = puntoMedio;
                valorFinal = valorFinal;
            }

            else
            {
                valorInicial = valorInicial;
                valorFinal = puntoMedio;
            }

            //Paso 4
            double errorCalc = Math.abs((valorFinal - valorInicial)/2);
            if (errorCalc <= error)
            {
                ladoB = puntoMedio;
                break;
            }
        }
    }
}
