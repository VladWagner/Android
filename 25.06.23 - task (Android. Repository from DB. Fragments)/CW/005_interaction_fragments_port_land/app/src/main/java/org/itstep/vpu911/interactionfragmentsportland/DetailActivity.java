package org.itstep.vpu911.interactionfragmentsportland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    String selectedItem = "Не выбрано";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // при горизонтальной ориентации - выход, для корректного отображения - при
        // помощи MainActivity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        } // if

        setContentView(R.layout.activity_detail);

        // получение даннных для фрагмента
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            selectedItem = extras.getString(SELECTED_ITEM);
    } // onCreate

    // перед началом работы активности, в момент когда фрагмент гарантированно готов к работе,
    // передать данные фрагменту
    @Override
    protected void onResume() {
        super.onResume();
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null)
            fragment.setSelectedItem(selectedItem);
    } // onResume

    //region Меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mniBack) {
            // возврат из активности
            finish();
        } // if

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}