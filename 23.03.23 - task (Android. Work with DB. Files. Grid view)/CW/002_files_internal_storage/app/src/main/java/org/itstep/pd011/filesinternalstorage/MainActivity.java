package org.itstep.pd011.filesinternalstorage;

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

        // так можно удалить файл во внутренней памяти приложения
        // внутренняя или локальная память приложения: /data/data/пакет/files
        // this.deleteFile(fileName);
        // String[] files = fileList();

        fileName = "file.txt";
        binFileName = "file.bin";
        txvText = findViewById(R.id.txvText);
    } // onCreate

    // Разрешения Linux/MacOS/Unix
    // d - directory
    // r - read
    // w - write
    // x - execute, open folder
    // - owner  owner group  other
    // d rwx    rwx          rwx

    // локальная память приложения /data/data/пакет/files

    // запись/дозапись в файл, размещенный в локальной памяти
    public void onSaveClickHandler(View view) {
        // получить поток вывода из контекста активности
        // MODE_PRIVATE | MODE_APPEND
        try (FileOutputStream fos = this.openFileOutput(fileName, MODE_PRIVATE | MODE_APPEND)) {
            fos.write("Привет, мир!\n".getBytes());
            fos.write("Еще одна строка текста\n".getBytes());
            fos.write("Spring MVC - круто\n".getBytes());
            fos.write("ASP.NET MVC Core - тоже круто\n".getBytes());
            fos.write("-------------------------\n".getBytes());

            // String msg = getFilesDir().getPath() + "\n" +
            //              getFileStreamPath(fileName).getPath() + "\n" +
            //         "Данные записаны в файл " + fileName;
            String msg = "Данные записаны в файл " + fileName;
            Toast
                .makeText(this, msg,Toast.LENGTH_LONG)
                .show();
        } catch (IOException ex) {
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_LONG).show();
        } // try-catch
    }  // onSaveClickHandler

    // запись/дозапись текста в файл, размещенный в локальной памяти
    public void onTextWriteClickHandler(View view) {
        try (PrintWriter pwr = new PrintWriter(openFileOutput(fileName, MODE_PRIVATE | MODE_APPEND))) {
            pwr.printf(Locale.UK, "Создана дата: %1$tY-%1$tm-%1$td\n", new Date());
            pwr.printf(Locale.UK, "Путь к файлу: %s\n", getFileStreamPath(fileName).getPath());
            pwr.printf("--------------------------------\n");

            Toast
                .makeText(this, "Данные записаны в файл " + fileName,
                        Toast.LENGTH_LONG)
                .show();
        } catch (IOException ex) {
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_LONG).show();
        } // try-catch
    }  // onTextWriteClickHandler

    // чтение из файла, размещенного в локальной памяти
    public void onReadClickHandler(View view) {
        try (FileInputStream fis = this.openFileInput(fileName)) {
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
        txvText.setText("");
        try (Scanner scn = new Scanner(openFileInput(fileName))) {
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
        try (DataOutputStream dos = new DataOutputStream(openFileOutput(binFileName,
                MODE_PRIVATE|MODE_APPEND))) {
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

    // чтение вещественных чисел из бинарного файла
    public void onBinaryReadClickHandler(View view) {

        try (DataInputStream dis = new DataInputStream(openFileInput(binFileName))) {
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
} // class MainActivity