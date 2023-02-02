module com.kodilla.seabattle_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;

    opens com.kodilla.seabattle_javafx to javafx.fxml;
    exports com.kodilla.seabattle_javafx;
}