package com.ni.taskmaster;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        dialogBuilder.setTitle("Agregar una nueva tarea");
        dialogBuilder.setMessage("Nombre de la tarea:");
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Agregar", (dialogInterface, i) -> {
            String taskName = input.getText().toString().trim();
            if (!taskName.isEmpty()) {
                showTimePickerDialog(taskName);
            } else {
                Toast.makeText(MenuActivity.this, "El nombre de la tarea no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

        dialogBuilder.create().show();
    }

    private void showTimePickerDialog(String taskName) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String taskTime = String.format("%02d:%02d", hourOfDay, minute);
            showDatePickerDialog(taskName, taskTime); // Mostrar el selector de fecha después del tiempo
        }, 12, 0, true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog(String taskName, String taskTime) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String taskDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            addTaskToRecyclerView(taskName, taskTime, taskDate);
        }, 2024, 0, 1);
        datePickerDialog.show();
    }

    private void addTaskToRecyclerView(String taskName, String taskTime, String taskDate) {
        Task newTask = new Task(taskName, "Pendiente", taskTime, taskDate);
        taskList.add(newTask);
        taskAdapter.notifyItemInserted(taskList.size() - 1);
    }
}