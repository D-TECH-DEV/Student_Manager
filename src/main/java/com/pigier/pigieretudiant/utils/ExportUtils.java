package com.pigier.pigieretudiant.utils;

import com.pigier.pigieretudiant.models.Etudiant;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportUtils {

    public static void exportEtudiantsToCSV(List<Etudiant> etudiants, Window ownerWindow) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les étudiants");
        fileChooser.setInitialFileName("etudiants_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        java.io.File file = fileChooser.showSaveDialog(ownerWindow);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // En-têtes
                writer.write("Nom,Prénoms,Matricule,Date de naissance,Lieu de naissance,Genre,Téléphone,Email,Nationalité,Filière,Niveau\n");
                
                // Données
                for (Etudiant etudiant : etudiants) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        escapeCSV(etudiant.getNom()),
                        escapeCSV(etudiant.getPrenom()),
                        escapeCSV(etudiant.getMatricule()),
                        escapeCSV(etudiant.getDateNaissance()),
                        escapeCSV(etudiant.getLieuxNaissance()),
                        escapeCSV(etudiant.getGenre()),
                        escapeCSV(etudiant.getContact()),
                        escapeCSV(etudiant.getEmail()),
                        escapeCSV(etudiant.getNationalite()),
                        escapeCSV(etudiant.getFiliere()),
                        escapeCSV(etudiant.getNiveau())
                    ));
                }
            }
        }
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static void generateStatisticsReport(List<Etudiant> etudiants, Window ownerWindow) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter le rapport statistique");
        fileChooser.setInitialFileName("rapport_statistiques_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers texte", "*.txt")
        );

        java.io.File file = fileChooser.showSaveDialog(ownerWindow);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("RAPPORT STATISTIQUE - PIGIER\n");
                writer.write("================================\n\n");
                writer.write("Date de génération: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n");
                
                // Statistiques générales
                writer.write("STATISTIQUES GÉNÉRALES\n");
                writer.write("----------------------\n");
                writer.write("Total étudiants: " + etudiants.size() + "\n");
                
                long hommes = etudiants.stream().filter(e -> "M".equals(e.getGenre())).count();
                long femmes = etudiants.stream().filter(e -> "F".equals(e.getGenre())).count();
                
                writer.write("Hommes: " + hommes + " (" + String.format("%.1f", (hommes * 100.0 / etudiants.size())) + "%)\n");
                writer.write("Femmes: " + femmes + " (" + String.format("%.1f", (femmes * 100.0 / etudiants.size())) + "%)\n\n");
                
                // Répartition par niveau
                writer.write("RÉPARTITION PAR NIVEAU\n");
                writer.write("----------------------\n");
                long l1 = etudiants.stream().filter(e -> "L1".equals(e.getNiveau())).count();
                long l2 = etudiants.stream().filter(e -> "L2".equals(e.getNiveau())).count();
                long l3 = etudiants.stream().filter(e -> "L3".equals(e.getNiveau())).count();
                
                writer.write("L1: " + l1 + "\n");
                writer.write("L2: " + l2 + "\n");
                writer.write("L3: " + l3 + "\n\n");
                
                // Répartition par filière
                writer.write("RÉPARTITION PAR FILIÈRE\n");
                writer.write("-----------------------\n");
                etudiants.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                        e -> e.getFiliere() != null ? e.getFiliere() : "Non définie",
                        java.util.stream.Collectors.counting()))
                    .forEach((filiere, count) -> {
                        try {
                            writer.write(filiere + ": " + count + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
        }
    }
}