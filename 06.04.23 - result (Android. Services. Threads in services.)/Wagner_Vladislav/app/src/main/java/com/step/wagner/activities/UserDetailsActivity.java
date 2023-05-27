package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.models.user.User;
import com.step.wagner.services.GetByIdService;

public class UserDetailsActivity extends AppCompatActivity {

    //Заголовок
    private TextView userActivityTitle;

    //Имя пользователя
    private TextView userNameTxv;

    //Никнейм пользователя
    private TextView usernameTxv;

    //E-mail пользователя
    private TextView userEmailTxv;

    //Сайт
    private TextView userWebsite;

    //Адрес пользователя
    private TextView userAddressTxv;

    //Телефон пользователя
    private TextView userPhoneTxv;

    //Компания пользователя
    private TextView userCompanyTxv;


    //Приёмник данных из сервиса
    private BroadcastReceiver broadcastReceiver;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userNameTxv = findViewById(R.id.userNameTxv);
        usernameTxv = findViewById(R.id.usernameTxv);
        userEmailTxv = findViewById(R.id.userEmailTxv);
        userWebsite = findViewById(R.id.userWebsite);
        userAddressTxv = findViewById(R.id.userAddress);
        userPhoneTxv = findViewById(R.id.userPhone);
        userCompanyTxv = findViewById(R.id.userCompany);

        findViewById(R.id.btnExit).setOnClickListener(v -> finish());

        Intent intent = getIntent();
        broadcastReceiver = createReceiver();

        int userId = intent.getIntExtra(Parameters.USER_ID_PARAM_NAME, 1);

        //Задать заголовок
        ((TextView) findViewById(R.id.userActivityTitle)).setText(String.format("Пользователь с id: %d", userId));

        //Зарегистрировать получателя
        registerReceiver(broadcastReceiver, new IntentFilter(Parameters.USER_DETAILS_FILTER.value1));

        //Запуск сервиса для запроса
        Intent serviceIntent = new Intent(getApplicationContext(), GetByIdService.class);

        //Задать тип запроса и id
        serviceIntent.putExtra(Parameters.REQUEST_TYPE_PARAM, Parameters.USER_REQUEST_NAME);
        serviceIntent.putExtra("id", userId);

        //Запустить сервис
        startService(serviceIntent);

    }

    //Снять регистрацию
    @Override
    protected void onDestroy() {
        super.onDestroy();


        unregisterReceiver(broadcastReceiver);
    } // onDestroy

    //Создание приёмника
    private BroadcastReceiver createReceiver() {
        return new BroadcastReceiver() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onReceive(Context context, Intent intent) {

                User user = intent.getParcelableExtra(User.class.getCanonicalName());

                if (user == null) {

                    userNameTxv.setText("Имя: не найдено");
                    usernameTxv.setText("Никнейм: не найдено");
                    userEmailTxv.setText("E-mail: не найдено");
                    userWebsite.setText("Сайт: не найдено");
                    userAddressTxv.setText("Адрес: не найдено");
                    userPhoneTxv.setText("Телефон:  не найдено");
                    userCompanyTxv.setText("Компания:  не найдено");

                    return;
                }//if

                //Задать значения в поля

                userNameTxv.setText(String.format("Имя: %s", user.getName()));
                usernameTxv.setText(String.format("Никнейм: %s", user.getUsername()));
                userEmailTxv.setText(String.format("E-mail: %s", user.getEmail()));
                userWebsite.setText(String.format("Сайт: %s", user.getWebsite()));
                userAddressTxv.setText(String.format("Адрес: %s, %s", user.getAddress().city,user.getAddress().street));
                userPhoneTxv.setText(String.format("Телефон: %s", user.getPhone()));
                userCompanyTxv.setText(String.format("Компания: %s", user.getCompany().name));
            }
        };
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion

}