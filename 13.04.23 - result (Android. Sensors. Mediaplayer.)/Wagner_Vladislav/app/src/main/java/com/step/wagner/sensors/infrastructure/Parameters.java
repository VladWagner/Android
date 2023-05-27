package com.step.wagner.sensors.infrastructure;

public class Parameters {

    //Последнее id сообщения
    public static int messageId = 0;

    //Столбец id
    public static final String ID_FIELD = "_id";

    //Имена таблиц БД
    public static final String ACCELEROMETERS = "accelerometers";
    public static final String COLLECTING_TABLE = "collecting";
    public static final String ACCELEROMETERS_STATISTICS = "accelerometer_statistics";
    public static final String STATISTIC = "statistics";

    public static final String HTML_FILE_NAME = "about.html";
    public static final String SESSION_STATE_FILE = "saving_session_state";
}
