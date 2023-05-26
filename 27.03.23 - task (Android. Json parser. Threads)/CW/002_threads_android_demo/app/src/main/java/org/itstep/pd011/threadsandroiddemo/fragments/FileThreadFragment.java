package org.itstep.pd011.threadsandroiddemo.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import org.itstep.pd011.threadsandroiddemo.R;
import org.itstep.pd011.threadsandroiddemo.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.Locale;

/*
 *  Оптимальнее потокам работать с фрагментами, чем напрямую с активностями
 *  */
public class FileThreadFragment extends Fragment {
    // контекст активности, в которой стартовал фрагмент
    private Context context;

    private Button btnReadFile;         // чтение файла
    private Button btnWriteFile;        // запись файла
    private TextView txvFileContent;    // место для вывода контента файла

    private String fileName = "data.bin";

    public FileThreadFragment() {
        // Required empty public constructor
    }

    // перехват события жизненного цикла onAttach -> подключение к активности
    // для доступа к контексту активности - для доступа к файловой системе
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    } // onAttach


    @SuppressLint("NewApi")  // для использования Stream API
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_thread, container, false);

        // получить ссылки на элементы разметки
        txvFileContent = view.findViewById(R.id.txvFileContent);
        btnReadFile = view.findViewById(R.id.btnReadFile);
        btnWriteFile = view.findViewById(R.id.btnWriteFile);

        // если контекст доступен проверим наличие файла, разрешим или
        // запретим кнопку чтения файла при его наличии/отсутсвии соответственно
        if (context != null)
            btnReadFile.setEnabled(Arrays.stream(context.fileList())
                .filter(v -> v.equals(fileName))
                .count() > 0);

        // по кликам на кнопки - запуск потоков обработки
        btnReadFile.setOnClickListener(v -> new Thread(fileReader).start());
        btnWriteFile.setOnClickListener(v -> new Thread(fileWriter).start());

        return view;
    } // onCreateView


    // поток чтения данных
    private Runnable fileReader = () -> {
        try (DataInputStream dis = new DataInputStream(context.openFileInput(fileName))) {
            StringBuilder sbr = new StringBuilder();

            int i = 0;
            while (dis.available() > 0) {
                double t = dis.readDouble();
                sbr.append(String.format(Locale.UK ,"%15.3f", t));
                if (++i % 4 == 0) {
                    sbr.append("\n");
                    // вариант вывода после каждой сформированной строки - много
                    // операций вывода, возможно снижение скорости работы
                    txvFileContent.post(() -> txvFileContent.setText(sbr.toString()));
                } // if
            } // while

            // вывод одной операцией - пауза перед выводом растет с увеличением размера файла
            // txvFileContent.post(() -> txvFileContent.setText(sbr.toString()));
        } catch (Exception e) {
            Log.d("fileReader", "Ошибка чтения, " + e.getMessage());
        } // try-catch
    };


    // поток записи данных
    private Runnable fileWriter = () -> {
        try (DataOutputStream dos = new DataOutputStream(context.openFileOutput(fileName,
                Context.MODE_PRIVATE|Context.MODE_APPEND))) {
            int n = 10 + Utils.random.nextInt(21);
            for (int i = 0; i < n; ++i)
                dos.writeDouble(Utils.getRand(-10, 10));

            txvFileContent.post(() -> {
                txvFileContent.setText("Данные записаны");
                btnReadFile.setEnabled(true);
            });
            // btnReadFile.post(() -> btnReadFile.setEnabled(true));
        } catch (Exception e) {
            Log.d("fileWriter", "Ошибка записи, " + e.getMessage());
        } // try-catch
    };
}