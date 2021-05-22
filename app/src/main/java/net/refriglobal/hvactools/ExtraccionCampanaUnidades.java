package net.refriglobal.hvactools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class ExtraccionCampanaUnidades extends AppCompatActivity {

    RadioButton rButtonSI, rButtonEEUU;

    public static char tipoSitema = 'i';


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraccion_campana_unidades);

        rButtonSI = findViewById(R.id.radioButtonSI);
        rButtonEEUU = findViewById(R.id.radioButtonEEUU);
    }


public void guardarSistema(View view)
{
    if (rButtonSI.isChecked() == true)
    {
        tipoSitema = 'i';
    }

    if (rButtonEEUU.isChecked()==true)
    {
        tipoSitema = 'e';
    }
}

}