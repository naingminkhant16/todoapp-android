package com.example.assignment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.database.DBHelper;


public class EditTaskActivity extends AppCompatActivity {
    Spinner prioritySpinner;
    String selectedPriority;
    DBHelper db;
    EditText newTitle;
    int taskId;
    Button btnUpdate, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        newTitle = findViewById(R.id.newTitle);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        //get extra
        Intent intent = getIntent();
        taskId = intent.getIntExtra("id", 0);
        newTitle.setText(intent.getStringExtra("title"));
        selectedPriority = intent.getStringExtra("priority");

        db = new DBHelper(this);

        loadSpinner();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation
                if (newTitle.getText().toString().isEmpty()) {
                    newTitle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                } else {
                    boolean result = db.updateTaskData(taskId, newTitle.getText().toString(), selectedPriority);
                    if (result) {
                        Toast.makeText(EditTaskActivity.this, "Updated Data.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditTaskActivity.this, "Failed To Update Data!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void loadSpinner() {
        prioritySpinner = findViewById(R.id.prioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        // set default selection by old value
        prioritySpinner.setSelection(adapter.getPosition(selectedPriority));

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
