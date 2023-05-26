-- Создание таблиц базы данных

-- удаление существующих таблиц, работает в MS SQL Server 2016+
drop table if exists appointments;
drop table if exists doctors;
drop table if exists patients;
drop table if exists persons;
drop table if exists specialities;


-- Таблица персональных данных, одинаковых для докторов 
-- и пациентов - persons 
create table persons
(
    id         int  primary key auto_increment,
    surname    varchar(60) not null, -- Фамилия персоны
    `name`     varchar(50) not null, -- Имя персоны
    patronymic varchar(60) not null  -- Отчество персоны
) engine InnoDB;

-- таблица -  справочник врачебных специальностей докторов Specialities
create table specialities
(
    id     int          not null primary key,
    `name` varchar(40) not null -- название врачебной специальности
);

-- Таблица сведений о докторах
create table doctors
(
    id            int   not null primary key auto_increment,
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

-- Таблица сведений о пациентах
create table patients
(
    id        int          primary key auto_increment,
    id_person int          not null, -- Внешний ключ, связь с персональными данными
    born_date date         not null, -- Дата рождения пациента
    `address` varchar(80) not null, -- Адрес проживания пациента
    passport  varchar(15) not null, -- Номер и серия паспорта

    -- внешний ключ - связь 1:1 к таблице persons
    constraint fk_Patients_persons foreign key (id_person) references persons (id)
    
    -- constraint fk_Patients_persons foreign key (id_person) references persons (id)
);

-- Таблица сведений о приемах пациентов 
create table appointments
(
    id               int  primary key auto_increment,
    appointment_date date not null,
    id_patient       int  not null,
    id_doctor        int  not null,

    -- внешний ключ - связь M:1 к таблице пациентов
    constraint fk_Appointments_patients foreign key (id_patient) references patients (id),

    -- внешний ключ - связь M:1 к таблице докторов
    constraint fk_Appointments_doctors foreign key (id_doctor) references doctors (id)
);