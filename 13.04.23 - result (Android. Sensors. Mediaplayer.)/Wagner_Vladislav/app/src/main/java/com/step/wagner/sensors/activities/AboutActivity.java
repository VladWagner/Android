package com.step.wagner.sensors.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.step.wagner.sensors.R;
import com.step.wagner.sensors.async_tasks.CommonTask;
import com.step.wagner.sensors.infrastructure.Parameters;
import com.step.wagner.sensors.infrastructure.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AboutActivity extends AppCompatActivity {

    //Вывод HTML
    private WebView webView;

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        webView = findViewById(R.id.aboutWebView);
        findViewById(R.id.btnExitAbout).setOnClickListener(v -> finish());
        currentView = this.findViewById(android.R.id.content);

        CommonTask commonTask = new CommonTask();

        //Запуск чтения файла в отдельной задаче
        commonTask.execute(() -> {

            StringBuffer contentSbf = new StringBuffer();

            //Октыртие потока для чтения файла
            try( BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(Parameters.HTML_FILE_NAME) )) ){

                String line = reader.readLine();

                //Чтение построчно
                while (line != null){
                    contentSbf.append(line).append("\n");
                    line = reader.readLine();

                }

            } catch (Exception e) {

                //Если упало исключение, то вывести сообщение
                return () -> {

                    Utils.showSnackBar(currentView,"Прочитать файл с разметкой не удалось!");

                    return null;
                };
            }//try-cath

            //Запись прочитанного файла в web view
            return () -> {

                webView.loadData(contentSbf.toString(),"text/html","UTF-8");

                return null;
            };
        });

    }//OnCreate

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.btnSettings:
                startActivity(new Intent(this,SettingsActivity.class));
            case R.id.btnExitFromActivity:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}