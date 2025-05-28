package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Etudiant {
    private String nom;
    private String prenom;
    private String matricule;
    private String dateNaissance;
    private String lieuxNaissance;
    private char genre;
    private String contact;
    private String email;
    private String nationalite;


    public Etudiant(String nom, String prenom, String matricule, String dateNaissance, String lieuxNaissance, char genre, String contact, String email, String nationalite) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateNaissance = dateNaissance;
        this.lieuxNaissance = lieuxNaissance;
        this.genre = genre;
        this.contact = contact;
        this.email = email;
        this.nationalite = nationalite;
    }

    public void create() throws SQLException {
        String query = "INSERT INTO etudiants(nom, prenoms, matricule, date_naissance, lieux_naissance, genre, telephone, email, nationnalite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, this.nom);
            stmt.setString(2, this.prenom);
            stmt.setString(3, this.matricule);
            stmt.setString(4, this.dateNaissance);
            stmt.setString(5, this.lieuxNaissance);
            stmt.setString(6, String.valueOf(this.genre)); // genre est un char
            stmt.setString(7, this.contact);
            stmt.setString(8, this.email);
            stmt.setString(9, this.nationalite);

            stmt.executeUpdate(); // Pas de retour ResultSet ici
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'étudiant", e);
        }
    }




    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getNom() {
        return nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getPrenom() {
        return prenom;
    }

    public String getNationalite() {
        return nationalite;
    }
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getMatricule() {
        return matricule;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }
    public char getGenre() {
        return genre;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getContact() {
        return contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public String getLieuxNaissance() {
        return lieuxNaissance;
    }
    public void setLieuxNaissance(String lieuxNaissance) {
        this.lieuxNaissance = lieuxNaissance;
    }

    public String getNationnalite() {
        return nationalite;
    }
    public void  setNationnalite(String nationalite){
        this.nationalite = nationalite;
    }
}