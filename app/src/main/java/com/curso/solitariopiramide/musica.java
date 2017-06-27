package com.curso.solitariopiramide;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;



public class musica {
    public static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    public static boolean isplayingAudio=false;
    public static  void  playAudio(Context C, int id){
        mediaPlayer=MediaPlayer.create(C,id);
        soundPool=new SoundPool(4, AudioManager.STREAM_MUSIC,100);
        if(!mediaPlayer.isPlaying()){
            isplayingAudio=true;
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

    public static void stopAudio() {
        isplayingAudio=false;
        mediaPlayer.stop();
    }
}
