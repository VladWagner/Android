package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.wagner.R;
import com.step.wagner.adapters.MessageAdapter;
import com.step.wagner.fragments.ContainerFragment;
import com.step.wagner.fragments.dialogues.MessageDialog;
import com.step.wagner.infrastructure.AppSettings;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.Message;
import com.step.wagner.services.MainService;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ServiceActivity extends AppCompatActivity {

    //Фрагмент с выводом recycler view
    private ContainerFragment containerFragment;

    //Intent для запуска/остановки сервиса
    private Intent intent;

    //Отслеживание подключения
    private ServiceConnection serviceConnection;

    //Сервис
    private MainService service;

    //Запуск/остановка сервиса
    private Button btnStartService;
    private Button btnStopService;

    private View currentView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        currentView = this.findViewById(android.R.id.content);

        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);

        containerFragment = (ContainerFragment) getSupportFragmentManager().findFragmentById(R.id.resultFragment);
        containerFragment.getTextView().setVisibility(View.GONE);

        btnStartService.setOnClickListener(v -> startService());
        btnStopService.setOnClickListener(v -> stopService());

        findViewById(R.id.btn_add).setOnClickListener(v -> addMessage());



        //Обработчик подключения/отключения от сервиса
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder binder) {
                service = ((MainService.ServiceBinder) binder).getService();

                containerFragment.getRecyclerView().setAdapter(new MessageAdapter(ServiceActivity.this,
                        service.messages, service, getSupportFragmentManager()));

                Utils.showToast(getApplicationContext(), "Привязка к сервису произошла");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        intent = new Intent(getApplicationContext(), MainService.class);

        //Запустить сервис
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        btnStartService.setEnabled(false);


    }

    //Запустить сервис
    private void startService() {

        //Если сервис ранее запускался, тогда перегдать коллекцию
        if (service != null)
            intent.putParcelableArrayListExtra(Message.class.getCanonicalName(),(ArrayList<? extends Parcelable>) service.messages);

        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        btnStopService.setEnabled(true);
        btnStartService.setEnabled(false);
    }

    //Остановить сервис
    private void stopService() {
        stopService(intent);
        unbindService(serviceConnection);

        //Остановить задачу
        service.stopTaskExecution();

        btnStopService.setEnabled(false);
        btnStartService.setEnabled(true);
    }

    //Добавление записи
    @SuppressLint("DefaultLocale")
    private void addMessage() {
        Bundle bundle = new Bundle();

        //Задать режим работы диалога
        bundle.putBoolean("isEditing", false);

        MessageDialog messageDialog = new MessageDialog();

        messageDialog.setArguments(bundle);

        //Задать обработчик клика по кнопке в диалоге
        messageDialog.setListener(params -> {
            if (params[0] == null) {
                Utils.showSnackBar(currentView, "Не удалось добавить сообщение ");
                return;
            }

            service.addItem(params[0]);

            containerFragment.getRecyclerView().getAdapter().notifyDataSetChanged();

            Utils.showSnackBar(currentView, String.format("Сообщение с id: %d успешно добавлено", params[0].getId()));

        });

        //Вывести диалог
        messageDialog.show(getSupportFragmentManager(), "message_dialog");
    }

    //Обработчик клика по элементу в адаптере
    private Consumer<Integer> createOnClickHandler() {
        return new Consumer<Integer>() {
            @Override
            public void accept(Integer index) {
                Message message = service.messages.get(index);

                Bundle bundle = new Bundle();

                bundle.putParcelable("message", message);
                bundle.putBoolean("isEditing", true);

                MessageDialog messageDialog = new MessageDialog();

                messageDialog.setArguments(bundle);

                messageDialog.setListener(params -> {

                });

                //Вывести диалог
                messageDialog.show(getSupportFragmentManager(), "message_dialog");
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

        switch (item.getItemId()){

            case R.id.btnSettings:
                startActivity(new Intent(this,SettingsActivity.class));
            case R.id.btnExitFromActivity:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

}