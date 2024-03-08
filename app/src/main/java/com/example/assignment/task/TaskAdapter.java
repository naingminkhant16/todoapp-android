package com.example.assignment.task;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.assignment.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    Context context;
    int layoutResourceId;
    public static List<Task> taskData = new ArrayList<>();

    public TaskAdapter(@NonNull Context context, int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        TaskAdapter.taskData = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        TaskHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TaskHolder();
            holder.checkBox = row.findViewById(R.id.checkbox);
            holder.priority = row.findViewById(R.id.priority);
//            holder.btnDelete = row.findViewById(R.id.btnDelete);
            holder.btnEdit = row.findViewById(R.id.btnEdit);

            row.setTag(holder);
        } else {
            holder = (TaskHolder) row.getTag();
        }

        Task task = taskData.get(position);
        holder.checkBox.setText(task.getTitle());
        holder.checkBox.setChecked(task.isStatus());
        setCheckBoxDesign(holder.checkBox);

        //set priority text and design
        holder.priority.setText(task.getPriority());
        setPriorityColor(holder.priority);

        return row;
    }

    private void setPriorityColor(TextView textView) {
        if (textView.getText().equals("High"))
            textView.setTextColor(ContextCompat.getColor(context, R.color.red));
        else if (textView.getText().equals("Medium"))
            textView.setTextColor(ContextCompat.getColor(context, R.color.mint));
        else if (textView.getText().equals("Low"))
            textView.setTextColor(ContextCompat.getColor(context, R.color.secondary));
    }

    private void setCheckBoxDesign(CheckBox cb) {
        if (cb.isChecked()) {
            cb.setAlpha(0.3f);
            cb.setTypeface(null, Typeface.ITALIC);
            cb.setTextColor(Color.GRAY);
        } else {
            cb.setAlpha(1.0f);
            cb.setTypeface(null, Typeface.BOLD);
            cb.setTextColor(ContextCompat.getColor(context, R.color.mint));
        }
    }

    public class TaskHolder {
        CheckBox checkBox;
        TextView priority;
        ImageButton btnEdit, btnDelete;
    }
}
