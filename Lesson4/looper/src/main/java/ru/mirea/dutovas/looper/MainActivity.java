package ru.mirea.dutovas.looper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import ru.mirea.dutovas.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("RESULT");
                Log.d("MainActivity", result);
            }
        };

        MyLooper myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("AGE", binding.editTextAge.getText().toString());
                bundle.putString("JOB", binding.editTextJob.getText().toString());
                msg.setData(bundle);

                if (myLooper.mHandler != null) {
                    myLooper.mHandler.sendMessage(msg);
                }
            }
        });
    }
}