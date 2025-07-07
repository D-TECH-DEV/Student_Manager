package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Matiere {
    private int id;
    private String nomMatiere;
    private int coef;
    private int niveauId;
    private String niveauCode;

    public Matiere(String nomMatiere, int coef, int niveauId) {
        this.nomMatiere = nomMatiere;
        this.coef = coef;
        this.niveauId = niveauId;
    }

    public Matiere(int id, String nomMatiere, int coef, int niveauId, String niveauCode) {
        this.id = id;
        this.nomMatiere = nomMatiere;
        this.coef = coef;
        this.niveauId = niveauId;
        this.niveauCode = niveauCode;
    }

    public void create() throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO matieres (nom_matiere, coef, niveau_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nomMatiere);
            stmt.setInt(2, coef);
            stmt.setInt(3, niveauId);
            
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
        String query = "UPDATE matieres SET nom_matiere = ?, coef = ?, niveau_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nomMatiere);
            stmt.setInt(2, coef);
            stmt.setInt(3, niveauId);
            stmt.setInt(4, id);
            
            stmt.executeUpdate();
        }
    }

    public void delete() throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM matieres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static List<Matiere> getAll() throws SQLException, ClassNotFoundException {
        List<Matiere> matieres = new ArrayList<>();
        String query = """
            SELECT m.*, n.code as niveau_code
            FROM matieres m
            JOIN niveaux n ON m.niveau_id = n.id
            ORDER BY n.code, m.nom_matiere
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Matiere matiere = new Matiere(
                    rs.getInt("id"),
                    rs.getString("nom_matiere"),
                    rs.getInt("coef"),
                    rs.getInt("niveau_id"),
                    rs.getString("niveau_code")
                );
                matieres.add(matiere);
            }
        }
        
        return matieres;
    }

    public static List<Matiere> getByNiveau(int niveauId) throws SQLException, ClassNotFoundException {
        List<Matiere> matieres = new ArrayList<>();
        String query = "SELECT * FROM matieres WHERE niveau_id = ? ORDER BY nom_matiere";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, niveauId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Matiere matiere = new Matiere(
                    rs.getInt("id"),
                    rs.getString("nom_matiere"),
                    rs.getInt("coef"),
                    rs.getInt("niveau_id"),
                    ""
                );
                matieres.add(matiere);
            }
        }
        
        return matieres;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomMatiere() { return nomMatiere; }
    public void setNomMatiere(String nomMatiere) { this.nomMatiere = nomMatiere; }

    public int getCoef() { return coef; }
    public void setCoef(int coef) { this.coef = coef; }

    public int getNiveauId() { return niveauId; }
    public void setNiveauId(int niveauId) { this.niveauId = niveauId; }

    public String getNiveauCode() { return niveauCode; }
    public void setNiveauCode(String niveauCode) { this.niveauCode = niveauCode; }

    @Override
    public String toString() {
        return nomMatiere + " (Coef: " + coef + ")";
    }
}