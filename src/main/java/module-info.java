module com.burgetjoel {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
   requires transitive javafx.graphics;

    opens com.burgetjoel to javafx.fxml;
    exports com.burgetjoel;
}
