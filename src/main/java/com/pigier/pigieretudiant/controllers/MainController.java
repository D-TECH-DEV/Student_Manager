package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class MainController {
    @FXML
    private Pane chilFenetre;

    public void initialize() throws SQLException, ClassNotFoundException {
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
    public void goToRessortissants() {
        // À implémenter selon vos besoins
        System.out.println("Navigation vers Ressortissants");
    }

    @FXML
    public void logout() {
        try {
            SceneUtils.openPage(chilFenetre, "/com/pigier/pigieretudiant/views/user/login.fxml", "Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}