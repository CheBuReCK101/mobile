package ru.mirea.dutovas.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class UploadWorker extends Worker {
    private static final String TAG = "UploadWorker";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: started");

        try {
            // Имитация долгой работы (10 секунд)
            TimeUnit.SECONDS.sleep(10);

            // Получение входных данных
            String inputData = getInputData().getString("KEY_DATA");
            Log.d(TAG, "Input data: " + inputData);

            // Возвращаем результат с выходными данными
            return Result.success();
        } catch (InterruptedException e) {
            Log.e(TAG, "Error during work", e);
            return Result.failure();
        }
    }
}