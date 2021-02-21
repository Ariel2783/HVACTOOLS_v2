package net.refriglobal.hvactools;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class CargaTermica extends AppCompatActivity {

    SeekBar barra_0, barra_1, barra_2, barra_3, barra_4;//barras de busquedas.
    TextView btu_hr1, ton1, fact1, cfm_v, cfm_area_v; //Varibles para mostrar resultado.
    EditText area1, editText8, editText9; //area del recinto valor de entrada del usuario.
    double btu_hr2, ton2, fact2; //Varibles para proceso matematico.
    double cfm, cfm_area; //Varibles para proceso matematico.
    double valor0, valor1, valor2, valor3, valor4; //Variable colectoras de valor de las barras.
    double area2, Lado_A, Lado_B; //area del recinto para realizar operacion matematica.
    Button guia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_termica);

        barra_0 = findViewById(R.id.seekBar0); //Factor X0.1
        barra_1 = findViewById(R.id.seekBar1); //Factor X1
        barra_2 = findViewById(R.id.seekBar2); //Factor X10
        barra_3 = findViewById(R.id.seekBar3); //Factor X100
        barra_4 = findViewById(R.id.seekBar4); //Factor X1000

        btu_hr1 = findViewById(R.id.textView4); //BTU/Hr
        ton1  	= findViewById(R.id.textView8); //Toneladas de refrigeracion
        fact1 	= findViewById(R.id.textView10); //Factor de BTU/Hr/m².
        area1 	= findViewById(R.id.editText1); //valor de entrada del usuario.
        editText8   = findViewById(R.id.editText8);
        editText9   = findViewById(R.id.editText9);
        cfm_v	    = findViewById(R.id.textView6); //Pies cubico por minutos.
        cfm_area_v	= findViewById(R.id.textView7); // CFM/m².
        guia        = findViewById(R.id.button2);

        guia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guia1 = new Intent(CargaTermica.this, guia_carga_termica.class);
                startActivity(guia1);
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /**
         * Las siguientes instrucciones son para borrar el contenido de texto
         * de los TexView, ya que se dejan visible en el XML para visualizar su ubicacion.
         */
        btu_hr1.setText("");
        ton1.setText("");
        fact1.setText("0 ");
        cfm_v.setText("");
        cfm_area_v.setText("");



        this.barra_0.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress0, boolean fromTouch){
                valor0 = (Double.parseDouble(new Integer(progress0*1).toString()))*(0.1);//se multiplica por 0.1 barra X0.1
                fact1.setText(String.format("%.1f",(valor0+valor1+valor2+valor3+valor4))); //Suma las barras de busqueda.

                if(area1.getText().toString().length() > 0){ //Sentencia para evitar calculo indefinido cuando el área no tiene valor.
                    fact2 = Double.parseDouble(fact1.getText().toString()); //Se convierte la variable fcat1 de textView a variable Double para el proceso matemático.

                    area2 = Double.parseDouble(area1.getText().toString()); //Se convierte la variable area1 de edittext a variable Double para el proceso matemático.
                    btu_hr2 = area2 * fact2; //Calculo de capacidad en BTU/Hr.
                    ton2 = btu_hr2/12000;  //Calculo de capacidad en toneladas de refrigeracion.
                    cfm = ton2 * 400; //Calculo de los CFM.
                    cfm_area = cfm/area2; //Calculo de los CFM/area.

                    btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
                    ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
                    cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
                    cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar barra_0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar barra_0) {
                // TODO Auto-generated method stub

            }
        });

        this.barra_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress1, boolean fromTouch){
                valor1 = Double.parseDouble(new Integer(progress1*1).toString());
                fact1.setText(String.format("%.1f",(valor0+valor1+valor2+valor3+valor4))); //Suma las barras de busqueda.

                if(area1.getText().toString().length() > 0){ //Sentencia para evitar calculo indefinido cuando el área no tiene valor.
                    fact2 = Double.parseDouble(fact1.getText().toString()); //Se convierte la variable fcat1 de textView a variable Double para el proceso matemático.
                    area2 = Double.parseDouble(area1.getText().toString()); //Se convierte la variable area1 de textView a variable Double para el proceso matemático.
                    btu_hr2 = area2 * fact2; //Calculo de capacidad en BTU/Hr.
                    ton2 = btu_hr2/12000;  //Calculo de capacidad en toneladas de refrigeracion.
                    cfm = ton2 * 400; //Calculo de los CFM.
                    cfm_area = cfm/area2; //Calculo de los CFM/area.

                    btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
                    ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
                    cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
                    cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar barra_1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar barra_1) {
                // TODO Auto-generated method stub

            }
        });

        this.barra_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress2, boolean fromTouch){
                valor2 = Double.parseDouble(new Integer(progress2*10).toString());
                fact1.setText(String.format("%.1f",(valor0+valor1+valor2+valor3+valor4)));

                if(area1.getText().toString().length() > 0){ //Sentencia para evitar calculo indefinido cuando el área no tiene valor.
                    fact2 = Double.parseDouble(fact1.getText().toString()); //Se convierte la variable fcat1 de textView a variable Double para el proceso matemático.
                    area2 = Double.parseDouble(area1.getText().toString()); //Se convierte la variable area1 de textView a variable Double para el proceso matemático.
                    btu_hr2 = area2 * fact2; //Calculo de capacidad en BTU/Hr.
                    ton2 = btu_hr2/12000;  //Calculo de capacidad en toneladas de refrigeracion.
                    cfm = ton2 * 400; //Calculo de los CFM.
                    cfm_area = cfm/area2; //Calculo de los CFM/area.

                    btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
                    ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
                    cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
                    cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar barra_2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar barra_2) {
                // TODO Auto-generated method stub
            }
        });

        this.barra_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress3, boolean fromTouch){
                valor3 = Double.parseDouble(new Integer(progress3*100).toString());
                fact1.setText(String.format("%.1f",(valor0+valor1+valor2+valor3+valor4)));

                if(area1.getText().toString().length() > 0){ //Sentencia para evitar calculo indefinido cuando el área no tiene valor.
                    fact2 = Double.parseDouble(fact1.getText().toString()); //Se convierte la variable fcat1 de textView a variable Double para el proceso matemático.
                    area2 = Double.parseDouble(area1.getText().toString()); //Se convierte la variable area1 de textView a variable Double para el proceso matemático.
                    btu_hr2 = area2 * fact2; //Calculo de capacidad en BTU/Hr.
                    ton2 = btu_hr2/12000;  //Calculo de capacidad en toneladas de refrigeracion.
                    cfm = ton2 * 400; //Calculo de los CFM.
                    cfm_area = cfm/area2; //Calculo de los CFM/area.

                    btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
                    ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
                    cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
                    cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar barra_3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar barra_3) {
                // TODO Auto-generated method stub
            }
        });

        this.barra_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress4, boolean fromTouch){
                valor4 = Double.parseDouble(new Integer(progress4*1000).toString());
                fact1.setText(String.format("%.1f",(valor0+valor1+valor2+valor3+valor4)));

                if(area1.getText().toString().length() > 0){ //Sentencia para evitar calculo indefinido cuando el área no tiene valor.
                    fact2 = Double.parseDouble(fact1.getText().toString()); //Se convierte la variable fcat1 de textView a variable Double para el proceso matemático.
                    area2 = Double.parseDouble(area1.getText().toString()); //Se convierte la variable area1 de textView a variable Double para el proceso matemático.
                    btu_hr2 = area2 * fact2; //Calculo de capacidad en BTU/Hr.
                    ton2 = btu_hr2/12000;  //Calculo de capacidad en toneladas de refrigeracion.
                    cfm = ton2 * 400; //Calculo de los CFM.
                    cfm_area = cfm/area2; //Calculo de los CFM/area.

                    btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
                    ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
                    cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
                    cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar barra_4) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar barra_4) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void actualizar(View view){ //Boton de actualizar, cuando se ingresa un area por segunda vez

        if(area1.getText().toString().length() > 0){
            fact2 = Double.parseDouble(fact1.getText().toString());
            area2 = Double.parseDouble(area1.getText().toString());
            btu_hr2 = area2 * fact2;
            ton2 = btu_hr2/12000;
            cfm = ton2 * 400;
            cfm_area = cfm/area2;

            btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
            ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
            cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
            cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
        }
    }

    /**
     * Metodo para actualizar los resultados cuando se realize un cambio de area y
     * se presione la tecla done, hecho o listo, en el editText1.
     */
    public void edicion_area (View view){
        if(area1.getText().toString().length() > 0){
            fact2 = Double.parseDouble(fact1.getText().toString());
            area2 = Double.parseDouble(area1.getText().toString());
            btu_hr2 = area2 * fact2;
            ton2 = btu_hr2/12000;
            cfm = ton2 * 400;
            cfm_area = cfm/area2;

            btu_hr1.setText(String.format("%.0f", btu_hr2) + " Btu/hr."); //Muestra en valor de capacidad en BTU/Hr.
            ton1.setText(String.format("%.2f", ton2) + " Ton. Refrig."); //Muestra en valor de capacidad en toneladas de refrigeracion.
            cfm_v.setText(String.format("%.2f", cfm)+" CFM"); //Muestra los CFM.
            cfm_area_v.setText(String.format("%.2f", cfm_area)+" CFM/m²"); //Muestra los CFM/area.
        }
    }

    public void lado_a (View view){
        if (editText8.getText().toString().length()>0 & editText9.getText().toString().length()>0){
            Lado_A = Double.parseDouble(editText8.getText().toString());
            Lado_B = Double.parseDouble(editText9.getText().toString());
            area2 = Lado_A * Lado_B;
            area1.setText(String.format("%.2f",area2));
        }
    }

    public void lado_b (View view){
        if (editText8.getText().toString().length()>0 & editText9.getText().toString().length()>0){
            Lado_A = Double.parseDouble(editText8.getText().toString());
            Lado_B = Double.parseDouble(editText9.getText().toString());
            area2 = Lado_A * Lado_B;
            area1.setText(String.format("%.2f",area2));
        }
    }

}
