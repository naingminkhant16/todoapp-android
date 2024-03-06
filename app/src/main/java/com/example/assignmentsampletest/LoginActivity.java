package com.example.assignmentsampletest;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignmentsampletest.database.DBHelper;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp;
    EditText username, password;
    DBHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        db = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    if (username.getText().toString().isEmpty()) {
                        username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    }

                    if (password.getText().toString().isEmpty()) {
                        password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    }
                } else {
                    //validation pass
                    boolean result = db.attemptLogin(username.getText().toString(), password.getText().toString());
                    if (result) {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();

                        //Go To MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Fail. Incorrect password or username.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
