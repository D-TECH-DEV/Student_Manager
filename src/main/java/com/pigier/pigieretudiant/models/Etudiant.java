package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Etudiant {
    private int id;
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty matricule = new SimpleStringProperty();
    private final StringProperty dateNaissance = new SimpleStringProperty();
    private final StringProperty lieuxNaissance = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final StringProperty contact = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty nationalite = new SimpleStringProperty();
    private final StringProperty filiere = new SimpleStringProperty();
    private final StringProperty niveau = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();

    public Etudiant(String nom, String prenom, String matricule, String dateNaissance,
                    String lieuxNaissance, String genre, String contact, String email, String nationalite) {
        setNom(nom);
        setPrenom(prenom);
        setMatricule(matricule);
        setDateNaissance(dateNaissance);
        setLieuxNaissance(lieuxNaissance);
        setGenre(genre);
        setContact(contact);
        setEmail(email);
        setNationalite(nationalite);
    }

    public void create(String filiereNiveauId) throws SQLException {
        String queryEtudiant = "INSERT INTO etudiants(nom, prenoms, matricule, date_naissance, lieux_naissance, genre, telephone, email, nationnalite, adresse) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String queryInscription = "INSERT INTO inscriptions(etudiant_id, filieresniveaux_id, user_id, annee_academique, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Insertion de l'étudiant
            PreparedStatement stmtEtudiant = conn.prepareStatement(queryEtudiant, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtEtudiant.setString(1, getNom());
            stmtEtudiant.setString(2, getPrenom());
            stmtEtudiant.setString(3, getMatricule());
            stmtEtudiant.setString(4, getDateNaissance());
            stmtEtudiant.setString(5, getLieuxNaissance());
            stmtEtudiant.setString(6, getGenre());
            stmtEtudiant.setString(7, getContact());
            stmtEtudiant.setString(8, getEmail());
            stmtEtudiant.setString(9, getNationalite());
            stmtEtudiant.setString(10, getAdresse());

            int affectedRows = stmtEtudiant.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmtEtudiant.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                    
                    // Insertion de l'inscription
                    PreparedStatement stmtInscription = conn.prepareStatement(queryInscription);
                    stmtInscription.setInt(1, this.id);
                    stmtInscription.setString(2, filiereNiveauId);
                    stmtInscription.setInt(3, 1); // ID de l'utilisateur par défaut
                    stmtInscription.setString(4, "2024-2025");
                    stmtInscription.setString(5, "inscrit");
                    
                    stmtInscription.executeUpdate();
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'étudiant", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() throws SQLException {
        String query = "UPDATE etudiants SET nom=?, prenoms=?, matricule=?, date_naissance=?, lieux_naissance=?, genre=?, telephone=?, email=?, nationnalite=?, adresse=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, getNom());
            stmt.setString(2, getPrenom());
            stmt.setString(3, getMatricule());
            stmt.setString(4, getDateNaissance());
            stmt.setString(5, getLieuxNaissance());
            stmt.setString(6, getGenre());
            stmt.setString(7, getContact());
            stmt.setString(8, getEmail());
            stmt.setString(9, getNationalite());
            stmt.setString(10, getAdresse());
            stmt.setInt(11, this.id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'étudiant", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() throws SQLException {
        String query = "DELETE FROM etudiants WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'étudiant", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Etudiant> getAll() throws SQLException, ClassNotFoundException {
        List<Etudiant> list = new ArrayList<>();
        String query = """
            SELECT e.id AS etudiant_id, e.nom, e.prenoms, e.matricule, e.date_naissance, 
                   e.lieux_naissance, e.genre, e.nationnalite, e.telephone, e.email, e.adresse,
                   i.annee_academique, i.statut, f.libelle AS filiere, n.code AS niveau,
                   u.nom AS agent_nom, u.prenom AS agent_prenom 
            FROM inscriptions i 
            JOIN etudiants e ON e.id = i.etudiant_id 
            JOIN filieresniveaux fn ON fn.id = i.filieresniveaux_id 
            JOIN filieres f ON f.id = fn.filieres_id 
            JOIN niveaux n ON n.id = fn.niveaux_id 
            JOIN users u ON u.id = i.user_id 
            ORDER BY e.nom, e.prenoms
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Etudiant etudiant = new Etudiant(
                        rs.getString("nom"),
                        rs.getString("prenoms"),
                        rs.getString("matricule"),
                        rs.getString("date_naissance"),
                        rs.getString("lieux_naissance"),
                        rs.getString("genre"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("nationnalite")
                );
                etudiant.id = rs.getInt("etudiant_id");
                etudiant.setFiliere(rs.getString("filiere"));
                etudiant.setNiveau(rs.getString("niveau"));
                etudiant.setAdresse(rs.getString("adresse"));
                list.add(etudiant);
            }
        }
        return list;
    }

    public static Etudiant getById(int id) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM etudiants WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Etudiant etudiant = new Etudiant(
                        rs.getString("nom"),
                        rs.getString("prenoms"),
                        rs.getString("matricule"),
                        rs.getString("date_naissance"),
                        rs.getString("lieux_naissance"),
                        rs.getString("genre"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("nationnalite")
                );
                etudiant.id = rs.getInt("id");
                etudiant.setAdresse(rs.getString("adresse"));
                return etudiant;
            }
        }
        return null;
    }

    // Property methods
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty matriculeProperty() { return matricule; }
    public StringProperty dateNaissanceProperty() { return dateNaissance; }
    public StringProperty lieuxNaissanceProperty() { return lieuxNaissance; }
    public StringProperty genreProperty() { return genre; }
    public StringProperty contactProperty() { return contact; }
    public StringProperty emailProperty() { return email; }
    public StringProperty nationaliteProperty() { return nationalite; }
    public StringProperty filiereProperty() { return filiere; }
    public StringProperty niveauProperty() { return niveau; }
    public StringProperty adresseProperty() { return adresse; }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }

    public String getMatricule() { return matricule.get(); }
    public void setMatricule(String matricule) { this.matricule.set(matricule); }

    public String getDateNaissance() { return dateNaissance.get(); }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance.set(dateNaissance); }

    public String getLieuxNaissance() { return lieuxNaissance.get(); }
    public void setLieuxNaissance(String lieuxNaissance) { this.lieuxNaissance.set(lieuxNaissance); }

    public String getGenre() { return genre.get(); }
    public void setGenre(String genre) { this.genre.set(genre); }

    public String getContact() { return contact.get(); }
    public void setContact(String contact) { this.contact.set(contact); }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public String getNationalite() { return nationalite.get(); }
    public void setNationalite(String nationalite) { this.nationalite.set(nationalite); }

    public String getNationnalite() { return getNationalite(); }
    public void setNationnalite(String nationalite) { setNationalite(nationalite); }

    public String getFiliere() { return filiere.get(); }
    public void setFiliere(String filiere) { this.filiere.set(filiere); }

    public String getNiveau() { return niveau.get(); }
    public void setNiveau(String niveau) { this.niveau.set(niveau); }

    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String adresse) { this.adresse.set(adresse); }

    @Override
    public String toString() {
        return nom.get() + " " + prenom.get() + " (" + matricule.get() + ")";
    }
}