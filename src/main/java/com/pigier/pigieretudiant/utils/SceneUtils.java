package com.pigier.pigieretudiant.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class SceneUtils {

    public static void openPage(Node sourceNode, String path, String title) throws IOException {
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource(path));
        Parent root = fxmlLoader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle("Pigier - " + title);
        newStage.setMaximized(true);
        newStage.show();
    }

    public static void openModal(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource(path));
        Parent root = fxmlLoader.load();

        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.UTILITY);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }


    public static void closeModal(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static void changeChild(StackPane chilFenetre, String fxmlPath) {
        try {
            Node nouveauContenu = FXMLLoader.load(Objects.requireNonNull(SceneUtils.class.getResource(fxmlPath)));
            chilFenetre.getChildren().setAll(nouveauContenu); // remplace tout le contenu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
