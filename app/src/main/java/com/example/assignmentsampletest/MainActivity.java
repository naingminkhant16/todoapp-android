package com.example.assignmentsampletest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.assignmentsampletest.auth.Auth;
import com.example.assignmentsampletest.task.Task;
import com.example.assignmentsampletest.task.TaskAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    Spinner prioritySpinner;
    String selectedPriority = "High";
    ListView listViewTask;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Signed in as " + Auth.username);

        //load spinner for priority
        loadSpinner();

        //adapter section
        loadAdapter();
    }

    public void loadAdapter() {
        listViewTask = findViewById(R.id.listViewTasks);
        //custom test data
        ArrayList<Task> testData = new ArrayList<>();
        testData.add(new Task("To Learn Spring", "Medium", false));
        testData.add(new Task("To Do Assignment", "High", false));
        testData.add(new Task("To Read Java Book", "Low", false));
        testData.add(new Task("Go To School", "High", true));
        //sort data by its priority
        List<Task> sortedData = testData.stream().sorted(Comparator.comparingInt(task -> {
            switch (task.getPriority().toLowerCase()) {
                case "high":
                    return 1;
                case "medium":
                    return 2;
                case "low":
                    return 3;
                default:
                    return 0;
            }
        })).collect(Collectors.toList());

        //create adapter
        taskAdapter = new TaskAdapter(this, R.layout.task_list_item_view, sortedData);
        //set adapter
        listViewTask.setAdapter(taskAdapter);
    }

    public void loadSpinner() {
        prioritySpinner = findViewById(R.id.prioritySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority = parent.getItemAtPosition(position).toString();

                Toast.makeText(MainActivity.this, "Selected priority : " + selectedPriority, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}