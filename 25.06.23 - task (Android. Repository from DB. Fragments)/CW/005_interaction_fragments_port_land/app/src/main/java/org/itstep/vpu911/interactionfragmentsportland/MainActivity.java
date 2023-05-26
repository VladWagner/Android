package org.itstep.vpu911.interactionfragmentsportland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// интерфейс, разработанный в ListFragment - для передачи данных в активность
public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentSendDataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } // onCreate

    // реализация интерфейса передачи данных из одного фрагмента в другой
    // через активность
    @Override
    public void onSendData(String selectedItem) {
        // если ориентация горизонтальная, передаем данные фрагменту
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // получаем ссылку на фрагмент-приемник
            DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailFragment);

            fragment.setSelectedItem(selectedItem);
        } else {
            // ориентация вертикальная, вызываем
            // активность, в которой и размещен целевой фрагмент
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.SELECTED_ITEM, selectedItem);
            startActivity(intent);
        } // if
    } // onSendData

    //region Меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mniExit) {
            // возврат из активности
            finish();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity