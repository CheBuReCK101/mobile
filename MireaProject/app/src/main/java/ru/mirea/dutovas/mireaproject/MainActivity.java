package ru.mirea.dutovas.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.dutovas.mireaproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Получаем NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment == null) {
            Toast.makeText(this, "NavHostFragment not found!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Получаем NavController
        NavController navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_data,
                R.id.nav_webview,
                R.id.nav_worker,
                R.id.nav_sensor,
                R.id.nav_camera,
                R.id.nav_microphone,
                R.id.nav_profile,
                R.id.nav_file_operations,
                R.layout.fragment_web_data)
                .setOpenableLayout(drawer)
                .build();

        // Настройка ActionBar и NavigationView
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Обработка нажатий на элементы меню (в том числе "Выйти")
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut(); // Разлогиниваем
                Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }

            // Остальные пункты — стандартный переход
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawer.closeDrawers();
            }
            return handled;
        });

        // Настройка FAB
        FloatingActionButton fab = binding.appBarMain.fab;
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_file_operations) {
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(view -> {
                    // Получаем текущий фрагмент
                    Fragment currentFragment = navHostFragment.getChildFragmentManager()
                            .getFragments().get(0);

                    if (currentFragment instanceof FileOperationsFragment) {
                        ((FileOperationsFragment) currentFragment).showNewNoteDialog();
                    }
                });
            } else {
                fab.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
