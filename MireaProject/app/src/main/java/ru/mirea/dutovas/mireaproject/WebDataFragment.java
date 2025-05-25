package ru.mirea.dutovas.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebDataFragment extends Fragment {

    private TextView textIp, textCity, textRegion, textCountry;
    private TextView textWeather, textTemperature, textWindspeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_data, container, false);

        textIp = view.findViewById(R.id.text_ip);
        textCity = view.findViewById(R.id.text_city);
        textRegion = view.findViewById(R.id.text_region);
        textCountry = view.findViewById(R.id.text_country);
        textWeather = view.findViewById(R.id.text_weather);
        textTemperature = view.findViewById(R.id.text_temperature);
        textWindspeed = view.findViewById(R.id.text_windspeed);

        Button buttonGetIp = view.findViewById(R.id.button_get_ip);
        buttonGetIp.setOnClickListener(v -> {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadPageTask().execute("https://ipinfo.io/json");
            } else {
                Toast.makeText(requireContext(), "Нет интернета", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
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
            try {
                JSONObject responseJson = new JSONObject(result);
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String country = responseJson.getString("country");
                String loc = responseJson.getString("loc");

                textIp.setText("IP: " + ip);
                textCity.setText("Город: " + city);
                textRegion.setText("Регион: " + region);
                textCountry.setText("Страна: " + country);

                String[] coords = loc.split(",");
                new DownloadWeatherTask().execute(
                        "https://api.open-meteo.com/v1/forecast?latitude=" + coords[0] +
                                "&longitude=" + coords[1] + "&current_weather=true");

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
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");

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
            } finally {
                if (inputStream != null) inputStream.close();
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
            try {
                JSONObject responseJson = new JSONObject(result);
                JSONObject currentWeather = responseJson.getJSONObject("current_weather");

                double temperature = currentWeather.getDouble("temperature");
                double windspeed = currentWeather.getDouble("windspeed");
                int weathercode = currentWeather.getInt("weathercode");

                textWeather.setText("Погода: " + getWeatherDescription(weathercode));
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
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    data = sb.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } finally {
                if (inputStream != null) inputStream.close();
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
