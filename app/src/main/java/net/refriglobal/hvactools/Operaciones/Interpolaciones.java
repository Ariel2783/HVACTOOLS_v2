package net.refriglobal.hvactools.Operaciones;

import android.widget.EditText;

import net.refriglobal.hvactools.ClasificacionListas.Listas;

import java.util.Locale;

public class Interpolaciones
{
    public void interpolacion1(EditText diaEqv, EditText velLineal, int indexPerdInf, int indexPerdSup, double perdUsuario, double flujoUsuario)
    {
       int i = 0;
       while (i < Listas.listaPPA.size())
       {

           if ( (Listas.listaPPA.get(i).size() >= indexPerdInf) && (Listas.listaPPA.get(i).get(indexPerdSup).cfm > flujoUsuario)  )
            {
                if (Listas.listaPPA.get(i-1).get(indexPerdSup).cfm < flujoUsuario)
                {
                    double diaSuperio = Listas.listaPPA.get(i).get(indexPerdSup).diametro;
                    double diainferior= Listas.listaPPA.get(i-1).get(indexPerdSup).diametro;

                    double difCFM = Listas.listaPPA.get(i).get(indexPerdSup).cfm - Listas.listaPPA.get(i-1).get(indexPerdSup).cfm;
                    double fraccionFlujo = (flujoUsuario - Listas.listaPPA.get(i-1).get(indexPerdInf).cfm)/difCFM;
                    double valorInterpolado = fraccionFlujo * (diaSuperio - diainferior);
                    double diaCalculado = diainferior + valorInterpolado;
                    diaEqv.setText(String.format(Locale.getDefault(), "%.2f", diaCalculado));
                    //TODO: 20211019; Continuar.
                    break;


                }
            }
       i++;
       }
    }
}
