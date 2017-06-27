package com.curso.solitariopiramide;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Portada extends AppCompatActivity {
    boolean flag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portadagrafica);
        musica.playAudio(this,R.raw.musica);
        final Intent intent1 = new Intent(Portada.this, opciones.class);
        final Button botStart = (Button) findViewById(R.id.botonp1);
        final Button botSAL = (Button) findViewById(R.id.botonp2);

        botStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                musica.stopAudio();
                startActivity(intent1);
            }
        });

        botSAL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(Portada.this)
                        .setMessage("¿Seguro que quieres Salir?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Portada.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Salir");
        ab.setMessage("¿Seguro que deseas salir?");
        ab.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myUid());
                musica.stopAudio();
                System.exit(1);
                finish();
            }
        });
        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ab.show();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(!flag) {
            if (musica.mediaPlayer.isPlaying()) {
                musica.mediaPlayer.pause();
            }
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(!musica.mediaPlayer.isPlaying())
            musica.mediaPlayer.start();

    }

}
