package org.itstep.pd011.preferencesaccessdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

// SharedPreferences - методика сохранения переменных/настроек приложения
// Они записываются в XML-файл в локальной памяти приложения
// формат хранения: ключ-значение
// !!! ограничивайте размер хранимых данных !!!
public class MainActivity extends AppCompatActivity {

    // имя файла
    private static final String PREFS_FILE = "account";

    // имя параметра
    private static final String PREF_NAME = "name";
    private static final String PREF_EDT_NAME = "edtName";

    // объект хранения
    SharedPreferences settings;

    // для автоматического сохранения введенных данных при выходе из активности
    SharedPreferences.Editor prefEditor;

    // элементы интерфейса
    TextView txvName;
    EditText edtName;

    // сохраняемые данные
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // связь с разметкой
        txvName = findViewById(R.id.txvName);
        edtName = findViewById(R.id.edtName);

        // получить ссылку на файл или создать файл настроек
        settings = this.getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        // получить ссылку на объект для изменения настроек
        prefEditor = settings.edit();

        // при автоматическом сохранении данных по выходу из активности (см. ниже)
        // данные можно получить из сохраненных настроек
        name = settings.getString(PREF_NAME,"");
        if (!name.isEmpty()) {
            edtName.setText(name);
            txvName.setText(name);
        } else
            edtName.setText(settings.getString(PREF_EDT_NAME, ""));
    } // onCreate

    // сохранение данных
    public void saveName(View view) {
        // получаем введенное имя
        name = edtName.getText().toString();

        // сохраняем его в настройках
        prefEditor.putString(PREF_NAME, name);
        prefEditor.apply();  // запись

        Snackbar sn = Snackbar.make(view, "Имя сохранено", Snackbar.LENGTH_INDEFINITE);
        sn.setAction("OK", v -> {});
        sn.show();
    } // saveName

    // чтение параметра
    public void getName(View view) {
        // получаем сохраненное имя
        name = settings.getString(PREF_NAME,"не определено");
        txvName.setText(name);
    } // getName

    // для автоматического сохранения введенных данных
    // при выходе из активности переопределяем метод onPause
    @Override
    protected void onPause(){
        super.onPause();

        // сохраняем введенное имя в настройках
        prefEditor.putString(PREF_EDT_NAME, edtName.getText().toString());
        prefEditor.putString(PREF_NAME, name);
        prefEditor.apply();
    } // onPause
}