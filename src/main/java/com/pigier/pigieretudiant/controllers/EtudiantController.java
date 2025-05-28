package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class EtudiantController {

    @FXML private TextField nomEtudiant;
    @FXML private TextField prenomsEtudiant;
    @FXML private TextField matriculeEtudiant;
    @FXML private DatePicker dateNaissanceEtudiant;
    @FXML private TextField lieuxNaissanceEtudiant;
    @FXML private ComboBox<String> genreEtudiant;
    @FXML private TextField contactEtudiant;
    @FXML private TextField emailEtudiant;
    @FXML private ComboBox<String> filierEtudiant;
    @FXML private ComboBox<String> niveauEtudiant;
    @FXML private TextField nationnaliteEtudiant;
    @FXML private Label erreurLabel;
    @FXML private Label successLabel;

    public void showInfoPersonnelle(){

    }
    public void editEtudiant() {
//        je doi recupperer de la base de donner
        Etudiant etudiant = new Etudiant("Youssouf", "DOUMDJE", "0501730", "2004-06-01", "DOBA", 'M', "0789681613", "dydoumdje2004@gmail.com", "Tchadienne");

        nomEtudiant.setText(etudiant.getNom());
        prenomsEtudiant.setText(etudiant.getPrenom());
        matriculeEtudiant.setText(etudiant.getMatricule());
        lieuxNaissanceEtudiant.setText(etudiant.getLieuxNaissance());
        dateNaissanceEtudiant.setValue(java.time.LocalDate.parse(etudiant.getDateNaissance())); // format yyyy-MM-dd
        genreEtudiant.setValue(String.valueOf(etudiant.getGenre()));
        contactEtudiant.setText(etudiant.getContact());
        emailEtudiant.setText(etudiant.getEmail());
        nationnaliteEtudiant.setText(etudiant.getNationnalite());
    }

    @FXML
    public void addEtudiant() throws IOException {
        SceneUtils.openModal("/com/pigier/pigieretudiant/views/etudiant/create.fxml");
    }

    public void createEtudiant(ActionEvent event) {
        try {
            // Validation des champs
            if (nomEtudiant.getText().isEmpty() || prenomsEtudiant.getText().isEmpty() ||
                    matriculeEtudiant.getText().isEmpty() || lieuxNaissanceEtudiant.getText().isEmpty() ||
                    contactEtudiant.getText().isEmpty() || emailEtudiant.getText().isEmpty() ||
                    nationnaliteEtudiant.getText().isEmpty()) {

                erreurLabel.setText("Tous les champs doivent être remplis.");
                return;
            }

            // Création de l'objet
            Etudiant etudiant = new Etudiant(
                    nomEtudiant.getText(),
                    prenomsEtudiant.getText(),
                    matriculeEtudiant.getText(),
                    dateNaissanceEtudiant.getValue().toString(),
                    lieuxNaissanceEtudiant.getText(),
                    'M', // À rendre dynamique selon une sélection radio par exemple
                    contactEtudiant.getText(),
                    emailEtudiant.getText(),
                    nationnaliteEtudiant.getText()
            );

            etudiant.create();
            successLabel.setText("Étudiant créé avec succès !");
            erreurLabel.setText("");

        } catch (SQLIntegrityConstraintViolationException e) {
            erreurLabel.setText("Erreur : doublon de matricule ou email !");
        } catch (SQLException e) {
            erreurLabel.setText("Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            erreurLabel.setText("Erreur inattendue : " + e.getMessage());
        }
    }


    public void cancelCreatEtudiant(ActionEvent event) throws IOException {
        SceneUtils.closeModal(event);
    }
}
