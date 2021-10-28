package net.refriglobal.hvactools;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity_ppa extends AppCompatActivity {

    ImageButton dimducto, cargatermica, extraccioncampana;
    ImageButton calculorefrigerante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ppa);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dimducto            = findViewById(R.id.dimducto);
        cargatermica        = findViewById(R.id.cargatermica);
        extraccioncampana   = findViewById(R.id.extraccioncampana);
        calculorefrigerante = findViewById(R.id.calculorefrigerante);

        dimducto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(MainActivity_ppa.this, DimDucto_Temporal.class);
                startActivity(intent_1);
            }
        });

        cargatermica.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent_2 = new Intent(MainActivity_ppa.this, CargaTermica.class);
                startActivity(intent_2);
            }
        });

        extraccioncampana.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent_3 = new Intent(MainActivity_ppa.this, ExtraccionCampana.class);
                startActivity(intent_3);
            }
        });

        calculorefrigerante.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent_4 = new Intent(MainActivity_ppa.this, CalculoRefrigerante.class);
                startActivity(intent_4);
            }
        });
    }


    /**Se consulta si se desea salir */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == event.KEYCODE_BACK)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.consultaCerrarApp)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
/*
 * pd: 7/3../0/2/1../5./5./2./0../{c}{h}{i}{d}{c}{h}{i}{d}
 */
