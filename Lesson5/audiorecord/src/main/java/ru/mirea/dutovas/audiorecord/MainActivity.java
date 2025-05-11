package ru.mirea.dutovas.audiorecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String TAG = "AudioRecordApp";

    private MediaRecorder recorder;
    private MediaPlayer player;
    private String recordFilePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    private Button recordButton, playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация кнопок
        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);

        // Путь к файлу во внутреннем хранилище
        recordFilePath = new File(getFilesDir(), "audio_record.3gp").getAbsolutePath();
        Log.d(TAG, "Файл будет сохранен: " + recordFilePath);

        // Настройка обработчиков
        recordButton.setOnClickListener(v -> toggleRecording());
        playButton.setOnClickListener(v -> togglePlayback());

        // Проверка разрешения
        if (checkPermission()) {
            setupButtons(true);
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private void setupButtons(boolean enabled) {
        recordButton.setEnabled(enabled);
        playButton.setEnabled(enabled && new File(recordFilePath).exists());
    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
            recordButton.setText("Начать запись");
            playButton.setEnabled(true);
        } else {
            startRecording();
            recordButton.setText("Остановить запись");
            playButton.setEnabled(false);
        }
        isRecording = !isRecording;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(recordFilePath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "Запись началась", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Ошибка подготовки записи", e);
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Ошибка остановки записи", e);
            } finally {
                recorder = null;
            }
        }
    }

    private void togglePlayback() {
        if (isPlaying) {
            stopPlaying();
            playButton.setText("Воспроизвести");
            recordButton.setEnabled(true);
        } else {
            startPlaying();
            playButton.setText("Остановить");
            recordButton.setEnabled(false);
        }
        isPlaying = !isPlaying;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            Toast.makeText(this, "Воспроизведение начато", Toast.LENGTH_SHORT).show();

            player.setOnCompletionListener(mp -> {
                stopPlaying();
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
                isPlaying = false;
            });
        } catch (IOException e) {
            Log.e(TAG, "Ошибка воспроизведения", e);
            Toast.makeText(this, "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupButtons(true);
            } else {
                Toast.makeText(this, "Нужно разрешение на запись аудио!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
        if (isPlaying) {
            stopPlaying();
        }
    }
}