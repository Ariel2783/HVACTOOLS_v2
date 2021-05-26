package net.refriglobal.hvactools.Alternos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import net.refriglobal.hvactools.MainActivity_ppa;
import net.refriglobal.hvactools.R;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask tareaInicio = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity_ppa.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tareaInicio, 250);
    }
}