package net.refriglobal.hvactools.Operaciones;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaVelocidad;
import net.refriglobal.hvactools.ClasificacionListas.ClasificasionListaPerdida;
import net.refriglobal.hvactools.ClasificacionListas.Listas;
import net.refriglobal.hvactools.DimDucto_Temporal;

import java.io.Flushable;
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

                double valorViscoCinematica = Double.parseDouble(DimDucto_Temporal.textViewViscoCinematica.getText().toString());
                operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), diaEqv, valorViscoCinematica);

                operacion.calculoFactorFriccion(diaEqv);

                double aireDensidad = Double.parseDouble(objDim.textViewDensidadAire.getText().toString());
                operacion.calculoPresionVelocidad(aireDensidad);

                operacion.calculoPerdidaFriccion(diaEqv);

                break;
            }

            if (flujoArie < listaVel.get(indexListaPerdida).cfm)
            {
                //Interpolar velocidad.
                Interpolaciones inter = new Interpolaciones();
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

        OperacionesFinales(flujoArie);
    }

    public void Caso3(int indexSuperior, int indexInferior, double flujoAire, double perdidaEstatica)
    {
        Interpolaciones inter = new Interpolaciones();
        inter.interpolacionDiametroEqv(indexInferior, indexSuperior, perdidaEstatica, flujoAire);
        inter.interpolacionVelocidad(indexInferior, indexSuperior, flujoAire, perdidaEstatica);

        OperacionesFinales(flujoAire);
    }

    public Double Caso4(double flujoAireUsuario, double velocidadUsuario)
    {
        double perdidaEstatica = 0;
        boolean datoEncontrado = false;
        boolean fueraRango = false;
        for (List<ClasificacionListaVelocidad> listaVel:Listas.listaVelocidadPPA)
        {
            for (ClasificacionListaVelocidad itemList : listaVel)
            {
                if (velocidadUsuario == itemList.velocidad && flujoAireUsuario == itemList.cfm)
                {
                    perdidaEstatica = itemList.perdida;
                    OperacionesFinales(flujoAireUsuario);

                    datoEncontrado = true;
                    break;
                }

                /*si la busqueda de velocidad no se encuentra en los valores de la lista*/
                if (itemList.velocidad > velocidadUsuario)
                {
                    fueraRango = true;
                    break;
                }

                /*Sentencia para determinar si la busqueda esta en la lista de velocidad incorrecta y saltar
                 * al siguiente valor de velocidad*/
                if (velocidadUsuario != itemList.velocidad)
                    break;
            }

            if (datoEncontrado == true || fueraRango == true)
                break;
        }

        return perdidaEstatica;
    }

    public Double Caso5(double flujoAireUsuario, double velocidadUsuario)
    {
        double perdidaInter = 0;
        double velocidadEnCurso = 0;
        for (List<ClasificacionListaVelocidad> listaVel:Listas.listaVelocidadPPA)
        {
            int i = 0;
            while (i < listaVel.size())
            {
                velocidadEnCurso = listaVel.get(i).velocidad;

                if (velocidadUsuario == velocidadEnCurso && listaVel.get(i).cfm > flujoAireUsuario)
                {
                    double cfmSuperior = listaVel.get(i).cfm;
                    double cfmInferior = listaVel.get(i-1).cfm;

                    double perdSuperior = listaVel.get(i-1).perdida;
                    double perdInfeior = listaVel.get(i).perdida;

                    double relacionDiferencia = cfmSuperior - cfmInferior;
                    double diferenciaCfmUsuario = flujoAireUsuario - cfmInferior;
                    double fraccionCfm = diferenciaCfmUsuario/relacionDiferencia;

                    double diferenciaPerdidas = perdSuperior - perdInfeior;
                    double valorExtraPerdida = fraccionCfm * diferenciaPerdidas;

                    perdidaInter = perdSuperior - valorExtraPerdida;
                }

                /*Sentencia para determinar si la busqueda esta en la lista de velocidad incorrecta y saltar
                 * al siguiente valor de velocidad*/
                if (velocidadUsuario != velocidadEnCurso || perdidaInter > 0)
                    break;

                i++;
            }

            if (perdidaInter > 0 || velocidadEnCurso > velocidadUsuario)
                break;
        }

        if (perdidaInter > 0)
        {
            int indexInferior = 0;
            int indexSuperior = 0;
            for (ClasificasionListaPerdida itemPerd : Listas.listaPerdida) {
                if (itemPerd.perdidaTabla < perdidaInter) {
                    indexInferior = itemPerd.index;
                    indexSuperior = itemPerd.index - 1;
                    break;
                }
            }

            if (indexInferior > 0 && indexSuperior > 0)
            {
                /*Se obtiene el diametro equivalente correspondiente*/
                Interpolaciones inter = new Interpolaciones();
                inter.interpolacionDiametroEqv(indexInferior, indexSuperior, perdidaInter, flujoAireUsuario);

                OperacionesFinales(flujoAireUsuario);
            }
        }
        return perdidaInter;
    }

    public Double Caso6(double flujoAireUsuario, double velocidadUsuario)
    {
        double perdidaInter = 0;
        double velocidadSuperior = 0;
        double velocidadInferior = 0;
        double perdidaSup = 0;
        double perdidaInf = 0;

        int h = 0;
        while (h < Listas.listaVelocidadPPA.size())
        {
            if (velocidadUsuario < Listas.listaVelocidadPPA.get(h).get(0).velocidad)
            {
                velocidadSuperior = Listas.listaVelocidadPPA.get(h).get(0).velocidad;
                velocidadInferior = Listas.listaVelocidadPPA.get(h-1).get(0).velocidad;

                int indexVelocidadSup = h;
                int indexVelocidadInf = h-1;

                int i = 0;
                while (i < Listas.listaVelocidadPPA.get(indexVelocidadSup).size())
                {
                    if (flujoAireUsuario == Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i).cfm)
                    {
                        perdidaSup = Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i).perdida;
                        break;
                    }

                    //Interpolacion para encontrar la perdida respectiva a los cfm que no se encuentran en la lista
                    if (flujoAireUsuario < Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i).cfm)
                    {
                        double cfmSuperior = Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i).cfm;
                        double cfmInferior = Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i-1).cfm;

                        double perdCfmSuperior = Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i).perdida;
                        double perdCfmInfeior = Listas.listaVelocidadPPA.get(indexVelocidadSup).get(i-1).perdida;

                        double relacionDiferencia = cfmSuperior - cfmInferior;
                        double diferenciaCfmUsuario = flujoAireUsuario - cfmInferior;
                        double fraccionCfm = diferenciaCfmUsuario/relacionDiferencia;

                        double diferenciaPerdidas = perdCfmInfeior - perdCfmSuperior ;
                        double valorExtraPerdida = fraccionCfm * diferenciaPerdidas;

                        perdidaSup = perdCfmSuperior + valorExtraPerdida;

                        break;
                    }

                    i++;
                }

                i = 0;
                while (i < Listas.listaVelocidadPPA.get(indexVelocidadInf).size())
                {
                    if (flujoAireUsuario == Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i).cfm)
                    {
                        perdidaInf = Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i).perdida;
                        break;
                    }

                    //Interpolacion para encontrar la perdida respectiva a los cfm que no se encuentran en la lista
                    if (flujoAireUsuario < Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i).cfm)
                    {
                        double cfmSuperior = Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i).cfm;
                        double cfmInferior = Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i-1).cfm;

                        double perdCfmSuperior = Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i).perdida;
                        double perdCfmInfeior = Listas.listaVelocidadPPA.get(indexVelocidadInf).get(i-1).perdida;

                        double relacionDiferencia = cfmSuperior - cfmInferior;
                        double diferenciaCfmUsuario = flujoAireUsuario - cfmInferior;
                        double fraccionCfm = diferenciaCfmUsuario/relacionDiferencia;

                        double diferenciaPerdidas = perdCfmInfeior - perdCfmSuperior ;
                        double valorExtraPerdida = fraccionCfm * diferenciaPerdidas;

                        perdidaInf = perdCfmSuperior + valorExtraPerdida;

                        break;
                    }

                    i++;
                }

                if (perdidaSup > 0 && perdidaInf > 0)
                    break;
            }

            h++;
        }

        if (perdidaSup > 0 && perdidaInf > 0)
        {
            //Interpolacion para obtener a perdida final respectiva a la velocidad y cfm del usuario.
            double diferenciaVelocidades = velocidadSuperior - velocidadInferior;
            double difVelUsuario = velocidadUsuario - velocidadInferior;

            double relacionVelUsuario = difVelUsuario / diferenciaVelocidades;

            double difPerdidaInterpoladas = perdidaSup - perdidaInf;
            double valorExtraPerdida = difPerdidaInterpoladas * relacionVelUsuario;
            perdidaInter = perdidaInf + valorExtraPerdida;
        }

        /****************************/
        if (perdidaInter > 0)
        {
            int indexInferior = 0;
            int indexSuperior = 0;
            for (ClasificasionListaPerdida itemPerd : Listas.listaPerdida) {
                if (itemPerd.perdidaTabla < perdidaInter) {
                    indexInferior = itemPerd.index;
                    indexSuperior = itemPerd.index - 1;
                    break;
                }
            }

            if (indexInferior > 0 && indexSuperior > 0)
            {
                /*Se obtiene el diametro equivalente correspondiente*/
                Interpolaciones inter = new Interpolaciones();
                inter.interpolacionDiametroEqv(indexInferior, indexSuperior, perdidaInter, flujoAireUsuario);

                OperacionesFinales(flujoAireUsuario);
            }
        }

        /****************************/

        return perdidaInter;
    }

    public Double Caso7(double flujo, double diametro) {
        double perdidaEstatica = 0;
        boolean finCiclo = false;

        for (List<ClasificacionListaPPA> listaPpa : Listas.listaPPA)
        {
            for (ClasificacionListaPPA itemLista : listaPpa)
            {
                if (diametro == itemLista.diametro && flujo == itemLista.cfm )
                {
                    perdidaEstatica = itemLista.perdida;

                    Interpolaciones inter = new Interpolaciones();

                    //Proceso para encontrar el indice de la perdida obtenida
                    int indexPerdida = -1;
                    for (ClasificasionListaPerdida itemPerd : Listas.listaPerdida)
                    {
                        if (itemPerd.perdidaTabla == perdidaEstatica)
                        {
                            //Proceso de interpolacion para obtener la velocidad de la grafica.
                            indexPerdida = itemPerd.index;
                            inter.interpolacionVelocidadMetodo2(flujo, indexPerdida);
                            break;
                        }
                    }

                    if (indexPerdida == -1)
                    {
                        //TODO: 20220304; Revisar si esto llega a ocurrir, de ser asi mejorar o colocar un mensaje al usuario
                        // en caso de que no se obtenga la velocidad.
                    }

                    if (indexPerdida > -1)
                    {
                        DimDucto_Temporal objDim = new DimDucto_Temporal();

                        //Se envia el valor del diametro introducido por el usuario a la variable diametroEqvFinal, para ser utilizados
                        //en los calculos finales.
                        inter.setDiametroEqv(objDim.edTextDiaEqv.getText().toString());
                        OperacionesFinales(flujo);
                        finCiclo = true;
                        break;
                    }
                }

                //Control para evitar revisar todos los valores de un diametro de la listaPPA
                if (diametro != itemLista.diametro)
                    break;

                //Control para no revisar toda la lista de no ser necesario
                if (itemLista.diametro > diametro)
                {
                    finCiclo = true;
                    break;
                }
            }

            if (finCiclo == true)
                break;
        }

        return perdidaEstatica;
    }

    public Double Caso8(double flujo, double diametro)
    {
        boolean finCiclo = false;

        int i=0;
        for (List<ClasificacionListaPPA> listaPpa : Listas.listaPPA)
        {
            int listaSup = -1;
            int listaInf = -1;

            int h = 0;
            for (ClasificacionListaPPA itemLista : listaPpa)
            {
                if (itemLista.diametro > diametro)
                {
                    listaSup = i;
                    listaInf = i-1;

                    Interpolaciones inter = new Interpolaciones();
                    inter.interpolacionPerdida(listaInf, listaSup, flujo, diametro);
                }

                else
                    break;

                h++;
            }

            i++;
        }

        return 27.0;
    }

    public void OperacionesFinales(double flujoAire)
    {
        Interpolaciones inter = new Interpolaciones();

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
