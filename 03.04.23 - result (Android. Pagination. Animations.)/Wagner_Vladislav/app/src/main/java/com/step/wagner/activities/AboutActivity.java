package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        if (item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion
}