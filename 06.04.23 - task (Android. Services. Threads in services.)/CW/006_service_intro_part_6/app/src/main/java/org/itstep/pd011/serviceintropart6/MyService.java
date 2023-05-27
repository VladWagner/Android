package org.itstep.pd011.serviceintropart6;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

// Notifications - это уведомления, которые пользователь видит в верхней части экрана,
// когда ему приходит новое письмо, сообщение, обновление и т.п
public class MyService extends Service {
    final String TAG = "service 06";

    // ссылка на менеджер уведомлений
    NotificationManager nm;

    final String CHANNEL_ID = "rg.itstep.pd011.serviceintropart6";

    @Override
    public void onCreate() {
        super.onCreate();

        // получить ссылку на менеджер уведомлений
        // https://developer.android.com/training/notify-user/build-notification#java
        nm = createNotificationChannel();
    } // onCreate

    // при запуске сервиса
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "сервис работает");

        // спим 2 с - имитация полезного действия
        // Utils.sleep(2);
        sendNotify(startId+1);  // вызываем наш метод для запуска уведомления

        Utils.sleep(2);
        sendNotify(startId+2);  // вызываем метод для запуска уведомления
        Log.d(TAG, "сервис выполнил полезную работу");

        Utils.sleep(2);
        String longText = "To have a notification appear in an expanded view, " +
                "first create a NotificationCompat.Builder object " +
                "with the normal view options you want. " +
                "Next, call Builder.setStyle() with an " +
                "expanded layout object as its argument.";
        sendNotifyLongText(startId+3, longText);

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    // создание канала для уведомлений
    private NotificationManager createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // для версий  8.0+
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
        // создание уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.submarine)
            .setContentTitle("Заголовок, id="+id)
            .setContentText("Текст уведомления")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // отправка уведомления
        nm.notify(id, builder.build());

        Log.d(TAG, "уведомление отправлено");
    } // sendNotify


    // отправка уведомления c длинным текстом
    void sendNotifyLongText(int id, String longText) {
        // настройка уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.submarine)
            .setContentTitle("Title, id = " + id)
            .setContentText("Notification text")
            .setStyle(new NotificationCompat.BigTextStyle().bigText(longText));

        // создать и отправить уведомление
        nm.notify(id, builder.build());
    }

    // обязателен для переопределения
    public IBinder onBind(Intent arg0) { return null; }
} // class MyService