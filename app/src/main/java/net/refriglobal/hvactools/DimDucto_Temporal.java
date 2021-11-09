package net.refriglobal.hvactools;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaVelocidad;
import net.refriglobal.hvactools.ClasificacionListas.ClasificasionListaPerdida;
import net.refriglobal.hvactools.ClasificacionListas.Listas;
import net.refriglobal.hvactools.Operaciones.Calculos;
import net.refriglobal.hvactools.Operaciones.Interpolaciones;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class DimDucto_Temporal extends AppCompatActivity
{
    Spinner condicionesAmbiente;
    TextView textViewDensidadAire, textViewViscoCinematica, textViewCalorEspcf, textViewFactorEnergia;
    TextView textViewDiaEqvFinal, textViewAreaFlujo, textViewVelFluidoFinal, textViewNumReynolds, textViewPerdFricion, textViewFactorFriccion,
             textViewPresionVelocidad;
    RadioButton chkCaudal, chkPerdEstatica, chkVelocidad, chkDiaEqv;
    EditText edTextCFM, edTextPerdEstatica, edTextVelocidad, edTextDiaEqv, edTextLadoADucto, edTextLadoBDucto;
    int idCFM, idPerdida, idVelocidad, idDiaEqv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dim_ducto);

        condicionesAmbiente = findViewById(R.id.spinner1);
        textViewDensidadAire        = findViewById(R.id.textViewDensidadAire);
        textViewViscoCinematica     = findViewById(R.id.TextViewViscoCinematica);
        textViewCalorEspcf          = findViewById(R.id.textViewCalorEspcf);
        textViewFactorEnergia       = findViewById(R.id.textViewFacEnergia);
        textViewDiaEqvFinal         = findViewById(R.id.textViewDiaEqvFinal);
        textViewAreaFlujo           = findViewById(R.id.textViewAreaFlujo);
        textViewVelFluidoFinal      = findViewById(R.id.textViewVelFluidoFinal);
        textViewNumReynolds         = findViewById(R.id.textViewNumReynolds);
        textViewPerdFricion   = findViewById(R.id.textViewPerdidaFriccion);
        textViewFactorFriccion = findViewById(R.id.textViewFactorFriccion);
        textViewPresionVelocidad = findViewById(R.id.textViewPresionVelocidad);

        chkCaudal       = findViewById(R.id.RadioButtonCaudal);
        chkPerdEstatica = findViewById(R.id.RadioButtonPedEstatica);
        chkVelocidad    = findViewById(R.id.RadioButtonVelocidad);
        chkDiaEqv       = findViewById(R.id.RadioButtonDiaEqv);

        edTextCFM          = findViewById(R.id.editTextCFM);
        edTextPerdEstatica = findViewById(R.id.editTextPerdEstatica);
        edTextVelocidad     = findViewById(R.id.editTextVelocidad);
        edTextDiaEqv        = findViewById(R.id.editTextDiaEqv);
        edTextLadoADucto    = findViewById(R.id.editTextLadoADucto);
        edTextLadoBDucto    = findViewById(R.id.editTextLadoBDucto);

        chkCaudal.setChecked(true);
        chkPerdEstatica.setChecked(true);
        chkVelocidad .setChecked(false);
        chkDiaEqv.setChecked(false);

        idCFM = 1;
        idPerdida = 2;
        idVelocidad = 0;
        idDiaEqv = 0;

        //Seleccion de opciones del Spinner (condiciones)
        condicionesAmbiente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                //MUESTRA LA SELECCION EN UN MENSAJE TEMPORAL.
               Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                String select1 = condicionesAmbiente.getSelectedItem().toString();
                if (select1.equals("10°C(50°F) Aire 97% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0778 ");    textViewViscoCinematica.setText("1.5285e-4 ");
                    textViewCalorEspcf.setText("0.24 ");      textViewFactorEnergia.setText("1.09 ");
                }

                if (select1.equals("20°C(68°F) Aire STP"))
                {
                    textViewDensidadAire.setText("0.0752 ");    textViewViscoCinematica.setText("1.6253e-4 ");
                    textViewCalorEspcf.setText("0.24 ");      textViewFactorEnergia.setText("1.08 ");
                }

                if (select1.equals("23.9°C(75°F) Aire 50% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0739 ");    textViewViscoCinematica.setText("1.6738e-4 ");
                    textViewCalorEspcf.setText("0.24 ");      textViewFactorEnergia.setText("1.05 ");
                }

                if (select1.equals("40°C(104°F) Aire 23% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0704 ");    textViewViscoCinematica.setText("1.8191e-4 ");
                    textViewCalorEspcf.setText("0.24 ");      textViewFactorEnergia.setText("1.01 ");
                }

                if (select1.equals("50°C(122°F) Aire 11% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0682 ");    textViewViscoCinematica.setText("1.9267e-4 ");
                    textViewCalorEspcf.setText("0.24 ");      textViewFactorEnergia.setText("0.96 ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // vacio
            }
        });

        Listas objListas = new Listas();
        objListas.ListaPerdida();
        objListas.ListaPPA();
        objListas.ListaVelocidadPPA();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void calcular(View view)
    {
        double flujoArie, perdidaEstatica;
        flujoArie = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        perdidaEstatica = Double.parseDouble(edTextPerdEstatica.getText().toString()); //Perdida, conversion de variable.

        int indexListaPerdida = -1;

        /*Obtener en este punto la posicion del arreglo de perdida, de la perdida introducida por el usario*/
       int i = 0;
        while (i < Listas.listaPerdida.size())
        {
            if (perdidaEstatica == Listas.listaPerdida.get(i).perdidaTabla)
            {
                indexListaPerdida = i;
                break;
            }
            i++;
        }

        /*Caso1: la perdida y flujo introducido por el usuario coincide con los valores obtenidos la grafica.*/
        if (indexListaPerdida >= 0)
            for (List<ClasificacionListaPPA> listaDia: Listas.listaPPA)
            {
                if (listaDia.size() > indexListaPerdida)
                {
                    if (flujoArie == listaDia.get(indexListaPerdida).cfm)
                    {
                        double diaEqvPrueba;
                        diaEqvPrueba = listaDia.get(indexListaPerdida).diametro;
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", diaEqvPrueba));

                        //Valor de velocidad del grafico
                        for (List<ClasificacionListaVelocidad> listaVel:Listas.listaVelocidadPPA)
                        {
                            if (flujoArie == listaVel.get(indexListaPerdida).cfm)
                            {
                                double velocidad;
                                velocidad = listaVel.get(indexListaPerdida).velocidad;
                                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", velocidad));
                                //TODO: Pendiente continuar con el resto de los calculos.
                                break;
                            }
                        }
                        break;
                    }

                    if (flujoArie < listaDia.get(indexListaPerdida).cfm)
                    {
                    /*Caso2: perdida coincide, el flujo no.
                    la perdida introducida por el usuario coincide con la grafica, pero flujo introducido por
                    el usuario no coincide con los valores de la grafica.
                    Se realizara interpolacion en el flujo de aire y diametros*/
                        Interpolaciones objInterpolacion = new Interpolaciones();

                        objInterpolacion.interpolacionDiametroEqvMetodo2(indexListaPerdida, flujoArie);
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", objInterpolacion.getDiametroEqvFinal()));

                        objInterpolacion.interpolacionVelocidadMetodo2(flujoArie,indexListaPerdida);
                        edTextVelocidad.setText(String.format(Locale.getDefault(),"%.1f", objInterpolacion.getVelocidadFlujoAire()));

                        Calculos operacion = new Calculos();
                        operacion.calculoArea(objInterpolacion.getDiametroEqvFinal());

                        operacion.calculoVelDiaEqv(flujoArie);

                        double valorViscoCinematica = Double.parseDouble(textViewViscoCinematica.getText().toString());
                        operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), objInterpolacion.getDiametroEqvFinal(), valorViscoCinematica);

                        operacion.calculoFactorFriccion(objInterpolacion.getDiametroEqvFinal());

                        double aireDensidad = Double.parseDouble(textViewDensidadAire.getText().toString());
                        operacion.calculoPresionVelocidad(aireDensidad);

                        operacion.calculoPerdidaFriccion(objInterpolacion.getDiametroEqvFinal());

                        resultados(); //muestra los resultados en los textView.
                        break;
                    }
                }
            }

        //-------------

        /*Si el indice de perdida continua == -1, significa que la perdida introducida por el usuario
        * no es un valor fijo de la lista de perdida, por ende se requiere interpolacion*/
        if (indexListaPerdida == -1)
        {
            for (ClasificasionListaPerdida itemPerd: Listas.listaPerdida)
            {
                if (itemPerd.perdidaTabla < perdidaEstatica)
                {
                    int indexInferior = itemPerd.index;
                    int indexSuperior = itemPerd.index - 1;

                    Interpolaciones inter = new Interpolaciones();
                    inter.interpolacionDiametroEqv(indexInferior, indexSuperior, perdidaEstatica, flujoArie);

                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));

                    Calculos operacion = new Calculos();
                    operacion.calculoArea(inter.getDiametroEqvFinal());

                    inter.interpolacionVelocidad(indexInferior, indexSuperior, flujoArie, perdidaEstatica);
                    edTextVelocidad.setText(String.format(Locale.getDefault(),"%.1f", inter.getVelocidadFlujoAire()));

                    operacion.calculoVelDiaEqv(flujoArie);

                    double valorViscoCinematica = Double.parseDouble(textViewViscoCinematica.getText().toString());
                    operacion.calculoNumeroReynolds(operacion.getVelocidadDiametro(), inter.getDiametroEqvFinal(), valorViscoCinematica);

                    operacion.calculoFactorFriccion(inter.getDiametroEqvFinal());

                    double aireDensidad = Double.parseDouble(textViewDensidadAire.getText().toString());
                    operacion.calculoPresionVelocidad(aireDensidad);

                    operacion.calculoPerdidaFriccion(inter.getDiametroEqvFinal());

                    resultados(); //muestra los resultados en los textView.

                    break;
                }
            }
        }
        //////------
    }

    public void resultados()
    {
        Calculos operacion = new Calculos();
        DecimalFormat formatoMilDecimal1 = new DecimalFormat("###,###.#");
        DecimalFormat formatoMil = new DecimalFormat("###,###");
        textViewAreaFlujo.setText(String.format(Locale.getDefault(),"%.4f", operacion.getAreaDiametroEqv())+" ");
        textViewVelFluidoFinal.setText(formatoMilDecimal1.format(operacion.getVelocidadDiametro())+" ");
        textViewNumReynolds.setText(formatoMil.format(operacion.getNumeroReynolds()));
        textViewFactorFriccion.setText(String.format(Locale.getDefault(),"%.5f", operacion.getFactorFriccion()));
        textViewPresionVelocidad.setText(String.format(Locale.getDefault(),"%.4f",operacion.getPresionVelocidad())+" ");
        textViewPerdFricion.setText(String.format(Locale.getDefault(),"%.4f", operacion.getPerdidaFriccion())+" ");
    }

    public void checkCFM(View view)
    {
        if (idCFM == 0)
        {
            idCFM = 1;

            if (idPerdida == 2)
            {
                edTextPerdEstatica.setText("");
                chkPerdEstatica.setChecked(false);
                idPerdida = 0;
            }

            if (idVelocidad == 2)
            {
                edTextVelocidad.setText("");
                chkVelocidad.setChecked(false);
                idVelocidad = 0;
            }

            if (idDiaEqv == 2)
            {
                edTextDiaEqv.setText("");
                chkDiaEqv.setChecked(false);
                idDiaEqv = 0;
            }

            if (idPerdida == 1)
                idPerdida = 2;

            if (idVelocidad == 1)
                idVelocidad = 2;

            if (idDiaEqv == 1)
                idDiaEqv = 2;

            if (edTextCFM.length() > 0)
                edTextCFM.setText("");
        }
    }

    public void checkPerdidaFriccion(View view)
    {
        if (idPerdida == 0)
        {
            idPerdida = 1;

            if (idCFM == 2)
            {
                edTextCFM.setText("");
                chkCaudal.setChecked(false);
                idCFM = 0;
            }

            if (idVelocidad == 2)
            {
                edTextVelocidad.setText("");
                chkVelocidad.setChecked(false);
                idVelocidad = 0;
            }

            if (idDiaEqv == 2)
            {
                edTextDiaEqv.setText("");
                chkDiaEqv.setChecked(false);
                idDiaEqv = 0;
            }

            if (idCFM == 1)
                idCFM = 2;

            if (idVelocidad == 1)
                idVelocidad = 2;

            if (idDiaEqv == 1)
                idDiaEqv = 2;

            if (edTextPerdEstatica.length() > 0)
                edTextPerdEstatica.setText("");
        }
    }

    public void checkVelocidad(View view)
    {
        if (idVelocidad == 0)
        {
            idVelocidad = 1;

            if (idCFM == 2)
            {
                edTextCFM.setText("");
                chkCaudal.setChecked(false);
                idCFM = 0;
            }

            if (idPerdida == 2)
            {
                edTextPerdEstatica.setText("");
                chkPerdEstatica.setChecked(false);
                idPerdida = 0;
            }

            if (idDiaEqv == 2)
            {
                edTextDiaEqv.setText("");
                chkDiaEqv.setChecked(false);
                idDiaEqv = 0;
            }

            if (idCFM == 1)
                idCFM = 2;

            if (idPerdida == 1)
                idPerdida = 2;

            if (idDiaEqv == 1)
                idDiaEqv = 2;

            if (edTextVelocidad.length() > 0)
                edTextVelocidad.setText("");
        }
    }

    public void checkDiametroEqv(View view)
    {
        if (idDiaEqv == 0)
        {
            idDiaEqv = 1;

            if (idCFM == 2)
            {
                edTextCFM.setText("");
                chkCaudal.setChecked(false);
                idCFM = 0;
            }

            if (idPerdida == 2)
            {
                edTextPerdEstatica.setText("");
                chkPerdEstatica.setChecked(false);
                idPerdida = 0;
            }

            if (idVelocidad == 2)
            {
                edTextVelocidad.setText("");
                chkVelocidad.setChecked(false);
                idVelocidad = 0;
            }

            if (idCFM == 1)
                idCFM = 2;

            if (idPerdida == 1)
                idPerdida = 2;

            if (idVelocidad == 1)
                idVelocidad = 2;

            if (edTextDiaEqv.length() > 0)
                edTextDiaEqv.setText("");
        }
    }

    public void dim1(View view)
    {
    }

    public void dim2(View view)
    {
    }

    public void borrar(View view)
    {
        edTextCFM.setText("");
        edTextPerdEstatica.setText("");
        edTextVelocidad.setText("");
        edTextDiaEqv.setText("");
        edTextLadoADucto.setText("");
        edTextLadoBDucto.setText("");
        textViewDiaEqvFinal.setText("");
        textViewAreaFlujo.setText("");
        textViewVelFluidoFinal.setText("");
        textViewNumReynolds.setText("");
        textViewFactorFriccion.setText("");
        textViewPresionVelocidad.setText("");
        textViewPerdFricion.setText("");
    }
}
