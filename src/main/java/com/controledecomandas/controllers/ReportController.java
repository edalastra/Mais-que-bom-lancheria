package com.controledecomandas.controllers;

import com.controledecomandas.database.PostgresConnection;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportController  {

    @FXML
    private ToggleGroup period;

    @FXML
    private DatePicker dpInit;

    @FXML
    private DatePicker dpEnd;


    @FXML
    public void handleButtonGenerate(ActionEvent event) throws JRException, SQLException, ParseException {
        Connection conn = PostgresConnection.createConnection();
        System.out.println(dpInit.getEditor().getText());

        HashMap parameters = new HashMap();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        parameters.put("period-init",dateFormat.parse("29/03/2021"));
        parameters.put("period-end", dateFormat.parse("29/03/2021"));

        JasperPrint jp = JasperFillManager.fillReport("src/main/resources/report/report_mqb.jasper", parameters, conn);
        JasperViewer viewer = new JasperViewer(jp, false);
        viewer.setVisible(true);
        viewer.show();

    }

}
