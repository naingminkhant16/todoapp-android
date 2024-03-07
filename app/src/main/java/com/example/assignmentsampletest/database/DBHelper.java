package com.example.assignmentsampletest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.assignmentsampletest.auth.Auth;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "TodoApp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY,username TEXT,password TEXT)");
        db.execSQL("CREATE TABLE tasks(id INTEGER PRIMARY KEY,title TEXT,priority TEXT,status INTEGER DEFAULT 0,task_owner_id INTEGER,FOREIGN KEY(task_owner_id) REFERENCES users(id)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists tasks");
        db.execSQL("DROP TABLE if exists users");
    }

    public boolean attemptLogin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor != null && cursor.moveToFirst()) {
            //login success
            do {
                //set auth data
                Auth.user_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                Auth.username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            } while (cursor.moveToNext());
            cursor.close();
            return true;
        } else {
            //login fail
            return false;
        }
    }

    public boolean insertTaskData(String title, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("priority", priority);
        contentValues.put("task_owner_id", Auth.user_id);

        long result = db.insert("tasks", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public boolean signUpUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = db.insert("users", null, contentValues);

        if (result == -1) return false;
        else return true;
    }

}
