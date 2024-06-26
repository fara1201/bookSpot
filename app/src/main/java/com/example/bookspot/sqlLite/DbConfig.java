package com.example.bookspot.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbConfig extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database-library";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "books_final";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "nim";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IS_LOGGED_IN = "isLoggedIn";
    public static final String FAVORITES_TABLE_NAME = "favorite";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_BOOK_ID = "book_id";

    public DbConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + FAVORITES_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_BOOK_ID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertUserData(String nim, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, nim);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IS_LOGGED_IN, 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public void updateRecord(int id, int isLoggedIn) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IS_LOGGED_IN, isLoggedIn);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteRecord(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.delete(FAVORITES_TABLE_NAME, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void insertFavorite(int userId, String bookId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_BOOK_ID, bookId);
        db.insert(FAVORITES_TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getFavoriteBooksByUserId(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(FAVORITES_TABLE_NAME, null, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public void deleteFavorite(int userId, String bookId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + FAVORITES_TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = " + userId + " AND " + COLUMN_BOOK_ID + " = '" + bookId + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
    }
}

