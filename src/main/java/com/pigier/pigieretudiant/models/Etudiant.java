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


    public Etudiant(String nom, String prenom, String matricule, String dateNaissance,
                    String lieuxNaissance, char genre, String contact, String email, String nationalite) {
        setNom(nom);
        setPrenom(prenom);
        setMatricule(matricule);
        setDateNaissance(dateNaissance);
        setLieuxNaissance(lieuxNaissance);
        setGenre(String.valueOf(genre));
        setContact(contact);
        setEmail(email);
        setNationalite(nationalite);
    }

    public void create(String idFIliere) throws SQLException {
        String query = "INSERT INTO etudiants(nom, prenoms, matricule, date_naissance, lieux_naissance, genre, telephone, email, nationnalite, filires_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setString(10, idFIliere);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'étudiant", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Etudiant> getAll() throws SQLException, ClassNotFoundException {
        List<Etudiant> list = new ArrayList<>();
        String query = "SELECT e.id AS etudiant_id, e.nom, e.prenoms, e.matricule, e.date_naissance, e.lieux_naissance, e.genre, e.nationnalite, e.telephone, e.email, e.adresse, i.annee_academique, i.statut, f.libelle AS filiere, n.code AS niveau, u.nom AS agent_nom, u.prenom AS agent_prenom FROM inscriptions i JOIN etudiants e ON e.id = i.etudiant_id JOIN filieresniveaux fn ON fn.id = i.filieresniveaux_id JOIN filieres f ON f.id = fn.filieres_id JOIN niveaux n ON n.id = fn.niveaux_id JOIN users u ON u.id = i.user_id ORDER BY e.nom, e.prenoms";
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
                        rs.getString("genre").charAt(0),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("nationnalite")
                );
                //                    etudiant.setFiliere(rs.getString("filiere"));
                list.add(etudiant);
            }
        }
        return list;
    }

    public static ResultSet liste() {
        String query = "SELECT * FROM etudiants WHERE annee_depart IS NULL ";

        try (Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




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

    // Getters et Setters
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

    public char getGenreChar() {
        String g = getGenre();
        return (g != null && !g.isEmpty()) ? g.charAt(0) : ' ';
    }
    public void setGenre(char genre) { setGenre(String.valueOf(genre)); }

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

}