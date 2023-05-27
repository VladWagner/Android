package com.step.wagner.sensors.repositories;

import static com.step.wagner.sensors.infrastructure.Utils.accelerometersRepository;
import static com.step.wagner.sensors.infrastructure.Utils.accelerometersStatisticsRepository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.step.wagner.sensors.infrastructure.Parameters;
import com.step.wagner.sensors.infrastructure.Utils;
import com.step.wagner.sensors.models.entities.AccelerometerStatistics;
import com.step.wagner.sensors.models.entities.Statistics;

import java.util.Calendar;
import java.util.Date;

public class StatisticsRepository extends BaseRepository<Statistics> {

    public StatisticsRepository(Context context) {

        super(context, Parameters.STATISTIC);

        //Задать строки для сырых запросов
        getAllQuery = "select * from view_statistics";

        //Запрос для получения элемента по id
        getByIdQuery = String.format("%s where view_statistics.%s = ?", getAllQuery,Parameters.ID_FIELD);
    }


    //Получение объекта из курсора
    @Override
    Statistics readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
        int accelStatIdIndex = cursor.getColumnIndex("accel_stat_id");

        int amountIndex = cursor.getColumnIndex("collected_amount");

        int approximation_max_index = cursor.getColumnIndex("approximation_max");
        int approximation_min_index = cursor.getColumnIndex("approximation_min");
        int approximation_avg_index = cursor.getColumnIndex("approximation_avg");

        int light_max_index = cursor.getColumnIndex("light_max");
        int light_min_index = cursor.getColumnIndex("light_min");
        int light_avg_index = cursor.getColumnIndex("light_avg");

        int collectingTimeIndex = cursor.getColumnIndex("collecting_start_time");
        int handlingTimeIndex = cursor.getColumnIndex("handling_time");
        int sensorsTypeIndex = cursor.getColumnIndex("sensors_type");


        //Получение значений
        int id = cursor.getInt(idIndex);
        int accelStatId = cursor.getInt(accelStatIdIndex);
        int amount = cursor.getInt(amountIndex);

        //Получить статистику по акселерометрам
        accelerometersStatisticsRepository.open();

        AccelerometerStatistics accelerometerStat =  accelerometersStatisticsRepository.getById(accelStatId);

        accelerometersStatisticsRepository.close();

        float approximationMax = cursor.getFloat(approximation_max_index);
        float approximationMin = cursor.getFloat(approximation_min_index);
        float approximationAvg = cursor.getFloat(approximation_avg_index);

        float lightMax = cursor.getFloat(light_max_index);
        float lightMin = cursor.getFloat(light_min_index);
        float lightAvg = cursor.getFloat(light_avg_index);

        Date collectingTime = Utils.tryParseDbDateTime(cursor.getString(collectingTimeIndex));
        Date handlingTime = Utils.tryParseDbDateTime(cursor.getString(handlingTimeIndex));
        String sensorsType = cursor.getString(sensorsTypeIndex);


        //Создать объект
        return new Statistics(id, amount,accelerometerStat,
                approximationMin,approximationAvg,approximationMax,
                lightMin,lightAvg,lightMax,
                collectingTime,handlingTime,sensorsType);
    }


    //Получение объекта сущности из курсора
    private Statistics readFromQueryCursor(Cursor cursor) {

        int amountIndex = cursor.getColumnIndex("collected_count");

        int approximation_max_index = cursor.getColumnIndex("max_approximation");
        int approximation_min_index = cursor.getColumnIndex("min_approximation");
        int approximation_avg_index = cursor.getColumnIndex("avg_approximation");

        int light_max_index = cursor.getColumnIndex("max_light");
        int light_min_index = cursor.getColumnIndex("min_light");
        int light_avg_index = cursor.getColumnIndex("avg_light");

        int collectingTimeIndex = cursor.getColumnIndex("start_time");
        int sensorsTypeIndex = cursor.getColumnIndex("sensors_type");

        //Получение значений
        int amount = cursor.getInt(amountIndex);

        float approximationMax = cursor.getFloat(approximation_max_index);
        float approximationMin = cursor.getFloat(approximation_min_index);
        float approximationAvg = cursor.getFloat(approximation_avg_index);

        float lightMax = cursor.getFloat(light_max_index);
        float lightMin = cursor.getFloat(light_min_index);
        float lightAvg = cursor.getFloat(light_avg_index);

        Date collectingTime = Utils.tryParseDbDateTime(cursor.getString(collectingTimeIndex));
        Date handlingTime = Calendar.getInstance().getTime();
        String sensorsType = cursor.getString(sensorsTypeIndex);


        //Создать объект
        return new Statistics(0, amount,new AccelerometerStatistics(),
                approximationMin,approximationAvg,approximationMax,
                lightMin,lightAvg,lightMax,
                collectingTime,handlingTime,sensorsType);
    }

    //Получить статистику по акселерометрам из таблицы сбора
    public long getStatisticsFromCollecting(){

        //Добавить статистику по акселерометру
        accelerometersStatisticsRepository.open();

        long accelStatId = accelerometersStatisticsRepository.getAccelerometersStatisticsFromCollecting();

        AccelerometerStatistics accelerometerStatistics = accelerometersStatisticsRepository.getById((int) accelStatId);

        accelerometersRepository.close();

        //Запрос на получение статистики по остальным датчикам
        String query = "select  count() as collected_count," +

                " ifnull(min(approximation),0) as min_approximation," +
                " ifnull(avg(approximation),0) as avg_approximation," +
                " ifnull(max(approximation),0) as max_approximation," +

                " ifnull(min(light),0) as min_light," +
                " ifnull(avg(light),0) as avg_light," +
                " ifnull(max(light),0) as max_light," +

                "(select start_time from view_collecting where view_collecting._id = 1) as start_time," +
                "(select sensors_type from view_collecting where view_collecting._id = 1) as sensors_type from view_collecting;";

        Cursor cursor = db.rawQuery(query,null);

        long insertedId = -1;

        //Если значения были прочитаны, тогда добавить запись в таблицу обработок
        if (cursor.moveToFirst()){
            Statistics statistics = readFromQueryCursor(cursor);

            statistics.setAccelerometerStatistics(accelerometerStatistics);

            insertedId = insert(statistics);
        }

        return insertedId;
    }

    //Добавление записи
    public long insert(Statistics statistics){

        ContentValues cv = new ContentValues();

        cv.put("collected_amount",            statistics.getAmount());
        cv.put("accelerometer_statistics_id", statistics.getAccelerometerStatistics().getId());
        cv.put("approximation_min", statistics.getApproximationMin());
        cv.put("approximation_avg", statistics.getApproximationAvg());
        cv.put("approximation_max", statistics.getApproximationMax());

        cv.put("light_min", statistics.getLightMin());
        cv.put("light_avg", statistics.getLightAvg());
        cv.put("light_max", statistics.getLightMax());

        cv.put("collecting_start_time", Utils.dbDateTimeFormat.format(statistics.getCollectingStartTime()));
        cv.put("handling_time",         Utils.dbDateTimeFormat.format(statistics.getHandlingTime()));
        cv.put("sensors_type",          statistics.getSensorsTypes());

        return db.insert(tableName, null, cv);
    } // insert

    //Очистить таблицу
    public void cleanAll(){

        //Удалить все записи
        String query = "delete from statistics where _id > 0; ";

        db.execSQL(query);

        //Сбросить автоинкремент
        query = "delete from `sqlite_sequence` where `name` = 'statistics';";

        db.execSQL(query);
    }//cleanAll

    //Получить последнюю добавленную запись
    public Statistics getLastAddedItem(){
        String query = "select * from view_statistics " +
                "where view_statistics._id = (select max(_id) from view_statistics);";

        Cursor cursor = db.rawQuery(query,null);

        Statistics statistics = null;

        if (cursor.moveToFirst())
            statistics = readFromCursor(cursor);

        cursor.close();
        return statistics;
    }
}
