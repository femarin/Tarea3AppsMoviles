package com.curso.solitariopiramide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class instrucciones extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruccionesgraf);}

    public void onClick(View view) {
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
