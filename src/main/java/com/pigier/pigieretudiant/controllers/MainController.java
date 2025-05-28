package com.pigier.pigieretudiant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        loadPage("Accueil");
    }

    @FXML
    private void goToAccueil() {
        loadPage("Accueil");
    }

    @FXML
    private void goToEtudiants() {
        loadPage("Etudiant");
    }

    private void loadPage(String path) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/pigier/views/" + page + ".fxml"));
            contentArea.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
