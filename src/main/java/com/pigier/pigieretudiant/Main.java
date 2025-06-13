package com.pigier.pigieretudiant;

import com.pigier.pigieretudiant.utils.DatabaseInitializer;
import com.pigier.pigieretudiant.utils.SplashScreen;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Initialiser la base de données au démarrage
        try {
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
        }
        
        // Lancer l'application JavaFX
        Application.launch(SplashScreen.class, args);
    }
}