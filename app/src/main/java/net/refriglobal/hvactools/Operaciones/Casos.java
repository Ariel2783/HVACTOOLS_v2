package net.refriglobal.hvactools.Operaciones;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaVelocidad;
import net.refriglobal.hvactools.ClasificacionListas.Listas;
import net.refriglobal.hvactools.DimDucto_Temporal;

import java.util.List;
import java.util.Locale;

public class Casos {

    private static double velocidadEqv;
    public Double getVelocidadDiametro() { return velocidadEqv; }

    public void Caso1(double diaEqv, double flujoArie, int indexListaPerdida)
    {
        //Valor de velocidad del grafico
        for (List<ClasificacionListaVelocidad> listaVel: Listas.listaVelocidadPPA)
        {
            if (flujoArie == listaVel.get(indexListaPerdida).cfm)
            {
                velocidadEqv = listaVel.get(indexListaPerdida).velocidad;

                Calculos operacion = new Calculos();
                operacion.calculoArea(diaEqv);

                operacion.calculoVelDiaEqv(flujoArie);

                DimDucto_Temporal objDim = new DimDucto_Temporal();

                double valorViscoCinematica = Double.parseDouble(objDim.textViewViscoCinematica.getText().toString());
                operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), diaEqv, valorViscoCinematica);

                operacion.calculoFactorFriccion(diaEqv);

                double aireDensidad = Double.parseDouble(objDim.textViewDensidadAire.getText().toString());
                operacion.calculoPresionVelocidad(aireDensidad);

                operacion.calculoPerdidaFriccion(diaEqv);

                break;
            }

            Interpolaciones inter = new Interpolaciones();

            if (flujoArie < listaVel.get(indexListaPerdida).cfm)
            {
                //Interpolar velocidad.
                inter.interpolacionVelocidadMetodo2(flujoArie, indexListaPerdida);

                velocidadEqv = inter.getVelocidadFlujoAire();

                Calculos operacion = new Calculos();
                operacion.calculoArea(diaEqv);

                operacion.calculoVelDiaEqv(flujoArie);

                DimDucto_Temporal objDim = new DimDucto_Temporal();

                double valorViscoCinematica = Double.parseDouble(objDim.textViewViscoCinematica.getText().toString());
                operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), diaEqv, valorViscoCinematica);

                operacion.calculoFactorFriccion(diaEqv);

                double aireDensidad = Double.parseDouble(objDim.textViewDensidadAire.getText().toString());
                operacion.calculoPresionVelocidad(aireDensidad);

                operacion.calculoPerdidaFriccion(diaEqv);

                break;
            }
        }
    }

    public void Caso2(double flujoArie, int indexListaPerdida)
    {
        Interpolaciones objInterpolacion = new Interpolaciones();
        objInterpolacion.interpolacionDiametroEqvMetodo2(indexListaPerdida, flujoArie);
        objInterpolacion.interpolacionVelocidadMetodo2(flujoArie,indexListaPerdida);

        Calculos operacion = new Calculos();
        operacion.calculoArea(objInterpolacion.getDiametroEqvFinal());

        operacion.calculoVelDiaEqv(flujoArie);

        DimDucto_Temporal objDim = new DimDucto_Temporal();

        double valorViscoCinematica = Double.parseDouble(objDim.textViewViscoCinematica.getText().toString());
        operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), objInterpolacion.getDiametroEqvFinal(), valorViscoCinematica);

        operacion.calculoFactorFriccion(objInterpolacion.getDiametroEqvFinal());

        double aireDensidad = Double.parseDouble(objDim.textViewDensidadAire.getText().toString());
        operacion.calculoPresionVelocidad(aireDensidad);

        operacion.calculoPerdidaFriccion(objInterpolacion.getDiametroEqvFinal());
    }

    public void Caso3(int indexSuperior, int indexInferior, double flujoAire, double perdidaEstatica )
    {
        Interpolaciones inter = new Interpolaciones();
        inter.interpolacionDiametroEqv(indexInferior, indexSuperior, perdidaEstatica, flujoAire);
        inter.interpolacionVelocidad(indexInferior, indexSuperior, flujoAire, perdidaEstatica);

        Calculos operacion = new Calculos();
        operacion.calculoArea(inter.getDiametroEqvFinal());

        operacion.calculoVelDiaEqv(flujoAire);

        DimDucto_Temporal objDim = new DimDucto_Temporal();

        double valorViscoCinematica = Double.parseDouble(objDim.textViewViscoCinematica.getText().toString());
        operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), inter.getDiametroEqvFinal(), valorViscoCinematica);

        operacion.calculoFactorFriccion(inter.getDiametroEqvFinal());

        double aireDensidad = Double.parseDouble(objDim.textViewDensidadAire.getText().toString());
        operacion.calculoPresionVelocidad(aireDensidad);

        operacion.calculoPerdidaFriccion(inter.getDiametroEqvFinal());
    }
}
