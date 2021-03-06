package com.controledecomandas.controllers;

import com.controledecomandas.database.PostgresConnection;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportController implements Initializable {

    @FXML
    private ToggleGroup period;

    @FXML
    private DatePicker dpInit;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private Label labelError;




    @FXML
    public void handleButtonGenerate(ActionEvent event) throws JRException, SQLException, ParseException {
        RadioButton periodSelected = (RadioButton) period.getSelectedToggle();

        if(!dpInit.getEditor().getText().equals("") && (!periodSelected.getText().equals("personalizado") || !dpEnd.getEditor().getText().equals(""))) {
            labelError.setVisible(false);
            Connection conn = PostgresConnection.createConnection();
            HashMap parameters = new HashMap();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


            Date periodEnd = dateFormat.parse(
                    periodSelected.getText().equals("personalizado") ?
                            dpEnd.getEditor().getText() :
                            dpInit.getEditor().getText()
            );

            Calendar c = Calendar.getInstance();
            c.setTime(periodEnd);
            c.add(Calendar.DATE, 1);
            periodEnd = c.getTime();

            parameters.put("period-init", dateFormat.parse(dpInit.getEditor().getText()));
            parameters.put("period-end", periodEnd);


            JasperPrint jp = JasperFillManager.fillReport("src/main/resources/report/report_mqb.jasper", parameters, conn);
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setVisible(true);
            viewer.show();
        } else {
            labelError.setVisible(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        period.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton periodSelected = (RadioButton) period.getSelectedToggle();
               boolean longPeriod = periodSelected.getText().equals("personalizado");
                    dpEnd.setVisible(longPeriod);
                    dpInit.setPromptText(longPeriod ? "De" : "Selecione o dia");
            }
        });
    }
}
