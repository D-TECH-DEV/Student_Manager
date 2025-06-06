package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.models.Filiere;
import com.pigier.pigieretudiant.models.Niveaux;
import com.pigier.pigieretudiant.utils.SceneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.FileWriter;
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
    @FXML private ComboBox<String> filterFiliere;
    @FXML private ComboBox<String> filterNiveaux;
    @FXML private TextField searchField;

    private ObservableList<Etudiant> etudiantsList = FXCollections.observableArrayList();

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
//            filterFiliere.getItems().add("Toutes");

            filterFiliere.getItems().addAll( Filiere.getListe());
            filterFiliere.setValue("Toutes les filières");
//            filterFiliere.setValue("Toutes");

        }
        if (filterNiveaux != null) {
            filterNiveaux.getItems().add("Tous les niveaux");
            filterNiveaux.getItems().addAll( Niveaux.getListe());
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
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("niveau"));

    }

    public void showInfoPersonnelle() {
    }

    public void editEtudiant() {
        Etudiant etudiant = new Etudiant("Youssouf", "DOUMDJE", "0501730", "2004-06-01", "DOBA", 'M', "0789681613", "dydoumdje2004@gmail.com", "Tchadienne");

        if (nomEtudiant != null) nomEtudiant.setText(etudiant.getNom());
        if (prenomsEtudiant != null) prenomsEtudiant.setText(etudiant.getPrenom());
        if (matriculeEtudiant != null) matriculeEtudiant.setText(etudiant.getMatricule());
        if (lieuxNaissanceEtudiant != null) lieuxNaissanceEtudiant.setText(etudiant.getLieuxNaissance());
        if (dateNaissanceEtudiant != null) dateNaissanceEtudiant.setValue(java.time.LocalDate.parse(etudiant.getDateNaissance()));
        if (genreEtudiant != null) genreEtudiant.setValue(String.valueOf(etudiant.getGenre()));
        if (contactEtudiant != null) contactEtudiant.setText(etudiant.getContact());
        if (emailEtudiant != null) emailEtudiant.setText(etudiant.getEmail());
        if (nationnaliteEtudiant != null) nationnaliteEtudiant.setText(etudiant.getNationnalite());
    }

    @FXML
    public void addEtudiant() throws IOException {
        SceneUtils.openModal("/com/pigier/pigieretudiant/views/etudiant/create.fxml");
    }

    public void createEtudiant(ActionEvent event) {
        try {
            if (isFormValid()) {
                Etudiant nouvelEtudiant = new Etudiant(
                        nomEtudiant.getText().trim(),
                        prenomsEtudiant.getText().trim(),
                        matriculeEtudiant.getText().trim(),
                        dateNaissanceEtudiant.getValue() != null ? dateNaissanceEtudiant.getValue().toString() : "",
                        lieuxNaissanceEtudiant.getText().trim(),
                        genreEtudiant.getValue() != null ? genreEtudiant.getValue().charAt(0) : ' ',
                        contactEtudiant.getText().trim(),
                        emailEtudiant.getText().trim(),
                        nationnaliteEtudiant.getText().trim()
                );

                if (filierEtudiant.getValue() != null) {
                    nouvelEtudiant.setFiliere(filierEtudiant.getValue());
                }

                nouvelEtudiant.create("1");

                if (successLabel != null) {
                    successLabel.setText("Étudiant créé avec succès!");
                }

                clearForm();

                // Actualiser le tableau si présent
                if (tableEtudiants != null) {
                    refreshData(null);
                }

            } else {
                if (erreurLabel != null) {
                    erreurLabel.setText("Veuillez remplir tous les champs obligatoires");
                }
            }
        } catch (Exception e) {
            if (erreurLabel != null) {
                erreurLabel.setText("Erreur lors de la création: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    private boolean isFormValid() {
        return nomEtudiant != null && !nomEtudiant.getText().trim().isEmpty() &&
                prenomsEtudiant != null && !prenomsEtudiant.getText().trim().isEmpty() &&
                matriculeEtudiant != null && !matriculeEtudiant.getText().trim().isEmpty();
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
    }

    public void cancelCreatEtudiant(ActionEvent event) throws IOException {
        SceneUtils.closeModal(event);
    }

    public void filterByFiliere(ActionEvent event) {
        if (filterFiliere == null || tableEtudiants == null) return;

        String filiere = filterFiliere.getValue();
        if (filiere.equals("Toutes les filières")) {
            tableEtudiants.setItems(etudiantsList);
        } else {
            ObservableList<Etudiant> filtered = etudiantsList.filtered(
                    etudiant -> filiere.equalsIgnoreCase(etudiant.getFiliere())
            );
            tableEtudiants.setItems(filtered);
        }
    }

    public void refreshData(ActionEvent event) {
        if (tableEtudiants == null) return;

        try {
            etudiantsList.clear();
            etudiantsList.addAll(Etudiant.getAll());
            tableEtudiants.setItems(etudiantsList);
            if (successLabel != null) {
                successLabel.setText("Données actualisées");
            }
        } catch (Exception e) {
            if (erreurLabel != null) {
                erreurLabel.setText("Erreur de chargement : " + e.getMessage());
            }
        }
    }

    public void exportData(ActionEvent event) {
        if (tableEtudiants == null) return;

        try {
            FileWriter writer = new FileWriter("etudiants_export.csv");
            writer.write("Nom,Prenoms,Matricule,Filiere\n");
            for (Etudiant etudiant : tableEtudiants.getItems()) {
                writer.write(String.format("%s,%s,%s,%s\n",
                        etudiant.getNom(),
                        etudiant.getPrenom(),
                        etudiant.getMatricule()
//                        etudiant.getFiliere()
                ));
            }
            writer.close();
            if (successLabel != null) {
                successLabel.setText("Export réussi !");
            }
        } catch (IOException e) {
            if (erreurLabel != null) {
                erreurLabel.setText("Erreur export : " + e.getMessage());
            }
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
        tableEtudiants.setItems(etudiantsList);
    }
}