package com.step.wagner.sensors.repositories;

import static com.step.wagner.sensors.infrastructure.Utils.accelerometersRepository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.step.wagner.sensors.infrastructure.Parameters;
import com.step.wagner.sensors.infrastructure.Utils;
import com.step.wagner.sensors.models.entities.Collecting;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CollectingRepository extends BaseRepository<Collecting> {

    public CollectingRepository(Context context) {

        super(context, Parameters.COLLECTING_TABLE);

        //Задать строки для сырых запросов
        getAllQuery = "select * from view_collecting";

        //Запрос для получения элемента по id
        getByIdQuery = String.format("%s where view_collecting.%s = ?", getAllQuery,Parameters.ID_FIELD);
    }


    //Получение объекта из курсора
    @Override
    Collecting readFromCursor(Cursor cursor) {
        //Индексы столбцов
        int idIndex = cursor.getColumnIndex(Parameters.ID_FIELD);
        int startDateTimeIndex = cursor.getColumnIndex("start_time");
        int accelerometerIdIndex = cursor.getColumnIndex("accelerometer_id");
        int axis_X_index = cursor.getColumnIndex("axis_X");
        int axis_Y_index = cursor.getColumnIndex("axis_Y");
        int axis_Z_index = cursor.getColumnIndex("axis_Z");
        int approximationIndex = cursor.getColumnIndex("approximation");
        int lightIndex = cursor.getColumnIndex("light");
        int receivingTimeIndex = cursor.getColumnIndex("receiving_time");
        int sensorsTypeIndex = cursor.getColumnIndex("sensors_type");


        //Получение значений
        int id = cursor.getInt(idIndex);

        Date startDateTime = Utils.tryParseDbDateTime(cursor.getString(startDateTimeIndex));

        int accelerometerId = cursor.getInt(accelerometerIdIndex);
        float axisX = cursor.getFloat(axis_X_index);
        float axisY = cursor.getFloat(axis_Y_index);
        float axisZ = cursor.getFloat(axis_Z_index);
        float approximation = cursor.getFloat(approximationIndex);
        float light = cursor.getFloat(lightIndex);

        Date receivingTime = Utils.tryParseTime(cursor.getString(receivingTimeIndex));
        String sensorsType = cursor.getString(sensorsTypeIndex);


        //Создать объект
        return new Collecting(id,startDateTime,receivingTime,
                accelerometerId,axisX,axisY,axisZ,
                approximation,light,sensorsType);
    }

    //Добавление
    public long insert(Collecting collectingItem){

        ContentValues cv = new ContentValues();

        //Если запись с акселерометром была создана и её нет в связанной таблице и репозиторий иницализирован
        if (collectingItem.getAccelerometer().getId() == 0 && accelerometersRepository != null){
            accelerometersRepository.open();

            long addedId = accelerometersRepository.insert(collectingItem.getAccelerometer());
            cv.put("accelerometer_id",addedId);

            accelerometersRepository.close();

        }
        else cv.put("accelerometer_id",collectingItem.getAccelerometer().getId());

        cv.put("start_time",     Utils.dbDateTimeFormat.format(collectingItem.getStartDateTime()));
        cv.put("approximation",  collectingItem.getApproximation());
        cv.put("light",          collectingItem.getLight());
        cv.put("receiving_time", Utils.timeFormat.format(collectingItem.getReceivingTime()));
        cv.put("sensors_type",   collectingItem.getSensorsTypes());

        return db.insert(tableName, null, cv);
    } // insert

    //Добавление коллекции записей
    @SuppressLint("DefaultLocale")
    public void insertAll(List<Collecting> collectingItems){

        //Добавить значения в таблицу акселерометров
        accelerometersRepository.open();

        accelerometersRepository.insertAll(collectingItems
                .stream()
                .map(Collecting::getAccelerometer)
                .collect(Collectors.toList()));

        //Получить id добавленных элементов
        List<Integer> addedIds = accelerometersRepository.getIdsList();

        accelerometersRepository.close();

        StringBuilder querySb = new StringBuilder("insert into collecting " +
                "(start_time, accelerometer_id, approximation, light, receiving_time, sensors_type) values");


        int addedItemsSize = addedIds.size();

        for (int i = 0; i < collectingItems.size(); i++) {
            Collecting collectingItem = collectingItems.get(i);

            //Задать значения после сбора информации о датчиках. Так же проверяем, чтобы i не выходил за пределы списка id акселерометров
            querySb.append(String.format("('%s', %d, %.3f, %.3f, '%s', '%s')%s",
                    Utils.dbDateTimeFormat.format(collectingItem.getStartDateTime()), addedIds.get(i)/*addedIds.get(Math.min(i, addedItemsSize-1))*/,
                    collectingItem.getApproximation(), collectingItem.getLight(), Utils.timeFormat.format(collectingItem.getReceivingTime()),
                    collectingItem.getSensorsTypes(), i < collectingItems.size()-1 ? "," : ";"));
        }


        db.execSQL(querySb.toString());
    } // insertAll

    //Очистить таблицу
    public void cleanAll(){

        //Удалить все записи
        String query = "delete from collecting where _id > 0; ";

        db.execSQL(query);

        //Сбросить автоинкремент
        query = "delete from `sqlite_sequence` where `name` = 'collecting';";

        db.execSQL(query);

        //Очистить справочную таблицу акселерометров
        accelerometersRepository.open();

        accelerometersRepository.cleanAll();

        accelerometersRepository.close();

    }//cleanAll


}
