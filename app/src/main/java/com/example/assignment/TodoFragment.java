package com.example.assignment;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignment.database.DBHelper;
import com.example.assignment.task.Task;
import com.example.assignment.task.TaskAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TodoFragment extends Fragment {
    Spinner prioritySpinner;
    String selectedPriority = "High";
    ListView listViewTask;
    TaskAdapter taskAdapter;
    DBHelper db;
    ImageButton btnAdd;
    EditText newTaskTitle;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo, container, false);

        db = new DBHelper(getContext());
        btnAdd = view.findViewById(R.id.btnAdd);
        newTaskTitle = view.findViewById(R.id.newTask);

        //load spinner for priority
        loadSpinner();

        //adapter section and get data from database
        TaskAdapter.taskData.clear();//clear data before loading
        loadData();

        //add button section
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newTaskTitle.getText().toString().isEmpty()) {
                    //empty field validation
                    newTaskTitle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                } else {
                    //validation passed
                    boolean result = db.insertNewTaskData(newTaskTitle.getText().toString(), selectedPriority);
                    if (result) {
                        newTaskTitle.setText("");
                        newTaskTitle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.mint)));

                        taskAdapter.clear();
                        loadData();
                    } else {
                        Toast.makeText(getContext(), "Failed To Add New Task!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    public void loadData() {
        listViewTask = view.findViewById(R.id.listViewTasks);

        //get data from database
        Cursor cursor = db.selectTodoData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String task = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String priority = cursor.getString(cursor.getColumnIndexOrThrow("priority"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));

                boolean s = status == 1;

                TaskAdapter.taskData.add(new Task(id, task, priority, s));
            } while (cursor.moveToNext());
            cursor.close();
        }

        //sort data by its priority
        List<Task> sortedData = TaskAdapter.taskData.stream().sorted(Comparator.comparingInt(task -> {
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
        taskAdapter = new TaskAdapter(getContext(), R.layout.task_list_item_view, sortedData);
        //set adapter
        listViewTask.setAdapter(taskAdapter);
    }

    public void loadSpinner() {
        prioritySpinner = view.findViewById(R.id.prioritySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

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
