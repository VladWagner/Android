package org.itstep.pd011.webviewdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/*
 * Для получения данных с определенного интернет-ресурса мы можем использовать классы
 * HttpUrlConnection (для протокола HTTP) и HttpsUrlConnection (для протокола HTTPS)
 * из стандартной библиотеки Java.
 *
 * */
public class WebLoadActivity extends AppCompatActivity {

    private TextView contentView; // вывод данных в это поле
    private WebView webView;      // вывод отрендеренного HTML

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_load);

        contentView = findViewById(R.id.content);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        Button btnFetch = findViewById(R.id.downloadBtn);
        btnFetch.setOnClickListener(v -> clickDownload());
    } // onCreate

    // обработчик клика по кнопке
    private void clickDownload() {

        // вывод сообщения о начале загрузки
        contentView.setText("Загрузка...");

        // запуск потока загрузки данных
        new Thread(runnable).start();
    } // clickDownload


    // исполняемый код загрузчика данных
    // TODO: решить проблему с кодировкой
    private Runnable runnable = () -> {
        try {
            // собственно загрузка данных
            String path = "http://lib.ru/";
            String content = getContent(path);

            // поместить данные в WebView - отрендеренный HTML
            webView.post(() -> {
                webView.loadDataWithBaseURL(path, content,"text/html", "utf-8", null);
            });

            // поместить необработанные данные в TextView
            contentView.post(() -> contentView.setText(content));
        } catch (IOException ex){

            contentView.post(() -> {
                String str = "Ошибка: " + ex.getMessage();
                contentView.setText(str);
            });
        } // try-catch
    };

    // чтение данных из HTTP-соединения
    private String getContent(String path) throws IOException {
        HttpURLConnection connection = null;  // соединение HTTP, для HTTPS HttpsURLConnection

        try {
            // адрес для соединения
            URL url = new URL(path);
            connection = (HttpURLConnection)url.openConnection();

            // настройка и отправка запроса
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10_000);
            connection.connect();

            // получение потока ввода из HTTP-соединения
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                // чтение данных из соединения в буфер buf
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    buf.append(line);

                // вернуть строку, полученную по HTTP
                return (buf.toString());
            } // try-resources
        } finally {
            if (connection != null)  connection.disconnect();
        } // try-finally
    } // getContent

    //region Меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mniActivityBack) {
            // возврат из активности
            finish();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
	//endregion
}