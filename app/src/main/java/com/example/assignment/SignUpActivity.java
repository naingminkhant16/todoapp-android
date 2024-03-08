package com.example.assignment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.database.DBHelper;

public class SignUpActivity extends AppCompatActivity {
    EditText username, password, confirm_password;
    Button btnSignUp, btnLogin;
    CheckBox showPassword;
    DBHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        showPassword = findViewById(R.id.showPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        db = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation here
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()) {
                    if (username.getText().toString().isEmpty()) {
                        username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    }

                    if (password.getText().toString().isEmpty()) {
                        password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    }

                    if (confirm_password.getText().toString().isEmpty()) {
                        confirm_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    }
                } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Password doesn't match!", Toast.LENGTH_LONG).show();
                } else {
                    //validation passed
                    boolean result = db.signUpUser(username.getText().toString(), password.getText().toString());

                    if (result) {
                        username.setText("");
                        password.setText("");
                        confirm_password.setText("");
                        Toast.makeText(SignUpActivity.this, "You can now login.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //password show hide toggle
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    // Show password
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    // Hide password
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }
}
