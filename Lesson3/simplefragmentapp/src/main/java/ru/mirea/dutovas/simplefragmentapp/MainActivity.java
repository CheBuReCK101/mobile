package ru.mirea.dutovas.simplefragmentapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.mirea.dutovas.simplefragmentapp.FirstFragment;
import ru.mirea.dutovas.simplefragmentapp.R;
import ru.mirea.dutovas.simplefragmentapp.SecondFragment;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment1, fragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v,
                                                                            insets) -> {
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                    systemBars.bottom);
            return insets;
        });
        fragment1 = new FirstFragment();
        fragment2 = new SecondFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Button btnFirstFragment = (Button) findViewById(R.id.btnFirstFragment);
            btnFirstFragment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                            fragment1).commit();
                }
            });
            Button btnSecondFragment = (Button) findViewById(R.id.btnSecondFragment);
            btnSecondFragment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                            fragment2).commit();
                }
            });
        }
    }


}