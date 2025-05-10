package ru.mirea.dutovas.serviceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.dutovas.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Проверка и запрос разрешений
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.POST_NOTIFICATIONS,
                            Manifest.permission.FOREGROUND_SERVICE
                    },
                    PERMISSION_REQUEST_CODE);
        }

        // Обработчики кнопок
        binding.playButton.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
            ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
        });

        binding.stopButton.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
            stopService(serviceIntent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Разрешения получены");
            } else {
                Log.d("MainActivity", "Разрешения не получены");
            }
        }
    }
}