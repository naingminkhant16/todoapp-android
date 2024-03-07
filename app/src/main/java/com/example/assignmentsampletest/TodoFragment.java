package com.example.assignmentsampletest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignmentsampletest.auth.Auth;
import com.example.assignmentsampletest.task.Task;
import com.example.assignmentsampletest.task.TaskAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TodoFragment extends Fragment {
    Spinner prioritySpinner;
    String selectedPriority = "High";
    ListView listViewTask;
    TaskAdapter taskAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);


        //load spinner for priority
        loadSpinner(view);

        //adapter section
        loadAdapter(view);

        return view;
    }

    public void loadAdapter(View view) {
        listViewTask = view.findViewById(R.id.listViewTasks);
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
        taskAdapter = new TaskAdapter(getContext(), R.layout.task_list_item_view, sortedData);
        //set adapter
        listViewTask.setAdapter(taskAdapter);
    }

    public void loadSpinner(View view) {
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
