package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.models.Note;
import com.pigier.pigieretudiant.utils.ExportUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class RapportsController {

    @FXML private ComboBox<String> typeRapportComboBox;
    @FXML private ComboBox<String> filiereFilterComboBox;
    @FXML private ComboBox<String> niveauFilterComboBox;
    @FXML private TableView<RapportEtudiant> rapportTable;
    @FXML private TableColumn<RapportEtudiant, String> colNom;
    @FXML private TableColumn<RapportEtudiant, String> colMatricule;
    @FXML private TableColumn<RapportEtudiant, String> colFiliere;
    @FXML private TableColumn<RapportEtudiant, String> colNiveau;
    @FXML private TableColumn<RapportEtudiant, Double> colMoyenne;
    @FXML private TableColumn<RapportEtudiant, String> colMention;
    @FXML private Label totalEtudiantsLabel;
    @FXML private Label moyenneGeneraleLabel;

    private ObservableList<RapportEtudiant> rapportList = FXCollections.observableArrayList();

    public void initialize() {
        try {
            initializeComboBoxes();
            initializeTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeComboBoxes() {
        typeRapportComboBox.setItems(FXCollections.observableArrayList(
            "Bulletin de notes",
            "Relevé de notes par filière",
            "Classement général",
            "Étudiants en difficulté",
            "Tableau d'honneur"
        ));

        filiereFilterComboBox.setItems(FXCollections.observableArrayList(
            "Toutes les filières", "RGL", "AD", "CF", "TH", "CDM", "MA"
        ));
        filiereFilterComboBox.setValue("Toutes les filières");

        niveauFilterComboBox.setItems(FXCollections.observableArrayList(
            "Tous les niveaux", "L1", "L2", "L3", "M1", "M2"
        ));
        niveauFilterComboBox.setValue("Tous les niveaux");
    }

    private void initializeTable() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        colMoyenne.setCellValueFactory(new PropertyValueFactory<>("moyenne"));
        colMention.setCellValueFactory(new PropertyValueFactory<>("mention"));

        // Colorer les lignes selon la mention
        rapportTable.setRowFactory(tv -> new TableRow<RapportEtudiant>() {
            @Override
            protected void updateItem(RapportEtudiant item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getMention()) {
                        case "Très Bien":
                            setStyle("-fx-background-color: #d4edda;");
                            break;
                        case "Bien":
                            setStyle("-fx-background-color: #d1ecf1;");
                            break;
                        case "Assez Bien":
                            setStyle("-fx-background-color: #fff3cd;");
                            break;
                        case "Passable":
                            setStyle("-fx-background-color: #f8d7da;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    @FXML
    private void genererRapport(ActionEvent event) {
        try {
            String typeRapport = typeRapportComboBox.getValue();
            if (typeRapport == null) {
                showAlert("Veuillez sélectionner un type de rapport");
                return;
            }

            List<Etudiant> etudiants = Etudiant.getAll();
            rapportList.clear();

            for (Etudiant etudiant : etudiants) {
                // Appliquer les filtres
                if (!filiereFilterComboBox.getValue().equals("Toutes les filières") &&
                    !etudiant.getFiliere().equals(filiereFilterComboBox.getValue())) {
                    continue;
                }

                if (!niveauFilterComboBox.getValue().equals("Tous les niveaux") &&
                    !etudiant.getNiveau().equals(niveauFilterComboBox.getValue())) {
                    continue;
                }

                double moyenne = Note.calculateMoyenne(etudiant.getId());
                String mention = calculateMention(moyenne);

                // Appliquer le filtre selon le type de rapport
                boolean includeStudent = true;
                switch (typeRapport) {
                    case "Étudiants en difficulté":
                        includeStudent = moyenne < 10;
                        break;
                    case "Tableau d'honneur":
                        includeStudent = moyenne >= 14;
                        break;
                }

                if (includeStudent) {
                    RapportEtudiant rapport = new RapportEtudiant(
                        etudiant.getNom() + " " + etudiant.getPrenom(),
                        etudiant.getMatricule(),
                        etudiant.getFiliere(),
                        etudiant.getNiveau(),
                        moyenne,
                        mention
                    );
                    rapportList.add(rapport);
                }
            }

            // Trier selon le type de rapport
            if (typeRapport.equals("Classement général") || typeRapport.equals("Tableau d'honneur")) {
                rapportList.sort((a, b) -> Double.compare(b.getMoyenne(), a.getMoyenne()));
            }

            rapportTable.setItems(rapportList);
            updateStatistics();

        } catch (Exception e) {
            showAlert("Erreur lors de la génération du rapport: " + e.getMessage());
        }
    }

    @FXML
    private void exporterRapport(ActionEvent event) {
        try {
            if (rapportList.isEmpty()) {
                showAlert("Aucun rapport à exporter. Veuillez d'abord générer un rapport.");
                return;
            }

            ExportUtils.exportRapportToCSV(rapportList, rapportTable.getScene().getWindow());
            showAlert("Rapport exporté avec succès!");

        } catch (Exception e) {
            showAlert("Erreur lors de l'export: " + e.getMessage());
        }
    }

    private void updateStatistics() {
        totalEtudiantsLabel.setText("Total étudiants: " + rapportList.size());
        
        if (!rapportList.isEmpty()) {
            double moyenneGenerale = rapportList.stream()
                .mapToDouble(RapportEtudiant::getMoyenne)
                .average()
                .orElse(0.0);
            moyenneGeneraleLabel.setText(String.format("Moyenne générale: %.2f/20", moyenneGenerale));
        } else {
            moyenneGeneraleLabel.setText("Moyenne générale: N/A");
        }
    }

    private String calculateMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        else if (moyenne >= 14) return "Bien";
        else if (moyenne >= 12) return "Assez Bien";
        else if (moyenne >= 10) return "Passable";
        else return "Insuffisant";
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Classe interne pour représenter un rapport d'étudiant
    public static class RapportEtudiant {
        private String nomComplet;
        private String matricule;
        private String filiere;
        private String niveau;
        private double moyenne;
        private String mention;

        public RapportEtudiant(String nomComplet, String matricule, String filiere, String niveau, double moyenne, String mention) {
            this.nomComplet = nomComplet;
            this.matricule = matricule;
            this.filiere = filiere;
            this.niveau = niveau;
            this.moyenne = moyenne;
            this.mention = mention;
        }

        // Getters
        public String getNomComplet() { return nomComplet; }
        public String getMatricule() { return matricule; }
        public String getFiliere() { return filiere; }
        public String getNiveau() { return niveau; }
        public double getMoyenne() { return moyenne; }
        public String getMention() { return mention; }
    }
}