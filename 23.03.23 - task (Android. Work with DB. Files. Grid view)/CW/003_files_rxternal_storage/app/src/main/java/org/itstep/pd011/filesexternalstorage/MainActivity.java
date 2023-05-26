package org.itstep.pd011.filesexternalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private String fileName;      // имя текстового файла
    private String binFileName;   // имя бинарного файла

    // для отображения данных, прочитанных из файла
    private TextView txvText;

    // для формирования данных бинарного файла
    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileName = "file.txt";
        binFileName = "file.bin";
        txvText = findViewById(R.id.txvText);
    } // onCreate

    // получение пути к внешнему хранилищу
    // storage/emulated/0/Android/data/[название_пакета]/files
    // storage/self/primary/Android/data/[название_пакета]/files
    private File getExternalPath(String fileName) {
        return new File(getExternalFilesDir(null), fileName);
    } // getExternalPath

    // Разрешения
    // d - directory
    // r - read
    // w - write
    // x - execute, open folder
    // d rwx rwx rwx


    // запись/дозапись в файл, размещенный в внешней памяти
    public void onSaveClickHandler(View view) {
        // получить поток вывода из контекста активности
        try (FileOutputStream fos = new FileOutputStream(this.getExternalPath(fileName), true)) {
            fos.write("Привет, мир!\n".getBytes());
            fos.write("Еще одна строка текста\n".getBytes());
            fos.write("Spring MVC - круто\n".getBytes());
            fos.write("Android Stdio :(\n".getBytes());
            fos.write("-------------------------------------\n".getBytes());

            Toast
                .makeText(this, "Данные записаны в файл " + fileName,
                        Toast.LENGTH_LONG)
                .show();
        } catch (IOException ex) {
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_LONG).show();
        } // try-catch
    }  // onSaveClickHandler

    // TODO: нет записи в файл
    // запись/дозапись текста в файл, размещенный во внешней памяти
    public void onTextWriteClickHandler(View view) {
        try (PrintWriter pwr = new PrintWriter(new FileOutputStream(this.getExternalPath(binFileName), true))) {
            pwr.printf(Locale.UK, "Cформирована дата: %1$tY-%1$tm-%1$td\n", new Date());
            pwr.printf(Locale.UK, "Имя каталога: %s\n", getExternalFilesDir(null));
            pwr.printf("--------------------------------\n");

            Toast
                .makeText(this, "Данные записаны в файл " + fileName,
                        Toast.LENGTH_LONG)
                .show();
        } catch (IOException ex) {
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_LONG).show();
        } // try-catch
    }  // onTextWriteClickHandler

    // чтение из файла, размещенного во внешней памяти
    public void onReadClickHandler(View view) {
        // чистим поле вывода
        txvText.setText("");

        try (FileInputStream fis = new FileInputStream(getExternalPath(fileName))) {
            // fis.available() - размер файла в байтах
            byte[] bytes = new byte[fis.available()];

            // читаем весь файл в массив байтов
            fis.read(bytes);

            // сформировать и вывести строку из прочитанного массива байта
            txvText.setText(new String(bytes));
        } catch (IOException ex) {
            Toast.makeText(this, "Ошибка чтения", Toast.LENGTH_LONG).show();
        } // try-catch
    }  // onReadClickHandler

    // демонстрация построчного чтения текстового файла при помощи  Scanner
    public void onTextReadClickHandler(View view) {
        // чистим поле вывода
        txvText.setText("");

        try (Scanner scn = new Scanner(new FileInputStream(getExternalPath(fileName)))) {
            StringBuilder sbr = new StringBuilder();

            // демонстрация построчного чтения
            while(scn.hasNext()) {
                sbr.append(scn.nextLine()).append("\n");
            } // while

            // вывести прочитанный текст
            txvText.setText(sbr.toString());
        } catch (IOException ex) {
            Toast.makeText(this, "Ошибка чтения", Toast.LENGTH_LONG).show();
        } // try-catch
    }  // onTextReadClickHandler


    // запись/дозапись 10 вещественных чисел в бинарный файл
    public void onBinaryWriteClickHandler(View view) {
        int n = 10;
        double lo = -10., hi = 10.;
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(getExternalPath(binFileName), true))) {
            for (int i = 0; i < n; ++i) {
                dos.writeDouble(lo + (hi - lo)*random.nextDouble());
            }

            Toast
                .makeText(this, "Данные записаны в файл " + binFileName,
                        Toast.LENGTH_LONG)
                .show();
        } catch (Exception ex) {
            Toast
                .makeText(this, "Ошибка записи в бинарный файл " + binFileName,
                        Toast.LENGTH_LONG)
                .show();
        } // try-catch
    } // onBinaryWriteClickHandler

    // TODO: не идет чтение
    // чтение вещественных чисел из бинарного файла
    public void onBinaryReadClickHandler(View view) {

        // чистим поле вывода
        txvText.setText("");

        try (DataInputStream dis = new DataInputStream(
                new FileInputStream(getExternalPath(binFileName)))) {
            int i = 1;
            StringBuilder sb = new StringBuilder();

            // чтение и обработка данных
            while(dis.available() > 0) {
                double t = dis.readDouble();

                sb.append(String.format(Locale.UK, "%15.3f", t));
                if (i % 3 == 0) sb.append("\n");
                ++i;
            } // while

            // вывод данных в поле вывода
            txvText.setText(sb.toString());
        } catch (Exception ex) {
            Toast
                .makeText(this, "Ошибка чтения из бинарного файла " + binFileName,
                        Toast.LENGTH_LONG)
                .show();
        } // try-catch
    } // onBinaryReadClickHandler

    //region Меню приложения
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // обработчик выбора меню
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // оставляем switсh в надежде на развитие меню
        switch(item.getItemId()) {
            // выход из приложения
            case R.id.mniExit:
                finish();
                break;
        } // switch
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}