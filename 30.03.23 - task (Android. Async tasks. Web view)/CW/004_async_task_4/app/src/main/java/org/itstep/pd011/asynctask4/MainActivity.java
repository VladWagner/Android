package org.itstep.pd011.asynctask4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Демонстрация действий для сохраненеия AsyncTask при смене ориентации экрана
// Идея
// При смене ориентации неизбежно пересоздается активность, но мы обеспечиваем
// работу с единственным экземпляром AsyncTask - просто связываем его с текущим 
// экземпляром активности при ее создании и разрываем эту связь при удалении 
// активности
public class MainActivity extends Activity {
    private MyTask mt;
    public TextView tv;
    public Button btnQuit;

    // параметр запуска задачи - имитируем какую-то бизнес-логику
    int amount;

    // сигнатура для вывода в журнал
    public static final String LOG_TAG = "AT_04";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "создание MainActivity: hashCode = " + this.hashCode());

        // связь с элементами интерфейса
        // строка вывода текста
        tv = findViewById(R.id.tv);

        // начальное значение параметра запуска задачи
        amount = Utils.getRandom(10, 30);

        // кнопка выхода из приложения
        btnQuit = findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(v -> finish());


        // получить ссылку, сохраненную при уничтожении активности
        // (в д.с. при повороте экрана)
        mt = (MyTask) getLastNonConfigurationInstance();  // есть только в Activity
        if (mt == null) {        // если экземпляр класса еще не создан
            mt = new MyTask();   // то создать экземпляр класса
            MyTask mt1 = new MyTask();   // то создать экземпляр класса

            // связь MyTask c MainActivity
            // передаем в MyTask ссылку на текущее MainActivity
            mt.link(this);

            mt.execute(amount);        // запуск задачи м.б. и от кнопки и от других источников...
            Log.d(LOG_TAG, "Создан MyTask: hashCode = " + mt.hashCode());
            Log.d(LOG_TAG, "Создан MyTask: hashCode = " + mt1.hashCode());
        } else {
            // при повторном входе в активность - т.е. при повороте устройства
            // восстанавливаем связь MyTask c MainActivity без запуска задачи
            // (задача уже работает)
            mt.link(this);
            Log.d(LOG_TAG, String.format("Связь с MyTask: hashCode = %d восстановлена", mt.hashCode()));
        } // if

        // в этот момент ссылка на MyTask уже доступна, определяем видимость кнопки Выход
        // кнопка не видна, если задача не имеет состояние FINISHED
        if (!mt.getStatus().equals(AsyncTask.Status.FINISHED)) {
            btnQuit.setVisibility(View.INVISIBLE);
        }
    } // onCreate

    // вызывается при уничтожении активности, в д.с. при повороте экрана
    // сохраняет ссылку на объект mt
    @Override
    public Object onRetainNonConfigurationInstance() {
        // разрываем связь MyTask с MainActivity
        mt.unLink();
        return mt;   // сохранение mt
    } // onRetainNonConfigurationInstance

    // Сохранение данных при создании активности после поворота устройства
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // сохранение данных
        outState.putString("tv", tv.getText().toString());
        outState.putInt("amount", amount);
    } // onSaveInstanceState

    // Восстановление данных при создании активности после поворота устройства
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // восстановление данных
        amount = savedInstanceState.getInt("amount", 0);
    } // onRestoreInstanceState


} // class MainActivity


// Класс для AsyncTask
// при работе с activity проверить ее наличие, чтобы избежать
// NullPointerException
/* static */ class MyTask extends AsyncTask<Integer, Integer, Integer> {

    MainActivity activity;  // ссылка на текущую активность

    // связь с MainActivity - получаем ссылку на MainActivity
    void link(MainActivity activity) { this.activity = activity; } // link

    // разрыв связи с MainActivity -  обнуляем ссылку на MainActivity
    void unLink() { activity = null; }

    @Override
    protected Integer doInBackground(Integer... params) {
        int counter;
        for (counter = 1; counter <= params[0]; counter++) {
            Utils.sleepMs(1_500); // имитация длительной обработки

            publishProgress(counter);  // вывести промежуточный результат
            Log.d(MainActivity.LOG_TAG, String.format("i = %d, MyTask: %d, MainActivity: %d",
                    counter, this.hashCode(), activity != null?activity.hashCode():0));
        } // for counter

        // чтобы увидеть результат последней итерации по счетчику counter
        Utils.sleepMs(1_500);

        return counter;
    } // doInBackground


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        String temp = "counter = " + values[0];

        // вывод в активность только, если эта активность уже создана
        if (activity != null) activity.tv.setText(temp);
        Log.d(MainActivity.LOG_TAG, temp );
    } // onProgressUpdate


    @Override
    protected void onPostExecute(Integer counter) {
        super.onPostExecute(counter);

        String temp = String.format("Задача выполнена, counter = %d", counter);
        Log.d(MainActivity.LOG_TAG, temp );

        // вывод в активность только, если эта активность уже создана
        if (activity == null) return;

        // формируем итоговое сообщение
        activity.tv.setText(temp);
        activity.btnQuit.setVisibility(View.VISIBLE);
    } // onPostExecute
} // class MyTask
