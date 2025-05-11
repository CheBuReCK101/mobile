package ru.mirea.dutovas.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.dutovas.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final String TAG = "CameraApp";
    private ActivityMainBinding binding;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Проверка наличия камеры
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, "На устройстве нет камеры", Toast.LENGTH_LONG).show();
            return;
        }

        binding.imageView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Не найдено приложение камеры", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoFile = createImageFile();
        if (photoFile == null) {
            Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            return;
        }

        photoUri = FileProvider.getUriForFile(this,
                "ru.mirea.dutovas.camera.fileprovider",
                photoFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePictureIntent, REQUEST_CAMERA_PERMISSION);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        try {
            File storageDir = new File(getFilesDir(), "images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            Log.e(TAG, "Ошибка создания файла", e);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_PERMISSION && resultCode == RESULT_OK) {
            binding.imageView.setImageURI(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Нужно разрешение камеры", Toast.LENGTH_SHORT).show();
            }
        }
    }
}