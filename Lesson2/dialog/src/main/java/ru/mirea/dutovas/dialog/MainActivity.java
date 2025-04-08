package ru.mirea.dutovas.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Обработчики для кнопок
    public void onClickShowDialog(View view) {
        DialogFragment dialogFragment = new AlertDialogFrament();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onClickShowTimeDialog(View view) {
        DialogFragment timeDialog = new MyTimeDialogFragment();
        timeDialog.show(getSupportFragmentManager(), "timePicker");
    }

    public void onClickShowDateDialog(View view) {
        DialogFragment dateDialog = new MyDateDialogFragment();
        dateDialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void onClickShowProgressDialog(View view) {
        DialogFragment progressDialog = new MyProgressDialogFragment();
        progressDialog.show(getSupportFragmentManager(), "progressDialog");

        // Покажем Snackbar после закрытия ProgressDialog
        Snackbar.make(view, "ProgressDialog показан", Snackbar.LENGTH_LONG)
                .setAction("OK", v -> Toast.makeText(this, "Snackbar action", Toast.LENGTH_SHORT).show())
                .show();
    }

    // Методы обратного вызова
    public void onTimeSet(int hour, int minute) {
        Toast.makeText(this, String.format("Выбрано время: %02d:%02d", hour, minute),
                Toast.LENGTH_SHORT).show();
    }

    public void onDateSet(int year, int month, int day) {
        Toast.makeText(this, String.format("Выбрана дата: %02d.%02d.%d", day, month+1, year),
                Toast.LENGTH_SHORT).show();
    }

    public void onOkClicked() {
        showSnackbar("Вы выбрали кнопку \"Иду дальше\"!");
    }

    public void onCancelClicked() {
        showSnackbar("Вы выбрали кнопку \"Нет\"!");
    }

    public void onNeutralClicked() {
        showSnackbar("Вы выбрали кнопку \"На паузе\"!");
    }

    private void showSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}