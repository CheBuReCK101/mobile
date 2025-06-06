package ru.mirea.dutovas.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class MyDateDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((MainActivity)requireActivity()).onDateSet(year, month, day);
    }
}