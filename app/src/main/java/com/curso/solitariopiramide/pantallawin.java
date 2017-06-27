package com.curso.solitariopiramide;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class pantallawin extends AppCompatActivity {
    private MediaPlayer inicio;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);

        inicio = MediaPlayer.create(this, R.raw.ganar);
        inicio.start();

        final Intent intent1 = new Intent(pantallawin.this, opciones.class);
        final Intent intent2 = new Intent(pantallawin.this, Portada.class);
        final Button botReinicio = (Button) findViewById(R.id.botonp1);
        final Button botSAL = (Button) findViewById(R.id.botonp2);

        botReinicio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(intent1);
            }
        });

        botSAL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(intent2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(pantallawin.this, juego.class);
        finish();
        startActivity(i);
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
