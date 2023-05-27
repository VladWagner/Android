-- Запрос к таблице сбора текущего сеанса для выборки статистики
select
    count() as collected_count,

    --Значения, которые сначала попадают в статистику по акселерометру
    ifnull(min(axis_X),0) as min_axis_X,
    ifnull(avg(axis_X),0) as avg_axis_X,
    ifnull(max(axis_X),0) as max_axis_X,

    ifnull(min(axis_Y),0) as min_axis_Y,
    ifnull(avg(axis_Y),0) as avg_axis_Y,
    ifnull(max(axis_Y),0) as max_axis_Y,

    ifnull(min(axis_Z),0) as min_axis_Z,
    ifnull(avg(axis_Z),0) as avg_axis_Z,
    ifnull(max(axis_Z),0) as max_axis_Z,

    -- Значения для таблицы статистики ↓ ↓

    ifnull(min(approximation),0) as min_approximation,
    ifnull(avg(approximation),0) as avg_approximation,
    ifnull(max(approximation),0) as max_approximation,

    ifnull(min(light),0) as min_light,
    ifnull(avg(light),0) as avg_light,
    ifnull(max(light),0) as max_light,

    (select start_time from view_collecting where view_collecting._id = 1) as start_time,
    (select sensors_type from view_collecting where view_collecting._id = 1) as sensors_type

from view_collecting;

-- таблица значений собранных с акселерометров
select
    *
from accelerometers;

-- представление таблицы сбора
select
    *
from view_collecting;

-- таблица статистики по акселерометрам
select
    *
from accelerometer_statistics;

-- представление таблицы статистики
select
    *
from view_statistics;