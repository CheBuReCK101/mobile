package ru.mirea.dutovas.control_lesson1;

import static ru.mirea.dutovas.control_lesson1.R.id.buttonsus;
import static ru.mirea.dutovas.control_lesson1.R.id.checkBox;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TextView myTextView = findViewById(R.id.textViewStudent);
        //TextView myTextView = (TextView) findViewById(R.id.textViewStudent);
        myTextView.setText("New text in MIREA");
        Log.d(TAG, "Мой код выполняется!");
        Button button = findViewById(buttonsus);
        button.setText("MireaButton");

        ImageView imageView = findViewById(R.id.imageView);

        // Загрузка GIF из ресурсов
        Glide.with(this)
                .asGif()
                .load(R.drawable.among_us_twerk) // Укажите ваш GIF-файл в папке res/drawable
                .into(imageView);

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}