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

public class DimDucto extends AppCompatActivity {

    Spinner spin1;
    TextView tv11, tv12, tv13, tv14;
    TextView tv29, tv30, tv32, tv34, tv36;
    CheckBox chk1, chk2, chk3, chk4;
    EditText edt1, edt2, edt3, edt4, edt5, edt6;

    double[][]MA;
    double[][]VEL;
    double []PE;
    double []D;
    double []DIAEQV;
    double []D1;
    double [][]D2;
    double [][]D3;
    double flujoArie, perdidaEstatica, d1, d2; //CONTINUAR.
    double vel, vel2, dia, area1, area2, diaeqv;
    double diamin, diamax, vel_sup, vel_inf;
    double diafict1, diafict2, vel_fict1, vel_fict2;
    double perd1, perd2, perd_new;
    int i, z, v, s;
    int indexPerdida, b, g, n, w, m, h; //a => cuando la perd. introducida por el usuario-
    // coincide exactamente con unos de los valores de la matriz "PE".
    //b y g => cuando la perd. introducida por el usuario no-
    //coincide exactamente con unos de los valores de la matriz "PE".

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dim_ducto);

        spin1 = findViewById(R.id.spinner1);
        tv11 = findViewById(R.id.textViewDensidadAire);
        tv12 = findViewById(R.id.TextViewViscoCinematica);
        tv13 = findViewById(R.id.textViewCalorEspcf);
        tv14 = findViewById(R.id.textViewFacEnergia);
        tv29 = findViewById(R.id.textViewDiaEqvFinal);
        tv30 = findViewById(R.id.textViewAreaFlujo);
        tv32 = findViewById(R.id.textViewVelFluidoFinal);
        tv34 = findViewById(R.id.textViewNumReynolds);
        tv36 = findViewById(R.id.textViewPerdidaFriccion);

        chk1 = findViewById(R.id.checkBoxCaudal);
        chk2 = findViewById(R.id.checkBoxPedEstatica);
        chk3 = findViewById(R.id.checkBoxVelocidad);
        chk4 = findViewById(R.id.checkBoxDiaEqv);

        edt1 = findViewById(R.id.editTextCFM);
        edt2 = findViewById(R.id.editTextPerdEstatica);
        edt3 = findViewById(R.id.editTextVelocidad);
        edt5 = findViewById(R.id.editTextLadoADucto);
        edt4 = findViewById(R.id.editTextDiaEqv);
        edt6 = findViewById(R.id.editTextLadoBDucto);

        //Seleccion de opciones del Spinner (condiciones)
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                //MUESTRA LA SELECCION EN UN MENSAJE TEMPORAL.
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                String select1 = spin1.getSelectedItem().toString();
                if (select1.equals("10°C(50°F) Aire 97% RH 1 atm"))
                {
                    tv11.setText("0.0778 ");    tv12.setText("1.5285x10ˉ⁴ ");
                    tv13.setText("0.24 ");      tv14.setText("1.09 ");
                }

                if (select1.equals("20°C(68°F) Aire STP"))
                {
                    tv11.setText("0.0752 ");    tv12.setText("1.6253x10ˉ⁴ ");
                    tv13.setText("0.24 ");      tv14.setText("1.08 ");
                }

                if (select1.equals("23.9°C(75°F) Aire 50% RH 1 atm"))
                {
                    tv11.setText("0.0739 ");    tv12.setText("1.6738x10ˉ⁴ ");
                    tv13.setText("0.24 ");      tv14.setText("1.05 ");
                }

                if (select1.equals("40°C(104°F) Aire 23% RH 1 atm"))
                {
                    tv11.setText("0.0704 ");    tv12.setText("1.8191x10ˉ⁴ ");
                    tv13.setText("0.24 ");      tv14.setText("1.01 ");
                }

                if (select1.equals("50°C(122°F) Aire 11% RH 1 atm"))
                {
                    tv11.setText("0.0682 ");    tv12.setText("1.9267x10ˉ⁴ ");
                    tv13.setText("0.24 ");      tv14.setText("0.96 ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // vacio
            }
        });

        MA=new double[31][37]; //MA[i][j][y], [DIAMETRO], [FILAS-PERDIDAS],[= CFM]
        this.valores_1(); //CARGA LA MATRIS CON LOS VALORES, DIAMETROS, CFM Y PERDIDAS.

        VEL=new double[35][37]; //VEL[i][j][y], [VELOCIDAD], [FILAS-PERDIDAS],[= CFM]
        this.valores_2(); //CARGA LA MATRIS CON LOS VALORES, VELOCIDAD, CFM Y PERDIDAS.

        PE = new double [37]; //Matriz con los valores de las perdidas
        this.valores_3();

        D = new double [24]; //Matriz con los valores de diametros equivalente.
        this.valores_4();

        D1 = new double [80]; //Matriz con los valores de lado de ducto en plg.
        this.valores_5();

        D2 = new double [24][80]; //Matriz con los valores de lado de ducto en plg.
        this.valores_6();

        D3 = new double [24][45]; //Matriz con los valores de lado de ducto en plg.
        this.valores_7();

        DIAEQV = new double [31]; //Matriz con los valores de dia.eqv de la grafica de perdidas.
        this.valores_8();

        chk1.setClickable(false);
        chk2.setClickable(false);
        chk3.setClickable(true);
        chk4.setClickable(true);

    }

    public void calcular(View view)
    {
        if (chk1.isChecked() == true & chk2.isChecked() == true) //Seleccion de Caudal y perdida
        {
            // Iniciacion para borrar los valores anteriores.
            edt3.setText(String.format(""));
            edt4.setText(String.format(""));
            vel = 0;
            dia = 0;

            if (edt1.getText().toString().length() > 0 & edt2.getText().toString().length() > 0)
            {
                flujoArie = Double.parseDouble(edt1.getText().toString()); //CFM, conversion de variable.
                perdidaEstatica = Double.parseDouble(edt2.getText().toString()); //Perdida, conversion de variable.

                if(flujoArie <= 75000 & perdidaEstatica <= 6.75) //Limite maximo (75,000 CFM), Limite maximo de perdida(6.75 plg/100ft),
                {
                    if (flujoArie >= 10 & perdidaEstatica >= 0.01) //Limite minimo (10 CFM), Limite minimo de perdida(0.01 plg/100ft),
                    {
                        /*
                         * Metodo para determinar si la perdida introducida por el usuario concide con los valores exacto
                         * de las matrices, correspondiente a un valor fijo del grafico de perdidas y friccion.
                         * De no coincidir se guardaran los valores fijos mayor y menor a los del usuario, para utilizarlo
                         * en las interpolaciones.
                         * a => cuando la perd. introducida por el usuario
                         * coincide exactamente con unos de los valores de la matriz "PE".
                         * b y g => cuando la perd. introducida por el usuario no-
                         * coincide exactamente con unos de los valores de la matriz "PE".
                         */
                        indexPerdida = 0;
                        int x=0;
                        while (x < PE.length)
                        {
                            if (PE[x] == perdidaEstatica)
                            {
                                indexPerdida = x; //Perdida exacta
                                /*
                                 * Metodo para cuando la perdida coincide, Caso1.
                                 * para el caso1, "a" es distinto de 0 "cero" (a!=0)
                                 */
                            }
                            x++;
                        }

                        //cuando la perdida no es exacta
                        if (indexPerdida == 0)
                        {
                            for (int t = 0; t < 37; t++)
                            {
                                if (PE[t] < perdidaEstatica)
                                {
                                    b = t; //valor menor que del usuario
                                    g = (t-1); //valor mayor que del usuario
                                    break;
                                    /**
                                     * Metodo para cuando la perdida no coincide, Caso2, caso3.
                                     */
                                }
                            }
                        }
                        /**
                         * Final
                         */
                        if (indexPerdida != 0) //Caso Ideal, la perdida y los CFM introducido coincide con los valores de la matriz.
                        {
                            for (i = 0; i < 31; i++) //diametro
                            {
                                if (MA[i][indexPerdida] == flujoArie) //x1-cfm.
                                {
                                    this.diametros_1(); //Asignacion de diametros.
                                    edt4.setText(String.format("%.2f", dia)); //Muestra el diametro.
                                    this.area1(); //Calculo del area1.
                                    this.cal_vel(); //Proceso para determinar la velocidad, segun la perdida y caudal seleccionado.
                                }
                            }
                        }

                        if(dia == 0 )
                        {
                            this.interpolacion_1();
                        }

                    }
                }
                this.alertas();//Mensajes de alerta.
            }
            this.toast_show(); //Mensaje flotantes.
        }
        this.num_reynolds(); //calculo del numero de Reynolds.

        tv36.setText(String.format("%.3f", perdidaEstatica)+" "); //muestra la perdida / 100 ft en la parte inferior

        //Instrucciones para dimesionar el ducto retangular en caso de que este tenga valor.
        if (edt5.getText().toString().length() > 0)
        {
            this.dim1(view);
        }
    }

    //Interpolacion para matriz con valores de, perdida, caudal, diametro equivalente.
    public void interpolacion_1(){

        //(CASO 1: perd. coincide, flujo no)
        if (indexPerdida!=0) //significa que contiene un valor, debido a la comparacion con la matriz "PE".
        {
            for (i = 0; i < 31; i++) //diametro
            {
                if (MA[i][indexPerdida] > flujoArie) //Comparacion del valor mayor, de los CFM
                {
                    if (MA[i-1][indexPerdida] < flujoArie) //Comparacion del valor menor
                    {
                        this.diametros_3(); //Asignacion de diametros.

                        double c = MA[i][indexPerdida] - MA[i-1][indexPerdida]; //Diferencia entre los Cfm, superior e inferior
                        double d = (flujoArie - MA[i-1][indexPerdida]) / c;  //Diferencia entre cfm inferior y del usuario, entre c, para sbaer la fraccion.

                        double e = (diamax - diamin) * d;
                        dia = diamin + e;  //Valor del diametro interpolado.

                        edt4.setText(String.format("%.1f", dia)); //Muestra el diametro
                        this.area1(); //Calculo del area1.
                        this.cal_vel(); //Proceso para determinar la velocidad, segun la perdida y caudal seleccionado.
                        break;
                    }
                }
            }
        }//Salida Caso 1

        //(CASO 2: perd. no coincide, flujo si)
        //Este metodo funciona, solo si el valor de los cfm introducido se encuentra en ambos diametros.
        /*
        Se elimino caso 2, ya que este posiblemente numca ocurrira, 2017/jul/07.
         */

        //Caso 3, diametro ficticio #1.
        if (dia == 0) //Sentencia, de ser cierta, indica que no se a realizado ninguna interpolacion anterior a esta.
        {
            for (i=0; i<31; i++) //diametro
            {
                if (MA[i][g] > flujoArie) //El CFM de la perdida mayor debe ser mayor al CFM introducido por el usuario.
                {
                    if (MA[i-1][g] < flujoArie) //Comparacion de los CFM del (i-1)-diametro anterior, a "i", los CFM del (i-1) deben ser menor a los CFM del usuario
                    {
                        //Interpolacion para encontrar el diametro ficticio #1.
                        this.diametros_3(); //Asignacion de diametros.

                        double c = MA[i][g] - MA[i-1][g]; //diferencia de CFM, para usarlo como unidad.
                        double d = (flujoArie - MA[i-1][b]) / c;
                        double e = d * (diamax - diamin);
                        diafict1 = diamin + e; //diametro ficticio #1, que sera utilizado mas adelante.
                        perd1 = PE[g];   //perdida del diametro ficticio #1.
                        break;
                    }
                }
            }//Salida Caso 3, diametro ficticio #1.

            //Caso 3, diametro ficticio #2.
            for (i=0; i<31; i++) //diametro
            {
                if (MA[i][b] > flujoArie)
                {
                    if (MA[i-1][b] < flujoArie)
                    {
                        //Interpolacion para encontrar el diametro ficticio #2.
                        this.diametros_3();

                        double c = MA[i][b] - MA[i-1][b];
                        double d = (flujoArie - MA[i-1][b]) / c;
                        double e = d * (diamax - diamin);
                        diafict2 = diamin + e;
                        perd2 = PE[b];
                        break;
                    }
                }
            }//Salida Caso 3, diametro ficticio #2.

            //Interpolacion final del caso 3
            double m = (perdidaEstatica - perd2) / (perd1 - perd2);
            double f = m * Math.abs((diafict1 - diafict2));

            if (diafict1 < diafict2)
            {
                dia =  diafict1 + f;
            }

            if (diafict2 < diafict1)
            {
                dia =  diafict2 + f;
            }

            edt4.setText(String.format("%.2f", dia)); //Muestra el diametro
            this.area1(); //Calculo del area1.
            this.cal_vel(); //Proceso para determinar la velocidad, segun la perdida y caudal seleccionado.
        }
    } // salida de interpolacion.

    public void cal_vel(){

        //Caso Ideal, cuando el caudal y la perdida coinciden con los valores de la matriz de velocidad.

        if(indexPerdida!=0)
        {
            for (v = 0; v < 35; v++) //Velocidad
            {
                if (VEL[v][indexPerdida] == flujoArie)
                {
                    this.velocidad_1();  //Extrae la velocidad correspondiente
                    edt3.setText(String.format("%.1f", vel)); //muestra la velocidad
                    break;
                }
            }
        }

        //Sentencia para determiar cuando no hay coincidencia en la matriz y requiere interpolacion.
        if (vel == 0)
        {
            this.cal_vel_inter();
        }

        this.vel_2(); //velocidad secundaria.
    }

    public void cal_vel_inter() {
        //Caso 1-vel, perdida concide, flujo no
        if (indexPerdida != 0) //significa que "a" contiene un valor, debido a la comparacion con la matriz "PE".
        {
            for (v = 0; v < 35; v++) //Matriz de Velocidad
            {
                if (VEL[v][indexPerdida] > flujoArie) //1ª etapa, los cfm encontrados, deben ser mayor a los cfm del usuario.
                {
                    if (VEL[v - 1][indexPerdida] < flujoArie) //2ª etapa, los cfm encontrados, deben ser menor a los cfm del usuario.
                    {
                        double c = VEL[v][indexPerdida] - VEL[v - 1][indexPerdida]; //Diferencia entre los cfm superiores e inferiores.
                        double d = (flujoArie - VEL[v - 1][indexPerdida]) / c; //Diferencia entre los cfm de usuario y cfm inferiores, entre c, para saber la fraccion.

                        this.velocidad_2(); //cargar velocidades
                        double e = (vel_sup - vel_inf) * d;
                        vel = vel_inf + e;
                        edt3.setText(String.format("%.1f", vel)); //muestra la velocidad
                        break;
                        /**
                         * Revisar caso 1 vel 2017-07-03, resuelto
                         */
                    }
                }
            }
        }//Caso 1-vel.

        //Caso 2-vel, perdida no concide, flujo si.
        //Este metodo funciona, solo si el valor de los cfm introducido se encuentra en ambas matrices de velocidad.
        /*
        Se elimino caso 2-vel, ya que este posiblemente numca ocurrira, 2017/jul/07.
         */

        //Caso 3-vel, perdida no concide, flujo tampoco.
        if(vel==0)
        {
            //Calculo de velocidad ficticia #1
            for(v = 0; v < 35; v++) //Matriz de Velocidad
            {
                if(VEL[v][g] > flujoArie) //Verificacion de los cfm, deben ser mayor que los introducidos por el usuario.
                {
                    if (VEL[v-1][g] < flujoArie) //Los CFm deben ser menor a los introducidos por el usuario.
                    {
                        //interpolacion para determinar la velocidad ficticia #1.
                        this.velocidad_2(); //asignacion de los valores de velocida
                        //cfm sup       cfm inferior
                        double c = VEL[v][g] - VEL[v-1][g]; //Diferencia de velocidad superior e inferior.
                        double d = (flujoArie - VEL[v-1][g]) / c;
                        double e = d * (vel_sup - vel_inf);
                        vel_fict1 = vel_inf + e; //Velocidad ficticia #1, sera utilizada mas adelante.
                        perd1 = PE[g]; //valor para ser utilizada en el calculo final.
                        break;
                    }
                }
            } //Salida de velocidad ficticia #1.


            //Caso 3-vel, velocidad ficticia #2.
            for(v = 0; v < 35; v++) //Matriz de Velocidad
            {
                if (VEL[v][b] > flujoArie) //Los cfm deben ser mayor a los introducidos por el usuario.
                {
                    if (VEL[v-1][b] < flujoArie) //los cfm deben ser menor a los del usuario.
                    {
                        //interpolacion para determinar la velocidad ficticia #2.
                        this.velocidad_2(); //asignacion de los valores de velocida
                        //Velocida sup   velocida inferior
                        double c = VEL[v][b] - VEL[v-1][b];
                        double d = (flujoArie - VEL[v-1][b]) / c;
                        double e = d * (vel_sup - vel_inf);
                        vel_fict2 = vel_inf + e;
                        perd2 = PE[b]; //valor para ser utilizada en el calculo final.
                        break;
                    }
                }
            }

            double m = (perdidaEstatica - perd2)/(perd1 - perd2);
            double f = m * (vel_fict1 - vel_fict2);
            vel = vel_fict2 + f;
            edt3.setText(String.format("%.1f", vel)); // Muestra la velocidad.
            //Toast toast3 = Toast.makeText(getApplicationContext(), "Caso 3-vel", Toast.LENGTH_SHORT);
            //toast3.show();
        }
    }

    public void vel_2(){ //Calculo de la velocidad secundaria en funcion del diametro eqv. encontrado

        //Calculo de la velocidad del aire en funcion del diametro encontrado en la grafica.
        this.area1();
        vel2 = flujoArie/area1;
        tv32.setText(String.format("%.0f", vel2)+" ");
    }

    public void area1() { //calculo del area del ducto equivalente

        area1 = (Math.PI * Math.pow(dia,2))/576; //dia en plg, area1 en ft²
        tv30.setText(String.format("%.4f", area1)+" ");

    }

    public void dim1(View view) { //dimensionamiento del ducto rectangular.

        if (edt1.getText().toString().length()>0 & edt2.getText().toString().length()>0 & edt4.getText().toString().length()>0 & edt5.getText().toString().length()>0)
        {
            d1 = Double.parseDouble(edt5.getText().toString()); //primera dimension del ducto rectangula del usuario

            if (d1 < 100)
            {
                n = 0;
                w = 0;
                m = 0;


                diaeqv = Math.round(dia); //Trunca el valor de diametro a un numero entero, para ser utilizado en la grafica.

                /** #001
                 * Metodo para determinar si el diametro equivalente calculado, concide con los valores exacto
                 * de la grafica de ducto rectangular, de no coincider se buscaran un valor mayor y menor al
                 * al diametro equivalente.
                 */

                //Caso ideal
                //cuando el diametro equivalente es exacto, a un valor de la grafica de ducto rectangular.
                for (int x = 0; x < 24; x++) {
                    if (D[x] == diaeqv) {
                        n = x; //diametro equivalente exacto a los valores de la grafica de ducto retangular
                        break;
                    }
                }

                //Caso interpolacion
                //cuando el diametro equivalente no coincide con un valor de la grafica de ducto rectangular.
                if (n == 0) {
                    for (int x = 0; x < 24; x++) {
                        if (D[x] > diaeqv) {
                            w = x; //diametro equivalente mayor
                            m = (x - 1); //diametro equivalente menor
                            break;
                        }
                    }
                }

                //comparacion de un lado del ducto introducido por el usuaraio, caso ideal
                for (int k = 0; k < 80; k++) {
                    if (D1[k] == d1) {
                        h = k; //caso ideal, cuando el valor introducido por el usuario concuerda con un valor de la grafica.
                        break;
                    }
                }

                //Caso ideal
                if (n != 0) {
                    d2 = D2[n][h];  //[n]=> numero del diametro, [h]=> numero de fila

                    /* #002
                     * La siguiente sentencia es para hacer un cambio de eje en la comparacion de los valores
                     * ya en ciertos casos el valor vertical, no tiene valor horizontal para ciertos diametros eqv.
                     * por ende se pasa a usar el eje horizontal correspondiente a la matriz "D3".
                     */
                    if (d2 == 0) //cuando el valor vertical, no tiene valor en el grafico.
                    {
                        d2 = D3[n][h]; //Utiliza los valores horizontales de la grafica de ducto rectangular.
                    }
                    edt6.setText(String.format("%.0f", d2));
                }

                //Interpolacion, cuando el diametro equivalente no coincide con los diametros de la grafica.
                if (n == 0)
                {
                    double t = D[w] - D[m]; //diametro eqv mayor menos dia eqv menor
                    double y = (diaeqv - D[m]) / t;
                    double u = (D2[w][h] - D2[m][h]) * y;
                    d2 = D2[m][h] + u;

                    /* #003
                     * Igual que en el inciso #002
                     */
                    if (d2 == 0)
                    {
                        u = (D3[w][h] - D3[m][h]) * y;
                        d2 = D3[m][h] + u;
                    }

                    /** #004
                     *  sentencia para cuando el diametro mayor no tiene valor en el eje horizontal, se procede
                     *  hacer el cambio al eje vertical utilizando la matriz "D3".
                     */
                    if (D2[w][h] == 0)
                    {
                        double t2 = D[w] - D[m]; //diametro eqv mayor menos dia eqv menor
                        double y2 = (diaeqv - D[m]) / t2;
                        double u2 = (D3[w][h] - D3[m][h]) * y2;
                        d2 = D3[m][h] + u2;
                    }

                    edt6.setText(String.format("%.0f", d2));
                }
                tv29.setText(String.format("%.1f", diaeqv) + " "); //Revisar **
                dia = diaeqv; //se le asigna nuevo valor al diametro.
                this.area1(); //recalcula el area con nuevo valor del diametro equivalente.
                this.vel_2(); //recalcula el nuevo valor de la velocidad en funcion al nuevo diametro equivalente.
                vel = vel2; //Se le asigna el valor de vel2, para el recalculo del numero reynolds.
                this.num_reynolds(); //recalculo del numero de reynolds.
            }
        }

        if (d1 > 100) //cuando el valor esta fuera de rango
        {
            this.alertas();//Mensajes de alerta.
        }


    } //Salida de dim1

    public void dim2(View view){ //No se esta utilizando
        /**
         * Instrucciones para recalcular los valores del aire, en caso de que modifique por el usuario el
         * segundo lado (lado B) del ducto rectangular.
         * Se calcula el area, luego se determina un diametro equivalente, luego se entra con el dia.eqv. a
         * la matriz de MA para encontrar la concidencia de los Cfm y el dia.eqv. luego por ultimo se entra la
         * a la Matriz PE para determinar la perdida y mostrarla en el textView36.
         */
        perd_new = 0; //Reinicio de valor
        n = 0; //Reinicio de valor

        if(edt1.getText().toString().length()>0 & edt5.getText().toString().length()>0 & edt6.getText().toString().length()>0 )
        {
            d1 = Double.parseDouble(edt5.getText().toString()); //primera dimension del ducto rectangula del usuario
            d2 = Double.parseDouble(edt6.getText().toString()); //segunda dimension del ducto rectangula del usuario

            diaeqv = Math.round(2*(Math.sqrt((d1*d2)/Math.PI))); //calculo del diametro equivalente en plg.
            area2 = (Math.PI * Math.pow(diaeqv,2))/576; //diaeqv en plg, area1 en ft²

            tv29.setText(String.format("%.2f", diaeqv)+" ");
            tv30.setText(String.format("%.4f", area2)+" ");
            tv32.setText(String.format("%.0f", (flujoArie/area2))+" ");

            //Calculo del nuemro Reynold
            double Re = (((flujoArie/area2)/60)*(diaeqv/12))/(1.6738e-4);
            tv34.setText(String.format("%.0f", Re)+" ");

            for (int x = 0; x < 31; x++)
            {
                if (DIAEQV[x] == diaeqv)
                {
                    n = x; //diametro equivalente exacto a los valores de la grafica de perdidas.
                    break;
                }
            }

            //Caso interpolacion
            //cuando el diametro equivalente no coincide con un valor de la grafica de perdidas.
            if (n == 0)
            {
                for (int x = 0; x < 31; x++)
                {
                    if (DIAEQV[x] > diaeqv)
                    {
                        double ds = x; //diametro equivalente mayor
                        double di = (x-1); //diametro equivalente menor
                        n = x; //se escogue que este sea el diametro por desicion propia.
                        break;
                    }
                }
            }

            //Esta opcion nunca ocurria posiblemente
            for(int r = 0; r < 37; r++)
            {
                if(MA[n][r] == flujoArie)
                {
                    PE[r]=perd_new;
                    tv36.setText(String.format("%.3f", perd_new)+" ");
                    break;
                }
            }

            if(perd_new==0) //Pendiente mejorar interpolacion
            {
                for(int r = 0; r < 37; r++)
                {
                    if(MA[n][r] < flujoArie)
                    {
                        perd_new = PE[r];
                        tv36.setText(String.format("%.3f", perd_new)+" ");
                        //tv34.setText(Integer.toString(n)+"/"+Integer.toString(r));  //Instruccion para revisar el codigo.
                        break;
                    }
                }
            }
        }
    }

    public void num_reynolds(){

        double Re = ((vel/60)*(dia/12))/(1.6738e-4);
        tv34.setText(String.format("%.0f", Re)+" ");
    }

    public void diametros_1(){ //Diametros de la matriz de valores
        switch (i)
        {
            case 0 : dia = 1.5; break; //diametro equivale a 1.5 plg
            case 1 : dia = 2.0; break; //diametro equivale a 2.0 plg.
            case 2 : dia = 3.0; break; //diametro equivale a 3.0 plg.
            case 3 : dia = 4.0; break; //diametro equivale a 4.0 plg.
            case 4 : dia = 5.0; break; //diametro equivale a 5.0 plg.
            case 5 : dia = 6.0; break; //diametro equivale a 6.0 plg.
            case 6 : dia = 7.0; break; //diametro equivale a 7.0 plg.
            case 7 : dia = 8.0; break; //diametro equivale a 8.0 plg.
            case 8 : dia = 9.0; break; //diametro equivale a 9.0 plg.
            case 9 : dia = 10.0; break; //diametro equivale a 10.0 plg.
            case 10 : dia = 12.0; break; //diametro equivale a 12.0 plg.
            case 11 : dia = 14.0; break; //diametro equivale a 14.0 plg.
            case 12 : dia = 16.0; break; //diametro equivale a 16.0 plg.
            case 13 : dia = 18.0; break; //diametro equivale a 18.0 plg.
            case 14 : dia = 20.0; break; //diametro equivale a 20.0 plg.
            case 15 : dia = 22.0; break; //diametro equivale a 22.0 plg.
            case 16 : dia = 24.0; break; //diametro equivale a 24.0 plg.
            case 17 : dia = 26.0; break; //diametro equivale a 26.0 plg.
            case 18 : dia = 28.0; break; //diametro equivale a 28.0 plg.
            case 19 : dia = 30.0; break; //diametro equivale a 30.0 plg.
            case 20 : dia = 32.0; break; //diametro equivale a 32.0 plg.
            case 21 : dia = 34.0; break; //diametro equivale a 34.0 plg.
            case 22 : dia = 36.0; break; //diametro equivale a 36.0 plg.
            case 23 : dia = 38.0; break; //diametro equivale a 38.0 plg.
            case 24 : dia = 40.0; break; //diametro equivale a 40.0 plg.
            case 25 : dia = 45.0; break; //diametro equivale a 45.0 plg.
            case 26 : dia = 50.0; break; //diametro equivale a 50.0 plg.
            case 27 : dia = 55.0; break; //diametro equivale a 55.0 plg.
            case 28 : dia = 60.0; break; //diametro equivale a 60.0 plg.
            case 29 : dia = 70.0; break; //diametro equivale a 70.0 plg.
            case 30 : dia = 80.0; break; //diametro equivale a 80.0 plg.
        }
    }

    public void diametros_3(){ //Diametros del caso 1, caso 3, para el diametros ficticio #1.

        switch (i) //Instruccion para buscar del diametro menor
        {
            case 0 : diamax = 1.5; break; //diametro equivale a 1.5 plg
            case 1 : diamax = 2.0; break; //diametro equivale a 2.0 plg.
            case 2 : diamax = 3.0; break; //diametro equivale a 3.0 plg.
            case 3 : diamax = 4.0; break; //diametro equivale a 4.0 plg.
            case 4 : diamax = 5.0; break; //diametro equivale a 5.0 plg.
            case 5 : diamax = 6.0; break; //diametro equivale a 6.0 plg.
            case 6 : diamax = 7.0; break; //diametro equivale a 7.0 plg.
            case 7 : diamax = 8.0; break; //diametro equivale a 8.0 plg.
            case 8 : diamax = 9.0; break; //diametro equivale a 9.0 plg.
            case 9 : diamax = 10.0; break; //diametro equivale a 10.0 plg.
            case 10 : diamax = 12.0; break; //diametro equivale a 12.0 plg.
            case 11 : diamax = 14.0; break; //diametro equivale a 14.0 plg.
            case 12 : diamax = 16.0; break; //diametro equivale a 16.0 plg.
            case 13 : diamax = 18.0; break; //diametro equivale a 18.0 plg.
            case 14 : diamax = 20.0; break; //diametro equivale a 20.0 plg.
            case 15 : diamax = 22.0; break; //diametro equivale a 22.0 plg.
            case 16 : diamax = 24.0; break; //diametro equivale a 24.0 plg.
            case 17 : diamax = 26.0; break; //diametro equivale a 26.0 plg.
            case 18 : diamax = 28.0; break; //diametro equivale a 28.0 plg.
            case 19 : diamax = 30.0; break; //diametro equivale a 30.0 plg.
            case 20 : diamax = 32.0; break; //diametro equivale a 32.0 plg.
            case 21 : diamax = 34.0; break; //diametro equivale a 34.0 plg.
            case 22 : diamax = 36.0; break; //diametro equivale a 36.0 plg.
            case 23 : diamax = 38.0; break; //diametro equivale a 38.0 plg.
            case 24 : diamax = 40.0; break; //diametro equivale a 40.0 plg.
            case 25 : diamax = 45.0; break; //diametro equivale a 45.0 plg.
            case 26 : diamax = 50.0; break; //diametro equivale a 50.0 plg.
            case 27 : diamax = 55.0; break; //diametro equivale a 55.0 plg.
            case 28 : diamax = 60.0; break; //diametro equivale a 60.0 plg.
            case 29 : diamax = 70.0; break; //diametro equivale a 70.0 plg.
            case 30 : diamax = 80.0; break; //diametro equivale a 80.0 plg.
        }

        z = i - 1; //Instruccion para buscar el diametro minimo

        switch (z) {
            case 0 : diamin = 1.5; break; //diametro equivale a 1.5 plg
            case 1 : diamin = 2.0; break; //diametro equivale a 2.0 plg.
            case 2 : diamin = 3.0; break; //diametro equivale a 3.0 plg.
            case 3 : diamin = 4.0; break; //diametro equivale a 4.0 plg.
            case 4 : diamin = 5.0; break; //diametro equivale a 5.0 plg.
            case 5 : diamin = 6.0; break; //diametro equivale a 6.0 plg.
            case 6 : diamin = 7.0; break; //diametro equivale a 7.0 plg.
            case 7 : diamin = 8.0; break; //diametro equivale a 8.0 plg.
            case 8 : diamin = 9.0; break; //diametro equivale a 9.0 plg.
            case 9 : diamin = 10.0; break; //diametro equivale a 10.0 plg.
            case 10 : diamin = 12.0; break; //diametro equivale a 12.0 plg.
            case 11 : diamin = 14.0; break; //diametro equivale a 14.0 plg.
            case 12 : diamin = 16.0; break; //diametro equivale a 16.0 plg.
            case 13 : diamin = 18.0; break; //diametro equivale a 18.0 plg.
            case 14 : diamin = 20.0; break; //diametro equivale a 20.0 plg.
            case 15 : diamin = 22.0; break; //diametro equivale a 22.0 plg.
            case 16 : diamin = 24.0; break; //diametro equivale a 24.0 plg.
            case 17 : diamin = 26.0; break; //diametro equivale a 26.0 plg.
            case 18 : diamin = 28.0; break; //diametro equivale a 28.0 plg.
            case 19 : diamin = 30.0; break; //diametro equivale a 30.0 plg.
            case 20 : diamin = 32.0; break; //diametro equivale a 32.0 plg.
            case 21 : diamin = 34.0; break; //diametro equivale a 34.0 plg.
            case 22 : diamin = 36.0; break; //diametro equivale a 36.0 plg.
            case 23 : diamin = 38.0; break; //diametro equivale a 38.0 plg.
            case 24 : diamin = 40.0; break; //diametro equivale a 40.0 plg.
            case 25 : diamin = 45.0; break; //diametro equivale a 45.0 plg.
            case 26 : diamin = 50.0; break; //diametro equivale a 50.0 plg.
            case 27 : diamin = 55.0; break; //diametro equivale a 55.0 plg.
            case 28 : diamin = 60.0; break; //diametro equivale a 60.0 plg.
            case 29 : diamin = 70.0; break; //diametro equivale a 70.0 plg.
            case 30 : diamin = 80.0; break; //diametro equivale a 80.0 plg.
        }
    }

    public void velocidad_1(){  //Valores de velocidad en el caso ideal.

        switch (v)
        {
            case 0 : vel = 200; break; //velocidad equivale a 200 FPM
            case 1 : vel = 300; break; //velocidad equivale a 300 FPM.
            case 2 : vel = 400; break; //velocidad equivale a 400 FPM.
            case 3 : vel = 500; break; //velocidad equivale a 500 FPM.
            case 4 : vel = 600; break; //velocidad equivale a 600 FPM.
            case 5 : vel = 700; break; //velocidad equivale a 700 FPM.
            case 6 : vel = 800; break; //velocidad equivale a 800 FPM.
            case 7 : vel = 900; break; //velocidad equivale a 900 FPM.
            case 8 : vel = 1000; break; //velocidad equivale a 1000 FPM.
            case 9 : vel = 1200; break; //velocidad equivale a 1200 FPM.
            case 10 : vel = 1400; break; //velocidad equivale a 1400 FPM.
            case 11 : vel = 1600; break; //velocidad equivale a 1600 FPM.
            case 12 : vel = 1800; break; //velocidad equivale a 1800 FPM.
            case 13 : vel = 2000; break; //velocidad equivale a 2000 FPM.
            case 14 : vel = 2200; break; //velocidad equivale a 2200 FPM.
            case 15 : vel = 2400; break; //velocidad equivale a 2400 FPM.
            case 16 : vel = 2600; break; //velocidad equivale a 2600 FPM.
            case 17 : vel = 2800; break; //velocidad equivale a 2800 FPM.
            case 18 : vel = 3000; break; //velocidad equivale a 3000 FPM.
            case 19 : vel = 3200; break; //velocidad equivale a 3200 FPM.
            case 20 : vel = 3400; break; //velocidad equivale a 3400 FPM.
            case 21 : vel = 3600; break; //velocidad equivale a 3600 FPM.
            case 22 : vel = 3800; break; //velocidad equivale a 3800 FPM.
            case 23 : vel = 4000; break; //velocidad equivale a 4000 FPM.
            case 24 : vel = 4500; break; //velocidad equivale a 4500 FPM.
            case 25 : vel = 5000; break; //velocidad equivale a 5000 FPM.
            case 26 : vel = 5500; break; //velocidad equivale a 5500 FPM.
            case 27 : vel = 6000; break; //velocidad equivale a 6000 FPM.
            case 28 : vel = 6500; break; //velocidad equivale a 6500 FPM.
            case 29 : vel = 7000; break; //velocidad equivale a 7000 FPM.
            case 30 : vel = 7500; break; //velocidad equivale a 7500 FPM.
            case 31 : vel = 8000; break; //velocidad equivale a 8000 FPM.
            case 32 : vel = 9000; break; //velocidad equivale a 9000 FPM.
            case 33 : vel = 10000; break; //velocidad equivale a 10000 FPM.
            case 34 : vel = 12000; break; //velocidad equivale a 12000 FPM.
        }
    }

    public void velocidad_2(){

        switch (v) //velocida superior
        {
            case 0 : vel_sup = 200; break; //velocidad equivale a 200 FPM
            case 1 : vel_sup = 300; break; //velocidad equivale a 300 FPM.
            case 2 : vel_sup = 400; break; //velocidad equivale a 400 FPM.
            case 3 : vel_sup = 500; break; //velocidad equivale a 500 FPM.
            case 4 : vel_sup = 600; break; //velocidad equivale a 600 FPM.
            case 5 : vel_sup = 700; break; //velocidad equivale a 700 FPM.
            case 6 : vel_sup = 800; break; //velocidad equivale a 800 FPM.
            case 7 : vel_sup = 900; break; //velocidad equivale a 900 FPM.
            case 8 : vel_sup = 1000; break; //velocidad equivale a 1000 FPM.
            case 9 : vel_sup = 1200; break; //velocidad equivale a 1200 FPM.
            case 10 : vel_sup = 1400; break; //velocidad equivale a 1400 FPM.
            case 11 : vel_sup = 1600; break; //velocidad equivale a 1600 FPM.
            case 12 : vel_sup = 1800; break; //velocidad equivale a 1800 FPM.
            case 13 : vel_sup = 2000; break; //velocidad equivale a 2000 FPM.
            case 14 : vel_sup = 2200; break; //velocidad equivale a 2200 FPM.
            case 15 : vel_sup = 2400; break; //velocidad equivale a 2400 FPM.
            case 16 : vel_sup = 2600; break; //velocidad equivale a 2600 FPM.
            case 17 : vel_sup = 2800; break; //velocidad equivale a 2800 FPM.
            case 18 : vel_sup = 3000; break; //velocidad equivale a 3000 FPM.
            case 19 : vel_sup = 3200; break; //velocidad equivale a 3200 FPM.
            case 20 : vel_sup = 3400; break; //velocidad equivale a 3400 FPM.
            case 21 : vel_sup = 3600; break; //velocidad equivale a 3600 FPM.
            case 22 : vel_sup = 3800; break; //velocidad equivale a 3800 FPM.
            case 23 : vel_sup = 4000; break; //velocidad equivale a 4000 FPM.
            case 24 : vel_sup = 4500; break; //velocidad equivale a 4500 FPM.
            case 25 : vel_sup = 5000; break; //velocidad equivale a 5000 FPM.
            case 26 : vel_sup = 5500; break; //velocidad equivale a 5500 FPM.
            case 27 : vel_sup = 6000; break; //velocidad equivale a 6000 FPM.
            case 28 : vel_sup = 6500; break; //velocidad equivale a 6500 FPM.
            case 29 : vel_sup = 7000; break; //velocidad equivale a 7000 FPM.
            case 30 : vel_sup = 7500; break; //velocidad equivale a 7500 FPM.
            case 31 : vel_sup = 8000; break; //velocidad equivale a 8000 FPM.
            case 32 : vel_sup = 9000; break; //velocidad equivale a 9000 FPM.
            case 33 : vel_sup = 10000; break; //velocidad equivale a 10000 FPM.
            case 34 : vel_sup = 12000; break; //velocidad equivale a 12000 FPM.
        }

        s = v-1; // Velocidad inferior.
        switch (s)
        {
            case 0  : vel_inf = 200; break; //velocidad equivale a 200 FPM
            case 1  : vel_inf = 300; break; //velocidad equivale a 300 FPM.
            case 2  : vel_inf = 400; break; //velocidad equivale a 400 FPM.
            case 3  : vel_inf = 500; break; //velocidad equivale a 500 FPM.
            case 4  : vel_inf = 600; break; //velocidad equivale a 600 FPM.
            case 5  : vel_inf = 700; break; //velocidad equivale a 700 FPM.
            case 6  : vel_inf = 800; break; //velocidad equivale a 800 FPM.
            case 7  : vel_inf = 900; break; //velocidad equivale a 900 FPM.
            case 8  : vel_inf = 1000; break; //velocidad equivale a 1000 FPM.
            case 9  : vel_inf = 1200; break; //velocidad equivale a 1200 FPM.
            case 10 : vel_inf = 1400; break; //velocidad equivale a 1400 FPM.
            case 11 : vel_inf = 1600; break; //velocidad equivale a 1600 FPM.
            case 12 : vel_inf = 1800; break; //velocidad equivale a 1800 FPM.
            case 13 : vel_inf = 2000; break; //velocidad equivale a 2000 FPM.
            case 14 : vel_inf = 2200; break; //velocidad equivale a 2200 FPM.
            case 15 : vel_inf = 2400; break; //velocidad equivale a 2400 FPM.
            case 16 : vel_inf = 2600; break; //velocidad equivale a 2600 FPM.
            case 17 : vel_inf = 2800; break; //velocidad equivale a 2800 FPM.
            case 18 : vel_inf = 3000; break; //velocidad equivale a 3000 FPM.
            case 19 : vel_inf = 3200; break; //velocidad equivale a 3200 FPM.
            case 20 : vel_inf = 3400; break; //velocidad equivale a 3400 FPM.
            case 21 : vel_inf = 3600; break; //velocidad equivale a 3600 FPM.
            case 22 : vel_inf = 3800; break; //velocidad equivale a 3800 FPM.
            case 23 : vel_inf = 4000; break; //velocidad equivale a 4000 FPM.
            case 24 : vel_inf = 4500; break; //velocidad equivale a 4500 FPM.
            case 25 : vel_inf = 5000; break; //velocidad equivale a 5000 FPM.
            case 26 : vel_inf = 5500; break; //velocidad equivale a 5500 FPM.
            case 27 : vel_inf = 6000; break; //velocidad equivale a 6000 FPM.
            case 28 : vel_inf = 6500; break; //velocidad equivale a 6500 FPM.
            case 29 : vel_inf = 7000; break; //velocidad equivale a 7000 FPM.
            case 30 : vel_inf = 7500; break; //velocidad equivale a 7500 FPM.
            case 31 : vel_inf = 8000; break; //velocidad equivale a 8000 FPM.
            case 32 : vel_inf = 9000; break; //velocidad equivale a 9000 FPM.
            case 33 : vel_inf = 10000; break; //velocidad equivale a 10000 FPM.
            case 34 : vel_inf = 12000; break; //velocidad equivale a 12000 FPM.
        }
    }

    public void perdida_new(){

        switch (i)
        {
            case 0 : perd_new = 10 ; break ;
            case 1 : perd_new = 9 ; break ;
            case 2 : perd_new = 8 ; break ;
            case 3 : perd_new = 7 ; break ;
            case 4 : perd_new = 6 ; break ;
            case 5 : perd_new = 5 ; break ;
            case 6 : perd_new = 4 ; break ;
            case 7 : perd_new = 3.5 ; break ;
            case 8 : perd_new = 3.0 ; break ;
            case 9 : perd_new = 2.5 ; break ;
            case 10 : perd_new = 2.0 ; break ;
            case 11 : perd_new = 1.5 ; break ;
            case 12 : perd_new = 1.0 ; break ;
            case 13 : perd_new = 0.9 ; break ;
            case 14 : perd_new = 0.8 ; break ;
            case 15 : perd_new = 0.7 ; break ;
            case 16 : perd_new = 0.6 ; break ;
            case 17 : perd_new = 0.5 ; break ;
            case 18 : perd_new = 0.4 ; break ;
            case 19 : perd_new = 0.35 ; break ;
            case 20 : perd_new = 0.30 ; break ;
            case 21 : perd_new = 0.25 ; break ;
            case 22 : perd_new = 0.20 ; break ;
            case 23 : perd_new = 0.15 ; break ;
            case 24 : perd_new = 0.10 ; break ;
            case 25 : perd_new = 0.09 ; break ;
            case 26 : perd_new = 0.08 ; break ;
            case 27 : perd_new = 0.07 ; break ;
            case 28 : perd_new = 0.06 ; break ;
            case 29 : perd_new = 0.05 ; break ;
            case 30 : perd_new = 0.04 ; break ;
            case 31 : perd_new = 0.035 ; break ;
            case 32 : perd_new = 0.030 ; break ;
            case 33 : perd_new = 0.025 ; break ;
            case 34 : perd_new = 0.020 ; break ;
            case 35 : perd_new = 0.015 ; break ;
            case 36 : perd_new = 0.010 ; break ;
        }
    }

    public void check1(View view){

        if(chk2.isChecked()==true & chk3.isChecked()==true)
        {
            chk3.setChecked(false);

            edt1.setText(String.format("")); //Borra valores introducidos anteriormente.
        }

        if(chk2.isChecked()==true & chk4.isChecked()==true)
        {
            chk4.setChecked(false);

            edt1.setText(String.format("")); //Borra valores introducidos anteriormente.
        }

        if(chk3.isChecked()==true & chk4.isChecked()==true)
        {
            chk4.setChecked(false);

            edt1.setText(String.format("")); //Borra valores introducidos anteriormente.
        }
    } //Flujo.

    public void check2(View view){

        if(chk1.isChecked()==true & chk3.isChecked()==true)
        {
            chk3.setChecked(false);

            edt2.setText(String.format("")); //Borra valores introducidos anteriormente.
        }

        if(chk1.isChecked()==true & chk4.isChecked()==true)
        {
            chk4.setChecked(false);

            edt2.setText(String.format("")); //Borra valores introducidos anteriormente.
        }

        if(chk3.isChecked()==true & chk4.isChecked()==true)
        {
            chk4.setChecked(false);

            edt2.setText(String.format("")); //Borra valores introducidos anteriormente.
        }
    } //Perdida estatica.

    public void check3(View view){

        Toast toast1 = Toast.makeText(getApplicationContext(), R.string.Fun_indip, Toast.LENGTH_SHORT);
        toast1.show();

        if(chk3.isChecked()==true)
        {
            chk3.setChecked(false);
        }


        //if(chk1.isChecked()==true & chk2.isChecked()==true)
        //{
        //chk2.setChecked(false);

        //edt3.setText(String.format("")); //Borra valores introducidos anteriormente.
        //}

        //if(chk1.isChecked()==true & chk4.isChecked()==true)
        //{
        //chk4.setChecked(false);

        //edt3.setText(String.format("")); //Borra valores introducidos anteriormente.
        //}

        //if(chk2.isChecked()==true & chk4.isChecked()==true)
        //{
        //chk4.setChecked(false);

        ///edt3.setText(String.format("")); //Borra valores introducidos anteriormente.
        //}
    }

    public void check4(View view){

        Toast toast2 = Toast.makeText(getApplicationContext(), R.string.Fun_indip, Toast.LENGTH_SHORT);
        toast2.show();

        if(chk4.isChecked()==true)
        {
            chk4.setChecked(false);
        }

        //if(chk1.isChecked()==true & chk2.isChecked()==true)
        //{
        //chk2.setChecked(false);

        //edt4.setText(String.format("")); //Borra valores introducidos anteriormente.
        //}

        //if(chk1.isChecked()==true & chk3.isChecked()==true)
        //{
        //chk3.setChecked(false);

        //edt4.setText(String.format("")); //Borra valores introducidos anteriormente.
        //}

        //if(chk2.isChecked()==true & chk3.isChecked()==true)
        //{
        //chk3.setChecked(false);

        //edt4.setText(String.format("")); //Borra valores introducidos anteriormente.
        //}

    }

    public void borrar(View view){
        edt1.setText(String.format("")); edt2.setText(String.format("")); edt3.setText(String.format(""));
        edt4.setText(String.format("")); edt5.setText(String.format("")); edt6.setText(String.format(""));
        tv29.setText(String.format("")); tv30.setText(String.format("")); tv32.setText(String.format(""));
        tv34.setText(String.format("")); tv36.setText(String.format(""));
    }

    public void alertas(){

        if(flujoArie > 75000) //Mensaje de alerta cuando los cfm estan fuera de rango
        {
            AlertDialog.Builder msg_cfm = new AlertDialog.Builder(this);
            msg_cfm.setTitle("Fuera de rango");
            msg_cfm.setMessage("EL Caudal introducido está fuera de rango de la gráfica de pérdidas por fricción. Limite máximo 75,000 CFM.");
            msg_cfm.setPositiveButton("Aceptar",null);
            msg_cfm.create();
            msg_cfm.show();
        }

        if(perdidaEstatica > 6.75) //Mensaje de alerta cuando la perdida esta fuera de rango
        {
            AlertDialog.Builder msg_perd = new AlertDialog.Builder(this);
            msg_perd.setTitle("Fuera de rango");
            msg_perd.setMessage("La pérdida introducida está fuera de rango de la gráfica de pérdidas por fricción. Límite máximo 6.75 plg/100ft.");
            msg_perd.setPositiveButton("Aceptar",null);
            msg_perd.create();
            msg_perd.show();
        }

        if(flujoArie < 10) //Mensaje de alerta cuando los cfm estan fuera de rango
        {
            AlertDialog.Builder msg_cfm = new AlertDialog.Builder(this);
            msg_cfm.setTitle("Fuera de rango");
            msg_cfm.setMessage("EL Caudal introducido está fuera de rango de la gráfica de pérdidas por fricción. Limite minimo 10 CFM.");
            msg_cfm.setPositiveButton("Aceptar",null);
            msg_cfm.create();
            msg_cfm.show();
        }

        if(perdidaEstatica < 0.01) //Mensaje de alerta cuando la perdida esta fuera de rango
        {
            AlertDialog.Builder msg_perd = new AlertDialog.Builder(this);
            msg_perd.setTitle("Fuera de rango");
            msg_perd.setMessage("La pérdida introducida está fuera de rango de la gráfica de pérdidas por fricción. Límite minimo 0.01 plg/100ft.");
            msg_perd.setPositiveButton("Aceptar",null);
            msg_perd.create();
            msg_perd.show();
        }

        if (d1 > 100)
        {
            AlertDialog.Builder msg_dia = new AlertDialog.Builder(this);
            msg_dia.setTitle("Fuera de rango");
            msg_dia.setMessage("El diámetro fuera de rango de la gráfica 'Diámetro de ductos redondos'. Límite mínimo 3 plg, máximo 100 plg.");
            msg_dia.setPositiveButton("Aceptar",null);
            msg_dia.create();
            msg_dia.show();
        }
    }

    public void toast_show(){

        //Genera un mensaje cuando no se ha introducido la perdida.
        if(edt1.getText().toString().length() > 0 & edt2.getText().toString().length() == 0 )
        {
            Toast toast3 = Toast.makeText(getApplicationContext(), R.string.Perd_Falt, Toast.LENGTH_SHORT);
            toast3.show();
        }

        //Genera un mensaje cuando no se ha introducido el caudal.
        if(edt1.getText().toString().length() == 0 & edt2.getText().toString().length() > 0 )
        {
            Toast toast4 = Toast.makeText(getApplicationContext(), R.string.Caudal_Falt, Toast.LENGTH_SHORT);
            toast4.show();
        }

        //Genera un mensaje cuando no se ha introducido el caudal y la perdida.
        if(edt1.getText().toString().length() == 0 & edt2.getText().toString().length() == 0 )
        {
            Toast toast5 = Toast.makeText(getApplicationContext(), R.string.Cad_Perd, Toast.LENGTH_SHORT);
            toast5.show();
            //edt1.setBackgroundResource(R.drawable.editbox_dropdown_background_dark);

            //try{
            //    Thread.sleep(2000);
            //}catch(InterruptedException e){}

            //edt1.setBackgroundResource(R.drawable.editbox_dropdown_background);


        }

    }

    public void valores_1() { //Valores de la matriz, perdida, flujo, diametro

        MA[0][0]=30;	    MA[1][0]=66;	    MA[2][0]=195;	MA[3][0]=423;	MA[4][0]=766;	MA[5][0]=1232;	MA[6][0]=1854;	MA[7][0]=2639;	MA[8][0]=3579;	MA[9][0]=4730;	MA[10][0]=7642;	MA[11][0]=11124;	MA[12][0]=15155;	MA[13][0]=21358;	MA[14][0]=28789;	MA[15][0]=36625;
        MA[0][1]=29;	    MA[1][1]=63;	    MA[2][1]=188;	MA[3][1]=405;	MA[4][1]=732;	MA[5][1]=1177;	MA[6][1]=1770;	MA[7][1]=2520;	MA[8][1]=3415;	MA[9][1]=4509;	MA[10][1]=7307;	MA[11][1]=10614;	MA[12][1]=15155;	MA[13][1]=20351;	MA[14][1]=27418;	MA[15][1]=34881;
        MA[0][2]=27;	    MA[1][2]=59;	    MA[2][2]=176;	MA[3][2]=380;	MA[4][2]=687;	MA[5][2]=1102;	MA[6][2]=1664;	MA[7][2]=2369;	MA[8][2]=3208;	MA[9][2]=4250;	MA[10][2]=6864;	MA[11][2]=9970;	    MA[12][2]=14235;	MA[13][2]=19233;	MA[14][2]=25608;	MA[15][2]=32577;
        MA[0][3]=26;	    MA[1][3]=55;	    MA[2][3]=163;	MA[3][3]=352;	MA[4][3]=638;	MA[5][3]=1029;	MA[6][3]=1549;	MA[7][3]=2205;	MA[8][3]=2985;	MA[9][3]=3947;	MA[10][3]=6407;	MA[11][3]=9335;	    MA[12][3]=13288;	MA[13][3]=17990;	MA[14][3]=23917;	MA[15][3]=30426;
        MA[0][4]=24;	    MA[1][4]=51;	    MA[2][4]=151;	MA[3][4]=324;	MA[4][4]=588;	MA[5][4]=945;	MA[6][4]=1425;	MA[7][4]=2032;	MA[8][4]=2752;	MA[9][4]=3639;	MA[10][4]=5906;	MA[11][4]=8606;	    MA[12][4]=12287;	MA[13][4]=16585;	MA[14][4]=22121;	MA[15][4]=28141;
        MA[0][5]=21;	    MA[1][5]=46;	    MA[2][5]=136;	MA[3][5]=294;	MA[4][5]=532;	MA[5][5]=855;	MA[6][5]=1292;	MA[7][5]=1844;	MA[8][5]=2497;	MA[9][5]=3308;	MA[10][5]=5376;	MA[11][5]=7834; 	MA[12][5]=11186;	MA[13][5]=15167;	MA[14][5]=20260;	MA[15][5]=25775;
        MA[0][6]=19;	    MA[1][6]=41; 	    MA[2][6]=121;	MA[3][6]=259;	MA[4][6]=472;	MA[5][6]=759;	MA[6][6]=1145;	MA[7][6]=1638;	MA[8][6]=2217;	MA[9][6]=2932;	MA[10][6]=4788;	MA[11][6]=6999;	    MA[12][6]=9994;	    MA[13][6]=13624;	MA[14][6]=18021;	MA[15][6]=22926;
        MA[0][7]=18;	    MA[1][7]=38;	    MA[2][7]=113;	MA[3][7]=241;	MA[4][7]=439;	MA[5][7]=707;	MA[6][7]=1067;	MA[7][7]=1524;	MA[8][7]=2063;	MA[9][7]=2733;	MA[10][7]=4469;	MA[11][7]=6534;	    MA[12][7]=9300;	    MA[13][7]=12683;	MA[14][7]=16831;	MA[15][7]=21412;
        MA[0][8]=16;	    MA[1][8]=35;	    MA[2][8]=103;	MA[3][8]=222;	MA[4][8]=404;	MA[5][8]=649;	MA[6][8]=984;	MA[7][8]=1405;	MA[8][8]=1902;	MA[9][8]=2523;	MA[10][8]=4133;	MA[11][8]=6042;	    MA[12][8]=8600;	    MA[13][8]=11692;	MA[14][8]=15567;	MA[15][8]=19804;
        MA[0][9]=15;	    MA[1][9]=32;	    MA[2][9]=94;	MA[3][9]=201;	MA[4][9]=365;	MA[5][9]=589;	MA[6][9]=891;	MA[7][9]=1275;	MA[8][9]=1726;	MA[9][9]=2290;	MA[10][9]=3757;	MA[11][9]=5500;	    MA[12][9]=7853;	    MA[13][9]=10639;	MA[14][9]=14120;	MA[15][9]=17963;
        MA[0][10]=13;       MA[1][10]=28;	    MA[2][10]=83;	MA[3][10]=178;	MA[4][10]=324;	MA[5][10]=522;	MA[6][10]=791;	MA[7][10]=1132;	MA[8][10]=1532;	MA[9][10]=2030;	MA[10][10]=3346;	MA[11][10]=4899;	MA[12][10]=6994;	MA[13][10]=9557;	MA[14][10]=12559;   MA[15][10]=16135;
        MA[0][11]=11;       MA[1][11]=24;	    MA[2][11]=71;	MA[3][11]=152;	MA[4][11]=277;	MA[5][11]=447;	MA[6][11]=678;	MA[7][11]=971;	MA[8][11]=1319;	MA[9][11]=1744;	MA[10][11]=2879;	MA[11][11]=4229;	MA[12][11]=6038;	MA[13][11]=8282;	MA[14][11]=10849;   MA[15][11]=13802;
        MA[0][12]=10;       MA[1][12]=19;	    MA[2][12]=57;	MA[3][12]=122;	MA[4][12]=223;	MA[5][12]=359;	MA[6][12]=546;	MA[7][12]=782;	MA[8][12]=1063;	MA[9][12]=1407;	MA[10][12]=2335;	MA[11][12]=3439;	MA[12][12]=4895;	MA[13][12]=6747;	MA[14][12]=8839;	MA[15][12]=11244;
                            MA[1][13]=19;	    MA[2][13]=54;	MA[3][13]=115;	MA[4][13]=211;	MA[5][13]=340;	MA[6][13]=516;	MA[7][13]=739;	MA[8][13]=1001;	MA[9][13]=1328;	MA[10][13]=2207;	MA[11][13]=3261;	MA[12][13]=4642;	MA[13][13]=6364;	MA[14][13]=8336;	MA[15][13]=10605;
                            MA[1][14]=18;	    MA[2][14]=51;	MA[3][14]=108;	MA[4][14]=197;	MA[5][14]=318;	MA[6][14]=483;	MA[7][14]=693;	MA[8][14]=940;	MA[9][14]=1248;	MA[10][14]=2079;	MA[11][14]=3073;	MA[12][14]=4374;	MA[13][14]=6002;	MA[14][14]=7862;	MA[15][14]=10002;
                            MA[1][15]=18;	    MA[2][15]=47;	MA[3][15]=101;	MA[4][15]=184;	MA[5][15]=296;	MA[6][15]=450;	MA[7][15]=646;	MA[8][15]=878;	MA[9][15]=1163;	MA[10][15]=1941;	MA[11][15]=2868;	MA[12][15]=4082;	MA[13][15]=5605;	MA[14][15]=7272;	MA[15][15]=9341;
                            MA[1][16]=17;	    MA[2][16]=43;	MA[3][16]=92;	MA[4][16]=169;	MA[5][16]=272;	MA[6][16]=415;	MA[7][16]=595;	MA[8][16]=807;	MA[9][16]=1072;	MA[10][16]=1789;	MA[11][16]=2644;	MA[12][16]=3775;	MA[13][16]=5184;	MA[14][16]=6725;	MA[15][16]=8640;
                            MA[1][17]=17;	    MA[2][17]=39;	MA[3][17]=84;	MA[4][17]=153;	MA[5][17]=247;	MA[6][17]=377;	MA[7][17]=541;	MA[8][17]=732;	MA[9][17]=973;	MA[10][17]=1624;	MA[11][17]=2400;	MA[12][17]=3426;	MA[13][17]=4702;	MA[14][17]=6160;	MA[15][17]=7836;
                            MA[1][18]=16;	    MA[2][18]=35;	MA[3][18]=74;	MA[4][18]=135;	MA[5][18]=218;	MA[6][18]=334;	MA[7][18]=479;	MA[8][18]=652;	MA[9][18]=864;	MA[10][18]=1442;	MA[11][18]=2130;	MA[12][18]=3042;	MA[13][18]=4183;	MA[14][18]=5479;	MA[15][18]=6970;
                            MA[1][19]=15;	    MA[2][19]=32;	MA[3][19]=69;	MA[4][19]=126;	MA[5][19]=204;	MA[6][19]=310;	MA[7][19]=446;	MA[8][19]=607;	MA[9][19]=804;	MA[10][19]=1342;	MA[11][19]=1989;	MA[12][19]=2839;	MA[13][19]=3906;	MA[14][19]=5068;	MA[15][19]=6510;
                            MA[1][20]=15;	    MA[2][20]=30;	MA[3][20]=64;	MA[4][20]=116;	MA[5][20]=187;	MA[6][20]=286;	MA[7][20]=412;	MA[8][20]=559;	MA[9][20]=741;	MA[10][20]=1237;	MA[11][20]=1833;	MA[12][20]=2618;	MA[13][20]=3613;	MA[14][20]=4702;	MA[15][20]=5963;
                            MA[1][21]=14;	    MA[2][21]=27;	MA[3][21]=58;	MA[4][21]=105;	MA[5][21]=170;	MA[6][21]=260;	MA[7][21]=374;	MA[8][21]=508;	MA[9][21]=672;	MA[10][21]=1122;	MA[11][21]=1666;	MA[12][21]=2383;	MA[13][21]=3277;	MA[14][21]=4258;	MA[15][21]=5408;
                            MA[1][22]=13;	    MA[2][22]=24;	MA[3][22]=51;	MA[4][22]=93;	MA[5][22]=151;	MA[6][22]=231;	MA[7][22]=332;	MA[8][22]=451;	MA[9][22]=597;	MA[10][22]=1000;	MA[11][22]=1482;	MA[12][22]=2116;	MA[13][22]=2915;	MA[14][22]=3787;	MA[15][22]=4858;
                            MA[1][23]=13;	    MA[2][23]=21;	MA[3][23]=44;	MA[4][23]=80;	MA[5][23]=129;	MA[6][23]=197;	MA[7][23]=285;	MA[8][23]=387;	MA[9][23]=512;	MA[10][23]=855;	    MA[11][23]=1273;	MA[12][23]=1815;	MA[13][23]=2494;	MA[14][23]=3250;	MA[15][23]=4156;
                            MA[1][24]=12;	    MA[2][24]=17;	MA[3][24]=35;	MA[4][24]=64;	MA[5][24]=104;	MA[6][24]=159;	MA[7][24]=229;	MA[8][24]=312;	MA[9][24]=413;	MA[10][24]=691;	    MA[11][24]=1029;	MA[12][24]=1471;	MA[13][24]=2012;	MA[14][24]=2627;	MA[15][24]=3353;
                            MA[1][25]=11;	    MA[2][25]=16;	MA[3][25]=33;	MA[4][25]=60;	MA[5][25]=98;	MA[6][25]=150;	MA[7][25]=216;	MA[8][25]=294;	MA[9][25]=391;	MA[10][25]=653;	    MA[11][25]=973;	    MA[12][25]=1391;	MA[13][25]=1897;	MA[14][25]=2481;	MA[15][25]=3162;
                                                MA[2][26]=15;	MA[3][26]=31;	MA[4][26]=57;	MA[5][26]=92;	MA[6][26]=141;	MA[7][26]=203;	MA[8][26]=277;	MA[9][26]=368;	MA[10][26]=613;	    MA[11][26]=915;	    MA[12][26]=1306;	MA[13][26]=1789;	MA[14][26]=2337;	MA[15][26]=2982;
                                                MA[2][27]=14;	MA[3][27]=28;	MA[4][27]=53;	MA[5][27]=85;	MA[6][27]=131;	MA[7][27]=190;	MA[8][27]=257;	MA[9][27]=342;	MA[10][27]=571;	    MA[11][27]=853;	    MA[12][27]=1216;	MA[13][27]=1671;	MA[14][27]=2164;	MA[15][27]=2758;
                                                MA[2][28]=13;	MA[3][28]=26;	MA[4][28]=49;	MA[5][28]=79;	MA[6][28]=121;	MA[7][28]=175;	MA[8][28]=237;	MA[9][28]=315;	MA[10][28]=526;	    MA[11][28]=786;	    MA[12][28]=1121;	MA[13][28]=1531;	MA[14][28]=2005;	MA[15][28]=2551;
                                                MA[2][29]=12;	MA[3][29]=24;	MA[4][29]=44;	MA[5][29]=71;	MA[6][29]=109;	MA[7][29]=158;	MA[8][29]=215;	MA[9][29]=286;	MA[10][29]=478;	    MA[11][29]=713;	    MA[12][29]=1020;	MA[13][29]=1402;	MA[14][29]=1825;	MA[15][29]=2314;
                                                MA[2][30]=11;	MA[3][30]=21;	MA[4][30]=39;	MA[5][30]=63;	MA[6][30]=97;	MA[7][30]=141;	MA[8][30]=191;	MA[9][30]=254;	MA[10][30]=424;	    MA[11][30]=633;	    MA[12][30]=906;	    MA[13][30]=1235;	MA[14][30]=1615;	MA[15][30]=2058;
                                                                MA[3][31]=19;	MA[4][31]=36;	MA[5][31]=59;	MA[6][31]=90;	MA[7][31]=131;	MA[8][31]=178;	MA[9][31]=237;	MA[10][31]=393;	MA[11][31]=588;	    MA[12][31]=843;	    MA[13][31]=1153;	MA[14][31]=1506;	MA[15][31]=1922;
                                                                MA[3][32]=18;	MA[4][32]=33;	MA[5][32]=54;	MA[6][32]=83;	MA[7][32]=121;	MA[8][32]=164;	MA[9][32]=218;	MA[10][32]=361;	MA[11][32]=540;	    MA[12][32]=775; 	MA[13][32]=1067;	MA[14][32]=1393;	MA[15][32]=1778;
                                                                MA[3][33]=16;	MA[4][33]=30;	MA[5][33]=49;	MA[6][33]=75;	MA[7][33]=109;	MA[8][33]=149;	MA[9][33]=198;	MA[10][33]=327;	MA[11][33]=490;	    MA[12][33]=701;	    MA[13][33]=968;	    MA[14][33]=1261;	MA[15][33]=1613;
                                                                MA[3][34]=15;	MA[4][34]=27;	MA[5][34]=43;	MA[6][34]=67;	MA[7][34]=97;	MA[8][34]=133;	MA[9][34]=176;	MA[10][34]=289;	MA[11][34]=433;	    MA[12][34]=620;	    MA[13][34]=861;	    MA[14][34]=1128;	MA[15][34]=1434;
                                                                MA[3][35]=12;	MA[4][35]=23;	MA[5][35]=37;	MA[6][35]=57;	MA[7][35]=83;	MA[8][35]=114;	MA[9][35]=151;	MA[10][35]=247;	MA[11][35]=370;	    MA[12][35]=530;	    MA[13][35]=736;	    MA[14][35]=966; 	MA[15][35]=1227;
                                                                MA[3][36]=10;	MA[4][36]=18;	MA[5][36]=30;	MA[6][36]=46;	MA[7][36]=67;	MA[8][36]=92;	MA[9][36]=122;	MA[10][36]=198;	MA[11][36]=297;	    MA[12][36]=425;	    MA[13][36]=600;	    MA[14][36]=779;	    MA[15][36]=1000;


        MA[16][0]=46745;	MA[17][0]=57720;	MA[18][0]=68173;	MA[19][0]=83272;	MA[20][0]=97662;	MA[21][0]=0;	    MA[22][0]=0;	    MA[23][0]=0;	    MA[24][0]=0; 	    MA[25][0]=0;     MA[26][0]=0;	    MA[27][0]=0;	        MA[28][0]=0;	        MA[29][0]=0;	    MA[30][0]=0;
        MA[16][1]=44519;	MA[17][1]=54548;	MA[18][1]=64436;	MA[19][1]=79178;	MA[20][1]=92860;	MA[21][1]=0;	    MA[22][1]=0;	    MA[23][1]=0;	    MA[24][1]=0; 	    MA[25][1]=0;	    MA[26][1]=0;	    MA[27][1]=0;	        MA[28][1]=0;	        MA[29][1]=0;	    MA[30][1]=0;
        MA[16][2]=41579;	MA[17][2]=51614;	MA[18][2]=60837;	MA[19][2]=74675;	MA[20][2]=87579;	MA[21][2]=0;	    MA[22][2]=0;	    MA[23][2]=0;	    MA[24][2]=0; 	    MA[25][2]=0;	    MA[26][2]=0;	    MA[27][2]=0;	        MA[28][2]=0;	        MA[29][2]=0;	    MA[30][2]=0;
        MA[16][3]=38833;	MA[17][3]=48212;	MA[18][3]=56820;	MA[19][3]=69744;	MA[20][3]=81796;	MA[21][3]=95931;	MA[22][3]=0;	    MA[23][3]=0;	    MA[24][3]=0;	    MA[25][3]=0;	    MA[26][3]=0; 	MA[27][3]=0; 	    MA[28][3]=0;	        MA[29][3]=0;	    MA[30][3]=0;
        MA[16][4]=35917;	MA[17][4]=44591;	MA[18][4]=52724;	MA[19][4]=64506;	MA[20][4]=75766;	MA[21][4]=88726;	MA[22][4]=0;	    MA[23][4]=0;	    MA[24][4]=0;	    MA[25][4]=0;	    MA[26][4]=0;	    MA[27][4]=0; 	    MA[28][4]=0;	        MA[29][4]=0;	    MA[30][4]=0;
        MA[16][5]=32897;	MA[17][5]=40775;	MA[18][5]=48134;	MA[19][5]=58508;	MA[20][5]=68842;	MA[21][5]=81265;	MA[22][5]=95205;	MA[23][5]=0;	    MA[24][5]=0;        MA[25][5]=0;	    MA[26][5]=0;	    MA[27][5]=0;	        MA[28][5]=0;	        MA[29][5]=0;	    MA[30][5]=0;
        MA[16][6]=29261;	MA[17][6]=36210;	MA[18][6]=42814;	MA[19][6]=52552;	MA[20][6]=61835;	MA[21][6]=72285;	MA[22][6]=84776;	MA[23][6]=0;	    MA[24][6]=0;	    MA[25][6]=0;	    MA[26][6]=0;	    MA[27][6]=0;	        MA[28][6]=0; 	    MA[29][6]=0;	    MA[30][6]=0;
        MA[16][7]=27329;	MA[17][7]=33985;	MA[18][7]=39987;	MA[19][7]=49242;	MA[20][7]=57752;	MA[21][7]=67731;	MA[22][7]=79178;	MA[23][7]=91498;	MA[24][7]=0; 	    MA[25][7]=0;	    MA[26][7]=0;	    MA[27][7]=0;	        MA[28][7]=0;	        MA[29][7]=0;	    MA[30][7]=0;
        MA[16][8]=25277;	MA[17][8]=31330;	MA[18][8]=36984;	MA[19][8]=45544;	MA[20][8]=53414;	MA[21][8]=62645;	MA[22][8]=73231;	MA[23][8]=85052;	MA[24][8]=96766;	MA[25][8]=0; 	MA[26][8]=0;	    MA[27][8]=0;	        MA[28][8]=0;	        MA[29][8]=0;	    MA[30][8]=0;
        MA[16][9]=22926;	MA[17][9]=28556;	MA[18][9]=33874;	MA[19][9]=41309;	MA[20][9]=48678;	MA[21][9]=56820;	MA[22][9]=66639;	MA[23][9]=77144;	MA[24][9]=88150;    MA[25][9]=0;	    MA[26][9]=0;	    MA[27][9]=0;	        MA[28][9]=0;	        MA[29][9]=0;	    MA[30][9]=0;
        MA[16][10]=20593;   MA[17][10]=25442;   MA[18][10]=30229;   MA[19][10]=37104;   MA[20][10]=43440;   MA[21][10]=51036;   MA[22][10]=59856;   MA[23][10]=69066;   MA[24][10]=79178;   MA[25][10]=0;	MA[26][10]=0;	MA[27][10]=0;	    MA[28][10]=0;	    MA[29][10]=0;    MA[30][10]=0;
        MA[16][11]=17788;   MA[17][11]=22010;   MA[18][11]=26112;   MA[19][11]=32052;   MA[20][11]=37524;   MA[21][11]=44086;   MA[22][11]=51705;   MA[23][11]=59556;   MA[24][11]=68396;   MA[25][11]=91560;MA[26][11]=0;	MA[27][11]=0;	    MA[28][11]=0;	    MA[29][11]=0;	MA[30][11]=0;
        MA[16][12]=14492;   MA[17][12]=17873;   MA[18][12]=21343;   MA[19][12]=26112;   MA[20][12]=30670;   MA[21][12]=36034;   MA[22][12]=42124;   MA[23][12]=48606;   MA[24][12]=55903;   MA[25][12]=75030;MA[26][12]=99912;MA[27][12]=0;	    MA[28][12]=0;	    MA[29][12]=0;	MA[30][12]=0;
        MA[16][13]=13668;   MA[17][13]=16886;   MA[18][13]=20159;   MA[19][13]=24627;   MA[20][13]=28977;   MA[21][13]=33985;   MA[22][13]=39728;   MA[23][13]=46291;   MA[24][13]=52724;   MA[25][13]=70887;MA[26][13]=94076;MA[27][13]=0;	    MA[28][13]=0;	    MA[29][13]=0;	MA[30][13]=0;
        MA[16][14]=12891;   MA[17][14]=15950;   MA[18][14]=19046;   MA[19][14]=23227;   MA[20][14]=27370;   MA[21][14]=32052;   MA[22][14]=37590;   MA[23][14]=43375;   MA[24][14]=49725;   MA[25][14]=66955;MA[26][14]=88726;MA[27][14]=0;	    MA[28][14]=0;	    MA[29][14]=0;	MA[30][14]=0;
        MA[16][15]=12039;   MA[17][15]=14826;   MA[18][15]=17846;   MA[19][15]=21693;   MA[20][15]=25646;   MA[21][15]=29935;   MA[22][15]=35108;   MA[23][15]=40643;   MA[24][15]=46442;   MA[25][15]=62238;MA[26][15]=82867;MA[27][15]=0;	    MA[28][15]=0;	    MA[29][15]=0;	MA[30][15]=0;
        MA[16][16]=11135;   MA[17][16]=13802;   MA[18][16]=16453;   MA[19][16]=20064;   MA[20][16]=23643;   MA[21][16]=27687;   MA[22][16]=32472;   MA[23][16]=37590;   MA[24][16]=42954;   MA[25][16]=57838;MA[26][16]=76395;MA[27][16]=100074;	MA[28][16]=0;	    MA[29][16]=0;	MA[30][16]=0;
        MA[16][17]=10199;   MA[17][17]=12519;   MA[18][17]=14996;   MA[19][17]=18377;   MA[20][17]=21587;   MA[21][17]=25359;   MA[22][17]=29741;   MA[23][17]=34430;   MA[24][17]=39342;MA[25][17]=52896;MA[26][17]=69744;MA[27][17]=90475;	MA[28][17]=0;	    MA[29][17]=0;	MA[30][17]=0;
        MA[16][18]=9072;	MA[17][18]=11244;   MA[18][18]=13469;   MA[19][18]=16399;   MA[20][18]=19202;   MA[21][18]=22778;   MA[22][18]=26454;   MA[23][18]=30625;   MA[24][18]=34994;MA[25][18]=47274;MA[26][18]=62645;MA[27][18]=80476;	MA[28][18]=100074;	MA[29][18]=0;	MA[30][18]=0;
        MA[16][19]=8473;	MA[17][19]=10502;   MA[18][19]=12580;   MA[19][19]=15316;   MA[20][19]=17934;   MA[21][19]=21067;   MA[22][19]=24708;   MA[23][19]=28603;   MA[24][19]=32683;MA[25][19]=44296;MA[26][19]=58508;MA[27][19]=75030;	MA[28][19]=93062;	MA[29][19]=0;	MA[30][19]=0;
        MA[16][20]=7836;	MA[17][20]=9745;	MA[18][20]=11654;   MA[19][20]=14166;   MA[20][20]=16750;   MA[21][20]=19485;   MA[22][20]=22852;   MA[23][20]=26454;   MA[24][20]=30229;MA[25][20]=40836;MA[26][20]=54290;MA[27][20]=69744;	MA[28][20]=85607;	MA[29][20]=0;	MA[30][20]=0;
        MA[16][21]=7108;	MA[17][21]=8839;	MA[18][21]=10553;   MA[19][21]=12849;   MA[20][21]=15167;   MA[21][21]=17905;   MA[22][21]=20930;   MA[23][21]=24187;   MA[24][21]=27777;MA[25][21]=37347;MA[26][21]=49242;MA[27][21]=63767;	MA[28][21]=78409;	MA[29][21]=0;	MA[30][21]=0;
        MA[16][22]=6384;	MA[17][22]=7836;	MA[18][22]=9479;	MA[19][22]=11541;   MA[20][22]=13646;   MA[21][22]=15926;   MA[22][22]=18678;   MA[23][22]=21622;   MA[24][22]=24708;MA[25][22]=33328;MA[26][22]=43943;MA[27][22]=56905;	MA[28][22]=69744;	MA[29][22]=0;	MA[30][22]=0;
        MA[16][23]=5461;	MA[17][23]=6769;	MA[18][23]=8162;	MA[19][23]=9969;	MA[20][23]=11788;   MA[21][23]=13757;   MA[22][23]=16135;   MA[23][23]=18678;   MA[24][23]=21343;MA[25][23]=28836;MA[26][23]=37959;MA[27][23]=49156;	MA[28][23]=60443;	MA[29][23]=92860;MA[30][23]=0;
        MA[16][24]=4363;	MA[17][24]=5461;	MA[18][24]=6575;	MA[19][24]=8043;	MA[20][24]=9485;	MA[21][24]=11116;   MA[22][24]=13017;   MA[23][24]=15069;   MA[24][24]=17388;MA[25][24]=23493;MA[26][24]=31275;MA[27][24]=40052;	MA[28][24]=49242;	MA[29][24]=75653;MA[30][24]=0;
        MA[16][25]=4115;	MA[17][25]=5101;	MA[18][25]=6190;	MA[19][25]=7512;	MA[20][25]=8940;	MA[21][25]=10468;   MA[22][25]=12277;   MA[23][25]=14212;   MA[24][25]=16240;MA[25][25]=22157;MA[26][25]=29548;MA[27][25]=37774;	MA[28][25]=46442;	MA[29][25]=71350;MA[30][25]=98353;
        MA[16][26]=3881;	MA[17][26]=4811;	MA[18][26]=5818;	MA[19][26]=7085;	MA[20][26]=8328;	MA[21][26]=9791;	MA[22][26]=11466;   MA[23][26]=13360;   MA[24][26]=15316;MA[25][26]=20693;MA[26][26]=27549;MA[27][26]=35611;	MA[28][26]=43800;	MA[29][26]=67292;MA[30][26]=92558;
        MA[16][27]=3590;	MA[17][27]=4449;	MA[18][27]=5381;	MA[19][27]=6617;	MA[20][27]=7786;	MA[21][27]=9131;	MA[22][27]=10709;   MA[23][27]=12456;   MA[24][27]=14305;MA[25][27]=19277;MA[26][27]=25729;MA[27][27]=33058;	MA[28][27]=40908;	MA[29][27]=62849;MA[30][27]=86447;
        MA[16][28]=3320;	MA[17][28]=4121;	MA[18][28]=4970;	MA[19][28]=6061;	MA[20][28]=7124;	MA[21][28]=8445;	MA[22][28]=9905;	MA[23][28]=11429;   MA[24][28]=13102;MA[25][28]=17829;MA[26][28]=23762;MA[27][28]=30563;	MA[28][28]=37468;	MA[29][28]=58129;MA[30][28]=79178;
        MA[16][29]=2982;	MA[17][29]=3745;	MA[18][29]=4513;	MA[19][29]=5497;	MA[20][29]=6478;	MA[21][29]=7610;	MA[22][29]=8896;	MA[23][29]=10382;   MA[24][29]=11884;MA[25][29]=16066;MA[26][29]=21444;MA[27][29]=27549;	MA[28][29]=34318;	MA[29][29]=52724;MA[30][29]=72520;
        MA[16][30]=2653;	MA[17][30]=3309;	MA[18][30]=4002;	MA[19][30]=4842;	MA[20][30]=5734;	MA[21][30]=6757;	MA[22][30]=7913;	MA[23][30]=9206;	MA[24][30]=10570;MA[25][30]=14233;MA[26][30]=18984;MA[27][30]=24627;	MA[28][30]=30274;	MA[29][30]=46897;MA[30][30]=64506;
        MA[16][31]=2477;	MA[17][31]=3061;	MA[18][31]=3702;	MA[19][31]=4522;	MA[20][31]=5346;	MA[21][31]=6302;	MA[22][31]=7391;	MA[23][31]=8575;	MA[24][31]=9777;	MA[25][31]=13274;MA[26][31]=17731;MA[27][31]=22738;	MA[28][31]=28092;	MA[29][31]=43375;MA[30][31]=60246;
        MA[16][32]=2269;	MA[17][32]=2831;	MA[18][32]=3424;	MA[19][32]=4183;	MA[20][32]=4905;	MA[21][32]=5781;	MA[22][32]=6769;	MA[23][32]=7846;	MA[24][32]=9042;	MA[25][32]=12197;MA[26][32]=16240;MA[27][32]=21030;	MA[28][32]=25982;	MA[29][32]=40117;MA[30][32]=55722;
        MA[16][33]=2058;	MA[17][33]=2551;	MA[18][33]=3081;	MA[19][33]=3794;	MA[20][33]=4456;	MA[21][33]=5243;	MA[22][33]=6140;	MA[23][33]=7143;	MA[24][33]=8202;	MA[25][33]=11080;MA[26][33]=14730;MA[27][33]=18951;	MA[28][33]=23489;	MA[29][33]=36387;MA[30][33]=50541;
        MA[16][34]=1831;	MA[17][34]=2280;	MA[18][34]=2744;	MA[19][34]=3342;	MA[20][34]=3945;	MA[21][34]=4633;	MA[22][34]=5461;	MA[23][34]=6333;	MA[24][34]=7224;	MA[25][34]=9808;	MA[26][34]=13102;MA[27][34]=16912;	MA[28][34]=20758;	MA[29][34]=32052;MA[30][34]=44955;
        MA[16][35]=1566;	MA[17][35]=1938;	MA[18][35]=2340;	MA[19][35]=2859;	MA[20][35]=3375;	MA[21][35]=3964;	MA[22][35]=4672;	MA[23][35]=5417;	MA[24][35]=6180;	MA[25][35]=8349;	MA[26][35]=11208;MA[27][35]=14420;	MA[28][35]=17788;	MA[29][35]=27418;MA[30][35]=38456;
        MA[16][36]=1263;	MA[17][36]=1569;	MA[18][36]=1885;	MA[19][36]=2306;	MA[20][36]=2722;	MA[21][36]=3211;	MA[22][36]=3769;	MA[23][36]=4373;	MA[24][36]=4986;	MA[25][36]=6747;	MA[26][36]=8955;	MA[27][36]=11579;	MA[28][36]=14305;	MA[29][36]=22121;MA[30][36]=31330;









    }

    public void valores_2() { //Valores de la matriz, [velocidad][filas][perdida(0),flujo(1)]

        VEL[0][0]=0;	VEL[1][0]=0;	VEL[2][0]=0;	VEL[3][0]=0;	VEL[4][0]=0;	VEL[5][0]=0;	VEL[6][0]=0;	VEL[7][0]=0;	VEL[8][0]=0;	VEL[9][0]=0;
        VEL[0][1]=0;	VEL[1][1]=0;	VEL[2][1]=0;	VEL[3][1]=0;	VEL[4][1]=0;	VEL[5][1]=0;	VEL[6][1]=0;	VEL[7][1]=0;	VEL[8][1]=0;	VEL[9][1]=0;
        VEL[0][2]=0;	VEL[1][2]=0;	VEL[2][2]=0;	VEL[3][2]=0;	VEL[4][2]=0;	VEL[5][2]=0;	VEL[6][2]=0;	VEL[7][2]=0;	VEL[8][2]=0;	VEL[9][2]=0;
        VEL[0][3]=0;	VEL[1][3]=0;	VEL[2][3]=0;	VEL[3][3]=0;	VEL[4][3]=0;	VEL[5][3]=0;	VEL[6][3]=0;	VEL[7][3]=0;	VEL[8][3]=0;	VEL[9][3]=0;
        VEL[0][4]=0;	VEL[1][4]=0;	VEL[2][4]=0;	VEL[3][4]=0;	VEL[4][4]=0;	VEL[5][4]=0;	VEL[6][4]=0;	VEL[7][4]=0;	VEL[8][4]=0;	VEL[9][4]=0;
        VEL[0][5]=0;	VEL[1][5]=0;	VEL[2][5]=0;	VEL[3][5]=0;	VEL[4][5]=0;	VEL[5][5]=0;	VEL[6][5]=0;	VEL[7][5]=0;	VEL[8][5]=0;	VEL[9][5]=0;
        VEL[0][6]=0;	VEL[1][6]=0;	VEL[2][6]=0;	VEL[3][6]=0;	VEL[4][6]=0;	VEL[5][6]=0;	VEL[6][6]=0;	VEL[7][6]=0;	VEL[8][6]=0;	VEL[9][6]=0;
        VEL[0][7]=0;	VEL[1][7]=0;	VEL[2][7]=0;	VEL[3][7]=0;	VEL[4][7]=0;	VEL[5][7]=0;	VEL[6][7]=0;	VEL[7][7]=0;	VEL[8][7]=0;	VEL[9][7]=0;
        VEL[0][8]=0;	VEL[1][8]=0;	VEL[2][8]=0;	VEL[3][8]=0;	VEL[4][8]=0;	VEL[5][8]=0;	VEL[6][8]=0;	VEL[7][8]=0;	VEL[8][8]=0;	VEL[9][8]=11;
        VEL[0][9]=0;	VEL[1][9]=0;	VEL[2][9]=0;	VEL[3][9]=0;	VEL[4][9]=0;	VEL[5][9]=0;	VEL[6][9]=0;	VEL[7][9]=0;	VEL[8][9]=0;	VEL[9][9]=14;
        VEL[0][10]=0;	VEL[1][10]=0;	VEL[2][10]=0;	VEL[3][10]=0;	VEL[4][10]=0;	VEL[5][10]=0;	VEL[6][10]=0;	VEL[7][10]=0;	VEL[8][10]=10;	VEL[9][10]=20;
        VEL[0][11]=0;	VEL[1][11]=0;	VEL[2][11]=0;	VEL[3][11]=0;	VEL[4][11]=0;	VEL[5][11]=0;	VEL[6][11]=0;	VEL[7][11]=10;	VEL[8][11]=15;	VEL[9][11]=31;
        VEL[0][12]=0;	VEL[1][12]=0;	VEL[2][12]=0;	VEL[3][12]=0;	VEL[4][12]=0;	VEL[5][12]=0;	VEL[6][12]=12;	VEL[7][12]=19;	VEL[8][12]=29;	VEL[9][12]=59;
        VEL[0][13]=0;	VEL[1][13]=0;	VEL[2][13]=0;	VEL[3][13]=0;	VEL[4][13]=0;	VEL[5][13]=0;	VEL[6][13]=14;	VEL[7][13]=23;	VEL[8][13]=34;	VEL[9][13]=69;
        VEL[0][14]=0;	VEL[1][14]=0;	VEL[2][14]=0;	VEL[3][14]=0;	VEL[4][14]=0;	VEL[5][14]=11;	VEL[6][14]=17;	VEL[7][14]=27;	VEL[8][14]=41;	VEL[9][14]=83;
        VEL[0][15]=0;	VEL[1][15]=0;	VEL[2][15]=0;	VEL[3][15]=0;	VEL[4][15]=0;	VEL[5][15]=13;	VEL[6][15]=21;	VEL[7][15]=33;	VEL[8][15]=50;	VEL[9][15]=103;
        VEL[0][16]=0;	VEL[1][16]=0;	VEL[2][16]=0;	VEL[3][16]=0;	VEL[4][16]=0;	VEL[5][16]=17;	VEL[6][16]=27;	VEL[7][16]=42;	VEL[8][16]=64;	VEL[9][16]=132;
        VEL[0][17]=0;	VEL[1][17]=0;	VEL[2][17]=0;	VEL[3][17]=0;	VEL[4][17]=11;	VEL[5][17]=22;	VEL[6][17]=35;	VEL[7][17]=56;	VEL[8][17]=86;	VEL[9][17]=177;
        VEL[0][18]=0;	VEL[1][18]=0;	VEL[2][18]=0;	VEL[3][18]=0;	VEL[4][18]=16;	VEL[5][18]=30;	VEL[6][18]=50;	VEL[7][18]=80;	VEL[8][18]=121;	VEL[9][18]=252;
        VEL[0][19]=0;	VEL[1][19]=0;	VEL[2][19]=0;	VEL[3][19]=0;	VEL[4][19]=20;	VEL[5][19]=37;	VEL[6][19]=62;	VEL[7][19]=99;	VEL[8][19]=151;	VEL[9][19]=310;
        VEL[0][20]=0;	VEL[1][20]=0;	VEL[2][20]=0;	VEL[3][20]=13;	VEL[4][20]=26;	VEL[5][20]=47;	VEL[6][20]=79;	VEL[7][20]=126;	VEL[8][20]=192;	VEL[9][20]=396;
        VEL[0][21]=0;	VEL[1][21]=0;	VEL[2][21]=0;	VEL[3][21]=17;	VEL[4][21]=34;	VEL[5][21]=61;	VEL[6][21]=105;	VEL[7][21]=168;	VEL[8][21]=257;	VEL[9][21]=533;
        VEL[0][22]=0;	VEL[1][22]=0;	VEL[2][22]=10;	VEL[3][22]=23;	VEL[4][22]=48;	VEL[5][22]=88;	VEL[6][22]=151;	VEL[7][22]=240;	VEL[8][22]=368;	VEL[9][22]=769;
        VEL[0][23]=0;	VEL[1][23]=0;	VEL[2][23]=16;	VEL[3][23]=37;	VEL[4][23]=76;	VEL[5][23]=139;	VEL[6][23]=242;	VEL[7][23]=382;	VEL[8][23]=588;	VEL[9][23]=1230;
        VEL[0][24]=0;	VEL[1][24]=10;	VEL[2][24]=31;	VEL[3][24]=69;	VEL[4][24]=146;	VEL[5][24]=264;	VEL[6][24]=458;	VEL[7][24]=745;	VEL[8][24]=1126;	VEL[9][24]=2340;
        VEL[0][25]=0;   VEL[1][25]=12;	VEL[2][25]=36;	VEL[3][25]=82;	VEL[4][25]=173;	VEL[5][25]=315;	VEL[6][25]=466;	VEL[7][25]=879;	VEL[8][25]=1345;	VEL[9][25]=2804;
        VEL[0][26]=0;   VEL[1][26]=14;	VEL[2][26]=43;	VEL[3][26]=100;	VEL[4][26]=209;	VEL[5][26]=380;	VEL[6][26]=666;	VEL[7][26]=1058;	VEL[8][26]=1646;	VEL[9][26]=3393;
        VEL[0][27]=0;   VEL[1][27]=18;	VEL[2][27]=52;	VEL[3][27]=124;	VEL[4][27]=258;	VEL[5][27]=474;	VEL[6][27]=829;	VEL[7][27]=1325;	VEL[8][27]=2045;	VEL[9][27]=4233;
        VEL[0][28]=0;   VEL[1][28]=23;	VEL[2][28]=68;	VEL[3][28]=159;	VEL[4][28]=332;	VEL[5][28]=605;	VEL[6][28]=1066;	VEL[7][28]=1700;	VEL[8][28]=2629;	VEL[9][28]=5489;
        VEL[0][29]=0;   VEL[1][29]=30;	VEL[2][29]=89;	VEL[3][29]=212;	VEL[4][29]=445;	VEL[5][29]=816;	VEL[6][29]=1434;	VEL[7][29]=2279;	VEL[8][29]=3526;	VEL[9][29]=7497;
        VEL[0][30]=10;  VEL[1][30]=43;	VEL[2][30]=127;	VEL[3][30]=303;	VEL[4][30]=642;	VEL[5][30]=1187;	VEL[6][30]=2067;	VEL[7][30]=3281;	VEL[8][30]=5106;	VEL[9][30]=10862;
        VEL[0][31]=11;  VEL[1][31]=53;	VEL[2][31]=157;	VEL[3][31]=373;	VEL[4][31]=798;	VEL[5][31]=1470;	VEL[6][31]=2568;	VEL[7][31]=4092;	VEL[8][31]=6287;	VEL[9][31]=13507;
        VEL[0][32]=14;  VEL[1][32]=67;	VEL[2][32]=200;	VEL[3][32]=479;	VEL[4][32]=1018;	VEL[5][32]=1892;	VEL[6][32]=3294;	VEL[7][32]=5258;	VEL[8][32]=8118;	VEL[9][32]=17478;
        VEL[0][33]=19;  VEL[1][33]=90;	VEL[2][33]=265;	VEL[3][33]=644;	VEL[4][33]=1381;	VEL[5][33]=2562;	VEL[6][33]=4457;	VEL[7][33]=7135;	VEL[8][33]=10966;	VEL[9][33]=23733;
        VEL[0][34]=27;  VEL[1][34]=128;	VEL[2][34]=384;	VEL[3][34]=926;	VEL[4][34]=1975;	VEL[5][34]=3681;	VEL[6][34]=6476;	VEL[7][34]=10219;	VEL[8][34]=15799;	VEL[9][34]=35076;
        VEL[0][35]=42;  VEL[1][35]=200;	VEL[2][35]=613;	VEL[3][35]=1493;	VEL[4][35]=3193;	VEL[5][35]=5919;	VEL[6][35]=10265;	VEL[7][35]=16336;	VEL[8][35]=25650;	VEL[9][35]=55224;
        VEL[0][36]=77;  VEL[1][36]=375;	VEL[2][36]=1172;	VEL[3][36]=2927;	VEL[4][36]=6108;	VEL[5][36]=11576;	VEL[6][36]=19916;	VEL[7][36]=31486;	VEL[8][36]=49335;	VEL[9][36]=99145;

        VEL[10][0]=0;	VEL[11][0]=0;	VEL[12][0]=0;	VEL[13][0]=0;	VEL[14][0]=19;	VEL[15][0]=26;	VEL[16][0]=35;	VEL[17][0]=49;	VEL[18][0]=61;	VEL[19][0]=81;
        VEL[10][1]=0;	VEL[11][1]=0;	VEL[12][1]=10;	VEL[13][1]=15;	VEL[14][1]=21;	VEL[15][1]=30;	VEL[16][1]=40;	VEL[17][1]=55;	VEL[18][1]=70;	VEL[19][1]=93;
        VEL[10][2]=0;	VEL[11][2]=0;	VEL[12][2]=12;	VEL[13][2]=18;	VEL[14][2]=25;	VEL[15][2]=35;	VEL[16][2]=48;	VEL[17][2]=66;	VEL[18][2]=85;	VEL[19][2]=113;
        VEL[10][3]=0;	VEL[11][3]=0;	VEL[12][3]=15;	VEL[13][3]=22;	VEL[14][3]=31;	VEL[15][3]=43;	VEL[16][3]=59;	VEL[17][3]=82;	VEL[18][3]=104;	VEL[19][3]=140;
        VEL[10][4]=0;	VEL[11][4]=11;	VEL[12][4]=18;	VEL[13][4]=27;	VEL[14][4]=39;	VEL[15][4]=55;	VEL[16][4]=75;	VEL[17][4]=103;	VEL[18][4]=133;	VEL[19][4]=175;
        VEL[10][5]=0;	VEL[11][5]=15;	VEL[12][5]=24;	VEL[13][5]=36;	VEL[14][5]=51;	VEL[15][5]=72;	VEL[16][5]=100;	VEL[17][5]=138;	VEL[18][5]=180;	VEL[19][5]=238;
        VEL[10][6]=13;	VEL[11][6]=21;	VEL[12][6]=34;	VEL[13][6]=50;	VEL[14][6]=72;	VEL[15][6]=101;	VEL[16][6]=142;	VEL[17][6]=195;	VEL[18][6]=256;	VEL[19][6]=339;
        VEL[10][7]=16;	VEL[11][7]=26;	VEL[12][7]=41;	VEL[13][7]=61;	VEL[14][7]=88;	VEL[15][7]=123;	VEL[16][7]=175;	VEL[17][7]=239;	VEL[18][7]=318;	VEL[19][7]=417;
        VEL[10][8]=20;	VEL[11][8]=33;	VEL[12][8]=52;	VEL[13][8]=78;	VEL[14][8]=112;	VEL[15][8]=159;	VEL[16][8]=222;	VEL[17][8]=308;	VEL[18][8]=407;	VEL[19][8]=533;
        VEL[10][9]=26;	VEL[11][9]=44;	VEL[12][9]=68;	VEL[13][9]=103;	VEL[14][9]=149;	VEL[15][9]=212;	VEL[16][9]=299;	VEL[17][9]=410;	VEL[18][9]=546;	VEL[19][9]=715;
        VEL[10][10]=37;	VEL[11][10]=61;	VEL[12][10]=96;	VEL[13][10]=145;	VEL[14][10]=213;	VEL[15][10]=303;	VEL[16][10]=432;	VEL[17][10]=587;	VEL[18][10]=782;	VEL[19][10]=1025;
        VEL[10][11]=58;	VEL[11][11]=96;	VEL[12][11]=154;	VEL[13][11]=230;	VEL[14][11]=339;	VEL[15][11]=481;	VEL[16][11]=689;	VEL[17][11]=930;	VEL[18][11]=1249;	VEL[19][11]=1657;
        VEL[10][12]=109;	VEL[11][12]=183;	VEL[12][12]=295;	VEL[13][12]=441;	VEL[14][12]=657;	VEL[15][12]=934;	VEL[16][12]=1333;	VEL[17][12]=1813;	VEL[18][12]=2470;	VEL[19][12]=3246;
        VEL[10][13]=129;	VEL[11][13]=218;	VEL[12][13]=351;	VEL[13][13]=530;	VEL[14][13]=779;	VEL[15][13]=1113;	VEL[16][13]=1578;	VEL[17][13]=2174;	VEL[18][13]=2941;	VEL[19][13]=3810;
        VEL[10][14]=156;	VEL[11][14]=262;	VEL[12][14]=427;	VEL[13][14]=643;	VEL[14][14]=943;	VEL[15][14]=1360;	VEL[16][14]=1931;	VEL[17][14]=2634;	VEL[18][14]=3580;	VEL[19][14]=4647;
        VEL[10][15]=191;	VEL[11][15]=325;	VEL[12][15]=524;	VEL[13][15]=797;	VEL[14][15]=1185;	VEL[15][15]=1704;	VEL[16][15]=2414;	VEL[17][15]=3306;	VEL[18][15]=4482;	VEL[19][15]=5780;
        VEL[10][16]=245;	VEL[11][16]=415;	VEL[12][16]=672;	VEL[13][16]=1037;	VEL[14][16]=1523;	VEL[15][16]=2199;	VEL[16][16]=3131;	VEL[17][16]=4247;	VEL[18][16]=5837;	VEL[19][16]=7528;
        VEL[10][17]=330;	VEL[11][17]=558;	VEL[12][17]=902;	VEL[13][17]=1403;	VEL[14][17]=2064;	VEL[15][17]=2947;	VEL[16][17]=4214;	VEL[17][17]=5747;	VEL[18][17]=7808;	VEL[19][17]=10154;
        VEL[10][18]=471;	VEL[11][18]=801;	VEL[12][18]=1294;	VEL[13][18]=2039;	VEL[14][18]=3001;	VEL[15][18]=4315;	VEL[16][18]=6124;	VEL[17][18]=8330;	VEL[18][18]=11280;	VEL[19][18]=14794;
        VEL[10][19]=578;	VEL[11][19]=998;	VEL[12][19]=1615;	VEL[13][19]=2550;	VEL[14][19]=3734;	VEL[15][19]=5336;	VEL[16][19]=7664;	VEL[17][19]=10447;	VEL[18][19]=13890;	VEL[19][19]=18319;
        VEL[10][20]=739;	VEL[11][20]=1291;	VEL[12][20]=2086;	VEL[13][20]=3296;	VEL[14][20]=4828;	VEL[15][20]=6893;	VEL[16][20]=9939;	VEL[17][20]=13392;	VEL[18][20]=17962;	VEL[19][20]=23652;
        VEL[10][21]=993;	VEL[11][21]=1729;	VEL[12][21]=2831;	VEL[13][21]=4444;	VEL[14][21]=6599;	VEL[15][21]=9331;	VEL[16][21]=13349;	VEL[17][21]=18181;	VEL[18][21]=24329;	VEL[19][21]=31902;
        VEL[10][22]=1422;	VEL[11][22]=2507;	VEL[12][22]=4123;	VEL[13][22]=6485;	VEL[14][22]=9557;	VEL[15][22]=13415;	VEL[16][22]=19459;	VEL[17][22]=26120;	VEL[18][22]=35793;	VEL[19][22]=46070;
        VEL[10][23]=2269;	VEL[11][23]=4018;	VEL[12][23]=6613;	VEL[13][23]=10337;	VEL[14][23]=15260;	VEL[15][23]=21822;	VEL[16][23]=30989;	VEL[17][23]=41730;	VEL[18][23]=56489;	VEL[19][23]=74242;
        VEL[10][24]=4354;	VEL[11][24]=7783;	VEL[12][24]=12889;	VEL[13][24]=19998;	VEL[14][24]=30025;	VEL[15][24]=43229;	VEL[16][24]=60798;	VEL[17][24]=82633;
        VEL[10][25]=5193;	VEL[11][25]=9247;	VEL[12][25]=15353;	VEL[13][25]=23915;	VEL[14][25]=35510;	VEL[15][25]=51712;	VEL[16][25]=72134;	VEL[17][25]=98728;
        VEL[10][26]=6337;	VEL[11][26]=11167;	VEL[12][26]=18680;	VEL[13][26]=29000;	VEL[14][26]=43425;	VEL[15][26]=62651;	VEL[16][26]=87881;
        VEL[10][27]=7929;	VEL[11][27]=13963;	VEL[12][27]=23110;	VEL[13][27]=36057;	VEL[14][27]=54052;	VEL[15][27]=78672;
        VEL[10][28]=10226;	VEL[11][28]=17955;	VEL[12][28]=29838;	VEL[13][28]=47014;	VEL[14][28]=70563;	VEL[15][28]=99807;
        VEL[10][29]=13984;	VEL[11][29]=24238;	VEL[12][29]=40507;	VEL[13][29]=63136;	VEL[14][29]=95913;
        VEL[10][30]=20225;	VEL[11][30]=35085;	VEL[12][30]=58429;	VEL[13][30]=92961;
        VEL[10][31]=25077;	VEL[11][31]=43728;	VEL[12][31]=73256;
        VEL[10][32]=32326;	VEL[11][32]=56594;	VEL[12][32]=94419;
        VEL[10][33]=44450;	VEL[11][33]=76648;
        VEL[10][34]=64069;

        VEL[20][0]=104;	VEL[21][0]=125;	VEL[22][0]=151;	VEL[23][0]=190;	VEL[24][0]=306;	VEL[25][0]=487;	VEL[26][0]=716;	VEL[27][0]=1014;	VEL[28][0]=1444;	VEL[29][0]=2006;
        VEL[20][1]=120;	VEL[21][1]=144;	VEL[22][1]=174;	VEL[23][1]=218;	VEL[24][1]=354;	VEL[25][1]=565;	VEL[26][1]=836;	VEL[27][1]=1195;	VEL[28][1]=1699;	VEL[29][1]=2373;
        VEL[20][2]=144;	VEL[21][2]=173;	VEL[22][2]=210;	VEL[23][2]=264;	VEL[24][2]=430;	VEL[25][2]=687;	VEL[26][2]=1017;	VEL[27][2]=1456;	VEL[28][2]=2069;	VEL[29][2]=2865;
        VEL[20][3]=178;	VEL[21][3]=215;	VEL[22][3]=262;	VEL[23][3]=331;	VEL[24][3]=533;	VEL[25][3]=858;	VEL[26][3]=1259;	VEL[27][3]=1822;	VEL[28][3]=2590;	VEL[29][3]=3577;
        VEL[20][4]=227;	VEL[21][4]=275;	VEL[22][4]=340;	VEL[23][4]=422;	VEL[24][4]=693;	VEL[25][4]=1110;	VEL[26][4]=1617;	VEL[27][4]=2349;	VEL[28][4]=3327;	VEL[29][4]=4592;
        VEL[20][5]=302;	VEL[21][5]=371;	VEL[22][5]=455;	VEL[23][5]=571;	VEL[24][5]=936;	VEL[25][5]=1499;	VEL[26][5]=2181;	VEL[27][5]=3188;	VEL[28][5]=4536;	VEL[29][5]=6153;
        VEL[20][6]=433;	VEL[21][6]=532;	VEL[22][6]=656;	VEL[23][6]=822;	VEL[24][6]=1338;	VEL[25][6]=2164;	VEL[26][6]=3167;	VEL[27][6]=4628;	VEL[28][6]=6630;	VEL[29][6]=8886;
        VEL[20][7]=533;	VEL[21][7]=657;	VEL[22][7]=818;	VEL[23][7]=1022;	VEL[24][7]=1691;	VEL[25][7]=2679;	VEL[26][7]=3972;	VEL[27][7]=5774;	VEL[28][7]=8252;	VEL[29][7]=11085;
        VEL[20][8]=686;	VEL[21][8]=851;	VEL[22][8]=1055;	VEL[23][8]=1318;	VEL[24][8]=2179;	VEL[25][8]=3472;	VEL[26][8]=5131;	VEL[27][8]=7483;	VEL[28][8]=10738;	VEL[29][8]=14431;
        VEL[20][9]=922;	VEL[21][9]=1139;	VEL[22][9]=1418;	VEL[23][9]=1793;	VEL[24][9]=2927;	VEL[25][9]=4683;	VEL[26][9]=6926;	VEL[27][9]=9986;	VEL[28][9]=14438;	VEL[29][9]=19331;
        VEL[20][10]=1319;	VEL[21][10]=1646;	VEL[22][10]=2049;	VEL[23][10]=2568;	VEL[24][10]=4248;	VEL[25][10]=6783;	VEL[26][10]=10059;	VEL[27][10]=14421;	VEL[28][10]=20965;	VEL[29][10]=27934;
        VEL[20][11]=2137;	VEL[21][11]=2636;	VEL[22][11]=3315;	VEL[23][11]=4137;	VEL[24][11]=6797;	VEL[25][11]=10963;	VEL[26][11]=16115;	VEL[27][11]=23116;	VEL[28][11]=33813;	VEL[29][11]=44764;
        VEL[20][12]=4174;	VEL[21][12]=5111;	VEL[22][12]=6585;	VEL[23][12]=8101;	VEL[24][12]=13243;	VEL[25][12]=21266;	VEL[26][12]=31459;	VEL[27][12]=45254;	VEL[28][12]=65997;	VEL[29][12]=89661;
        VEL[20][13]=5004;	VEL[21][13]=6112;	VEL[22][13]=7805;	VEL[23][13]=9624;	VEL[24][13]=15712;	VEL[25][13]=25264;	VEL[26][13]=37415;	VEL[27][13]=53593;	VEL[28][13]=78770;
        VEL[20][14]=6059;	VEL[21][14]=7413;	VEL[22][14]=9470;	VEL[23][14]=11732;	VEL[24][14]=19019;	VEL[25][14]=30633;	VEL[26][14]=44997;	VEL[27][14]=65623;	VEL[28][14]=95492;
        VEL[20][15]=7605;	VEL[21][15]=9215;	VEL[22][15]=11615;	VEL[23][15]=14567;	VEL[24][15]=23959;	VEL[25][15]=38408;	VEL[26][15]=56317;	VEL[27][15]=81345;
        VEL[20][16]=9741;	VEL[21][16]=11803;	VEL[22][16]=15040;	VEL[23][16]=18858;	VEL[24][16]=30786;	VEL[25][16]=49304;	VEL[26][16]=72343;	VEL[27][16]=99301;
        VEL[20][17]=13182;	VEL[21][17]=15971;	VEL[22][17]=20326;	VEL[23][17]=25260;	VEL[24][17]=41611;	VEL[25][17]=66815;	VEL[26][17]=97591;
        VEL[20][18]=18986;	VEL[21][18]=23001;	VEL[22][18]=29747;	VEL[23][18]=36717;	VEL[24][18]=60839;	VEL[25][18]=96714;
        VEL[20][19]=23642;	VEL[21][19]=29289;	VEL[22][19]=37087;	VEL[23][19]=45871;	VEL[24][19]=76233;
        VEL[20][20]=30370;	VEL[21][20]=37501;	VEL[22][20]=47826;	VEL[23][20]=59723;	VEL[24][20]=99075;
        VEL[20][21]=40656;	VEL[21][21]=50683;	VEL[22][21]=65150;	VEL[23][21]=80915;
        VEL[20][22]=58559;	VEL[21][22]=73221;	VEL[22][22]=94792;
        VEL[20][23]=95293;

        VEL[30][0]=2612;	VEL[31][0]=3428;	VEL[32][0]=5574;	VEL[33][0]=8626;	VEL[34][0]=19309;
        VEL[30][1]=3078;	VEL[31][1]=4064;	VEL[32][1]=6604;	VEL[33][1]=10292;	VEL[34][1]=22993;
        VEL[30][2]=3745;	VEL[31][2]=4935;	VEL[32][2]=8079;	VEL[33][2]=12590;	VEL[34][2]=28244;
        VEL[30][3]=4665;	VEL[31][3]=6153;	VEL[32][3]=10060;	VEL[33][3]=15762;	VEL[34][3]=35570;
        VEL[30][4]=6019;	VEL[31][4]=7956;	VEL[32][4]=12977;	VEL[33][4]=20380;	VEL[34][4]=45674;
        VEL[30][5]=8165;	VEL[31][5]=10768;	VEL[32][5]=17613;	VEL[33][5]=27919;	VEL[34][5]=62478;
        VEL[30][6]=11825;	VEL[31][6]=15667;	VEL[32][6]=25594;	VEL[33][6]=40654;	VEL[34][6]=90520;
        VEL[30][7]=14662;	VEL[31][7]=19626;	VEL[32][7]=32311;	VEL[33][7]=50387;
        VEL[30][8]=19027;	VEL[31][8]=25106;	VEL[32][8]=41577;	VEL[33][8]=64822;
        VEL[30][9]=25848;	VEL[31][9]=33753;	VEL[32][9]=56716;	VEL[33][9]=89279;
        VEL[30][10]=37073;	VEL[31][10]=49016;	VEL[32][10]=81757;
        VEL[30][11]=59949;	VEL[31][11]=79263;


    }

    public void valores_3(){  // Posiciones de las perdidas.
        PE[0]=10;	PE[10]=2.0;	PE[19]=0.35;	PE[28]=0.06;
        PE[1]=9;	PE[11]=1.5;	PE[20]=0.30;	PE[29]=0.05;
        PE[2]=8;	PE[12]=1.0;	PE[21]=0.25;	PE[30]=0.04;
        PE[3]=7;	PE[13]=0.9;	PE[22]=0.20;	PE[31]=0.035;
        PE[4]=6;	PE[14]=0.8;	PE[23]=0.15;	PE[32]=0.030;
        PE[5]=5;	PE[15]=0.7;	PE[24]=0.10;	PE[33]=0.025;
        PE[6]=4;	PE[16]=0.6;	PE[25]=0.09;	PE[34]=0.020;
        PE[7]=3.5;	PE[17]=0.5;	PE[26]=0.08;	PE[35]=0.015;
        PE[8]=3.0;	PE[18]=0.4;	PE[27]=0.07;	PE[36]=0.010;
        PE[9]=2.5;
    }

    public void valores_4(){  // diametros equivalentes, de la grafica de ducto rectangular

        D[0]=4;
        D[1]=5;
        D[2]=6;
        D[3]=7;
        D[4]=8;
        D[5]=9;
        D[6]=10;
        D[7]=12;
        D[8]=14;
        D[9]=16;
        D[10]=18;
        D[11]=20;
        D[12]=22;
        D[13]=24;
        D[14]=26;
        D[15]=28;
        D[16]=30;
        D[17]=32;
        D[18]=34;
        D[19]=36;
        D[20]=38;
        D[21]=40;
        D[22]=45;
        D[23]=50;
    }

    public void valores_5(){ // lado vertical de la grafica de ducto rectangular

        D1[0]=3.0;	    D1[40]=26;
        D1[1]=3.5;	    D1[41]=27;
        D1[2]=4.0;	    D1[42]=28;
        D1[3]=4.5;	    D1[43]=29;
        D1[4]=5.0;	    D1[44]=30;
        D1[5]=5.5;	    D1[45]=32;
        D1[6]=6.0;	    D1[46]=34;
        D1[7]=6.5;	    D1[47]=36;
        D1[8]=7.0;	    D1[48]=38;
        D1[9]=7.5;	    D1[49]=40;
        D1[10]=8.0;	    D1[50]=42;
        D1[11]=8.5;	    D1[51]=44;
        D1[12]=9.0;	    D1[52]=46;
        D1[13]=9.5;	    D1[53]=48;
        D1[14]=10.0;	D1[54]=50;
        D1[15]=10.5;	D1[55]=52;
        D1[16]=11.0;	D1[56]=54;
        D1[17]=11.5;	D1[57]=56;
        D1[18]=12.0;	D1[58]=58;
        D1[19]=12.5;	D1[59]=60;
        D1[20]=13.0;	D1[60]=62;
        D1[21]=13.5;	D1[61]=64;
        D1[22]=14.0;	D1[62]=66;
        D1[23]=14.5;	D1[63]=68;
        D1[24]=15.0;	D1[64]=70;
        D1[25]=15.5;	D1[65]=72;
        D1[26]=16.0;	D1[66]=74;
        D1[27]=16.5;	D1[67]=76;
        D1[28]=17.0;	D1[68]=78;
        D1[29]=17.5;	D1[69]=80;
        D1[30]=18.0;	D1[70]=82;
        D1[31]=18.5;	D1[71]=84;
        D1[32]=19.0;	D1[72]=86;
        D1[33]=19.5;	D1[73]=88;
        D1[34]=20;	    D1[74]=90;
        D1[35]=21;	    D1[75]=92;
        D1[36]=22;	    D1[76]=94;
        D1[37]=23;	    D1[77]=96;
        D1[38]=24;	    D1[78]=98;
        D1[39]=25;	    D1[79]=100;

    }

    public void valores_6(){ // lado vertical de la grafica de ducto rectangular

        D2[0][0]=4.31;	D2[1][0]=6.96;	D2[2][0]=10.68;	D2[3][0]=15.85;	D2[4][0]=21.49;	D2[5][0]=29.04;	D2[6][0]=0.00;	D2[7][0]=0.00;	D2[8][0]=0.00;	D2[9][0]=0.00;	    D2[10][0]=0.00;	    D2[11][0]=0.00;	    D2[12][0]=0.00;	    D2[13][0]=0.00;	    D2[14][0]=0.00;	    D2[15][0]=0.00;	    D2[16][0]=0.00;	    D2[17][0]=0.00;	    D2[18][0]=0.00;	    D2[19][0]=0.00;	    D2[20][0]=0.00;	    D2[21][0]=0.00;	    D2[22][0]=0.00;	    D2[23][0]=0.00;
        D2[0][1]=3.66;	D2[1][1]=5.86;	D2[2][1]=8.82;	D2[3][1]=12.73;	D2[4][1]=17.25;	D2[5][1]=23.10;	D2[6][1]=0.00;	D2[7][1]=0.00;	D2[8][1]=0.00;	D2[9][1]=0.00;	    D2[10][1]=0.00;	    D2[11][1]=0.00;	    D2[12][1]=0.00;	    D2[13][1]=0.00;	    D2[14][1]=0.00;	    D2[15][1]=0.00;	    D2[16][1]=0.00;	    D2[17][1]=0.00;	    D2[18][1]=0.00;	    D2[19][1]=0.00;	    D2[20][1]=0.00;	    D2[21][1]=0.00;	    D2[22][1]=0.00;	    D2[23][1]=0.00;
        D2[0][2]=3.20;	D2[1][2]=5.09;	D2[2][2]=7.55;	D2[3][2]=10.66;	D2[4][2]=14.44;	D2[5][2]=19.11;	D2[6][2]=24.48;	D2[7][2]=0.00;	D2[8][2]=0.00;	D2[9][2]=0.00;	    D2[10][2]=0.00;	    D2[11][2]=0.00;	    D2[12][2]=0.00;	    D2[13][2]=0.00;	    D2[14][2]=0.00;	    D2[15][2]=0.00;	    D2[16][2]=0.00;	    D2[17][2]=0.00;	    D2[18][2]=0.00;	    D2[19][2]=0.00;	    D2[20][2]=0.00;	    D2[21][2]=0.00;	    D2[22][2]=0.00;	    D2[23][2]=0.00;
        D2[1][3]=4.53;	D2[2][3]=6.65;	D2[3][3]=9.26;	D2[4][3]=12.43;	D2[5][3]=16.28;	D2[6][3]=20.60;	D2[7][3]=0.00;	D2[8][3]=0.00;	D2[9][3]=0.00;	    D2[10][3]=0.00;	    D2[11][3]=0.00;	    D2[12][3]=0.00;	    D2[13][3]=0.00;	    D2[14][3]=0.00;	    D2[15][3]=0.00;	    D2[16][3]=0.00;	    D2[17][3]=0.00;	    D2[18][3]=0.00;	    D2[19][3]=0.00;	    D2[20][3]=0.00;	    D2[21][3]=0.00;	    D2[22][3]=0.00;	    D2[23][3]=0.00;
        D2[1][4]=4.10;	D2[2][4]=5.95;	D2[3][4]=8.20;	D2[4][4]=10.87;	D2[5][4]=14.26;	D2[6][4]=17.87;	D2[7][4]=29.62;	D2[8][4]=0.00;	D2[9][4]=0.00;	    D2[10][4]=0.00;	    D2[11][4]=0.00;	    D2[12][4]=0.00;	    D2[13][4]=0.00;	    D2[14][4]=0.00;	    D2[15][4]=0.00;	    D2[16][4]=0.00;	    D2[17][4]=0.00;	    D2[18][4]=0.00;	    D2[19][4]=0.00;	    D2[20][4]=0.00;	    D2[21][4]=0.00;	    D2[22][4]=0.00;	    D2[23][4]=0.00;
        D2[1][5]=3.74;	D2[2][5]=5.42;	D2[3][5]=7.42;	D2[4][5]=9.70;	D2[5][5]=12.68;	D2[6][5]=15.90;	D2[7][5]=25.53;	D2[8][5]=0.00;	D2[9][5]=0.00;	    D2[10][5]=0.00;	    D2[11][5]=0.00;	    D2[12][5]=0.00;	    D2[13][5]=0.00;	    D2[14][5]=0.00;	    D2[15][5]=0.00;	    D2[16][5]=0.00;	    D2[17][5]=0.00;	    D2[18][5]=0.00;	    D2[19][5]=0.00;	    D2[20][5]=0.00;	    D2[21][5]=0.00;	    D2[22][5]=0.00;	    D2[23][5]=0.00;
        D2[1][6]=3.47;	D2[2][6]=5.00;	D2[3][6]=6.77;	D2[4][6]=8.78;	D2[5][6]=11.41;	D2[6][6]=14.28;	D2[7][6]=22.41;	D2[8][6]=0.00;	D2[9][6]=0.00;	    D2[10][6]=0.00;	    D2[11][6]=0.00;	    D2[12][6]=0.00;	    D2[13][6]=0.00;	    D2[14][6]=0.00;	    D2[15][6]=0.00;	    D2[16][6]=0.00;	    D2[17][6]=0.00;	    D2[18][6]=0.00;	    D2[19][6]=0.00;	    D2[20][6]=0.00;	    D2[21][6]=0.00;	    D2[22][6]=0.00;	    D2[23][6]=0.00;
        D2[1][7]=3.25;	D2[2][7]=4.66;	D2[3][7]=6.26;	D2[4][7]=8.00;	D2[5][7]=10.38;	D2[6][7]=12.98;	D2[7][7]=20.00;	D2[8][7]=29.06;	D2[9][7]=0.00;	    D2[10][7]=0.00;	    D2[11][7]=0.00;	    D2[12][7]=0.00;	    D2[13][7]=0.00;	    D2[14][7]=0.00;	    D2[15][7]=0.00;	    D2[16][7]=0.00;	    D2[17][7]=0.00;	    D2[18][7]=0.00;	    D2[19][7]=0.00;	    D2[20][7]=0.00;	    D2[21][7]=0.00;	    D2[22][7]=0.00;	    D2[23][7]=0.00;
        D2[1][8]=3.06;	D2[2][8]=4.36;	D2[3][8]=5.82;	D2[4][8]=7.43;	D2[5][8]=9.55;	D2[6][8]=11.87;	D2[7][8]=18.14;	D2[8][8]=26.13;	D2[9][8]=0.00;	    D2[10][8]=0.00;	    D2[11][8]=0.00;	    D2[12][8]=0.00;	    D2[13][8]=0.00;	    D2[14][8]=0.00;	    D2[15][8]=0.00;	    D2[16][8]=0.00;	    D2[17][8]=0.00;	    D2[18][8]=0.00;	    D2[19][8]=0.00;	    D2[20][8]=0.00;	    D2[21][8]=0.00;	    D2[22][8]=0.00;	    D2[23][8]=0.00;
        D2[2][9]=4.12;	D2[3][9]=5.46;	D2[4][9]=6.94;	D2[5][9]=8.86;	D2[6][9]=10.99;	D2[7][9]=16.60;	D2[8][9]=23.61;	D2[9][9]=0.00;	    D2[10][9]=0.00;	    D2[11][9]=0.00;	    D2[12][9]=0.00;	    D2[13][9]=0.00;	    D2[14][9]=0.00;	    D2[15][9]=0.00;	    D2[16][9]=0.00;	    D2[17][9]=0.00;	    D2[18][9]=0.00;	    D2[19][9]=0.00;	    D2[20][9]=0.00;	    D2[21][9]=0.00;	    D2[22][9]=0.00;	    D2[23][9]=0.00;
        D2[2][10]=3.90;	D2[3][10]=5.16;	D2[4][10]=6.51;	D2[5][10]=8.29;	D2[6][10]=10.26;D2[7][10]=15.33;D2[8][10]=21.63;D2[9][10]=29.86;	D2[10][10]=0.00;	D2[11][10]=0.00;	D2[12][10]=0.00;	D2[13][10]=0.00;	D2[14][10]=0.00;	D2[15][10]=0.00;	D2[16][10]=0.00;	D2[17][10]=0.00;	D2[18][10]=0.00;	D2[19][10]=0.00;	D2[20][10]=0.00;	D2[21][10]=0.00;	D2[22][10]=0.00;	D2[23][10]=0.00;
        D2[2][11]=3.71;	D2[3][11]=4.90;	D2[4][11]=6.16;	D2[5][11]=7.82;	D2[6][11]=9.65;	D2[7][11]=14.29;D2[8][11]=19.98;D2[9][11]=27.48;	D2[10][11]=0.00;	D2[11][11]=0.00;	D2[12][11]=0.00;	D2[13][11]=0.00;	D2[14][11]=0.00;	D2[15][11]=0.00;	D2[16][11]=0.00;	D2[17][11]=0.00;	D2[18][11]=0.00;	D2[19][11]=0.00;	D2[20][11]=0.00;	D2[21][11]=0.00;	D2[22][11]=0.00;	D2[23][11]=0.00;
        D2[2][12]=3.55;	D2[3][12]=4.67;	D2[4][12]=5.85;	D2[5][12]=7.43;	D2[6][12]=9.12;	D2[7][12]=13.45;D2[8][12]=18.68;D2[9][12]=25.43;	D2[10][12]=0.00;	D2[11][12]=0.00;	D2[12][12]=0.00;	D2[13][12]=0.00;	D2[14][12]=0.00;	D2[15][12]=0.00;	D2[16][12]=0.00;	D2[17][12]=0.00;	D2[18][12]=0.00;	D2[19][12]=0.00;	D2[20][12]=0.00;	D2[21][12]=0.00;	D2[22][12]=0.00;	D2[23][12]=0.00;
        D2[2][13]=3.41;	D2[3][13]=4.46;	D2[4][13]=5.58;	D2[5][13]=7.08;	D2[6][13]=8.61;	D2[7][13]=12.68;D2[8][13]=17.59;D2[9][13]=23.76;	D2[10][13]=0.00;	D2[11][13]=0.00;	D2[12][13]=0.00;	D2[13][13]=0.00;	D2[14][13]=0.00;	D2[15][13]=0.00;	D2[16][13]=0.00;	D2[17][13]=0.00;	D2[18][13]=0.00;	D2[19][13]=0.00;	D2[20][13]=0.00;	D2[21][13]=0.00;	D2[22][13]=0.00;	D2[23][13]=0.00;
        D2[2][14]=3.28;	D2[3][14]=4.29;	D2[4][14]=5.33;	D2[5][14]=6.77;	D2[6][14]=8.18;	D2[7][14]=12.02;D2[8][14]=16.59;D2[9][14]=22.31;	D2[10][14]=29.29;	D2[11][14]=0.00;	D2[12][14]=0.00;	D2[13][14]=0.00;	D2[14][14]=0.00;	D2[15][14]=0.00;	D2[16][14]=0.00;	D2[17][14]=0.00;	D2[18][14]=0.00;	D2[19][14]=0.00;	D2[20][14]=0.00;	D2[21][14]=0.00;	D2[22][14]=0.00;	D2[23][14]=0.00;
        D2[2][15]=3.17;	D2[3][15]=4.13;	D2[4][15]=5.13;	D2[5][15]=6.48;	D2[6][15]=7.81;	D2[7][15]=11.42;D2[8][15]=15.72;D2[9][15]=21.07;	D2[10][15]=27.61;	D2[11][15]=0.00;	D2[12][15]=0.00;	D2[13][15]=0.00;	D2[14][15]=0.00;	D2[15][15]=0.00;	D2[16][15]=0.00;	D2[17][15]=0.00;	D2[18][15]=0.00;	D2[19][15]=0.00;	D2[20][15]=0.00;	D2[21][15]=0.00;	D2[22][15]=0.00;	D2[23][15]=0.00;
        D2[2][16]=3.07;	D2[3][16]=3.98;	D2[4][16]=4.94;	D2[5][16]=6.22;	D2[6][16]=7.49;	D2[7][16]=10.90;D2[8][16]=14.93;D2[9][16]=19.97;	D2[10][16]=26.03;	D2[11][16]=0.00;	D2[12][16]=0.00;	D2[13][16]=0.00;	D2[14][16]=0.00;	D2[15][16]=0.00;	D2[16][16]=0.00;	D2[17][16]=0.00;	D2[18][16]=0.00;	D2[19][16]=0.00;	D2[20][16]=0.00;	D2[21][16]=0.00;	D2[22][16]=0.00;	D2[23][16]=0.00;
        D2[3][17]=3.85;	D2[4][17]=4.77;	D2[5][17]=5.99;	D2[6][17]=7.21;	D2[7][17]=10.41;D2[8][17]=14.26;D2[9][17]=18.95;	D2[10][17]=24.64;	D2[11][17]=0.00;	D2[12][17]=0.00;	D2[13][17]=0.00;	D2[14][17]=0.00;	D2[15][17]=0.00;	D2[16][17]=0.00;	D2[17][17]=0.00;	D2[18][17]=0.00;	D2[19][17]=0.00;	D2[20][17]=0.00;	D2[21][17]=0.00;	D2[22][17]=0.00;	D2[23][17]=0.00;
        D2[3][18]=3.74;	D2[4][18]=4.62;	D2[5][18]=5.78;	D2[6][18]=6.98;	D2[7][18]=9.97;	D2[8][18]=13.63;D2[9][18]=18.00;	D2[10][18]=23.40;	D2[11][18]=29.60;	D2[12][18]=0.00;	D2[13][18]=0.00;	D2[14][18]=0.00;	D2[15][18]=0.00;	D2[16][18]=0.00;	D2[17][18]=0.00;	D2[18][18]=0.00;	D2[19][18]=0.00;	D2[20][18]=0.00;	D2[21][18]=0.00;	D2[22][18]=0.00;	D2[23][18]=0.00;
        D2[3][19]=3.62;	D2[4][19]=4.48;	D2[5][19]=5.58;	D2[6][19]=6.78;	D2[7][19]=9.59;	D2[8][19]=13.08;D2[9][19]=17.20;	D2[10][19]=22.34;	D2[11][19]=28.12;	D2[12][19]=0.00;	D2[13][19]=0.00;	D2[14][19]=0.00;	D2[15][19]=0.00;	D2[16][19]=0.00;	D2[17][19]=0.00;	D2[18][19]=0.00;	D2[19][19]=0.00;	D2[20][19]=0.00;	D2[21][19]=0.00;	D2[22][19]=0.00;	D2[23][19]=0.00;
        D2[3][20]=3.52;	D2[4][20]=4.34;	D2[5][20]=5.41;	D2[6][20]=6.55;	D2[7][20]=9.25;	D2[8][20]=12.59;D2[9][20]=16.49;	D2[10][20]=21.34;	D2[11][20]=26.74;	D2[12][20]=0.00;	D2[13][20]=0.00;	D2[14][20]=0.00;	D2[15][20]=0.00;	D2[16][20]=0.00;	D2[17][20]=0.00;	D2[18][20]=0.00;	D2[19][20]=0.00;	D2[20][20]=0.00;	D2[21][20]=0.00;	D2[22][20]=0.00;	D2[23][20]=0.00;
        D2[3][21]=3.43;	D2[4][21]=4.23;	D2[5][21]=5.24;	D2[6][21]=6.35;	D2[7][21]=8.94;	D2[8][21]=12.15;D2[9][21]=15.85;	D2[10][21]=20.41;	D2[11][21]=25.50;	D2[12][21]=0.00;	D2[13][21]=0.00;	D2[14][21]=0.00;	D2[15][21]=0.00;	D2[16][21]=0.00;	D2[17][21]=0.00;	D2[18][21]=0.00;	D2[19][21]=0.00;	D2[20][21]=0.00;	D2[21][21]=0.00;	D2[22][21]=0.00;	D2[23][21]=0.00;
        D2[3][22]=3.35;	D2[4][22]=4.11;	D2[5][22]=5.09;	D2[6][22]=6.16;	D2[7][22]=8.64;	D2[8][22]=11.71;D2[9][22]=15.27;	D2[10][22]=19.61;	D2[11][22]=24.47;	D2[12][22]=0.00;	D2[13][22]=0.00;	D2[14][22]=0.00;	D2[15][22]=0.00;	D2[16][22]=0.00;	D2[17][22]=0.00;	D2[18][22]=0.00;	D2[19][22]=0.00;	D2[20][22]=0.00;	D2[21][22]=0.00;	D2[22][22]=0.00;	D2[23][22]=0.00;
        D2[3][23]=3.27;	D2[4][23]=4.01;	D2[5][23]=4.96;	D2[6][23]=5.97;	D2[7][23]=8.39;	D2[8][23]=11.35;D2[9][23]=14.74;	D2[10][23]=18.91;	D2[11][23]=23.52;	D2[12][23]=0.00;	D2[13][23]=0.00;	D2[14][23]=0.00;	D2[15][23]=0.00;	D2[16][23]=0.00;	D2[17][23]=0.00;	D2[18][23]=0.00;	D2[19][23]=0.00;	D2[20][23]=0.00;	D2[21][23]=0.00;	D2[22][23]=0.00;	D2[23][23]=0.00;
        D2[3][24]=3.20;	D2[4][24]=3.90;	D2[5][24]=4.83;	D2[6][24]=5.81;	D2[7][24]=8.17;	D2[8][24]=10.95;D2[9][24]=14.25;	D2[10][24]=18.21;	D2[11][24]=22.63;	D2[12][24]=28.51;	D2[13][24]=0.00;	D2[14][24]=0.00;	D2[15][24]=0.00;	D2[16][24]=0.00;	D2[17][24]=0.00;	D2[18][24]=0.00;	D2[19][24]=0.00;	D2[20][24]=0.00;	D2[21][24]=0.00;	D2[22][24]=0.00;	D2[23][24]=0.00;
        D2[3][25]=3.13;	D2[4][25]=3.81;	D2[5][25]=4.71;	D2[6][25]=5.67;	D2[7][25]=7.94;	D2[8][25]=10.60;D2[9][25]=13.76;	D2[10][25]=17.61;	D2[11][25]=21.83;	D2[12][25]=27.41;	D2[13][25]=0.00;	D2[14][25]=0.00;	D2[15][25]=0.00;	D2[16][25]=0.00;	D2[17][25]=0.00;	D2[18][25]=0.00;	D2[19][25]=0.00;	D2[20][25]=0.00;	D2[21][25]=0.00;	D2[22][25]=0.00;	D2[23][25]=0.00;
        D2[3][26]=3.06;	D2[4][26]=3.73;	D2[5][26]=4.61;	D2[6][26]=5.52;	D2[7][26]=7.73;	D2[8][26]=10.27;D2[9][26]=13.30;	D2[10][26]=17.03;	D2[11][26]=21.07;	D2[12][26]=26.34;	D2[13][26]=0.00;	D2[14][26]=0.00;	D2[15][26]=0.00;	D2[16][26]=0.00;	D2[17][26]=0.00;	D2[18][26]=0.00;	D2[19][26]=0.00;	D2[20][26]=0.00;	D2[21][26]=0.00;	D2[22][26]=0.00;	D2[23][26]=0.00;
        D2[4][27]=3.65;	D2[5][27]=4.50;	D2[6][27]=5.39;	D2[7][27]=7.54;	D2[8][27]=9.97;	D2[9][27]=12.90;	D2[10][27]=16.53;	D2[11][27]=20.39;	D2[12][27]=25.36;	D2[13][27]=0.00;	D2[14][27]=0.00;	D2[15][27]=0.00;	D2[16][27]=0.00;	D2[17][27]=0.00;	D2[18][27]=0.00;	D2[19][27]=0.00;	D2[20][27]=0.00;	D2[21][27]=0.00;	D2[22][27]=0.00;	D2[23][27]=0.00;
        D2[4][28]=3.58;	D2[5][28]=4.40;	D2[6][28]=5.26;	D2[7][28]=7.37;	D2[8][28]=9.73;	D2[9][28]=12.49;	D2[10][28]=16.02;	D2[11][28]=19.74;	D2[12][28]=24.50;	D2[13][28]=29.50;	D2[14][28]=0.00;	D2[15][28]=0.00;	D2[16][28]=0.00;	D2[17][28]=0.00;	D2[18][28]=0.00;	D2[19][28]=0.00;	D2[20][28]=0.00;	D2[21][28]=0.00;	D2[22][28]=0.00;	D2[23][28]=0.00;
        D2[4][29]=3.51;	D2[5][29]=4.31;	D2[6][29]=5.15;	D2[7][29]=7.19;	D2[8][29]=9.48;	D2[9][29]=12.16;	D2[10][29]=15.57;	D2[11][29]=19.16;	D2[12][29]=23.69;	D2[13][29]=28.47;	D2[14][29]=0.00;	D2[15][29]=0.00;	D2[16][29]=0.00;	D2[17][29]=0.00;	D2[18][29]=0.00;	D2[19][29]=0.00;	D2[20][29]=0.00;	D2[21][29]=0.00;	D2[22][29]=0.00;	D2[23][29]=0.00;
        D2[4][30]=3.44;	D2[5][30]=4.22;	D2[6][30]=5.05;	D2[7][30]=7.02;	D2[8][30]=9.28;	D2[9][30]=11.81;	D2[10][30]=15.09;	D2[11][30]=18.57;	D2[12][30]=22.99;	D2[13][30]=27.52;	D2[14][30]=0.00;	D2[15][30]=0.00;	D2[16][30]=0.00;	D2[17][30]=0.00;	D2[18][30]=0.00;	D2[19][30]=0.00;	D2[20][30]=0.00;	D2[21][30]=0.00;	D2[22][30]=0.00;	D2[23][30]=0.00;
        D2[4][31]=3.38;	D2[5][31]=4.14;	D2[6][31]=4.95;	D2[7][31]=6.87;	D2[8][31]=9.09;	D2[9][31]=11.49;	D2[10][31]=14.71;	D2[11][31]=18.09;	D2[12][31]=22.31;	D2[13][31]=26.63;	D2[14][31]=0.00;	D2[15][31]=0.00;	D2[16][31]=0.00;	D2[17][31]=0.00;	D2[18][31]=0.00;	D2[19][31]=0.00;	D2[20][31]=0.00;	D2[21][31]=0.00;	D2[22][31]=0.00;	D2[23][31]=0.00;
        D2[4][32]=3.32;	D2[5][32]=4.07;	D2[6][32]=4.85;	D2[7][32]=6.71;	D2[8][32]=8.90;	D2[9][32]=11.20;	D2[10][32]=14.33;	D2[11][32]=17.57;	D2[12][32]=21.68;	D2[13][32]=25.85;	D2[14][32]=29.92;	D2[15][32]=0.00;	D2[16][32]=0.00;	D2[17][32]=0.00;	D2[18][32]=0.00;	D2[19][32]=0.00;	D2[20][32]=0.00;	D2[21][32]=0.00;	D2[22][32]=0.00;	D2[23][32]=0.00;
        D2[4][33]=3.27;	D2[5][33]=3.99;	D2[6][33]=4.76;	D2[7][33]=6.58;	D2[8][33]=8.71;	D2[9][33]=10.96;	D2[10][33]=13.96;	D2[11][33]=17.12;	D2[12][33]=21.06;	D2[13][33]=25.07;	D2[14][33]=29.60;	D2[15][33]=0.00;	D2[16][33]=0.00;	D2[17][33]=0.00;	D2[18][33]=0.00;	D2[19][33]=0.00;	D2[20][33]=0.00;	D2[21][33]=0.00;	D2[22][33]=0.00;	D2[23][33]=0.00;
        D2[4][34]=3.21;	D2[5][34]=3.92;	D2[6][34]=4.67;	D2[7][34]=6.44;	D2[8][34]=8.52;	D2[9][34]=10.73;	D2[10][34]=13.63;	D2[11][34]=16.69;	D2[12][34]=20.55;	D2[13][34]=24.32;	D2[14][34]=28.53;	D2[15][34]=0.00;	D2[16][34]=0.00;	D2[17][34]=0.00;	D2[18][34]=0.00;	D2[19][34]=0.00;	D2[20][34]=0.00;	D2[21][34]=0.00;	D2[22][34]=0.00;	D2[23][34]=0.00;
        D2[4][35]=3.12;	D2[5][35]=3.79;	D2[6][35]=4.50;	D2[7][35]=6.20;	D2[8][35]=8.18;	D2[9][35]=10.31;	D2[10][35]=13.01;	D2[11][35]=15.92;	D2[12][35]=19.54;	D2[13][35]=23.10;	D2[14][35]=26.91;	D2[15][35]=0.00;	D2[16][35]=0.00;	D2[17][35]=0.00;	D2[18][35]=0.00;	D2[19][35]=0.00;	D2[20][35]=0.00;	D2[21][35]=0.00;	D2[22][35]=0.00;	D2[23][35]=0.00;
        D2[4][36]=3.03;	D2[5][36]=3.68;	D2[6][36]=4.35;	D2[7][36]=5.98;	D2[8][36]=7.85;	D2[9][36]=9.94;	    D2[10][36]=12.43;	D2[11][36]=15.24;	D2[12][36]=18.66;	D2[13][36]=21.97;	D2[14][36]=25.65;	D2[15][36]=0.00;	D2[16][36]=0.00;	D2[17][36]=0.00;	D2[18][36]=0.00;	D2[19][36]=0.00;	D2[20][36]=0.00;	D2[21][36]=0.00;	D2[22][36]=0.00;	D2[23][36]=0.00;
        D2[5][37]=3.56;	D2[6][37]=4.22;	D2[7][37]=5.79;	D2[8][37]=7.57;	D2[9][37]=9.61;	    D2[10][37]=11.97;	D2[11][37]=14.62;	D2[12][37]=17.93;	D2[13][37]=21.03;	D2[14][37]=24.46;	D2[15][37]=29.03;	D2[16][37]=0.00;	D2[17][37]=0.00;	D2[18][37]=0.00;	D2[19][37]=0.00;	D2[20][37]=0.00;	D2[21][37]=0.00;	D2[22][37]=0.00;	D2[23][37]=0.00;
        D2[5][38]=3.46;	D2[6][38]=4.09;	D2[7][38]=5.61;	D2[8][38]=7.31;	D2[9][38]=9.30;	    D2[10][38]=11.53;	D2[11][38]=14.08;	D2[12][38]=17.26;	D2[13][38]=20.18;	D2[14][38]=23.41;	D2[15][38]=27.79;	D2[16][38]=0.00;	D2[17][38]=0.00;	D2[18][38]=0.00;	D2[19][38]=0.00;	D2[20][38]=0.00;	D2[21][38]=0.00;	D2[22][38]=0.00;	D2[23][38]=0.00;
        D2[5][39]=3.37;	D2[6][39]=3.97;	D2[7][39]=5.45;	D2[8][39]=7.09;	D2[9][39]=9.01;	    D2[10][39]=11.17;	D2[11][39]=13.57;	D2[12][39]=16.64;	D2[13][39]=19.39;	D2[14][39]=22.47;	D2[15][39]=26.53;	D2[16][39]=0.00;	D2[17][39]=0.00;	D2[18][39]=0.00;	D2[19][39]=0.00;	D2[20][39]=0.00;	D2[21][39]=0.00;	D2[22][39]=0.00;	D2[23][39]=0.00;
        D2[5][40]=3.28;	D2[6][40]=3.87;	D2[7][40]=5.29;	D2[8][40]=6.86;	D2[9][40]=8.75;	    D2[10][40]=10.82;	D2[11][40]=13.10;	D2[12][40]=16.12;	D2[13][40]=18.74;	D2[14][40]=21.64;	D2[15][40]=25.50;	D2[16][40]=29.66;	D2[17][40]=0.00;	D2[18][40]=0.00;	D2[19][40]=0.00;	D2[20][40]=0.00;	D2[21][40]=0.00;	D2[22][40]=0.00;	D2[23][40]=0.00;
        D2[5][41]=3.20;	D2[6][41]=3.78;	D2[7][41]=5.14;	D2[8][41]=6.67;	D2[9][41]=8.51;	    D2[10][41]=10.51;	D2[11][41]=12.69;	D2[12][41]=15.61;	D2[13][41]=18.06;	D2[14][41]=20.92;	D2[15][41]=24.58;	D2[16][41]=28.50;	D2[17][41]=0.00;	D2[18][41]=0.00;	D2[19][41]=0.00;	D2[20][41]=0.00;	D2[21][41]=0.00;	D2[22][41]=0.00;	D2[23][41]=0.00;
        D2[5][42]=3.12;	D2[6][42]=3.69;	D2[7][42]=5.01;	D2[8][42]=6.48;	D2[9][42]=8.29;	    D2[10][42]=10.22;	D2[11][42]=12.29;	D2[12][42]=15.12;	D2[13][42]=17.52;	D2[14][42]=20.25;	D2[15][42]=23.76;	D2[16][42]=27.50;	D2[17][42]=0.00;	D2[18][42]=0.00;	D2[19][42]=0.00;	D2[20][42]=0.00;	D2[21][42]=0.00;	D2[22][42]=0.00;	D2[23][42]=0.00;
        D2[5][43]=3.05;	D2[6][43]=3.60;	D2[7][43]=4.88;	D2[8][43]=6.31;	D2[9][43]=8.09;	    D2[10][43]=9.96;	D2[11][43]=11.90;	D2[12][43]=14.67;	D2[13][43]=16.98;	D2[14][43]=19.60;	D2[15][43]=23.00;	D2[16][43]=26.60;	D2[17][43]=0.00;	D2[18][43]=0.00;	D2[19][43]=0.00;	D2[20][43]=0.00;	D2[21][43]=0.00;	D2[22][43]=0.00;	D2[23][43]=0.00;
        D2[6][44]=3.52;	D2[7][44]=4.77;	D2[8][44]=6.15;	D2[9][44]=7.88;	    D2[10][44]=9.70;	D2[11][44]=11.65;	D2[12][44]=14.23;	D2[13][44]=16.52;	D2[14][44]=19.02;	D2[15][44]=22.26;	D2[16][44]=25.78;	D2[17][44]=29.45;	D2[18][44]=0.00;	D2[19][44]=0.00;	D2[20][44]=0.00;	D2[21][44]=0.00;	D2[22][44]=0.00;	D2[23][44]=0.00;
        D2[6][45]=3.38;	D2[7][45]=4.57;	D2[8][45]=5.86;	D2[9][45]=7.53;	    D2[10][45]=9.25;	D2[11][45]=11.10;	D2[12][45]=13.46;	D2[13][45]=15.66;	D2[14][45]=17.99;	D2[15][45]=21.00;	D2[16][45]=24.33;	D2[17][45]=27.74;	D2[18][45]=0.00;	D2[19][45]=0.00;	D2[20][45]=0.00;	D2[21][45]=0.00;	D2[22][45]=0.00;	D2[23][45]=0.00;
        D2[6][46]=3.24;	D2[7][46]=4.38;	D2[8][46]=5.61;	D2[9][46]=7.20;	    D2[10][46]=8.86;	D2[11][46]=10.61;	D2[12][46]=12.82;	D2[13][46]=14.86;	D2[14][46]=17.10;	D2[15][46]=19.90;	D2[16][46]=22.99;	D2[17][46]=26.18;	D2[18][46]=29.74;	D2[19][46]=0.00;	D2[20][46]=0.00;	D2[21][46]=0.00;	D2[22][46]=0.00;	D2[23][46]=0.00;
        D2[6][47]=3.13;	D2[7][47]=4.21;	D2[8][47]=5.39;	D2[9][47]=6.90;	    D2[10][47]=8.49;	D2[11][47]=10.17;	D2[12][47]=12.29;	D2[13][47]=14.28;	D2[14][47]=16.33;	D2[15][47]=18.96;	D2[16][47]=21.86;	D2[17][47]=24.84;	D2[18][47]=28.18;	D2[19][47]=0.00;	D2[20][47]=0.00;	D2[21][47]=0.00;	D2[22][47]=0.00;	D2[23][47]=0.00;
        D2[6][48]=3.01;	D2[7][48]=4.06;	D2[8][48]=5.19;	D2[9][48]=6.63;	    D2[10][48]=8.16;	D2[11][48]=9.78;	D2[12][48]=11.79;	D2[13][48]=13.73;	D2[14][48]=15.65;	D2[15][48]=18.15;	D2[16][48]=20.84;	D2[17][48]=23.64;	D2[18][48]=26.82;	D2[19][48]=29.92;	D2[20][48]=0.00;	D2[21][48]=0.00;	D2[22][48]=0.00;	D2[23][48]=0.00;
        D2[7][49]=3.93;	D2[8][49]=5.02;	D2[9][49]=6.40;	    D2[10][49]=7.86;	D2[11][49]=9.42;	D2[12][49]=11.38;	D2[13][49]=13.22;	D2[14][49]=15.02;	D2[15][49]=17.40;	D2[16][49]=19.91;	D2[17][49]=22.60;	D2[18][49]=25.63;	D2[19][49]=28.64;	D2[20][49]=0.00;	D2[21][49]=0.00;	D2[22][49]=0.00;	D2[23][49]=0.00;
        D2[7][50]=3.81;	D2[8][50]=4.85;	D2[9][50]=6.18;	    D2[10][50]=7.61;	D2[11][50]=9.09;	D2[12][50]=10.98;	D2[13][50]=12.75;	D2[14][50]=14.49;	D2[15][50]=16.65;	D2[16][50]=19.16;	D2[17][50]=21.69;	D2[18][50]=24.53;	D2[19][50]=27.43;	D2[20][50]=0.00;	D2[21][50]=0.00;	D2[22][50]=0.00;	D2[23][50]=0.00;
        D2[7][51]=3.70;	D2[8][51]=4.71;	D2[9][51]=5.99;	    D2[10][51]=7.34;	D2[11][51]=8.79;	D2[12][51]=10.60;	D2[13][51]=12.31;	D2[14][51]=13.97;	D2[15][51]=16.00;	D2[16][51]=18.41;	D2[17][51]=20.83;	D2[18][51]=23.55;	D2[19][51]=26.38;	D2[20][51]=29.29;	D2[21][51]=0.00;	D2[22][51]=0.00;	D2[23][51]=0.00;
        D2[7][52]=3.60;	D2[8][52]=4.58;	D2[9][52]=5.81;	    D2[10][52]=7.12;	D2[11][52]=8.49;	D2[12][52]=10.28;	D2[13][52]=11.92;	D2[14][52]=13.50;	D2[15][52]=15.47;	D2[16][52]=17.71;	D2[17][52]=20.07;	D2[18][52]=22.67;	D2[19][52]=25.39;	D2[20][52]=28.20;	D2[21][52]=0.00;	D2[22][52]=0.00;	D2[23][52]=0.00;
        D2[7][53]=3.51;	D2[8][53]=4.46;	D2[9][53]=5.65;	    D2[10][53]=6.92;	D2[11][53]=8.25;	D2[12][53]=9.95;	D2[13][53]=11.56;	D2[14][53]=13.07;	D2[15][53]=14.96;	D2[16][53]=17.12;	D2[17][53]=19.33;	D2[18][53]=21.87;	D2[19][53]=24.48;	D2[20][53]=27.20;	D2[21][53]=29.91;	D2[22][53]=0.00;	D2[23][53]=0.00;
        D2[7][54]=3.42;	D2[8][54]=4.34;	D2[9][54]=5.49;	    D2[10][54]=6.73;	D2[11][54]=8.00;	D2[12][54]=9.66;	D2[13][54]=11.22;	D2[14][54]=12.71;	D2[15][54]=14.55;	D2[16][54]=16.57;	D2[17][54]=18.76;	D2[18][54]=21.13;	D2[19][54]=23.59;	D2[20][54]=26.31;	D2[21][54]=29.00;	D2[22][54]=0.00;	D2[23][54]=0.00;
        D2[7][55]=3.34;	D2[8][55]=4.24;	D2[9][55]=5.34;	    D2[10][55]=6.55;	D2[11][55]=7.79;	D2[12][55]=9.39;	D2[13][55]=10.92;	D2[14][55]=12.35;	D2[15][55]=14.12;	D2[16][55]=16.08;	D2[17][55]=18.17;	D2[18][55]=20.43;	D2[19][55]=22.82;	D2[20][55]=25.42;	D2[21][55]=28.11;	D2[22][55]=0.00;	D2[23][55]=0.00;
        D2[7][56]=3.26;	D2[8][56]=4.14;	D2[9][56]=5.22;	    D2[10][56]=6.38;	D2[11][56]=7.58;	D2[12][56]=9.15;	D2[13][56]=10.61;	D2[14][56]=12.01;	D2[15][56]=13.72;	D2[16][56]=15.63;	D2[17][56]=17.62;	D2[18][56]=19.83;	D2[19][56]=22.10;	D2[20][56]=24.66;	D2[21][56]=27.17;	D2[22][56]=0.00;	D2[23][56]=0.00;
        D2[7][57]=3.18;	D2[8][57]=4.05;	D2[9][57]=5.09;	    D2[10][57]=6.23;	D2[11][57]=7.39;	D2[12][57]=8.91;	D2[13][57]=10.33;	D2[14][57]=11.71;	D2[15][57]=13.32;	D2[16][57]=15.21;	D2[17][57]=17.13;	D2[18][57]=19.22;	D2[19][57]=21.35;	D2[20][57]=23.89;	D2[21][57]=26.32;	D2[22][57]=0.00;	D2[23][57]=0.00;
        D2[7][58]=3.11;	D2[8][58]=3.96;	D2[9][58]=4.97;	    D2[10][58]=6.09;	D2[11][58]=7.21;	D2[12][58]=8.71;	D2[13][58]=10.05;	D2[14][58]=11.43;	D2[15][58]=13.01;	D2[16][58]=14.82;	D2[17][58]=16.67;	D2[18][58]=18.73;	D2[19][58]=20.73;	D2[20][58]=23.20;	D2[21][58]=25.57;	D2[22][58]=0.00;	D2[23][58]=0.00;
        D2[7][59]=3.05;	D2[8][59]=3.88;	D2[9][59]=4.87;	    D2[10][59]=5.95;	D2[11][59]=7.04;	D2[12][59]=8.50;	D2[13][59]=9.81;	D2[14][59]=11.15;	D2[15][59]=12.69;	D2[16][59]=14.43;	D2[17][59]=16.21;	D2[18][59]=18.24;	D2[19][59]=20.14;	D2[20][59]=22.54;	D2[21][59]=24.87;	D2[22][59]=0.00;	D2[23][59]=0.00;
        D2[8][60]=3.81;	D2[9][60]=4.77;	    D2[10][60]=5.81;	D2[11][60]=6.89;	D2[12][60]=8.31;	D2[13][60]=9.57;	D2[14][60]=10.89;	D2[15][60]=12.39;	D2[16][60]=14.08;	D2[17][60]=15.83;	D2[18][60]=17.77;	D2[19][60]=19.67;	D2[20][60]=21.90;	D2[21][60]=24.26;	D2[22][60]=0.00;	D2[23][60]=0.00;
        D2[8][61]=3.74;	D2[9][61]=4.67;	    D2[10][61]=5.70;	D2[11][61]=6.75;	D2[12][61]=8.13;	D2[13][61]=9.38;	D2[14][61]=10.64;	D2[15][61]=12.12;	D2[16][61]=13.73;	D2[17][61]=15.46;	D2[18][61]=17.35;	D2[19][61]=19.19;	D2[20][61]=21.32;	D2[21][61]=23.58;	D2[22][61]=29.40;	D2[23][61]=0.00;
        D2[8][62]=3.67;	D2[9][62]=4.58;	    D2[10][62]=5.59;	D2[11][62]=6.61;	D2[12][62]=7.97;	D2[13][62]=9.17;	D2[14][62]=10.39;	D2[15][62]=11.87;	D2[16][62]=13.43;	D2[17][62]=15.09;	D2[18][62]=16.95;	D2[19][62]=18.72;	D2[20][62]=20.79;	D2[21][62]=22.90;	D2[22][62]=28.70;	D2[23][62]=0.00;
        D2[8][63]=3.61;	D2[9][63]=4.49;	    D2[10][63]=5.48;	D2[11][63]=6.48;	D2[12][63]=7.81;	D2[13][63]=8.97;	D2[14][63]=10.17;	D2[15][63]=11.62;	D2[16][63]=13.13;	D2[17][63]=14.76;	D2[18][63]=16.56;	D2[19][63]=18.28;	D2[20][63]=20.36;	D2[21][63]=22.29;	D2[22][63]=28.02;	D2[23][63]=0.00;
        D2[8][64]=3.54;	D2[9][64]=4.41;	    D2[10][64]=5.37;	D2[11][64]=6.37;	D2[12][64]=7.66;	D2[13][64]=8.80;	D2[14][64]=9.98;	D2[15][64]=11.39;	D2[16][64]=12.85;	D2[17][64]=14.46;	D2[18][64]=16.19;	D2[19][64]=17.88;	D2[20][64]=19.90;	D2[21][64]=21.75;	D2[22][64]=27.40;	D2[23][64]=0.00;
        D2[8][65]=3.49;	D2[9][65]=4.34;	    D2[10][65]=5.28;	D2[11][65]=6.25;	D2[12][65]=7.51;	D2[13][65]=8.63;	D2[14][65]=9.77;	D2[15][65]=11.16;	D2[16][65]=12.60;	D2[17][65]=14.16;	D2[18][65]=15.83;	D2[19][65]=17.53;	D2[20][65]=19.47;	D2[21][65]=21.28;	D2[22][65]=26.80;	D2[23][65]=0.00;
        D2[8][66]=3.43;	D2[9][66]=4.26; 	D2[10][66]=5.19;	D2[11][66]=6.13;	D2[12][66]=7.38;	D2[13][66]=8.48;	D2[14][66]=9.61;	D2[15][66]=10.97;	D2[16][66]=12.36;	D2[17][66]=13.90;	D2[18][66]=15.51;	D2[19][66]=17.18;	D2[20][66]=19.06;	D2[21][66]=20.83;	D2[22][66]=26.19;	D2[23][66]=0.00;
        D2[8][67]=3.39;	D2[9][67]=4.19;	    D2[10][67]=5.10;	D2[11][67]=6.03;	D2[12][67]=7.25;	D2[13][67]=8.33;	D2[14][67]=9.43;	D2[15][67]=10.76;	D2[16][67]=12.13;	D2[17][67]=13.62;	D2[18][67]=15.22;	D2[19][67]=16.84;	D2[20][67]=18.69;	D2[21][67]=20.43;	D2[22][67]=25.63;	D2[23][67]=0.00;
        D2[8][68]=3.34;	D2[9][68]=4.13;	    D2[10][68]=5.02;	D2[11][68]=5.93;	D2[12][68]=7.13;	D2[13][68]=8.19;	D2[14][68]=9.28;	D2[15][68]=10.58;	D2[16][68]=11.91;	D2[17][68]=13.36;	D2[18][68]=14.94;	D2[19][68]=16.51;	D2[20][68]=18.33;	D2[21][68]=20.01;	D2[22][68]=25.13;	D2[23][68]=29.89;
        D2[8][69]=3.29;	D2[9][69]=4.07;	    D2[10][69]=4.94;	D2[11][69]=5.84;	D2[12][69]=7.01;	D2[13][69]=8.04;	D2[14][69]=9.10;	D2[15][69]=10.41;	D2[16][69]=11.69;	D2[17][69]=13.15;	D2[18][69]=14.69;	D2[19][69]=16.23;	D2[20][69]=18.01;	D2[21][69]=19.64;	D2[22][69]=24.60;	D2[23][69]=29.34;
        D2[8][70]=3.24;	D2[9][70]=4.01;	    D2[10][70]=4.87;	D2[11][70]=5.75;	D2[12][70]=6.89;	D2[13][70]=7.92;	D2[14][70]=8.95;	D2[15][70]=10.21;	D2[16][70]=11.49;	D2[17][70]=12.90;	D2[18][70]=14.44;	D2[19][70]=15.91;	D2[20][70]=17.68;	D2[21][70]=19.28;	D2[22][70]=24.17;	D2[23][70]=28.74;
        D2[8][71]=3.20;	D2[9][71]=3.95;	    D2[10][71]=4.79;	D2[11][71]=5.66;	D2[12][71]=6.80;	D2[13][71]=7.81;	D2[14][71]=8.81;	D2[15][71]=10.06;	D2[16][71]=11.30;	D2[17][71]=12.68;	D2[18][71]=14.16;	D2[19][71]=15.59;	D2[20][71]=17.38;	D2[21][71]=18.94;	D2[22][71]=23.73;	D2[23][71]=28.16;
        D2[8][72]=3.16;	D2[9][72]=3.90;	    D2[10][72]=4.72;	D2[11][72]=5.58;	D2[12][72]=6.69;	D2[13][72]=7.68;	D2[14][72]=8.67;	D2[15][72]=9.91;	D2[16][72]=11.12;	D2[17][72]=12.49;	D2[18][72]=13.92;	D2[19][72]=15.35;	D2[20][72]=17.07;	D2[21][72]=18.60;	D2[22][72]=23.28;	D2[23][72]=27.60;
        D2[8][73]=3.12;	D2[9][73]=3.85;	    D2[10][73]=4.65;	D2[11][73]=5.51;	D2[12][73]=6.60;	D2[13][73]=7.56;	D2[14][73]=8.55;	D2[15][73]=9.76;	D2[16][73]=10.95;	D2[17][73]=12.29;	D2[18][73]=13.69;	D2[19][73]=15.13;	D2[20][73]=16.80;	D2[21][73]=18.31;	D2[22][73]=22.88;	D2[23][73]=27.04;
        D2[8][74]=3.08;	D2[9][74]=3.80;	    D2[10][74]=4.59;	D2[11][74]=5.43;	D2[12][74]=6.51;	D2[13][74]=7.47;	D2[14][74]=8.41;	D2[15][74]=9.63;	D2[16][74]=10.76;	D2[17][74]=12.08;	D2[18][74]=13.47;	D2[19][74]=14.89;	D2[20][74]=16.53;	D2[21][74]=18.02;	D2[22][74]=22.49;	D2[23][74]=26.55;
        D2[8][75]=3.04;	D2[9][75]=3.75;	    D2[10][75]=4.53;	D2[11][75]=5.36;	D2[12][75]=6.43;	D2[13][75]=7.37;	D2[14][75]=8.30;	D2[15][75]=9.51;	D2[16][75]=10.61;	D2[17][75]=11.91;	D2[18][75]=13.25;	D2[19][75]=14.70;	D2[20][75]=16.24;	D2[21][75]=17.72;	D2[22][75]=22.12;	D2[23][75]=26.16;
        D2[8][76]=3.00;	D2[9][76]=3.70;	    D2[10][76]=4.48;	D2[11][76]=5.28;	D2[12][76]=6.34;	D2[13][76]=7.26;	D2[14][76]=8.20;	D2[15][76]=9.38;	D2[16][76]=10.45;	D2[17][76]=11.74;	D2[18][76]=13.06;	D2[19][76]=14.48;	D2[20][76]=16.04;	D2[21][76]=17.46;	D2[22][76]=21.79;	D2[23][76]=25.75;
        D2[9][77]=3.66;	    D2[10][77]=4.42;	D2[11][77]=5.22;	D2[12][77]=6.26;	D2[13][77]=7.17;	D2[14][77]=8.09;	D2[15][77]=9.26;	D2[16][77]=10.30;	D2[17][77]=11.57;	D2[18][77]=12.90;	D2[19][77]=14.27;	D2[20][77]=15.79;	D2[21][77]=17.16;	D2[22][77]=21.43;	D2[23][77]=25.36;
        D2[9][78]=3.62;	    D2[10][78]=4.37;	D2[11][78]=5.15;	D2[12][78]=6.18;	D2[13][78]=7.07;	D2[14][78]=8.01;	D2[15][78]=9.16;	D2[16][78]=10.18;	D2[17][78]=11.41;	D2[18][78]=12.70;	D2[19][78]=14.09;	D2[20][78]=15.61;	D2[21][78]=16.90;	D2[22][78]=21.11;	D2[23][78]=24.96;
        D2[10][79]=4.34;	D2[11][79]=5.10;	D2[12][79]=6.11;	D2[13][79]=6.99;	D2[14][79]=7.93;	D2[15][79]=9.05;	D2[16][79]=10.07;	D2[17][79]=11.28;	D2[18][79]=12.57;	D2[19][79]=13.93;	D2[20][79]=15.42;	D2[21][79]=16.71;	D2[22][79]=20.85;	D2[23][79]=24.68;


    }

    public void valores_7(){ //lado horizontal de la grafica de ducto rectangular

        D3[0][0]=4.31;	D3[1][0]=6.96;	D3[2][0]=10.68;	D3[3][0]=15.85;	D3[4][0]=21.49;	D3[5][0]=29.04;	D3[6][0]=38.26;	D3[7][0]=61.50;	D3[8][0]=94.27;	D3[9][0]=0.00;	D3[10][0]=0.00;	    D3[11][0]=0.00;	    D3[12][0]=0.00;	    D3[13][0]=0.00;	    D3[14][0]=0.00;	    D3[15][0]=0.00;	    D3[16][0]=0.00;	    D3[17][0]=0.00;	    D3[18][0]=0.00;	    D3[19][0]=0.00;	    D3[20][0]=0.00;	    D3[21][0]=0.00;	    D3[22][0]=0.00;	    D3[23][0]=0.00;
        D3[0][1]=3.66;	D3[1][1]=5.86;	D3[2][1]=8.82;	D3[3][1]=12.73;	D3[4][1]=17.25;	D3[5][1]=23.10;	D3[6][1]=30.30;	D3[7][1]=48.25;	D3[8][1]=71.61;	D3[9][1]=0.00;	D3[10][1]=0.00;	    D3[11][1]=0.00;	    D3[12][1]=0.00;	    D3[13][1]=0.00;	    D3[14][1]=0.00;	    D3[15][1]=0.00;	    D3[16][1]=0.00;	    D3[17][1]=0.00;	    D3[18][1]=0.00;	    D3[19][1]=0.00;	    D3[20][1]=0.00;	    D3[21][1]=0.00;	    D3[22][1]=0.00;	    D3[23][1]=0.00;
        D3[0][2]=3.20;	D3[1][2]=5.09;	D3[2][2]=7.55;	D3[3][2]=10.66;	D3[4][2]=14.44;	D3[5][2]=19.11;	D3[6][2]=24.83;	D3[7][2]=38.97;	D3[8][2]=57.22;	D3[9][2]=82.35;	D3[10][2]=0.00;	    D3[11][2]=0.00;	    D3[12][2]=0.00;	    D3[13][2]=0.00;	    D3[14][2]=0.00;	    D3[15][2]=0.00;	    D3[16][2]=0.00;	    D3[17][2]=0.00;	    D3[18][2]=0.00;	    D3[19][2]=0.00;	    D3[20][2]=0.00;	    D3[21][2]=0.00;	    D3[22][2]=0.00;	    D3[23][2]=0.00;
        D3[1][3]=4.53;	D3[2][3]=6.65;	D3[3][3]=9.26;	D3[4][3]=12.43;	D3[5][3]=16.28;	D3[6][3]=21.02;	D3[7][3]=32.74;	D3[8][3]=47.28;	D3[9][3]=67.88;	D3[10][3]=93.33;	D3[11][3]=0.00;	    D3[12][3]=0.00;	    D3[13][3]=0.00;	    D3[14][3]=0.00;	    D3[15][3]=0.00;	    D3[16][3]=0.00;	    D3[17][3]=0.00;	    D3[18][3]=0.00;	    D3[19][3]=0.00;	    D3[20][3]=0.00;	    D3[21][3]=0.00;	    D3[22][3]=0.00;	    D3[23][3]=0.00;
        D3[1][4]=4.10;	D3[2][4]=5.95;	D3[3][4]=8.20;	D3[4][4]=10.87;	D3[5][4]=14.26;	D3[6][4]=18.22;	D3[7][4]=28.10;	D3[8][4]=40.18;	D3[9][4]=57.53;	D3[10][4]=78.71;	D3[11][4]=99.69;	D3[12][4]=0.00;	    D3[13][4]=0.00; 	D3[14][4]=0.00;	    D3[15][4]=0.00; 	D3[16][4]=0.00;	    D3[17][4]=0.00;	    D3[18][4]=0.00;	    D3[19][4]=0.00;	    D3[20][4]=0.00;	    D3[21][4]=0.00;	    D3[22][4]=0.00;	    D3[23][4]=0.00;
        D3[1][5]=3.74;	D3[2][5]=5.42;	D3[3][5]=7.42;	D3[4][5]=9.70;	D3[5][5]=12.68;	D3[6][5]=16.10;	D3[7][5]=24.68;	D3[8][5]=34.93;	D3[9][5]=49.87;	D3[10][5]=67.74;	D3[11][5]=88.28;	D3[12][5]=0.00;	    D3[13][5]=0.00; 	D3[14][5]=0.00;	    D3[15][5]=0.00;	    D3[16][5]=0.00;	    D3[17][5]=0.00;	    D3[18][5]=0.00;	    D3[19][5]=0.00;	    D3[20][5]=0.00;	    D3[21][5]=0.00;	    D3[22][5]=0.00;	    D3[23][5]=0.00;
        D3[1][6]=3.47;	D3[2][6]=5.00;	D3[3][6]=6.77;	D3[4][6]=8.78;	D3[5][6]=11.41;	D3[6][6]=14.42;	D3[7][6]=21.97;	D3[8][6]=30.98;	D3[9][6]=43.86;	D3[10][6]=59.31;	D3[11][6]=76.61;	D3[12][6]=0.00;	    D3[13][6]=0.00;	    D3[14][6]=0.00;	    D3[15][6]=0.00;	    D3[16][6]=0.00;	    D3[17][6]=0.00;	    D3[18][6]=0.00;	    D3[19][6]=0.00;	    D3[20][6]=0.00;	    D3[21][6]=0.00;	    D3[22][6]=0.00;	    D3[23][6]=0.00;
        D3[1][7]=3.25;	D3[2][7]=4.66;	D3[3][7]=6.26;	D3[4][7]=8.00;	D3[5][7]=10.38;	D3[6][7]=13.11;	D3[7][7]=19.80;	D3[8][7]=27.93;	D3[9][7]=39.14;	D3[10][7]=52.48;	D3[11][7]=67.86;	D3[12][7]=90.33;	D3[13][7]=0.00;	    D3[14][7]=0.00;	    D3[15][7]=0.00;	    D3[16][7]=0.00;	    D3[17][7]=0.00;	    D3[18][7]=0.00;	    D3[19][7]=0.00;	    D3[20][7]=0.00;	    D3[21][7]=0.00;	    D3[22][7]=0.00;	    D3[23][7]=0.00;
        D3[1][8]=3.06;	D3[2][8]=4.36;	D3[3][8]=5.82;	D3[4][8]=7.43;	D3[5][8]=9.55;	D3[6][8]=11.95;	D3[7][8]=18.10;	D3[8][8]=25.41;	D3[9][8]=35.32;	D3[10][8]=47.18;	D3[11][8]=60.64;	D3[12][8]=80.31;	D3[13][8]=99.55;	D3[14][8]=0.00; 	D3[15][8]=0.00;	    D3[16][8]=0.00;	    D3[17][8]=0.00;	    D3[18][8]=0.00;	    D3[19][8]=0.00;	    D3[20][8]=0.00;	    D3[21][8]=0.00;	    D3[22][8]=0.00;	    D3[23][8]=0.00;
        D3[2][9]=4.12;	D3[3][9]=5.46;	D3[4][9]=6.94;	D3[5][9]=8.86;	D3[6][9]=10.96;	D3[7][9]=16.63;	D3[8][9]=23.21;	D3[9][9]=32.18;	D3[10][9]=42.81;	D3[11][9]=54.90;	D3[12][9]=72.33;	D3[13][9]=89.31;	D3[14][9]=0.00;	    D3[15][9]=0.00;	    D3[16][9]=0.00;	    D3[17][9]=0.00;	    D3[18][9]=0.00;	    D3[19][9]=0.00;	    D3[20][9]=0.00;	    D3[21][9]=0.00;	    D3[22][9]=0.00;	    D3[23][9]=0.00;
        D3[2][10]=3.90;	D3[3][10]=5.16;	D3[4][10]=6.51;	D3[5][10]=8.29;	D3[6][10]=10.24;D3[7][10]=15.36;D3[8][10]=21.55;D3[9][10]=29.41;D3[10][10]=39.06;	D3[11][10]=50.05;	D3[12][10]=65.54;	D3[13][10]=80.62;	D3[14][10]=98.15;	D3[15][10]=0.00;	D3[16][10]=0.00;	D3[17][10]=0.00;	D3[18][10]=0.00;	D3[19][10]=0.00;	D3[20][10]=0.00;	D3[21][10]=0.00;	D3[22][10]=0.00;	D3[23][10]=0.00;
        D3[2][11]=3.71;	D3[3][11]=4.90;	D3[4][11]=6.16;	D3[5][11]=7.82;	D3[6][11]=9.63;	D3[7][11]=14.29;D3[8][11]=20.07;D3[9][11]=26.99;D3[10][11]=36.00;	D3[11][11]=46.08;	D3[12][11]=59.95;	D3[13][11]=73.61;	D3[14][11]=88.55;	D3[15][11]=0.00;	D3[16][11]=0.00;	D3[17][11]=0.00;	D3[18][11]=0.00;	D3[19][11]=0.00;	D3[20][11]=0.00;	D3[21][11]=0.00;	D3[22][11]=0.00;	D3[23][11]=0.00;
        D3[2][12]=3.55;	D3[3][12]=4.67;	D3[4][12]=5.85;	D3[5][12]=7.43;	D3[6][12]=9.11;	D3[7][12]=13.39;D3[8][12]=18.71;D3[9][12]=25.06;D3[10][12]=33.31;	D3[11][12]=42.54;	D3[12][12]=55.30;	D3[13][12]=67.81;	D3[14][12]=81.36;	D3[15][12]=99.84;	D3[16][12]=0.00;	D3[17][12]=0.00;	D3[18][12]=0.00;	D3[19][12]=0.00;	D3[20][12]=0.00;	D3[21][12]=0.00;	D3[22][12]=0.00;	D3[23][12]=0.00;
        D3[2][13]=3.41;	D3[3][13]=4.46;	D3[4][13]=5.58;	D3[5][13]=7.08;	D3[6][13]=8.62;	D3[7][13]=12.63;D3[8][13]=17.46;D3[9][13]=23.32;D3[10][13]=30.96;	D3[11][13]=39.56;	D3[12][13]=51.20;	D3[13][13]=62.68;	D3[14][13]=75.21;	D3[15][13]=92.18;	D3[16][13]=0.00;	D3[17][13]=0.00;	D3[18][13]=0.00;	D3[19][13]=0.00;	D3[20][13]=0.00;	D3[21][13]=0.00;	D3[22][13]=0.00;	D3[23][13]=0.00;
        D3[2][14]=3.28;	D3[3][14]=4.29;	D3[4][14]=5.33;	D3[5][14]=6.77;	D3[6][14]=8.20;	D3[7][14]=11.98;D3[8][14]=16.44;D3[9][14]=21.87;D3[10][14]=28.86;	D3[11][14]=36.85;	D3[12][14]=47.67;	D3[13][14]=58.38;	D3[14][14]=69.74;	D3[15][14]=84.63;	D3[16][14]=99.66;	D3[17][14]=0.00;	D3[18][14]=0.00;	D3[19][14]=0.00;	D3[20][14]=0.00;	D3[21][14]=0.00;	D3[22][14]=0.00;	D3[23][14]=0.00;
        D3[2][15]=3.17;	D3[3][15]=4.13;	D3[4][15]=5.13;	D3[5][15]=6.48;	D3[6][15]=7.81;	D3[7][15]=11.39;D3[8][15]=15.65;D3[9][15]=20.54;D3[10][15]=27.06;	D3[11][15]=34.45;	D3[12][15]=44.58;	D3[13][15]=54.71;	D3[14][15]=64.98;	D3[15][15]=78.83;	D3[16][15]=93.13;	D3[17][15]=0.00;	D3[18][15]=0.00;	D3[19][15]=0.00;	D3[20][15]=0.00;	D3[21][15]=0.00;	D3[22][15]=0.00;	D3[23][15]=0.00;
        D3[2][16]=3.07;	D3[3][16]=3.98;	D3[4][16]=4.94;	D3[5][16]=6.22;	D3[6][16]=7.49;	D3[7][16]=10.88;D3[8][16]=14.96;D3[9][16]=19.42;D3[10][16]=25.49;	D3[11][16]=32.32;	D3[12][16]=41.87;	D3[13][16]=51.52;	D3[14][16]=61.15;	D3[15][16]=73.73;	D3[16][16]=87.48;	D3[17][16]=0.00;	D3[18][16]=0.00;	D3[19][16]=0.00;	D3[20][16]=0.00;	D3[21][16]=0.00;	D3[22][16]=0.00;	D3[23][16]=0.00;
        D3[3][17]=3.85;	D3[4][17]=4.77;	D3[5][17]=5.99;	D3[6][17]=7.21;	D3[7][17]=10.43;D3[8][17]=14.30;D3[9][17]=18.49;D3[10][17]=24.10;	D3[11][17]=30.49;	D3[12][17]=39.36;	D3[13][17]=48.46;	D3[14][17]=57.41;	D3[15][17]=69.00;	D3[16][17]=82.07;	D3[17][17]=97.06;	D3[18][17]=0.00;	D3[19][17]=0.00;	D3[20][17]=0.00;	D3[21][17]=0.00;	D3[22][17]=0.00;	D3[23][17]=0.00;
        D3[3][18]=3.74;	D3[4][18]=4.62;	D3[5][18]=5.78;	D3[6][18]=6.93;	D3[7][18]=10.03;D3[8][18]=13.66;D3[9][18]=17.70;D3[10][18]=22.98;	D3[11][18]=28.87;	D3[12][18]=37.20;	D3[13][18]=45.67;	D3[14][18]=54.04;	D3[15][18]=64.80;	D3[16][18]=77.05;	D3[17][18]=91.04;	D3[18][18]=0.00;	D3[19][18]=0.00;	D3[20][18]=0.00;	D3[21][18]=0.00;	D3[22][18]=0.00;	D3[23][18]=0.00;
        D3[3][19]=3.62;	D3[4][19]=4.48;	D3[5][19]=5.58;	D3[6][19]=6.70;	D3[7][19]=9.63;	D3[8][19]=13.12;D3[9][19]=17.00;D3[10][19]=21.93;	D3[11][19]=27.42;	D3[12][19]=35.25;	D3[13][19]=43.09;	D3[14][19]=51.17;	D3[15][19]=61.32;	D3[16][19]=72.84;	D3[17][19]=85.91;	D3[18][19]=0.00;	D3[19][19]=0.00;	D3[20][19]=0.00;	D3[21][19]=0.00;	D3[22][19]=0.00;	D3[23][19]=0.00;
        D3[3][20]=3.52;	D3[4][20]=4.34;	D3[5][20]=5.41;	D3[6][20]=6.48;	D3[7][20]=9.28;	D3[8][20]=12.57;D3[9][20]=16.39;D3[10][20]=21.00;	D3[11][20]=26.28;	D3[12][20]=33.45;	D3[13][20]=40.82;	D3[14][20]=48.44;	D3[15][20]=58.08;	D3[16][20]=68.98;	D3[17][20]=81.08;	D3[18][20]=94.80;	D3[19][20]=0.00;	D3[20][20]=0.00;	D3[21][20]=0.00;	D3[22][20]=0.00;	D3[23][20]=0.00;
        D3[3][21]=3.43;	D3[4][21]=4.23;	D3[5][21]=5.24;	D3[6][21]=6.29;	D3[7][21]=8.97;	D3[8][21]=12.10;D3[9][21]=15.78;D3[10][21]=20.22;	D3[11][21]=25.13;	D3[12][21]=31.93;	D3[13][21]=38.85;	D3[14][21]=46.04;	D3[15][21]=55.07;	D3[16][21]=65.39;	D3[17][21]=76.97;	D3[18][21]=89.69;	D3[19][21]=0.00;	D3[20][21]=0.00;	D3[21][21]=0.00;	D3[22][21]=0.00;	D3[23][21]=0.00;
        D3[3][22]=3.35;	D3[4][22]=4.11;	D3[5][22]=5.09;	D3[6][22]=6.10;	D3[7][22]=8.67;	D3[8][22]=11.68;D3[9][22]=15.25;D3[10][22]=19.47;	D3[11][22]=24.19;	D3[12][22]=30.56;	D3[13][22]=36.91;	D3[14][22]=43.86;	D3[15][22]=52.55;	D3[16][22]=62.43;	D3[17][22]=73.05;	D3[18][22]=85.48;	D3[19][22]=99.09;	D3[20][22]=0.00;	D3[21][22]=0.00;	D3[22][22]=0.00;	D3[23][22]=0.00;
        D3[3][23]=3.27;	D3[4][23]=4.01;	D3[5][23]=4.96;	D3[6][23]=5.92;	D3[7][23]=8.39;	D3[8][23]=11.32;D3[9][23]=14.76;D3[10][23]=18.75;	D3[11][23]=23.24;	D3[12][23]=29.35;	D3[13][23]=35.06;	D3[14][23]=41.93;	D3[15][23]=50.21;	D3[16][23]=59.63;	D3[17][23]=69.60;	D3[18][23]=81.39;	D3[19][23]=93.81;	D3[20][23]=0.00;	D3[21][23]=0.00;	D3[22][23]=0.00;	D3[23][23]=0.00;
        D3[3][24]=3.20;	D3[4][24]=3.90;	D3[5][24]=4.83;	D3[6][24]=5.76;	D3[7][24]=8.16;	D3[8][24]=10.98;D3[9][24]=14.25;D3[10][24]=18.11;	D3[11][24]=22.37;	D3[12][24]=28.23;	D3[13][24]=33.67;	D3[14][24]=40.05;	D3[15][24]=47.92;	D3[16][24]=57.12;	D3[17][24]=66.51;	D3[18][24]=77.56;	D3[19][24]=89.22;	D3[20][24]=0.00;	D3[21][24]=0.00;	D3[22][24]=0.00;	D3[23][24]=0.00;
        D3[3][25]=3.13;	D3[4][25]=3.81;	D3[5][25]=4.71;	D3[6][25]=5.60;	D3[7][25]=7.93;	D3[8][25]=10.64;D3[9][25]=13.82;D3[10][25]=17.59;	D3[11][25]=21.60;	D3[12][25]=27.21;	D3[13][25]=32.35;	D3[14][25]=38.46;	D3[15][25]=45.81;	D3[16][25]=54.54;	D3[17][25]=63.79;	D3[18][25]=73.98;	D3[19][25]=85.04;	D3[20][25]=98.96;	D3[21][25]=0.00;	D3[22][25]=0.00;	D3[23][25]=0.00;
        D3[3][26]=3.06;	D3[4][26]=3.73;	D3[5][26]=4.61;	D3[6][26]=5.46;	D3[7][26]=7.71;	D3[8][26]=10.35;D3[9][26]=13.37;D3[10][26]=17.00;	D3[11][26]=20.87;	D3[12][26]=26.26;	D3[13][26]=31.12;	D3[14][26]=36.89;	D3[15][26]=44.01;	D3[16][26]=52.35;	D3[17][26]=61.11;	D3[18][26]=70.99;	D3[19][26]=81.27;	D3[20][26]=94.12;	D3[21][26]=0.00;	D3[22][26]=0.00;	D3[23][26]=0.00;
        D3[4][27]=3.65;	D3[5][27]=4.50;	D3[6][27]=5.33;	D3[7][27]=7.53;	D3[8][27]=10.06;D3[9][27]=13.00;D3[10][27]=16.52;	D3[11][27]=20.24;	D3[12][27]=25.29;	D3[13][27]=29.99;	D3[14][27]=35.49;	D3[15][27]=42.46;	D3[16][27]=50.30;	D3[17][27]=58.69;	D3[18][27]=68.21;	D3[19][27]=78.05;	D3[20][27]=90.19;	D3[21][27]=0.00;	D3[22][27]=0.00;	D3[23][27]=0.00;
        D3[4][28]=3.58;	D3[5][28]=4.40;	D3[6][28]=5.21;	D3[7][28]=7.35;	D3[8][28]=9.79;	D3[9][28]=12.65;D3[10][28]=16.03;	D3[11][28]=19.64;	D3[12][28]=24.42;	D3[13][28]=28.95;	D3[14][28]=34.21;	D3[15][28]=41.08;	D3[16][28]=48.36;	D3[17][28]=56.50;	D3[18][28]=65.76;	D3[19][28]=75.25;	D3[20][28]=86.58;	D3[21][28]=97.25;	D3[22][28]=0.00;	D3[23][28]=0.00;
        D3[4][29]=3.51;	D3[5][29]=4.31;	D3[6][29]=5.08;	D3[7][29]=7.19;	D3[8][29]=9.54;	D3[9][29]=12.32;D3[10][29]=15.60;	D3[11][29]=19.10;	D3[12][29]=23.65;	D3[13][29]=28.01;	D3[14][29]=33.14;	D3[15][29]=39.69;	D3[16][29]=46.62;	D3[17][29]=54.59;	D3[18][29]=63.20;	D3[19][29]=72.17;	D3[20][29]=83.25;	D3[21][29]=93.67;	D3[22][29]=0.00;	D3[23][29]=0.00;
        D3[4][30]=3.44;	D3[5][30]=4.22;	D3[6][30]=4.98;	D3[7][30]=7.03;	D3[8][30]=9.30;	D3[9][30]=12.01;D3[10][30]=15.16;	D3[11][30]=18.58;	D3[12][30]=22.91;	D3[13][30]=27.11;	D3[14][30]=32.03;	D3[15][30]=38.40;	D3[16][30]=45.06;	D3[17][30]=52.56;	D3[18][30]=60.96;	D3[19][30]=69.28;	D3[20][30]=80.01;	D3[21][30]=90.15;	D3[22][30]=0.00;	D3[23][30]=0.00;
        D3[4][31]=3.38;	D3[5][31]=4.14;	D3[6][31]=4.87;	D3[7][31]=6.89;	D3[8][31]=9.08;	D3[9][31]=11.72;D3[10][31]=14.78;	D3[11][31]=18.07;	D3[12][31]=22.26;	D3[13][31]=26.33;	D3[14][31]=30.98;	D3[15][31]=37.17;	D3[16][31]=43.72;	D3[17][31]=50.81;	D3[18][31]=58.81;	D3[19][31]=66.96;	D3[20][31]=76.89;	D3[21][31]=86.87;	D3[22][31]=0.00;	D3[23][31]=0.00;
        D3[4][32]=3.32;	D3[5][32]=4.07;	D3[6][32]=4.77;	D3[7][32]=6.74;	D3[8][32]=8.87;	D3[9][32]=11.46;D3[10][32]=14.42;	D3[11][32]=17.62;	D3[12][32]=21.65;	D3[13][32]=25.56;	D3[14][32]=30.00;	D3[15][32]=35.97;	D3[16][32]=42.35;	D3[17][32]=49.15;	D3[18][32]=56.85;	D3[19][32]=64.82;	D3[20][32]=74.46;	D3[21][32]=83.57;	D3[22][32]=0.00;	D3[23][32]=0.00;
        D3[4][33]=3.27;	D3[5][33]=3.99;	D3[6][33]=4.68;	D3[7][33]=6.62;	D3[8][33]=8.69;	D3[9][33]=11.21;D3[10][33]=14.05;	D3[11][33]=17.22;	D3[12][33]=21.05;	D3[13][33]=24.88;	D3[14][33]=29.17;	D3[15][33]=34.89;	D3[16][33]=41.04;	D3[17][33]=47.50;	D3[18][33]=55.03;	D3[19][33]=62.72;	D3[20][33]=71.99;	D3[21][33]=80.77;	D3[22][33]=0.00;	D3[23][33]=0.00;
        D3[4][34]=3.21;	D3[5][34]=3.92;	D3[6][34]=4.60;	D3[7][34]=6.50;	D3[8][34]=8.51;	D3[9][34]=10.96;D3[10][34]=13.75;	D3[11][34]=16.79;	D3[12][34]=20.53;	D3[13][34]=24.26;	D3[14][34]=28.34;	D3[15][34]=33.86;	D3[16][34]=39.82;	D3[17][34]=46.12;	D3[18][34]=53.27;	D3[19][34]=60.52;	D3[20][34]=69.70;	D3[21][34]=77.96;	D3[22][34]=0.00;	D3[23][34]=0.00;
        D3[4][35]=3.12;	D3[5][35]=3.79;	D3[6][35]=4.45;	D3[7][35]=6.27;	D3[8][35]=8.19;	D3[9][35]=10.52;D3[10][35]=13.17;	D3[11][35]=16.05;	D3[12][35]=19.54;	D3[13][35]=23.03;	D3[14][35]=26.84;	D3[15][35]=32.03;	D3[16][35]=37.63;	D3[17][35]=43.63;	D3[18][35]=50.36;	D3[19][35]=57.10;	D3[20][35]=65.05;	D3[21][35]=73.19;	D3[22][35]=98.79;	D3[23][35]=0.00;
        D3[4][36]=3.03;	D3[5][36]=3.68;	D3[6][36]=4.30;	D3[7][36]=6.07;	D3[8][36]=7.89;	D3[9][36]=10.11;D3[10][36]=12.65;	D3[11][36]=15.39;	D3[12][36]=18.72;	D3[13][36]=21.95;	D3[14][36]=25.52;	D3[15][36]=30.45;	D3[16][36]=35.68;	D3[17][36]=41.27;	D3[18][36]=47.65;	D3[19][36]=54.27;	D3[20][36]=61.64;	D3[21][36]=69.03;	D3[22][36]=92.70;	D3[23][36]=0.00;
        D3[5][37]=3.56;	D3[6][37]=4.17;	D3[7][37]=5.89;	D3[8][37]=7.64;	D3[9][37]=9.76;	D3[10][37]=12.18;	D3[11][37]=14.78;	D3[12][37]=17.98;	D3[13][37]=21.09;	D3[14][37]=24.44;	D3[15][37]=28.96;	D3[16][37]=34.04;	D3[17][37]=39.21;	D3[18][37]=45.15;	D3[19][37]=51.51;	D3[20][37]=58.48;	D3[21][37]=65.67;	D3[22][37]=87.36;	D3[23][37]=0.00;
        D3[5][38]=3.46;	D3[6][38]=4.06;	D3[7][38]=5.73;	D3[8][38]=7.41;	D3[9][38]=9.42;	D3[10][38]=11.76;	D3[11][38]=14.22;	D3[12][38]=17.29;	D3[13][38]=20.27;	D3[14][38]=23.47;	D3[15][38]=27.70;	D3[16][38]=32.54;	D3[17][38]=37.42;	D3[18][38]=42.98;	D3[19][38]=49.01;	D3[20][38]=55.67;	D3[21][38]=62.82;	D3[22][38]=82.74;	D3[23][38]=0.00;
        D3[5][39]=3.37;	D3[6][39]=3.95;	D3[7][39]=5.58;	D3[8][39]=7.20;	D3[9][39]=9.12;	D3[10][39]=11.36;	D3[11][39]=13.75;	D3[12][39]=16.72;	D3[13][39]=19.54;	D3[14][39]=22.54;	D3[15][39]=26.54;	D3[16][39]=31.06;	D3[17][39]=35.67;	D3[18][39]=41.05;	D3[19][39]=46.87;	D3[20][39]=53.11;	D3[21][39]=59.66;	D3[22][39]=78.59;	D3[23][39]=97.80;
        D3[5][40]=3.28;	D3[6][40]=3.86;	D3[7][40]=5.42;	D3[8][40]=7.01;	D3[9][40]=8.85;	D3[10][40]=11.00;	D3[11][40]=13.29;	D3[12][40]=16.16;	D3[13][40]=18.90;	D3[14][40]=21.73;	D3[15][40]=25.55;	D3[16][40]=29.73;	D3[17][40]=34.25;	D3[18][40]=39.36;	D3[19][40]=44.75;	D3[20][40]=50.81;	D3[21][40]=56.79;	D3[22][40]=74.59;	D3[23][40]=92.78;
        D3[5][41]=3.20;	D3[6][41]=3.76;	D3[7][41]=5.30;	D3[8][41]=6.84;	D3[9][41]=8.60;	D3[10][41]=10.70;	D3[11][41]=12.89;	D3[12][41]=15.70;	D3[13][41]=18.29;	D3[14][41]=20.97;	D3[15][41]=24.60;	D3[16][41]=28.52;	D3[17][41]=32.90;	D3[18][41]=37.71;	D3[19][41]=42.78;	D3[20][41]=48.38;	D3[21][41]=54.47;	D3[22][41]=71.30;	D3[23][41]=88.24;
        D3[5][42]=3.12;	D3[6][42]=3.68;	D3[7][42]=5.19;	D3[8][42]=6.66;	D3[9][42]=8.37;	D3[10][42]=10.37;	D3[11][42]=12.53;	D3[12][42]=15.23;	D3[13][42]=17.74;	D3[14][42]=20.29;	D3[15][42]=23.80;	D3[16][42]=27.53;	D3[17][42]=31.67;	D3[18][42]=36.17;	D3[19][42]=40.99;	D3[20][42]=46.35;	D3[21][42]=52.21;	D3[22][42]=68.02;	D3[23][42]=84.58;
        D3[5][43]=3.05;	D3[6][43]=3.61;	D3[7][43]=5.07;	D3[8][43]=6.52;	D3[9][43]=8.16;	D3[10][43]=10.08;	D3[11][43]=12.18;	D3[12][43]=14.81;	D3[13][43]=17.21;	D3[14][43]=19.71;	D3[15][43]=23.03;	D3[16][43]=26.57;	D3[17][43]=30.49;	D3[18][43]=34.95;	D3[19][43]=39.38;	D3[20][43]=44.50;	D3[21][43]=50.03;	D3[22][43]=65.16;	D3[23][43]=81.18;
        D3[6][44]=3.55;	D3[7][44]=4.97;	D3[8][44]=6.37;	D3[9][44]=7.97;	D3[10][44]=9.86;	D3[11][44]=11.92;	D3[12][44]=14.43;	D3[13][44]=16.79;	D3[14][44]=19.28;	D3[15][44]=22.46;	D3[16][44]=25.84;	D3[17][44]=29.62;	D3[18][44]=33.83;	D3[19][44]=38.14;	D3[20][44]=43.01;	D3[21][44]=48.25;	D3[22][44]=62.37;	D3[23][44]=78.10;

    }

    public void valores_8(){
        DIAEQV[0]=1.5;
        DIAEQV[1]=2;
        DIAEQV[2]=3;
        DIAEQV[3]=4;
        DIAEQV[4]=5;
        DIAEQV[5]=6;
        DIAEQV[6]=7;
        DIAEQV[7]=8;
        DIAEQV[8]=9;
        DIAEQV[9]=10;
        DIAEQV[10]=12;
        DIAEQV[11]=14;
        DIAEQV[12]=16;
        DIAEQV[13]=18;
        DIAEQV[14]=20;
        DIAEQV[15]=22;
        DIAEQV[16]=24;
        DIAEQV[17]=26;
        DIAEQV[18]=28;
        DIAEQV[19]=30;
        DIAEQV[20]=32;
        DIAEQV[21]=34;
        DIAEQV[22]=36;
        DIAEQV[23]=38;
        DIAEQV[24]=40;
        DIAEQV[25]=45;
        DIAEQV[26]=50;
        DIAEQV[27]=55;
        DIAEQV[28]=60;
        DIAEQV[29]=70;
        DIAEQV[30]=80;
    }

}
