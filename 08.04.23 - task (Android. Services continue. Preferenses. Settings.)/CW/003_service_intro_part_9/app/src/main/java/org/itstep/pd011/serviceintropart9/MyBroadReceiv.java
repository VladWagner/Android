package org.itstep.pd011.serviceintropart9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadReceiv extends BroadcastReceiver {

    final String LOG_TAG = "service_09";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive " + intent.getAction());

        // запуск сервиса
        context.startService(new Intent(context, MyService.class));
    }
}
