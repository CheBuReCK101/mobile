package ru.mirea.dutovas.employeedb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText, powerEditText, strengthEditText, universeEditText;
    private Button addButton, viewAllButton;
    private TextView resultTextView;
    private AppDatabase db;
    private SuperHeroDao superHeroDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        powerEditText = findViewById(R.id.powerEditText);
        strengthEditText = findViewById(R.id.strengthEditText);
        universeEditText = findViewById(R.id.universeEditText);
        addButton = findViewById(R.id.addButton);
        viewAllButton = findViewById(R.id.viewAllButton);
        resultTextView = findViewById(R.id.resultTextView);

        db = App.getInstance().getDatabase();
        superHeroDao = db.superHeroDao();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSuperHero();
            }
        });

        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllSuperHeroes();
            }
        });
    }

    private void addSuperHero() {
        String name = nameEditText.getText().toString();
        String power = powerEditText.getText().toString();
        String strengthStr = strengthEditText.getText().toString();
        String universe = universeEditText.getText().toString();

        if (name.isEmpty() || power.isEmpty() || strengthStr.isEmpty() || universe.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int strength;
        try {
            strength = Integer.parseInt(strengthStr);
            if (strength < 1 || strength > 10) {
                Toast.makeText(this, "Уровень силы должен быть от 1 до 10", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректный уровень силы", Toast.LENGTH_SHORT).show();
            return;
        }

        SuperHero superHero = new SuperHero(name, power, strength, universe);
        superHeroDao.insert(superHero);
        Toast.makeText(this, "Герой добавлен!", Toast.LENGTH_SHORT).show();

        // Очистка полей после добавления
        nameEditText.setText("");
        powerEditText.setText("");
        strengthEditText.setText("");
        universeEditText.setText("");
    }

    private void showAllSuperHeroes() {
        List<SuperHero> superHeroes = superHeroDao.getAll();
        if (superHeroes.isEmpty()) {
            resultTextView.setText("В базе нет героев");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (SuperHero hero : superHeroes) {
            sb.append("ID: ").append(hero.id)
                    .append("\nИмя: ").append(hero.name)
                    .append("\nСпособность: ").append(hero.power)
                    .append("\nУровень силы: ").append(hero.strengthLevel)
                    .append("\nВселенная: ").append(hero.universe)
                    .append("\n\n");
        }
        resultTextView.setText(sb.toString());
    }
}