package com.example.assignment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignment.database.DBHelper;
import com.example.assignment.task.Task;
import com.example.assignment.task.TaskAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DoneFragment extends Fragment {
    ListView listViewTask;
    TaskAdapter taskAdapter;
    View view;
    DBHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_done, container, false);

        db = new DBHelper(getContext());

        //adapter section and get data from database
        TaskAdapter.taskData.clear();//clear data before loading
        loadData();

        return view;
    }

    public void loadData() {
        listViewTask = view.findViewById(R.id.listViewTasks);

        //get data from database
        Cursor cursor = db.selectDoneData();
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
}
