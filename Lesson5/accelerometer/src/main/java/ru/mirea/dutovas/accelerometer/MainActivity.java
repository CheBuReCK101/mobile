package ru.mirea.dutovas.accelerometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView azimuthTextView;
    private TextView pitchTextView;
    private TextView rollTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка обработчика системных окон (для корректного отображения на всех устройствах)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Инициализация SensorManager и акселерометра
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Получение ссылок на TextView
        azimuthTextView = findViewById(R.id.textViewAzimuth);
        pitchTextView = findViewById(R.id.textViewPitch);
        rollTextView = findViewById(R.id.textViewRoll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Регистрация слушателя сенсора с нормальной частотой обновления
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Отмена регистрации слушателя для экономии заряда батареи
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Проверяем, что событие пришло от акселерометра
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0]; // Ускорение по оси X (поперечное)
            float y = event.values[1]; // Ускорение по оси Y (продольное)
            float z = event.values[2]; // Ускорение по оси Z (вертикальное)

            // Обновляем текстовые поля с новыми значениями
            azimuthTextView.setText(String.format("X (Azimuth): %.2f m/s²", x));
            pitchTextView.setText(String.format("Y (Pitch): %.2f m/s²", y));
            rollTextView.setText(String.format("Z (Roll): %.2f m/s²", z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Метод вызывается при изменении точности датчика
        // Можно добавить обработку, если необходимо
    }
}