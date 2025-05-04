package ru.mirea.dutovas.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//public class ShareActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_share);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}

public class ShareActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Получение данных из MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView textViewDevBook = findViewById(R.id.textViewDevBook);
            TextView textViewDevQuote = findViewById(R.id.textViewDevQuote);

            String bookName = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quote = extras.getString(MainActivity.QUOTES_KEY);

            textViewDevBook.setText("Любимая книга разработчика: " + bookName);
            textViewDevQuote.setText("Цитата: " + quote);
        }

        // Обработка отправки данных
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            EditText editTextUserBook = findViewById(R.id.editTextUserBook);
            EditText editTextUserQuote = findViewById(R.id.editTextUserQuote);

            String userBook = editTextUserBook.getText().toString();
            String userQuote = editTextUserQuote.getText().toString();

            String resultText = "Название Вашей любимой книги: " + userBook +
                    ". Цитата: " + userQuote;

            Intent data = new Intent();
            data.putExtra(MainActivity.USER_MESSAGE, resultText);
            setResult(Activity.RESULT_OK, data);
            finish();
        });
    }
}