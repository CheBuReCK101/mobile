package ru.mirea.dutovas.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private EditText nameEditText, ageEditText, favoriteMovieEditText;
    private Button saveButton;
    private TextView profileInfoTextView;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        favoriteMovieEditText = view.findViewById(R.id.favoriteMovieEditText);
        saveButton = view.findViewById(R.id.saveButton);
        profileInfoTextView = view.findViewById(R.id.profileInfoTextView);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        loadProfile();

        saveButton.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("PROFILE_NAME", nameEditText.getText().toString());
        editor.putInt("PROFILE_AGE", Integer.parseInt(ageEditText.getText().toString()));
        editor.putString("PROFILE_MOVIE", favoriteMovieEditText.getText().toString());
        editor.apply();

        updateProfileInfo();
    }

    private void loadProfile() {
        String name = sharedPref.getString("PROFILE_NAME", "");
        int age = sharedPref.getInt("PROFILE_AGE", 0);
        String movie = sharedPref.getString("PROFILE_MOVIE", "");

        nameEditText.setText(name);
        ageEditText.setText(age > 0 ? String.valueOf(age) : "");
        favoriteMovieEditText.setText(movie);

        updateProfileInfo();
    }

    private void updateProfileInfo() {
        String name = sharedPref.getString("PROFILE_NAME", "Не указано");
        int age = sharedPref.getInt("PROFILE_AGE", 0);
        String movie = sharedPref.getString("PROFILE_MOVIE", "Не указан");

        String info = String.format("Имя: %s\nВозраст: %d\nЛюбимый фильм: %s", name, age, movie);
        profileInfoTextView.setText(info);
    }
}