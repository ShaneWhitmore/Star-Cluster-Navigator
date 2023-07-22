module com.example.spacenavigator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.spacenavigator to javafx.fxml;
    exports com.example.spacenavigator;
}