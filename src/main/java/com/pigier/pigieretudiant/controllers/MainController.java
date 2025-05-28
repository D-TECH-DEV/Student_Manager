package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private Pane chilFenetre; // correspond à ton fx:id dans le fichier parent

    public void initialize() {
        try {
            Node nouveauContenu = FXMLLoader.load(Objects.requireNonNull(SceneUtils.class.getResource("/com/pigier/pigieretudiant/views/dashbord.fxml")));
            chilFenetre.getChildren().setAll(nouveauContenu); // remplace tout le contenu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToDashbord() {
        try {
            Node nouveauContenu = FXMLLoader.load(Objects.requireNonNull(SceneUtils.class.getResource("/com/pigier/pigieretudiant/views/dashbord.fxml")));
            chilFenetre.getChildren().setAll(nouveauContenu); // remplace tout le contenu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToListeEtudiant() {
        try {
            Node nouveauContenu = FXMLLoader.load(Objects.requireNonNull(SceneUtils.class.getResource("/com/pigier/pigieretudiant/views/etudiant/list.fxml")));
            chilFenetre.getChildren().setAll(nouveauContenu); // remplace tout le contenu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}