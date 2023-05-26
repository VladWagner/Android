package org.itstep.pd011.themesintro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.itstep.pd011.themesintro.R;

// активность, использующая тему в контейнере - все только в разметке
public class ContainerOwnThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_own_theme);
    } // onCreate

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
} // class ContainerOwnThemeActivity