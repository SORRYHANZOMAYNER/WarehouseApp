module com.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.ws.rs;
    requires jersey.media.json.jackson;
    requires java.desktop;

    exports com.demo1.module1.modules;
    opens com.demo1 to javafx.fxml;
    exports com.demo1;
    exports com.demo1.storagePlaces;
    opens com.demo1.storagePlaces to javafx.fxml;
}