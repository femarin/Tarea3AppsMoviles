package com.curso.solitariopiramide;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class opciones extends AppCompatActivity{
    boolean flag = false;
    int secarga;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.des);

        final String archivostr= getFilesDir()+"savegame.txt";
        musica.playAudio(this, R.raw.musica);

        final Intent intent1 = new Intent(opciones.this, juego.class);
        final Button botRojo = (Button) findViewById(R.id.botonp1);
        final Button botAzul = (Button) findViewById(R.id.botonp2);
        final Button botCont = (Button) findViewById(R.id.button);

        try {
            FileReader file = new FileReader(getFilesDir()+"savegame.txt");
            BufferedReader br = new BufferedReader(file);
            String linea;
            if ((linea = br.readLine()) != null) {
                secarga=1;
            }else {secarga=0;}
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        botCont.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(secarga==1) {
                    finish();
                    intent1.putExtra("archivo",archivostr);
                    musica.stopAudio();
                    startActivity(intent1);
                }
                else {
                    DisplayToast("No existe partida en curso");
                }
            }
        });

        botRojo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                musica.stopAudio();
                String s ="b2fv";
                intent1.putExtra("fondo",s);
                startActivity(intent1);
            }
        });

        botAzul.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                musica.stopAudio();
                String s = "b1fv";
                intent1.putExtra("fondo",s);
                startActivity(intent1);
            }
        });
    }

    private void DisplayToast(String mensaje) {
        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        musica.stopAudio();
        Intent i = new Intent(opciones.this, Portada.class);
        finish();
        startActivity(i);
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
            musica.mediaPlayer.start();}
    }

