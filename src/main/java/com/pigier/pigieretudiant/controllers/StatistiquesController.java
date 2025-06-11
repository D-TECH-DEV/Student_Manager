package com.pigier.pigieretudiant.controllers;

import com.pigier.pigieretudiant.models.Etudiant;
import com.pigier.pigieretudiant.models.Filiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiquesController {

    @FXML private Label totalEtudiants;
    @FXML private Label totalHommes;
    @FXML private Label totalFemmes;
    @FXML private Label totalL1;
    @FXML private Label totalL2;
    @FXML private Label totalL3;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;

    public void initialize() {
        try {
            loadStatistics();
            loadCharts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStatistics() throws SQLException, ClassNotFoundException {
        List<Etudiant> etudiants = Etudiant.getAll();
        
        int total = etudiants.size();
        int hommes = (int) etudiants.stream().filter(e -> "M".equals(e.getGenre())).count();
        int femmes = (int) etudiants.stream().filter(e -> "F".equals(e.getGenre())).count();
        
        int l1 = (int) etudiants.stream().filter(e -> "L1".equals(e.getNiveau())).count();
        int l2 = (int) etudiants.stream().filter(e -> "L2".equals(e.getNiveau())).count();
        int l3 = (int) etudiants.stream().filter(e -> "L3".equals(e.getNiveau())).count();

        if (totalEtudiants != null) totalEtudiants.setText(String.valueOf(total));
        if (totalHommes != null) totalHommes.setText(String.valueOf(hommes));
        if (totalFemmes != null) totalFemmes.setText(String.valueOf(femmes));
        if (totalL1 != null) totalL1.setText(String.valueOf(l1));
        if (totalL2 != null) totalL2.setText(String.valueOf(l2));
        if (totalL3 != null) totalL3.setText(String.valueOf(l3));
    }

    private void loadCharts() throws SQLException, ClassNotFoundException {
        List<Etudiant> etudiants = Etudiant.getAll();
        
        // Données pour le graphique en barres (par filière et genre)
        if (barChart != null) {
            Map<String, Integer> hommesParFiliere = new HashMap<>();
            Map<String, Integer> femmesParFiliere = new HashMap<>();
            
            for (Etudiant etudiant : etudiants) {
                String filiere = etudiant.getFiliere() != null ? etudiant.getFiliere() : "Non définie";
                
                if ("M".equals(etudiant.getGenre())) {
                    hommesParFiliere.put(filiere, hommesParFiliere.getOrDefault(filiere, 0) + 1);
                } else if ("F".equals(etudiant.getGenre())) {
                    femmesParFiliere.put(filiere, femmesParFiliere.getOrDefault(filiere, 0) + 1);
                }
            }
            
            XYChart.Series<String, Number> seriesHommes = new XYChart.Series<>();
            seriesHommes.setName("Hommes");
            
            XYChart.Series<String, Number> seriesFemmes = new XYChart.Series<>();
            seriesFemmes.setName("Femmes");
            
            for (String filiere : hommesParFiliere.keySet()) {
                seriesHommes.getData().add(new XYChart.Data<>(filiere, hommesParFiliere.get(filiere)));
            }
            
            for (String filiere : femmesParFiliere.keySet()) {
                seriesFemmes.getData().add(new XYChart.Data<>(filiere, femmesParFiliere.get(filiere)));
            }
            
            barChart.getData().addAll(seriesHommes, seriesFemmes);
        }
        
        // Données pour le graphique circulaire (répartition par filière)
        if (pieChart != null) {
            Map<String, Integer> repartitionFiliere = new HashMap<>();
            
            for (Etudiant etudiant : etudiants) {
                String filiere = etudiant.getFiliere() != null ? etudiant.getFiliere() : "Non définie";
                repartitionFiliere.put(filiere, repartitionFiliere.getOrDefault(filiere, 0) + 1);
            }
            
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : repartitionFiliere.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            
            pieChart.setData(pieChartData);
        }
    }
}