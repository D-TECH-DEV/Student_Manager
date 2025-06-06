package com.pigier.pigieretudiant.models;

import com.pigier.pigieretudiant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Filiere {

    public static List<String> getListe() {
        List<String> filieres = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT code FROM filieres");
            while (rs.next()) {
                filieres.add(rs.getString("code"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return filieres;
    }

}