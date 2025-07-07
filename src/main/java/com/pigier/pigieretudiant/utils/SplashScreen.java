package com.pigier.pigieretudiant.utils;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;

public class SplashScreen extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent splashRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pigier/pigieretudiant/views/splash.fxml")));
        Scene splashScene = new Scene(splashRoot);

        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);
        splashStage.setTitle("Pigier - Chargement");
        splashStage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            try {
                // Rediriger vers la page de login au lieu du MainView
                FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/pigier/pigieretudiant/views/user/login.fxml"));
                Parent root = fxmlLoader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Pigier - Connexion");
                newStage.setResizable(false);
                newStage.show();
                splashStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }
}