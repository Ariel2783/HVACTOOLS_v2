package net.refriglobal.hvactools;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExtraccionCampana extends AppCompatActivity {

    Spinner spin01, spin02, spin03;
    ImageView img01;
    EditText edt1, edt2, edt3;
    TextView txv9, txv14, txv15, txv16, txv26, txv28, txv30, txv31;
    String select01, select02, select03, fact_vel_str;
    RadioButton radbtn1, radbtn2, radbtn3, radbtn4, radbtn5, radbtn6;
    RadioGroup radgrp1, radgrp2;

    double per_ef, ld1, ld2, alt_cap;
    double cfm_1, cfm_2, cfm_3, fact_vel, vel;

    /**
     ld1-> largo de campana
     ld2-> ancho de campana
     alt_cap -> altura de captacion de la campana
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraccion_campana);

        spin01  = findViewById(R.id.spinner1);
        spin02  = findViewById(R.id.spinner2);
        spin03  = findViewById(R.id.spinner3);
        edt1    = findViewById(R.id.editText1);
        edt2    = findViewById(R.id.editText2);
        edt3    = findViewById(R.id.editText3);
        txv9    = findViewById(R.id.textView9);
        txv26   = findViewById(R.id.textView26);
        txv28   = findViewById(R.id.textView28);
        txv30   = findViewById(R.id.textView30);
        txv31   = findViewById(R.id.textView31);
        img01   = findViewById(R.id.imageView_01);
        radbtn1 = findViewById(R.id.radioButton1);
        radbtn2 = findViewById(R.id.radioButton2);
        radbtn3 = findViewById(R.id.radioButton3);
        radbtn4 = findViewById(R.id.radioButton4);
        radbtn5 = findViewById(R.id.radioButton5);
        radbtn6 = findViewById(R.id.radioButton6);
        radgrp1 = findViewById(R.id.radiogroup1);
        radgrp2 = findViewById(R.id.radiogroup2);

        spin01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                select01 = spin01.getSelectedItem().toString();

                if (select01.equals("1 lado libre")) {img01.setImageResource(R.drawable.camp_4);}
                if (select01.equals("1 free side"))  {img01.setImageResource(R.drawable.camp_4_en);}

                if (select01.equals("2 lados libres")) {img01.setImageResource(R.drawable.camp_1);}
                if (select01.equals("2 free sides")) {img01.setImageResource(R.drawable.camp_1_en);}

                if (select01.equals("3 lados libres")) {img01.setImageResource(R.drawable.camp_2);}
                if (select01.equals("3 free sides")) {img01.setImageResource(R.drawable.camp_2_en);}

                if (select01.equals("4 lados libres")) {img01.setImageResource(R.drawable.camp_3);}
                if (select01.equals("4 free sides")) {img01.setImageResource(R.drawable.camp_3_en);}

                if (select01.equals("2 lados libres especial")) {img01.setImageResource(R.drawable.camp_6);}
                if (select01.equals("2 special free sides")) {img01.setImageResource(R.drawable.camp_6_en);}

                if (select01.equals("3 lados libres especial")) {img01.setImageResource(R.drawable.camp_5);}
                if (select01.equals("3 free sides special")) {img01.setImageResource(R.drawable.camp_5_en);}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spin03.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                select03 = spin03.getSelectedItem().toString();
                if(select03.equals("50")) { vel = 50; }
                if(select03.equals("60")) { vel = 60; }
                if(select03.equals("70")) { vel = 70; }
                if(select03.equals("80")) { vel = 80; }
                if(select03.equals("90")) { vel = 90; }
                if(select03.equals("100")) { vel = 100; }
                if(select03.equals("110")) { vel = 110; }
                if(select03.equals("120")) { vel = 120; }
                if(select03.equals("130")) { vel = 130; }
                if(select03.equals("140")) { vel = 140; }
                if(select03.equals("150")) { vel = 150; }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void calcular(View view)
    {
        if(radbtn1.isChecked()==true | radbtn2.isChecked()==true)
        {
            if (radbtn3.isChecked() == true | radbtn4.isChecked() == true | radbtn5.isChecked() == true | radbtn6.isChecked() == true)
            {
                if (edt1.getText().toString().length() > 0 & edt2.getText().toString().length() > 0 & edt3.getText().toString().length() > 0)
                {
                    ld1 = Double.parseDouble(edt1.getText().toString());
                    ld2 = Double.parseDouble(edt2.getText().toString());
                    alt_cap = Double.parseDouble(edt3.getText().toString());

                    if (select01.equals("1 lado libre") | select01.equals("1 free side")) {
                        per_ef = ld1;
                    }
                    if (select01.equals("2 lados libres") | select01.equals("2 free sides")) {
                        per_ef = ld1 + ld2;
                    }
                    if (select01.equals("3 lados libres") | select01.equals("3 free sides")) {
                        per_ef = ld1 + (2 * ld2);
                    }
                    if (select01.equals("4 lados libres") | select01.equals("4 free sides")) {
                        per_ef = (2 * ld1) + (2 * ld2);
                    }
                    if (select01.equals("2 lados libres especial") | select01.equals("2 special free sides")) {
                        per_ef = ld1 * 2;
                    }
                    if (select01.equals("3 lados libres especial") | select01.equals("3 free sides special ")) {
                        per_ef = (ld1 * 2) + ld2;
                    }

                    this.factor_campana();

                    cfm_1 = per_ef * fact_vel;
                    cfm_2 = per_ef * alt_cap * vel;
                    cfm_3 = (cfm_1 + cfm_2) / 2;
                    Double cfm_iny = cfm_3 * 0.8;

                    txv26.setText(String.format("%.0f" + " CFM", cfm_1));
                    txv28.setText(String.format("%.0f" + " CFM", cfm_2));
                    txv30.setText(String.format("%.0f" + " CFM", cfm_3));
                    txv31.setText(String.format("%.0f " + getResources().getString(R.string.flujoInyeccion), cfm_iny));
                }
            }
        }


        this.toast_show();
    }

    public void factor_campana()
    {
        select02 = spin02.getSelectedItem().toString();

        // spinner 2 Pared-Ligero
        if(select02.equals("150")) { fact_vel = 150;}
        if(select02.equals("160")) { fact_vel = 160;}
        if(select02.equals("170")) { fact_vel = 170;}
        if(select02.equals("180")) { fact_vel = 180;}
        if(select02.equals("190")) { fact_vel = 190;}
        if(select02.equals("200")) { fact_vel = 200;}

        // spinner 2 Pared-Medio
        if(select02.equals("210")) { fact_vel = 210;}
        if(select02.equals("220")) { fact_vel = 220;}
        if(select02.equals("230")) { fact_vel = 230;}
        if(select02.equals("240")) { fact_vel = 240;}
        if(select02.equals("250")) { fact_vel = 250;}
        if(select02.equals("260")) { fact_vel = 260;}
        if(select02.equals("270")) { fact_vel = 270;}
        if(select02.equals("280")) { fact_vel = 280;}
        if(select02.equals("290")) { fact_vel = 290;}
        if(select02.equals("300")) { fact_vel = 300;}

        // spinner 2 Pared-Pesado
        if(select02.equals("310")) { fact_vel =310;}
        if(select02.equals("320")) { fact_vel =320;}
        if(select02.equals("330")) { fact_vel =330;}
        if(select02.equals("340")) { fact_vel =340;}
        if(select02.equals("350")) { fact_vel =350;}
        if(select02.equals("360")) { fact_vel =360;}
        if(select02.equals("370")) { fact_vel =370;}
        if(select02.equals("380")) { fact_vel =380;}
        if(select02.equals("390")) { fact_vel =390;}
        if(select02.equals("400")) { fact_vel =400;}

        // spinner 2 Pared-Extra pesado
        if(select02.equals("410")) { fact_vel =410;}
        if(select02.equals("420")) { fact_vel =420;}
        if(select02.equals("430")) { fact_vel =430;}
        if(select02.equals("440")) { fact_vel =440;}
        if(select02.equals("450")) { fact_vel =450;}
        if(select02.equals("460")) { fact_vel =460;}
        if(select02.equals("470")) { fact_vel =470;}
        if(select02.equals("480")) { fact_vel =480;}
        if(select02.equals("490")) { fact_vel =490;}
        if(select02.equals("500")) { fact_vel =500;}

        // spinner 2 Simple-Ligero
        if(select02.equals("250")) { fact_vel =250;}
        if(select02.equals("260")) { fact_vel =260;}
        if(select02.equals("270")) { fact_vel =270;}
        if(select02.equals("280")) { fact_vel =280;}
        if(select02.equals("290")) { fact_vel =290;}
        if(select02.equals("300")) { fact_vel =300;}

        // spinner 2 Simple-Medio
        if(select02.equals("310")) { fact_vel =310;}
        if(select02.equals("320")) { fact_vel =320;}
        if(select02.equals("330")) { fact_vel =330;}
        if(select02.equals("340")) { fact_vel =340;}
        if(select02.equals("350")) { fact_vel =350;}
        if(select02.equals("360")) { fact_vel =360;}
        if(select02.equals("370")) { fact_vel =370;}
        if(select02.equals("380")) { fact_vel =380;}
        if(select02.equals("390")) { fact_vel =390;}
        if(select02.equals("400")) { fact_vel =400;}

        // spinner 2 Simple-Pesado
        if(select02.equals("410")) { fact_vel =410;}
        if(select02.equals("420")) { fact_vel =420;}
        if(select02.equals("430")) { fact_vel =430;}
        if(select02.equals("440")) { fact_vel =440;}
        if(select02.equals("450")) { fact_vel =450;}
        if(select02.equals("460")) { fact_vel =460;}
        if(select02.equals("470")) { fact_vel =470;}
        if(select02.equals("480")) { fact_vel =480;}
        if(select02.equals("490")) { fact_vel =490;}
        if(select02.equals("500")) { fact_vel =500;}

        // spinner 2 Simple-Extra-Pesado
        if(select02.equals("510")) { fact_vel =510;}
        if(select02.equals("520")) { fact_vel =520;}
        if(select02.equals("530")) { fact_vel =530;}
        if(select02.equals("540")) { fact_vel =540;}
        if(select02.equals("550")) { fact_vel =550;}
        if(select02.equals("560")) { fact_vel =560;}
        if(select02.equals("570")) { fact_vel =570;}
        if(select02.equals("580")) { fact_vel =580;}
        if(select02.equals("590")) { fact_vel =590;}
        if(select02.equals("600")) { fact_vel =600;}
    }

    public void check1(View view)
    {
        //Pared-Ligero
        if(radbtn1.isChecked()==true & radbtn3.isChecked()==true)
        {
            String [] ligero_pared = {"150","160","170","180","190","200"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, ligero_pared);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Pared-Medio
        if(radbtn1.isChecked()==true & radbtn4.isChecked()==true)
        {
            String [] ligero_medio = {"210","220","230","240","250","260","270","280","290","300"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, ligero_medio);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Pared-Pesado
        if(radbtn1.isChecked()==true & radbtn5.isChecked()==true)
        {
            String [] pared_pesado = {"310","320","330","340","350","360","370","380","390","400"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, pared_pesado);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Pared-Extra pesado
        if(radbtn1.isChecked()==true & radbtn6.isChecked()==true)
        {
            String [] pared_extra = {"410","420","430","440","450","460","470","480","490","500"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, pared_extra);
            spin02.setAdapter(adapter02);
        }
    }

    public void check2(View view)
    {
        // spinner 2 Simple-Ligero
        if(radbtn2.isChecked()==true & radbtn3.isChecked()==true)
        {
            String [] simple_ligero = {"250","260","270","280","290","300"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_ligero);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Medio
        if(radbtn2.isChecked()==true & radbtn4.isChecked()==true)
        {
            String [] simple_medio = {"310","320","330","340","350","360","370","380","390","400"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_medio);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Pesado
        if(radbtn2.isChecked()==true & radbtn5.isChecked()==true)
        {
            String [] simple_pesado = {"410","420","430","440","450","460","470","480","490","500"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_pesado);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Extra-Pesado
        if(radbtn2.isChecked()==true & radbtn6.isChecked()==true)
        {
            String [] simple_extra = {"510","520","530","540","550","560","570","580","590","600"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_extra);
            spin02.setAdapter(adapter02);
        }
    }

    public void check3(View view)
    {
        // spinner 2 Pared-Ligero
        if(radbtn1.isChecked()==true & radbtn3.isChecked()==true)
        {
            String [] ligero_pared = {"150","160","170","180","190","200"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, ligero_pared);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Ligero
        if(radbtn2.isChecked()==true & radbtn3.isChecked()==true)
        {
            String [] simple_ligero = {"250","260","270","280","290","300"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_ligero);
            spin02.setAdapter(adapter02);
        }
    }

    public void check4(View view)
    {
        // spinner 2 Pared-Medio
        if(radbtn1.isChecked()==true & radbtn4.isChecked()==true)
        {
            String [] ligero_medio = {"210","220","230","240","250","260","270","280","290","300"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, ligero_medio);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Medio
        if(radbtn2.isChecked()==true & radbtn4.isChecked()==true)
        {
            String [] simple_medio = {"310","320","330","340","350","360","370","380","390","400"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_medio);
            spin02.setAdapter(adapter02);
        }
    }

    public void check5(View view)
    {
        // spinner 2 Pared-Pesado
        if(radbtn1.isChecked()==true & radbtn5.isChecked()==true)
        {
            String [] pared_pesado = {"310","320","330","340","350","360","370","380","390","400"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, pared_pesado);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Pesado
        if(radbtn2.isChecked()==true & radbtn5.isChecked()==true)
        {
            String [] simple_pesado = {"410","420","430","440","450","460","470","480","490","500"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_pesado);
            spin02.setAdapter(adapter02);
        }
    }

    public void check6(View view)
    {
        // spinner 2 Pared-Extra pesado
        if(radbtn1.isChecked()==true & radbtn6.isChecked()==true)
        {
            String [] pared_extra = {"410","420","430","440","450","460","470","480","490","500"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, pared_extra);
            spin02.setAdapter(adapter02);
        }

        // spinner 2 Simple-Extra-Pesado
        if(radbtn2.isChecked()==true & radbtn6.isChecked()==true)
        {
            String [] simple_extra = {"510","520","530","540","550","560","570","580","590","600"};
            ArrayAdapter <String> adapter02 = new ArrayAdapter<String>(this,R.layout.spinner_item_factor, simple_extra);
            spin02.setAdapter(adapter02);
        }
    }

    public void toast_show()
    {
        if (edt1.getText().toString().length() == 0 & edt2.getText().toString().length() > 0 & edt3.getText().toString().length() > 0)
        {
            Toast toast1 = Toast.makeText(getApplicationContext(),R.string.Intro_largo, Toast.LENGTH_SHORT);
            toast1.show();
        }

        if (edt1.getText().toString().length() > 0 & edt2.getText().toString().length() == 0 & edt3.getText().toString().length() > 0)
        {
            Toast toast2 = Toast.makeText(getApplicationContext(),R.string.Intro_ancho, Toast.LENGTH_SHORT);
            toast2.show();
        }

        if (edt1.getText().toString().length() > 0 & edt2.getText().toString().length() > 0 & edt3.getText().toString().length() == 0)
        {
            Toast toast3 = Toast.makeText(getApplicationContext(),R.string.Intro_alt, Toast.LENGTH_SHORT);
            toast3.show();
        }

        if (edt1.getText().toString().length() == 0 & edt2.getText().toString().length() == 0 & edt3.getText().toString().length() > 0)
        {
            Toast toast4 = Toast.makeText(getApplicationContext(),R.string.Intro_largo_ancho, Toast.LENGTH_SHORT);
            toast4.show();
        }

        if (edt1.getText().toString().length() == 0 & edt2.getText().toString().length() > 0 & edt3.getText().toString().length() == 0)
        {
            Toast toast5 = Toast.makeText(getApplicationContext(),R.string.Intro_largo_alt, Toast.LENGTH_SHORT);
            toast5.show();
        }

        if (edt1.getText().toString().length() > 0 & edt2.getText().toString().length() == 0 & edt3.getText().toString().length() == 0)
        {
            Toast toast6 = Toast.makeText(getApplicationContext(),R.string.Intro_ancho_alt, Toast.LENGTH_SHORT);
            toast6.show();
        }

        if (edt1.getText().toString().length() == 0 & edt2.getText().toString().length() == 0 & edt3.getText().toString().length() == 0)
        {
            Toast toast6 = Toast.makeText(getApplicationContext(),R.string.Dimensiones, Toast.LENGTH_SHORT);
            toast6.show();
        }

        if (select01.equals("Seleccione N° de lados libres"))
        {
            Toast toast6 = Toast.makeText(getApplicationContext(),R.string.lados_libres, Toast.LENGTH_SHORT);
            toast6.show();
        }

        if(radbtn1.isChecked()==false & radbtn2.isChecked()==false)
        {
            Toast toast6 = Toast.makeText(getApplicationContext(),R.string.Selec_tipo_montaje, Toast.LENGTH_SHORT);
            toast6.show();
        }

        if(radbtn3.isChecked()==false & radbtn4.isChecked()==false & radbtn5.isChecked()==false & radbtn6.isChecked()==false)
        {
            Toast toast6 = Toast.makeText(getApplicationContext(),R.string.Selec_tipo_trabajo, Toast.LENGTH_SHORT);
            toast6.show();
        }
    }

    public void borrar(View view)
    {
        edt1.setText(String.format("")); edt2.setText(String.format("")); edt3.setText(String.format(""));
        txv26.setText(String.format("")); txv28.setText(String.format("")); txv30.setText(String.format(""));
    }

}
