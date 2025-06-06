module com.pigier.pigieretudiant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires annotations;

    exports com.pigier.pigieretudiant;

    opens com.pigier.pigieretudiant to javafx.fxml;
    opens com.pigier.pigieretudiant.controllers to javafx.fxml;
    opens com.pigier.pigieretudiant.models to javafx.fxml;
    opens com.pigier.pigieretudiant.utils to javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    exports com.pigier.pigieretudiant.utils;
    exports com.pigier.pigieretudiant.controllers;
    exports com.pigier.pigieretudiant.models;
}
