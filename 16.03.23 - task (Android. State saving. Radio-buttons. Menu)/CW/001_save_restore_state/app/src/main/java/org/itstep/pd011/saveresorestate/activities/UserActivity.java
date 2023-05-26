package org.itstep.pd011.saveresorestate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.itstep.pd011.saveresorestate.MainActivity;
import org.itstep.pd011.saveresorestate.R;
import org.itstep.pd011.saveresorestate.models.User;

public class UserActivity extends AppCompatActivity {

    private Button btnBack, btnProcess;
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
        btnBack = findViewById(R.id.btnUserActivityBack);
        btnProcess = findViewById(R.id.btnUserActivityProcess);

        // назначить обработчиков клика по кнопкам
        btnBack.setOnClickListener(v -> returnFromActivity());
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
    private void returnFromActivity() {
        Intent intent = new Intent();
        intent.putExtra(User.class.getCanonicalName(), user);
        setResult(MainActivity.RESULT_OK, intent);

        finish();
    } // returnFromActivity

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
} // class UserActivity