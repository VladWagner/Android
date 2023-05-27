package com.step.wagner.sensors.repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.step.wagner.sensors.infrastructure.Parameters;
import com.step.wagner.sensors.models.entities.Accelerometer;

import java.util.List;

public class AccelerometersRepository extends BaseRepository<Accelerometer> {

    public AccelerometersRepository(Context context) {

        super(context, Parameters.ACCELEROMETERS);

        //Задать строки для сырых запросов
        getAllQuery = "select "
        + Parameters.ID_FIELD
        + " , axis_X"
        + " , axis_Y"
        + " , axis_Z"
        + " from accelerometers";

        //Запрос для получения элемента по id
        getByIdQuery = String.format("%s where accelerometers.%s = ?", getAllQuery,Parameters.ID_FIELD);
    }


    //Получение объекта из курсора
    @Override
    Accelerometer readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
        int axis_X_index = cursor.getColumnIndex("axis_X");
        int axis_Y_index = cursor.getColumnIndex("axis_Y");
        int axis_Z_index = cursor.getColumnIndex("axis_Z");


        //Получение значений
        int id = cursor.getInt(idIndex);
        float axisX = cursor.getFloat(axis_X_index);
        float axisY = cursor.getFloat(axis_Y_index);
        float axisZ = cursor.getFloat(axis_Z_index);


        //Создать объект
        return new Accelerometer(id,axisX,axisY,axisZ);
    }

    //Добавление
    public long insert(Accelerometer accelerometer){

        ContentValues cv = new ContentValues();

        cv.put("axis_X", accelerometer.getAxisX());
        cv.put("axis_Y", accelerometer.getAxisY());
        cv.put("axis_Z", accelerometer.getAxisZ());

        return db.insert(tableName, null, cv);
    } // insert

    //Изменение
    @SuppressLint("DefaultLocale")
    public void insertAll(List<Accelerometer> accelerometers){

        StringBuilder querySb = new StringBuilder("insert into accelerometers (axis_X, axis_Y, axis_Z) values");


        for (int i = 0; i < accelerometers.size(); i++) {
            Accelerometer accel = accelerometers.get(i);

            //Задать значения осей и если элемент последний, то задать ; в конце
            querySb.append(String.format("(%.3f,%.3f,%.3f)%s",
                    accel.getAxisX(),accel.getAxisY(),accel.getAxisZ(),
                    i < accelerometers.size()-1 ? "," : ";"));
        }

        db.execSQL(querySb.toString());
    } // update

    //Очистить таблицу
    public void cleanAll(){

        //Удалить все записи
        String query = "delete from accelerometers where _id > 0; ";

        db.execSQL(query);

        //Сбросить автоинкремент
        query = "delete from `sqlite_sequence` where `name` = 'accelerometers';";

        db.execSQL(query);
    }//cleanAll

    //Получить последнюю добавленную запись
    /*public Accelerometer getLastAddedItem(){
        String query = "select * from accelerometers " +
                "where accelerometers._id = (select max(_id) from accelerometers);";

        Cursor cursor = db.rawQuery(query,null);

        Accelerometer accelerometer = null;

        if (cursor.moveToFirst())
            accelerometer = readFromCursor(cursor);

        cursor.close();
        return accelerometer;
    }*/
}
