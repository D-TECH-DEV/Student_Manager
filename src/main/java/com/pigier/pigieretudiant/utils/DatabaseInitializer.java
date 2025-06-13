package com.pigier.pigieretudiant.utils;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try {
            createTables();
            insertDefaultData();
            System.out.println("Base de données initialisée avec succès!");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException, ClassNotFoundException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Table users
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    prenoms VARCHAR(100) NOT NULL,
                    email VARCHAR(150) UNIQUE NOT NULL,
                    mot_de_passe VARCHAR(255) NOT NULL,
                    type_user ENUM('admin', 'user') DEFAULT 'user',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Table filieres
            String createFilieresTable = """
                CREATE TABLE IF NOT EXISTS filieres (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    code VARCHAR(10) UNIQUE NOT NULL,
                    libelle VARCHAR(100) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Table niveaux
            String createNiveauxTable = """
                CREATE TABLE IF NOT EXISTS niveaux (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    code VARCHAR(10) UNIQUE NOT NULL,
                    libelle VARCHAR(50) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Table filieresniveaux (relation many-to-many)
            String createFilieresNiveauxTable = """
                CREATE TABLE IF NOT EXISTS filieresniveaux (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    filieres_id INT NOT NULL,
                    niveaux_id INT NOT NULL,
                    FOREIGN KEY (filieres_id) REFERENCES filieres(id) ON DELETE CASCADE,
                    FOREIGN KEY (niveaux_id) REFERENCES niveaux(id) ON DELETE CASCADE,
                    UNIQUE KEY unique_filiere_niveau (filieres_id, niveaux_id)
                )
            """;
            
            // Table etudiants
            String createEtudiantsTable = """
                CREATE TABLE IF NOT EXISTS etudiants (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    prenoms VARCHAR(100) NOT NULL,
                    matricule VARCHAR(50) UNIQUE NOT NULL,
                    date_naissance DATE,
                    lieux_naissance VARCHAR(100),
                    genre ENUM('M', 'F') NOT NULL,
                    telephone VARCHAR(20),
                    email VARCHAR(150),
                    nationnalite VARCHAR(50),
                    adresse TEXT,
                    filires_id INT,
                    annee_depart YEAR NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (filires_id) REFERENCES filieres(id)
                )
            """;
            
            // Table inscriptions
            String createInscriptionsTable = """
                CREATE TABLE IF NOT EXISTS inscriptions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    etudiant_id INT NOT NULL,
                    filieresniveaux_id INT NOT NULL,
                    annee_academique VARCHAR(20) NOT NULL,
                    statut ENUM('actif', 'suspendu', 'diplome') DEFAULT 'actif',
                    user_id INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
                    FOREIGN KEY (filieresniveaux_id) REFERENCES filieresniveaux(id),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            
            // Table documents
            String createDocumentsTable = """
                CREATE TABLE IF NOT EXISTS documents (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(255) NOT NULL,
                    type VARCHAR(100) NOT NULL,
                    etudiant_id INT NOT NULL,
                    chemin_fichier TEXT NOT NULL,
                    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE
                )
            """;

            // Exécution des requêtes
            PreparedStatement stmt = conn.prepareStatement(createUsersTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createFilieresTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createNiveauxTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createFilieresNiveauxTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createEtudiantsTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createInscriptionsTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createDocumentsTable);
            stmt.executeUpdate();
        }
    }

    private static void insertDefaultData() throws SQLException, ClassNotFoundException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Insertion des filières par défaut
            String insertFilieres = """
                INSERT IGNORE INTO filieres (code, libelle, description) VALUES
                ('INFO', 'Informatique', 'Formation en développement logiciel et systèmes informatiques'),
                ('COMPTA', 'Comptabilité', 'Formation en comptabilité et gestion financière'),
                ('MARKET', 'Marketing', 'Formation en marketing et communication'),
                ('RH', 'Ressources Humaines', 'Formation en gestion des ressources humaines'),
                ('LOGIST', 'Logistique', 'Formation en logistique et transport')
            """;
            
            // Insertion des niveaux par défaut
            String insertNiveaux = """
                INSERT IGNORE INTO niveaux (code, libelle) VALUES
                ('L1', 'Licence 1'),
                ('L2', 'Licence 2'),
                ('L3', 'Licence 3'),
                ('M1', 'Master 1'),
                ('M2', 'Master 2')
            """;
            
            // Insertion d'un utilisateur admin par défaut
            String insertAdmin = """
                INSERT IGNORE INTO users (nom, prenoms, email, mot_de_passe, type_user) VALUES
                ('Admin', 'Système', 'admin@pigier.com', 'admin123', 'admin')
            """;

            PreparedStatement stmt = conn.prepareStatement(insertFilieres);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(insertNiveaux);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(insertAdmin);
            stmt.executeUpdate();
            
            // Création des relations filières-niveaux
            String insertFilieresNiveaux = """
                INSERT IGNORE INTO filieresniveaux (filieres_id, niveaux_id)
                SELECT f.id, n.id FROM filieres f CROSS JOIN niveaux n
                WHERE n.code IN ('L1', 'L2', 'L3')
            """;
            
            stmt = conn.prepareStatement(insertFilieresNiveaux);
            stmt.executeUpdate();
        }
    }
}