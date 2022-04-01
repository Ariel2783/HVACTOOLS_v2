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
import net.refriglobal.hvactools.Operaciones.Casos;
import net.refriglobal.hvactools.Operaciones.Interpolaciones;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class DimDucto_Temporal extends AppCompatActivity
{
    Spinner condicionesAmbiente;
    public static TextView textViewDensidadAire, textViewViscoCinematica, textViewCalorEspcf, textViewFactorEnergia;
    public static TextView textViewDiaEqvFinal, textViewAreaFlujo, textViewVelFluidoFinal, textViewNumReynolds, textViewPerdFricion, textViewFactorFriccion,
             textViewPresionVelocidad;
    RadioButton chkCaudal, chkPerdEstatica, chkVelocidad, chkDiaEqv;
    public static EditText edTextCFM, edTextPerdEstatica, edTextVelocidad, edTextDiaEqv, edTextLadoADucto, edTextLadoBDucto;
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
        if (chkCaudal.isChecked() == true && chkPerdEstatica.isChecked() == true)
        {
            if (edTextCFM.getText().length() > 0 && edTextPerdEstatica.getText().length() > 0)
                MetodoCaudalPerdEstatica();
        }

        if (chkCaudal.isChecked() == true && chkVelocidad.isChecked() == true)
        {
            if (edTextCFM.getText().length() > 0 && edTextVelocidad.getText().length() > 0)
                MetodoCaudalVelocidad();
        }

        if (chkCaudal.isChecked() == true && chkDiaEqv.isChecked() == true)
        {
            if (edTextCFM.getText().length() > 0 && edTextDiaEqv.getText().length() > 0)
                MetodoCaudalDiaEqv();
        }

        if (chkPerdEstatica.isChecked() == true && chkVelocidad.isChecked() == true)
        {
            if (edTextPerdEstatica.getText().length() > 0 && edTextVelocidad.getText().length() > 0)
                MetodoPerdidaVelocidad();
        }

        if (chkPerdEstatica.isChecked() == true && chkDiaEqv.isChecked() == true)
        {
        }

        if (chkVelocidad.isChecked() == true && chkDiaEqv.isChecked() == true)
        {
            if (edTextVelocidad.getText().length() > 0 && edTextDiaEqv.getText().length() > 0)
                MetodoVelDiaEqv();
        }
    }

    public void MetodoCaudalPerdEstatica()
    {
        boolean resultadosFinales = false;
        double flujoArie, perdidaEstatica;

        flujoArie = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        perdidaEstatica = Double.parseDouble(edTextPerdEstatica.getText().toString()); //Perdida, conversion de variable.

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setInfo = new Interpolaciones();
        setInfo.setVelocidadFlujoAire(0.00);
        setInfo.setDiametroEqv(0.00);

        int indexListaPerdida = -1;

        /**Obtener en este punto la posicion de la lista de perdida, de la perdida introducida por el usario*/
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

        /**Caso1: la perdida y flujo introducido por el usuario coincide con los valores obtenidos de la lista*/
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

                        if (chkDiaEqv.isChecked() == false)
                            edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", diaEqv));

                        if (chkVelocidad.isChecked() == false)
                            edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", infoCaso.getVelocidadDiametro()));

                        resultados();
                        resultadosFinales = true;
                        break;
                    }

                    if (listaDia.get(indexListaPerdida).cfm > flujoArie)
                        break;
                }
            }

        /**Caso2: perdida coincide, el flujo no.
        la perdida introducida por el usuario coincide con la grafica, pero flujo introducido por
        el usuario no coincide con los valores de la grafica.
        Se realizara interpolacion en el flujo de aire y diametros*/
        if (indexListaPerdida >= 0 && resultadosFinales == false)
        {
            Casos infoCaso = new Casos();
            infoCaso.Caso2(flujoArie, indexListaPerdida);

            Interpolaciones datoInter = new Interpolaciones();

            if (chkDiaEqv.isChecked() == false)
                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", datoInter.getDiametroEqvFinal()));

            if (chkVelocidad.isChecked() == false)
                edTextVelocidad.setText(String.format(Locale.getDefault(),"%.1f", datoInter.getVelocidadFlujoAire()));

            resultados(); //muestra los resultados en los textView.
            resultadosFinales = true;
        }

        /**Caso 3: si el indice de perdida continua == -1, significa que la perdida introducida por el usuario
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

                    if (chkDiaEqv.isChecked() == false)
                        edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", datoInter.getDiametroEqvFinal()));

                    if (chkVelocidad.isChecked() == false)
                        edTextVelocidad.setText(String.format(Locale.getDefault(),"%.1f", datoInter.getVelocidadFlujoAire()));

                    resultados(); //muestra los resultados en los textView.
                    resultadosFinales = true;

                    break;
                }
            }
        }
    }

    public void MetodoCaudalVelocidad()
    {
        double perdidaEstatica = 0;
        boolean resultadosFinales = false;
        double flujoAire = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        double velocidaUsuario = Double.parseDouble(edTextVelocidad.getText().toString());

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
        perdidaEstatica = infoCaso.Caso4(flujoAire, velocidaUsuario);

        if (perdidaEstatica > 0)
        {
            if (chkPerdEstatica.isChecked() == false)
                edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));

            Interpolaciones inter = new Interpolaciones();

            if (chkDiaEqv.isChecked() == false)
                edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));

            resultados(); //muestra los resultados en los textView.
            resultadosFinales = true;
        }

        //Caso 5: los CFM no coinciden con la lista y la velocidad si.
        if (resultadosFinales == false)
        {
            perdidaEstatica = infoCaso.Caso5(flujoAire, velocidaUsuario);

            if (perdidaEstatica > 0)
            {
                if (chkPerdEstatica.isChecked() == false)
                    edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));

                Interpolaciones inter = new Interpolaciones();

                if (chkDiaEqv.isChecked() == false)
                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));

                resultados(); //muestra los resultados en los textView.
                resultadosFinales = true;
            }
        }

        //Caso 6: Los CFM coinciden o no, y la velocidad no.
        if (resultadosFinales == false)
        {
            perdidaEstatica = infoCaso.Caso6(flujoAire, velocidaUsuario);
            if (perdidaEstatica > 0)
            {
                if (chkPerdEstatica.isChecked() == false)
                    edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));

                Interpolaciones inter = new Interpolaciones();

                if (chkDiaEqv.isChecked() == false)
                    edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", inter.getDiametroEqvFinal()));

                resultados(); //muestra los resultados en los textView.
                resultadosFinales = true;
            }
        }

        /*Caso 7: pendiente cuando la velocidad es inferior al la velocida minima (200)
        * y superior a la maxima (12000)*/

    }

    public void MetodoCaudalDiaEqv()
    {
        double perdidaEstatica = 0;
        boolean resultadosFinales = false;
        double flujoAire = Double.parseDouble(edTextCFM.getText().toString()); //CFM, conversion de variable.
        double DiaEqvUsuario = Double.parseDouble(edTextDiaEqv.getText().toString());

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
        perdidaEstatica = inter.getPerdidaFinal();

        if (perdidaEstatica > 0)
        {
            edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));
            edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", inter.getVelocidadFlujoAire()));
            resultados();
            resultadosFinales = true;
        }

        //Caso 8: el diametro coincide y el caudal no coincide con los valores de la grafica
        if (resultadosFinales == false)
        {
            perdidaEstatica = infoCaso.Caso8(flujoAire, DiaEqvUsuario);

            if (perdidaEstatica > 0)
                resultadosFinales = true;
        }

        //Caso 9: el diametro no coincide y el caudal no coincide con los valores de la grafica
        if (resultadosFinales == false)
        {
            perdidaEstatica = infoCaso.Caso9(flujoAire, DiaEqvUsuario);

            if (perdidaEstatica > 0)
                resultadosFinales = true;
        }

        if (resultadosFinales == true)
        {
            edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));
            edTextVelocidad.setText(String.format(Locale.getDefault(), "%.1f", inter.getVelocidadFlujoAire()));
            resultados();
        }
    }

    public void MetodoPerdidaVelocidad()
    {
        boolean resultadosFinales = false;
        double perdidaUsuario = Double.parseDouble(edTextPerdEstatica.getText().toString());
        double velocidadUsuario = Double.parseDouble(edTextVelocidad.getText().toString());

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
        if (resultadosFinales == false)
        {
            infoCasos.Caso11(perdidaUsuario, velocidadUsuario);

            if (setInfo.getFlujoAire() > 0 && setInfo.getDiametroEqvFinal() > 0)
                resultadosFinales = true;
        }

        //Caso 12: la perdida y la velocidad no coinciden
        if (resultadosFinales == false)
        {
            infoCasos.Caso12(perdidaUsuario, velocidadUsuario);
            if (setInfo.getFlujoAire() > 0 && setInfo.getDiametroEqvFinal() > 0)
                resultadosFinales = true;
        }

        if (resultadosFinales == true)
        {
            Interpolaciones getInfo = new Interpolaciones();

            edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", getInfo.getFlujoAire()));
            edTextDiaEqv.setText(String.format(Locale.getDefault(), "%.2f", getInfo.getDiametroEqvFinal()));
            Casos operaciones = new Casos();
            operaciones.OperacionesFinales(getInfo.getFlujoAire());
            resultados();
        }
    }

    public void MetodoVelDiaEqv()
    {
        boolean resultadosFinales = false;
        double velocidaUsuario = Double.parseDouble(edTextVelocidad.getText().toString());
        double DiaEqvUsuario = Double.parseDouble(edTextDiaEqv.getText().toString());

        //Se restablercen a cero los valores del calculo anterior.
        Interpolaciones setGetInfo = new Interpolaciones();
        setGetInfo.setFlujoAire(0.00);
        setGetInfo.setPerdidaFinal(0.00);

        //Calculo de los cfm en funcion de la velocidad y diametro del usuario.
        double cfm = velocidaUsuario * (Math.PI * Math.pow((DiaEqvUsuario/24), 2));
        cfm = Math.round(cfm*100.0)/100.0;

        //Caso 7: los cfm calculados y el diametro conciden con los valores de la lista ListaPPA
        Casos infoCaso = new Casos();
        infoCaso.Caso7(cfm, DiaEqvUsuario);

        //Se restablece al valor del usuario ya que
        //en el metodo de interpolacion del caso 7 se obtiene la velocidad de la lista de la grafica.
        setGetInfo.setDiametroEqv(DiaEqvUsuario);
        setGetInfo.setVelocidadFlujoAire(velocidaUsuario);

        double perdidaEstatica = setGetInfo.getPerdidaFinal();

        if (perdidaEstatica > 0)
            resultadosFinales = true;

        if (resultadosFinales == false)
        {
            //Caso 13:
            Casos infoCasos = new Casos();
            infoCasos.Caso13(cfm, velocidaUsuario, DiaEqvUsuario);
        }

        if (resultadosFinales == true)
        {
            edTextCFM.setText(String.format(Locale.getDefault(), "%.0f", cfm));
            edTextPerdEstatica.setText(String.format(Locale.getDefault(), "%.3f", perdidaEstatica));
            Casos operaciones = new Casos();
            operaciones.OperacionesFinales(cfm);
            resultados();
        }
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
