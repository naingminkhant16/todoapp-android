package com.example.assignmentsampletest.task;

public class Task {
    private int id, task_owner_id;
    private String title, priority;
    private boolean status;

    public Task(String title, String priority, boolean status) {
        this.title = title;
        this.priority = priority;
        this.status = status;
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
