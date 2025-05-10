package ru.mirea.dutovas.serviceapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class PlayerService extends Service {
    private MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "PlayerServiceChannel";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            stopForeground(true);
            stopSelf();
        });
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Создание уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Mr.Kitty - Make It Right")
                .setContentText("Сейчас играет")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Make It Right (Feat. Kakoi Nizimine)"));

        // Создание канала уведомлений (для Android 8.0+)
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Player Service",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Канал для уведомлений плеера");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Запуск сервиса с уведомлением
        startForeground(1, builder.build());

        // Инициализация MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.make_it_right);
        mediaPlayer.setLooping(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}