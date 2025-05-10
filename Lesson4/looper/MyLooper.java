//public class MyLooper {
//}
//package ru.mirea.dutovas.looper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String age = bundle.getString("AGE");
                String job = bundle.getString("JOB");
                int delay = Integer.parseInt(age) * 1000;

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                Bundle resultBundle = new Bundle();
                resultBundle.putString("RESULT", "Профессия: " + job + ", Возраст: " + age);
                message.setData(resultBundle);
                mainHandler.sendMessage(message);
            }
        };

        Looper.loop();
    }
}