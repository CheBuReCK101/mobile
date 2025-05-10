package ru.mirea.dutovas.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.crypto.SecretKey;

import ru.mirea.dutovas.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {
    private ActivityMainBinding binding;
    private final int LoaderID = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите сообщение", Toast.LENGTH_SHORT).show();
                    return;
                }

                SecretKey secretKey = CryptoUtils.generateKey();
                byte[] encryptedMessage = CryptoUtils.encryptMsg(message, secretKey);

                Bundle bundle = new Bundle();
                bundle.putByteArray(MyLoader.ARG_WORD, encryptedMessage);
                bundle.putByteArray(MyLoader.ARG_KEY, secretKey.getEncoded());

                LoaderManager.getInstance(MainActivity.this).initLoader(LoaderID, bundle, MainActivity.this);
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LoaderID) {
            Toast.makeText(this, "Загрузчик создан", Toast.LENGTH_SHORT).show();
            return new MyLoader(this, args);
        }
        throw new IllegalArgumentException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data != null) {
            Toast.makeText(this, "Расшифрованное сообщение: " + data, Toast.LENGTH_LONG).show();
            Log.d("MainActivity", "Расшифрованное сообщение: " + data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d("MainActivity", "onLoaderReset");
    }
}