package com.hanan.taskstodo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.insert(task));
    }

    public void delete(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.delete(task));
    }
}

