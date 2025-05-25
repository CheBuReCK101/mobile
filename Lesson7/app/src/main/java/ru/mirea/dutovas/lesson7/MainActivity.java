package ru.mirea.dutovas.lesson7;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.dutovas.lesson7.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String host = "nist.time.gov"; // Обновленный хост
    private final int port = 13;
    private static final String TAG = "SocketTimeApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Запрос времени запущен");
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                Log.d(TAG, "Попытка подключения к " + host + ":" + port);
                Socket socket = new Socket(host, port);
                Log.d(TAG, "Соединение установлено");

                BufferedReader reader = SocketUtils.getReader(socket);
                String firstLine = reader.readLine(); // первая строка
                Log.d(TAG, "Первая строка ответа: " + firstLine);

                timeResult = reader.readLine(); // вторая строка
                Log.d(TAG, "Вторая строка ответа: " + timeResult);

                socket.close();
                Log.d(TAG, "Соединение закрыто");
            } catch (IOException e) {
                Log.e(TAG, "Ошибка соединения", e);
                return "Ошибка: " + e.getMessage();
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "Обработка результата: " + result);

            if (result.startsWith("Ошибка")) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                String[] parts = result.trim().split("\\s+");
                Log.d(TAG, "Разделено на " + parts.length + " частей");

                for (int i = 0; i < parts.length; i++) {
                    Log.d(TAG, "Часть " + i + ": " + parts[i]);
                }

                if (parts.length >= 3) {
                    String dateStr = parts[1];
                    String timeStr = parts[2];
                    Log.d(TAG, "Дата: " + dateStr + ", Время: " + timeStr);

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yy-MM-dd", Locale.US);
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

                    Date date = inputFormat.parse(dateStr);
                    String formattedDate = outputDateFormat.format(date);
                    Log.d(TAG, "Отформатированная дата: " + formattedDate);

                    binding.textViewDate.setText("Дата: " + formattedDate);
                    binding.textViewTime.setText("Время: " + timeStr);
                } else {
                    String errorMsg = "Неверный формат данных: " + result;
                    Log.e(TAG, errorMsg);
                    binding.textViewDate.setText(errorMsg);
                    binding.textViewTime.setText("");
                }
            } catch (ParseException e) {
                String errorMsg = "Ошибка разбора даты: " + e.getMessage();
                Log.e(TAG, errorMsg, e);
                binding.textViewDate.setText(errorMsg);
                binding.textViewTime.setText(result);
            }
        }
    }
}