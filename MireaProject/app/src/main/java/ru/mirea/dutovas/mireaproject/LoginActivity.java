//package ru.mirea.dutovas.mireaproject.auth;

package ru.mirea.dutovas.mireaproject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.dutovas.mireaproject.MainActivity;
import ru.mirea.dutovas.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.emailSignInButton.setOnClickListener(v -> signIn(
                binding.fieldEmail.getText().toString(),
                binding.fieldPassword.getText().toString()
        ));

        binding.emailCreateAccountButton.setOnClickListener(v -> createAccount(
                binding.fieldEmail.getText().toString(),
                binding.fieldPassword.getText().toString()
        ));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startMainActivity();
        }
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        startMainActivity();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        startMainActivity();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.fieldEmail.getText().toString();
        if (email.isEmpty()) {
            binding.fieldEmail.setError("Обязательное поле");
            valid = false;
        } else {
            binding.fieldEmail.setError(null);
        }

        String password = binding.fieldPassword.getText().toString();
        if (password.isEmpty()) {
            binding.fieldPassword.setError("Обязательное поле");
            valid = false;
        } else {
            binding.fieldPassword.setError(null);
        }

        return valid;
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}