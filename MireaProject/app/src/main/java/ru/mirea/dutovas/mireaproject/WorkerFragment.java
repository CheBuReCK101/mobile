package ru.mirea.dutovas.mireaproject;

import android.content.Context;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class WorkerFragment extends Fragment {
    private Button startWorkButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        startWorkButton = view.findViewById(R.id.start_work_button);
        startWorkButton.setOnClickListener(v -> startBackgroundWork());

        return view;
    }

    private void startBackgroundWork() {
        if (areConstraintsSatisfied()) {
            // Создание запроса
            OneTimeWorkRequest workRequest =
                    new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                            .build();

            // Подписка на запуск
            WorkManager.getInstance(requireContext())
                    .getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(getViewLifecycleOwner(), workInfo -> {
                        if (workInfo != null && workInfo.getState() == androidx.work.WorkInfo.State.RUNNING) {
                            Toast.makeText(getContext(), "Фоновая задача действительно начала выполняться", Toast.LENGTH_SHORT).show();
                        }
                    });

            WorkManager.getInstance(requireContext()).enqueue(workRequest);
        } else {
            Toast.makeText(getContext(), "Не соблюдены требования: устройство должно быть на зарядке и подключено к безлимитной сети", Toast.LENGTH_LONG).show();
        }
    }


    private boolean areConstraintsSatisfied() {
        // Проверка на зарядку
        android.os.BatteryManager batteryManager = (android.os.BatteryManager) requireContext().getSystemService(Context.BATTERY_SERVICE);
        boolean isCharging = batteryManager != null &&
                (batteryManager.isCharging() || batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS) == BatteryManager.BATTERY_STATUS_CHARGING);

        // Проверка на unmetered сеть
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isUnmetered = false;
        if (cm != null) {
            android.net.NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (capabilities != null) {
                isUnmetered = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }

        return isCharging && isUnmetered;
    }

}