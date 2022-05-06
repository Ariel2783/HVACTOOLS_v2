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

                        if (item.LadoHor > ladoA) //Termina el ciclo, para no seguir busqcando, ya que no existira el valor
                            break;
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
                                break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
