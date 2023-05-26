package org.itstep.pd011.saveresorestate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.pd011.saveresorestate.activities.BeveridgeActivity;
import org.itstep.pd011.saveresorestate.activities.UserActivity;
import org.itstep.pd011.saveresorestate.models.Beveridge;
import org.itstep.pd011.saveresorestate.models.User;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // коды активностей для вызова
    public static final int USER_ACTIVITY = 1010, BEVERIGE_ACTIVITY = 1020;

    // коды завершения работы активности
    public static final int RESULT_OK = 0, RESUL_ERR = 1;

    private static Random random = new Random();

    // данные для обработки
    private User user;
    private Beveridge beveridge;

    private Button btnUser, btnCreateUser, btnBeveridge, btnCreateBeveridge;
    private TextView txvUser, txvBeverige;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user      = new User();
        beveridge = new Beveridge();

        // получить ссылки на элементы интерфейса
        txvUser = findViewById(R.id.txvUser);
        txvBeverige = findViewById(R.id.txvBeveridge);

        btnUser = findViewById(R.id.btnUser);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnBeveridge = findViewById(R.id.btnBeverige);
        btnCreateBeveridge = findViewById(R.id.btnCreateBeverige);

        btnUser.setOnClickListener(v -> startUserActivity());
        btnCreateUser.setOnClickListener(v -> createUser());
        btnBeveridge.setOnClickListener(v -> startBeveridgeActivity());
        btnCreateBeveridge.setOnClickListener(v -> createBeveridge());
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
    } // onCreate


    //region Сохранение и восстановление контекста при повороте устройства
    // т.е. Stop -> Start
    @Override // сохранение состояния при повороте устройства
    public void onSaveInstanceState(Bundle outState) {
        // Bundle - коллекция пар ключ-значение
        // необходимо сохранить объекты данных
        outState.putParcelable(User.class.getCanonicalName(), user);
        outState.putParcelable(Beveridge.class.getCanonicalName(), beveridge);

        // для примера сохраним и TextView - мы можем их просто вывести при восстановлении
        // однако есть элементы интерфейса, которые сами умеют сохранять свое состояние
        outState.putString("txvUser", txvUser.getText().toString());
        outState.putString("txvBeverige", txvBeverige.getText().toString());

        // !! очень важно !!
        super.onSaveInstanceState(outState);
    } // onSaveInstanceState


    @Override // Восстановление значений, сохраненных при повороте устройства
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // !! очень важно !!
        super.onRestoreInstanceState(savedInstanceState);

        // получение ранее сохраненных параметров
        user = savedInstanceState.getParcelable(User.class.getCanonicalName());
        beveridge = savedInstanceState.getParcelable(Beveridge.class.getCanonicalName());

        txvUser.setText(savedInstanceState.getString("txvUser"));
        txvBeverige.setText(savedInstanceState.getString("txvBeverige"));
    } // onRestoreInstanceState
    //endregion

    private void createUser() {
        user = new User("Романов Н.А.", random.nextInt(100), random.nextInt(20_000));
        txvUser.setText(user.toString());
    } // createUser

    private void createBeveridge() {
        beveridge = new Beveridge("кофе американо", random.nextInt(250),
                random.nextInt(80), random.nextBoolean());
        txvBeverige.setText(beveridge.toString());
    } // createBeveridge

    // обработчик клика по кнопке вызова активности для User
    private void startUserActivity() {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(User.class.getCanonicalName(), user);
        startActivityForResult(intent, USER_ACTIVITY);
    } // startUserActivity

    // обработчик клика по кнопке вызова активности для Beverige
    private void startBeveridgeActivity() {
        Intent intent = new Intent(this, BeveridgeActivity.class);
        intent.putExtra(Beveridge.class.getCanonicalName(), beveridge);
        startActivityForResult(intent, BEVERIGE_ACTIVITY);
    } // startBeveridgeActivity

    // обработчик события получения данных из другой активности
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                String.format("Ошибка %d в активности %d", resultCode, requestCode),
                Toast.LENGTH_LONG)
                .show();
            return;
        } // if

        // при успешной работе активностей - принять данные, вывести в TextView
        switch (requestCode) {
            case USER_ACTIVITY:
                user = data.getParcelableExtra(User.class.getCanonicalName());
                String text = txvUser.getText() + "\n" + user.toString();
                txvUser.setText(text);
                break;
            case BEVERIGE_ACTIVITY:
                beveridge = data.getParcelableExtra(Beveridge.class.getCanonicalName());
                txvBeverige.setText(beveridge.toString());
                break;
        } // switch

    } // onActivityResult
} // class MainActivity