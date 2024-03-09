package com.example.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.assignment.auth.Auth;

public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public DBHelper(Context context) {
        super(context, "TodoApp.db", null, 1);
        this.context = context;
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

    //crud task
    public boolean insertNewTaskData(String title, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("priority", priority);
        contentValues.put("task_owner_id", Auth.user_id);

        long result = db.insert("tasks", null, contentValues);
        return result != -1;
    }

    public Cursor selectTodoData() {
        String auth_id = Integer.toString(Auth.user_id);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM tasks WHERE task_owner_id = ? AND status = ?", new String[]{auth_id, "0"});
    }

    public Cursor selectDoneData() {
        String auth_id = Integer.toString(Auth.user_id);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM tasks WHERE task_owner_id = ? AND status = ?", new String[]{auth_id, "1"});
    }

    public boolean updateTaskData(int id, String title, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("priority", priority);

        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE id = ? ", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {
            long result = db.update("tasks", contentValues, "id=?", new String[]{String.valueOf(id)});

            cursor.close();
            return result != -1;
        } else {
            return false;
        }
    }

    public boolean updateTaskStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE id = ? ", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = db.update("tasks", contentValues, "id=?", new String[]{String.valueOf(id)});
            cursor.close();
            return result != -1;
        } else {
            return false;
        }
    }

    //login and sign up
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

            Log.i("auth_id", Integer.toString(Auth.user_id));
            Log.i("auth_username", Auth.username);
            return true;
        } else {
            //login fail
            return false;
        }
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

    public boolean deleteTaskData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE id = ? ", new String[]{Integer.toString(id)});
        if (cursor.getCount() > 0) {
            long result = db.delete("tasks", "id = ?", new String[]{Integer.toString(id)});
            cursor.close();

            return result != -1;
        }
        return false;
    }


}
