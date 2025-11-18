package com.idra.gestionpeluqueria.util;

import java.util.regex.Pattern;

/**
 * Clase de utilidades para la validacion de datos.
 * Proporciona metodos para validar formatos de datos comunes como Emails,
 * telefonos, nombres, precios y otros campos segun las reglas de negocio.
 * 
 * @author Idra
 */
public class Validator {
    
    // Patrones para validaciones
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[0-9]{10,15}$");
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$");
    private static final Pattern PRICE_PATTERN = 
        Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
    private static final Pattern TIME_PATTERN = 
        Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    
    /**
     * Valida un email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Valida un número de teléfono
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remover espacios, guiones, paréntesis
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Valida un nombre (solo letras y espacios)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }
    
    /**
     * Valida un precio (número positivo con hasta 2 decimales)
     */
    public static boolean isValidPrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return false;
        }
        return PRICE_PATTERN.matcher(price.trim()).matches();
    }
    
    /**
     * Valida un formato de hora (HH:MM)
     */
    public static boolean isValidTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            return false;
        }
        return TIME_PATTERN.matcher(time.trim()).matches();
    }
    
    /**
     * Valida que un texto no esté vacío
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
    
    /**
     * Valida que un número sea positivo
     */
    public static boolean isPositiveNumber(Number number) {
        return number != null && number.doubleValue() > 0;
    }
    
    /**
     * Valida que un número esté en un rango específico
     */
    public static boolean isInRange(Number number, Number min, Number max) {
        if (number == null) return false;
        double value = number.doubleValue();
        return value >= min.doubleValue() && value <= max.doubleValue();
    }
    
    /**
     * Valida la longitud de un texto
     */
    public static boolean isValidLength(String text, int minLength, int maxLength) {
        if (text == null) return false;
        return text.length() >= minLength && text.length() <= maxLength;
    }
    
    /**
     * Sanitiza un texto removiendo espacios extras y caracteres peligrosos
     */
    public static String sanitizeText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("[<>\"']", "");
    }
    
    /**
     * Valida un DNI 
     */
    public static boolean isValidDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }
        // DNI argentino: 7-8 dígitos
        return dni.matches("^[0-9]{7,8}$");
    }
    
    /**
     * Valida que una fecha no sea en el pasado 
     */
    public static boolean isNotPastDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return !dateTime.isBefore(java.time.LocalDateTime.now());
    }
    
    /**
     * Valida la duración de un servicio 
     */
    public static boolean isValidServiceDuration(int minutes) {
        return minutes >= 15 && minutes <= 480;
    }
}