package com.step.wagner.sensors.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.step.wagner.sensors.infrastructure.Parameters;
import com.step.wagner.sensors.models.entities.AccelerometerStatistics;

public class AccelerometersStatisticsRepository extends BaseRepository<AccelerometerStatistics> {

    public AccelerometersStatisticsRepository(Context context) {

        super(context, Parameters.ACCELEROMETERS_STATISTICS);

        //Задать строки для сырых запросов
        getAllQuery = "select "
        + Parameters.ID_FIELD
        + " , accelerometer_X_min"
        + " , accelerometer_X_avg"
        + " , accelerometer_X_max"

        + " , accelerometer_Y_min"
        + " , accelerometer_Y_avg"
        + " , accelerometer_Y_max"

        + " , accelerometer_Z_min"
        + " , accelerometer_Z_avg"
        + " , accelerometer_Z_max"
        + " from accelerometer_statistics";

        //Запрос для получения элемента по id
        getByIdQuery = String.format("%s where accelerometer_statistics.%s = ?", getAllQuery,Parameters.ID_FIELD);
    }


    //Получение объекта из курсора
    @Override
    AccelerometerStatistics readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
        int axis_X_min_index = cursor.getColumnIndex("accelerometer_X_min");
        int axis_X_avg_index = cursor.getColumnIndex("accelerometer_X_avg");
        int axis_X_max_index = cursor.getColumnIndex("accelerometer_X_max");

        int axis_Y_min_index = cursor.getColumnIndex("accelerometer_Y_min");
        int axis_Y_avg_index = cursor.getColumnIndex("accelerometer_Y_avg");
        int axis_Y_max_index = cursor.getColumnIndex("accelerometer_Y_max");

        int axis_Z_min_index = cursor.getColumnIndex("accelerometer_Z_min");
        int axis_Z_avg_index = cursor.getColumnIndex("accelerometer_Z_avg");
        int axis_Z_max_index = cursor.getColumnIndex("accelerometer_Z_max");


        //Получение значений
        int id = cursor.getInt(idIndex);
        float axisX_min = cursor.getFloat(axis_X_min_index);
        float axisX_avg = cursor.getFloat(axis_X_avg_index);
        float axisX_max = cursor.getFloat(axis_X_max_index);

        float axisY_min = cursor.getFloat(axis_Y_min_index);
        float axisY_avg = cursor.getFloat(axis_Y_avg_index);
        float axisY_max = cursor.getFloat(axis_Y_max_index);

        float axisZ_min = cursor.getFloat(axis_Z_min_index);
        float axisZ_avg = cursor.getFloat(axis_Z_avg_index);
        float axisZ_max = cursor.getFloat(axis_Z_max_index);


        //Создать объект
        return new AccelerometerStatistics(id,axisX_min,axisX_avg,axisX_max,
                axisY_min,axisY_avg, axisY_max,axisZ_min,axisZ_avg, axisZ_max);
    }

    //Получить объект статистики по акселерометру из запроса к таблице сбора
    AccelerometerStatistics readFromQueryCursor(Cursor cursor) {
        //Индексы столбцов
        int axis_X_min_index = cursor.getColumnIndex("accelerometer_X_min");
        int axis_X_avg_index = cursor.getColumnIndex("accelerometer_X_avg");
        int axis_X_max_index = cursor.getColumnIndex("accelerometer_X_max");

        int axis_Y_min_index = cursor.getColumnIndex("accelerometer_Y_min");
        int axis_Y_avg_index = cursor.getColumnIndex("accelerometer_Y_avg");
        int axis_Y_max_index = cursor.getColumnIndex("accelerometer_Y_max");

        int axis_Z_min_index = cursor.getColumnIndex("accelerometer_Z_min");
        int axis_Z_avg_index = cursor.getColumnIndex("accelerometer_Z_avg");
        int axis_Z_max_index = cursor.getColumnIndex("accelerometer_Z_max");


        //Получение значений
        float axisX_min = cursor.getFloat(axis_X_min_index);
        float axisX_avg = cursor.getFloat(axis_X_avg_index);
        float axisX_max = cursor.getFloat(axis_X_max_index);

        float axisY_min = cursor.getFloat(axis_Y_min_index);
        float axisY_avg = cursor.getFloat(axis_Y_avg_index);
        float axisY_max = cursor.getFloat(axis_Y_max_index);

        float axisZ_min = cursor.getFloat(axis_Z_min_index);
        float axisZ_avg = cursor.getFloat(axis_Z_avg_index);
        float axisZ_max = cursor.getFloat(axis_Z_max_index);


        //Создать объект
        return new AccelerometerStatistics(0,axisX_min,axisX_avg,axisX_max,
                axisY_min,axisY_avg, axisY_max,axisZ_min,axisZ_avg, axisZ_max);
    }


    //Получить статистику по акселерометрам из таблицы сбора
    public long getAccelerometersStatisticsFromCollecting(){
        String query = "select ifnull(min(axis_X),0) as accelerometer_X_min," +
                "ifnull(avg(axis_X),0) as accelerometer_X_avg," +
                "ifnull(max(axis_X),0) as accelerometer_X_max," +

                "ifnull(min(axis_Y),0) as accelerometer_Y_min," +
                "ifnull(avg(axis_Y),0) as accelerometer_Y_avg," +
                "ifnull(max(axis_Y),0) as accelerometer_Y_max," +

                "ifnull(min(axis_Z),0) as accelerometer_Z_min," +
                "ifnull(avg(axis_Z),0) as accelerometer_Z_avg," +
                "ifnull(max(axis_Z),0) as accelerometer_Z_max from view_collecting;";

        Cursor cursor = db.rawQuery(query,null);

        long insertedId = -1;

        if (cursor.moveToFirst())
            insertedId = insert(readFromQueryCursor(cursor));

        return insertedId;
    }

    //Добавление записи
    public long insert(AccelerometerStatistics accelerometerStatistics){

        ContentValues cv = new ContentValues();

        cv.put("accelerometer_X_min", accelerometerStatistics.getAxisX_min());
        cv.put("accelerometer_X_avg", accelerometerStatistics.getAxisX_avg());
        cv.put("accelerometer_X_max", accelerometerStatistics.getAxisX_max());

        cv.put("accelerometer_Y_min", accelerometerStatistics.getAxisY_min());
        cv.put("accelerometer_Y_avg", accelerometerStatistics.getAxisY_avg());
        cv.put("accelerometer_Y_max", accelerometerStatistics.getAxisY_max());

        cv.put("accelerometer_Z_min", accelerometerStatistics.getAxisZ_min());
        cv.put("accelerometer_Z_avg", accelerometerStatistics.getAxisZ_avg());
        cv.put("accelerometer_Z_max", accelerometerStatistics.getAxisZ_max());

        return db.insert(tableName, null, cv);
    } // insert

    //Очистить таблицу
    public void cleanAll(){

        //Удалить все записи
        String query = "delete from accelerometer_statistics where _id > 0; ";

        db.execSQL(query);

        //Сбросить автоинкремент
        query = "delete from `sqlite_sequence` where `name` = 'accelerometer_statistics';";

        db.execSQL(query);
    }//cleanAll
}
