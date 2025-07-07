package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Note {
    private int id;
    private int etudiantId;
    private int matiereId;
    private double note;
    private String etudiantNom;
    private String matiereNom;
    private int matiereCoef;

    public Note(int etudiantId, int matiereId, double note) {
        this.etudiantId = etudiantId;
        this.matiereId = matiereId;
        this.note = note;
    }

    public Note(int id, int etudiantId, int matiereId, double note, String etudiantNom, String matiereNom, int matiereCoef) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.matiereId = matiereId;
        this.note = note;
        this.etudiantNom = etudiantNom;
        this.matiereNom = matiereNom;
        this.matiereCoef = matiereCoef;
    }

    public void create() throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO notes (etudiant_id, matiere_id, note) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, etudiantId);
            stmt.setInt(2, matiereId);
            stmt.setDouble(3, note);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }
        }
    }

    public void update() throws SQLException, ClassNotFoundException {
        String query = "UPDATE notes SET note = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, note);
            stmt.setInt(2, id);
            
            stmt.executeUpdate();
        }
    }

    public void delete() throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static List<Note> getByEtudiant(int etudiantId) throws SQLException, ClassNotFoundException {
        List<Note> notes = new ArrayList<>();
        String query = """
            SELECT n.*, CONCAT(e.nom, ' ', e.prenoms) as etudiant_nom, m.nom_matiere, m.coef
            FROM notes n
            JOIN etudiants e ON n.etudiant_id = e.id
            JOIN matieres m ON n.matiere_id = m.id
            WHERE n.etudiant_id = ?
            ORDER BY m.nom_matiere
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, etudiantId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Note note = new Note(
                    rs.getInt("id"),
                    rs.getInt("etudiant_id"),
                    rs.getInt("matiere_id"),
                    rs.getDouble("note"),
                    rs.getString("etudiant_nom"),
                    rs.getString("nom_matiere"),
                    rs.getInt("coef")
                );
                notes.add(note);
            }
        }
        
        return notes;
    }

    public static List<Note> getAll() throws SQLException, ClassNotFoundException {
        List<Note> notes = new ArrayList<>();
        String query = """
            SELECT n.*, CONCAT(e.nom, ' ', e.prenoms) as etudiant_nom, m.nom_matiere, m.coef
            FROM notes n
            JOIN etudiants e ON n.etudiant_id = e.id
            JOIN matieres m ON n.matiere_id = m.id
            ORDER BY e.nom, e.prenoms, m.nom_matiere
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Note note = new Note(
                    rs.getInt("id"),
                    rs.getInt("etudiant_id"),
                    rs.getInt("matiere_id"),
                    rs.getDouble("note"),
                    rs.getString("etudiant_nom"),
                    rs.getString("nom_matiere"),
                    rs.getInt("coef")
                );
                notes.add(note);
            }
        }
        
        return notes;
    }

    public static double calculateMoyenne(int etudiantId) throws SQLException, ClassNotFoundException {
        String query = """
            SELECT SUM(n.note * m.coef) / SUM(m.coef) as moyenne
            FROM notes n
            JOIN matieres m ON n.matiere_id = m.id
            WHERE n.etudiant_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, etudiantId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("moyenne");
            }
        }
        
        return 0.0;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEtudiantId() { return etudiantId; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }

    public int getMatiereId() { return matiereId; }
    public void setMatiereId(int matiereId) { this.matiereId = matiereId; }

    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }

    public String getEtudiantNom() { return etudiantNom; }
    public void setEtudiantNom(String etudiantNom) { this.etudiantNom = etudiantNom; }

    public String getMatiereNom() { return matiereNom; }
    public void setMatiereNom(String matiereNom) { this.matiereNom = matiereNom; }

    public int getMatiereCoef() { return matiereCoef; }
    public void setMatiereCoef(int matiereCoef) { this.matiereCoef = matiereCoef; }

    @Override
    public String toString() {
        return matiereNom + ": " + note + "/20";
    }
}