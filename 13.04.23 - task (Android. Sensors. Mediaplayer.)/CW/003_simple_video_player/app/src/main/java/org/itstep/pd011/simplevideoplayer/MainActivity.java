package org.itstep.pd011.simplevideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    // Видеопросмотр
    VideoView vivPlayer;

    // кнопки управления
    Button btnPlay, btnPause, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        vivPlayer = findViewById(R.id.vivPlayer);

        // настройка плеера
        // источник видео - файл в папке ресуррсов
        // String strUri = "android.resource://" + getPackageName() + "/" +  R.raw.xanadu;
        // vivPlayer.setVideoURI(Uri.parse(strUri));

        // получение доступа к видео в интернете
        String strPath = "http://techslides.com/demos/sample-videos/small.mp4";
        vivPlayer.setVideoPath(strPath);

        // для включения дополнительных элементов управления
        // видеоплеера
        MediaController mediaController = new MediaController(this);
        vivPlayer.setMediaController(mediaController);
        mediaController.setMediaPlayer(vivPlayer);

        // обработчики кликов по кнопкам управления
        btnPlay.setOnClickListener(v -> vivPlayer.start());
        btnPause.setOnClickListener(v -> vivPlayer.pause());
        btnStop.setOnClickListener(v -> {
            // стоп воспроизведения, подготовиться к повторному запуску
            vivPlayer.stopPlayback();
            vivPlayer.resume();
        });
    }  // onCreate
} // class MainActivity