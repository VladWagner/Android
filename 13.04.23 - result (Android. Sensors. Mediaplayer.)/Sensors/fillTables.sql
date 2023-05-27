-- Заполнение таблиц базы данных Polyclinic

-- Тестовые значения по акселерометрам
delete from accelerometers where _id > 0;
DELETE FROM `sqlite_sequence` WHERE `name` = 'accelerometers';

insert into accelerometers
(axis_X, axis_Y, axis_Z)
values
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 1
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 2
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 3
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 4
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 5
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 6
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 7
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 8
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 9
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0), -- 10
    (random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0); -- 11

-- Тестовые значения для таблицы сбора
delete from collecting where _id > 0;
DELETE FROM `sqlite_sequence` WHERE `name` = 'collecting';

insert into collecting
(start_time, accelerometer_id, approximation, light, receiving_time, sensors_type)
values
    ('2000-01-31 15:59:00' ,1, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 15:59:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 2
    ('2000-01-31 15:53:00' ,2, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 15:53:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 3
    ('2000-01-31 15:43:00' ,3, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 15:43:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 4
    ('2000-01-31 15:53:00' ,4, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 15:53:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 5
    ('2000-01-31 15:59:00' ,5, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 15:59:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 1
    ('2000-01-31 16:01:00' ,6, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 16:01:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 6
    ('2000-01-31 16:00:00' ,7, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 16:00:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 7
    ('2000-01-31 16:15:00' ,8, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 16:15:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 8
    ('2000-01-31 16:20:00' ,9, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0 , '2000-01-31 16:20:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 9
    ('2000-01-31 17:10:00' ,10, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, '2000-01-31 17:10:55' ,'Акселерометр\n Приближение\n Освещённость'), -- 10
    ('2000-01-31 17:15:00' ,11, random()%(15.1 - 5.0) + 5.0, random()%(15.1 - 5.0) + 5.0, '2000-01-31 17:15:55' ,'Акселерометр\n Приближение\n Освещённость'); -- 11

-- Заполнение таблицы статистики акселерометров
delete from accelerometer_statistics where _id > 0;
DELETE FROM `sqlite_sequence` WHERE `name` = 'accelerometer_statistics';

--Вставка всегда только одного элемента, поскольку выборка происходит из всей накопленной таблицы сбора
insert into accelerometer_statistics
(accelerometer_X_min, accelerometer_X_avg, accelerometer_X_max,
 accelerometer_Y_min, accelerometer_Y_avg, accelerometer_Y_max,
 accelerometer_Z_min, accelerometer_Z_avg, accelerometer_Z_max)
values
    (
        (select min(view_collecting.axis_X) from view_collecting),(select avg(view_collecting.axis_X) from view_collecting),(select max(view_collecting.axis_X) from view_collecting),
        (select min(view_collecting.axis_Y) from view_collecting),(select avg(view_collecting.axis_Y) from view_collecting),(select max(view_collecting.axis_Y) from view_collecting),
        (select min(view_collecting.axis_Z) from view_collecting),(select avg(view_collecting.axis_Z) from view_collecting),(select max(view_collecting.axis_Z) from view_collecting)
    ); -- 1

-- Таблица статистики
-- В репозитории заполнение будет происходить совершенно по-другому: к таблице сбора будет делаться
-- запрос с агрегатными функциями -> из курсора будет создаваться StatisticEntity, из этой модели будет выбираться объкект AccelerometerStatistic
-- и добавляться в таблицу статистики по акселерометрам (через свой репозиторий), а возвращенный id будет писаться уже в insert в общую таблицу статистики

delete from statistics where _id > 0;
DELETE FROM `sqlite_sequence` WHERE `name` = 'statistics';

insert into statistics
(accelerometer_statistics_id, approximation_min, approximation_avg, approximation_max, light_min, light_avg, light_max, collecting_start_time, handling_time, sensors_type)
values
    -- последняя добавленная запись статистики по акселерометру
    -- агрегатные вычисления значений датчика приближения
    -- агрегатные вычисления значений датчика освещённости
    -- время начала сбора
    -- типы датчиков
    (
        (select max(_id) from accelerometer_statistics),
        (select min(approximation) from view_collecting),(select avg(approximation) from view_collecting),(select max(approximation) from view_collecting),
        (select min(light) from view_collecting),(select avg(light) from view_collecting),(select max(light) from view_collecting),
        (select start_time from view_collecting), CURRENT_DATE,(select view_collecting.sensors_type from view_collecting)
    ); -- 1


