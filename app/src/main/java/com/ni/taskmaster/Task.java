package com.ni.taskmaster;

import androidx.annotation.NonNull;

public class Task {
    private String name;
    private String status;
    private String time;
    private String date;

    public Task(String name, String status, String time, String date) {
        this.name = name;
        this.status = status;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        if (status.equals("Pendiente") || status.equals("Completado")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Estado inválido. Debe ser 'Pendiente' o 'Completado'.");
        }
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}