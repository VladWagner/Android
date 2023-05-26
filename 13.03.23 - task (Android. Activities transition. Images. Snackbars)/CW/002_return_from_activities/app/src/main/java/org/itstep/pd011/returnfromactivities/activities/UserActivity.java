package org.itstep.pd011.returnfromactivities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.itstep.pd011.returnfromactivities.MainActivity;
import org.itstep.pd011.returnfromactivities.R;
import org.itstep.pd011.returnfromactivities.models.User;

// активность, возвращающая в вызывающую активность данные
public class UserActivity extends AppCompatActivity {

    Button btnBack, btnProcess;
    TextView txvUser;

    User user;  // данные для обработки

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
    } // onCreate

    // возврат из активности
    private void returnFromActivity() {
        Intent intent = new Intent();
        // данные, возвращаемые из активности
        intent.putExtra(User.class.getCanonicalName(), user);

        // установить результат работы активности
        setResult(MainActivity.RESULT_OK, intent);

        // завершить активность
        finish();
    } // returnFromActivity

    // обработка пользователя
    private void processUser() {
        user.setAge(user.getAge()+1);
        user.setSalary(user.getSalary()+1000);
        txvUser.setText(user.toString());
    } // processUser
}