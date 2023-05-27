package org.itstep.pd011.contentproviderintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
* Приложение может сохранять разнообразую информацию о пользователе,
* какие-то связанные данные в файлах или настройках. Однако ОС Android
* уже хранит ряд важной информации, связанной с пользователем, к которой
* имеем доступ и которую мы можем использовать.
* Это и списки контактов, и файлы сохраненных изображений и видеоматериалов,
* и какие-то отметки о звонках и т.д., то есть некоторый контент. А для доступа
* к этому контенту в OC Android определены провайдеры контента (content provider)
*
* В Android имеются следующие встроенные провайдеры, определенные
* в пакете android.content:
*     Alarm  : управление будильником
*     Browser: история браузера и закладки
*     CalendarContract: каледарь и информация о событиях
*     CallLog: информация о звонках
*     ContactsContract: контакты
*     MediaStore: медиа-файлы
*     SearchRecentSuggestions: подсказки по поиску
*     Settings: системные настройки
*     UserDictionary: словарь слов, которые используются для быстрого набора
*     VoicemailContract: записи голосовой почты
*
* Пример доступа к провайдеру контента - работа с контактами
* Контакты в Android обладают встроенным API, который позволяет получать
* и изменять список контактов. Все контакты хранятся в базе данных SQLite,
* однако они не представляют единой таблицы. Для контактов отведено
* три таблицы, связанных отношением один-ко-многим:
*     * таблица информации о людях,
*     * таблица телефонов этих людей
*     * таблица адресов электронной почты этих людей.
* При помощи Android API мы можем абстрагироваться от связей между таблицами.
*/
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;
    ArrayList<String> contacts = new ArrayList<>();
    Button btnAdd;
    EditText contactText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        contactText = findViewById(R.id.edtContact);

        // получаем разрешения
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        // если устройство до API 23, устанавливаем разрешение
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(
                this,
                new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } // if

        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED){
            loadContacts();
        } // if

        btnAdd.setEnabled(READ_CONTACTS_GRANTED);
        btnAdd.setOnClickListener(v -> onAddContact(v));
    } // onCreate

    // обработчик стандартного диалога запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                READ_CONTACTS_GRANTED = true;
            } // if
            btnAdd.setEnabled(READ_CONTACTS_GRANTED);
        } // if

        if (READ_CONTACTS_GRANTED) {
            loadContacts();
        } else {
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        } // if
    } // onRequestPermissionsResult


    // чтение данных провайдера контента
    private void loadContacts(){
        contacts.clear();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(cursor == null) return;

        while (cursor.moveToNext()) {

            // получаем каждый контакт
            @SuppressLint("Range") String contact = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            // добавляем контакт в список
            contacts.add(contact);
        } // while
        cursor.close();

        // создаем адаптер для отображения списка контактов
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);
        // устанавливаем для списка адаптер
        ListView contactList = findViewById(R.id.contactList);
        contactList.setAdapter(adapter);
    } // loadContacts


    // добавление в Контакты
    public void onAddContact(View v) {
        // для записи в базу данных
        ContentValues contactValues = new ContentValues();

        String newContact = contactText.getText().toString();
        contactText.setText("");

        contactValues.put(ContactsContract.RawContacts.ACCOUNT_NAME, newContact);
        contactValues.put(ContactsContract.RawContacts.ACCOUNT_TYPE, newContact);
        Uri newUri = getContentResolver().insert(
                ContactsContract.RawContacts.CONTENT_URI, contactValues);

        long rawContactsId = ContentUris.parseId(newUri);
        contactValues.clear();
        contactValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactsId);
        contactValues.put(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contactValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newContact);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contactValues);

        Toast.makeText(getApplicationContext(),
            newContact + " добавлен в список контактов",
            Toast.LENGTH_LONG).show();
        loadContacts();
    } // onAddContact


    //region Работа с меню приложения
    @Override  // создание меню приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    @Override // обработка выбора в меню приложения
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mniExit) {
            finish();
            return true;
        } // if
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity