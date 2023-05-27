package com.step.wagner.content_providers.infrastructure;

import java.util.Calendar;

public class Parameters {

    //Названия таблиц
    public static final String SUBSCRIPTIONS = "subscriptions";
    public static final String PUBLICATIONS = "publications";

    //Столбец id
    public static final String ID_FIELD = "_id";

    public static final String HTML_FILE_NAME = "about.html";

    //Номера запросов
    public static final int QUERIES_AMOUNT = 1;
    public static final int PUBLICATIONS_TABLE_INDEX = 2;
    public static final int SUBSCRIPTIONS_TABLE_INDEX = 3;

    public static final String  DIALOG_SETTINGS_FILE = "subscription_dialog";

    //Активность, содержащая фрагмент вывода результатов запросов
    public static final int QUERIES_ACTIVITY_ID = 3;
    public static final int RESULT_OK = 200;

    public static final int DESC = 0;
    public static final int ASC = 1;


}
