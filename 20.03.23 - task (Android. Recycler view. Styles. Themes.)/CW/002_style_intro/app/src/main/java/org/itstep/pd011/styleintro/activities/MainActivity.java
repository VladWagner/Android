package org.itstep.pd011.styleintro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.itstep.pd011.styleintro.R;

// Использование стилей - см. res/values/styles
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

            // вызов активности, не использующей стили
            case R.id.mniNoStyle:
                // примитивный вызов активности :(
                startActivity(new Intent(this, NoStyleActivity.class));
                break;

            // вызов активности, использующей стили
            case R.id.mniUseStyle:
                startActivity(new Intent(this, UseStyleActivity.class));
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // MainActivity