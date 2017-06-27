package com.curso.solitariopiramide;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;


public class juego extends AppCompatActivity{
    boolean flag = false;
    Chronometer mChronometer;
    int cont =0;
    int c = 0;
    Vector<Integer> contadorjugadas = new Vector<Integer>();
    Vector<carta> destapadas = new Vector<carta>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras(); // leer informacion de otras actividades
        final String colordorso=bundle.getString("fondo");
        musica.playAudio(this, R.raw.musica);
        String archivostr;
        final Intent intent1 = new Intent(juego.this, instrucciones.class); // creaciones de intents
        final Intent intent2 = new Intent(juego.this, juego.class);
        final Intent intent3 = new Intent(juego.this, pantallawin.class);
        final Intent intent4 = new Intent(juego.this, pantallalose.class);
        mChronometer= (Chronometer) findViewById(R.id.chronometer4);
        final Vector<Long>  timestopped = new Vector<Long>();
        final ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constr);
        final TextView numjugadas = (TextView) findViewById(R.id.textView);
        numjugadas.setText("Nº Jugadas: " + String.valueOf(contadorjugadas));
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics); //obtencion de medidas de la pantalla
        final int W = metrics.widthPixels;
        final int H = metrics.heightPixels;
        final float anchocarta = W / 9;
        final float altocarta = H / 6;
        final float posxmazo = (float) 0.15 * W;
        final float posymazo = (float) 0.75 * H;
        final float posxpila = (float) 0.30 * W;
        final float posypila = (float) 0.75 * H;
        final float posxdescarte = (float) 0.75 * W;
        final float posydescarte = (float) 0.75 * H;
        // creamos vector de cartas
        final Vector<carta> cartas = new Vector<carta>();
        //se crea vector de descarte
        final Vector<carta> descarte = new Vector<carta>();
        // se crea matriz de strings que llevara el tablero(piramide) actualizado
        final String[][] tablerojava = new String[7][7];
        char[] pintas = {'p', 'c', 't', 'd'};
        // se crean las pilas para el mazo y el tablero, luego estas se llenan.
        final Stack<carta> mazo = new Stack<carta>(), tablero = new Stack<carta>();
        //se crea la matriz de cartas tablero para crear los ontouchlistener de las cartas mas adelante
        final carta[][] tab = new carta[7][7];
        if((archivostr = bundle.getString("archivo"))!=null){ //carga archivos si hay partida pendiente
            cargajuego(mazo,tablero,descarte,cl);//se carga el juego
            cargatiempo(timestopped);
            cargajugadas(contadorjugadas);
            mChronometer.setBase(SystemClock.elapsedRealtime()+timestopped.get(0));// se ajusta el tiempo al valor guardado y se activa
            mChronometer.start();
            numjugadas.setText("Nº Jugadas: "+String.valueOf(contadorjugadas.get(0)));
            int k = 0; // se asignan los valores a la matriz de strings del tablerojava y a la matriz de cartas
            for (int i = 0; i < tab.length; i++) {
                for (int j = 0; j < tab.length; j++) {
                    if (j <= i) {
                        tab[i][j] = tablero.get(k);
                        if (tablero.get(k).getLugar()=='T'){
                            tablerojava[i][j]=tablero.get(k).getCarta();
                        }else if (tablero.get(k).getLugar()=='D'){
                            tablerojava[i][j]="0";
                        }
                        k++;
                    } else {
                        carta aux = new carta();
                        aux.setCarta("0");
                        tab[i][j] = aux;
                        tablerojava[i][j]=aux.getCarta();
                    }
                }
            }
        }else {
            mChronometer.setBase(SystemClock.elapsedRealtime()); // se inicia el tiempo en 0
            mChronometer.start();
            contadorjugadas.removeAllElements();
            contadorjugadas.add(0);// se inicia el contador de jugadas en 0
            numjugadas.setText("Nº Jugadas: "+String.valueOf(contadorjugadas.get(0)));
            // creamos vector de cartas
            for (int i = 0; i < pintas.length; i++) {
                for (int j = 0; j < 13; j++) {
                    carta c = new carta();
                    c.setCarta((pintas[i]) + Integer.toString(j + 1));
                    c.setPinta(pintas[i]);
                    c.setNumero((j + 1));
                    c.setImgOriginal(cl, this);
                    c.setFondo(colordorso);
                    cartas.add(c);
                }
            }
            Collections.shuffle(cartas); // se barajan las cartas
            // se separan las cartas en el tablero y mazo
            int esp =0;
            for (int i = 0; i < cartas.size(); i++) {
                if (i < 28) {
                    tablero.push(cartas.get(i));
                } else {
                    mazo.push(cartas.get(i));
                    cartas.get(i).setCoordxoriginal((float)(posxmazo+esp*(0.15)));
                    cartas.get(i).setCoordyoriginal((float)(posymazo-esp*(0.15)));
                    System.out.println(cartas.get(i).getCarta());
                    cartas.get(i).volteacarta(this);
                    cartas.get(i).getImg().setElevation(i);
                    cartas.get(i).setElevacion(i);
                    cartas.get(i).setLugar('M');
                    cartas.get(i).setLugaroriginal('M');
                    esp++;
                }
            }
            // se agrega la carta transparente al final del mazo para ser usada como reiniciador de la pila
            final carta mazovacio = new carta();
            mazovacio.setCarta("trans");
            mazovacio.setImgOriginal(cl, this);
            mazovacio.setCoordxoriginal(posxmazo);
            mazovacio.setCoordyoriginal(posymazo);
            mazovacio.getImg().setElevation(0);
            mazovacio.setElevacion(0);
            mazo.push(mazovacio);
            int k = 0;
            // se asignan los valores a la matriz de strings del tablerojava y a la matriz de cartas
            for (int i = 0; i < tab.length; i++) {
                for (int j = 0; j < tab.length; j++) {
                    if (j <= i) {
                        tab[i][j] = tablero.get(k);
                        tablerojava[i][j]=tablero.get(k).getCarta();
                        k++;
                    } else {
                        carta aux = new carta();
                        aux.setCarta("0");
                        tab[i][j] = aux;
                        tablerojava[i][j]=aux.getCarta();
                    }
                }
            }
            // se inicializan las posiciones de las cartas en el tablero
            int p = 0;
            for (int i = 0; i < 7; i++) {
                final int fila = i;
                float espaciado = -(2 * anchocarta / 3) * i;
                for (int j = 0; j <= i; j++) {
                    final int colu = j;
                    if (j != 0) {
                        espaciado = espaciado + (int) (0.038 * W) + anchocarta;
                        tablero.get(p).setCoordxoriginal(espaciado);
                        tablero.get(p).setCoordyoriginal((2*altocarta / 5) * i);
                        tablero.get(p).getImg().setElevation(i);
                        tablero.get(p).setR(this);
                        tablero.get(p).setLugaroriginal('T');
                        tablero.get(p).setLugar('T');
                        p++;

                    } else {
                        espaciado = espaciado + (int) ((W - anchocarta * (j + 1)) / (j + 2));
                        tablero.get(p).setCoordxoriginal(espaciado);
                        tablero.get(p).setCoordyoriginal((2*altocarta / 5) * i);
                        tablero.get(p).getImg().setElevation(i);
                        tablero.get(p).setR(this);
                        tablero.get(p).setLugaroriginal('T');
                        tablero.get(p).setLugar('T');
                        p++;
                    }
                }
            }
        }
        // se crean los botones de instrucciones, reiniciar y deshacer
        final Button botonInst = (Button) findViewById(R.id.ins);
        final Button botonR = (Button) findViewById(R.id.reiniciar);
        final Button b = (Button) findViewById(R.id.deshacer);
        // si el usuario selecciona fondo azul, se cambia el fondo de los botones a azul.
        // en caso contrario, se cargan por defecto en rojo
         if(mazo.get(0).getFondo().equals("b1fv")){
            botonInst.setBackground(getResources().getDrawable(R.drawable.instrucciones));
            botonR.setBackground(getResources().getDrawable(R.drawable.reinioc));
            b.setBackground(getResources().getDrawable(R.drawable.deshacera));
        }
        botonInst.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(intent1);
            }
        });

        botonR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                musica.stopAudio();
                intent2.putExtra("fondo",colordorso); // se entrega el color del dorso de las cartas para reiniciar el juego
                finish();
                startActivity(intent2);
            }
        });
        // se crean ontouchlistener para las cartas del tablero
        for (int i = 0; i < 7; i++) {
            final int fila = i;
            for (int j = 0; j <= i; j++) {
                final int colu = j;
                tab[i][j].getImg().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (jugadavalida(fila, colu, tablerojava)) {// se comprueba que se ejecuta una accion sobre las cartas destapadas
                            float x = event.getX();
                            float y = event.getY();
                            final float xor = tab[fila][colu].getCoordx();
                            final float yor = tab[fila][colu].getCoordy();
                            final float w = v.getWidth();
                            final float h = v.getHeight();
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (tab[fila][colu].getNumero()==13){//si la carta tocada es un kaiser, se lleva al descarte
                                        descartacarta(tab[fila][colu]);
                                        descarte.add(tab[fila][colu]);
                                        tablerojava[fila][colu]="0";
                                        guardajuego(mazo,tablero,descarte);
                                        guardatiempo();
                                        int aux = contadorjugadas.get(0);
                                        contadorjugadas.removeAllElements();
                                        contadorjugadas.add(aux+1);
                                        numjugadas.setText("Nº Jugadas: "+String.valueOf(contadorjugadas.get(0)));
                                        guardajugadas();
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if(tab[fila][colu].getCoordx() != posxdescarte && tab[fila][colu].getCoordy() != posydescarte ){//si las cartas no estan en el descarte, se permite movimiento
                                        tab[fila][colu].setCoordx(xor + x - w / 2);
                                        tab[fila][colu].setCoordy(yor + y - h / 2);
                                        tab[fila][colu].getImg().setElevation(1000);// se asigna elevacion muy alta para que se sobreponga a todas las otras cartas
                                        v.postInvalidate();
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if(tab[fila][colu].getCoordx() != posxdescarte && tab[fila][colu].getCoordy() != posydescarte) { //si la carta movida no pertenece al descarte
                                        for (int i=0; i<7; i++){
                                            for (int j=0; j<=i ; j++){ // se busca sobre que carta se soltó
                                                if(tab[i][j].getR().contains((int) event.getRawX(), (int) event.getRawY()) && !tab[i][j].getCarta().equals(tab[fila][colu].getCarta())){
                                                    if (sumadiagonal(i,j,tablerojava,tab[fila][colu])||jugadavalida(i,j,tablerojava)){ // Se verifica si sobre la carta que se solto se puede sumar en diagonal, o si esta destapada
                                                        if (sumacorrecta(tab[i][j],tab[fila][colu])){ // se verifica si suman 13 y se descartan ambas
                                                            descarte.add(tab[i][j]);
                                                            descartacarta(tab[i][j]);
                                                            tablerojava[i][j]="0";
                                                            descarte.add(tab[fila][colu]);
                                                            descartacarta(tab[fila][colu]);
                                                            tab[fila][colu].getImg().setElevation(fila);
                                                            tablerojava[fila][colu]="0";
                                                            guardajuego(mazo,tablero,descarte);
                                                            guardatiempo();
                                                            int aux = contadorjugadas.get(0);
                                                            contadorjugadas.removeAllElements();
                                                            contadorjugadas.add(aux+1);
                                                            numjugadas.setText("Nº Jugadas: "+String.valueOf(contadorjugadas.get(0)));
                                                            guardajugadas();
                                                            i=7;
                                                            j=i+1;
                                                        }else {
                                                            tab[fila][colu].getImg().setElevation(fila);
                                                            tab[fila][colu].posoriginal();
                                                        }
                                                    }else {
                                                        tab[fila][colu].getImg().setElevation(fila);
                                                        tab[fila][colu].posoriginal();
                                                    }


                                                }else {
                                                    tab[fila][colu].posoriginal();
                                                    tab[fila][colu].getImg().setElevation(fila);
                                                }
                                            }
                                        }

                                    }
                                    if(gano(tablerojava)){ // si en el tablero no quedan cartas, se gana el juego y se elimina el archivo guardado, dado que no hay juego pendiente
                                        File f = new File(getFilesDir()+"savegame.txt");
                                        boolean borra = f.delete();
                                        finish();
                                        startActivity(intent3);
                                    }
                                    break;
                            }
                        }
                        return true;
                    }
                });

            }
        }
// se crean ontouchlistener para las cartas en el mazo
        for (int i = 0; i < mazo.size(); i++) {
            final int elem = i;
            mazo.get(i).getImg().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float y = event.getY();
                    final float xor = mazo.get(elem).getCoordx();
                    final float yor = mazo.get(elem).getCoordy();
                    final float w = v.getWidth();
                    final float h = v.getHeight();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!mazo.get(elem).getCarta().equals("trans")) { // se verifica que la carta pulsada no sea la transparente
                                if (mazo.get(elem).getLugar() == 'M') { // si la carta esta en el mazo, se muestra en la pila
                                    cont++;
                                    mazo.get(elem).setCoordx(posxpila);
                                    mazo.get(elem).volteacarta(juego.this);
                                    mazo.get(elem).getImg().setElevation(cont);
                                    mazo.get(elem).setElevacion(cont);
                                    guardajuego(mazo,tablero,descarte);
                                    guardatiempo();
                                }else if(mazo.get(elem).getLugar() == 'P' && mazo.get(elem).getNumero() == 13 ){ //si esta en la pila y es un kaiser, se descarta
                                    descarte.add(mazo.get(elem));
                                    mazo.get(elem).setPosicionendescarte(descarte.size());
                                    descartacarta(mazo.get(elem));
                                    guardajuego(mazo,tablero,descarte);
                                    guardatiempo();
                                    int aux = contadorjugadas.get(0);
                                    contadorjugadas.removeAllElements();
                                    contadorjugadas.add(aux+1);
                                    numjugadas.setText("Nº Jugadas: "+String.valueOf(contadorjugadas.get(0)));
                                    guardajugadas();
                                }
                            }else { // si es pulsada la carta transparente, se reinicia la pila del mazo
                                destapadas = new Vector<carta>();
                                rellenarmazo(mazo);
                                cartasdestapadas(tablerojava,tab,destapadas);
                                // se verifican si existen jugadas validas que realizar, caso contrario, se da por perdido el juego, dando  la opcion de retomar este y deshacer jugadas
                                if (!entredestapadas(destapadas)&&!cartasjuntas(tablerojava,tab)&&!haykaiser(destapadas)&&!mazoconcartadest(mazo,destapadas)) {
                                    finish();
                                    startActivity(intent4);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mazo.get(elem).getLugar()=='P' ) { // se permiten movimientos de las cartas de la pila
                                mazo.get(elem).getImg().setElevation(1000);
                                mazo.get(elem).setCoordx(xor + x - w / 2);
                                mazo.get(elem).setCoordy(yor + y - h / 2);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!mazo.get(elem).getCarta().equals("trans")) { //se verifica que no sea la carta transparente
                                if ( mazo.get(elem).getLugar()!='D') { // se verifica que la carta no esté en el descarte
                                    for (int i = 6; i >= 0; i--) {
                                        for (int j = i; j >= 0; j--) {
                                            if (jugadavalida(i, j, tablerojava)) { // se verifica si la carta del tablero donde se soltó la carta del mazo está destapada
                                                if (tab[i][j].getR().contains((int) event.getRawX(), (int) event.getRawY())) {// se busca sobre que carta del tablero se soltó
                                                    if (sumacorrecta(mazo.get(elem), tab[i][j])) { // si suman 13, se descartan ambas

                                                        descarte.add(mazo.get(elem));
                                                        descarte.add(tab[i][j]);
                                                        descartacarta(mazo.get(elem));
                                                        descartacarta(tab[i][j]);
                                                        guardajuego(mazo,tablero,descarte);
                                                        guardatiempo();
                                                        tablerojava[i][j]= "0";
                                                        int aux = contadorjugadas.get(0);
                                                        contadorjugadas.removeAllElements();
                                                        contadorjugadas.add(aux+1);
                                                        numjugadas.setText("Nº Jugadas: "+String.valueOf(contadorjugadas.get(0)));
                                                        guardajugadas();
                                                        i = -1;
                                                        j = -1;
                                                        c++;
                                                    }
                                                    else { // en caso que las condiciones anteriores no se cumplan, se retorna la carta a la pila
                                                        mazo.get(elem).getImg().setElevation(cont);
                                                        mazo.get(elem).setLugar('P');
                                                        mazo.get(elem).setCoordx(posxpila);
                                                        mazo.get(elem).setCoordy(posypila);
                                                    }
                                                }else {
                                                    mazo.get(elem).getImg().setElevation(cont);
                                                    mazo.get(elem).setLugar('P');
                                                    mazo.get(elem).setCoordx(posxpila);
                                                    mazo.get(elem).setCoordy(posypila);
                                                }
                                            } else {
                                                mazo.get(elem).getImg().setElevation(cont);
                                                mazo.get(elem).setLugar('P');
                                                mazo.get(elem).setCoordx(posxpila);
                                                mazo.get(elem).setCoordy(posypila);
                                            }
                                        }
                                    }
                                }
                            }
                            if(gano(tablerojava)){ // se verifica que el tablero este vacio, y se declara victoria en el juego
                                // se elimina el archivo guardado, ya que el juego no queda pendiente
                                File f = new File(getFilesDir()+"savegame.txt");
                                boolean borra = f.delete();
                                finish();
                                startActivity(intent3);
                            }
                            break;
                    }
                    return true;
                }
            });
        }
        // se define un onckicklistener para el boton deshacer jugadas
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deshacer(descarte,mazo,tablero);
                actualizatabjava(tablero,tablerojava);
                guardajuego(mazo,tablero,descarte);
                guardatiempo();
            }
        });

    }


    boolean jugadavalida(int posx, int posy, String[][] t){
        if (posx == 6|| ((t[posx+1][posy].equals("0")) && (t[posx+1][posy+1].equals("0")))){
            return true;
        }else {
            return false;}
    }
    boolean sumacorrecta(carta c1, carta c2){
        int suma;
        if ((suma= c1.getNumero()+c2.getNumero())==13){
            return true;
        }else { return false;}
    }

    boolean sumadiagonal(int posx, int posy, String[][] t,carta c){
        if (posx == 6 || ((t[posx+1][posy].equals("0")) && (t[posx+1][posy+1].equals(c.getCarta())))){
            return true;
        }else if (posx ==6 || ((t[posx+1][posy+1].equals("0")) && (t[posx+1][posy].equals(c.getCarta())))){
            return true;
        }else {
            return false;
        }
    }
    void rellenarmazo(Stack<carta> m){
        int W = getResources().getDisplayMetrics().widthPixels;
        int H = getResources().getDisplayMetrics().heightPixels;
        int k = 1;
        int j=0;
        for ( carta c : m){
            if (c.getLugar()=='P'){
                c.posoriginal();
                if (!c.getCarta().equals("trans")){
                    c.volteacarta(juego.this);
                    c.getImg().setElevation(k);
                    c.setLugar('M');
                    k++;
                }
            }
        }
        System.out.println((k-1));
    }
    public void descartacarta(carta c){
        int W = getResources().getDisplayMetrics().widthPixels;
        int H = getResources().getDisplayMetrics().heightPixels;
        float posxdescarte = (float) 0.75 * W;
        float posydescarte = (float) 0.75 * H;
        c.setCoordx(posxdescarte);
        c.setCoordy(posydescarte);
        c.setLugar('D');
        c.setR(juego.this);
        c.volteacarta(juego.this);
    }

    boolean gano(String[][] t){
        for (String[] s : t){
            for (String ss : s){
                if (!ss.equals("0")){
                    return false;
                }
            }
        }
        return true;
    }

    void cartasdestapadas(String[][] t,carta[][] tab,Vector<carta> destapadas){
        for (int i =0 ; i< t.length;i++){
            for (int j =0; j<=i;j++){
                if (i < 6){
                    if (!t[i][j].equals("0")){
                        if (t[i+1][j].equals("0")&&t[i+1][j+1].equals("0")){
                            destapadas.add(tab[i][j]);
                        }
                    }
                }else if (!t[i][j].equals("0")){
                    destapadas.add(tab[i][j]);
                }
            }
        }
    }

    boolean cartasjuntas(String[][] t,carta[][] tab){
        for (int i = 0 ; i<6 ; i++){
            for (int j=0; j<=i;j++){
                if (!t[i][j].equals("0")){
                    carta aux = new carta();
                    carta aux2 = new carta();
                    carta aux3 = new carta();
                    aux.setCarta(t[i][j]);
                    aux2.setCarta(t[i+1][j]);
                    aux3.setCarta(t[i+1][j+1]);
                    if (i<5){
                        if ((aux.getNumero()+aux2.getNumero()==13) && (t[i+1][j+1].equals("0")) && (t[i+2][j].equals("0")) &&(t[i+2][j+1].equals("0")) ){
                            return true;
                        }else if ((aux.getNumero()+aux3.getNumero()==13) && (t[i+1][j].equals("0"))&& (t[i+2][j+1].equals("0")) &&(t[i+2][j+2].equals("0")) ){
                            return true;
                        }
                    }else {
                        if ((aux.getNumero()+aux2.getNumero()==13) && (t[i+1][j+1].equals("0"))){
                            return true;
                        }else if ((aux.getNumero()+aux3.getNumero()==13) && (t[i+1][j].equals("0"))){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean mazoconcartadest(Stack<carta> maz, Vector<carta> des){
        for (carta c : maz){
            for(carta c2 : des){
                if (c.getLugar()!='D' && !c.getCarta().equals("trans") && c.getNumero()+c2.getNumero()==13){
                    System.out.println(c.getCarta()+"con"+c2.getCarta());
                    return true;
                }
            }
        }
        return false;
    }

    boolean entredestapadas(Vector<carta> des){
        for (int i = 0; i<des.size();i++){
            for (int j = i; j<des.size();j++){
                if ((j!=i) && (des.get(i).getNumero()+des.get(j).getNumero() == 13)){
                    return true;
                }
            }
        }
        return false;
    }

    boolean haykaiser(Vector<carta> des){
        for (carta c : des){
            if (c.getNumero()==13){
                return true;
            }
        }
        return false;
    }

    void actualizatabjava(Stack<carta> tablero, String[][] tjava){
        System.out.println("Se actualizo");
        int k = 0;
        for (int i = 0; i < tjava.length; i++) {
            for (int j = 0; j < tjava.length; j++) {
                if (j <= i) {
                    if (tablero.get(k).getLugar()=='T'){
                        tjava[i][j]=tablero.get(k).getCarta();
                    }else if (tablero.get(k).getLugar()=='D'){
                        tjava[i][j]="0";
                    }
                    k++;
                } else {
                    carta aux = new carta();
                    aux.setCarta("0");
                    tjava[i][j]=aux.getCarta();
                }
            }
        }
    }



    void deshacer(Vector<carta> des, Stack<carta> maz, Stack<carta> tablero){
        int W = getResources().getDisplayMetrics().widthPixels;
        int H = getResources().getDisplayMetrics().heightPixels;
        final float posxpila = (float) 0.30 * W;
        final float posypila = (float) 0.75 * H;
        int a = des.size();
        System.out.println(a);
        Vector<Integer> v = new Vector<>();
        for (carta c : maz){
            v.add(c.getElevacion());
        }
        if (a>0){
            System.out.println("caca");
            if (des.get(a-1).getNumero()==13){
                if (des.get(a-1).getLugaroriginal()=='T'){
                    for (int i = 0; i<tablero.size();i++){
                        if (des.get(a-1).getCarta().equals(tablero.get(i).getCarta())){
                            tablero.get(i).posoriginal();
                            tablero.get(i).volteacarta(juego.this);
                            tablero.get(i).setLugar('T');
                            tablero.get(i).setR(juego.this);
                            des.removeElementAt(a-1);
                            break;
                        }
                    }
                }else if (des.get(a-1).getLugaroriginal()=='M'){
                    for (int i = 0; i<maz.size();i++){
                        if (des.get(a-1).getCarta().equals(maz.get(i).getCarta())){
                            maz.get(i).setCoordx(posxpila);
                            maz.get(i).setCoordy(posypila);
                            maz.get(i).setLugar('P');
                            maz.get(i).volteacarta(juego.this);
                            maz.get(i).getImg().setElevation(Collections.min(v));
                            des.removeElementAt(a-1);
                            break;
                        }
                    }
                }
            }else {
                System.out.println("entro aqui");
                System.out.println(des.get(a-1).getLugaroriginal()+" (a-2): "+des.get(a-2).getLugaroriginal());
                if (des.get(a-1).getLugaroriginal()=='T' && des.get(a-2).getLugaroriginal()=='T'){
                    for (int i = 0; i<tablero.size();i++){
                        if (des.get(a-1).getCarta().equals(tablero.get(i).getCarta())){
                            System.out.println("Encontro la carta "+tablero.get(i).getCarta());
                            tablero.get(i).posoriginal();
                            tablero.get(i).setLugar('T');
                            tablero.get(i).volteacarta(juego.this);
                            tablero.get(i).setR(juego.this);
                            des.removeElementAt(a-1);
                            break;
                        }
                    }
                    for (int i = 0; i<tablero.size();i++){
                        if (des.get(a-2).getCarta().equals(tablero.get(i).getCarta())){
                            System.out.println("Encontro la carta "+tablero.get(i).getCarta());
                            tablero.get(i).posoriginal();
                            tablero.get(i).setLugar('T');
                            tablero.get(i).volteacarta(juego.this);
                            tablero.get(i).setR(juego.this);
                            des.removeElementAt(a-2);
                            break;
                        }
                    }
                }else if (des.get(a-1).getLugaroriginal()=='T' && des.get(a-2).getLugaroriginal()=='M'){
                    System.out.println("mazo y tablero");
                    for (int i = 0; i<maz.size();i++){
                        if (des.get(a-2).getCarta().equals(maz.get(i).getCarta())){
                            System.out.println("Encontro la carta "+tablero.get(i).getCarta());
                            maz.get(i).setCoordx(posxpila);
                            maz.get(i).setCoordy(posypila);
                            maz.get(i).setLugar('P');
                            maz.get(i).volteacarta(juego.this);
                            maz.get(i).getImg().setElevation(Collections.min(v));
                            des.removeElementAt(a-2);
                            break;
                        }
                    }
                    for (int i = 0; i<tablero.size();i++){
                        if (des.get(a-2).getCarta().equals(tablero.get(i).getCarta())){
                            System.out.println("Encontro la carta "+tablero.get(i).getCarta());
                            tablero.get(i).posoriginal();
                            tablero.get(i).setLugar('T');
                            tablero.get(i).volteacarta(juego.this);
                            tablero.get(i).setR(juego.this);
                            //actualizatabjava(tablero,tjava);
                            des.removeElementAt(a-2);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void DisplayToast(String mensaje) {
        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    void guardatiempo(){
        try{
            FileOutputStream fos = openFileOutput("timesavegame.txt", MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            long timestopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
            dos.writeLong(timestopped);
            dos.flush();
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void cargatiempo(Vector<Long> timestopped){
        try{
            FileInputStream fis = openFileInput("timesavegame.txt");
            DataInputStream dis = new DataInputStream(fis);
            timestopped.add(dis.readLong());
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void guardajugadas(){
        try{
            FileOutputStream fos = openFileOutput("jugsavegame.txt", MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(contadorjugadas.get(0));
            dos.flush();
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void cargajugadas(Vector<Integer> timestopped){
        try{
            FileInputStream fis = openFileInput("jugsavegame.txt");
            DataInputStream dis = new DataInputStream(fis);
            timestopped.add(dis.readInt());
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void cargajuego( Stack<carta> mazo,Stack<carta> tablero, Vector<carta> des, ConstraintLayout cl){
        try {
            FileReader file = new FileReader(getFilesDir()+"savegame.txt");
            BufferedReader br = new BufferedReader(file);
            String linea;
            int j = 0;
            int k = 1;
            while ((linea = br.readLine()) != null) {
                String[] r = linea.split(" ");
                carta c2 = new carta();
                if (!r[0].equals("trans")&&j<=52){
                    c2.setCarta(r[0]+(r[1]));
                    c2.setNumero(Integer.parseInt(r[1]));
                    c2.setPinta(r[0].charAt(0));
                    c2.setImgOriginal(cl,this);
                    c2.setCoordxoriginal(Float.parseFloat(r[2]));
                    c2.setCoordyoriginal(Float.parseFloat(r[3]));
                    c2.setCoordx(Float.parseFloat(r[4]));
                    c2.setCoordy(Float.parseFloat(r[5]));
                    c2.setElevacion(Integer.parseInt(r[6]));
                    c2.setLugar(r[7].charAt(0));
                    c2.setR(juego.this);
                    c2.setLugaroriginal(r[8].charAt(0));
                    c2.setFondo(r[9]);
                    System.out.println(c2.getCarta()+" en 1");
                    c2.getImg().setElevation(c2.getElevacion());
                    if (c2.getLugar()=='M'){
                        c2.setCoordx(c2.getCoordxoriginal());
                        c2.setCoordy(c2.getCoordyoriginal());
                    }
                }else if (j<=52 && r[0].equals("trans")) {
                    c2.setCarta("trans");
                    c2.setImgOriginal(cl,this);
                    c2.setCoordxoriginal(Float.parseFloat(r[1]));
                    c2.setCoordyoriginal(Float.parseFloat(r[2]));
                    c2.setElevacion(Integer.parseInt(r[3]));
                    c2.setLugaroriginal('M');
                    c2.setLugar('M');
                    c2.getImg().setElevation(c2.getElevacion());
                    System.out.println(c2.getCarta()+" en 2");
                }else if(j>52){
                    c2.setCarta(r[0]+(r[1]));
                    c2.setNumero(Integer.parseInt(r[1]));
                    c2.setPinta(r[0].charAt(0));
                    c2.setLugaroriginal(r[8].charAt(0));
                    System.out.println(c2.getCarta()+" en 3");
                }
                if (c2.getLugaroriginal()=='M'&&j<=52){
                    System.out.println(c2.getCarta()+" Se puso en el mazo");
                    if(c2.getLugar()!='P' && !c2.getCarta().equals("trans")){
                        c2.volteacarta(juego.this);
                        c2.getImg().setElevation(k);
                        k++;
                    }
                    mazo.push(c2);
                }else if (j>52){
                    System.out.println(c2.getCarta()+" Se puso en el descarte");
                    des.add(c2);
                }
                else if (c2.getLugaroriginal()=='T'&&j<=52){
                    System.out.println(c2.getCarta()+" Se puso en el tablero");
                    if (c2.getLugar()=='D'){
                        c2.volteacarta(juego.this);
                    }
                    tablero.push(c2);
                }
                j++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void guardajuego( Stack<carta> mazo,Stack<carta> tablero, Vector<carta> des ){
        try{
            File f = new File(getFilesDir()+"savegame.txt");
            boolean borra = f.delete();
            FileWriter fOut = new FileWriter(getFilesDir()+"savegame.txt");
            BufferedWriter wr = new BufferedWriter(fOut);
            for (carta c1 : mazo){
                if (!c1.getCarta().equals("trans")){
                    wr.append(c1.getPinta());
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getNumero()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getCoordxoriginal()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getCoordyoriginal()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getCoordx()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getCoordy()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getElevacion()));
                    wr.append(" ");
                    if (c1.getLugar()!='D'){
                        wr.append('M');
                        wr.append(" ");
                    }else {
                        wr.append(c1.getLugar());
                        wr.append(" ");
                    }
                    wr.append(c1.getLugaroriginal());
                    wr.append(" ");
                    wr.append(c1.getFondo());
                    wr.append("\n");
                }else {
                    wr.append(c1.getCarta());
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getCoordxoriginal()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getCoordyoriginal()));
                    wr.append(" ");
                    wr.append(String.valueOf(c1.getElevacion()));
                    wr.append("\n");
                }
            }
            for (carta c1:tablero){
                wr.append(c1.getPinta());
                wr.append(" ");
                wr.append(String.valueOf(c1.getNumero()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordxoriginal()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordyoriginal()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordx()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordy()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getElevacion()));
                wr.append(" ");
                wr.append(c1.getLugar());
                wr.append(" ");
                wr.append(c1.getLugaroriginal());
                wr.append(" ");
                wr.append(c1.getFondo());
                wr.append("\n");
            }
            System.out.println("numero de cartas en descarte: "+des.size());
            for (carta c1: des){
                wr.append(c1.getPinta());
                wr.append(" ");
                wr.append(String.valueOf(c1.getNumero()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordxoriginal()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordyoriginal()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordx()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getCoordy()));
                wr.append(" ");
                wr.append(String.valueOf(c1.getElevacion()));
                wr.append(" ");
                wr.append(c1.getLugar());
                wr.append(" ");
                wr.append(c1.getLugaroriginal());
                wr.append(" ");
                wr.append(c1.getFondo());
                wr.append("\n");
            }
            wr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(juego.this, opciones.class);
        finish();
        musica.stopAudio();
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