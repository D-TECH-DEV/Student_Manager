package com.pigier.pigieretudiant.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[0-9]{8,15}$");
    
    private static final Pattern MATRICULE_PATTERN = 
        Pattern.compile("^[A-Z0-9]{6,12}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidMatricule(String matricule) {
        return matricule != null && MATRICULE_PATTERN.matcher(matricule).matches();
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidName(String name) {
        return isNotEmpty(name) && name.trim().length() >= 2;
    }

    public static String formatMatricule(String input) {
        if (input == null) return null;
        return input.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    public static String formatPhone(String phone) {
        if (phone == null) return null;
        return phone.replaceAll("[^0-9+]", "");
    }

    public static class ValidationResult {
        private boolean valid;
        private String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }

    public static ValidationResult validateEtudiant(String nom, String prenom, String matricule, String email, String phone) {
        if (!isValidName(nom)) {
            return new ValidationResult(false, "Le nom doit contenir au moins 2 caractères");
        }
        
        if (!isValidName(prenom)) {
            return new ValidationResult(false, "Le prénom doit contenir au moins 2 caractères");
        }
        
        if (!isValidMatricule(matricule)) {
            return new ValidationResult(false, "Le matricule doit contenir 6 à 12 caractères alphanumériques");
        }
        
        if (email != null && !email.isEmpty() && !isValidEmail(email)) {
            return new ValidationResult(false, "Format d'email invalide");
        }
        
        if (phone != null && !phone.isEmpty() && !isValidPhone(phone)) {
            return new ValidationResult(false, "Format de téléphone invalide");
        }
        
        return new ValidationResult(true, "Validation réussie");
    }
}