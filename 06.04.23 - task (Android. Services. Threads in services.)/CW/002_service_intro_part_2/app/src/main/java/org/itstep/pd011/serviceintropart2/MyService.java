package org.itstep.pd011.serviceintropart2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    final String LOG_TAG = "service_02";

    // класс для запуска сервиса в отдельном потоке/отдельных потоках
    // из пула потоков
    ExecutorService es;

    // вспомогательный объект для демонстрации конфликта по ресурсам
    Object someRes;

    // событие при создании сервиса
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService - onCreate");

        // Executors - класс, который умеет выполнять потоки в пуле потоков,
        //             размер пула фиксирован
        // параметр вызова - количество одновременно работающих потоков, начнем
        //             с 1 при повторном запуске укажем 3
        es = Executors.newFixedThreadPool(3);
        someRes = new Object();  // создание объекта - модель какого-либо ресурса
    } // onCreate


    // При уничтожении сервиса - уничтожим также ресурс
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService - onDestroy");

        // ресурс удаляется при завершении первого же потока
        // при условии, что в пуле стартуют все потоки параллельно
        someRes = null; // важно для демонстрации конфликтов по ресурсам
    } // onDestroy


    // intent  - параметры запуска
    // flags   - флаги запуска (пока не рассматриваем)
    // startId - идентификатор сервиса - фактически это счетчик вызовов
    public int onStartCommand(Intent intent, int flags, int startId) {
        // получить параметры для запуска службы
        Log.d(LOG_TAG, "MyService - onStartCommand");

        int time = intent.getIntExtra(MainActivity.SERVICE_DATA, 1);
        SomeClass someClass = intent.getParcelableExtra(SomeClass.class.getCanonicalName());

        // запуск службы в пуле потоков
        MyRun mr = new MyRun(time, someClass, startId);
        es.execute(mr);

        // пока просто возвращаем значение от базового метода
        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand


    // должен обязательно переопределяться, будем использовать позже
    // вызывается при закреплении клиента за сервисом с помощью метода
    // bindService()
    public IBinder onBind(Intent intent) {
        return null;
    } // onBind


    // Внутренний класс, реализующий полезную работу службы
    class MyRun implements Runnable {
        // параметры сервиса
        int time;     // время задержки
        int startId;  // идентификатор экземпляра сервиса
        SomeClass someClass;

        public MyRun(int time, SomeClass someClass, int startId) {
            this.time = time;
            this.someClass = someClass;
            this.startId = startId;
            Log.d(LOG_TAG, String.format("MyRun#%d создан. Получены данные: %d, %S",
                    startId, time, someClass));
        } // MyRun

        // исполняемая часть сервиса
        public void run() {
            Log.d(LOG_TAG, String.format("MyRun#%d запущен, time = %d", startId, time));
            Utils.sleep(time);

            // по окончании работы сервиса - попытка доступа к ресурсу someRes
            try {
                Log.d(LOG_TAG, String.format("MyRun#%d someRes.class = %s", startId, someRes.getClass()));
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, String.format("MyRun#%d ошибка, null pointer, ресурс уже освобожден", startId));
            }  // try-catch

            stop(); // завершение сервиса - для удобства упакована в отдельный метод
        } // run

        // Оболочка для удобного вызова методов завершения сервиса stopSelf(), stopSelfResult()
        // Сервис останавливается, когда последний запущенный (а не последний обработанный) вызов
        // выполняет метод  stopSelf(startId). При этом могут продолжать работать ранее
        // запущенные вызовы.
        void stop() {
            // stopSelf(startId);  // останавливает сервис с идентификатором startId
            // Log.d(LOG_TAG, String.format("MyRun#%1$d завершен, stopSelf(%1$d)", startId));

            // результат метода stopSelfResult() - true, если сервис был завершен методом
            // и false - если сервис не был завершен,
            // формат % N$ T
            // % - начало формата, N$ - порядковый номер в списке значений, T - тип значения
            boolean result = stopSelfResult(startId);
            Log.d(LOG_TAG, String.format("MyRun#%1$d завершен, stopSelfResult(%1$d) = %2$b", startId, result));
        } // stop
    } // class MyRun
} // class MyService