package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.User;
import com.pigier.pigieretudiant.utils.NavigationBar;
import com.pigier.pigieretudiant.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;

public class UserController {
    @FXML TextField emailLogin;
    @FXML TextField passwordLogin;
    @FXML Label errorEmail;
    @FXML Label errorPassword;
    @FXML TextField emailRegister;

    @FXML PasswordField passwordRegister;
    @FXML PasswordField passwordRegisterConfirmation;
    @FXML PasswordField codeInscription;
    @FXML Label erreurPasswordConfirmation;
    @FXML Label erreurCodeInscription;


    @FXML
    public void goToRegister(ActionEvent event) throws IOException {
        SceneUtils.openPage((Node) event.getSource(),"/com/pigier/pigieretudiant/views/user/register.fxml", "Login");
    }

    @FXML
    public void gotToLogin(ActionEvent event) throws IOException {
        SceneUtils.openPage((Node) event.getSource(),"/com/pigier/pigieretudiant/views/user/login.fxml", "Login");
    }



    public void onLogin(ActionEvent event) throws IOException {
        String testLogin = User.checkLogin(emailLogin.getText(), passwordLogin.getText());
        //System.out.println(testLogin);
        switch (testLogin) {
            case "OK" -> {
                SceneUtils.openPage((Node) event.getSource(),"/com/pigier/pigieretudiant/views/MainView.fxml", "Dashbord");

//                NavigationBar.goToDashbord(event);
//                EtudiantController etudiantController = new EtudiantController();
//                etudiantController.showInfoPersonnelle();

            }
            case "Mot de passe incorrect" -> {
                errorPassword.setText(testLogin);
                errorEmail.setText("");
            }
            case "Email introuvable" -> {
                errorPassword.setText("");
                errorEmail.setText(testLogin);
            }
        }
    }


    public void onRegister(ActionEvent event) throws IOException {
        erreurCodeInscription.setText("");
        erreurPasswordConfirmation.setText("");
        if (!passwordRegister.getText().equals(passwordRegisterConfirmation.getText())){
            erreurPasswordConfirmation.setText("Erreur de confimation");
        }
        if (!codeInscription.getText().equals("Pigier@univmetiers")){
            erreurCodeInscription.setText("Code d'inscription incorrect");
        }
        if(codeInscription.getText().isEmpty()){
            erreurCodeInscription.setText("Code d'inscription obligatoire");
        }
        User user = new User("", "", emailRegister.getText(),passwordRegister.getText(), "admin");

        user.create();
        gotToLogin(event);
    }


}