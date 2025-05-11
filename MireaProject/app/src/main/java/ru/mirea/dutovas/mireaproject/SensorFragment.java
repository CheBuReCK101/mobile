package ru.mirea.dutovas.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private ImageView compassImage;
    private TextView directionText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        compassImage = view.findViewById(R.id.compass_image);
        directionText = view.findViewById(R.id.direction_text);
        compassImage.setImageResource(R.drawable.ic_compass);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        if (accelerometer == null || magnetometer == null) {
            directionText.setText("Датчики не доступны на этом устройстве");
        }

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            lastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float azimuthInRadians = orientation[0];
            float azimuthInDegrees = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

            compassImage.setRotation(-azimuthInDegrees);

            String direction;
            if (azimuthInDegrees >= 337.5 || azimuthInDegrees < 22.5) {
                direction = "С: " + (int)azimuthInDegrees + "° (Север)";
            } else if (azimuthInDegrees >= 22.5 && azimuthInDegrees < 67.5) {
                direction = "С-В: " + (int)azimuthInDegrees + "° (Северо-Восток)";
            } else if (azimuthInDegrees >= 67.5 && azimuthInDegrees < 112.5) {
                direction = "В: " + (int)azimuthInDegrees + "° (Восток)";
            } else if (azimuthInDegrees >= 112.5 && azimuthInDegrees < 157.5) {
                direction = "Ю-В: " + (int)azimuthInDegrees + "° (Юго-Восток)";
            } else if (azimuthInDegrees >= 157.5 && azimuthInDegrees < 202.5) {
                direction = "Ю: " + (int)azimuthInDegrees + "° (Юг)";
            } else if (azimuthInDegrees >= 202.5 && azimuthInDegrees < 247.5) {
                direction = "Ю-З: " + (int)azimuthInDegrees + "° (Юго-Запад)";
            } else if (azimuthInDegrees >= 247.5 && azimuthInDegrees < 292.5) {
                direction = "З: " + (int)azimuthInDegrees + "° (Запад)";
            } else {
                direction = "С-З: " + (int)azimuthInDegrees + "° (Северо-Запад)";
            }

            directionText.setText(direction);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            }
            if (magnetometer != null) {
                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Не используется, но требуется реализовать
    }
}