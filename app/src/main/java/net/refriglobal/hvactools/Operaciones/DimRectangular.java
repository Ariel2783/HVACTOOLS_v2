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

    public DimRectangular()
    {
        ladoB = -1;
    }

    public void dimensionLadoB(double ladoA)
    {
        Interpolaciones setGetInfo = new Interpolaciones();
        double diaEqv = setGetInfo.getDiametroEqvFinal();
        boolean valorRangoHor = false;
        boolean valorRangoVer = false;

        if (diaEqv > 0)
        {
            for (List<LadoRectangular> listaRectangular : Listas.listaRectangularEqv)
            {
                if (diaEqv == listaRectangular.get(0).DiaEqv) //Se busca el diametro equivalente en las listas*
                {
                    for (LadoRectangular item : listaRectangular)
                    {
                        if (ladoA == item.LadoHor) //Se compara si el valor ladoA del usuario existe en la lista del diaEqv del lado horzontal.
                        {
                            ladoB = item.LadoVer;
                            break;
                        }

                        if (item.LadoHor > ladoA)//Termina el ciclo, para no seguir busqcando, ya que no existira el valor
                        {
                            valorRangoHor = true;
                            break;
                        }
                    }
                    //De no encontrar el valor en lado horizontaL se cambia a buscar en el lado vertical
                    if (ladoB == -1)
                    {
                        for (LadoRectangular itemVer : listaRectangular)
                        {
                            if (ladoA == itemVer.LadoVer)
                            {
                                ladoB = itemVer.LadoHor;
                                break;
                            }

                            if (itemVer.LadoVer < ladoA)
                            {
                                valorRangoVer = true;
                                break;
                            }
                        }
                    }
                    break;
                }
            }

            //Para cuando no coincide el valor en la lista, pero se encuentra dentro de los rangos de valores
            //de la lista en lado horizontal, se Procedera con una interpolacion.
            if (ladoB == -1 && valorRangoHor == true)
            {
                for (List<LadoRectangular> listaRectangular : Listas.listaRectangularEqv)
                {
                    if (diaEqv == listaRectangular.get(0).DiaEqv) //Se busca el diametro equivalente en las listas*
                    {
                        int i = 0;
                        for (LadoRectangular item : listaRectangular)
                        {
                            if (item.LadoHor > ladoA) //Busqueda en lado horizontal.
                            {
                                double datoMayorHor = item.LadoHor;
                                double datoMenorHor = listaRectangular.get(i-1).LadoHor;

                                double datoMayorHor_Ver = item.LadoVer;
                                double datoMenorHor_Ver = listaRectangular.get(i-1).LadoVer;

                                double fraccion = (ladoA - datoMenorHor) / (datoMayorHor - datoMenorHor);
                                double valorXtra = (datoMenorHor_Ver - datoMayorHor_Ver) * fraccion;
                                ladoB = datoMenorHor_Ver - valorXtra;
                                break;
                            }

                            i++;
                        }

                       break;
                    }
                }
            }

            //Para cuando no coincide el valor en la lista, pero se encuentra dentro de los rangos de valores
            //de la lista en lado vertical, se Procedera con una interpolacion.
            if (ladoB == -1 && valorRangoVer == true)
            {
                for (List<LadoRectangular> listaRectangular : Listas.listaRectangularEqv)
                {
                    if (diaEqv == listaRectangular.get(0).DiaEqv) //Se busca el diametro equivalente en las listas*
                    {
                        int i = 0;
                        for (LadoRectangular item : listaRectangular)
                        {
                            if (item.LadoVer < ladoA)
                            {
                                double datoMenorVer = item.LadoVer;
                                double datoMayorVer = listaRectangular.get(i-1).LadoVer;

                                double datoMenorVer_Hor = item.LadoHor;
                                double datoMayorVer_Hor = listaRectangular.get(i-1).LadoHor;

                                double fraccion = (ladoA - datoMenorVer) / (datoMayorVer - datoMenorVer);
                                double valorXtra = (datoMenorVer_Hor - datoMayorVer_Hor) * fraccion;
                                ladoB = datoMenorVer_Hor - valorXtra;
                                break;
                            }
                            i++;
                        }
                        break;
                    }
                }
            }
        }
    }
}
