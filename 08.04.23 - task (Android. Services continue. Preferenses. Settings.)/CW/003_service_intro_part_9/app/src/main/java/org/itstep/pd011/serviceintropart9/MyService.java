package org.itstep.pd011.serviceintropart9;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    final String LOG_TAG = "service_09";

    // ссылка на менеджер уведомлений
    NotificationManager nm;
    final String CHANNEL_ID = MyService.class.getCanonicalName();

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");

        // получить ссылку на менеджер уведомлений
        // https://developer.android.com/training/notify-user/build-notification#java
        nm = createNotificationChannel();
    } // onCreate

    // в этом примере возвращаем объект-заглушку, т.к.
    // пока что этот объект не будет использован
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "MyService onBind");
        return new Binder();
    } // onBind

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService Полезная работа сервиса");

        // запуск задачи с задержкой 1000 мс и периодом interval мс
        new Timer().schedule(new TimerTask() {
            public void run() {
                Log.d(LOG_TAG, "run - полезное действие задачи TimerTask");
                sendNotify(12);  // вызываем метод для запуска уведомления
            }
        }, 3_000, 3_000);

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    // создание канала для уведомлений
    private NotificationManager createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            return notificationManager;
        } else
            // для версий меньше 8.0+
            return (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // запуск/отправка уведомления
    void sendNotify(int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.submarine)
            .setContentTitle("Заголовок")
            .setContentText(String.format(Locale.UK, "Текст уведомления %d", id))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        nm.notify(id, builder.build());

        Log.d(LOG_TAG, "уведомление отправлено");
    } // sendNotif


    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, "MyService onRebind");
    } // onRebind

    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "MyService onUnbind");
        return super.onUnbind(intent);
    } // onUnbind

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
    } // onDestroy
}