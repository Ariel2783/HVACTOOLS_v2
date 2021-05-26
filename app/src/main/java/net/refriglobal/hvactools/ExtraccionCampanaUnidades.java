package net.refriglobal.hvactools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ExtraccionCampanaUnidades extends AppCompatActivity
{

    RadioButton rButtonSI, rButtonEEUU;
    Button guardar;
    public static char tipoSitemaUnidad = 'e';


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraccion_campana_unidades);

        rButtonSI = findViewById(R.id.radioButtonSI);
        rButtonEEUU = findViewById(R.id.radioButtonEEUU);
        guardar = findViewById(R.id.buttonGuardar);

        if(tipoSitemaUnidad == 'i')
        {
            rButtonSI.setChecked(true);
            rButtonEEUU.setChecked(false);
        }

        if(tipoSitemaUnidad == 'e')
        {
            rButtonSI.setChecked(false);
            rButtonEEUU.setChecked(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); /** Muestra la flecha de retorno*/

    }

    public void guardarSistemaUnidad(View view)
    {
        if (rButtonSI.isChecked() == true)
        {
            tipoSitemaUnidad = 'i';
        }

        if (rButtonEEUU.isChecked()==true)
        {
            tipoSitemaUnidad = 'e';
        }

        Intent dato01 = new Intent(ExtraccionCampanaUnidades.this, ExtraccionCampana.class);
        startActivity(dato01);
    }

    /**Accion para regresar la pantanlla anterior*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}