package ru.mirea.dutovas.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    public static final String ARG_KEY = "key";
    private byte[] cryptText;
    private byte[] key;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            cryptText = args.getByteArray(ARG_WORD);
            key = args.getByteArray(ARG_KEY);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        try {
            SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
            return CryptoUtils.decryptMsg(cryptText, originalKey);
        } catch (Exception e) {
            Log.e("MyLoader", "Error decrypting message", e);
            return null;
        }
    }
}