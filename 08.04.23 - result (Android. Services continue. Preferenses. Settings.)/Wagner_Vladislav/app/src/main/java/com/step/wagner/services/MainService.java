package com.step.wagner.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.step.wagner.R;
import com.step.wagner.converters.MessageConverter;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.AppSettings;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.Message;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {

    public List<Message> messages;

    //Задача таймера для исполнения обработок
    private TimerTask timerTask;

    private Timer timer;

    private ServiceBinder binder;

    private NotificationManager notificationManager;
    private String CHANNEL_ID = MainService.class.getCanonicalName();

    //Обработка события привязки
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    //Создание сервиса
    public void onCreate() {
        super.onCreate();

        //Получить колелкцию и объект привязчика
        //Parameters.messageId = 0;
        //messages = Utils.generateMessagesList();
        binder = new ServiceBinder();
        timer = new Timer();
        notificationManager = createNotificationManager();

        //Записать коллекцию в JSON
        //JsonHelper.writeToJson(this,messages,new MessageConverter(), AppSettings.jsonFileName);

    } // onCreate

    @Override
    //Запуск сервиса
    public int onStartCommand(Intent intent, int flags, int startId) {

        messages = intent.getParcelableArrayListExtra(Message.class.getCanonicalName());

        if (messages == null) {
            Parameters.messageId = 0;
            messages = Utils.generateMessagesList();
            JsonHelper.writeToJson(this, messages, new MessageConverter(), AppSettings.jsonFileName);
        }

        count();

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    //Привязчик сервиса
    public class ServiceBinder extends Binder {

        public MainService getService() {
            return MainService.this;
        }

    }//ServiceBinder

    //Обработка по заданию
    private void count() {

        //Если задача исполняется, тогда остановить
        if (timerTask != null)
            timerTask.cancel();

        //Задача с вычислениями
        timerTask = new TimerTask() {
            @Override
            public void run() {

                int textsLength = messages.stream().reduce(0,(sum,message) -> sum + message.getText().length(), Integer::sum);

                //Суммарная длина
                //int textsLength = messages.stream().map(m -> m.getText().length()).reduce(0, Integer::sum);

                //Кол-во вложений
                int attachmentsCount = (int) messages.stream().filter(Message::getAttachment).count();

                //Кол-во сообщений с темами
                int subjectsCount = (int) messages.stream().filter(m -> m.getSubject().length() > 0).count();

                sendNotification(textsLength, attachmentsCount, subjectsCount);
            }
        };

        timer.schedule(timerTask, AppSettings.delay, AppSettings.interval);
    }//count

    //Отправка уведомлений
    @SuppressLint("NotificationPermission")
    private void sendNotification(int lengthSum, int attachmentsCount, int subjectsCount) {

        @SuppressLint("DefaultLocale")
        String notificationMessage = String.format("Длина текстов сообщений: %d\nКол-во сообщений с вложениями: %d" +
                "\nКол-во сообщений с темами: %d", lengthSum, attachmentsCount, subjectsCount);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
                .bigText(notificationMessage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.data_sheet)
                .setContentTitle("Рассчёт параметров сообщений")
                .setStyle(style)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(12, builder.build());
    }

    //Получить менеджер уведомлений
    private NotificationManager createNotificationManager() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            //Регистрация канала уведомлений
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            return manager;
        } else
            return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }//createNotificationManager

    //Остановить имполнение задачи
    public void stopTaskExecution(){
        if (timerTask != null)
            timerTask.cancel();
    }

    //region CRUD
    //Добавление записи в список
    public void addItem(Message message) {
        if (message == null)
            return;

        messages.add(message);

        //Перезаписать коллекцию в JSON
        JsonHelper.writeToJson(this,messages,new MessageConverter(), AppSettings.jsonFileName);
    }


    //Изменение записи в списке
    public void updateItem(Message message, int index) {
        if (message == null)
            return;

        messages.set(index, message);

        //Перезаписать коллекцию в JSON
        JsonHelper.writeToJson(this,messages,new MessageConverter(), AppSettings.jsonFileName);
    }

    //Удаление записи в списке
    public void deleteItem(Message message) {
        if (message == null)
            return;

        messages.remove(message);

        //Перезаписать коллекцию в JSON
        JsonHelper.writeToJson(this,messages,new MessageConverter(), AppSettings.jsonFileName);
    }
    //endregion


}
