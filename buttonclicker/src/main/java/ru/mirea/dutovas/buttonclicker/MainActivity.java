package ru.mirea.dutovas.buttonclicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Сначала устанавливаем разметку

        EdgeToEdge.enable(this);

        // Связываем элементы интерфейса после setContentView()
        textViewStudent = findViewById(R.id.textViewStudent);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);

        // Обработчик кнопки "WhoAmI"
        btnWhoAmI.setOnClickListener(v -> textViewStudent.setText("Мой номер по списку № 6"));

        // Обработчик кнопки "ItIsNotMe"
        btnItIsNotMe.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Не я!", Toast.LENGTH_SHORT).show()
        );

        // Корректная работа с отступами окна
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Обработчик кнопки "ItIsNotMe2"
    public void onMyButtonClick(View view) {
        Toast.makeText(this, "Ещё один способ!", Toast.LENGTH_SHORT).show();
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(true); // Установить состояние "выбран"
    }
}
