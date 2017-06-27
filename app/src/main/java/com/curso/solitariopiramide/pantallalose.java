package com.curso.solitariopiramide;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class pantallalose extends AppCompatActivity{
    private MediaPlayer inicio;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose);


        inicio = MediaPlayer.create(this, R.raw.perder);
        inicio.start();

        final Intent intent1 = new Intent(pantallalose.this, Portada.class);
        final Intent intent2 = new Intent(pantallalose.this, opciones.class);
        final Button botReinicio = (Button) findViewById(R.id.botonp1);
        final Button botSAL = (Button) findViewById(R.id.botonp3);
        final Button botREB= (Button) findViewById(R.id.botonp2);

        botReinicio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(intent2);
            }
        });
        botREB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(intent2);
            }
        });
        botSAL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                startActivity(intent1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
