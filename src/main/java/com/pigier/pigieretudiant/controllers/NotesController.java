package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.models.Matiere;
import com.pigier.pigieretudiant.models.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class NotesController {

    @FXML private ComboBox<Etudiant> etudiantComboBox;
    @FXML private ComboBox<Matiere> matiereComboBox;
    @FXML private TextField noteField;
    @FXML private TableView<Note> notesTable;
    @FXML private TableColumn<Note, String> colEtudiant;
    @FXML private TableColumn<Note, String> colMatiere;
    @FXML private TableColumn<Note, Double> colNote;
    @FXML private TableColumn<Note, Integer> colCoef;
    @FXML private TableColumn<Note, Void> colActions;
    @FXML private Label messageLabel;
    @FXML private Label moyenneLabel;

    private ObservableList<Note> notesList = FXCollections.observableArrayList();

    public void initialize() {
        try {
            initializeComboBoxes();
            initializeTable();
            loadNotes();
        } catch (Exception e) {
            showMessage("Erreur d'initialisation: " + e.getMessage(), true);
        }
    }

    private void initializeComboBoxes() throws SQLException, ClassNotFoundException {
        // Charger les étudiants
        List<Etudiant> etudiants = Etudiant.getAll();
        etudiantComboBox.setItems(FXCollections.observableArrayList(etudiants));

        // Charger les matières
        List<Matiere> matieres = Matiere.getAll();
        matiereComboBox.setItems(FXCollections.observableArrayList(matieres));

        // Listener pour calculer la moyenne quand un étudiant est sélectionné
        etudiantComboBox.setOnAction(e -> {
            if (etudiantComboBox.getValue() != null) {
                try {
                    double moyenne = Note.calculateMoyenne(etudiantComboBox.getValue().getId());
                    moyenneLabel.setText(String.format("Moyenne: %.2f/20", moyenne));
                } catch (Exception ex) {
                    moyenneLabel.setText("Moyenne: N/A");
                }
            }
        });
    }

    private void initializeTable() {
        colEtudiant.setCellValueFactory(new PropertyValueFactory<>("etudiantNom"));
        colMatiere.setCellValueFactory(new PropertyValueFactory<>("matiereNom"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colCoef.setCellValueFactory(new PropertyValueFactory<>("matiereCoef"));

        // Ajouter les boutons d'action
        addActionButtonsToTable();
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<Note, Void>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");

            {
                editBtn.setStyle("-fx-background-color: #4361ee; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                
                editBtn.setOnAction(event -> {
                    Note note = getTableView().getItems().get(getIndex());
                    editNote(note);
                });
                
                deleteBtn.setOnAction(event -> {
                    Note note = getTableView().getItems().get(getIndex());
                    deleteNote(note);
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
    private void ajouterNote(ActionEvent event) {
        try {
            if (isFormValid()) {
                Note note = new Note(
                    etudiantComboBox.getValue().getId(),
                    matiereComboBox.getValue().getId(),
                    Double.parseDouble(noteField.getText().trim())
                );

                note.create();
                showMessage("Note ajoutée avec succès!", false);
                clearForm();
                loadNotes();
                updateMoyenne();
            } else {
                showMessage("Veuillez remplir tous les champs correctement", true);
            }
        } catch (Exception e) {
            showMessage("Erreur lors de l'ajout: " + e.getMessage(), true);
        }
    }

    private void editNote(Note note) {
        // Charger les données dans le formulaire
        etudiantComboBox.setValue(etudiantComboBox.getItems().stream()
            .filter(e -> e.getId() == note.getEtudiantId())
            .findFirst().orElse(null));
        
        matiereComboBox.setValue(matiereComboBox.getItems().stream()
            .filter(m -> m.getId() == note.getMatiereId())
            .findFirst().orElse(null));
        
        noteField.setText(String.valueOf(note.getNote()));
    }

    private void deleteNote(Note note) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la note");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette note ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    note.delete();
                    showMessage("Note supprimée avec succès!", false);
                    loadNotes();
                    updateMoyenne();
                } catch (Exception e) {
                    showMessage("Erreur lors de la suppression: " + e.getMessage(), true);
                }
            }
        });
    }

    @FXML
    private void filtrerParEtudiant(ActionEvent event) {
        if (etudiantComboBox.getValue() != null) {
            try {
                List<Note> notes = Note.getByEtudiant(etudiantComboBox.getValue().getId());
                notesList.clear();
                notesList.addAll(notes);
                notesTable.setItems(notesList);
                updateMoyenne();
            } catch (Exception e) {
                showMessage("Erreur de filtrage: " + e.getMessage(), true);
            }
        }
    }

    private void loadNotes() {
        try {
            notesList.clear();
            notesList.addAll(Note.getAll());
            notesTable.setItems(notesList);
        } catch (Exception e) {
            showMessage("Erreur de chargement: " + e.getMessage(), true);
        }
    }

    private void updateMoyenne() {
        if (etudiantComboBox.getValue() != null) {
            try {
                double moyenne = Note.calculateMoyenne(etudiantComboBox.getValue().getId());
                moyenneLabel.setText(String.format("Moyenne: %.2f/20", moyenne));
            } catch (Exception e) {
                moyenneLabel.setText("Moyenne: N/A");
            }
        }
    }

    private boolean isFormValid() {
        try {
            double noteValue = Double.parseDouble(noteField.getText().trim());
            return etudiantComboBox.getValue() != null &&
                   matiereComboBox.getValue() != null &&
                   noteValue >= 0 && noteValue <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearForm() {
        etudiantComboBox.setValue(null);
        matiereComboBox.setValue(null);
        noteField.clear();
        moyenneLabel.setText("Moyenne: N/A");
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }
}