package com.example.gauchogymtrackerlite; // <-- REEMPLAZA con tu paquete

// Clase simple para contener los datos de una entrada del historial
public class HistoryEntry {
    public String date;
    public String summary;

    // Constructor
    public HistoryEntry(String date, String summary) {
        this.date = date;
        this.summary = summary;
    }
}