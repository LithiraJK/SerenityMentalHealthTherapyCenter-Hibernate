package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.BOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PatientsBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.PatientTM;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PatientsViewController implements Initializable {

    @FXML
    private ComboBox cmbGender;

    @FXML
    private TableView<PatientTM> tblPatient;

    @FXML
    private TableColumn<PatientTM, Integer> colPatientId;

    @FXML
    private TableColumn<PatientTM, String> colName;

    @FXML
    private TableColumn<PatientTM, String> colAddress;

    @FXML
    private TableColumn<PatientTM, Date> colDateOfBirth;

    @FXML
    private TableColumn<PatientTM, String> colEmail;

    @FXML
    private TableColumn<PatientTM, String> colPhone;

    @FXML
    private TableColumn<PatientTM, String> colTherapyProgram;

    @FXML
    private JFXTextField txtPatientName;

    @FXML
    private JFXTextField txtPatientAddress;

    @FXML
    private JFXTextField txtPatientPhone;

    @FXML
    private JFXTextField txtPatientEmail;

    @FXML
    private DatePicker datePickerDob;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private TextField txtSearch;

    String id;

    private ObservableList<PatientTM> patientList = FXCollections.observableArrayList();

    private final PatientsBO patientsBO = (PatientsBO) BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateGender();
        setTableListener();
        loadPatientData();
        setCellValueFactory();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private void loadPatientData() {
        patientList.clear();
        try {
            List<PatientDTO> patients = patientsBO.getAllPatients();
            for (PatientDTO dto : patients) {
                patientList.add(new PatientTM(
                        dto.getPatientId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getPhoneNumber(),
                        dto.getAddress(),
                        dto.getGender(),
                        dto.getDateOfBirth()
                ));
            }
            tblPatient.setItems(patientList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    private void setTableListener() {
        tblPatient.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setPatientData(newValue);
            }
        });
    }

    private void setPatientData(PatientTM patient) {
        txtPatientName.setText(patient.getName());
        txtPatientAddress.setText(patient.getAddress());
        txtPatientPhone.setText(patient.getPhoneNumber());
        txtPatientEmail.setText(patient.getEmail());
        cmbGender.setValue(patient.getGender());
        id = String.valueOf(patient.getPatientId());
        if (patient.getDateOfBirth() != null && !patient.getDateOfBirth().isEmpty()) {
            datePickerDob.setValue(LocalDate.parse(patient.getDateOfBirth()));
        }
        btnDelete.setDisable(false);
        btnUpdate.setDisable(false);
    }


    private void populateGender() {
        cmbGender.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    void btnDeletePatientOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean isDeleted = patientsBO.deletePatient(id);
        if (isDeleted) {
            new Alert(Alert.AlertType.INFORMATION, "Patient deleted successfully!").show();
            loadPatientData();
            clearFields();
            btnDelete.setDisable(true);
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to delete patient!").show();
        }
    }

    private void clearFields() {
        txtPatientName.clear();
        txtPatientAddress.clear();
        txtPatientPhone.clear();
        txtPatientEmail.clear();
        cmbGender.getSelectionModel().clearSelection();
        datePickerDob.setValue(null);
        id = null;
    }

    @FXML
    void btnSavePatientOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = txtPatientName.getText();
        String address = txtPatientAddress.getText();
        String phone = txtPatientPhone.getText();
        String email = txtPatientEmail.getText();
        String gender = cmbGender.getSelectionModel().getSelectedItem().toString();
        String dateOfBirth = datePickerDob.getValue().toString();

        PatientDTO patientDTO = new PatientDTO(name, address, gender, dateOfBirth, email, phone);

        boolean isSaved = patientsBO.savePatient(patientDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Patient saved successfully!").show();
            clearFields();
            loadPatientData();
        }else {
            new Alert(Alert.AlertType.ERROR, "Failed to save patient!").show();
        }
    }

    @FXML
    void btnUpdatePatientOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = txtPatientName.getText();
        String address = txtPatientAddress.getText();
        String phone = txtPatientPhone.getText();
        String email = txtPatientEmail.getText();
        String gender = cmbGender.getSelectionModel().getSelectedItem().toString();
        String dateOfBirth = datePickerDob.getValue().toString();

        PatientDTO patientDTO = new PatientDTO(id,name, address, gender, dateOfBirth, email, phone);

        boolean isUpdate = patientsBO.UpdatePatient(patientDTO);

        if (isUpdate){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Patient updated successfully!");
            clearFields();
            loadPatientData();
            btnUpdate.setDisable(true);
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Failed to update patient!");
        }
    }

    @FXML
    void txtSearchOnAction(KeyEvent event) {

    }
}
