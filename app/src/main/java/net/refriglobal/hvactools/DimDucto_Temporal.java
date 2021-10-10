package net.refriglobal.hvactools;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.Listas;
import java.util.List;

public class DimDucto_Temporal extends AppCompatActivity
{
    Spinner condicionesAmbiente;
    TextView densidadAire, viscoCinematica, calorEspcf, factorEnergia;
    TextView diaEqvFinal, areaFlujo, velFluidoFinal, numRaynolds, perdEstaticaFinal;
    CheckBox chkCaudal, chkPerdEstatica, chkVelocidad, chkDiaEqv;
    EditText edTextCFM, edTextPerdEstatica, edTextVelocidad, edTextDiaEqv, edTextLadoADucto, edTextLadoBDucto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dim_ducto);

        condicionesAmbiente = findViewById(R.id.spinner1);
        densidadAire        = findViewById(R.id.textViewDensidadAire);
        viscoCinematica     = findViewById(R.id.TextViewViscoCinematica);
        calorEspcf          = findViewById(R.id.textViewCalorEspcf);
        factorEnergia       = findViewById(R.id.textViewFacEnergia);
        diaEqvFinal         = findViewById(R.id.textViewDiaEqvFinal);
        areaFlujo           = findViewById(R.id.textViewAreaFlujo);
        velFluidoFinal      = findViewById(R.id.textViewVelFluidoFinal);
        numRaynolds         = findViewById(R.id.textViewNumReynolds);
        perdEstaticaFinal   = findViewById(R.id.textViewPerdestaticaFinal);

        chkCaudal       = findViewById(R.id.checkBoxCaudal);
        chkPerdEstatica = findViewById(R.id.checkBoxPedEstatica);
        chkVelocidad    = findViewById(R.id.checkBoxVelocidad);
        chkDiaEqv       = findViewById(R.id.checkBoxDiaEqv);

        edTextCFM          = findViewById(R.id.editTextCFM);
        edTextPerdEstatica = findViewById(R.id.editTextPerdEstatica);
        edTextVelocidad     = findViewById(R.id.editTextVelocidad);
        edTextDiaEqv        = findViewById(R.id.editTextDiaEqv);
        edTextLadoADucto    = findViewById(R.id.editTextLadoADucto);
        edTextLadoBDucto    = findViewById(R.id.editTextLadoBDucto);

        //Seleccion de opciones del Spinner (condiciones)
        condicionesAmbiente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                //MUESTRA LA SELECCION EN UN MENSAJE TEMPORAL.
               Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                String select1 = condicionesAmbiente.getSelectedItem().toString();
                if (select1.equals("10°C(50°F) Aire 97% RH 1 atm"))
                {
                    densidadAire.setText("0.0778 ");    viscoCinematica.setText("1.5285x10ˉ⁴ ");
                    calorEspcf.setText("0.24 ");      factorEnergia.setText("1.09 ");
                }

                if (select1.equals("20°C(68°F) Aire STP"))
                {
                    densidadAire.setText("0.0752 ");    viscoCinematica.setText("1.6253x10ˉ⁴ ");
                    calorEspcf.setText("0.24 ");      factorEnergia.setText("1.08 ");
                }

                if (select1.equals("23.9°C(75°F) Aire 50% RH 1 atm"))
                {
                    densidadAire.setText("0.0739 ");    viscoCinematica.setText("1.6738x10ˉ⁴ ");
                    calorEspcf.setText("0.24 ");      factorEnergia.setText("1.05 ");
                }

                if (select1.equals("40°C(104°F) Aire 23% RH 1 atm"))
                {
                    densidadAire.setText("0.0704 ");    viscoCinematica.setText("1.8191x10ˉ⁴ ");
                    calorEspcf.setText("0.24 ");      factorEnergia.setText("1.01 ");
                }

                if (select1.equals("50°C(122°F) Aire 11% RH 1 atm"))
                {
                    densidadAire.setText("0.0682 ");    viscoCinematica.setText("1.9267x10ˉ⁴ ");
                    calorEspcf.setText("0.24 ");      factorEnergia.setText("0.96 ");
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

        chkCaudal.setClickable(false);
        chkPerdEstatica.setClickable(false);
        chkVelocidad.setClickable(true);
        chkDiaEqv.setClickable(true);
    }

    public void calcular(View view)
    {
        //double cfmPrueba = 101;
        //double perdPrueba = 0.7;

        double flujoArie, perdidaEstatica;
        flujoArie = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        perdidaEstatica = Double.parseDouble(edTextPerdEstatica.getText().toString()); //Perdida, conversion de variable.

        int indexListaPerdida = 0;
        Listas objListas = new Listas();

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
        //////------



        for (List<ClasificacionListaPPA> listaDia: Listas.listaPPA)
        {
            double diaEqvPrueba;
            if (listaDia.size() > indexListaPerdida)
            {
                if (flujoArie == listaDia.get(indexListaPerdida).cfm)
                {
                    /*Caso1: la perdida y flujo introducido por el usuario coincide con los valores obtenidos la grafica.*/
                    diaEqvPrueba = listaDia.get(indexListaPerdida).diametro;
                    edTextDiaEqv.setText(String.format("%.2f", diaEqvPrueba));
                    //TODO: 20211010; Continuar con la obtencion del valor de velocida con la clase Velocidad Tabla.
                    break;
                }

                if (listaDia.get(indexListaPerdida).cfm > flujoArie)
                {
                /*Caso2: perdida coincide, el flujo no.
                la perdida introducida por el usuario coincide con la grafica, pero flujo introducido por
                el usuario no coincide con los valores de la grafica.
                Se realizara interpolacion en el flujo de aire y diametros*/
                    break;
                }
            }
        }
    }


}
