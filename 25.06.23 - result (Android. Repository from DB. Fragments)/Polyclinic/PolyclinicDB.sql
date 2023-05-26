--
-- Файл сгенерирован с помощью SQLiteStudio v3.4.3 в пн март 27 09:40:05 2023
--
-- Использованная кодировка текста: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Таблица: appointments
CREATE TABLE IF NOT EXISTS appointments
(
    id               int  not null primary key,
    appointment_date date not null,
    id_patient       int  not null,
    id_doctor        int  not null,

    -- внешний ключ - связь M:1 к таблице пациентов
    constraint fk_Appointments_patients foreign key (id_patient) references patients (id),

    -- внешний ключ - связь M:1 к таблице докторов
    constraint fk_Appointments_doctors foreign key (id_doctor) references doctors (id)
);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (1, '2021-10-21', 1, 1);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (2, '2021-10-21', 2, 1);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (3, '2021-10-21', 3, 2);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (4, '2021-10-22', 4, 3);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (5, '2021-04-22', 4, 5);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (6, '2021-01-31', 3, 5);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (7, '2021-12-22', 2, 6);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (8, '2021-10-23', 5, 2);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (9, '2021-10-23', 6, 5);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (10, '2021-10-24', 9, 6);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (11, '2021-12-22', 7, 6);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (12, '2021-10-28', 3, 7);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (13, '2021-10-28', 2, 8);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (14, '2021-10-28', 4, 9);
INSERT INTO appointments (id, appointment_date, id_patient, id_doctor) VALUES (15, '2021-11-02', 6, 9);

-- Таблица: doctors
CREATE TABLE IF NOT EXISTS doctors
(
    id            int   not null primary key,
    id_person     int   not null, -- Внешний ключ, связь с персональными данными
    id_speciality int   not null, -- Внешний ключ, связь со справочником врачебных специальностей
    price         int   not null, -- Стоимость приема
    `percent`     float not null, -- Процент отчисления от стоимости приема на зарплату врача

    -- ограничения полей таблицы
    constraint ck_doctors_price check (price > 0),
    constraint ck_doctors_percent check (`percent` > 0),

    -- внешний ключ связи таблиц doctor & person. Тип связи 1:1
    constraint fk_doctors_persons foreign key (id_person) references persons (id),

    -- внешний ключ связи таблиц doctor & speciality. Тип связи М:1
    constraint fk_doctors_specialities foreign key (id_speciality) references specialities (id)
);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (1, 1, 1, 110, 5.0);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (2, 2, 2, 600, 3.0);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (3, 3, 1, 160, 3.5);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (4, 4, 1, 250, 3.2);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (5, 5, 3, 300, 4.5);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (6, 6, 2, 600, 4.5);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (7, 7, 4, 300, 4.0);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (8, 8, 5, 250, 3.5);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (9, 9, 6, 800, 5.1);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (10, 10, 6, 1300, 4.5);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (11, 11, 2, 350, 3.1);
INSERT INTO doctors (id, id_person, id_speciality, price, percent) VALUES (12, 12, 1, 120, 6.2);

-- Таблица: patients
CREATE TABLE IF NOT EXISTS patients
(
    id        int          primary key,
    id_person int          not null, -- Внешний ключ, связь с персональными данными
    born_date date         not null, -- Дата рождения пациента
    `address` varchar(80) not null, -- Адрес проживания пациента
    passport  varchar(15) not null, -- Номер и серия паспорта

    -- внешний ключ - связь 1:1 к таблице persons
    constraint fk_Patients_persons foreign key (id_person) references persons (id)
    
    -- constraint fk_Patients_persons foreign key (id_person) references persons (id)
);
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (1, 13, '2001-02-12', 'ул. Садовая, д. 123, кв. 12', 'KA45718');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (2, 14, '1991-07-09', 'ул. Зайцева, д. 2', 'ВВ34524');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (3, 15, '1963-03-18', 'пр. Титова, д. 11, кв. 91', 'ВС65423');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (4, 16, '1957-04-21', 'ул. Содовая, д. 9', 'ВО12312');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (5, 17, '2001-02-12', 'ул. Челюскинцев, д. 112, кв. 211', 'СК67443');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (6, 18, '1935-06-01', 'пр. Титова, д. 9, кв. 21', 'ВА12234');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (7, 19, '1992-07-06', 'пл. Конституции, д. 3, кв. 75', 'СО54334');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (8, 20, '1991-07-09', 'пр. Мира, д. 3, кв. 64', 'ВС89532');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (9, 21, '1947-12-23', 'ул. Содовая, д. 9, кв. 6', 'СС34267');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (10, 22, '1991-07-09', 'ул. Садовая, д. 10', 'АТ23414');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (11, 23, '1956-09-29', 'ул. Судовая, д. 1, кв. 91', 'СО98751');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (12, 24, '1963-04-13', 'пр. Титова, д. 31, кв. 42', 'ВА32456');
INSERT INTO patients (id, id_person, born_date, address, passport) VALUES (13, 25, '1957-04-21', 'ул. Челюскинцев, д. 3', 'ВО98412');

-- Таблица: persons
CREATE TABLE IF NOT EXISTS persons
(
    id         int  primary key,
    surname    varchar(60) not null, -- Фамилия персоны
    name     varchar(50) not null, -- Имя персоны
    patronymic varchar(60) not null  -- Отчество персоны
);
INSERT INTO persons (id, surname, name, patronymic) VALUES (1, 'Юрковский', 'Марк', 'Олегович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (2, 'Якубовская', 'Диана', 'Павловна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (3, 'Шапиро', 'Федор', 'Федорович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (4, 'Вожжаев', 'Сергей', 'Денисович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (5, 'Хроменко', 'Игорь', 'Владимирович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (6, 'Пелых', 'Марина', 'Ульяновна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (7, 'Лапотникова', 'Тамара', 'Оскаровна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (8, 'Огородников', 'Сергей', 'Иванович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (9, 'Яйло', 'Екатерина', 'Николаевна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (10, 'Бутусова', 'Инна', 'Александровна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (11, 'Михайлович', 'Анна', 'Валентиновна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (12, 'Тарапата', 'Михаил', 'Исаакович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (13, 'Трубихин', 'Эдуард', 'Михайлович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (14, 'Чмыхало', 'Олег', 'Тарасович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (15, 'Швец', 'Степан', 'Сидорович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (16, 'Потемкина', 'Наталья', 'Павловна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (17, 'Гритченко', 'Степан', 'Романович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (18, 'Селиванов', 'Александр', 'Михайлович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (19, 'Любарская', 'Лариса', 'Ильинична');
INSERT INTO persons (id, surname, name, patronymic) VALUES (20, 'Яструб', 'Владимир', 'Данилович');
INSERT INTO persons (id, surname, name, patronymic) VALUES (21, 'Мелашенко', 'Александр', 'Алексеевич');
INSERT INTO persons (id, surname, name, patronymic) VALUES (22, 'Пономаренко', 'Владислов', 'Дмитриевич');
INSERT INTO persons (id, surname, name, patronymic) VALUES (23, 'Хавалджи', 'Любовь', 'Амировна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (24, 'Пархоменко', 'Ирина', 'Владимировна');
INSERT INTO persons (id, surname, name, patronymic) VALUES (25, 'Демидова', 'Алина', 'Александровна');

-- Таблица: specialities
CREATE TABLE IF NOT EXISTS specialities
(
    id     int          not null primary key,
    `name` varchar(40) not null -- название врачебной специальности
);
INSERT INTO specialities (id, name) VALUES (1, 'терапевт');
INSERT INTO specialities (id, name) VALUES (2, 'стоматолог');
INSERT INTO specialities (id, name) VALUES (3, 'окулист');
INSERT INTO specialities (id, name) VALUES (4, 'кардиолог');
INSERT INTO specialities (id, name) VALUES (5, 'пульманолог');
INSERT INTO specialities (id, name) VALUES (6, 'хирург');
INSERT INTO specialities (id, name) VALUES (7, 'акушер');
INSERT INTO specialities (id, name) VALUES (8, 'отоларинголог');
INSERT INTO specialities (id, name) VALUES (9, 'инфекционист');
INSERT INTO specialities (id, name) VALUES (10, 'физиотерпевт');
INSERT INTO specialities (id, name) VALUES (11, 'онколог');
INSERT INTO specialities (id, name) VALUES (12, 'уролог');

-- Представление: view_appointments
CREATE VIEW IF NOT EXISTS view_appointments AS select 
appointments.id
     , appointments.appointment_date                  -- Дата приёма
     , view_doctors.doctor_surname    -- Фамилия врача
     , view_doctors.doctor_name        -- Имя врача
     , view_doctors.doctor_patronymic  -- Отчество врача
     , view_doctors.speciality  -- Специальность врача
     , view_doctors.price                                  -- Цена приёма у врача
     , view_doctors.percent                              -- Процент от цены приёма 
     , view_patients.patient_surname    -- Фамилия пациента
     , view_patients.patient_name       -- Имя пациента
     , view_patients.patient_patronymic -- Отчество пациента
     , view_patients.born_date                      -- Дата рождения пациента
     , view_patients.address                      -- Адрес проживания пациента
     , view_patients.passport                       -- Паспортные данные
from appointments
          join view_doctors on appointments.id_doctor = view_doctors.id
          join view_patients on appointments.id_patient = view_patients.id;

-- Представление: view_doctors
CREATE VIEW IF NOT EXISTS view_doctors
as
select doctors.id,
	persons.surname    as doctor_surname   , -- Фамилия врача
	persons.`name`     as doctor_name      , -- Имя врача
	persons.patronymic as doctor_patronymic, -- Отчество врача
	specialities.name  as speciality  , -- Специальность
	doctors.price                          , -- Цена приёма
	doctors.`percent`                       -- Процент от цены приёма врачу
from doctors
         join persons on doctors.id_person = persons.id
         join specialities on doctors.id_speciality = specialities.id;

-- Представление: view_patients
CREATE VIEW IF NOT EXISTS view_patients
as
select patients.id,
      persons.surname    as patient_surname   , -- Фамилия 
      persons.`name`     as patient_name      , -- Имя 
      persons.patronymic as patient_patronymic, -- Отчество 
      patients.born_date                      , -- Дата рождения пациента
      patients.`address`                      , -- Адрес проживания пациента 
      patients.passport                        -- Паспортные данные пациента
from patients
         join persons on patients.id_person = persons.id;

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
