-- Запрос 1
select
   *
from
   view_patients
where
   view_patients.patient_surname like ?

-- Запрос 2
select
   *
from
   view_doctors
where
   view_doctors.percent > ?;

-- Запрос 3
select
view_appointments.id
, view_appointments.appointment_date
, view_appointments.doctor_id
, view_appointments.doctor_surname
, view_appointments.doctor_name
, view_appointments.doctor_patronymic
, view_appointments.speciality
, view_appointments.price
, view_appointments.percent
, view_appointments.patient_id
, view_appointments.patient_surname
, view_appointments.patient_name
, view_appointments.patient_patronymic
, view_appointments.born_date
, view_appointments.address
, view_appointments.passport
from
    view_appointments
where
    view_appointments.appointment_date between ? and ?
    --view_appointments.appointment_date between '2021-05-01' and '2021-11-01';

-- Запрос 4
select
    *
from
    view_doctors
where view_doctors.speciality like ?

-- Запрос 5
select
    view_appointments.id
    , view_appointments.appointment_date
    , view_appointments.doctor_surname
    , view_appointments.doctor_name
    , view_appointments.doctor_patronymic
    , view_appointments.speciality
    , view_appointments.price
    , view_appointments.percent
    , view_appointments.price * percent / 100 as salary
from
    view_appointments
order by
    view_appointments.speciality

-- Запрос 6
select
    view_appointments.appointment_date as 'date',
    count(*) as amount,
    max(view_appointments.price) as maxPrice
from
    view_appointments
group by
    view_appointments.appointment_date

-- Запрос 7
select
    view_appointments.speciality,
    count(*) as amount,
    avg(view_appointments.percent) as avgPercent
from
    view_appointments
group by
    view_appointments.speciality

 -- Выбрать специальности

 select specialities.name as speciality
 from specialities;