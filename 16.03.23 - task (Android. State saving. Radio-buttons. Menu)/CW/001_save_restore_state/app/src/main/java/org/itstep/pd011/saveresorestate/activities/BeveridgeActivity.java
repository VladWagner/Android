package org.itstep.pd011.saveresorestate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.itstep.pd011.saveresorestate.MainActivity;
import org.itstep.pd011.saveresorestate.R;
import org.itstep.pd011.saveresorestate.models.Beveridge;

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

        findViewById(R.id.btnBeveridgeActivityBack).setOnClickListener(v -> back());
    } // onCreate

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

    // обработка клика по кнопке выхода из активности
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
} // class BeveridgeActivity