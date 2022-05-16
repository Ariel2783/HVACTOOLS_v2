package net.refriglobal.hvactools;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.refriglobal.hvactools.ClasificacionListas.ClasificacionListaPPA;
import net.refriglobal.hvactools.ClasificacionListas.ClasificasionListaPerdida;
import net.refriglobal.hvactools.ClasificacionListas.Listas;
import net.refriglobal.hvactools.Operaciones.Calculos;
import net.refriglobal.hvactools.Operaciones.Casos;
import net.refriglobal.hvactools.Operaciones.DimRectangular;
import net.refriglobal.hvactools.Operaciones.Interpolaciones;

import java.util.List;
import java.util.Locale;

public class DimDucto_Temporal extends AppCompatActivity
{
    Spinner condicionesAmbiente;
    public TextView textViewDensidadAire, textViewViscoCinematica, textViewCalorEspcf,
                    textViewFactorEnergia;

    public TextView textViewDiaEqvFinal, textViewAreaFlujo, textViewVelFluidoFinal,
                    textViewNumReynolds, textViewPerdFricion, textViewFactorFriccion,
                    textViewPresionVelocidad;

    public TextView txtvDensidadAireUnidad, txtvViscosidadCinematicaUnidad, txtvCalorEspecifico,
                    txtvFactorEnergia, txtvCaudalUnidad, txtvWCUnidad, txtvVelUnidad, txtvDiaUnidad,
                    txtvLadoA, txtvLadoB, txtvDiaUnidad2, txtvAreaFlujoUnidad, txtvVelUnidad2,
                    txtvPresionVelUnidad, txtvWCUnidad2;

    RadioButton chkCaudal, chkPerdEstatica, chkVelocidad, chkDiaEqv;
    public EditText edTextCFM, edTextPerdEstatica, edTextVelocidad, edTextDiaEqv,
                           edTextLadoADucto, edTextLadoBDucto;

    int idCFM, idPerdida, idVelocidad, idDiaEqv;

    boolean isOpen = false;
    boolean configSI = false;
    boolean configUS = true;

    public static double viscocidadCinematica, densidadAire, calorEspecifico, factorEnergia;

    FloatingActionButton btnConfigUnidad, fabSI, fabUS;
    Animation fabAbrir, fabCerrar, fabRotarExpandir, fabRotarContraer;

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
        txtvDensidadAireUnidad = findViewById(R.id.textView7);
        txtvViscosidadCinematicaUnidad = findViewById(R.id.textView8);
        txtvCalorEspecifico = findViewById(R.id.textView9);
        txtvFactorEnergia = findViewById(R.id.textView10);
        txtvCaudalUnidad = findViewById(R.id.textView);
        txtvWCUnidad = findViewById(R.id.textView15);
        txtvVelUnidad = findViewById(R.id.textView16);
        txtvDiaUnidad = findViewById(R.id.textView17);
        txtvLadoA = findViewById(R.id.textView18);
        txtvLadoB = findViewById(R.id.textView21);
        txtvDiaUnidad2 = findViewById(R.id.textView37);
        txtvAreaFlujoUnidad = findViewById(R.id.textView38);
        txtvVelUnidad2 = findViewById(R.id.textView39);
        txtvPresionVelUnidad = findViewById(R.id.textView62);
        txtvWCUnidad2 = findViewById(R.id.textView43);


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

        btnConfigUnidad = findViewById(R.id.botonUnidad);
        fabSI = findViewById(R.id.flButton_SI);
        fabUS = findViewById(R.id.fltButton_US);

        fabAbrir = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabCerrar = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        fabRotarExpandir = AnimationUtils.loadAnimation(this,R.anim.rotacion_unidad_abrir);
        fabRotarContraer = AnimationUtils.loadAnimation(this,R.anim.rotacion_unidad_cerrar);

        chkCaudal.setChecked(true);
        chkPerdEstatica.setChecked(true);

        chkVelocidad.setChecked(false);
        edTextVelocidad.setEnabled(false);

        chkDiaEqv.setChecked(false);
        edTextDiaEqv.setEnabled(false);

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
                    textViewDensidadAire.setText("0.0778 ");    densidadAire = 0.0778;
                    textViewViscoCinematica.setText("1.5285e-4 ");  viscocidadCinematica = 1.5285e-4;
                    textViewCalorEspcf.setText("0.24 ");    calorEspecifico = 0.24;
                    textViewFactorEnergia.setText("1.09 "); factorEnergia = 1.09;
                }

                if (select1.equals("20°C(68°F) Aire STP"))
                {
                    textViewDensidadAire.setText("0.0752 ");    densidadAire = 0.0752;
                    textViewViscoCinematica.setText("1.6253e-4 ");  viscocidadCinematica = 1.6253e-4;
                    textViewCalorEspcf.setText("0.24 ");    calorEspecifico = 0.24;
                    textViewFactorEnergia.setText("1.08 "); factorEnergia = 1.08;
                }

                if (select1.equals("23.9°C(75°F) Aire 50% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0739 ");    densidadAire = 0.0739;
                    textViewViscoCinematica.setText("1.6738e-4 ");  viscocidadCinematica = 1.6738e-4;
                    textViewCalorEspcf.setText("0.24 ");    calorEspecifico = 0.24;
                    textViewFactorEnergia.setText("1.05 "); factorEnergia = 1.05;
                }

                if (select1.equals("40°C(104°F) Aire 23% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0704 ");    densidadAire = 0.0704;
                    textViewViscoCinematica.setText("1.8191e-4 ");  viscocidadCinematica = 1.8191e-4;
                    textViewCalorEspcf.setText("0.24 ");    calorEspecifico = 0.24;
                    textViewFactorEnergia.setText("1.01 "); factorEnergia = 1.01;
                }

                if (select1.equals("50°C(122°F) Aire 11% RH 1 atm"))
                {
                    textViewDensidadAire.setText("0.0682 ");    densidadAire = 0.0682;
                    textViewViscoCinematica.setText("1.9267e-4 ");  viscocidadCinematica = 1.9267e-4;
                    textViewCalorEspcf.setText("0.24 ");    calorEspecifico = 0.24;
                    textViewFactorEnergia.setText("0.96 "); factorEnergia = 0.96;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // vacio
            }
        });

        btnConfigUnidad.setOnClickListener(v -> AnimacionBoton());

        fabSI.setOnClickListener(v -> {
            AnimacionBoton();
            Toast.makeText(DimDucto_Temporal.this, "Sistema metrico", Toast.LENGTH_SHORT).show();
            configSI = true;
            configUS = false;
            cambioUnidad();

        });

        fabUS.setOnClickListener(v -> {
            AnimacionBoton();
            Toast.makeText(DimDucto_Temporal.this, "Sistema US", Toast.LENGTH_SHORT).show();
            configSI = false;
            configUS = true;
            cambioUnidad();
        });

        Listas objListas = new Listas();
        objListas.ListaPerdida();
        objListas.ListaPPA();
        objListas.ListaVelocidadPPA();
        cambioUnidad();
    }

    private void AnimacionBoton()
    {
        if (isOpen)
        {
            btnConfigUnidad.startAnimation(fabRotarExpandir);
            fabSI.startAnimation(fabCerrar);
            fabUS.startAnimation(fabCerrar);
            fabSI.setClickable(false);
            fabUS.setClickable(false);
            isOpen = false;
        }

        else
        {
            btnConfigUnidad.startAnimation(fabRotarContraer);
            fabSI.startAnimation(fabAbrir);
            fabUS.startAnimation(fabAbrir);
            fabSI.setClickable(true);
            fabUS.setClickable(true);
            isOpen = true;
        }
    }

    private void cambioUnidad()
    {
        if (configSI) //Conversion de US a Metrico.
        {
                //Unidad anterior.
                String cadenaUnidad = txtvCaudalUnidad.getText().toString();

            if (cadenaUnidad.equals("cfm") && edTextCFM.length() > 0)
            {
                double valorL_seg = Double.parseDouble(edTextCFM.getText().toString())/2.119;
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", valorL_seg));
            }

            if (cadenaUnidad.equals("cfm") && edTextPerdEstatica.length() > 0)
            {
                double valor_perd = Double.parseDouble(edTextPerdEstatica.getText().toString()) * 8.173;
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", valor_perd));
            }

            if (cadenaUnidad.equals("cfm") && edTextVelocidad.length() > 0)
            {
                double valorVelm_s = Double.parseDouble(edTextVelocidad.getText().toString()) / 196.850;
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.3f", valorVelm_s));
            }

            if (cadenaUnidad.equals("cfm") && edTextDiaEqv.length() > 0)
            {
                double valorDiamm = Double.parseDouble(edTextDiaEqv.getText().toString()) * 25.400;
                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", valorDiamm));
            }

            if (cadenaUnidad.equals("cfm") && edTextLadoADucto.length() > 0)
            {
                double valorLongmm = Double.parseDouble(edTextLadoADucto.getText().toString()) * 25.400;
                edTextLadoADucto.setText(String.format(Locale.getDefault(), "%.0f", valorLongmm));
            }

            if (cadenaUnidad.equals("cfm") && edTextLadoBDucto.length() > 0)
            {
                double valorLongmm = Double.parseDouble(edTextLadoBDucto.getText().toString()) * 25.400;
                edTextLadoBDucto.setText(String.format(Locale.getDefault(), "%.0f", valorLongmm));
            }

            if (cadenaUnidad.equals("cfm") && textViewDiaEqvFinal.length() > 0)
            {
                double valorDiaEqvmm = Double.parseDouble(textViewDiaEqvFinal.getText().toString()) * 25.400;
                textViewDiaEqvFinal.setText(String.format(Locale.getDefault(), "%.1f", valorDiaEqvmm) + " ");
            }

            if (cadenaUnidad.equals("cfm") && textViewAreaFlujo.length() > 0)
            {
                double valorAream2 = Double.parseDouble(textViewAreaFlujo.getText().toString()) / 10.764;
                textViewAreaFlujo.setText(String.format(Locale.getDefault(), "%.4f", valorAream2) + " ");
            }

            if (cadenaUnidad.equals("cfm") && textViewVelFluidoFinal.length() > 0)
            {
                double valorVelms= Double.parseDouble(textViewVelFluidoFinal.getText().toString() ) / 196.850 ;
                textViewVelFluidoFinal.setText(String.format(Locale.getDefault(), "%.3f", valorVelms) + " ");
            }

            if (cadenaUnidad.equals("cfm") && textViewPresionVelocidad.length() > 0)
            {
                double valorPresionPa = Double.parseDouble(textViewPresionVelocidad.getText().toString()) * 249.089;
                textViewPresionVelocidad.setText(String.format(Locale.getDefault(), "%.4f", valorPresionPa) + " ");
            }

            if (cadenaUnidad.equals("cfm") && textViewPerdFricion.length() > 0)
            {
                double valorPerdWCFt = Double.parseDouble(textViewPerdFricion.getText().toString()) * 8.173;
                textViewPerdFricion.setText(String.format(Locale.getDefault(), "%.4f", valorPerdWCFt) + " ");
            }

            txtvDensidadAireUnidad.setText(R.string.Kg_m3);
            txtvViscosidadCinematicaUnidad.setText(R.string.centistokes);
            txtvCalorEspecifico.setText(R.string.KJ_KgC);
            txtvFactorEnergia.setText(R.string.W_C_Ls);
            txtvCaudalUnidad.setText(R.string.Ls);
            txtvWCUnidad.setText(R.string.Pam);
            txtvVelUnidad.setText(R.string.ms);
            txtvDiaUnidad.setText(R.string.Diamm);
            txtvLadoA.setText(R.string.Diamm);
            txtvLadoB.setText(R.string.Diamm);
            txtvDiaUnidad2.setText(R.string.Diamm);
            txtvAreaFlujoUnidad.setText(R.string.m2);
            txtvVelUnidad2.setText(R.string.ms);
            txtvPresionVelUnidad.setText(R.string.Pa);
            txtvWCUnidad2.setText(R.string.Pam);

        }

        if (configUS)
        {
            String cadenaUnidad = txtvCaudalUnidad.getText().toString();

            if (edTextCFM.length() > 0 && cadenaUnidad.equals("L/s")) //Conversion de L/seg a cfm.
            {
                double valorCFM = Double.parseDouble(edTextCFM.getText().toString()) * 2.119;
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", valorCFM));
            }

            if (cadenaUnidad.equals("L/s") && edTextPerdEstatica.length() > 0)
            {
                double valor_perd = Double.parseDouble(edTextPerdEstatica.getText().toString()) / 8.173;
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", valor_perd));
            }

            if (cadenaUnidad.equals("L/s") && edTextVelocidad.length() > 0)
            {
                double valorVelm_s = Double.parseDouble(edTextVelocidad.getText().toString()) * 196.850;
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", valorVelm_s));
            }

            if (cadenaUnidad.equals("L/s") && edTextDiaEqv.length() > 0)
            {
                double valorDiamm = Double.parseDouble(edTextDiaEqv.getText().toString()) / 25.400;
                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", valorDiamm));
            }
            if (cadenaUnidad.equals("L/s") && edTextLadoADucto.length() > 0)
            {
                double valorLongPlg = Double.parseDouble(edTextLadoADucto.getText().toString()) / 25.400;
                edTextLadoADucto.setText(String.format(Locale.getDefault(), "%.1f", valorLongPlg));
            }

            if (cadenaUnidad.equals("L/s") && edTextLadoBDucto.length() > 0)
            {
                double valorLongPlg = Double.parseDouble(edTextLadoBDucto.getText().toString()) / 25.400;
                edTextLadoBDucto.setText(String.format(Locale.getDefault(), "%.1f", valorLongPlg));
            }

            if (cadenaUnidad.equals("L/s") && textViewDiaEqvFinal.length() > 0)
            {
                double valorDiaEqvPlg = Double.parseDouble(textViewDiaEqvFinal.getText().toString()) / 25.400;
                textViewDiaEqvFinal.setText(String.format(Locale.getDefault(), "%.1f", valorDiaEqvPlg) + " ");
            }

            if (cadenaUnidad.equals("L/s") && textViewAreaFlujo.length() > 0)
            {
                double valorAreaFt2 = Double.parseDouble(textViewAreaFlujo.getText().toString()) * 10.764 ;
                textViewAreaFlujo.setText(String.format(Locale.getDefault(), "%.4f", valorAreaFt2) + " ");
            }

            if (cadenaUnidad.equals("L/s") && textViewVelFluidoFinal.length() > 0)
            {
                double valorVelfpm= Double.parseDouble(textViewVelFluidoFinal.getText().toString()) * 196.850 ;
                textViewVelFluidoFinal.setText(String.format(Locale.getDefault(), "%.1f", valorVelfpm) + " ");
            }

            if (cadenaUnidad.equals("L/s") && textViewPresionVelocidad.length() > 0)
            {
                double valorPresionPlgH2O = Double.parseDouble(textViewPresionVelocidad.getText().toString()) / 249.089;
                textViewPresionVelocidad.setText(String.format(Locale.getDefault(), "%.4f", valorPresionPlgH2O) + " ");
            }

            if (cadenaUnidad.equals("L/s") && textViewPerdFricion.length() > 0)
            {
                double valorPerPa_m = Double.parseDouble(textViewPerdFricion.getText().toString()) / 8.173;
                textViewPerdFricion.setText(String.format(Locale.getDefault(), "%.3f", valorPerPa_m) + " ");
            }

            txtvDensidadAireUnidad.setText(R.string.lb_ft3);
            txtvViscosidadCinematicaUnidad.setText(R.string.lb_fth);
            txtvCalorEspecifico.setText(R.string.Btu_lbF);
            txtvFactorEnergia.setText(R.string.Btu_hFcfm);
            txtvCaudalUnidad.setText(R.string.cfm);
            txtvWCUnidad.setText(R.string.inWC_100ft);
            txtvVelUnidad.setText(R.string.fpm);
            txtvDiaUnidad.setText(R.string.in);
            txtvLadoA.setText(R.string.in);
            txtvLadoB.setText(R.string.in);
            txtvDiaUnidad2.setText(R.string.in);
            txtvAreaFlujoUnidad.setText(R.string.ft2);
            txtvVelUnidad2.setText( R.string.fpm);
            txtvPresionVelUnidad.setText(R.string.inWC);
            txtvWCUnidad2.setText(R.string.inWC_100ft);

        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void calcular(View view)
    {
        if (chkCaudal.isChecked() && chkPerdEstatica.isChecked())
        {
            if (edTextCFM.getText().length() > 0 && edTextPerdEstatica.getText().length() > 0)
                MetodoCaudalPerdEstatica();
        }

        if (chkCaudal.isChecked() && chkVelocidad.isChecked())
        {
            if (edTextCFM.getText().length() > 0 && edTextVelocidad.getText().length() > 0)
                MetodoCaudalVelocidad();
        }

        if (chkCaudal.isChecked() && chkDiaEqv.isChecked())
        {
            if (edTextCFM.getText().length() > 0 && edTextDiaEqv.getText().length() > 0)
                MetodoCaudalDiaEqv();
        }

        if (chkPerdEstatica.isChecked() && chkVelocidad.isChecked())
        {
            if (edTextPerdEstatica.getText().length() > 0 && edTextVelocidad.getText().length() > 0)
                MetodoPerdidaVelocidad();
        }

        if (chkPerdEstatica.isChecked() && chkDiaEqv.isChecked())
        {
            if (edTextPerdEstatica.getText().length() > 0 && edTextDiaEqv.getText().length() > 0)
                MetodoPerdidaDiaEqv();
        }

        if (chkVelocidad.isChecked() && chkDiaEqv.isChecked())
        {
            if (edTextVelocidad.getText().length() > 0 && edTextDiaEqv.getText().length() > 0)
                MetodoVelDiaEqv();
        }

        if (edTextLadoADucto.getText().length() > 0)
        {
            dimLadoABiseccion();
        }

    }

    public void MetodoCaudalPerdEstatica()
    {
        boolean resultadosFinales = false;
        double flujoArie, perdidaEstatica;

        flujoArie = Double.parseDouble(edTextCFM.getText().toString()); //CFM
        perdidaEstatica = Double.parseDouble(edTextPerdEstatica.getText().toString()); //Perdida in-H2O/100 Ft

        /*Se convierten los valores introducidos por el usuario en SI al sistema US
        * ya que el codigo esta escrito para operar con el sitema US*/
        if (configSI)
        {
            flujoArie = RedondearDecimal(flujoArie*2.119 ,0)  ; //CFM, conversion de variable.
            perdidaEstatica = RedondearDecimal(perdidaEstatica/8.173,2); //Perdida, conversion de variable.
        }

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setInfo = new Interpolaciones();
        setInfo.setVelocidadFlujoAire(0.00);
        setInfo.setDiametroEqv(0.00);

        //Se establecen los valores en la clase de interpolaciones.
        setInfo.setFlujoAire(flujoArie);
        setInfo.setPerdidaFinal(perdidaEstatica);

        int indexListaPerdida = -1;

        /*Obtener en este punto la posicion de la lista de perdida, de la perdida introducida por el usario*/
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

        /*Caso1: la perdida y flujo introducido por el usuario coincide con los valores obtenidos de la lista*/
        if (indexListaPerdida >= 0)
            for (List<ClasificacionListaPPA> listaDia: Listas.listaPPA)
            {
                if (listaDia.size() > indexListaPerdida)
                {
                    if (flujoArie == listaDia.get(indexListaPerdida).cfm)
                    {
                        double diaEqv = listaDia.get(indexListaPerdida).diametro;

                        Casos infoCaso = new Casos();
                        infoCaso.Caso1(diaEqv, flujoArie, indexListaPerdida);

                        //************
                        if (configSI)
                        {
                            if (!chkDiaEqv.isChecked())
                                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", (diaEqv *25.4) ));

                            if (!chkVelocidad.isChecked())
                                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.3f", (infoCaso.getVelocidadDiametro() / 196.850) ));
                        }

                        if (configUS)
                        {
                            if (!chkDiaEqv.isChecked())
                                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", diaEqv));

                            if (!chkVelocidad.isChecked())
                                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", infoCaso.getVelocidadDiametro()));
                        }

                        resultados();
                        resultadosFinales = true;
                        break;
                    }

                    if (listaDia.get(indexListaPerdida).cfm > flujoArie)
                        break;
                }
            }

        /*Caso2: perdida coincide, el flujo no.
        la perdida introducida por el usuario coincide con la grafica, pero flujo introducido por
        el usuario no coincide con los valores de la grafica.
        Se realizara interpolacion en el flujo de aire y diametros*/
        if (indexListaPerdida >= 0 && !resultadosFinales)
        {
            Casos infoCaso = new Casos();
            infoCaso.Caso2(flujoArie, indexListaPerdida);

            Interpolaciones datoInter = new Interpolaciones();

            if (configSI)
            {
                if (!chkDiaEqv.isChecked())
                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", datoInter.getDiametroEqvFinal()*25.4));

                if (!chkVelocidad.isChecked())
                    edTextVelocidad.setText(String.format(Locale.getDefault(),"%.3f", datoInter.getVelocidadFlujoAire()/ 196.850));
            }

            if (configUS)
            {
                if (!chkDiaEqv.isChecked())
                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", datoInter.getDiametroEqvFinal()));

                if (!chkVelocidad.isChecked())
                    edTextVelocidad.setText(String.format(Locale.getDefault(),"%.1f", datoInter.getVelocidadFlujoAire()));
            }


            resultados(); //muestra los resultados en los textView.
        }

        /*Caso 3: si el indice de perdida continua == -1, significa que la perdida introducida por el usuario
         * no es un valor fijo de la lista de perdida, por ende se requiere interpolacion*/
        if (indexListaPerdida == -1)
        {
            for (ClasificasionListaPerdida itemPerd: Listas.listaPerdida)
            {
                if (itemPerd.perdidaTabla < perdidaEstatica)
                {
                    int indexInferior = itemPerd.index;
                    int indexSuperior = itemPerd.index - 1;

                    Casos infoCaso = new Casos();
                    infoCaso.Caso3(indexSuperior, indexInferior, flujoArie, perdidaEstatica);

                    Interpolaciones datoInter = new Interpolaciones();

                    if (configSI)
                    {
                        if (!chkDiaEqv.isChecked())
                            edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", datoInter.getDiametroEqvFinal()*25.4));

                        if (!chkVelocidad.isChecked())
                            edTextVelocidad.setText(String.format(Locale.getDefault(),"%.3f", datoInter.getVelocidadFlujoAire()/ 196.850));
                    }

                    if (configUS)
                    {
                        if (!chkDiaEqv.isChecked())
                            edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", datoInter.getDiametroEqvFinal()));

                        if (!chkVelocidad.isChecked())
                            edTextVelocidad.setText(String.format(Locale.getDefault(),"%.1f", datoInter.getVelocidadFlujoAire()));
                    }

                    resultados(); //muestra los resultados en los textView.
                    break;
                }
            }
        }
    }

    public void MetodoCaudalVelocidad()
    {
        boolean resultadosFinales = false;

        double flujoAire = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        double velocidaUsuario = Double.parseDouble(edTextVelocidad.getText().toString());

        if (configSI)
        {
            flujoAire = RedondearDecimal(flujoAire*2.119 ,0);
            velocidaUsuario = RedondearDecimal(velocidaUsuario*196.850,1);
        }

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setInfo = new Interpolaciones();
        setInfo.setPerdidaFinal(0.00);
        setInfo.setDiametroEqv(0.00);

        //Se envia la informacion inicial del usuario a los metodos set de la variables.
        Interpolaciones setVariables = new Interpolaciones();
        setVariables.setFlujoAire(flujoAire);
        setVariables.setVelocidadFlujoAire(velocidaUsuario);

        //Caso 4: el flujo y la velocidad conciden con los valores de la lista de velocidad.
        Casos infoCaso = new Casos();
        double perdidaEstatica = infoCaso.Caso4(flujoAire, velocidaUsuario);

        if (perdidaEstatica > 0)
        {
            Interpolaciones inter = new Interpolaciones();

            if (configSI)
            {
                if (!chkPerdEstatica.isChecked())
                    edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica*8.173));

                if (!chkDiaEqv.isChecked())
                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", inter.getDiametroEqvFinal()*25.4));
            }

            if (configUS)
            {
                if (!chkPerdEstatica.isChecked())
                    edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));

                if (!chkDiaEqv.isChecked())
                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));
            }

            resultados(); //muestra los resultados en los textView.
            resultadosFinales = true;
        }

        //Caso 5: los CFM no coinciden con la lista y la velocidad si.
        if (!resultadosFinales)
        {
            perdidaEstatica = infoCaso.Caso5(flujoAire, velocidaUsuario);

            if (perdidaEstatica > 0)
            {
                Interpolaciones inter = new Interpolaciones();

                if (configSI)
                {
                    if (!chkPerdEstatica.isChecked())
                        edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica*8.173));

                    if (!chkDiaEqv.isChecked())
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", inter.getDiametroEqvFinal()*25.4));
                }

                if (configUS)
                {
                    if (!chkPerdEstatica.isChecked())
                        edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));

                    if (!chkDiaEqv.isChecked())
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));
                }

                resultados(); //muestra los resultados en los textView.
                resultadosFinales = true;
            }
        }

        //Caso 6: Los CFM coinciden o no, y la velocidad no.
        if (!resultadosFinales)
        {
            perdidaEstatica = infoCaso.Caso6(flujoAire, velocidaUsuario);
            if (perdidaEstatica > 0)
            {
                Interpolaciones inter = new Interpolaciones();

                if (configSI)
                {
                    if (!chkPerdEstatica.isChecked())
                        edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica*8.173));

                    if (!chkDiaEqv.isChecked())
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", inter.getDiametroEqvFinal()*25.4));
                }

                if (configUS)
                {
                    if (!chkPerdEstatica.isChecked())
                        edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));

                    if (!chkDiaEqv.isChecked())
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));
                }

                resultados(); //muestra los resultados en los textView.
            }
        }

        //TODO: Pendiente.
        /*Caso 7: pendiente cuando la velocidad es inferior al la velocida minima (200)
        * y superior a la maxima (12000)*/

    }

    public void MetodoCaudalDiaEqv()
    {
        boolean resultadosFinales = false;
        double flujoAire = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        double DiaEqvUsuario = Double.parseDouble(edTextDiaEqv.getText().toString());

        if (configSI)
        {
            flujoAire = RedondearDecimal(flujoAire*2.119 ,0);
            DiaEqvUsuario = RedondearDecimal(DiaEqvUsuario / 25.4,1);
        }

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setInfo = new Interpolaciones();
        setInfo.setPerdidaFinal(0.00);
        setInfo.setVelocidadFlujoAire(0.00);

        //Se envia la informacion inicial del usuario a los metodos set de la variables.
        Interpolaciones inter = new Interpolaciones();
        inter.setFlujoAire(flujoAire);
        inter.setDiametroEqv(DiaEqvUsuario);

        //Caso 7: el flujo y diametro conciden con los valores de la lista ListaPPA
        Casos infoCaso = new Casos();
        infoCaso.Caso7(flujoAire, DiaEqvUsuario);
        double perdidaEstatica = inter.getPerdidaFinal();

        if (perdidaEstatica > 0)
        {
            if (configSI)
            {
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica*8.173));
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", inter.getVelocidadFlujoAire()/196.850));
            }

            if (configUS)
            {
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", inter.getVelocidadFlujoAire()));
            }

            resultados();
            resultadosFinales = true;
        }

        //Caso 8: el diametro coincide y el caudal no coincide con los valores de la grafica
        if (!resultadosFinales)
        {
            perdidaEstatica = infoCaso.Caso8(flujoAire, DiaEqvUsuario);

            if (perdidaEstatica > 0)
                resultadosFinales = true;
        }

        //Caso 9: el diametro no coincide y el caudal no coincide con los valores de la grafica
        if (!resultadosFinales)
        {
            perdidaEstatica = infoCaso.Caso9(flujoAire, DiaEqvUsuario);

            if (perdidaEstatica > 0)
                resultadosFinales = true;
        }

        if (resultadosFinales)
        {
            if (configSI)
            {
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica*8.173));
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", inter.getVelocidadFlujoAire()/196.850));
            }

            if (configUS)
            {
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", inter.getVelocidadFlujoAire()));
            }
            resultados();
        }
    }

    public void MetodoPerdidaVelocidad()
    {
        boolean resultadosFinales = false;
        double perdidaUsuario = Double.parseDouble(edTextPerdEstatica.getText().toString());
        double velocidadUsuario = Double.parseDouble(edTextVelocidad.getText().toString());

        if (configSI)
        {
            perdidaUsuario = RedondearDecimal(perdidaUsuario/8.173,2); //Perdida, conversion de variable.
            velocidadUsuario = RedondearDecimal(velocidadUsuario*196.850,1);
        }

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setInfo = new Interpolaciones();
        setInfo.setFlujoAire(0.00);
        setInfo.setDiametroEqv(0.00);

        //Se envia la informacion inicial del usuario a los metodos set de las variables.
        setInfo.setPerdidaFinal(perdidaUsuario);
        setInfo.setVelocidadFlujoAire(velocidadUsuario);

        Casos infoCasos = new Casos();

        //Caso 10: la perdida y la veloidad coinciden con los valores de la lista de la grafica.
        infoCasos.Caso10(perdidaUsuario, velocidadUsuario);
        if (setInfo.getFlujoAire() > 0 && setInfo.getDiametroEqvFinal() > 0)
            resultadosFinales = true;

        //Caso 11: la perdida no coincide la velocidad si.
        if (!resultadosFinales)
        {
            infoCasos.Caso11(perdidaUsuario, velocidadUsuario);

            if (setInfo.getFlujoAire() > 0 && setInfo.getDiametroEqvFinal() > 0)
                resultadosFinales = true;
        }

        //Caso 12: la perdida y la velocidad no coinciden
        if (!resultadosFinales)
        {
            infoCasos.Caso12(perdidaUsuario, velocidadUsuario);
            if (setInfo.getFlujoAire() > 0 && setInfo.getDiametroEqvFinal() > 0)
                resultadosFinales = true;
        }

        if (resultadosFinales)
        {
            Interpolaciones getInfo = new Interpolaciones();

            if (configSI)
            {
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", getInfo.getFlujoAire()/2.119));
                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", getInfo.getDiametroEqvFinal()*25.4));
            }

            if (configUS)
            {
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", getInfo.getFlujoAire()));
                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.1f", getInfo.getDiametroEqvFinal()));
            }

            Casos operaciones = new Casos();
            operaciones.OperacionesFinales(getInfo.getFlujoAire());
            resultados();
        }
    }

    public void MetodoVelDiaEqv()
    {
        boolean resultadosFinales = false;
        double velocidadUsuario = Double.parseDouble(edTextVelocidad.getText().toString());
        double DiaEqvUsuario = Double.parseDouble(edTextDiaEqv.getText().toString());

        if (configSI)
        {
            velocidadUsuario = RedondearDecimal(velocidadUsuario*196.850,1);
            DiaEqvUsuario = RedondearDecimal(DiaEqvUsuario/25.4,1);
        }

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setGetInfo = new Interpolaciones();
        setGetInfo.setFlujoAire(0.00);
        setGetInfo.setPerdidaFinal(0.00);

        //Calculo de los cfm en funcion de la velocidad y diametro del usuario.
        double cfm = velocidadUsuario * (Math.PI * Math.pow((DiaEqvUsuario/24), 2));
        cfm = Math.round(cfm*100.0)/100.0;

        //Caso 7: los cfm calculados y el diametro conciden con los valores de la lista ListaPPA
        Casos infoCaso = new Casos();
        infoCaso.Caso7(cfm, DiaEqvUsuario);

        //Se restablece al valor del usuario ya que
        //en el metodo de interpolacion del caso 7 se obtiene la velocidad de la lista de la grafica.
        setGetInfo.setDiametroEqv(DiaEqvUsuario);
        setGetInfo.setVelocidadFlujoAire(velocidadUsuario);

        if (setGetInfo.getPerdidaFinal() > 0)
            resultadosFinales = true;

        if (!resultadosFinales)
        {
            //Caso 13: los cfm calculados no coinciden con la lista y el diametro si concide con los valores de la lista ListaPPA
            infoCaso.Caso13(cfm, DiaEqvUsuario);
            if (setGetInfo.getPerdidaFinal() > 0)
                resultadosFinales = true;
        }

        if (!resultadosFinales)
        {
            //Caso 14: los cfm calculados y el diametro no concide con los valores de la lista ListaPPA
            infoCaso.Caso14(cfm, velocidadUsuario, DiaEqvUsuario);
            if (setGetInfo.getPerdidaFinal() > 0)
                resultadosFinales = true;
        }

        if (resultadosFinales)
        {
            if (configSI)
            {
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", cfm/2.119));
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", setGetInfo.getPerdidaFinal()*8.173));
            }

            if (configUS)
            {
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", cfm));
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", setGetInfo.getPerdidaFinal()));
            }

            Casos operaciones = new Casos();
            operaciones.OperacionesFinales(cfm);
            resultados();
        }
    }

    public void MetodoPerdidaDiaEqv()
    {
        boolean resultadosFinales = false;
        double perdidaUsuario = Double.parseDouble(edTextPerdEstatica.getText().toString());
        double DiaEqvUsuario = Double.parseDouble(edTextDiaEqv.getText().toString());

        if (configSI)
        {
            perdidaUsuario = RedondearDecimal(perdidaUsuario/8.173,2); //Perdida, conversion de variable.
            DiaEqvUsuario = RedondearDecimal(DiaEqvUsuario / 25.4,1);
        }

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setGetInfo = new Interpolaciones();
        setGetInfo.setFlujoAire(0.00);
        setGetInfo.setVelocidadFlujoAire(0.00);

        //Se envia la informacion inicial del usuario a los metodos set de las variables.
        setGetInfo.setPerdidaFinal(perdidaUsuario);
        setGetInfo.setDiametroEqv(DiaEqvUsuario);

        //Caso 15: La perdida y el diametro coinciden con los valores de la lista.
        Casos infoCasos = new Casos();
        infoCasos.Caso15(perdidaUsuario, DiaEqvUsuario);
        if (setGetInfo.getFlujoAire() > 0 && setGetInfo.getVelocidadFlujoAire() > 0)
            resultadosFinales = true;

        //Caso16: la perdida no coincide el diametro si.
        if (!resultadosFinales)
        {
            infoCasos.Caso16(perdidaUsuario, DiaEqvUsuario);

            if (setGetInfo.getFlujoAire() > 0 && setGetInfo.getVelocidadFlujoAire() > 0)
                resultadosFinales = true;
        }

        //Caso17: la perdida coincide el diametro no.
        if (!resultadosFinales)
        {
            infoCasos.Caso17(perdidaUsuario, DiaEqvUsuario);

            if (setGetInfo.getFlujoAire() > 0 && setGetInfo.getVelocidadFlujoAire() > 0)
                resultadosFinales = true;
        }

        //Caso18: la perdida no coincide y el diametro tampoco.
        if (!resultadosFinales)
        {
            infoCasos.Caso18(perdidaUsuario, DiaEqvUsuario);

            if (setGetInfo.getFlujoAire() > 0 && setGetInfo.getVelocidadFlujoAire() > 0)
                resultadosFinales = true;
        }

        if (resultadosFinales)
        {
            if (configSI)
            {
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", setGetInfo.getFlujoAire()/2.119));
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", setGetInfo.getVelocidadFlujoAire()/196.850));
            }

            if (configUS)
            {
                edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", setGetInfo.getFlujoAire()));
                edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", setGetInfo.getVelocidadFlujoAire()));
            }

            Casos operaciones = new Casos();
            operaciones.OperacionesFinales(setGetInfo.getFlujoAire());
            resultados();
        }
    }

    public void resultados()
    {
        Calculos operacion = new Calculos();

        if (configUS)
        {
            textViewAreaFlujo.setText(String.format(Locale.getDefault(),"%.4f", operacion.getAreaDiametroEqv())+" ");
            textViewVelFluidoFinal.setText(String.format(Locale.getDefault(), "%.1f",operacion.getVelocidadDiametro())+" ");
            textViewNumReynolds.setText(String.format(Locale.getDefault(), "%.0f", operacion.getNumeroReynolds()));
            textViewFactorFriccion.setText(String.format(Locale.getDefault(),"%.5f", operacion.getFactorFriccion()));
            textViewPresionVelocidad.setText(String.format(Locale.getDefault(),"%.4f",operacion.getPresionVelocidad())+" ");
            textViewPerdFricion.setText(String.format(Locale.getDefault(),"%.4f", operacion.getPerdidaFriccion())+" ");
        }

        if (configSI)
        {
            textViewAreaFlujo.setText(String.format(Locale.getDefault(),"%.4f", operacion.getAreaDiametroEqv() / 10.764)+" ");
            textViewVelFluidoFinal.setText(String.format(Locale.getDefault(), "%.1f",operacion.getVelocidadDiametro() / 196.850)+" ");
            textViewNumReynolds.setText(String.format(Locale.getDefault(), "%.0f", operacion.getNumeroReynolds()));
            textViewFactorFriccion.setText(String.format(Locale.getDefault(),"%.5f", operacion.getFactorFriccion()));
            textViewPresionVelocidad.setText(String.format(Locale.getDefault(),"%.4f",operacion.getPresionVelocidad() * 249.089)+" ");
            textViewPerdFricion.setText(String.format(Locale.getDefault(),"%.4f", operacion.getPerdidaFriccion() * 8.173)+" ");
        }
    }

    public static Double RedondearDecimal(double valor, int numDecimales)
    {
        double parteEntera, resultado;
        resultado = valor;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado/Math.pow(10, numDecimales)) + parteEntera;

        return resultado;
    }

    public void chkCaudalClick(View view)
    {
        if (idCFM == 0)
        {
            idCFM = 1;
            edTextCFM.setEnabled(true);

            if (idPerdida == 2)
            {
                edTextPerdEstatica.setText("");
                edTextPerdEstatica.setEnabled(false);
                chkPerdEstatica.setChecked(false);
                idPerdida = 0;
            }

            if (idVelocidad == 2)
            {
                edTextVelocidad.setText("");
                edTextVelocidad.setEnabled(false);
                chkVelocidad.setChecked(false);
                idVelocidad = 0;
            }

            if (idDiaEqv == 2)
            {
                edTextDiaEqv.setText("");
                edTextDiaEqv.setEnabled(false);
                chkDiaEqv.setChecked(false);
                idDiaEqv = 0;
            }

            if (idPerdida == 1)
            {
                idPerdida = 2;
                edTextPerdEstatica.setEnabled(true);
            }


            if (idVelocidad == 1)
            {
                idVelocidad = 2;
                edTextVelocidad.setEnabled(true);
            }

            if (idDiaEqv == 1)
            {
                idDiaEqv = 2;
                edTextDiaEqv.setEnabled(true);
            }

            if (edTextCFM.length() > 0)
                edTextCFM.setText("");
        }
    }

    public void chkPerdEstaticaClick(View view)
    {
        if (idPerdida == 0)
        {
            idPerdida = 1;
            edTextPerdEstatica.setEnabled(true);

            if (idCFM == 2)
            {
                edTextCFM.setText("");
                edTextCFM.setEnabled(false);
                chkCaudal.setChecked(false);
                idCFM = 0;
            }

            if (idVelocidad == 2)
            {
                edTextVelocidad.setText("");
                edTextVelocidad.setEnabled(false);
                chkVelocidad.setChecked(false);
                idVelocidad = 0;
            }

            if (idDiaEqv == 2)
            {
                edTextDiaEqv.setText("");
                edTextDiaEqv.setEnabled(false);
                chkDiaEqv.setChecked(false);
                idDiaEqv = 0;
            }

            if (idCFM == 1)
            {
                edTextCFM.setEnabled(true);
                idCFM = 2;
            }

            if (idVelocidad == 1)
            {
                edTextVelocidad.setEnabled(true);
                idVelocidad = 2;
            }

            if (idDiaEqv == 1)
            {
                edTextDiaEqv.setEnabled(true);
                idDiaEqv = 2;
            }

            if (edTextPerdEstatica.length() > 0)
                edTextPerdEstatica.setText("");
        }
    }

    public void chkVelocidadClick(View view)
    {
        if (idVelocidad == 0)
        {
            idVelocidad = 1;
            edTextVelocidad.setEnabled(true);

            if (idCFM == 2)
            {
                edTextCFM.setText("");
                edTextCFM.setEnabled(false);
                chkCaudal.setChecked(false);
                idCFM = 0;
            }

            if (idPerdida == 2)
            {
                edTextPerdEstatica.setText("");
                edTextPerdEstatica.setEnabled(false);
                chkPerdEstatica.setChecked(false);
                idPerdida = 0;
            }

            if (idDiaEqv == 2)
            {
                edTextDiaEqv.setText("");
                edTextDiaEqv.setEnabled(false);
                chkDiaEqv.setChecked(false);
                idDiaEqv = 0;
            }

            if (idCFM == 1)
            {
                idCFM = 2;
                edTextCFM.setEnabled(true);
            }

            if (idPerdida == 1)
            {
                idPerdida = 2;
                edTextPerdEstatica.setEnabled(true);
            }

            if (idDiaEqv == 1)
            {
                idDiaEqv = 2;
                edTextDiaEqv.setEnabled(true);
            }

            if (edTextVelocidad.length() > 0)
                edTextVelocidad.setText("");
        }
    }

    public void chkDiaEqvClick(View view)
    {
        if (idDiaEqv == 0)
        {
            idDiaEqv = 1;
            edTextDiaEqv.setEnabled(true);

            if (idCFM == 2)
            {
                edTextCFM.setText("");
                edTextCFM.setEnabled(false);
                chkCaudal.setChecked(false);
                idCFM = 0;
            }

            if (idPerdida == 2)
            {
                edTextPerdEstatica.setText("");
                edTextPerdEstatica.setEnabled(false);
                chkPerdEstatica.setChecked(false);
                idPerdida = 0;
            }

            if (idVelocidad == 2)
            {
                edTextVelocidad.setText("");
                edTextVelocidad.setEnabled(false);
                chkVelocidad.setChecked(false);
                idVelocidad = 0;
            }

            if (idCFM == 1)
            {
                idCFM = 2;
                edTextCFM.setEnabled(true);
            }

            if (idPerdida == 1)
            {
                idPerdida = 2;
                edTextPerdEstatica.setEnabled(true);
            }

            if (idVelocidad == 1)
            {
                idVelocidad = 2;
                edTextVelocidad.setEnabled(true);
            }

            if (edTextDiaEqv.length() > 0)
                edTextDiaEqv.setText("");
        }
    }

    public void dim1(View view)
    {
        edTextLadoBDucto.setText("");
        dimLadoABiseccion();
    }

    public void dimLadoABiseccion()
    {
        double ladoA = Double.parseDouble(edTextLadoADucto.getText().toString());
        DimRectangular objDim = new DimRectangular();
        objDim.dimensionLadoBBiseccion(ladoA);

        double ladoB = objDim.getLadoB();

        /*Calculo del diametro equivalente*/
        if (ladoA > 0 && ladoB > 0)
        {
            edTextLadoADucto.setText(String.format(Locale.getDefault(), "%.1f", ladoA));
            edTextLadoBDucto.setText(String.format(Locale.getDefault(), "%.1f", ladoB));

            //2009 ASHRAE Handbook Fundamentals, CHAPTER 21, pg - 21.7, eq - 25
            double diaEqvCal = (1.3 * Math.pow(ladoA*ladoB,0.625))/Math.pow((ladoA + ladoB),0.25);
            textViewDiaEqvFinal.setText(String.format(Locale.getDefault(),"%.2f", diaEqvCal) + " ");

            Interpolaciones interSetGet = new Interpolaciones();

            Casos operaciones = new Casos();
            operaciones.OperacionesFinales(interSetGet.getFlujoAire());
            resultados();
        }
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
