-- удаление представлений
drop view if exists view_collecting;
drop view if exists view_statistics;

-- создание представления таблицы сбора

create view view_collecting as
select collecting._id as _id,
       collecting.start_time,
       a._id          as accelerometer_id,
       a.axis_X,
       a.axis_Y,
       a.axis_Z,
       collecting.approximation,
       collecting.light,
       collecting.receiving_time,
       collecting.sensors_type

from collecting
         join accelerometers a on a._id = collecting.accelerometer_id;


-- представление таблицы статистики
create view view_statistics as
select
    statistics._id,
    statistics.collected_amount,    -- количество данных

    accelStat._id as accel_stat_id,
    accelStat.accelerometer_X_min, -- ускорение
    accelStat.accelerometer_X_avg, -- ускорение
    accelStat.accelerometer_X_max, -- ускорение

    accelStat.accelerometer_Y_min, -- ускорение
    accelStat.accelerometer_Y_avg, -- ускорение
    accelStat.accelerometer_Y_max, -- ускорение

    accelStat.accelerometer_Z_min, -- ускорение
    accelStat.accelerometer_Z_avg, -- ускорение
    accelStat.accelerometer_Z_max, -- ускорение

    statistics.approximation_min, -- приближение
    statistics.approximation_avg, -- приближение
    statistics.approximation_max, -- приближение

    statistics.light_min,         -- освещённость
    statistics.light_avg,         -- освещённость
    statistics.light_max,         -- освещённость

    statistics.collecting_start_time, -- Время сбора
    statistics.handling_time,         --
    statistics.sensors_type           --
from
    statistics join accelerometer_statistics accelStat on statistics.accelerometer_statistics_id = accelStat._id

