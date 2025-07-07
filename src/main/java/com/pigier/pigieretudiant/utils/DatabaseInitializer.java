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
                    nom VARCHAR(255),
                    prenom VARCHAR(255),
                    telephone VARCHAR(255),
                    email VARCHAR(255),
                    password VARCHAR(255)
                )
            """;
            
            // Table filieres
            String createFilieresTable = """
                CREATE TABLE IF NOT EXISTS filieres (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    libelle VARCHAR(255),
                    code VARCHAR(20) NOT NULL
                )
            """;
            
            // Table niveaux
            String createNiveauxTable = """
                CREATE TABLE IF NOT EXISTS niveaux (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    libelle VARCHAR(300),
                    code VARCHAR(10)
                )
            """;
            
            // Table filieresniveaux
            String createFilieresNiveauxTable = """
                CREATE TABLE IF NOT EXISTS filieresniveaux (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    niveaux_id INT NOT NULL,
                    filieres_id INT NOT NULL,
                    FOREIGN KEY (filieres_id) REFERENCES filieres(id),
                    FOREIGN KEY (niveaux_id) REFERENCES niveaux(id)
                )
            """;
            
            // Table etudiants
            String createEtudiantsTable = """
                CREATE TABLE IF NOT EXISTS etudiants (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(255),
                    prenoms VARCHAR(255),
                    matricule VARCHAR(10),
                    date_naissance DATE,
                    lieux_naissance VARCHAR(50),
                    genre VARCHAR(10),
                    nationnalite VARCHAR(255),
                    telephone VARCHAR(255),
                    email VARCHAR(255),
                    adresse TEXT,
                    annee_depart DATE
                )
            """;
            
            // Table inscriptions
            String createInscriptionsTable = """
                CREATE TABLE IF NOT EXISTS inscriptions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    etudiant_id INT NOT NULL,
                    filieresniveaux_id INT NOT NULL,
                    user_id INT NOT NULL,
                    annee_academique VARCHAR(255),
                    statut VARCHAR(255),
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id),
                    FOREIGN KEY (filieresniveaux_id) REFERENCES filieresniveaux(id),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            
            // Table typedocuments
            String createTypeDocumentsTable = """
                CREATE TABLE IF NOT EXISTS typedocuments (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    libelle VARCHAR(255)
                )
            """;
            
            // Table documents
            String createDocumentsTable = """
                CREATE TABLE IF NOT EXISTS documents (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    etudiant_id INT NOT NULL,
                    typedocument_id INT NOT NULL,
                    fichier VARCHAR(255),
                    date_ajout DATE,
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id),
                    FOREIGN KEY (typedocument_id) REFERENCES typedocuments(id)
                )
            """;
            
            // Table matieres
            String createMatieresTable = """
                CREATE TABLE IF NOT EXISTS matieres (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom_matiere VARCHAR(255),
                    coef INT,
                    niveau_id INT NOT NULL,
                    FOREIGN KEY (niveau_id) REFERENCES niveaux(id)
                )
            """;
            
            // Table notes
            String createNotesTable = """
                CREATE TABLE IF NOT EXISTS notes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    etudiant_id INT NOT NULL,
                    matiere_id INT NOT NULL,
                    note DECIMAL(5,2),
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id),
                    FOREIGN KEY (matiere_id) REFERENCES matieres(id)
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
            
            stmt = conn.prepareStatement(createTypeDocumentsTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createDocumentsTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createMatieresTable);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(createNotesTable);
            stmt.executeUpdate();
        }
    }

    private static void insertDefaultData() throws SQLException, ClassNotFoundException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Insertion des filières
            String insertFilieres = """
                INSERT IGNORE INTO filieres (id, libelle, code) VALUES
                (1, 'RÉSEAUX ET GÉNIE LOGICIEL', 'RGL'),
                (2, 'ASSISTANAT ET DIRECTION', 'AD'),
                (3, 'COMPTABILITÉ ET FINANCE', 'CF'),
                (4, 'TOURISME ET HÔTELLERIE', 'TH'),
                (5, 'COMMUNICATION ET DIRECTION DE MARQUE', 'CDM'),
                (6, 'MARKETING', 'MA')
            """;
            
            // Insertion des niveaux
            String insertNiveaux = """
                INSERT IGNORE INTO niveaux (id, libelle, code) VALUES
                (1, NULL, 'L1'),
                (2, NULL, 'L2'),
                (3, NULL, 'L3'),
                (4, NULL, 'M1'),
                (5, NULL, 'M2')
            """;
            
            // Insertion d'un utilisateur par défaut
            String insertUser = """
                INSERT IGNORE INTO users (id, nom, prenom, telephone, email, password) VALUES
                (1, 'Youssouf', 'DOUMDJE', '+225 0789681613', 'dydoumdje2004@gmail.com', 'You@2004')
            """;
            
            // Insertion des types de documents
            String insertTypeDocuments = """
                INSERT IGNORE INTO typedocuments (libelle) VALUES
                ('Acte de naissance'),
                ('Diplôme BAC'),
                ('Relevé de notes'),
                ('Photo d\'identité'),
                ('Certificat médical'),
                ('Autre')
            """;

            PreparedStatement stmt = conn.prepareStatement(insertFilieres);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(insertNiveaux);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(insertUser);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement(insertTypeDocuments);
            stmt.executeUpdate();
            
            // Création des relations filières-niveaux
            String insertFilieresNiveaux = """
                INSERT IGNORE INTO filieresniveaux (id, niveaux_id, filieres_id) VALUES
                (1, 1, 1), (2, 2, 1), (3, 3, 1), (4, 4, 1), (5, 5, 1),
                (6, 1, 2), (7, 2, 2), (8, 3, 2), (9, 4, 2), (10, 5, 2),
                (11, 1, 3), (12, 2, 3), (13, 3, 3), (14, 4, 3), (15, 5, 3),
                (16, 1, 4), (17, 2, 4), (18, 3, 4), (19, 4, 4), (20, 5, 4),
                (21, 1, 5), (22, 2, 5), (23, 3, 5), (24, 4, 5), (25, 5, 5),
                (26, 1, 6), (27, 2, 6), (28, 3, 6), (29, 4, 6), (30, 5, 6)
            """;
            
            stmt = conn.prepareStatement(insertFilieresNiveaux);
            stmt.executeUpdate();
        }
    }
}