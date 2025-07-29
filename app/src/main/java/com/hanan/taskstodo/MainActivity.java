package com.hanan.taskstodo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hanan.taskstodo.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new TaskAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, adapter::setTasks);

        binding.addTaskBtn.setOnClickListener(v -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        EditText taskTitle = view.findViewById(R.id.taskTitle);
        Button selectTimeBtn = view.findViewById(R.id.selectTimeBtn);
        final long[] deadlineTime = {System.currentTimeMillis()};

        selectTimeBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view1, year, month, dayOfMonth) -> {
                        TimePickerDialog timePicker = new TimePickerDialog(this,
                                (timeView, hourOfDay, minute) -> {
                                    calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                                    deadlineTime[0] = calendar.getTimeInMillis();
                                },
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                        timePicker.show();
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Add New Task")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = taskTitle.getText().toString().trim();
                    if (!title.isEmpty()) {
                        Task task = new Task();
                        task.title = title;
                        task.deadline = deadlineTime[0];
                        taskViewModel.insert(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
