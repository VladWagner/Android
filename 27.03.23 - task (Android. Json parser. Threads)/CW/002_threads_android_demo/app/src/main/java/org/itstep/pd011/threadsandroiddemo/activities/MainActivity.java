package org.itstep.pd011.threadsandroiddemo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.itstep.pd011.threadsandroiddemo.R;
import org.itstep.pd011.threadsandroiddemo.utils.Utils;

/*
 * Когда мы запускаем приложение на Android, система создает поток, который называется
 * основным потоком приложения или UI-поток. Этот поток обрабатывает все изменения и
 * события пользовательского интерфейса.
 * Однако для вспомогательных операций, таких как отправка или загрузка файла, продолжительные
 * вычисления и т.д., мы можем создавать дополнительные потоки.
 *
 * Для создания новых потоков нам доcтупен стандартный функционал класса Thread из базовой
 * библиотеки Java из пакета java.util.concurrent, которые особой трудности не представляют.
 *
 * Тем не менее трудности могут возникнуть при обновлении визуального интерфейса из потока.
 * */
public class MainActivity extends AppCompatActivity {

    private TextView txvOutput;
    private Button btnWrongThread;           // кнопка запуска "неправильного" потока
    private Button btnCorrectThread;         // кнопка запуска "правильного" потока
    private Button btnCorrectDelayedThread;  // кнопка запуска "правильного" потока с задержкой

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получим ссылку на элемент вывода
        txvOutput = findViewById(R.id.txvOutput);
        txvOutput.setText(Utils.currentTimeToString());

        // получить ссылки на объекты разметки
        btnWrongThread = findViewById(R.id.btnWrongThread);
        btnCorrectThread = findViewById(R.id.btnCorrectThread);

        // запуск потока с ошибкой - неправильным взаимодействием с элементами интерфейса
        // приложение должно упасть при клике на эту кнопку
        // btnWrongThread.setOnClickListener(v -> new Thread(runnable1).start());

        // runOnUiThread() - запуск в потоке UI (User Interface), замораживание интерфейса...
        btnWrongThread.setOnClickListener(v -> new Thread(() -> runOnUiThread(runnable1)).start());

        // запуск потока без ошибки - с правильным взаимодействием с элементами интерфейса
        btnCorrectThread.setOnClickListener(v -> new Thread(runnable2).start());

        // запуск потока без ошибки - с правильным взаимодействием с элементами интерфейса,
        // задержкой запуска задачи вывода
        btnCorrectDelayedThread = findViewById(R.id.btnCorrectDelayedThread);
        btnCorrectDelayedThread.setOnClickListener(v -> new Thread(runnable3).start());
    } // onCreate

    // Демонстрация работы с потоком с ошибкой, неправильным взаимодействием
    // с элементами интерфейса
    private Runnable runnable1 = () -> {
        // запретить кнопку запуска потока
        btnWrongThread.setEnabled(false);

        // получаем текущее время в строковом формате
        String strTime = Utils.currentTimeToString();

        // отображаем в текстовом поле
        txvOutput.setText(strTime);

        // пауза...
        Utils.sleep(5_000);
        btnWrongThread.setEnabled(true);

        strTime = Utils.currentTimeToString();

        // отображаем в текстовом поле
        txvOutput.setText(strTime);
    };

    // Демонстрация работы с потоком без ошибки, с правильным взаимодействием
    // с элементами интерфейса - при помощи метода post() элемента графического
    // интерфейса
    private Runnable runnable2 = () -> {
        // запретить кнопку запуска потока - с использованием метода post()
        btnCorrectThread.post(() -> {
            btnCorrectThread.setEnabled(false);                // запрет кнопки
            btnCorrectThread.setText("Поток выполняется...");  // смена текста на кнопке
        });

        // для удобства восприятия
        Utils.sleep(2_000);

        // получаем текущее время в строковом формате
        String strTime = Utils.currentTimeToString();

        // для удобства восприятия
        Utils.sleep(2_000);

        // !! для TextView не обязательно
        // отображаем в текстовом поле с использованием метода post() для взаимодействия
        // во вторичном потоке с элементами графического интерфейса - :
        txvOutput.post(() -> txvOutput.setText(strTime));
        // txvOutput.setText(strTime);   // так тоже работает, к сожалению...

        // разрешить кнопку запуска потока - с использованием метода post()
        btnCorrectThread.post(() -> {
            btnCorrectThread.setEnabled(true);
            btnCorrectThread.setText(R.string.btn_correct);  // восстановление текста из ресурсов
        });
    };

    // Демонстрация работы с потоком без ошибки, с правильным взаимодействием
    // с элементами интерфейса - при помощи метода postDelayed() элемента графического
    // интерфейса, запуск обмена с задержкой
    private Runnable runnable3 = () -> {
        // для удобства работы выведем текущее время в начале работы потока, тогда время
        // после задержки воспринимается легче, виден интервал срабатывания
        txvOutput.post(() -> {
            txvOutput.setText(Utils.currentTimeToString());
            txvOutput.setTextColor(Color.RED);
        });

        // запретить кнопку на время работы потока
        btnCorrectDelayedThread.post(() -> btnCorrectDelayedThread.setEnabled(false));

        // отображаем в текстовом поле с использованием метода postDelayed() для взаимодействия
        // во вторичном потоке с элементами графического интерфейса - с задержкой
        // запуска в 3000 мс
        txvOutput.postDelayed(() -> {
            // получаем текущее время в строковом формате
            String strTime = Utils.currentTimeToString();

            txvOutput.setText(strTime + ", воть...");
            txvOutput.setTextColor(Color.BLUE);
            btnCorrectDelayedThread.setEnabled(true);
        }, 3_000);
    };

    //region Меню приложения
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            // выход из приложения
            case R.id.mniExit:
                finish();
                break;

            // вызов активности, демонстрирующей потоки и фрагменты
            case R.id.mniFragmentThread:
                // примитивный вызов активности :(
                startActivity(new Intent(this, ThreadFragmentActivity.class));
                break;

            // вызов активности работы с потоками, файлом
            case R.id.mniFragmentFileThread:
                // примитивный вызов активности :(
                startActivity(new Intent(this, FileThreadActivity.class));
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity