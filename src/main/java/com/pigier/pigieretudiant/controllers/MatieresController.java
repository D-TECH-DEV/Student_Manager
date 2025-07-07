package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Matiere;
import com.pigier.pigieretudiant.models.Niveaux;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class MatieresController {

    @FXML private TextField nomMatiereField;
    @FXML private TextField coefField;
    @FXML private ComboBox<String> niveauComboBox;
    @FXML private TableView<Matiere> matieresTable;
    @FXML private TableColumn<Matiere, String> colNomMatiere;
    @FXML private TableColumn<Matiere, Integer> colCoef;
    @FXML private TableColumn<Matiere, String> colNiveau;
    @FXML private TableColumn<Matiere, Void> colActions;
    @FXML private Label messageLabel;

    private ObservableList<Matiere> matieresList = FXCollections.observableArrayList();
    private Matiere selectedMatiere;

    public void initialize() {
        try {
            initializeComboBoxes();
            initializeTable();
            loadMatieres();
        } catch (Exception e) {
            showMessage("Erreur d'initialisation: " + e.getMessage(), true);
        }
    }

    private void initializeComboBoxes() throws SQLException {
        // Charger les niveaux
        niveauComboBox.setItems(FXCollections.observableArrayList(Niveaux.getListe()));
    }

    private void initializeTable() {
        colNomMatiere.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));
        colCoef.setCellValueFactory(new PropertyValueFactory<>("coef"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveauCode"));

        // Ajouter les boutons d'action
        addActionButtonsToTable();
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<Matiere, Void>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");

            {
                editBtn.setStyle("-fx-background-color: #4361ee; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                
                editBtn.setOnAction(event -> {
                    Matiere matiere = getTableView().getItems().get(getIndex());
                    editMatiere(matiere);
                });
                
                deleteBtn.setOnAction(event -> {
                    Matiere matiere = getTableView().getItems().get(getIndex());
                    deleteMatiere(matiere);
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
    private void ajouterMatiere(ActionEvent event) {
        try {
            if (isFormValid()) {
                if (selectedMatiere == null) {
                    // Création d'une nouvelle matière
                    int niveauId = getNiveauIdByCode(niveauComboBox.getValue());
                    Matiere matiere = new Matiere(
                        nomMatiereField.getText().trim(),
                        Integer.parseInt(coefField.getText().trim()),
                        niveauId
                    );
                    matiere.create();
                    showMessage("Matière ajoutée avec succès!", false);
                } else {
                    // Modification d'une matière existante
                    selectedMatiere.setNomMatiere(nomMatiereField.getText().trim());
                    selectedMatiere.setCoef(Integer.parseInt(coefField.getText().trim()));
                    selectedMatiere.setNiveauId(getNiveauIdByCode(niveauComboBox.getValue()));
                    selectedMatiere.update();
                    showMessage("Matière modifiée avec succès!", false);
                }
                
                clearForm();
                loadMatieres();
            } else {
                showMessage("Veuillez remplir tous les champs correctement", true);
            }
        } catch (Exception e) {
            showMessage("Erreur lors de l'opération: " + e.getMessage(), true);
        }
    }

    private void editMatiere(Matiere matiere) {
        selectedMatiere = matiere;
        nomMatiereField.setText(matiere.getNomMatiere());
        coefField.setText(String.valueOf(matiere.getCoef()));
        niveauComboBox.setValue(matiere.getNiveauCode());
    }

    private void deleteMatiere(Matiere matiere) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la matière");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette matière ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    matiere.delete();
                    showMessage("Matière supprimée avec succès!", false);
                    loadMatieres();
                } catch (Exception e) {
                    showMessage("Erreur lors de la suppression: " + e.getMessage(), true);
                }
            }
        });
    }

    private void loadMatieres() {
        try {
            matieresList.clear();
            matieresList.addAll(Matiere.getAll());
            matieresTable.setItems(matieresList);
        } catch (Exception e) {
            showMessage("Erreur de chargement: " + e.getMessage(), true);
        }
    }

    private int getNiveauIdByCode(String code) {
        // Mapping simple des codes vers les IDs
        switch (code) {
            case "L1": return 1;
            case "L2": return 2;
            case "L3": return 3;
            case "M1": return 4;
            case "M2": return 5;
            default: return 1;
        }
    }

    private boolean isFormValid() {
        try {
            int coef = Integer.parseInt(coefField.getText().trim());
            return nomMatiereField.getText() != null && !nomMatiereField.getText().trim().isEmpty() &&
                   coef > 0 && niveauComboBox.getValue() != null;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearForm() {
        selectedMatiere = null;
        nomMatiereField.clear();
        coefField.clear();
        niveauComboBox.setValue(null);
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }
}