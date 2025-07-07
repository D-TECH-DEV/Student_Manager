package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.utils.SceneUtils;
import com.pigier.pigieretudiant.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class MainController {
    @FXML
    private Pane chilFenetre;
    
    @FXML
    private Label userNameLabel;

    public void initialize() throws SQLException, ClassNotFoundException {
        // Afficher le nom de l'utilisateur connecté
        if (userNameLabel != null) {
            userNameLabel.setText(SessionManager.getInstance().getCurrentUserName());
        }
        
        goToDashbord();
    }

    @FXML
    public void goToDashbord() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/dashbord.fxml", chilFenetre);
    }

    @FXML
    public void goToListeEtudiant() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/etudiant/list.fxml", chilFenetre);
    }

    @FXML
    public void goToDocuments() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/Document.fxml", chilFenetre);
    }

    @FXML
    public void goToStatistiques() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/Statistiques.fxml", chilFenetre);
    }

    @FXML
    public void goToFilieres() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/Filieres.fxml", chilFenetre);
    }

    @FXML
    public void goToMatieres() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/Matieres.fxml", chilFenetre);
    }

    @FXML
    public void goToNotes() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/Notes.fxml", chilFenetre);
    }

    @FXML
    public void goToRapports() {
        SceneUtils.changeChild("/com/pigier/pigieretudiant/views/Rapports.fxml", chilFenetre);
    }

    @FXML
    public void goToRessortissants() {
        // À implémenter selon vos besoins
        System.out.println("Navigation vers Ressortissants");
    }

    @FXML
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Confirmer la déconnexion");
        alert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Déconnecter l'utilisateur
                    SessionManager.getInstance().logout();
                    
                    // Rediriger vers la page de login
                    SceneUtils.openPage(chilFenetre, "/com/pigier/pigieretudiant/views/user/login.fxml", "Connexion");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}