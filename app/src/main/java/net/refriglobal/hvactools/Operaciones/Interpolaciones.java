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
    public Double getFlujoAire(){return  flujoAire;}

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

    public void interpolacionPerdida2(int listaDiaInf, int listaDiaSup, double flujoCFM, double diaEqvUsuario)
    {
        double diaInf = Listas.listaPPA.get(listaDiaInf).get(1).diametro;
        double diaSup = Listas.listaPPA.get(listaDiaSup).get(1).diametro;

        double perdidaInferior = -1;
        double perdidaSuperior = -1;

        int i = 0;
        while (i < Listas.listaPPA.get(listaDiaInf).size())
        {
            if (Listas.listaPPA.get(listaDiaInf).get(i).cfm == flujoCFM)
            {
                perdidaInferior = Listas.listaPPA.get(listaDiaInf).get(i).perdida;
                break;
            }

            //Instruccion para terminar el ciclo, para evitar buscar en los valores inferiores al flujo del usuario.
            if(Listas.listaPPA.get(listaDiaInf).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaDiaInf).get(i).cfm != 0)
                break;

            i++;
        }

        i = 0;
        while (i < Listas.listaPPA.get(listaDiaSup).size())
        {
            if (Listas.listaPPA.get(listaDiaSup).get(i).cfm == flujoCFM)
            {
                perdidaSuperior = Listas.listaPPA.get(listaDiaSup).get(i).perdida;
                break;
            }

            //Instruccion para terminar el ciclo, para evitar buscar en los valores inferiores al flujo del usuario.
            if(Listas.listaPPA.get(listaDiaSup).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaDiaSup).get(i).cfm != 0)
                break;

            i++;
        }

        if (perdidaInferior == -1 || perdidaSuperior == -1)
        {
            i = 0;
            while (i < Listas.listaPPA.get(listaDiaInf).size())
            {
                if (Listas.listaPPA.get(listaDiaInf).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaDiaInf).get(i).cfm !=0)
                {
                    double perd01 = Listas.listaPPA.get(listaDiaInf).get(i).perdida;
                    double perd02 = Listas.listaPPA.get(listaDiaInf).get(i-1).perdida;
                    diaInf = Listas.listaPPA.get(listaDiaInf).get(1).diametro;

                    double cfm01 = Listas.listaPPA.get(listaDiaInf).get(i).cfm;
                    double cfm02 = Listas.listaPPA.get(listaDiaInf).get(i-1).cfm;

                    double difCFM = cfm02 - cfm01;
                    double fraccionCFM = (flujoCFM - cfm01) / difCFM;

                    double difPerdida = perd02 - perd01;
                    perdidaInferior = (difPerdida * fraccionCFM) + perd01;
                    break;
                }
                i++;
            }

            i = 0;
            while (i < Listas.listaPPA.get(listaDiaSup).size())
            {
                if (Listas.listaPPA.get(listaDiaSup).get(i).cfm < flujoCFM && Listas.listaPPA.get(listaDiaSup).get(i).cfm !=0)
                {
                    double perd01 = Listas.listaPPA.get(listaDiaSup).get(i).perdida;
                    double perd02 = Listas.listaPPA.get(listaDiaSup).get(i-1).perdida;
                    diaSup = Listas.listaPPA.get(listaDiaSup).get(1).diametro;

                    double cfm01 = Listas.listaPPA.get(listaDiaSup).get(i).cfm;
                    double cfm02 = Listas.listaPPA.get(listaDiaSup).get(i-1).cfm;

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

    public void interpolacionPerdida3(double cfmCal, double flujoInf, double flujoSup, double perdInf, double perdSup)
    {
        double fraccionCFM = (cfmCal - flujoInf)/(flujoSup - flujoInf);
        double valorInterpolado = (perdSup - perdInf)*fraccionCFM;
        perdidaFinal = valorInterpolado + perdInf;
    }

    public void interpolacionCFM(int indexPerdInf, int indexPerdSup, double perdUsuario, double velocidadUsuario)
    {
        for (List<ClasificacionListaVelocidad> listaVel : Listas.listaVelocidadPPA)
        {
            if (velocidadUsuario == listaVel.get(0).velocidad)
            {
                double cfmPerdInf = listaVel.get(indexPerdInf).cfm;
                double cfm0PerdSup = listaVel.get(indexPerdSup).cfm;

                double perdInf = listaVel.get(indexPerdInf).perdida;
                double perdSup = listaVel.get(indexPerdSup).perdida;

                double fraccionPerd = (perdUsuario - perdInf) / (perdSup - perdInf);
                double valorextraCFM = Math.abs(cfmPerdInf - cfm0PerdSup) * fraccionPerd;
                flujoAire = cfmPerdInf - valorextraCFM;
                break;
            }
        }
    }

    public void interpolacionCFM2(int indexPerdInf, int indexPerdSup, int indexListaVelSup, int indexListaVelInf, double perdUsuario, double velocidadUsuario)
    {
        //Lista superior de velocidad.
        double perdInf = Listas.listaPerdida.get(indexPerdInf).perdidaTabla;
        double perdSup = Listas.listaPerdida.get(indexPerdSup).perdidaTabla;
        double fraccionPerdida = (perdUsuario - perdInf)/(perdSup - perdInf);

        double cfmPerdInfVelSup = Listas.listaVelocidadPPA.get(indexListaVelSup).get(indexPerdInf).cfm;
        double cfmPerdSupVelSup = Listas.listaVelocidadPPA.get(indexListaVelSup).get(indexPerdSup).cfm;

        double valorInterpolado = (cfmPerdInfVelSup - cfmPerdSupVelSup)*fraccionPerdida;
        double cfmVelSup = cfmPerdInfVelSup - valorInterpolado;

        //Lista inferior de velocidad.
        double cfmPerdInfVelInf = Listas.listaVelocidadPPA.get(indexListaVelInf).get(indexPerdInf).cfm;
        double cfmPerdSupVelInf = Listas.listaVelocidadPPA.get(indexListaVelInf).get(indexPerdSup).cfm;

        valorInterpolado = (cfmPerdInfVelInf - cfmPerdSupVelInf)*fraccionPerdida;
        double cfmVelInf = cfmPerdInfVelInf - valorInterpolado;

        double velocidadInf = Listas.listaVelocidadPPA.get(indexListaVelInf).get(0).velocidad;
        double velocidadSup = Listas.listaVelocidadPPA.get(indexListaVelSup).get(0).velocidad;

        double fraccionVel = (velocidadUsuario - velocidadInf)/(velocidadSup - velocidadInf);

        flujoAire = ( (Math.abs(cfmVelSup - cfmVelInf))*fraccionVel ) + cfmVelInf;
    }

    public void interpolacionCFM3(int indexPerdInf, int indexPerdSup, int indexDiaEqv)
    {
        double perdidaInf = Listas.listaPPA.get(indexDiaEqv).get(indexPerdInf).perdida;
        double perdidaSup = Listas.listaPPA.get(indexDiaEqv).get(indexPerdSup).perdida;
        double cfmInf = Listas.listaPPA.get(indexDiaEqv).get(indexPerdInf).cfm;
        double cfmSup = Listas.listaPPA.get(indexDiaEqv).get(indexPerdSup).cfm;

        double fraccionPerdida = (perdidaFinal - perdidaInf) / (perdidaSup - perdidaInf);
        double valorInterCFM = (cfmSup - cfmInf) * fraccionPerdida;
        flujoAire = cfmInf + valorInterCFM;
    }

}
