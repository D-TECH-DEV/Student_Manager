package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String password;

    public User(String nom, String prenom, String email, String password, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
    }

    public void create() {
        String query = "INSERT INTO `users` (`nom`, `prenom`, `telephone`, `email`, `password`) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, this.nom);
            stmt.setString(2, this.prenom);
            stmt.setString(3, this.telephone);
            stmt.setString(4, this.email);
            stmt.setString(5, this.password);

            stmt.executeUpdate();

        }catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String checkLogin(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return "Email introuvable";
            }

            String dbPassword = result.getString("password");
            if (!dbPassword.equals(password)) {
                return "Mot de passe incorrect";
            }

            return "OK";
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}