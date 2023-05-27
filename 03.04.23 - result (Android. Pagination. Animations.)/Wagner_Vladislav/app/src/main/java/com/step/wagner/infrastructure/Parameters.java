package com.step.wagner.infrastructure;

import java.util.Calendar;

public class Parameters {

    //Названия таблиц
    public static final String APPOINTMENTS = "appointments";
    public static final String DOCTORS = "doctors";
    public static final String PATIENTS = "patients";

    //Столбец id
    public static final String ID_FIELD = "_id";

    //Имена файлов
    public static final String APPOINTMENTS_FILE_NAME = "appointments.json";
    public static final String DOCTORS_FILE_NAME = "doctors.json";
    public static final String PATIENTS_FILE_NAME = "patients.json";

    public static final String HTML_FILE_NAME = "about.html";

    //Заданные в запросах параметры

    //Запрос 3
    public static Calendar CALENDAR_MIN_DATE;
    public static Calendar CALENDAR_MAX_DATE;

}
