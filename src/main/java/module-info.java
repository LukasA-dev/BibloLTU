module com.example.bibloltu {
    requires javafx.controls;
    requires javafx.fxml;
            
                    requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.bibloltu to javafx.fxml;
    exports com.example.bibloltu;
}