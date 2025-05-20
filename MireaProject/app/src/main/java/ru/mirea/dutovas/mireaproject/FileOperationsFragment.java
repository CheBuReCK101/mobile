package ru.mirea.dutovas.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperationsFragment extends Fragment {
    private EditText fileContentEditText;
    private Button encryptButton, decryptButton, saveToFileButton, loadFromFileButton;
    private final String FILENAME = "secret_file.txt";
    private boolean isEncrypted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_operations, container, false);

        fileContentEditText = view.findViewById(R.id.fileContentEditText);
        encryptButton = view.findViewById(R.id.encryptButton);
        decryptButton = view.findViewById(R.id.decryptButton);
        saveToFileButton = view.findViewById(R.id.saveToFileButton);
        loadFromFileButton = view.findViewById(R.id.loadFromFileButton);

        encryptButton.setOnClickListener(v -> encryptText());
        decryptButton.setOnClickListener(v -> decryptText());
        saveToFileButton.setOnClickListener(v -> saveToFile());
        loadFromFileButton.setOnClickListener(v -> loadFromFile());

        return view;
    }

    private void encryptText() {
        String text = fileContentEditText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(getContext(), "Введите текст для шифрования", Toast.LENGTH_SHORT).show();
            return;
        }

        // Простейший "шифр" - инвертирование строки
        String encrypted = new StringBuilder(text).reverse().toString();
        fileContentEditText.setText(encrypted);
        isEncrypted = true;
        Toast.makeText(getContext(), "Текст зашифрован", Toast.LENGTH_SHORT).show();
    }

    private void decryptText() {
        String text = fileContentEditText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(getContext(), "Нет текста для расшифровки", Toast.LENGTH_SHORT).show();
            return;
        }

        // "Расшифровка" - снова инвертирование строки
        String decrypted = new StringBuilder(text).reverse().toString();
        fileContentEditText.setText(decrypted);
        isEncrypted = false;
        Toast.makeText(getContext(), "Текст расшифрован", Toast.LENGTH_SHORT).show();
    }

    private void saveToFile() {
        String text = fileContentEditText.getText().toString();
        if (text.isEmpty()) {
            Toast.makeText(getContext(), "Нет текста для сохранения", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileOutputStream fos = requireActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
            Toast.makeText(getContext(), "Все записи сохранены в файл", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromFile() {
        try (FileInputStream fis = requireActivity().openFileInput(FILENAME)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String text = new String(bytes);
            fileContentEditText.setText(text);
            Toast.makeText(getContext(), "Записи загружены из файла", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка при загрузке файла", Toast.LENGTH_SHORT).show();
        }
    }

    // Простейшая проверка на зашифрованность текста
    private boolean isEncrypted(String text) {
        if (text.length() < 2) return false;
        // Проверяем, можно ли "расшифровать" текст
        String decrypted = new StringBuilder(text).reverse().toString();
        return !decrypted.equals(text); // Если после "расшифровки" текст изменился
    }

    public void showNewNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_note, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.noteTitleEditText);
        EditText contentEditText = dialogView.findViewById(R.id.noteContentEditText);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (!title.isEmpty() && !content.isEmpty()) {
                // Сохраняем новую запись
                saveNewNote(title, content);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void saveNewNote(String title, String content) {
        // Здесь можно сохранить запись в файл или базу данных
        // Пока просто добавим в EditText
        String note = "=== " + title + " ===\n" + content + "\n\n";
        fileContentEditText.append(note);
        Toast.makeText(getContext(), "Запись добавлена", Toast.LENGTH_SHORT).show();
    }
}