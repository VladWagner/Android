package org.itstep.pd011.androiddbstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

// Создаание БД в коде приложения
public class MainActivity extends AppCompatActivity {

    // Вложенный класс для создания/изменения БД
    DBHelper helper;

    EditText edtId, edtName, edtAge;
    private static final String LOG_TAG = "SQLiteStart";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получить поля ввода по идентификаторам
        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);

        // создание БД и првязка ее к текущей активонсоти
        helper = new DBHelper(this);
        Log.d(LOG_TAG, "---- Приложение готово к работе ----");
    } // onCreate


    // Обработчик нажатий на экранные кнопки
    public void onClick(View view) {
        int viewId = view.getId();

        String id;                // для запросов обновления/удаления
        SQLiteDatabase db = null; // объект для доступа к БД
        ContentValues cv = null;  // объект для доступа к данным - строка таблицы
        Cursor c;                 // объект для размещения выборки (прочитанных данных)
        // из базы данных - CURrent Set Of Rows

        // Данные из полей ввода
        String name;
        int age;

        // Выполняем подключение к БД для всех команд, кроме команды "Выход"
        if (viewId != R.id.btnFinish) {
            // создание объекта для доступа к значениям, полученным из базы данных
            cv = new ContentValues();

            // подключение к БД как по записи, так и по чтению
            db = helper.getWritableDatabase();
        } // if

        switch (viewId) {
            // Добавление записи в таблицу
            case R.id.btnAdd:
                // если возраст и/или имя не заданы - не выполяем добавление
                String strAge = edtAge.getText().toString();
                String strName = edtName.getText().toString();

                if (strAge.isEmpty() || strName.isEmpty()) {
                    Toast toast = Toast.makeText(this, "Надо ввести и возраст и имя", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    break;
                } // if

                // добавить данные в формате поле - значение в объект ContentValues
                cv.put("name", strName);
                cv.put("age", Integer.parseInt(strAge));

                // insert() - вставка, возвращает ид вставленной строки
                long rowID = db.insert("Persons", null, cv);
                Log.d(LOG_TAG, "Данные добавлены, ID = " + rowID);
                break;

            // Модификация/редактирование записи по ее идентификатору
            case R.id.btnUpdate:
                // получить ид из строки ввода, строковый формат
                // полностью устраивает, т.к. в запросе update
                // параметры - в строковом формате
                id = edtId.getText().toString();

                // не обрабатываем пустую строку
                if (id.isEmpty()) {
                    Toast toast = Toast.makeText(this, "Надо ввести идентификатор", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    break;
                } // if

                Log.d(LOG_TAG, "---- обновление записей ----");

                // добавить данные в формате поле - значение
                cv.put("name", edtName.getText().toString());
                cv.put("age", edtAge.getText().toString());

                // update - параметры
                // 1 - таблица
                // 2 - данные для обновления
                // 3 - условие для обновления записей, ? - так обозначаем параметр
                // 4 - массив строк - значений параметров условия выборки
                int updCount = db.update("Persons", cv, "_id = ?", new String[] {id});
                Log.d(LOG_TAG, "---- обновлено записей " + updCount + " ----");
                break;

            // Удаление записи по ее идентификатору
            case R.id.btnDelete:
                id = edtId.getText().toString();

                // не обрабатываем пустую строку
                if (id.isEmpty()) {
                    Toast toast = Toast.makeText(this, "Надо ввести идентификатор", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    break;
                } // if

                Log.d(LOG_TAG, "---- удаление записей ----");

                // Параметры
                // 1 - таблица
                // 2 - условие для обновления записей, ? - так обозначаем параметр
                // 3 - массив строк - значений параметров условия выборки
                int delCount = db.delete("Persons", "_id = ?", new String[] {id});
                Log.d(LOG_TAG, "---- удалено записей " + delCount + " ----");
                break;

            // "Вывод" данных - в Log.d()
            case R.id.btnRead:
                Log.d(LOG_TAG, "---- Данные в таблице -----");

                // Построить курсор - специальный объект, коллекция записей,
                // выбранная из таблицы по запросу
                // query - мощный метод для выборки данных, фактически - аналог select
                // в данном примере фактически делается вот что: select * from Persons
                // c = db.query("Persons", null, null, null, null, null, null);

                // Пример применения rawQuery - запрос, возвращающий данные,
                // сам запрос - текст SQLite
                // параметры запроса - обозначаются ?, значения параметров задаются
                // в массиве, порядок параметров - порядок появления в запросе
                // параметры - в строковом формате
                // Параметры rawQuery
                // 1 - SQL-выражение, параметры обозначаем знаком ?
                // 2 - строковый массив параметров для SQL-выражения
                // c = db.rawQuery(
                //        "select * from Persons " +
                //        "where age between ? and ? " +
                //        "order by name", new String[] {"20", "30"});
                c = db.rawQuery("select * from Persons", null);
                // c = db.query("Persons", new String[] {"_id", "name", "age"},
                //         null, null,
                //         null, null, null);

                // Шагнуть на первую запись - возвращается false если данных в таблице нет
                if (c.moveToFirst()) {
                    // Доступ к полям в курсоре (выборке данных) - по индексам полей
                    // в таблице => необходимо получить индекс по имени поля
                    int idIndex = c.getColumnIndex("_id");
                    int nameIndex = c.getColumnIndex("name");
                    int ageIndex = c.getColumnIndex("age");

                    do {
                        Log.d(LOG_TAG, String.format("_id = %d, name = %s, age = %d",
                                c.getInt(idIndex), c.getString(nameIndex), c.getInt(ageIndex))
                        );
                    } while (c.moveToNext());
                } else {
                    Log.d(LOG_TAG, "Строк в таблице: 0");
                } // if
                break;

            // Демо для работы с агрегатными функциями
            case R.id.btnAverage:
                Log.d(LOG_TAG, "---- Агрегатная функция ----");
                // c = db.query("Persons", new String[] {"Avg(age) as average"}, null, null, null, null, null);
                // c = db.query("Persons", new String[] {"Min(age) as min", "Max(age) as max"}, null, null, null, null, null);
                c = db.rawQuery("select Min(age), Max(age) from Persons", null);
                c.moveToFirst();

                // Log.d(LOG_TAG, "Средний возраст: " + c.getDouble(0));
                Log.d(LOG_TAG, "минимальный возраст: " + c.getInt(0) +
                        ", максимальный возраст " + c.getInt(1));
                Log.d(LOG_TAG, "---- Агрегатная функция отработала ----");
                break;

            // Удалить все записи из таблицы
            case R.id.btnClear:
                Log.d(LOG_TAG, "---- Очистка таблицы ----");
                int clearCount = db.delete("Persons", null, null);
                Log.d(LOG_TAG, "удалено строк: " + clearCount);
                break;

            // Выход из приложения
            case R.id.btnFinish:
                // закрытие БД возможно и тут, но тогда не обрабатывается закрытие по
                // кнопкам "Назад", "Домой"
                // helper.close();  // Закрыть БД - рекомендуемый
                // db.close();   // Закрыть БД - достаточный
                Log.d(LOG_TAG, "---- Запрос на финиш приложения ----");
                finish();
                break;
        } // switch
    } // onClick

    // событие вызываемое при закрытии активности, в т.ч. по кнопке "Назад"
    @Override
    protected void onDestroy() {
        super.onDestroy();

        helper.close();  // Закрыть БД - рекомендуемый
        // db.close();   // Закрыть БД - достаточный
        Log.d(LOG_TAG, "---- Закрытие БД ----");
        Log.d(LOG_TAG, "---- Конец работы ----");
    }

    // Вложенный именованный класс
    // Поля и методы класса доступны содержащему классу
    class DBHelper extends SQLiteOpenHelper {

        // Context - ссылка на активность
        public DBHelper(Context context) {
            super(context, "firstDB", null, 1);
        } // DBHelper

        // Создание БД - выполняется однократно!!!
        // Если БД существует, метод не вызывется
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "---- создание БД -----");

            // Создать таблицу, рекомендуемое имя поля первичного ключа _id
            db.execSQL("CREATE TABLE Persons (\n" +
                    "    _id  INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name VARCHAR (60) NOT NULL,\n" +
                    "    age  INTEGER      CHECK (age >= 0) \n" +
                    ");"
            );

            // Начальное заполнение БД --- начало - так можно заполнить
            // любое количество таблиц
            // ContentValues - тип для записи в таблицу
            ContentValues cv = new ContentValues();
            String[] names = {"Варвара", "Евлампия", "Дормидонт", "Полуэкт", "Егорий"};
            int[]    ages  = {23, 45, 32, 43, 35};

            for (int i = 0; i < names.length; i++) {
                // добавить данные в формате поле - значение
                cv.put("name", names[i]);
                cv.put("age", ages[i]);

                // сконструировать и выполнить запрос insert
                db.insert("Persons", null, cv);
            } // for i
            // Начальное заполнение БД --- конец
        } // onCreate

        // Для изменения структуры БД:
        // а) изменение структуры таблиц
        // б) добавление таблиц
        // в) удаление таблиц
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    } // class DBHelper
} // class MainActivity