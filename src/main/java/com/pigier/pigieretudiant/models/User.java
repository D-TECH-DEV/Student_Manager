package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public User(int id, String nom, String prenom, String telephone, String email, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
    }

    public void create() {
        String query = "INSERT INTO `users` (`nom`, `prenom`, `telephone`, `email`, `password`) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, this.nom);
            stmt.setString(2, this.prenom);
            stmt.setString(3, this.telephone);
            stmt.setString(4, this.email);
            stmt.setString(5, this.password);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur: " + e.getMessage(), e);
        }
    }

    public void update() throws SQLException, ClassNotFoundException {
        String query = "UPDATE users SET nom = ?, prenom = ?, telephone = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, telephone);
            stmt.setString(4, email);
            stmt.setInt(5, id);
            
            stmt.executeUpdate();
        }
    }

    public static String checkLogin(String email, String password) {
        String query = "SELECT id, nom, prenom, password FROM users WHERE email = ?";

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

            // Ici on pourrait stocker les informations de l'utilisateur connecté
            // dans une session ou un singleton pour les utiliser dans l'application
            
            return "OK";
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException("Erreur lors de la vérification des identifiants: " + ex.getMessage(), ex);
        }
    }

    public static boolean emailExists(String email) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            
            if (result.next()) {
                return result.getInt(1) > 0;
            }
        }
        
        return false;
    }

    public static List<User> getAll() throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                users.add(user);
            }
        }
        
        return users;
    }

    public static User getById(int id) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        }
        
        return null;
    }

    public static User getByEmail(String email) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        }
        
        return null;
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

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + email + ")";
    }
}