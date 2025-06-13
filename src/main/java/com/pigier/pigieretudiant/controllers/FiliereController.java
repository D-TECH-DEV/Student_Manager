package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Filiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class FiliereController {

    @FXML private TextField codeFiliere;
    @FXML private TextField libelleFiliere;
    @FXML private TextArea descriptionFiliere;
    @FXML private TableView<FiliereModel> tableFiliere;
    @FXML private TableColumn<FiliereModel, String> colCode;
    @FXML private TableColumn<FiliereModel, String> colLibelle;
    @FXML private TableColumn<FiliereModel, String> colDescription;
    @FXML private TableColumn<FiliereModel, Void> colActions;
    @FXML private Label messageLabel;

    private ObservableList<FiliereModel> filieresList = FXCollections.observableArrayList();

    public void initialize() {
        try {
            initializeTable();
            loadFilieres();
        } catch (Exception e) {
            showMessage("Erreur d'initialisation: " + e.getMessage(), true);
        }
    }

    private void initializeTable() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        // Ajouter les boutons d'action
        addActionButtonsToTable();
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<FiliereModel, Void>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");

            {
                editBtn.setStyle("-fx-background-color: #4361ee; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                
                editBtn.setOnAction(event -> {
                    FiliereModel filiere = getTableView().getItems().get(getIndex());
                    editFiliere(filiere);
                });
                
                deleteBtn.setOnAction(event -> {
                    FiliereModel filiere = getTableView().getItems().get(getIndex());
                    deleteFiliere(filiere);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                    hbox.getChildren().addAll(editBtn, deleteBtn);
                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    private void ajouterFiliere(ActionEvent event) {
        try {
            if (isFormValid()) {
                // Ici vous devriez créer une nouvelle filière
                // FiliereModel.create(codeFiliere.getText(), libelleFiliere.getText(), descriptionFiliere.getText());
                
                showMessage("Filière ajoutée avec succès!", false);
                clearForm();
                loadFilieres();
            } else {
                showMessage("Veuillez remplir tous les champs obligatoires", true);
            }
        } catch (Exception e) {
            showMessage("Erreur lors de l'ajout: " + e.getMessage(), true);
        }
    }

    private void editFiliere(FiliereModel filiere) {
        codeFiliere.setText(filiere.getCode());
        libelleFiliere.setText(filiere.getLibelle());
        descriptionFiliere.setText(filiere.getDescription());
    }

    private void deleteFiliere(FiliereModel filiere) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la filière");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette filière ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // filiere.delete();
                    showMessage("Filière supprimée avec succès!", false);
                    loadFilieres();
                } catch (Exception e) {
                    showMessage("Erreur lors de la suppression: " + e.getMessage(), true);
                }
            }
        });
    }

    private void loadFilieres() {
        try {
            filieresList.clear();
            // Charger les filières depuis la base de données
            // List<FiliereModel> filieres = FiliereModel.getAll();
            // filieresList.addAll(filieres);
            tableFiliere.setItems(filieresList);
        } catch (Exception e) {
            showMessage("Erreur de chargement: " + e.getMessage(), true);
        }
    }

    private boolean isFormValid() {
        return codeFiliere.getText() != null && !codeFiliere.getText().trim().isEmpty() &&
               libelleFiliere.getText() != null && !libelleFiliere.getText().trim().isEmpty();
    }

    private void clearForm() {
        codeFiliere.clear();
        libelleFiliere.clear();
        descriptionFiliere.clear();
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }

    // Classe interne pour représenter une filière dans le tableau
    public static class FiliereModel {
        private String code;
        private String libelle;
        private String description;

        public FiliereModel(String code, String libelle, String description) {
            this.code = code;
            this.libelle = libelle;
            this.description = description;
        }

        // Getters et setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getLibelle() { return libelle; }
        public void setLibelle(String libelle) { this.libelle = libelle; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}