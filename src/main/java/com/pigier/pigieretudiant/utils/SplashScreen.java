package com.pigier.pigieretudiant.utils;

import com.pigier.pigieretudiant.controllers.UserController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
        splashStage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            try {
//               FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/pigier/pigieretudiant/views/user/login.fxml"));
                FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/pigier/pigieretudiant/views/MainView.fxml"));

                Parent root = fxmlLoader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Pigier - Login");
                newStage.setMaximized(true);
                newStage.show();
                splashStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }
}
