module com.controledecomandas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;

    opens com.controledecomandas.controllers to javafx.fxml;
    opens com.controledecomandas.controllers.Dialogs to javafx.fxml;
    opens com.controledecomandas.textFieldsValidators to javafx.fxml;
    opens com.controledecomandas.models to javafx.base;
    exports com.controledecomandas;
}