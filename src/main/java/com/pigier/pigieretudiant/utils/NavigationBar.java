package com.pigier.pigieretudiant.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;
import java.util.Objects;

public class NavigationBar {



    @FXML
    public void goToDashbord(MouseEvent mouseEvent) throws IOException {
//        SceneUtils.changeChild(chilFenetre, fxmlPath);
//        SceneUtils.openPage((Node) mouseEvent.getSource(),"/com/pigier/pigieretudiant/views/dashbord.fxml", "Liste des Etudiant");
    }
    @FXML
    public void goToListeEtudiant(MouseEvent mouseEvent) throws IOException {
        SceneUtils.openPage((Node) mouseEvent.getSource(),"/com/pigier/pigieretudiant/views/etudiant/list.fxml", "Liste des Etudiant");

    }

    @FXML
    public static void goToRessortissant() throws IOException {
//        SceneUtils.openPage("/com/pigier/pigieretudiant/views/etudiant/list.fxml", "Liste des Etudiant");
    }
    @FXML
    public static void gotToFillier() throws IOException {
//        SceneUtils.openPage("/com/pigier/pigieretudiant/views/etudiant/list.fxml", "Liste des Etudiant");

    }



//    public void goToDashbord(MouseEvent event) {
//    }
}