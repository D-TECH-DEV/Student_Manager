package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String nom;
    private String prenoms;
    private String email;
    private String mot_de_passe;

    public User(String nom, String prenoms, String email, String mot_de_passe, String type_user) {
        this.nom = nom;
        this.prenoms = prenoms;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
    }

    public void create() {
        String query = "INSERT INTO `users` (`nom`, `prenoms`, `email`, `mot_de_passe`) VALUES (?, ?, ?, ?);";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, this.nom);
            stmt.setString(2, this.prenoms);
            stmt.setString(3, this.email);
            stmt.setString(4, this.mot_de_passe);

            stmt.executeUpdate(); // exécute l'insertion

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static String checkLogin(String email, String password) {
        String query = "SELECT mot_de_passe FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return "Email introuvable";
            }

            String dbPassword = result.getString("mot_de_passe");
            if (!dbPassword.equals(password)) {
                return "Mot de passe incorrect";
            }

            return "OK";
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}