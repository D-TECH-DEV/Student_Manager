package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.models.Filiere;
import com.pigier.pigieretudiant.models.Niveaux;
import com.pigier.pigieretudiant.utils.SceneUtils;
import com.pigier.pigieretudiant.utils.ValidationUtils;
import com.pigier.pigieretudiant.utils.ExportUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;

public class EtudiantController {

    // Form fields
    @FXML private TextField nomEtudiant;
    @FXML private TextField prenomsEtudiant;
    @FXML private TextField matriculeEtudiant;
    @FXML private DatePicker dateNaissanceEtudiant;
    @FXML private TextField lieuxNaissanceEtudiant;
    @FXML private ComboBox<String> genreEtudiant;
    @FXML private TextField contactEtudiant;
    @FXML private TextField emailEtudiant;
    @FXML private ComboBox<String> filierEtudiant;
    @FXML private ComboBox<String> niveauEtudiant;
    @FXML private TextField nationnaliteEtudiant;
    @FXML private Label erreurLabel;
    @FXML private Label successLabel;

    @FXML private TableView<Etudiant> tableEtudiants;
    @FXML private TableColumn<Etudiant, String> colNom;
    @FXML private TableColumn<Etudiant, String> colPrenoms;
    @FXML private TableColumn<Etudiant, String> colMatricule;
    @FXML private TableColumn<Etudiant, String> colFiliere;
    @FXML private TableColumn<Etudiant, String> colNiveau;
    @FXML private TableColumn<Etudiant, Void> colActions;
    @FXML private ComboBox<String> filterFiliere;
    @FXML private ComboBox<String> filterNiveaux;
    @FXML private TextField searchField;

    private ObservableList<Etudiant> etudiantsList = FXCollections.observableArrayList();
    private Etudiant selectedEtudiant;

    public void initialize() throws SQLException {
        if (tableEtudiants != null && colNom != null) {
            initializeTable();
        }

        if (genreEtudiant != null) {
            genreEtudiant.getItems().addAll("M", "F");
        }

        if (filierEtudiant != null) {
            filierEtudiant.getItems().addAll(Filiere.getListe());
        }

        if (niveauEtudiant != null) {
            niveauEtudiant.getItems().addAll(Niveaux.getListe());
        }

        if (filterFiliere != null) {
            filterFiliere.getItems().add("Toutes les filières");
            filterFiliere.getItems().addAll(Filiere.getListe());
            filterFiliere.setValue("Toutes les filières");
        }
        
        if (filterNiveaux != null) {
            filterNiveaux.getItems().add("Tous les niveaux");
            filterNiveaux.getItems().addAll(Niveaux.getListe());
            filterNiveaux.setValue("Tous les niveaux");
        }

        if (tableEtudiants != null) {
            refreshData(null);
        }
    }

    private void initializeTable() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenoms.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));

        // Ajouter les boutons d'action
        addActionButtonsToTable();
        
        // Ajouter un listener pour la sélection
        tableEtudiants.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                selectedEtudiant = newSelection;
            }
        );
    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<Etudiant, Void>, TableCell<Etudiant, Void>> cellFactory = 
            new Callback<TableColumn<Etudiant, Void>, TableCell<Etudiant, Void>>() {
                @Override
                public TableCell<Etudiant, Void> call(final TableColumn<Etudiant, Void> param) {
                    final TableCell<Etudiant, Void> cell = new TableCell<Etudiant, Void>() {

                        private final Button editBtn = new Button("Modifier");
                        private final Button deleteBtn = new Button("Supprimer");

                        {
                            editBtn.setStyle("-fx-background-color: #4361ee; -fx-text-fill: white; -fx-background-radius: 5;");
                            deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5;");
                            
                            editBtn.setOnAction((ActionEvent event) -> {
                                Etudiant etudiant = getTableView().getItems().get(getIndex());
                                editEtudiant(etudiant);
                            });
                            
                            deleteBtn.setOnAction((ActionEvent event) -> {
                                Etudiant etudiant = getTableView().getItems().get(getIndex());
                                deleteEtudiant(etudiant);
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
                    };
                    return cell;
                }
            };

        colActions.setCellFactory(cellFactory);
    }

    public void showInfoPersonnelle() {
        // Méthode pour afficher les informations personnelles
    }

    public void editEtudiant(Etudiant etudiant) {
        if (etudiant == null) return;
        
        try {
            // Charger le formulaire d'édition avec les données de l'étudiant
            if (nomEtudiant != null) nomEtudiant.setText(etudiant.getNom());
            if (prenomsEtudiant != null) prenomsEtudiant.setText(etudiant.getPrenom());
            if (matriculeEtudiant != null) matriculeEtudiant.setText(etudiant.getMatricule());
            if (lieuxNaissanceEtudiant != null) lieuxNaissanceEtudiant.setText(etudiant.getLieuxNaissance());
            if (dateNaissanceEtudiant != null && etudiant.getDateNaissance() != null) {
                dateNaissanceEtudiant.setValue(java.time.LocalDate.parse(etudiant.getDateNaissance()));
            }
            if (genreEtudiant != null) genreEtudiant.setValue(etudiant.getGenre());
            if (contactEtudiant != null) contactEtudiant.setText(etudiant.getContact());
            if (emailEtudiant != null) emailEtudiant.setText(etudiant.getEmail());
            if (nationnaliteEtudiant != null) nationnaliteEtudiant.setText(etudiant.getNationalite());
            if (filierEtudiant != null) filierEtudiant.setValue(etudiant.getFiliere());
            
            selectedEtudiant = etudiant;
            
            // Ouvrir le modal d'édition
            SceneUtils.openModal("/com/pigier/pigieretudiant/views/etudiant/create.fxml");
        } catch (Exception e) {
            showError("Erreur lors du chargement des données: " + e.getMessage());
        }
    }

    private void deleteEtudiant(Etudiant etudiant) {
        if (etudiant == null) return;
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'étudiant");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer " + etudiant.getNom() + " " + etudiant.getPrenom() + " ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    etudiant.delete();
                    refreshData(null);
                    showSuccess("Étudiant supprimé avec succès!");
                } catch (Exception e) {
                    showError("Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void addEtudiant() throws IOException {
        clearForm();
        selectedEtudiant = null;
        SceneUtils.openModal("/com/pigier/pigieretudiant/views/etudiant/create.fxml");
    }

    public void createEtudiant(ActionEvent event) {
        try {
            // Validation des données
            ValidationUtils.ValidationResult validation = ValidationUtils.validateEtudiant(
                nomEtudiant.getText(),
                prenomsEtudiant.getText(),
                matriculeEtudiant.getText(),
                emailEtudiant.getText(),
                contactEtudiant.getText()
            );

            if (!validation.isValid()) {
                showError(validation.getMessage());
                return;
            }

            if (selectedEtudiant == null) {
                // Création d'un nouveau étudiant
                Etudiant nouvelEtudiant = new Etudiant(
                        nomEtudiant.getText().trim(),
                        prenomsEtudiant.getText().trim(),
                        ValidationUtils.formatMatricule(matriculeEtudiant.getText()),
                        dateNaissanceEtudiant.getValue() != null ? dateNaissanceEtudiant.getValue().toString() : "",
                        lieuxNaissanceEtudiant.getText().trim(),
                        genreEtudiant.getValue() != null ? genreEtudiant.getValue().charAt(0) : ' ',
                        ValidationUtils.formatPhone(contactEtudiant.getText()),
                        emailEtudiant.getText().trim(),
                        nationnaliteEtudiant.getText().trim()
                );

                if (filierEtudiant.getValue() != null) {
                    nouvelEtudiant.setFiliere(filierEtudiant.getValue());
                }

                nouvelEtudiant.create("1");
                showSuccess("Étudiant créé avec succès!");
            } else {
                // Mise à jour d'un étudiant existant
                selectedEtudiant.setNom(nomEtudiant.getText().trim());
                selectedEtudiant.setPrenom(prenomsEtudiant.getText().trim());
                selectedEtudiant.setMatricule(ValidationUtils.formatMatricule(matriculeEtudiant.getText()));
                selectedEtudiant.setDateNaissance(dateNaissanceEtudiant.getValue() != null ? 
                    dateNaissanceEtudiant.getValue().toString() : "");
                selectedEtudiant.setLieuxNaissance(lieuxNaissanceEtudiant.getText().trim());
                selectedEtudiant.setGenre(genreEtudiant.getValue() != null ? 
                    genreEtudiant.getValue().charAt(0) : ' ');
                selectedEtudiant.setContact(ValidationUtils.formatPhone(contactEtudiant.getText()));
                selectedEtudiant.setEmail(emailEtudiant.getText().trim());
                selectedEtudiant.setNationalite(nationnaliteEtudiant.getText().trim());
                if (filierEtudiant.getValue() != null) {
                    selectedEtudiant.setFiliere(filierEtudiant.getValue());
                }

                selectedEtudiant.update();
                showSuccess("Étudiant modifié avec succès!");
            }

            clearForm();
            refreshData(null);

        } catch (Exception e) {
            showError("Erreur lors de l'opération: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        if (nomEtudiant != null) nomEtudiant.clear();
        if (prenomsEtudiant != null) prenomsEtudiant.clear();
        if (matriculeEtudiant != null) matriculeEtudiant.clear();
        if (lieuxNaissanceEtudiant != null) lieuxNaissanceEtudiant.clear();
        if (contactEtudiant != null) contactEtudiant.clear();
        if (emailEtudiant != null) emailEtudiant.clear();
        if (nationnaliteEtudiant != null) nationnaliteEtudiant.clear();
        if (dateNaissanceEtudiant != null) dateNaissanceEtudiant.setValue(null);
        if (genreEtudiant != null) genreEtudiant.setValue(null);
        if (filierEtudiant != null) filierEtudiant.setValue(null);
        if (niveauEtudiant != null) niveauEtudiant.setValue(null);
        if (erreurLabel != null) erreurLabel.setText("");
        if (successLabel != null) successLabel.setText("");
    }

    public void cancelCreatEtudiant(ActionEvent event) throws IOException {
        clearForm();
        selectedEtudiant = null;
        SceneUtils.closeModal(event);
    }

    public void filterByFiliere(ActionEvent event) {
        if (filterFiliere == null || tableEtudiants == null) return;

        String filiere = filterFiliere.getValue();
        String niveau = filterNiveaux != null ? filterNiveaux.getValue() : "Tous les niveaux";
        
        applyFilters(filiere, niveau);
    }

    public void filterByNiveau(ActionEvent event) {
        if (filterNiveaux == null || tableEtudiants == null) return;

        String niveau = filterNiveaux.getValue();
        String filiere = filterFiliere != null ? filterFiliere.getValue() : "Toutes les filières";
        
        applyFilters(filiere, niveau);
    }

    private void applyFilters(String filiere, String niveau) {
        ObservableList<Etudiant> filtered = etudiantsList;
        
        if (!filiere.equals("Toutes les filières")) {
            filtered = filtered.filtered(etudiant -> filiere.equalsIgnoreCase(etudiant.getFiliere()));
        }
        
        if (!niveau.equals("Tous les niveaux")) {
            filtered = filtered.filtered(etudiant -> niveau.equalsIgnoreCase(etudiant.getNiveau()));
        }
        
        tableEtudiants.setItems(filtered);
    }

    public void refreshData(ActionEvent event) {
        if (tableEtudiants == null) return;

        try {
            etudiantsList.clear();
            etudiantsList.addAll(Etudiant.getAll());
            tableEtudiants.setItems(etudiantsList);
            showSuccess("Données actualisées");
        } catch (Exception e) {
            showError("Erreur de chargement : " + e.getMessage());
        }
    }

    public void exportData(ActionEvent event) {
        if (tableEtudiants == null) return;

        try {
            ExportUtils.exportEtudiantsToCSV(tableEtudiants.getItems(), tableEtudiants.getScene().getWindow());
            showSuccess("Export réussi !");
        } catch (IOException e) {
            showError("Erreur export : " + e.getMessage());
        }
    }

    public void handleSearch(ActionEvent event) {
        if (searchField == null || tableEtudiants == null) return;

        String keyword = searchField.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tableEtudiants.setItems(etudiantsList);
        } else {
            ObservableList<Etudiant> result = etudiantsList.filtered(
                    etudiant -> etudiant.getNom().toLowerCase().contains(keyword) ||
                            etudiant.getPrenom().toLowerCase().contains(keyword) ||
                            etudiant.getMatricule().toLowerCase().contains(keyword)
            );
            tableEtudiants.setItems(result);
        }
    }

    public void filterAllFilieres(ActionEvent event) {
        if (filterFiliere == null || tableEtudiants == null) return;

        filterFiliere.setValue("Toutes les filières");
        if (filterNiveaux != null) {
            filterNiveaux.setValue("Tous les niveaux");
        }
        tableEtudiants.setItems(etudiantsList);
    }

    private void showError(String message) {
        if (erreurLabel != null) {
            erreurLabel.setText(message);
        }
        if (successLabel != null) {
            successLabel.setText("");
        }
    }

    private void showSuccess(String message) {
        if (successLabel != null) {
            successLabel.setText(message);
        }
        if (erreurLabel != null) {
            erreurLabel.setText("");
        }
    }
}