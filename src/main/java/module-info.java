module ds.esb.expertsystembuilder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires org.json;
    requires com.google.gson;

    opens ds.esb.expertsystembuilder to javafx.fxml;
    opens ds.esb.expertsystembuilder.controllers to javafx.fxml, javafx.base;
    opens ds.esb.expertsystembuilder.classes to javafx.fxml, javafx.base;
    opens ds.esb.expertsystembuilder.classes.bean to javafx.fxml, javafx.base;
    exports ds.esb.expertsystembuilder;
    exports ds.esb.expertsystembuilder.controllers;
}