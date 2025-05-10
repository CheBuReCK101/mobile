package ru.mirea.dutovas.thread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import ru.mirea.dutovas.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Показ информации о потоке
        binding.buttonThreadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread mainThread = Thread.currentThread();
                binding.textView.setText("Имя текущего потока: " + mainThread.getName());
                // Меняем имя и выводим в текстовом поле
                mainThread.setName("МОЙ НОМЕР ГРУППЫ: БИСО-01-20, НОМЕР ПО СПИСКУ: 10, МОЙ ЛЮБИМЫЙ ФИЛЬМ: Железный человек");
                binding.textView.append("\nНовое имя потока: " + mainThread.getName());
                Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
                Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());
            }
        });

        // Расчет среднего количества пар
        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверка ввода
                if (binding.editTextTotalClasses.getText().toString().isEmpty() ||
                        binding.editTextStudyDays.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите оба значения", Toast.LENGTH_SHORT).show();
                    return;
                }

                int totalClasses = Integer.parseInt(binding.editTextTotalClasses.getText().toString());
                int studyDays = Integer.parseInt(binding.editTextStudyDays.getText().toString());

                // Запуск фонового потока для расчета
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format(
                                "Запущен поток № %d студентом группы № %s номер по списку № %d",
                                numberThread, "БИСО-01-20", -10));

                        // Расчет среднего количества пар
                        final double average = (double) totalClasses / studyDays;

                        // Обновление UI из главного потока
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewResult.setText(String.format(
                                        "Среднее количество пар в день: %.2f", average));
                            }
                        });

                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                    }
                }).start();
            }
        });
    }
}