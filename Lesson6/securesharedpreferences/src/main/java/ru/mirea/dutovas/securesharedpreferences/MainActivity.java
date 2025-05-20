package ru.mirea.dutovas.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private TextView poetNameTextView;
    private ImageView poetImageView;
    private Button saveButton;
    private SharedPreferences secureSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poetNameTextView = findViewById(R.id.poetNameTextView);
        poetImageView = findViewById(R.id.poetImageView);
        saveButton = findViewById(R.id.saveButton);

        try {
            // Создание мастер-ключа
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // Инициализация EncryptedSharedPreferences
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Загрузка данных
            String poetName = secureSharedPreferences.getString("POET_NAME", "Александр Пушкин");
            poetNameTextView.setText(poetName);

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при инициализации безопасного хранилища", Toast.LENGTH_SHORT).show();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Сохранение данных (можно изменить на вашего любимого поэта)
            secureSharedPreferences.edit()
                    .putString("POET_NAME", "Айзек Азимов")
                    .apply();

            Toast.makeText(this, "Данные сохранены безопасно", Toast.LENGTH_SHORT).show();
            poetNameTextView.setText("Айзек Азимов");

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при сохранении данных", Toast.LENGTH_SHORT).show();
        }
    }
}