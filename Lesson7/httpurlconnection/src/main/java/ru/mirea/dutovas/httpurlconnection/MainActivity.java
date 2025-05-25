package ru.mirea.dutovas.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView textIp, textCity, textRegion, textCountry;
    private TextView textWeather, textTemperature, textWindspeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textIp = findViewById(R.id.text_ip);
        textCity = findViewById(R.id.text_city);
        textRegion = findViewById(R.id.text_region);
        textCountry = findViewById(R.id.text_country);
        textWeather = findViewById(R.id.text_weather);
        textTemperature = findViewById(R.id.text_temperature);
        textWindspeed = findViewById(R.id.text_windspeed);

        findViewById(R.id.button_get_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = null;
                if (connectivityManager != null) {
                    networkinfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkinfo != null && networkinfo.isConnected()) {
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textIp.setText("Загружаем данные...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(MainActivity.class.getSimpleName(), result);

            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);

                // Получаем данные IP
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String country = responseJson.getString("country");
                String loc = responseJson.getString("loc");

                textIp.setText("IP: " + ip);
                textCity.setText("Город: " + city);
                textRegion.setText("Регион: " + region);
                textCountry.setText("Страна: " + country);

                // Получаем координаты
                String[] coords = loc.split(",");
                String latitude = coords[0];
                String longitude = coords[1];

                // Запрашиваем погоду по координатам
                new DownloadWeatherTask().execute(
                        "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                                "&longitude=" + longitude + "&current_weather=true");

            } catch (JSONException e) {
                e.printStackTrace();
                textIp.setText("Ошибка разбора данных");
            }
        }

        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    bos.close();
                    data = bos.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadWeatherInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(MainActivity.class.getSimpleName(), "Weather: " + result);

            try {
                JSONObject responseJson = new JSONObject(result);
                JSONObject currentWeather = responseJson.getJSONObject("current_weather");

                double temperature = currentWeather.getDouble("temperature");
                double windspeed = currentWeather.getDouble("windspeed");
                int weathercode = currentWeather.getInt("weathercode");

                String weatherDescription = getWeatherDescription(weathercode);

                textWeather.setText("Погода: " + weatherDescription);
                textTemperature.setText("Температура: " + temperature + "°C");
                textWindspeed.setText("Скорость ветра: " + windspeed + " км/ч");

            } catch (JSONException e) {
                e.printStackTrace();
                textWeather.setText("Ошибка получения погоды");
            }
        }

        private String downloadWeatherInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    data = stringBuilder.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }

        private String getWeatherDescription(int code) {
            switch (code) {
                case 0: return "Ясно";
                case 1: return "Преимущественно ясно";
                case 2: return "Переменная облачность";
                case 3: return "Пасмурно";
                case 45: case 48: return "Туман";
                case 51: case 53: case 55: return "Морось";
                case 56: case 57: return "Ледяная морось";
                case 61: case 63: case 65: return "Дождь";
                case 66: case 67: return "Ледяной дождь";
                case 71: case 73: case 75: return "Снег";
                case 77: return "Снежные зерна";
                case 80: case 81: case 82: return "Ливень";
                case 85: case 86: return "Снегопад";
                case 95: return "Гроза";
                case 96: case 99: return "Гроза с градом";
                default: return "Неизвестно";
            }
        }
    }
}