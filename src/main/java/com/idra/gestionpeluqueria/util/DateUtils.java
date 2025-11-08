package com.idra.gestionpeluqueria.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Clase de utilidades para el manejo y formateo de fechas y horas.
 * Proporciona metodos para parsear, formatear, validar y realizar 
 * operaciones con fechas y horas en el sistema.
 * 
 * @author Idra
 */
public class DateUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    /**
     * Convierte un String a LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM-DD");
        }
    }
    
    /**
     * Convierte un String a LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha/hora inválido. Use YYYY-MM-DD HH:MM");
        }
    }
    
    /**
     * Formatea una fecha para mostrar al usuario
     */
    public static String formatDisplayDate(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Formatea una fecha para la base de datos
     */
    public static String formatDatabaseDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Verifica si una fecha es válida
     */
    public static boolean isValidDate(String dateString) {
        try {
            parseDate(dateString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Verifica si una fecha/hora es válida
     */
    public static boolean isValidDateTime(String dateTimeString) {
        try {
            parseDateTime(dateTimeString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Obtiene la fecha actual formateada
     */
    public static String getCurrentDateFormatted() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
    /**
     * Obtiene la fecha/hora actual formateada
     */
    public static String getCurrentDateTimeFormatted() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
    
    /**
     * Verifica si una fecha es futura
     */
    public static boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Verifica si una fecha/hora es futura
     */
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Calcula la diferencia en minutos entre dos fechas/horas
     */
    public static long getMinutesDifference(LocalDateTime start, LocalDateTime end) {
        return java.time.Duration.between(start, end).toMinutes();
    }
}