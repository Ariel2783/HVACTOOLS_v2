package net.refriglobal.hvactools;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalculoRefrigerante extends AppCompatActivity {

    /**
     * Variable para utilizadaz para calculos
     */
    RadioButton r410, r22; // Variables para la seleccion de refrigerante.
    RadioButton si, eu; // Variables para la seleccion de sistema de medida.
    Spinner spinner1, spinner2; // Variable de spinner para los diametros de tuberias.
    Spinner spinner3, spinner4; //Variables spinner para las densidades de liquidos y succion.
    TextView txv_19, txv_22, txv_25, txv_30;
    Button calcular, borrar;

    EditText edt1, edt2, edt3, edt4;
    double cgevap, cgcond;
    double longtubliq, longtubsucc; // Longitud de tuberia de liquido y succion.
    double voltubliq, voltubsucc; // Longitud de tuberia de liquido y succion.
    double cgtubliq, cgtubsucc; // Carga en la tuberia de liquido y succion.
    double cgliqsucc, cgtotal; //Suma de la carga de tub. de liquido, succion y total, utilizado en proceso de conversion de unidad.
    double denliq, densucc; // Densidad del refrigerante en la tuberia de líquido y succion.
    double diatubliq, diatubsucc; //Diametro de la tuberia de liquido y succion.

    /**
     * Varible para mostrar objetos txv_3(unidad de carga de evaporador),
     * txv_4(unidad de carga de condensador). txv_11(unidad de longuitud de tub.
     * liq.), txv_12(unidad de longuitud de tub. succ.) txv_15(unidad de
     * densidad de liq.), txv_16(unidad de densidad de liq.)
     */
    ImageView image; // Variable para la imagen de refrigerante.
    TextView txv_3, txv_4, txv_11, txv_12, txv_15, txv_16; //
    TextView txv_13, txv_14, txv_5, txv_6,txv_7, txv_8, txv_9, txv_10, txv_17, txv_18, txv_21;
    TextView txv_24, txv_27, txv_28, txv_29, txv_31, txv_32, txv_33, txv_56, txv_57, txv_58;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_refrigerante);
        getSupportActionBar().hide(); //Oculta el toolbar

        r410 = findViewById(R.id.R410);
        r22  = findViewById(R.id.R22);
        si   = findViewById(R.id.SI);
        eu   = findViewById(R.id.EEUU);
        edt1 = findViewById(R.id.editText1);
        edt2 = findViewById(R.id.editText2);
        edt3 = findViewById(R.id.editText3);
        edt4 = findViewById(R.id.editText4);

        image  = findViewById(R.id.imageView1);
        txv_3  = findViewById(R.id.textView3);
        txv_4  = findViewById(R.id.textView4);
        txv_5  = findViewById(R.id.textView5);
        txv_6  = findViewById(R.id.textView6);
        txv_7  = findViewById(R.id.textView7);
        txv_8  = findViewById(R.id.textView8);
        txv_9  = findViewById(R.id.textView9);
        txv_10 = findViewById(R.id.textView10);
        txv_11 = findViewById(R.id.textView11);
        txv_12 = findViewById(R.id.textView12);
        txv_13 = findViewById(R.id.textView13);
        txv_14 = findViewById(R.id.textView14);
        txv_15 = findViewById(R.id.textView15);
        txv_16 = findViewById(R.id.textView16);
        txv_17 = findViewById(R.id.textView17);
        txv_18 = findViewById(R.id.textView18);
        txv_19 = findViewById(R.id.textView19);
        txv_21 = findViewById(R.id.textView21);
        txv_22 = findViewById(R.id.textView22);
        txv_24 = findViewById(R.id.textView24);
        txv_25 = findViewById(R.id.textView25);
        txv_27 = findViewById(R.id.textView27);
        txv_28 = findViewById(R.id.textView28);
        txv_29 = findViewById(R.id.textView29);
        txv_30 = findViewById(R.id.textView30);
        txv_31 = findViewById(R.id.textView31);
        txv_32 = findViewById(R.id.textView32);
        txv_33 = findViewById(R.id.textView33);
        txv_56 = findViewById(R.id.textView56);
        txv_57 = findViewById(R.id.textView57);
        txv_58 = findViewById(R.id.textView58);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);

        calcular = findViewById(R.id.button1);
        borrar = findViewById(R.id.button2);
    }

    //Seleccion de refrigerante R410.
    public void r410_click(View view)
    {
        if (r410.isChecked() == true)
        {
            image.setImageResource(R.drawable.r410);
        }

        //Creacion del Spinner para la densidad de liquido del R410 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []denliq_list={"1060.00","1070.00","1100.00"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R410 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []densucc_list={"4.12","4.20"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }

        //Creacion del Spinner para la densidad de liquido del R410 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []denliq_list={"66.174","66.798","68.671"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R410 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []densucc_list={"0.257","0.262"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }

        r410.setClickable(false); //Instruccion para evitar la reiteracion de la misma opcion.
        r22.setClickable(true); //Activa la opcion contraria.
    }

    //Seleccion de refrigerante R22.
    public void r22_click(View view)
    {
        if (r22.isChecked() == true)
        {
            image.setImageResource(R.drawable.r22_2);
        }

        //Creacion del Spinner para la densidad de liquido del R22 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []denliq_list={"1190.00","1194.00","1210.00"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R22 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []densucc_list={"3.60","4.50","4.70"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }

        //Creacion del Spinner para la densidad de liquido del R22 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []denliq_list={"74.289","74.539","75.538"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R22 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []densucc_list={"0.225","0.281","0.293"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }
        r410.setClickable(true);  //Activa la opcion contraria.
        r22.setClickable(false); //Instruccion para evitar la reiteracion de la misma opcion.
    }

    //Seleccion del sistema internaciona (SI)
    public void si_click(View view)
    {
        if (si.isChecked() == true)
        {
            txv_3.setText("Kg.");
            txv_4.setText("Kg.");
            txv_11.setText("m.");
            txv_12.setText("m.");
            txv_15.setText("Kg/m³.");
            txv_16.setText("Kg/m³.");
        }

        if (edt1.getText().toString().length() > 0)
        {
            cgevap = Double.parseDouble(edt1.getText().toString());
            edt1.setText(String.format("%.3f",cgevap/2.205));
        }//Conversion de Lb a Kg para mostrarlo cuando se cambia de sistema de unidad.

        if (edt2.getText().toString().length()>0)
        {
            cgcond = Double.parseDouble(edt2.getText().toString());
            edt2.setText(String.format("%.3f",cgcond/2.205));
        }// Conversion de Lb a Kg para mostrarlo cuando se cambia de sistema de unidad.

        if (edt3.getText().toString().length()>0)
        {
            longtubliq = Double.parseDouble(edt3.getText().toString());
            edt3.setText(String.format("%.2f",longtubliq/3.281));
        }//Conversion de Ft a m para mostrarlo cuando se cambia de sistema de unidad.

        if (edt4.getText().toString().length()>0)
        {
            longtubsucc = Double.parseDouble(edt4.getText().toString());
            edt4.setText(String.format("%.2f",longtubsucc/3.281));
        }//Conversion de Ft a m para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_19.getText().toString().length()>0)
        {
            cgtubliq = cgtubliq/2.205;
            txv_19.setText(String.format("%.3f"+" Kg",cgtubliq));
        }// Conversion de Lb a Kg para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_22.getText().toString().length()>0)
        {
            cgtubsucc = cgtubsucc/2.205;
            txv_22.setText(String.format("%.3f"+" Kg",cgtubsucc));
        }// Conversion de Lb a Kg para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_25.getText().toString().length()>0)
        {
            cgliqsucc = cgliqsucc/2.205;
            txv_25.setText(String.format("%.3f"+" Kg",cgliqsucc));
        }// Conversion de Lb a Kg para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_30.getText().toString().length()>0)
        {
            cgtotal = cgtotal/2.205;
            txv_30.setText(String.format("%.3f"+" Kg",cgtotal));
        }// Conversion de Lb a Kg para mostrarlo cuando se cambia de sistema de unidad.


        //Creacion del Spinner para la densidad de liquido del R22 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []denliq_list={"1190.00","1194.00","1210.00"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R22 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []densucc_list={"3.60","4.50","4.70"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }

        //Creacion del Spinner para la densidad de liquido del R410 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []denliq_list={"1060.00","1070.00","1100.00"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R410 en el sistema S.I.
        if (si.isChecked() == true)
        { //Seleccion del sistema S.I.
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []densucc_list={"4.12","4.20"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }
        si.setClickable(false); //Instruccion para evitar la reiteracion de la misma opcion.
        eu.setClickable(true); //Activa la opcion contraria.

        //borra las conversion en Lb Oz, del sistema eeuu
        txv_56.setText(String.format(""));txv_57.setText(String.format(""));txv_58.setText(String.format(""));
        txv_31.setText(String.format(""));
    }

    //Seleccion del sistema ingles (EE.UU)
    public void eu_click(View view) {
        if (eu.isChecked() == true) {
            txv_3.setText("Lb.");
            txv_4.setText("Lb.");
            txv_11.setText("Ft.");
            txv_12.setText("Ft.");
            txv_15.setText("Lb/Ft³.");
            txv_16.setText("Lb/Ft³.");
        }

        if (edt1.getText().toString().length()>0)
        {
            cgevap = Double.parseDouble(edt1.getText().toString());
            edt1.setText(String.format("%.3f",cgevap*2.205));
        }//Conversion de Kg a Lb para mostrarlo cuando se cambia de sistema de unidad.

        if (edt2.getText().toString().length()>0)
        {
            cgcond = Double.parseDouble(edt2.getText().toString());
            edt2.setText(String.format("%.3f",cgcond*2.205));
        }// Conversion de Kg a Lb para mostrarlo cuando se cambia de sistema de unidad.

        if (edt3.getText().toString().length()>0)
        {
            longtubliq = Double.parseDouble(edt3.getText().toString());
            edt3.setText(String.format("%.2f",longtubliq*3.281));
        }//Conversion de m a Ft para mostrarlo cuando se cambia de sistema de unidad.

        if (edt4.getText().toString().length()>0)
        {
            longtubsucc = Double.parseDouble(edt4.getText().toString());
            edt4.setText(String.format("%.2f",longtubsucc*3.281));
        }//Conversion de m a Ft para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_19.getText().toString().length()>0)
        {
            cgtubliq = cgtubliq*2.205;
            txv_19.setText(String.format("%.3f"+" Lb",cgtubliq));
        } // Conversion de Kg a Lb para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_22.getText().toString().length()>0)
        {
            cgtubsucc = cgtubsucc*2.205;
            txv_22.setText(String.format("%.3f"+" Lb",cgtubsucc));
        }// Conversion de Kg a Lb para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_25.getText().toString().length()>0)
        {
            cgliqsucc = cgliqsucc*2.205;
            txv_25.setText(String.format("%.3f"+" Lb",cgliqsucc));
        }// Conversion de Kg a Lb para mostrarlo cuando se cambia de sistema de unidad.

        if (txv_30.getText().toString().length()>0)
        {
            cgtotal = cgtotal*2.205;
            txv_30.setText(String.format("%.3f"+" Lb",cgtotal));
        }// Conversion de Kg a Lb para mostrarlo cuando se cambia de sistema de unidad.

        //Creacion del Spinner para la densidad de liquido del R22 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []denliq_list={"74.289","74.539","75.538"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R22 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r22.isChecked() == true)
            { //Si esta activado la seleccion del R22
                String []densucc_list={"0.225","0.281","0.293"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }

        //Creacion del Spinner para la densidad de liquido del R410 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []denliq_list={"66.174","66.798","68.671"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, denliq_list);
                spinner3.setAdapter(adapter1);
            }
        }

        //Creacion del Spinner para la densidad de succion del R410 en el sistema EE.UU
        if (eu.isChecked() == true)
        { //Seleccion del sistema EE.UU
            if (r410.isChecked() == true)
            { //Si esta activado la seleccion del R410
                String []densucc_list={"0.257","0.262"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, densucc_list);
                spinner4.setAdapter(adapter2);
            }
        }
        si.setClickable(true);   //Activa la opcion contraria.
        eu.setClickable(false); //Instruccion para evitar la reiteracion de la misma opcion.

        if (edt3.getText().toString().length()>0 & edt4.getText().toString().length()>0)
        {
            //se extrae el entero para presentar el valor en LB y Oz
            int ent_cgtubliq = (int) cgtubliq;
            int ent_cgtubsucc = (int) cgtubsucc;
            int ent_cgliqsucc = (int) cgliqsucc;
            int ent_cgtotal = (int) cgtotal;

            double oz_cgtubliq = (cgtubliq - ent_cgtubliq) * 16;
            double oz_cgtubsucc = (cgtubsucc - ent_cgtubsucc) * 16;
            double oz_cgliqsucc = (cgliqsucc - ent_cgliqsucc) * 16;
            double oz_cgtotal = (cgtotal - ent_cgtotal) * 16;

            txv_19.setText(String.format("%.3f" + " Lb", cgtubliq));
            txv_22.setText(String.format("%.3f" + " Lb", cgtubsucc));
            txv_25.setText(String.format("%.3f" + " Lb", cgliqsucc));
            txv_30.setText(String.format("%.3f" + " Lb", cgtotal));
            txv_56.setText(String.format("(%d" + " Lb" + " %.1f" + " Oz)", ent_cgtubliq, oz_cgtubliq));
            txv_57.setText(String.format("(%d" + " Lb" + " %.1f" + " Oz)", ent_cgtubsucc, oz_cgtubsucc));
            txv_58.setText(String.format("(%d" + " Lb" + " %.1f" + " Oz)", ent_cgliqsucc, oz_cgliqsucc));
            txv_31.setText(String.format("(%d" + " Lb" + " %.1f" + " Oz)", ent_cgtotal, oz_cgtotal));
        }

    }//Salida public void eu_click

    //Proceso de claculo
    public void Calcular(View view){

        //Sentencia para evitar que se detenga la app, por si se presiona el boton
        //calcular y no se a seleccionado el refrigerante y el sistema de unidad.
        if(r410.isChecked() == true || r22.isChecked() == true)
        { //Sentencia 1
            if(si.isChecked() == true || eu.isChecked() == true) { //Sentencia 2
                //Asignacion de diametro efectivo para la tuberia de liquido
                String selec1 = spinner1.getSelectedItem().toString();
                if (selec1.equals("⅛")) { diatubliq = 0.125; }
                if (selec1.equals("¼")) {
                    diatubliq = 0.200;
                }
                if (selec1.equals("⅜")) {
                    diatubliq = 0.315;
                }
                if (selec1.equals("½")) {
                    diatubliq = 0.430;
                }
                if (selec1.equals("⅝")) {
                    diatubliq = 0.550;
                }
                if (selec1.equals("¾")) {
                    diatubliq = 0.785;
                }
                if (selec1.equals("⅞")) {
                    diatubliq = 0.790;
                }
                if (selec1.equals("1")) {
                    diatubliq = 1.025;
                }
                if (selec1.equals("1⅛")) {
                    diatubliq = 1.030;
                }
                if (selec1.equals("1¼")) {
                    diatubliq = 1.265;
                }
                if (selec1.equals("1½")) {
                    diatubliq = 1.505;
                }
                if (selec1.equals("2")) {
                    diatubliq = 1.985;
                }
                if (selec1.equals("2½")) {
                    diatubliq = 2.465;
                }
                if (selec1.equals("3")) {
                    diatubliq = 2.945;
                }
                if (selec1.equals("3½")) {
                    diatubliq = 3.425;
                }
                if (selec1.equals("4")) {
                    diatubliq = 3.902;
                }

                //Asignacion de diametro efectivo para la tuberia de succion
                String selec2 = spinner2.getSelectedItem().toString();
                if (selec2.equals("⅛")) {
                    diatubsucc = 0.125;
                }
                if (selec2.equals("¼")) {
                    diatubsucc = 0.200;
                }
                if (selec2.equals("⅜")) {
                    diatubsucc = 0.315;
                }
                if (selec2.equals("½")) {
                    diatubsucc = 0.430;
                }
                if (selec2.equals("⅝")) {
                    diatubsucc = 0.550;
                }
                if (selec2.equals("¾")) {
                    diatubsucc = 0.785;
                }
                if (selec2.equals("⅞")) {
                    diatubsucc = 0.790;
                }
                if (selec2.equals("1")) {
                    diatubsucc = 1.025;
                }
                if (selec2.equals("1⅛")) {
                    diatubsucc = 1.030;
                }
                if (selec2.equals("1¼")) {
                    diatubsucc = 1.265;
                }
                if (selec2.equals("1½")) {
                    diatubsucc = 1.505;
                }
                if (selec2.equals("2")) {
                    diatubsucc = 1.985;
                }
                if (selec2.equals("2½")) {
                    diatubsucc = 2.465;
                }
                if (selec2.equals("3")) {
                    diatubsucc = 2.945;
                }
                if (selec2.equals("3½")) {
                    diatubsucc = 3.425;
                }
                if (selec2.equals("4")) {
                    diatubsucc = 3.902;
                }

                //Asignar densidad de los spinners
                String selec3 = spinner3.getSelectedItem().toString(); //Densidad de liquido
                String selec4 = spinner4.getSelectedItem().toString(); //Densidad de succion

                //Densidad de liquido del R410 en el sitema SI.
                if (selec3.equals("1060.00")) {
                    denliq = 1060.00;
                }
                if (selec3.equals("1070.00")) {
                    denliq = 1070.00;
                }
                if (selec3.equals("1100.00")) {
                    denliq = 1100.00;
                }

                //Densidad de succion del R410 en el sitema SI.
                if (selec4.equals("4.12")) {
                    densucc = 4.12;
                }
                if (selec4.equals("4.20")) {
                    densucc = 4.20;
                }

                //Densidad de liquido del R410 en el sitema EE.UU.
                if (selec3.equals("66.174")) {
                    denliq = 66.174;
                }
                if (selec3.equals("66.798")) {
                    denliq = 66.798;
                }
                if (selec3.equals("68.671")) {
                    denliq = 68.671;
                }

                //Densidad de succion del R410 en el sitema EE.UU.
                if (selec4.equals("0.257")) {
                    densucc = 0.257;
                }
                if (selec4.equals("0.262")) {
                    densucc = 0.262;
                }

                //Densidad de liquido del R22 en el sitema SI.
                if (selec3.equals("1190.00")) {
                    denliq = 1190.00;
                }
                if (selec3.equals("1194.00")) {
                    denliq = 1194.00;
                }
                if (selec3.equals("1210.00")) {
                    denliq = 1210.00;
                }

                //Densidad de succion del R22 en el sitema SI.
                if (selec4.equals("3.60")) {
                    densucc = 3.60;
                }
                if (selec4.equals("4.50")) {
                    densucc = 4.50;
                }
                if (selec4.equals("4.70")) {
                    densucc = 4.70;
                }

                //Densidad de liquido del R22 en el sitema EE.UU.
                if (selec3.equals("74.289")) {
                    denliq = 74.289;
                }
                if (selec3.equals("74.539")) {
                    denliq = 74.539;
                }
                if (selec3.equals("75.538"))
                {
                    denliq = 75.538;
                }

                //Densidad de succion del R22 en el sitema EE.UU.
                if (selec4.equals("0.225"))
                {
                    densucc = 0.225;
                }
                if (selec4.equals("0.281"))
                {
                    densucc = 0.281;
                }
                if (selec4.equals("0.293"))
                {
                    densucc = 0.293;
                }

                if (edt1.getText().toString().length() > 0)
                {
                    cgevap = Double.parseDouble(edt1.getText().toString()); //Carga del evaporador
                } else { cgevap = 0;}

                if (edt2.getText().toString().length() > 0)
                {
                    cgcond = Double.parseDouble(edt2.getText().toString()); //Carga del condensador
                } else { cgcond = 0;}

                if (edt3.getText().toString().length() > 0)
                {
                    longtubliq = Double.parseDouble(edt3.getText().toString()); //Longuitud de la tuberia de liquido
                } else{longtubliq = 0;}

                if (edt4.getText().toString().length() > 0)
                {
                    longtubsucc = Double.parseDouble(edt4.getText().toString()); //Longuitud de la tuberia de succion
                } else {longtubsucc = 0;}

                  //Sistema SI selecionado	   conversion de los diametro de tuberias liq, succ. a metros.
                if (si.isChecked() == true) {
                    diatubliq = diatubliq / 39.37;
                    diatubsucc = diatubsucc / 39.370;
                }

                //Sistema EE.UU selecionado	    conversion de los diametro de tuberias liq, succ. a pies.
                if (eu.isChecked() == true) {
                    diatubliq = diatubliq / 12;
                    diatubsucc = diatubsucc / 12;
                }

                voltubliq = (Math.PI * Math.pow(diatubliq, 2) * longtubliq) / 4; //calculo del volumen de la tub de liquido
                voltubsucc = (Math.PI * Math.pow(diatubsucc, 2) * longtubsucc) / 4; //calculo del volumen de la tub de succion
                cgtubliq = voltubliq * denliq; //Cantidad de refrigerante en la tub. de liquido.
                cgtubsucc = voltubsucc * densucc; //Cantidad de refrigerante en la tub. de succion.
                cgliqsucc = cgtubliq + cgtubsucc;
                cgtotal = cgtubliq + cgtubsucc + cgevap + cgcond;

                //Modificar cambio de unidades utilizando la variable directa.
                if (si.isChecked() == true) {
                    txv_19.setText(String.format("%.3f" + " Kg", cgtubliq));
                    txv_22.setText(String.format("%.3f" + " Kg", cgtubsucc));
                    txv_25.setText(String.format("%.3f" + " Kg", cgliqsucc));
                    txv_30.setText(String.format("%.3f" + " Kg", cgtotal));
                }

                if (eu.isChecked() == true) {

                    //se extrae el entero para presentar el valor en LB y Oz
                    int ent_cgtubliq = (int)cgtubliq;
                    int ent_cgtubsucc = (int)cgtubsucc;
                    int ent_cgliqsucc = (int)cgliqsucc;
                    int ent_cgtotal = (int)cgtotal;

                    //se extrae la parte decimal, se multiplica x 16 para llevarlo a Oz.
                    double oz_cgtubliq = (cgtubliq - ent_cgtubliq)*16;
                    double oz_cgtubsucc = (cgtubsucc - ent_cgtubsucc)*16;
                    double oz_cgliqsucc = (cgliqsucc - ent_cgliqsucc)*16;
                    double oz_cgtotal = (cgtotal - ent_cgtotal)*16;

                    txv_19.setText(String.format("%.3f"+" Lb", cgtubliq));
                    txv_22.setText(String.format("%.3f"+" Lb", cgtubsucc));
                    txv_25.setText(String.format("%.3f"+" Lb", cgliqsucc));
                    txv_30.setText(String.format("%.3f"+" Lb", cgtotal));
                    txv_56.setText(String.format("(%d"+" Lb"+  " %.1f"+" Oz)", ent_cgtubliq,oz_cgtubliq));
                    txv_57.setText(String.format("(%d"+" Lb"+  " %.1f"+" Oz)", ent_cgtubsucc,oz_cgtubsucc));
                    txv_58.setText(String.format("(%d"+" Lb"+  " %.1f"+" Oz)", ent_cgliqsucc,oz_cgliqsucc));
                    txv_31.setText(String.format("(%d"+" Lb"+  " %.1f"+" Oz)", ent_cgtotal,oz_cgtotal));
                }


              this.toast_show();
            }//Sentencia 2
        } this.toast_show();//Sentencia 1
    } //Salida Proceso de calculo

    public void Borrar(View view){
        edt1.setText(String.format(""));edt2.setText(String.format(""));
        edt3.setText(String.format(""));edt4.setText(String.format(""));
        txv_19.setText(String.format("0.000"));txv_22.setText(String.format("0.000"));
        txv_25.setText(String.format("0.000"));txv_30.setText(String.format("0.000"));

    }//Salida Proceso de borrar

    public void toast_show(){

        if (edt3.getText().toString().length() == 0 || edt4.getText().toString().length() == 0)
        {
            Toast toast1 = Toast.makeText(getApplicationContext(), R.string.intro_long, Toast.LENGTH_SHORT);
            toast1.show();
        }

        if (r410.isChecked() == false & r22.isChecked() == false)
        {
            Toast toast2 = Toast.makeText(getApplicationContext(), R.string.selct_refr, Toast.LENGTH_SHORT);
            toast2.show();
        }

        if (si.isChecked() == false & eu.isChecked() == false)
        {
            Toast toast3 = Toast.makeText(getApplicationContext(), R.string.selct_sist_unid, Toast.LENGTH_SHORT);
            toast3.show();
        }
    }
}
