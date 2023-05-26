package org.itstep.pd011.menudemo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.menudemo.R;
import org.itstep.pd011.menudemo.models.Beveridge;

public class BeveridgeActivity extends AppCompatActivity {
    private Beveridge beveridge;
    private TextView txvBeveridge;
    private Switch swtUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beveridge);

        // получить данные из вызывающей активности
        beveridge = getIntent().getParcelableExtra(Beveridge.class.getCanonicalName());

        txvBeveridge = findViewById(R.id.txvBeveridgeAvtivityInfo);
        txvBeveridge.setText(beveridge.toString());

        // установить переключатель по значению поля объекта beveridge
        swtUse = findViewById(R.id.swtUseTermoIsolation);
        swtUse.setChecked(beveridge.isUseTermoIsolation());
    } // onCreate

    // избыточно в данном случае - т.к. параметры получаем через интент при создании активности
    // и там же выводим параметры в элементы отображения
    //region Сохранение и восстановление контекста при повороте устройства
    // т.е. Stop -> Start
    @Override // сохранение состояния при повороте устройства
    public void onSaveInstanceState(Bundle outState) {
        // Bundle - коллекция пар ключ-значение
        outState.putParcelable(Beveridge.class.getCanonicalName(), beveridge);
        outState.putString("txvBeveridge",  txvBeveridge.getText().toString());

        super.onSaveInstanceState(outState);
    } // onSaveInstanceState

    @Override // Восстановление значений, сохраненных при повороте устройства
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // получение ранее сохраненных параметров
        beveridge = savedInstanceState.getParcelable(Beveridge.class.getCanonicalName());
        txvBeveridge.setText(savedInstanceState.getString("txvBeveridge"));
    } // onRestoreInstanceState
    //endregion

    // обработка клика по пункту меню выхода из активности
    private void back() {
        Intent intent = new Intent();
        intent.putExtra(Beveridge.class.getCanonicalName(), beveridge);

        // для примера вернем код ошибки
        setResult(MainActivity.RESULT_OK, intent);
        finish();
    } // back

    // обработчик клика по switch
    public void onClickUseHandler(View view) {
        Switch swtUse = (Switch)view;

        beveridge.setUseTermoIsolation(swtUse.isChecked());
        txvBeveridge.setText(beveridge.toString());
    } // onClickUseHandler


    // обработчик клика по чек-боксу - один из спсособов задания обработчика,
    // при помощи разметки
    public void onClickCheckBoxHandler(View view) {
        Snackbar snackbar = Snackbar.make(
            view,
            "Чек-бокс " + (((CheckBox) view).isChecked()?"":"не ") + "установлен",
            Snackbar.LENGTH_INDEFINITE);

        // задать кнопку "Закрыть" и обработчик ее клика
        // !! оработчик м.б. пустым !!
        snackbar.setAction("Закрыть", v -> {});
        snackbar.show();
    } // onClickCheckBoxHandler

    // region Работа с главным меню активности
    // обработчик события создани меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // связать разметку с ссылкой на меню
        // getMenuInflater() - загрузчик меню
        // inflate()         - загрузка меню
        getMenuInflater().inflate(R.menu.back_menu, menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    // обработчик события выбора в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // обработка выбора в меню по ид пункта
        if (item.getItemId() == R.id.mniBack) {
            back();
        } // if
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    // endregion


    // перехват нажатия клавиши "назад"
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    } // onBackPressed
} // class BeveridgeActivity