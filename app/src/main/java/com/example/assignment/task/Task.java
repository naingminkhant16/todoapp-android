package com.example.assignment.task;

public class Task {
    private int id, task_owner_id;
    private String title, priority;
    private boolean status;

    public Task(int id, String title, String priority, boolean status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getTask_owner_id() {
        return task_owner_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isStatus() {
        return status;
    }
}
