package ru.mirea.dutovas.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFrament extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Диалоговое окно")
                .setMessage("Привет, МИРЭА!")
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton("Иду дальше", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Действие при нажатии кнопки
                        ((MainActivity)requireActivity()).onOkClicked();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Действие при нажатии кнопки
                        ((MainActivity)requireActivity()).onCancelClicked();
                        dialog.cancel();
                    }
                })
                .setNeutralButton("На паузе", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Действие при нажатии кнопки
                        ((MainActivity)requireActivity()).onNeutralClicked();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}