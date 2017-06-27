package com.curso.solitariopiramide;

import android.content.Context;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

public class carta {

    public String carta;
    public int numero;
    public char pinta;
    public ImageView img;
    public float coordx;
    public float coordy;
    public float coordxoriginal = 0;
    public float coordyoriginal = 0;
    public String fondo;
    public Rect r;
    public int Elevacion;
    public char lugar;
    int v = 0;
    public char lugaroriginal;
    public int posicionendescarte;


    public int getPosicionendescarte() {
        return posicionendescarte;
    }

    public void setPosicionendescarte(int posicionendescarte) {
        this.posicionendescarte = posicionendescarte;
    }

    public char getLugaroriginal() {
        return lugaroriginal;
    }

    public void setLugaroriginal(char lugaroriginal) {
        this.lugaroriginal = lugaroriginal;
    }


    public int getElevacion() {
        return Elevacion;
    }

    public void setElevacion(int elevacion) {
        Elevacion = elevacion;
    }

    public char getLugar() {
        return lugar;
    }

    public void setLugar(char lugar) {
        this.lugar = lugar;
    }


    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public float getCoordxoriginal() {
        return coordxoriginal;
    }

    public void setCoordxoriginal(float coordxoriginal) {
        this.coordx = coordxoriginal;
        this.img.setX(coordxoriginal);
        this.coordxoriginal = coordxoriginal;
    }

    public float getCoordyoriginal() {
        return coordyoriginal;
    }

    public void setCoordyoriginal(float coordyoriginal) {
        this.coordy = coordyoriginal;
        this.img.setY(coordyoriginal);
        this.coordyoriginal = coordyoriginal;
    }

    public Rect getR() {
        return r;
    }

    public void setR(Context context) {// se crea un rectangulo para las imagenes con el fin de verificar sobre que carta del tablero se suelta una carta
        final int W = context.getResources().getDisplayMetrics().widthPixels;
        final int H = context.getResources().getDisplayMetrics().heightPixels;
        final float anchocarta = W/9;
        final float altocarta = H/8;
        Rect r = getImg().getDrawable().getBounds();
        int tops = (int)(getImg().getY() + r.top);
        int left = (int)(getImg().getX()+ r.left);
        int bottom = (int)(getImg().getY()+r.bottom+altocarta);
        int right = (int)(getImg().getX()+r.right+anchocarta);
        Rect a = new Rect(left,tops,right,bottom);
        this.r = a;
    }

    public float getCoordx() {
        return coordx;
    }

    public void setCoordx(float coordx) {
        this.img.setX(coordx);
        this.coordx = coordx;
    }

    public float getCoordy() {
        return coordy;
    }

    public void setCoordy(float coordy) {
        this.img.setY(coordy);
        this.coordy = coordy;
    }

    public String getCarta() {
        return carta;
    }

    public void setCarta(String c) {
        this.carta = c;
    }

    public int getNumero() { // si la carta es un string 0, se asigna el numero 0. Caso contrario, se asiga el numero que viene despues de la pinta.
        if (!this.carta.equals("0")){
            numero = Integer.parseInt(carta.substring(1));
        }else {
            numero=0;
        }
        return numero;
    }
    public ImageView getImg() {
        return img;
    }

    public void posoriginal() { // se cambia la posicion de la carta  a la posicion original
        this.coordx = coordxoriginal;
        this.coordy = coordyoriginal;
        this.img.setX(this.coordxoriginal);
        this.img.setY(this.coordyoriginal);
    }

    public void setImgOriginal(ConstraintLayout cl, Context context) { // se asigna la imagen correspondiente a la carta con su ancho y alto, y se agrega al layout padre
        ImageView img = new ImageView(context);
        final int W = context.getResources().getDisplayMetrics().widthPixels;
        final int H = context.getResources().getDisplayMetrics().heightPixels;
        final float anchocarta = W/9;
        final float altocarta = H/8;
        img.setX(coordxoriginal);
        img.setY(coordyoriginal);
        cl.addView(img);
        img.getLayoutParams().width = (int) anchocarta;
        img.getLayoutParams().height = (int) altocarta;
        int ident= context.getResources().getIdentifier(this.getCarta(),"drawable",context.getPackageName());
        img.setImageResource(ident);
        this.img = img;
    }

    public void volteacarta(Context context){ // se voltea la carta dependiendo si esta con el dorso a la vista o no
        if (v == 1){
            int ident= context.getResources().getIdentifier(this.getCarta(),"drawable",context.getPackageName());
            this.img.setImageResource(ident);
            v = 0;
        }else if (v ==0){
            int ident= context.getResources().getIdentifier(this.fondo,"drawable",context.getPackageName());
            this.img.setImageResource(ident);
            v = 1;
        }
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public char getPinta() {
        return pinta;
    }

    public void setPinta(char pinta) {
        this.pinta = pinta;
    }

}