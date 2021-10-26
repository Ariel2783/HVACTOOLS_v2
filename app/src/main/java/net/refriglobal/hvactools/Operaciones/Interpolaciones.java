package net.refriglobal.hvactools.Operaciones;

import android.widget.EditText;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.Listas;

import java.util.List;
import java.util.Locale;

public class Interpolaciones
{
    private double diametroEqvFinal;
    public Double getDiametroEqvFinal() {return diametroEqvFinal;}

    private double velocidadFinal;
    public Double getVelocidadFlujoAire() {return velocidadFinal;}

    public void interpolacionDiametroEqv(int indexPerdInf, int indexPerdSup, double perdUsuario, double flujoUsuario)
    {
       double diametro1 = 0;
       int i = 0;
       while (i < Listas.listaPPA.size())
       {
           if ((Listas.listaPPA.get(i).size() >= indexPerdInf) &&
               (Listas.listaPPA.get(i).get(indexPerdSup).cfm > flujoUsuario) &&
               (Listas.listaPPA.get(i-1).get(indexPerdSup).cfm < flujoUsuario))
           {
                double diaSuperio = Listas.listaPPA.get(i).get(indexPerdSup).diametro;
                double diainferior= Listas.listaPPA.get(i-1).get(indexPerdSup).diametro;

                double difCFM = Listas.listaPPA.get(i).get(indexPerdSup).cfm - Listas.listaPPA.get(i-1).get(indexPerdSup).cfm;
                double fraccionFlujo = (flujoUsuario - Listas.listaPPA.get(i-1).get(indexPerdInf).cfm)/difCFM;
                double valorInterpolado = fraccionFlujo * (diaSuperio - diainferior);
                diametro1 = diainferior + valorInterpolado;
                break;
           }
        i++;
       }

       double diametro2 = 0;
       i = 0;
       while (i < Listas.listaPPA.size())
       {
           if ((Listas.listaPPA.get(i).size() >= indexPerdInf) &&
               (Listas.listaPPA.get(i).get(indexPerdInf).cfm > flujoUsuario) &&
               (Listas.listaPPA.get(i-1).get(indexPerdInf).cfm < flujoUsuario))
           {
               double diaSuperio = Listas.listaPPA.get(i).get(indexPerdInf).diametro;
               double diainferior= Listas.listaPPA.get(i-1).get(indexPerdInf).diametro;

               double difCFM = Listas.listaPPA.get(i).get(indexPerdInf).cfm - Listas.listaPPA.get(i-1).get(indexPerdInf).cfm;
               double fraccionFlujo = (flujoUsuario - Listas.listaPPA.get(i-1).get(indexPerdInf).cfm)/difCFM;
               double valorInterpolado = fraccionFlujo * (diaSuperio - diainferior);
               diametro2 = diainferior + valorInterpolado;
               break;
           }

           i++;
       }

       if (diametro1 != 0 & diametro2 != 0)
       {
           //Interpolacion final.
           double perdidaInferior = Listas.listaPerdida.get(indexPerdInf).perdidaTabla;
           double perdidaSuperior = Listas.listaPerdida.get(indexPerdSup).perdidaTabla;
           double fraccionPerdida = (perdUsuario - perdidaInferior) / (perdidaSuperior - perdidaInferior);
           double valor = fraccionPerdida * Math.abs(diametro1 - diametro2);

           if (diametro1 < diametro2)
               diametroEqvFinal = diametro1 + valor;

           if (diametro2 < diametro1)
               diametroEqvFinal = diametro2 + valor;
       }
    }

    public void interpolacionVelocidad(int indexPerdInf, int indexPerdSup, double flujoCFM, double perdidaEstatica)
    {
        double velocidad1=0;
        int i = 0;
        while (i < Listas.listaVelocidadPPA.size())
        {
            if (Listas.listaVelocidadPPA.get(i).get(indexPerdSup).cfm > flujoCFM &&
                Listas.listaVelocidadPPA.get(i-1).get(indexPerdSup).cfm < flujoCFM)
            {
                double velocidadSuperior = Listas.listaVelocidadPPA.get(i).get(indexPerdSup).velocidad;
                double velocidadInferrio = Listas.listaVelocidadPPA.get(i-1).get(indexPerdSup).velocidad;

                double diferenciaFlujos = Listas.listaVelocidadPPA.get(i).get(indexPerdSup).cfm - Listas.listaVelocidadPPA.get(i-1).get(indexPerdSup).cfm;
                double fraccionFlujo = (flujoCFM - Listas.listaVelocidadPPA.get(i-1).get(indexPerdSup).cfm)/diferenciaFlujos;
                double valorInterpolado = fraccionFlujo * (velocidadSuperior - velocidadInferrio);
                velocidad1 = velocidadInferrio + valorInterpolado;
                break;
            }

            i++;
        }

        double velocidad2 = 0;
        i = 0;
        while (i < Listas.listaVelocidadPPA.size())
        {
            if (Listas.listaVelocidadPPA.get(i).get(indexPerdInf).cfm > flujoCFM &&
                Listas.listaVelocidadPPA.get(i-1).get(indexPerdInf).cfm < flujoCFM)
            {
                double velocidadSuperior = Listas.listaVelocidadPPA.get(i).get(indexPerdSup).velocidad;
                double velocidadInferrio = Listas.listaVelocidadPPA.get(i-1).get(indexPerdSup).velocidad;

                double diferenciaFlujos = Listas.listaVelocidadPPA.get(i).get(indexPerdInf).cfm - Listas.listaVelocidadPPA.get(i-1).get(indexPerdInf).cfm;
                double fraccionFlujo = (flujoCFM - Listas.listaVelocidadPPA.get(i-1).get(indexPerdInf).cfm)/diferenciaFlujos;
                double valorInterpolado = fraccionFlujo * (velocidadSuperior - velocidadInferrio);
                velocidad2 = velocidadInferrio + valorInterpolado;
                break;
            }

            i++;
        }

        double perdidaSuperior = Listas.listaPerdida.get(indexPerdSup).perdidaTabla;
        double perdidaInferior = Listas.listaPerdida.get(indexPerdInf).perdidaTabla;

        double fraccionFinal = (perdidaEstatica - perdidaInferior)/(perdidaSuperior - perdidaInferior);
        double valorFinal = fraccionFinal * (velocidad1 - velocidad2);
        velocidadFinal = velocidad2 + valorFinal;

    }
}
