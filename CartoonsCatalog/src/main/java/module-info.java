module it.unipi.cartoonscatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.google.gson;

    opens it.unipi.cartoonscatalog to javafx.fxml;
    exports it.unipi.cartoonscatalog;
}
