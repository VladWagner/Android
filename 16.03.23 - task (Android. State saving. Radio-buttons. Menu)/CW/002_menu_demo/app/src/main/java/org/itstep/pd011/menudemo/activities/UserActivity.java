package org.itstep.pd011.menudemo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.itstep.pd011.menudemo.R;
import org.itstep.pd011.menudemo.models.User;

public class UserActivity extends AppCompatActivity {

    private Button btnProcess;
    private TextView txvUser;

    private User user;

    // приращение возраста
    private int incAge;

    // выбранное значение оклада
    private int salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // получить параметр из вызывающей активности
        Intent intent = getIntent();
        user = intent.getParcelableExtra(User.class.getCanonicalName());

        // получить ссылки на элементы управления
        txvUser = findViewById(R.id.txvUserActivityInfo);
        btnProcess = findViewById(R.id.btnUserActivityProcess);

        // назначить обработчиков клика по кнопке
        btnProcess.setOnClickListener(v -> processUser());

        // вывести полученную информацию в поле выводв
        txvUser.setText(user.toString());

        // установить incAge и соответсвующую радиокнопку
        incAge = 1;
        ((RadioButton)findViewById(R.id.rbtAgeInc2)).setChecked(true);

        // установка обработчика события назначения оклада по клику на РК 2й РГ
        ((RadioGroup)findViewById(R.id.rgrSalary))
                .setOnCheckedChangeListener((group, checkedId) -> onSalaryRBchanged(group, checkedId));
    } // onCreate

    // возврат из активности
    private void back() {
        Intent intent = new Intent();
        intent.putExtra(User.class.getCanonicalName(), user);
        setResult(MainActivity.RESULT_OK, intent);

        finish();
    } // back

    // обработка пользователя
    private void processUser() {
        user.setAge(user.getAge()+incAge);
        if (salary != 0) user.setSalary(salary);
        txvUser.setText(user.toString());
    } // processUser

    // обработчик клика по радиокнопке из группы приращения возраста
    public void onClickAgeIncHandler(View view) {
        RadioButton rb = (RadioButton) view;

        // так можно выяснить отмечен или не отмечен RadioButton
        // boolean checked = rb.isChecked();

        switch (rb.getId()) {
            case R.id.rbtAgeInc1:
                incAge = -5;
                break;
            case R.id.rbtAgeInc2:
                incAge = 1;
                break;
            case R.id.rbtAgeInc3:
                incAge = 5;
                break;
        } // switch
    } // onClickAgeIncHandler

    // обработка клика по радиокнопке во 2й группе
    private void onSalaryRBchanged(RadioGroup group, int id) {
        switch (id) {
            case R.id.rbtSalary1:
                salary = 25_000;
                break;
            case R.id.rbtSalary2:
                salary = 42_000;
                break;
            case R.id.rbtSalary3:
                salary = 65_000;
        } // salary

        // обработка при изменении значения радиокнопки
        user.setSalary(salary);
        txvUser.setText(user.toString());
    } // onSalaryRBchanged

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
        switch (item.getItemId()) {
            case R.id.mniBack:
                back();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    // endregion
} // class UserActivity