package com.step.home_work.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.step.home_work.R;
import com.step.home_work.models.DBHelper;

import java.io.DataOutputStream;

public class Query1Activity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private ListView lsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query1);

        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createDb();
    }

    @Override
    public void onResume(){
        super.onResume();

        //Открыть подключние к БД
    }
}