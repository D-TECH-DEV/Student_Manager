package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Document;
import com.pigier.pigieretudiant.models.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class DocumentController {

    @FXML private ComboBox<Etudiant> etudiantComboBox;
    @FXML private ComboBox<String> typeDocumentComboBox;
    @FXML private TextField nomDocumentField;
    @FXML private Label cheminFichierLabel;
    @FXML private TableView<Document> documentsTable;
    @FXML private TableColumn<Document, String> colNomDocument;
    @FXML private TableColumn<Document, String> colTypeDocument;
    @FXML private TableColumn<Document, String> colEtudiant;
    @FXML private TableColumn<Document, String> colDateAjout;
    @FXML private Label messageLabel;

    private ObservableList<Document> documentsList = FXCollections.observableArrayList();
    private File selectedFile;

    public void initialize() {
        try {
            initializeComboBoxes();
            initializeTable();
            loadDocuments();
        } catch (Exception e) {
            showMessage("Erreur d'initialisation: " + e.getMessage(), true);
        }
    }

    private void initializeComboBoxes() throws SQLException, ClassNotFoundException {
        // Charger les étudiants
        List<Etudiant> etudiants = Etudiant.getAll();
        etudiantComboBox.setItems(FXCollections.observableArrayList(etudiants));

        // Types de documents
        typeDocumentComboBox.setItems(FXCollections.observableArrayList(
            "Acte de naissance",
            "Diplôme BAC",
            "Relevé de notes",
            "Photo d'identité",
            "Certificat médical",
            "Autre"
        ));
    }

    private void initializeTable() {
        colNomDocument.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colTypeDocument.setCellValueFactory(new PropertyValueFactory<>("type"));
        colEtudiant.setCellValueFactory(new PropertyValueFactory<>("etudiantNom"));
        colDateAjout.setCellValueFactory(new PropertyValueFactory<>("dateAjout"));
    }

    @FXML
    private void choisirFichier(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un document");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"),
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"),
            new FileChooser.ExtensionFilter("PDF", "*.pdf"),
            new FileChooser.ExtensionFilter("Documents Word", "*.doc", "*.docx")
        );

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            cheminFichierLabel.setText(selectedFile.getName());
        }
    }

    @FXML
    private void ajouterDocument(ActionEvent event) {
        try {
            if (isFormValid()) {
                Document document = new Document(
                    nomDocumentField.getText().trim(),
                    typeDocumentComboBox.getValue(),
                    etudiantComboBox.getValue().getId(),
                    selectedFile.getAbsolutePath()
                );

                document.create();
                showMessage("Document ajouté avec succès!", false);
                clearForm();
                loadDocuments();
            } else {
                showMessage("Veuillez remplir tous les champs et sélectionner un fichier", true);
            }
        } catch (Exception e) {
            showMessage("Erreur lors de l'ajout: " + e.getMessage(), true);
        }
    }

    @FXML
    private void supprimerDocument(ActionEvent event) {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            showMessage("Veuillez sélectionner un document à supprimer", true);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le document");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce document ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    selectedDocument.delete();
                    showMessage("Document supprimé avec succès!", false);
                    loadDocuments();
                } catch (Exception e) {
                    showMessage("Erreur lors de la suppression: " + e.getMessage(), true);
                }
            }
        });
    }

    @FXML
    private void ouvrirDocument(ActionEvent event) {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            showMessage("Veuillez sélectionner un document à ouvrir", true);
            return;
        }

        try {
            File file = new File(selectedDocument.getCheminFichier());
            if (file.exists()) {
                java.awt.Desktop.getDesktop().open(file);
            } else {
                showMessage("Le fichier n'existe plus à l'emplacement spécifié", true);
            }
        } catch (Exception e) {
            showMessage("Erreur lors de l'ouverture: " + e.getMessage(), true);
        }
    }

    private void loadDocuments() {
        try {
            documentsList.clear();
            documentsList.addAll(Document.getAll());
            documentsTable.setItems(documentsList);
        } catch (Exception e) {
            showMessage("Erreur de chargement des documents: " + e.getMessage(), true);
        }
    }

    private boolean isFormValid() {
        return nomDocumentField.getText() != null && !nomDocumentField.getText().trim().isEmpty() &&
               typeDocumentComboBox.getValue() != null &&
               etudiantComboBox.getValue() != null &&
               selectedFile != null;
    }

    private void clearForm() {
        nomDocumentField.clear();
        typeDocumentComboBox.setValue(null);
        etudiantComboBox.setValue(null);
        cheminFichierLabel.setText("Aucun fichier sélectionné");
        selectedFile = null;
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }
}