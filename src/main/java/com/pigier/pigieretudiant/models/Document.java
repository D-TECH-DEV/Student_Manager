package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private int id;
    private int etudiantId;
    private int typeDocumentId;
    private String etudiantNom;
    private String typeDocumentLibelle;
    private String fichier;
    private LocalDate dateAjout;

    public Document(int etudiantId, int typeDocumentId, String fichier) {
        this.etudiantId = etudiantId;
        this.typeDocumentId = typeDocumentId;
        this.fichier = fichier;
        this.dateAjout = LocalDate.now();
    }

    public Document(int id, int etudiantId, int typeDocumentId, String etudiantNom, 
                   String typeDocumentLibelle, String fichier, LocalDate dateAjout) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.typeDocumentId = typeDocumentId;
        this.etudiantNom = etudiantNom;
        this.typeDocumentLibelle = typeDocumentLibelle;
        this.fichier = fichier;
        this.dateAjout = dateAjout;
    }

    public void create() throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO documents (etudiant_id, typedocument_id, fichier, date_ajout) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, etudiantId);
            stmt.setInt(2, typeDocumentId);
            stmt.setString(3, fichier);
            stmt.setDate(4, Date.valueOf(dateAjout));
            
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
        String query = """
            SELECT d.*, CONCAT(e.nom, ' ', e.prenoms) as etudiant_nom, td.libelle as type_libelle
            FROM documents d 
            JOIN etudiants e ON d.etudiant_id = e.id 
            JOIN typedocuments td ON d.typedocument_id = td.id
            ORDER BY d.date_ajout DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Document document = new Document(
                    rs.getInt("id"),
                    rs.getInt("etudiant_id"),
                    rs.getInt("typedocument_id"),
                    rs.getString("etudiant_nom"),
                    rs.getString("type_libelle"),
                    rs.getString("fichier"),
                    rs.getDate("date_ajout").toLocalDate()
                );
                documents.add(document);
            }
        }
        
        return documents;
    }

    public static List<Document> getByEtudiant(int etudiantId) throws SQLException, ClassNotFoundException {
        List<Document> documents = new ArrayList<>();
        String query = """
            SELECT d.*, td.libelle as type_libelle
            FROM documents d 
            JOIN typedocuments td ON d.typedocument_id = td.id
            WHERE d.etudiant_id = ? 
            ORDER BY d.date_ajout DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, etudiantId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Document document = new Document(
                    rs.getInt("id"),
                    rs.getInt("etudiant_id"),
                    rs.getInt("typedocument_id"),
                    "",
                    rs.getString("type_libelle"),
                    rs.getString("fichier"),
                    rs.getDate("date_ajout").toLocalDate()
                );
                documents.add(document);
            }
        }
        
        return documents;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEtudiantId() { return etudiantId; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }

    public int getTypeDocumentId() { return typeDocumentId; }
    public void setTypeDocumentId(int typeDocumentId) { this.typeDocumentId = typeDocumentId; }

    public String getEtudiantNom() { return etudiantNom; }
    public void setEtudiantNom(String etudiantNom) { this.etudiantNom = etudiantNom; }

    public String getTypeDocumentLibelle() { return typeDocumentLibelle; }
    public void setTypeDocumentLibelle(String typeDocumentLibelle) { this.typeDocumentLibelle = typeDocumentLibelle; }

    public String getFichier() { return fichier; }
    public void setFichier(String fichier) { this.fichier = fichier; }

    public LocalDate getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDate dateAjout) { this.dateAjout = dateAjout; }

    @Override
    public String toString() {
        return typeDocumentLibelle + " - " + etudiantNom;
    }
}