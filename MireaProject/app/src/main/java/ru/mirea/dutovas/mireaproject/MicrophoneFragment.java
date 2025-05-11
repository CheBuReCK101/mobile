package ru.mirea.dutovas.mireaproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class MicrophoneFragment extends Fragment {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String TAG = "MicrophoneFragment";

    private MediaRecorder recorder;
    private MediaPlayer player;
    private String recordFilePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    private Button recordButton;
    private Button playButton;
    private TextView statusText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        recordButton = view.findViewById(R.id.record_button);
        playButton = view.findViewById(R.id.play_button);
        statusText = view.findViewById(R.id.status_text);

        recordFilePath = new File(requireContext().getFilesDir(), "audio_record.3gp").getAbsolutePath();
        Log.d(TAG, "Файл будет сохранен: " + recordFilePath);

        recordButton.setOnClickListener(v -> toggleRecording());
        playButton.setOnClickListener(v -> togglePlayback());

        if (checkPermission()) {
            setupButtons(true);
        } else {
            requestPermission();
        }

        return view;
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
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
            statusText.setText("Запись остановлена");
        } else {
            startRecording();
            recordButton.setText("Остановить запись");
            playButton.setEnabled(false);
            statusText.setText("Идет запись...");
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
            Toast.makeText(getContext(), "Запись началась", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Ошибка подготовки записи", e);
            Toast.makeText(getContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                Toast.makeText(getContext(), "Запись сохранена", Toast.LENGTH_SHORT).show();
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
            statusText.setText("Воспроизведение остановлено");
        } else {
            startPlaying();
            playButton.setText("Остановить");
            recordButton.setEnabled(false);
            statusText.setText("Идет воспроизведение...");
        }
        isPlaying = !isPlaying;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            Toast.makeText(getContext(), "Воспроизведение начато", Toast.LENGTH_SHORT).show();

            player.setOnCompletionListener(mp -> {
                stopPlaying();
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
                statusText.setText("Воспроизведение завершено");
                isPlaying = false;
            });
        } catch (IOException e) {
            Log.e(TAG, "Ошибка воспроизведения", e);
            Toast.makeText(getContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Нужно разрешение на запись аудио!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
        if (isPlaying) {
            stopPlaying();
        }
    }
}