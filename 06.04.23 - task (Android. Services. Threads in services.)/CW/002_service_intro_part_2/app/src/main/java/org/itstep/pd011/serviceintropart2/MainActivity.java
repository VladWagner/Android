package org.itstep.pd011.serviceintropart2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // имя передаваемого в сервис параметра
    public static final String SERVICE_DATA = "duration";

    // вывод инфррмации по работе приложения
    private TextView txvInfo;

    /*
     *  передача данных в сервис
     *  передаем int, SomeClass
     *  методы остановки сервиса stopSelf и stopSelfResult
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvInfo = findViewById(R.id.txvInfo);
        txvInfo.setText("Старт приложения: ок");
    } // onCreate


    // запускаем сервисы
    public void onClickStart(View v) {
        String className = SomeClass.class.getCanonicalName();

        final int par1 = 3, par2 = 7, par3 = 2;
        txvInfo.setText(String.format("Сервисы запущены с параметрами %d, %d, %d", par1, par2, par3));
        Intent intent = new Intent(this, MyService.class);

        // длинный формат вызова для передачи данных в сервис и запуска сервиса
        intent.putExtra(SERVICE_DATA, par1);  // длительность работы 3 с
        intent.putExtra(className, new SomeClass(42, "Тамара"));
        startService(intent);

        // запускаем еще два экземпляра сервиса
        intent.putExtra(SERVICE_DATA, par2);
        intent.putExtra(className, new SomeClass(18, "Екатерина"));
        startService(intent);  // длительность работы 7 с

        intent.putExtra(SERVICE_DATA, par3);
        intent.putExtra(className, new SomeClass(81, "Всеволод"));
        startService(intent);  // длительность работы 2 с
    } // onClickStart
} // class MainActivity