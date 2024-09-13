package com.ni.taskmaster;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import android.app.DatePickerDialog;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    // Constructor del adaptador que recibe una lista de tareas
    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout para cada item de la lista
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Obtiene la tarea correspondiente y actualiza la vista
        Task task = taskList.get(position);
        holder.taskName.setText(task.getName());
        holder.taskStatus.setText(task.getStatus());
        holder.taskTime.setText(task.getTime());
        holder.taskDate.setText(task.getDate());

        // Configura el botón para editar la tarea
        holder.btnEditTask.setOnClickListener(v -> {
            showEditTaskDialog(holder.itemView.getContext(), task, position);
        });

        // Configura el botón para eliminar la tarea
        holder.btnDeleteTask.setOnClickListener(v -> {
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
        });
    }

    @Override
    public int getItemCount() {
        // Retorna el número de tareas en la lista
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskStatus, taskTime, taskDate;
        Button btnEditTask, btnDeleteTask;

        // Inicializa los componentes de la vista
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.tvTaskName);
            taskStatus = itemView.findViewById(R.id.tvTaskStatus);
            taskTime = itemView.findViewById(R.id.tvTaskTime);
            taskDate = itemView.findViewById(R.id.tvTaskDate);
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
        }
    }

    private void showEditTaskDialog(Context context, Task task, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // Crea un layout vertical para el diálogo
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Campo para ingresar el nombre de la tarea
        final EditText inputName = new EditText(context);
        inputName.setText(task.getName());
        layout.addView(inputName);

        // Spinner para seleccionar el estado de la tarea
        final Spinner statusSpinner = new Spinner(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, new String[]{"Pendiente", "Completado"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Configura la posición del spinner según el estado actual
        int spinnerPosition = adapter.getPosition(task.getStatus());
        statusSpinner.setSelection(spinnerPosition);
        layout.addView(statusSpinner);

        // Campo para seleccionar la fecha
        final EditText dateInput = new EditText(context);
        dateInput.setText(task.getDate());
        dateInput.setHint("Seleccionar Fecha");
        layout.addView(dateInput);

        // Configura el DatePickerDialog para seleccionar la fecha
        final String[] updatedDate = {task.getDate()};
        dateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
                updatedDate[0] = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                dateInput.setText(updatedDate[0]);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Botón para seleccionar la hora
        final Button timeButton = new Button(context);
        timeButton.setText(task.getTime() != null && !task.getTime().isEmpty()
                ? task.getTime()
                : "Seleccionar Hora");
        layout.addView(timeButton);

        // Configura el TimePickerDialog para seleccionar la hora
        final String[] updatedTime = {task.getTime()};
        timeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
                updatedTime[0] = String.format("%02d:%02d", hourOfDay, minute);
                timeButton.setText(updatedTime[0]);
            }, 12, 0, true);
            timePickerDialog.show();
        });

        // Configura el diálogo con el layout y los botones
        dialogBuilder.setTitle("Editar tarea");
        dialogBuilder.setMessage("Modifica el nombre, el estado, la fecha y la hora de la tarea:");
        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String updatedTaskName = inputName.getText().toString();
            String updatedTaskStatus = statusSpinner.getSelectedItem().toString();
            String updatedTaskDate = dateInput.getText().toString();

            // Actualiza la tarea si todos los campos están completos
            if (!updatedTaskName.isEmpty() && updatedTime[0] != null && !updatedTaskDate.isEmpty()) {
                task.setName(updatedTaskName);
                task.setStatus(updatedTaskStatus);
                task.setTime(updatedTime[0]);
                task.setDate(updatedTaskDate);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Por favor, completa el nombre de la tarea, selecciona una fecha y una hora.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());

        // Muestra el diálogo
        dialogBuilder.create().show();
    }
}