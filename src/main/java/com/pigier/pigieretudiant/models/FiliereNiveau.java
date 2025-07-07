package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FiliereNiveau {
    private int id;
    private int niveauxId;
    private int filieresId;
    private String filiereLibelle;
    private String niveauCode;

    public FiliereNiveau(int id, int niveauxId, int filieresId, String filiereLibelle, String niveauCode) {
        this.id = id;
        this.niveauxId = niveauxId;
        this.filieresId = filieresId;
        this.filiereLibelle = filiereLibelle;
        this.niveauCode = niveauCode;
    }

    public static List<FiliereNiveau> getAll() throws SQLException, ClassNotFoundException {
        List<FiliereNiveau> filiereNiveaux = new ArrayList<>();
        String query = """
            SELECT fn.id, fn.niveaux_id, fn.filieres_id, f.libelle as filiere_libelle, n.code as niveau_code
            FROM filieresniveaux fn
            JOIN filieres f ON fn.filieres_id = f.id
            JOIN niveaux n ON fn.niveaux_id = n.id
            ORDER BY f.libelle, n.code
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FiliereNiveau fn = new FiliereNiveau(
                    rs.getInt("id"),
                    rs.getInt("niveaux_id"),
                    rs.getInt("filieres_id"),
                    rs.getString("filiere_libelle"),
                    rs.getString("niveau_code")
                );
                filiereNiveaux.add(fn);
            }
        }
        
        return filiereNiveaux;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNiveauxId() { return niveauxId; }
    public void setNiveauxId(int niveauxId) { this.niveauxId = niveauxId; }

    public int getFilieresId() { return filieresId; }
    public void setFilieresId(int filieresId) { this.filieresId = filieresId; }

    public String getFiliereLibelle() { return filiereLibelle; }
    public void setFiliereLibelle(String filiereLibelle) { this.filiereLibelle = filiereLibelle; }

    public String getNiveauCode() { return niveauCode; }
    public void setNiveauCode(String niveauCode) { this.niveauCode = niveauCode; }

    @Override
    public String toString() {
        return filiereLibelle + " - " + niveauCode;
    }
}