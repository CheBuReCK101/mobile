package ru.mirea.dutovas.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.mirea.dutovas.workmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStartWork.setOnClickListener(v -> startWork());
    }

    private void startWork() {
        // Создание входных данных
        Data inputData = new Data.Builder()
                .putString("KEY_DATA", "Данные для Worker")
                .build();

        // Установка ограничений
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // Только Wi-Fi
                .setRequiresCharging(true) // Только при зарядке
                .build();

        // Создание запроса на выполнение работы
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        // Запуск работы
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);

        // Наблюдение за состоянием работы
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null) {
                        String status = "Статус: " + workInfo.getState().name();
                        binding.textStatus.setText(status);

                        if (workInfo.getState().isFinished()) {
                            Toast.makeText(this, "Работа завершена", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}