package net.refriglobal.hvactools.Operaciones;

import android.widget.EditText;

import com.google.android.gms.dynamic.IFragmentWrapper;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaVelocidad;
import net.refriglobal.hvactools.ClasificacionListas.Listas;

import java.util.List;
import java.util.Locale;

public class Interpolaciones
{
    private static double diametroEqvFinal;
    public Double getDiametroEqvFinal() {return diametroEqvFinal;}

    public void setDiametroEqv(double dia)
    {
        diametroEqvFinal = dia;
    }

    private static double velocidadFinal;
    public Double getVelocidadFlujoAire() {return velocidadFinal;}

    public void setVelocidadFlujoAire(double velocidad) { velocidadFinal = velocidad;}

    private static double perdidaFinal;
    public Double getPerdidaFinal() {return perdidaFinal;}

    public void setPerdidaFinal(double perdida){perdidaFinal = perdida;}

    private static double flujoAire;
    public void setFlujoAire(double caudal) { flujoAire = caudal;}

    public void interpolacionDiametroEqv(int indexPerdInf, int indexPerdSup, double perdUsuario, double flujoUsuario)
    {
       double diametro1 = 0;
       int i = 0;
       while (i < Listas.listaPPA.size())
       {
           if ((Listas.listaPPA.get(i).size() > indexPerdInf) &&
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
           if ((Listas.listaPPA.get(i).size() > indexPerdInf) &&
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

    public void interpolacionDiametroEqvMetodo2(int indexPerdidaUsuario, double cfmUsuario)
    {
        int i = 0;
        while (i < Listas.listaPPA.size())
        {
            if ((Listas.listaPPA.get(i).size() >= indexPerdidaUsuario) &&
                    Listas.listaPPA.get(i).get(indexPerdidaUsuario).cfm > cfmUsuario &&
                    Listas.listaPPA.get(i-1).get(indexPerdidaUsuario).cfm < cfmUsuario)
            {
                double flujoSuperior = Listas.listaPPA.get(i).get(indexPerdidaUsuario).cfm;
                double flujoInferior = Listas.listaPPA.get(i-1).get(indexPerdidaUsuario).cfm;
                double diametroSuperior = Listas.listaPPA.get(i).get(indexPerdidaUsuario).diametro;
                double diametroInferior = Listas.listaPPA.get(i-1).get(indexPerdidaUsuario).diametro;

                double difCFM = flujoSuperior - flujoInferior;
                double fraccionFlujo = (cfmUsuario - flujoInferior)/difCFM;

                double valorExtraDiametro = (diametroSuperior - diametroInferior) * fraccionFlujo;

                diametroEqvFinal = diametroInferior + valorExtraDiametro;
                break;
            }

            i++;
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

    public void interpolacionVelocidadMetodo2(double cfmUsuario, int indexPerdidaUsuario)
    {
        velocidadFinal = 0;

        /*Caso Ideal, cuando el caudal y la perdida coinciden con los valores de la matriz de velocidad.*/
        if (indexPerdidaUsuario >= 0)
            for (List<ClasificacionListaVelocidad> listaVel:Listas.listaVelocidadPPA)
            {
                if (listaVel.get(indexPerdidaUsuario).cfm == cfmUsuario )
                {
                    velocidadFinal = listaVel.get(indexPerdidaUsuario).cfm;
                    break;
                }

                if (listaVel.get(indexPerdidaUsuario).cfm > cfmUsuario)
                    break;
            }

        /*La perdida coincide con los valores de la lista, el valor de la velocidad no coincide con los valores de la lista*/
        if (velocidadFinal == 0)
        {
            int i = 0;
            while (i < Listas.listaVelocidadPPA.size())
            {
                try
                {
                    if (Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).cfm > cfmUsuario &&
                            Listas.listaVelocidadPPA.get(i - 1).get(indexPerdidaUsuario).cfm < cfmUsuario)
                    {
                        double cfmSuperior = Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).cfm;
                        double cfmInferrio = Listas.listaVelocidadPPA.get(i - 1).get(indexPerdidaUsuario).cfm;

                        double diferenciaFlujos = cfmSuperior - cfmInferrio;
                        double fraccionFlujo = (cfmUsuario - cfmInferrio) / diferenciaFlujos;

                        double velocidadSuperior = Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).velocidad;
                        double velocidadInferior = Listas.listaVelocidadPPA.get(i - 1).get(indexPerdidaUsuario).velocidad;

                        double valorExtraVelocidad = (velocidadSuperior - velocidadInferior) * fraccionFlujo;
                        velocidadFinal = velocidadInferior + valorExtraVelocidad;
                        break;
                    }
                    i++;
                }
                catch (Exception e) //Para cuando se requiere extrapolacion
                {
                    if (Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).velocidad == 200 ||
                            Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).velocidad == 12000 )
                    {
                        double cfmSuperior = Listas.listaVelocidadPPA.get(i+1).get(indexPerdidaUsuario).cfm;
                        double cfmInferior = Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).cfm;

                        double fraccionVelocidad = (cfmInferior - cfmUsuario)/(cfmSuperior - cfmInferior);
                        double factorCorrecion = 0.96607; //Factor de correcion para aproximar la grafica logaritmica.
                        velocidadFinal = (Listas.listaVelocidadPPA.get(i).get(indexPerdidaUsuario).velocidad * (1-fraccionVelocidad))*factorCorrecion;
                        break;
                    }
                }
            }
        }
    }

    public void interpolacionPerdida(double flujoCFM, double diaEqvUsuario, int listaDia)
    {
        int i = 0;
        while (i < Listas.listaPPA.get(listaDia).size())
        {
            if (Listas.listaPPA.get(listaDia).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaDia).get(i).cfm !=0)
            {
                double perd01 = Listas.listaPPA.get(listaDia).get(i).perdida;
                double perd02 = Listas.listaPPA.get(listaDia).get(i-1).perdida;

                double cfm01 = Listas.listaPPA.get(listaDia).get(i).cfm;
                double cfm02 = Listas.listaPPA.get(listaDia).get(i-1).cfm;

                double difCFM = cfm02 - cfm01;
                double fraccionCFM = (flujoCFM - cfm01) / difCFM;

                double difPerdida = perd02 - perd01;
                perdidaFinal = (difPerdida * fraccionCFM) + perd01;
                break;
            }

            i++;
        }
    }

    public void interpolacionPerdida2(int listaInf, int listaSup, double flujoCFM, double diaEqvUsuario)
    {
        double diaInf = Listas.listaPPA.get(listaInf).get(1).diametro;
        double diaSup = Listas.listaPPA.get(listaSup).get(1).diametro;

        double perdidaInferior = -1;
        double perdidaSuperior = -1;

        int i = 0;
        while (i < Listas.listaPPA.get(listaInf).size())
        {
            if (Listas.listaPPA.get(listaInf).get(i).cfm == flujoCFM)
            {
                perdidaInferior = Listas.listaPPA.get(listaInf).get(i).perdida;
                break;
            }

            //Instruccion para terminar el ciclo, para evitar buscar en los valores inferiores al flujo del usuario.
            if(Listas.listaPPA.get(listaInf).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaInf).get(i).cfm != 0)
                break;

            i++;
        }

        i = 0;
        while (i < Listas.listaPPA.get(listaSup).size())
        {
            if (Listas.listaPPA.get(listaSup).get(i).cfm == flujoCFM)
            {
                perdidaSuperior = Listas.listaPPA.get(listaSup).get(i).perdida;
                break;
            }

            //Instruccion para terminar el ciclo, para evitar buscar en los valores inferiores al flujo del usuario.
            if(Listas.listaPPA.get(listaSup).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaSup).get(i).cfm != 0)
                break;

            i++;
        }

        if (perdidaInferior == -1 || perdidaSuperior == -1)
        {
            i = 0;
            while (i < Listas.listaPPA.get(listaInf).size())
            {
                if (Listas.listaPPA.get(listaInf).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaInf).get(i).cfm !=0)
                {
                    double perd01 = Listas.listaPPA.get(listaInf).get(i).perdida;
                    double perd02 = Listas.listaPPA.get(listaInf).get(i-1).perdida;
                    diaInf = Listas.listaPPA.get(listaInf).get(1).diametro;

                    double cfm01 = Listas.listaPPA.get(listaInf).get(i).cfm;
                    double cfm02 = Listas.listaPPA.get(listaInf).get(i-1).cfm;

                    double difCFM = cfm02 - cfm01;
                    double fraccionCFM = (flujoCFM - cfm01) / difCFM;

                    double difPerdida = perd02 - perd01;
                    perdidaInferior = (difPerdida * fraccionCFM) + perd01;
                    break;
                }
                i++;
            }

            i = 0;
            while (i < Listas.listaPPA.get(listaSup).size())
            {
                if (Listas.listaPPA.get(listaSup).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaSup).get(i).cfm !=0)
                {
                    double perd01 = Listas.listaPPA.get(listaSup).get(i).perdida;
                    double perd02 = Listas.listaPPA.get(listaSup).get(i-1).perdida;
                    diaSup = Listas.listaPPA.get(listaSup).get(1).diametro;

                    double cfm01 = Listas.listaPPA.get(listaSup).get(i).cfm;
                    double cfm02 = Listas.listaPPA.get(listaSup).get(i-1).cfm;

                    double difCFM = cfm02 - cfm01;
                    double fraccionCFM = (flujoCFM - cfm01) / difCFM;

                    double difPerdida = perd02 - perd01;
                    perdidaSuperior = (difPerdida * fraccionCFM) + perd01;
                    break;
                }
                i++;
            }
        }

        if (perdidaInferior > -1 || perdidaSuperior > -1)
        {
            double fraccionDiametro = (diaEqvUsuario - diaInf)/(diaSup - diaInf);
            double difPerdida = Math.abs(perdidaSuperior - perdidaInferior);
            perdidaFinal = Math.abs((difPerdida * fraccionDiametro) - perdidaInferior);
        }
    }
}
