-- удаление представлений
drop view if exists view_appointments;
drop view if exists view_doctors;
drop view if exists view_patients;

-- создание представления таблицы doctors

create view view_doctors
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



-- представление таблицы пациентов
create view view_patients
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


-- представление таблицы приемов

-- удаление представления 
-- drop view if exists view_appointments;
-- 

-- создание представления
create view view_appointments
as
select 
appointments.id
     , appointments.appointment_date    -- Дата приёма
     , view_doctors.id as doctor_id      -- id врача
     , view_doctors.doctor_surname      -- Фамилия врача
     , view_doctors.doctor_name         -- Имя врача
     , view_doctors.doctor_patronymic   -- Отчество врача
     , view_doctors.speciality          -- Специальность врача
     , view_doctors.price               -- Цена приёма у врача
     , view_doctors.percent             -- Процент от цены приёма 
     , view_patients.id as patient_id   -- id пациента
     , view_patients.patient_surname    -- Фамилия пациента
     , view_patients.patient_name       -- Имя пациента
     , view_patients.patient_patronymic -- Отчество пациента
     , view_patients.born_date          -- Дата рождения пациента
     , view_patients.address            -- Адрес проживания пациента
     , view_patients.passport           -- Паспортные данные
from appointments
          join view_doctors on appointments.id_doctor = view_doctors.id
          join view_patients on appointments.id_patient = view_patients.id
