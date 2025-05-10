package ru.mirea.dutovas.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class BackgroundWorker extends Worker {
    private static final String TAG = "BackgroundWorker";

    public BackgroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(TAG, "Фоновая задача начата");

        try {
            // Имитация долгой операции (10 секунд)
            TimeUnit.SECONDS.sleep(10);

            // Здесь можно выполнить реальную фоновую работу:
            // - загрузку данных
            // - обработку файлов
            // - синхронизацию с сервером и т.д.

            Log.d(TAG, "Фоновая задача успешно завершена");
            return Result.success();
        } catch (InterruptedException e) {
            Log.e(TAG, "Задача прервана", e);
            return Result.failure();
        }
    }
}