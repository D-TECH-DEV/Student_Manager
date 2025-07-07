package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeDocument {
    private int id;
    private String libelle;

    public TypeDocument(String libelle) {
        this.libelle = libelle;
    }

    public TypeDocument(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public static List<TypeDocument> getAll() throws SQLException, ClassNotFoundException {
        List<TypeDocument> types = new ArrayList<>();
        String query = "SELECT * FROM typedocuments ORDER BY libelle";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TypeDocument type = new TypeDocument(
                    rs.getInt("id"),
                    rs.getString("libelle")
                );
                types.add(type);
            }
        }
        
        return types;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    @Override
    public String toString() {
        return libelle;
    }
}