package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import javafx.application.Platform;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PatientBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.PatientBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.PaymentBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.TherapyProgramBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PaymentDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.PaymentTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PaymentsController implements Initializable {

    private boolean fromMainPage = false;

    public void setFromMainPage(boolean fromMainPage) {
        this.fromMainPage = fromMainPage;
    }

    public void configurePage() {
        if (fromMainPage) {
            searchButton.setVisible(false);
            deleteButton.setVisible(false);
            updateButton.setVisible(false);
        }
    }

    @FXML
    private TableColumn<PaymentTM, Double> amountCol;

    @FXML
    private TextField amountTxt;

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private TableColumn<PaymentTM, LocalDate> dateCol;

    @FXML
    private DatePicker dateTxt;


    @FXML
    private Button getInvoice;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<PaymentTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private Button patientSearchButton;

    @FXML
    private TableColumn<PaymentTM, String> paymentIdCol;

    @FXML
    private TextField paymentIdTxt;

    @FXML
    private ChoiceBox<String> paymentTypeChoice;

    @FXML
    private TableView<PaymentTM> paymentsTable;

    @FXML
    private TableColumn<PaymentTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private TextField programNameTxt;

    @FXML
    private Button programSearchButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableColumn<PaymentTM, String> sessionIdCol;

    @FXML
    private HBox sessionIdPart;

    @FXML
    private TextField sessionIdTxt;

    @FXML
    private Button updateButton;

    private final PatientBO patientBO = new PatientBOImpl();
    private final TherapyProgramBO programBO = new TherapyProgramBOImpl();
    PaymentBO paymentBO = new PaymentBOImpl();

    public void initialize(URL location, ResourceBundle resources) {


        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgramId"));
        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("therapySessionId"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        refreshPage();

        paymentTypeChoice.setItems(FXCollections.observableArrayList("Program Register", "Session Payment"));
        paymentTypeChoice.setValue("Program Register");
        sessionIdPart.setVisible(false);
        paymentTypeChoice.setOnAction(e -> toggleSessionField());

    }

    private void toggleSessionField() {
        sessionIdPart.setVisible(!"Program Register".equals(paymentTypeChoice.getValue()));
    }

    private void refreshTable() {
        ArrayList<PaymentDto> paymentList = paymentBO.getAllPayments();
        ObservableList<PaymentTM> payments = FXCollections.observableArrayList();

        for (PaymentDto dto : paymentList) {
            String session = null;
            if (dto.getTherapySession() != null) {
                session = dto.getTherapySession().getSession_id();
            }
            payments.add(new PaymentTM(
                    dto.getPaymentId(),
                    dto.getPatient().getPatient_id(),
                    dto.getTherapyProgram().getProgram_id(),
                    session,
                    dto.getAmount(),
                    dto.getPaymentDate()
            ));
        }

        paymentsTable.setItems(payments);
    }

    private void clearFields() {
        paymentIdTxt.clear();
        patientIdTxt.clear();
        patientNameTxt.clear();
        programIdTxt.clear();
        programNameTxt.clear();
        sessionIdTxt.clear();
        amountTxt.clear();
        dateTxt.setValue(null);
    }


    @FXML
    void delete(ActionEvent event) {
        String id = paymentIdTxt.getText();
        if (paymentBO.deletePayment(id)) {
            showAlert("Success", "Payment deleted", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Delete failed", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void getInvoiceOnAction(ActionEvent event) {
        PaymentTM selectedPayment = paymentsTable.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            showAlert("Error", "Please select a payment to generate the invoice.", Alert.AlertType.WARNING);
            return;
        }

        new Thread(() -> {
            try {
                java.io.InputStream reportStream = getClass().getResourceAsStream("/report/paymentInvoice.jrxml");
                if (reportStream == null) {
                    throw new RuntimeException("Could not find report template at /report/paymentInvoice.jrxml");
                }

                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("PaymentId", selectedPayment.getPaymentId());

                java.sql.Connection connection = null;
                JasperPrint jasperPrint = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    connection = java.sql.DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/smhtc_db",
                        "root",
                        "mysql"
                    );

                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
                } catch (ClassNotFoundException | java.sql.SQLException e) {
                    throw new RuntimeException("Database connection error: " + e.getMessage(), e);
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (java.sql.SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                java.io.File outputDir = new java.io.File("src/main/resources/report/output");
                if (!outputDir.exists()) {
                    boolean dirCreated = outputDir.mkdirs();
                    if (!dirCreated) {
                        throw new RuntimeException("Failed to create output directory at: " + outputDir.getAbsolutePath());
                    }
                }

                if (!outputDir.canWrite()) {
                    throw new RuntimeException("Output directory is not writable: " + outputDir.getAbsolutePath());
                }

                String outputPath = "src/main/resources/report/output/payment_invoice_" + selectedPayment.getPaymentId() + ".pdf";
                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                java.io.File outputFile = new java.io.File(outputPath);
                String absolutePath = outputFile.getAbsolutePath();

                Platform.runLater(() -> showAlert("Success", "Invoice generated successfully at: " + absolutePath, Alert.AlertType.INFORMATION));
            } catch (Exception e) {
                e.printStackTrace();
                String errorMessage = "Failed to generate invoice: " + e.getMessage();

                if (e instanceof java.io.FileNotFoundException) {
                    errorMessage = "Report template file not found: " + e.getMessage();
                } else if (e instanceof net.sf.jasperreports.engine.JRException) {
                    errorMessage = "JasperReports error: " + e.getMessage();
                } else if (e instanceof java.sql.SQLException) {
                    errorMessage = "Database error: " + e.getMessage();
                } else if (e instanceof java.io.IOException) {
                    errorMessage = "I/O error: " + e.getMessage();
                }

                final String finalErrorMessage = errorMessage;
                Platform.runLater(() -> showAlert("Error", finalErrorMessage, Alert.AlertType.ERROR));
            }
        }).start();
    }

    @FXML
    void save(ActionEvent event) {
        PaymentDto dto = paymentBO.constructPaymentDto(
                paymentIdTxt.getText(),
                patientIdTxt.getText(),
                programIdTxt.getText(),
                sessionIdTxt.getText(),
                new BigDecimal(amountTxt.getText()),
                dateTxt.getValue()
        );

        if (paymentBO.savePayment(dto)) {
            showAlert("Success", "Payment saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save payment", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String query = searchTxt.getText();
        if (query.isEmpty()) {
            showAlert("Error", "Please enter a search term", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }
        ArrayList<PaymentDto> results = paymentBO.searchByPatientName(query);

        ObservableList<PaymentTM> payments = FXCollections.observableArrayList();

        for (PaymentDto dto : results) {
            String sessionId = null;
            if (dto.getTherapySession() != null) {
                sessionId = dto.getTherapySession().getSession_id();
            }

            payments.add(new PaymentTM(
                    true,
                    dto.getPaymentId(),
                    dto.getPatient().getPatient_id(),
                    dto.getTherapyProgram().getProgram_id(),
                    sessionId,
                    dto.getAmount(),
                    dto.getPaymentDate()
            ));
        }

        paymentsTable.setItems(payments);
    }

    private void refreshPage() {
        clearFields();
        refreshTable();
        paymentIdTxt.setText(paymentBO.getNextPaymentPK());
    }

    @FXML
    void searchPatient(ActionEvent event) {
        String name = patientNameTxt.getText().trim();
        ArrayList<PatientDto> patients = patientBO.findByPatientName(name);

        if (patients.isEmpty()) {
            showAlert("Not Found", "Patient not found", Alert.AlertType.WARNING);
            return;
        }

        PatientDto patient = patients.getFirst();

        patientIdTxt.setText(patient.getPatientId());
        patientNameTxt.setText(patient.getName());
    }

    @FXML
    void searchProgram(ActionEvent event) {
        String name = programNameTxt.getText().trim();
        ArrayList<TherapyProgramDto> programs = programBO.findTherapyProgramByName(name);

        if (programs.isEmpty()) {
            showAlert("Not Found", "Program not found", Alert.AlertType.WARNING);
            return;
        }

        TherapyProgramDto program = programs.getFirst();

        programIdTxt.setText(program.getProgramId());
        programNameTxt.setText(program.getName());
    }

    @FXML
    void update(ActionEvent event) {
        PaymentDto dto = paymentBO.constructPaymentDto(
                paymentIdTxt.getText(),
                patientIdTxt.getText(),
                programIdTxt.getText(),
                sessionIdTxt.getText(),
                new BigDecimal(amountTxt.getText()),
                dateTxt.getValue()
        );

        if (paymentBO.updatePayment(dto)) {
            showAlert("Success", "Payment updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update payment", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void tableClick(MouseEvent event) {
        PaymentTM selected = paymentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            paymentIdTxt.setText(selected.getPaymentId());
            patientIdTxt.setText(selected.getPatientId());
            patientNameTxt.setText(patientBO.findPatientByID(selected.getPatientId()).getName());
            programIdTxt.setText(selected.getTherapyProgramId());
            programNameTxt.setText(programBO.findTherapyProgramByID(selected.getTherapyProgramId()).getName());
            sessionIdTxt.setText(selected.getTherapySessionId());
            amountTxt.setText(String.valueOf(selected.getAmount()));
            dateTxt.setValue(selected.getPaymentDate());
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
