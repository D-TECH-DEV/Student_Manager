package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private int id;
    private String nom;
    private String type;
    private int etudiantId;
    private String etudiantNom;
    private String cheminFichier;
    private LocalDateTime dateAjout;

    public Document(String nom, String type, int etudiantId, String cheminFichier) {
        this.nom = nom;
        this.type = type;
        this.etudiantId = etudiantId;
        this.cheminFichier = cheminFichier;
        this.dateAjout = LocalDateTime.now();
    }

    public Document(int id, String nom, String type, int etudiantId, String etudiantNom, 
                   String cheminFichier, LocalDateTime dateAjout) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.etudiantId = etudiantId;
        this.etudiantNom = etudiantNom;
        this.cheminFichier = cheminFichier;
        this.dateAjout = dateAjout;
    }

    public void create() throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO documents (nom, type, etudiant_id, chemin_fichier, date_ajout) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nom);
            stmt.setString(2, type);
            stmt.setInt(3, etudiantId);
            stmt.setString(4, cheminFichier);
            stmt.setTimestamp(5, Timestamp.valueOf(dateAjout));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }
        }
    }

    public void delete() throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM documents WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static List<Document> getAll() throws SQLException, ClassNotFoundException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT d.*, CONCAT(e.nom, ' ', e.prenoms) as etudiant_nom " +
                      "FROM documents d " +
                      "JOIN etudiants e ON d.etudiant_id = e.id " +
                      "ORDER BY d.date_ajout DESC";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Document document = new Document(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("type"),
                    rs.getInt("etudiant_id"),
                    rs.getString("etudiant_nom"),
                    rs.getString("chemin_fichier"),
                    rs.getTimestamp("date_ajout").toLocalDateTime()
                );
                documents.add(document);
            }
        }
        
        return documents;
    }

    public static List<Document> getByEtudiant(int etudiantId) throws SQLException, ClassNotFoundException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM documents WHERE etudiant_id = ? ORDER BY date_ajout DESC";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, etudiantId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Document document = new Document(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("type"),
                    rs.getInt("etudiant_id"),
                    "",
                    rs.getString("chemin_fichier"),
                    rs.getTimestamp("date_ajout").toLocalDateTime()
                );
                documents.add(document);
            }
        }
        
        return documents;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getEtudiantId() { return etudiantId; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }

    public String getEtudiantNom() { return etudiantNom; }
    public void setEtudiantNom(String etudiantNom) { this.etudiantNom = etudiantNom; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    @Override
    public String toString() {
        return nom + " (" + type + ")";
    }
}