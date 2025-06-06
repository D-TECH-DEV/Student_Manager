package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class MainController {
    @FXML
    private Pane chilFenetre;

    public void initialize() throws SQLException, ClassNotFoundException {
        goToDashbord();
//        System.out.println(Etudiant.getAll());
    }

    public void goToDashbord() {
        SceneUtils.changeChild( "/com/pigier/pigieretudiant/views/dashbord.fxml" ,chilFenetre);
    }

    public void goToListeEtudiant() {
        SceneUtils.changeChild( "/com/pigier/pigieretudiant/views/etudiant/list.fxml" ,chilFenetre);
    }
}