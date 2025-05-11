package ru.mirea.dutovas.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ImageView capturedImage;
    private Uri imageUri;
    private boolean isWork = false;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        capturedImage = view.findViewById(R.id.captured_image);
        Button captureButton = view.findViewById(R.id.capture_button);

        // Инициализация launcher для камеры
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        capturedImage.setImageURI(imageUri);
                    }
                });

        // Проверка разрешений
        checkPermissions();

        captureButton.setOnClickListener(v -> {
            if (isWork) {
                try {
                    File photoFile = createImageFile();
                    String authorities = requireContext().getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(
                            requireContext(),
                            authorities,
                            photoFile);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Log.d("CameraFragment", "Launching camera intent...");
                    cameraLauncher.launch(cameraIntent);
                } catch (IOException e) {
                    Log.e("CameraFragment", "Error creating file", e);
                    Toast.makeText(getContext(), "Ошибка при создании файла", Toast.LENGTH_SHORT).show();
                }
            } else {
                checkPermissions(); // Повторная проверка
                Toast.makeText(getContext(), "Нужны разрешения для камеры", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void checkPermissions() {
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext().getFilesDir();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isWork = true;
            } else {
                Toast.makeText(getContext(), "Нужно разрешение для камеры!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
