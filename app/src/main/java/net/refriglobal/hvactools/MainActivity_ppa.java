package net.refriglobal.hvactools;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ppa);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
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
                Intent intent_1 = new Intent(MainActivity_ppa.this, DimDucto.class);
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
}
/**
 * pd: 7/3../0/2/1../5./5./2./0../{c}{h}{i}{d}{c}{h}{i}{d}
 */
