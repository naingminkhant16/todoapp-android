package com.example.assignment.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.assignment.EditTaskActivity;
import com.example.assignment.R;
import com.example.assignment.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    Context context;
    int layoutResourceId;
    DBHelper db;
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

        //delete
        holder.checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showConfirmDialog(task);
                return true;
            }
        });

        //edit
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("id", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("priority", task.getPriority());

                context.startActivity(intent);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                db = new DBHelper(getContext());
                boolean result = db.updateTaskStatus(task.getId(), cb.isChecked() ? 1 : 0);

                if (result) {
                    //update design
                    setCheckBoxDesign(cb);
                    //delay some second
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < TaskAdapter.taskData.size(); i++) {
                                if (TaskAdapter.taskData.get(i).getId() == task.getId()) {
                                    TaskAdapter.taskData.remove(i);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }, 3000);
                }
            }
        });
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

    private void showConfirmDialog(Task currentTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Confirmation!");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(currentTask);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteData(Task currentTask) {
        db = new DBHelper(getContext());
        boolean result = db.deleteTaskData(currentTask.getId());

        if (result) {
            for (int i = 0; i < TaskAdapter.taskData.size(); i++) {
                if (TaskAdapter.taskData.get(i).getId() == currentTask.getId()) {
                    TaskAdapter.taskData.remove(i);
                    notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(context, "Failed To Delete!", Toast.LENGTH_LONG).show();
        }
    }

    public class TaskHolder {
        CheckBox checkBox;
        TextView priority;
        ImageButton btnEdit;
    }
}
