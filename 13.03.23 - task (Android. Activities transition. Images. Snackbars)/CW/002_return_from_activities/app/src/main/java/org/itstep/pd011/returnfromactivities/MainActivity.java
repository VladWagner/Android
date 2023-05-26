package org.itstep.pd011.returnfromactivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.pd011.returnfromactivities.activities.BeveridgeActivity;
import org.itstep.pd011.returnfromactivities.activities.UserActivity;
import org.itstep.pd011.returnfromactivities.models.Beveridge;
import org.itstep.pd011.returnfromactivities.models.User;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // коды активностей для вызова
    public static final int ID_USER_ACTIVITY = 1010, ID_BEVERIGE_ACTIVITY = 1020;

    // коды завершения работы активности
    public static final int RESULT_OK = 0, RESULT_ERR = 1;

    private static Random random = new Random();

    private User user;
    private Beveridge beveridge;

    private Button btnUser, btnCreateUser, btnBeveridge, btnCreateBeveridge;
    private TextView txvUser, txvBeverige;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // данные для обработки
        user      = new User();
        beveridge = new Beveridge();

        // получить ссылки на элементы интерфейса
        txvUser = findViewById(R.id.txvUser);
        txvBeverige = findViewById(R.id.txvBeveridge);

        btnUser            = findViewById(R.id.btnUser);
        btnCreateUser      = findViewById(R.id.btnCreateUser);
        btnBeveridge       = findViewById(R.id.btnBeverige);
        btnCreateBeveridge = findViewById(R.id.btnCreateBeverige);

        btnUser.setOnClickListener(v -> startUserActivity());
        btnCreateUser.setOnClickListener(v -> createUser());
        btnBeveridge.setOnClickListener(v -> startBeveridgeActivity());
        btnCreateBeveridge.setOnClickListener(v -> createBeveridge());

        // выход
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
    } // onCreate

    // создание пользователя
    private void createUser() {
        user = new User("Романов Н.А.", random.nextInt(100),
                random.nextInt(200)*100);
        txvUser.setText(user.toString());
    } // createUser

    // создание напитка
    private void createBeveridge() {
        beveridge = new Beveridge("кофе американо",
                random.nextInt(250),
                random.nextInt(23)*10);
        txvBeverige.setText(beveridge.toString());
    } // createBeveridge

    // обработчик клика по кнопке вызова активности для User
    private void startUserActivity() {
        Intent intent = new Intent(this, UserActivity.class);
        // передача параметра в активность
        intent.putExtra(User.class.getCanonicalName(), user);

        // запуск активности с возвратом результата
        startActivityForResult(intent, ID_USER_ACTIVITY);
    } // startUserActivity

    // обработчик клика по кнопке вызова активности для Beverige
    private void startBeveridgeActivity() {
        Intent intent = new Intent(this, BeveridgeActivity.class);

        // передача параметра в активность
        intent.putExtra(Beveridge.class.getCanonicalName(), beveridge);

        // запуск активнсти с возвратом результата
        startActivityForResult(intent, ID_BEVERIGE_ACTIVITY);
    } // startBeveridgeActivity


    // обработчик события получения данных из другой активности
    // (функция обратного вызова)
    // requestCode - идентфикатор активности
    // resultCode - код завершения работы в активности
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                            String.format(Locale.UK, "Ошибка %d в активности с кодом %d", resultCode, requestCode),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        } // if

        // при успешной работе активностей - принять данные, вывести в TextView
        switch (requestCode) {
            case ID_USER_ACTIVITY:
                user = data.getParcelableExtra(User.class.getCanonicalName());
                txvUser.setText(user.toString());
                break;
            case ID_BEVERIGE_ACTIVITY:
                beveridge = data.getParcelableExtra(Beveridge.class.getCanonicalName());
                txvBeverige.setText(beveridge.toString());
                break;
        } // switch

    } // onActivityResult
} // class MainActivity