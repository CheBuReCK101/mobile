package ru.mirea.dutovas.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        Button buttonCount = findViewById(R.id.buttonCount);

        buttonCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                int charCount = text.length();

                // Ваши данные (замените на актуальные)
                String studentNumber = "10"; // Номер студента по списку
                String groupNumber = "БИСО-01-20"; // Номер группы

                String message = String.format(
                        "СТУДЕНТ № %s ГРУППА %s Количество символов - %d",
                        studentNumber,
                        groupNumber,
                        charCount
                );

                Toast.makeText(
                        MainActivity.this,
                        message,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}