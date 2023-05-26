package org.itstep.pd011.webviewdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.io.File;
import java.util.Scanner;

/* загрузка в WebView */
public class MainActivity extends AppCompatActivity {

    Button btnUrlLoad;    // для загрузки из сети
    Button btnFileLoad;   // для загрузки локального файла
    Button btnDataLoad;   // для загрузки из строки

    WebView wbvMain;      // элемент отображения HTML-страниц

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получение ссылок на элементы разметки
        btnUrlLoad = findViewById(R.id.btnUrlLoad);
        btnFileLoad = findViewById(R.id.btnFileLoad);
        btnDataLoad = findViewById(R.id.btnDataLoad);

        // получить и настроить WebView, включение использования JavaScript
        wbvMain = findViewById(R.id.wbvMain);
        WebSettings webSettings = wbvMain.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // назначение обработчиков клика кнопок
        btnUrlLoad.setOnClickListener(v -> loadUrl());
        btnFileLoad.setOnClickListener(v -> loadFile());
        btnDataLoad.setOnClickListener(v -> loadData());
    } // onCreate


    // загрузка контента из сети
    private void loadUrl() {
        wbvMain.loadUrl("http://lib.ru");
    } // loadUrl

    // загрузка контента из HTML-файла в assets
    private void loadFile() {
        // буфер для чтения данных из файла
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(getAssets().open("info.html"))) {
            while(sc.hasNext()) {
                sb.append(sc.nextLine()).append("\n");
            } // while

            // загрузка данных из буфера строки
            wbvMain.loadData(sb.toString(), "text/html", "UTF-8");
        } catch (Exception e) {
            Log.d("MyApp", "Ошибка " + e.getMessage());
        } // try-catch
    } // loadFile


    // загрузка контента из строки
    private void loadData() {
        // строка с HTML-данными
        String str = "<html><body><h2>Hello, Андроид!</h2><h3>группа ПД011</h3></body></html>";
        wbvMain.loadData(str, "text/html", "UTF-8");
    } // loadFile


    //region Меню приложения
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            // выход из приложения
            case R.id.mniExit:
                finish();
                break;

            // вызов активности, демонстрирующей загрузку данных по HTTP
            case R.id.mniWebLoadActivity:
                // примитивный вызов активности :(
                startActivity(new Intent(this, WebLoadActivity.class));
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}