package com.example.taskManager.entity;

public enum TaskStatus {
    TODO, IN_PROGRESS, DONE;

    public static boolean contains(String test) {
        try {
            TaskStatus.valueOf(test);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
