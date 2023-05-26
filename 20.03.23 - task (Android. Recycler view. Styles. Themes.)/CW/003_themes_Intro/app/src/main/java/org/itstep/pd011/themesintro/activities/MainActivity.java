package org.itstep.pd011.themesintro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.itstep.pd011.themesintro.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } // onCreate

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

            // вызов активности, использующей собственную тему
            case R.id.mniActivityTheme:
                // примитивный вызов активности :(
                startActivity(new Intent(this, OwnThemeActivity.class));
                break;

            // вызов активности, использующей тему в контейнере
            case R.id.mniContainerTheme:
                startActivity(new Intent(this, ContainerOwnThemeActivity.class));
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}