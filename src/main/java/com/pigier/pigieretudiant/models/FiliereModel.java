package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FiliereModel {
    private int id;
    private String code;
    private String libelle;
    private String description;

    public FiliereModel(String code, String libelle, String description) {
        this.code = code;
        this.libelle = libelle;
        this.description = description;
    }

    public FiliereModel(int id, String code, String libelle, String description) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
    }

    public void create() throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO filieres (code, libelle, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, code);
            stmt.setString(2, libelle);
            stmt.setString(3, description);
            
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
        String query = "UPDATE filieres SET code = ?, libelle = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, code);
            stmt.setString(2, libelle);
            stmt.setString(3, description);
            stmt.setInt(4, id);
            
            stmt.executeUpdate();
        }
    }

    public void delete() throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM filieres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static List<FiliereModel> getAll() throws SQLException, ClassNotFoundException {
        List<FiliereModel> filieres = new ArrayList<>();
        String query = "SELECT * FROM filieres ORDER BY libelle";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FiliereModel filiere = new FiliereModel(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("libelle"),
                    rs.getString("description")
                );
                filieres.add(filiere);
            }
        }
        
        return filieres;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return libelle + " (" + code + ")";
    }
}