package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.User;
import com.pigier.pigieretudiant.utils.SceneUtils;
import com.pigier.pigieretudiant.utils.ValidationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserController {
    
    // Login fields
    @FXML TextField emailLogin;
    @FXML PasswordField passwordLogin;
    @FXML Label errorEmail;
    @FXML Label errorPassword;
    @FXML Label loginMessage;
    
    // Register fields
    @FXML TextField nomRegister;
    @FXML TextField prenomRegister;
    @FXML TextField telephoneRegister;
    @FXML TextField emailRegister;
    @FXML PasswordField passwordRegister;
    @FXML PasswordField passwordRegisterConfirmation;
    @FXML PasswordField codeInscription;
    @FXML Label erreurNom;
    @FXML Label erreurPrenom;
    @FXML Label erreurTelephone;
    @FXML Label erreurEmailRegister;
    @FXML Label erreurPasswordRegister;
    @FXML Label erreurPasswordConfirmation;
    @FXML Label erreurCodeInscription;
    @FXML Label registerMessage;

    @FXML
    public void goToRegister(ActionEvent event) throws IOException {
        SceneUtils.openPage((Node) event.getSource(), "/com/pigier/pigieretudiant/views/user/register.fxml", "Inscription");
    }

    @FXML
    public void gotToLogin(ActionEvent event) throws IOException {
        SceneUtils.openPage((Node) event.getSource(), "/com/pigier/pigieretudiant/views/user/login.fxml", "Connexion");
    }

    public void onLogin(ActionEvent event) throws IOException {
        clearLoginErrors();
        
        // Validation des champs
        if (!validateLoginForm()) {
            return;
        }

        try {
            String testLogin = User.checkLogin(emailLogin.getText().trim(), passwordLogin.getText());
            
            switch (testLogin) {
                case "OK" -> {
                    showLoginMessage("Connexion réussie ! Redirection...", false);
                    // Petit délai pour que l'utilisateur voie le message
                    javafx.application.Platform.runLater(() -> {
                        try {
                            SceneUtils.openPage((Node) event.getSource(), "/com/pigier/pigieretudiant/views/MainView.fxml", "Dashboard - Pigier");
                        } catch (IOException e) {
                            showLoginMessage("Erreur lors de la redirection", true);
                        }
                    });
                }
                case "Mot de passe incorrect" -> {
                    errorPassword.setText(testLogin);
                    showLoginMessage("Vérifiez vos identifiants", true);
                }
                case "Email introuvable" -> {
                    errorEmail.setText(testLogin);
                    showLoginMessage("Compte non trouvé", true);
                }
                default -> {
                    showLoginMessage("Erreur de connexion", true);
                }
            }
        } catch (Exception e) {
            showLoginMessage("Erreur système : " + e.getMessage(), true);
        }
    }

    public void onRegister(ActionEvent event) throws IOException {
        clearRegisterErrors();
        
        // Validation complète du formulaire
        if (!validateRegisterForm()) {
            return;
        }

        try {
            // Vérifier si l'email existe déjà
            if (User.emailExists(emailRegister.getText().trim())) {
                erreurEmailRegister.setText("Cet email est déjà utilisé");
                showRegisterMessage("Email déjà existant", true);
                return;
            }

            // Créer le nouvel utilisateur
            User user = new User(
                nomRegister.getText().trim(),
                prenomRegister.getText().trim(),
                emailRegister.getText().trim(),
                passwordRegister.getText(), // En production, il faudrait hasher le mot de passe
                telephoneRegister.getText().trim()
            );

            user.create();
            showRegisterMessage("Inscription réussie ! Redirection vers la connexion...", false);
            
            // Redirection automatique vers la page de login après 2 secondes
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(2000);
                    gotToLogin(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            showRegisterMessage("Erreur lors de l'inscription : " + e.getMessage(), true);
        }
    }

    private boolean validateLoginForm() {
        boolean isValid = true;

        // Validation email
        if (emailLogin.getText() == null || emailLogin.getText().trim().isEmpty()) {
            errorEmail.setText("Email requis");
            isValid = false;
        } else if (!ValidationUtils.isValidEmail(emailLogin.getText().trim())) {
            errorEmail.setText("Format d'email invalide");
            isValid = false;
        }

        // Validation mot de passe
        if (passwordLogin.getText() == null || passwordLogin.getText().isEmpty()) {
            errorPassword.setText("Mot de passe requis");
            isValid = false;
        }

        return isValid;
    }

    private boolean validateRegisterForm() {
        boolean isValid = true;

        // Validation nom
        if (!ValidationUtils.isValidName(nomRegister.getText())) {
            erreurNom.setText("Nom requis (min 2 caractères)");
            isValid = false;
        }

        // Validation prénom
        if (!ValidationUtils.isValidName(prenomRegister.getText())) {
            erreurPrenom.setText("Prénom requis (min 2 caractères)");
            isValid = false;
        }

        // Validation téléphone
        if (!ValidationUtils.isValidPhone(telephoneRegister.getText())) {
            erreurTelephone.setText("Numéro de téléphone invalide");
            isValid = false;
        }

        // Validation email
        if (!ValidationUtils.isValidEmail(emailRegister.getText())) {
            erreurEmailRegister.setText("Format d'email invalide");
            isValid = false;
        }

        // Validation mot de passe
        if (passwordRegister.getText() == null || passwordRegister.getText().length() < 6) {
            erreurPasswordRegister.setText("Mot de passe requis (min 6 caractères)");
            isValid = false;
        }

        // Validation confirmation mot de passe
        if (!passwordRegister.getText().equals(passwordRegisterConfirmation.getText())) {
            erreurPasswordConfirmation.setText("Les mots de passe ne correspondent pas");
            isValid = false;
        }

        // Validation code d'inscription
        if (!codeInscription.getText().equals("Pigier@univmetiers")) {
            erreurCodeInscription.setText("Code d'inscription incorrect");
            isValid = false;
        }

        return isValid;
    }

    private void clearLoginErrors() {
        if (errorEmail != null) errorEmail.setText("");
        if (errorPassword != null) errorPassword.setText("");
        if (loginMessage != null) loginMessage.setText("");
    }

    private void clearRegisterErrors() {
        if (erreurNom != null) erreurNom.setText("");
        if (erreurPrenom != null) erreurPrenom.setText("");
        if (erreurTelephone != null) erreurTelephone.setText("");
        if (erreurEmailRegister != null) erreurEmailRegister.setText("");
        if (erreurPasswordRegister != null) erreurPasswordRegister.setText("");
        if (erreurPasswordConfirmation != null) erreurPasswordConfirmation.setText("");
        if (erreurCodeInscription != null) erreurCodeInscription.setText("");
        if (registerMessage != null) registerMessage.setText("");
    }

    private void showLoginMessage(String message, boolean isError) {
        if (loginMessage != null) {
            loginMessage.setText(message);
            loginMessage.setStyle(isError ? "-fx-text-fill: #dc3545;" : "-fx-text-fill: #28a745;");
        }
    }

    private void showRegisterMessage(String message, boolean isError) {
        if (registerMessage != null) {
            registerMessage.setText(message);
            registerMessage.setStyle(isError ? "-fx-text-fill: #dc3545;" : "-fx-text-fill: #28a745;");
        }
    }

    // Méthodes pour la validation en temps réel
    @FXML
    private void validateEmailOnType() {
        if (emailLogin != null && emailLogin.getText() != null && !emailLogin.getText().trim().isEmpty()) {
            if (!ValidationUtils.isValidEmail(emailLogin.getText().trim())) {
                errorEmail.setText("Format d'email invalide");
            } else {
                errorEmail.setText("");
            }
        }
    }

    @FXML
    private void validateRegisterEmailOnType() {
        if (emailRegister != null && emailRegister.getText() != null && !emailRegister.getText().trim().isEmpty()) {
            if (!ValidationUtils.isValidEmail(emailRegister.getText().trim())) {
                erreurEmailRegister.setText("Format d'email invalide");
            } else {
                erreurEmailRegister.setText("");
            }
        }
    }

    @FXML
    private void validatePasswordConfirmationOnType() {
        if (passwordRegister != null && passwordRegisterConfirmation != null) {
            if (!passwordRegister.getText().equals(passwordRegisterConfirmation.getText())) {
                erreurPasswordConfirmation.setText("Les mots de passe ne correspondent pas");
            } else {
                erreurPasswordConfirmation.setText("");
            }
        }
    }
}