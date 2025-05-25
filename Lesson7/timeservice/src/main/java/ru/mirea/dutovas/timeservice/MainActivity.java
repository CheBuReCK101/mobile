package ru.mirea.dutovas.timeservice;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.dutovas.timeservice.databinding.ActivityMainBinding;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "TimeServiceApp";
    private static final String TIME_API_URL = "https://quan.suning.com/getSysTime.do";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> {
            Log.d(TAG, "Запрос времени запущен");
            requestTime();
        });
    }

    private void requestTime() {
        Request request = new Request.Builder()
                .url(TIME_API_URL)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Ошибка запроса", e);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.textViewDate.setText("Ошибка: " + e.getMessage());
                    binding.textViewTime.setText("");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Код ответа: " + response.code());
                }

                String responseBody = response.body().string();
                Log.d(TAG, "Ответ сервера: " + responseBody);

                try {
                    JSONObject json = new JSONObject(responseBody);
                    String sysTime1 = json.getString("sysTime1"); // 20250523184147

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                    Date date = inputFormat.parse(sysTime1);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

                    // Китайское время
                    timeFormat.setTimeZone(TimeZone.getTimeZone("GMT-2"));
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-2"));

                    String formattedDate = dateFormat.format(date);
                    String formattedTime = timeFormat.format(date);

                    runOnUiThread(() -> {
                        binding.textViewDate.setText("Дата: " + formattedDate);
                        binding.textViewTime.setText("Время: " + formattedTime + " (GMT+3)");
                    });

                } catch (Exception e) {
                    Log.e(TAG, "Ошибка обработки JSON", e);
                    runOnUiThread(() -> {
                        binding.textViewDate.setText("Ошибка разбора данных");
                        binding.textViewTime.setText(responseBody);
                    });
                }
            }
        });
    }

}
