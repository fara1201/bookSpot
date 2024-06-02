package com.example.bookspot;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookspot.sqlLite.DbConfig;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView buttonRegister;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbConfig = new DbConfig(this);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register);

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim(); // Pastikan mengambil dari editTextUsername
            String password = editTextPassword.getText().toString().trim(); // Pastikan mengambil dari editTextPassword

            if (username.isEmpty()) {
                editTextUsername.setError("Please enter your Username");
            } else if (password.isEmpty()) {
                editTextPassword.setError("Please enter your password");
            } else {
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        String selection = DbConfig.COLUMN_USERNAME + "=? AND " + DbConfig.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(
                DbConfig.TABLE_NAME,
                new String[]{DbConfig.COLUMN_ID},
                selection,
                selectionArgs,
                null, null, null);

        // Debugging: Tampilkan informasi query dan parameter
        Log.d("LoginActivity", "Query: " + selection + ", Args: " + Arrays.toString(selectionArgs));

        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(DbConfig.COLUMN_ID);
            if (idColumnIndex != -1) {
                int userId = cursor.getInt(idColumnIndex);
                loginSuccess(userId);
                updateLoginStatus(username, true);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        } else {
            Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }

    private void updateLoginStatus(String username, boolean isLoggedIn) {
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
        db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_USERNAME + " = ?", new String[]{username});
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = db.query(
                DbConfig.TABLE_NAME,
                new String[]{DbConfig.COLUMN_ID},
                DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                new String[]{"1"},
                null, null, null);

        if (cursor.getCount() > 0) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        cursor.close();
        db.close();
    }

    // Metode untuk menyimpan ID pengguna yang berhasil login ke SharedPreferences
    private void loginSuccess(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }
}
