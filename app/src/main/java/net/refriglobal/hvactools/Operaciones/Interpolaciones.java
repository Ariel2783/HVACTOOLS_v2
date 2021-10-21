package net.refriglobal.hvactools.Operaciones;

import android.widget.EditText;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.Listas;

import java.util.List;
import java.util.Locale;

public class Interpolaciones
{
    public void interpolacionDiametroEqv(EditText diaEqv, EditText velLineal, int indexPerdInf, int indexPerdSup, double perdUsuario, double flujoUsuario)
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

           double diametroFinal = 0;

           if (diametro1 < diametro2)
               diametroFinal = diametro1 + valor;

           if (diametro2 < diametro1)
               diametroFinal = diametro2 + valor;

           diaEqv.setText(String.format(Locale.getDefault(), "%.2f", diametroFinal));
           //TODO: 20211020; Continuar.
       }
    }
}
