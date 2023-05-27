package org.itstep.pd011.simpleaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // вопроизвдение MP3
    MediaPlayer mediaPlayer;

    // системный сервис для управление звуком
    AudioManager audioManager;

    Button btnPlay, btnPause, btnStop;
    SeekBar skbVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получить медиаплеер, настроить на воспроизведение из ресурсов,
        // завершать работу плеера по окончании воспроизведения
        mediaPlayer = MediaPlayer.create(this, R.raw.baccara);
        mediaPlayer.setOnCompletionListener(mp -> stop(btnStop));

        // Управление звуком
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // управение громкостью
        skbVolume = findViewById(R.id.skbVolume);
        skbVolume.setMax(maxVolume);
        skbVolume.setProgress(curVolume);

        // кнопки интерфейса
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);

        // назначение слушателей событий
        skbVolume.setOnSeekBarChangeListener(onSeekBarChangeListner);
        btnPlay.setOnClickListener(v -> play(v));
        btnPause.setOnClickListener(v -> pause(v));
        btnStop.setOnClickListener(v -> stop(v));

        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
    } // onCreate

    private void play(View v) {
        mediaPlayer.start();

        btnPlay.setEnabled(false);
        btnPause.setEnabled(true);
        btnStop.setEnabled(true);
    } // play

    private void pause(View v) {
        mediaPlayer.pause();

        btnPlay.setEnabled(true);
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
    } // pause

    private void stop(View v) {
        mediaPlayer.stop();

        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } // try-catch

        btnPlay.setEnabled(true);
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
    } // stop

    // обработка события перемещения ползунка нашего регулятора громкости
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListner = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        } // onProgressChanged

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };
} // class MainActivity