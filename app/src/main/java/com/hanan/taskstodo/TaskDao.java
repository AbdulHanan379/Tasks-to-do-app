package com.hanan.taskstodo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    LiveData<List<Task>> getAllTasks();

    @Delete
    void delete(Task task);
}

