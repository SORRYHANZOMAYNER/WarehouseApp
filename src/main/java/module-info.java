module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.ws.rs;
    requires jersey.media.json.jackson;
    requires java.desktop;

    exports com.example.demo1.module1.modules;
    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    exports com.example.demo1.storagePlaces;
    opens com.example.demo1.storagePlaces to javafx.fxml;
}