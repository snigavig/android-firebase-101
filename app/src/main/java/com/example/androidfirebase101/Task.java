package com.example.androidfirebase101;

/**
 * Created by snigavig on 24.11.16.
 */

public class Task {
    public static final String COLLECTION_NAME = "tasks";
    private String id;
    private String name;
    private String description;
    private Boolean done;

    public Task() {
    }

    public Task(String id, String name, String description, Boolean done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isDone() {
        return done;
    }

    public String getId() {
        return id;
    }
}