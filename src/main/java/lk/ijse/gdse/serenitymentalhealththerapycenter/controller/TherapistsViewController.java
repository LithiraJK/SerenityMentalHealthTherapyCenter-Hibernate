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
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.BOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistsBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.PatientDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.PatientTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapistTM;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TherapistsViewController implements Initializable {
    @FXML
    private TableView<TherapistTM> tblTherapist;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistId;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistName;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistAddress;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistEmail;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistStatus;

    @FXML
    private TableColumn<TherapistTM, Date> colTherapistDob;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistPhone;

    @FXML
    private JFXTextField txtTherapistName;

    @FXML
    private JFXTextField txtTherapistAddress;

    @FXML
    private JFXTextField txtTherapistEmail;

    @FXML
    private JFXTextField txtTherapistPhone;

    @FXML
    private ComboBox<String> cmbTherapistStatus;

    @FXML
    private DatePicker datePickerDob;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private TextField txtTherapistSearch;

    String id;

    private ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();


    private final TherapistsBO therapistsBO = (TherapistsBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateStatus();
        setTableListener();
        loadTherapistData();
        setCellValueFactory();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private void loadTherapistData() {
        therapistTMS.clear();
        try {
            List<TherapistDTO> therapistDTOS = therapistsBO.getAllTherapists();
            for (TherapistDTO dto : therapistDTOS) {
                therapistTMS.add(new TherapistTM(
                        dto.getTherapistId(),
                        dto.getTherapistName(),
                        dto.getEmail(),
                        dto.getAddress(),
                        dto.getPhone(),
                        dto.getDateOfBirth(),
                        dto.getStatus()
                ));
            }
            tblTherapist.setItems(therapistTMS);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colTherapistName.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colTherapistEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTherapistAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTherapistPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colTherapistDob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colTherapistStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    private void populateStatus() {
        cmbTherapistStatus.getItems().addAll("Active", "Inactive");
    }

    private void setTableListener() {
        tblTherapist.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtTherapistName.setText(newSelection.getTherapistName());
                txtTherapistEmail.setText(newSelection.getEmail());
                txtTherapistAddress.setText(newSelection.getAddress());
                txtTherapistPhone.setText(newSelection.getPhone());
                datePickerDob.setValue(newSelection.getDateOfBirth().toLocalDate());
                cmbTherapistStatus.setValue(newSelection.getStatus());
                id = String.valueOf(newSelection.getTherapistId());

                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
            } else {
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
            }
        });
    }

    @FXML
    void btnDeleteTherapistOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean isDeleted = therapistsBO.DeleteTherapist(id);

        if (isDeleted) {
            loadTherapistData();
            clearFields();
            new Alert(Alert.AlertType.CONFIRMATION, "Therapist Deleted Successfully").show();
            btnDelete.setDisable(true);
        } else {
            clearFields();
            new Alert(Alert.AlertType.ERROR, "Therapist Deletion failed").show();
        }
    }

    @FXML
    void btnSaveTherapistOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = txtTherapistName.getText();
        String address = txtTherapistAddress.getText();
        String email = txtTherapistEmail.getText();
        String phone = txtTherapistPhone.getText();
        String status = cmbTherapistStatus.getSelectionModel().getSelectedItem();
        Date dob = Date.valueOf(datePickerDob.getValue());
        TherapistDTO therapistDTO = new TherapistDTO(name, email, address, phone,  dob, status);

        boolean isSaved = therapistsBO.saveTherapist(therapistDTO);
        if (isSaved) {
            loadTherapistData();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Therapist saved Successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Therapist could not be saved").show();
        }
    }

    private void clearFields() {
        txtTherapistName.clear();
        txtTherapistAddress.clear();
        txtTherapistEmail.clear();
        txtTherapistPhone.clear();
        cmbTherapistStatus.setValue(null);
        datePickerDob.setValue(null);
    }

    @FXML
    void btnUpdateTherapistOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = txtTherapistName.getText();
        String address = txtTherapistAddress.getText();
        String email = txtTherapistEmail.getText();
        String phone = txtTherapistPhone.getText();
        String status = cmbTherapistStatus.getSelectionModel().getSelectedItem();
        Date dob = Date.valueOf(datePickerDob.getValue());
        TherapistDTO therapistDTO = new TherapistDTO(name, email,address, phone,  dob, status);

        boolean isUpdated = therapistsBO.updateTherapist(therapistDTO);

        if (isUpdated) {
            loadTherapistData();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Therapist updated Successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Therapist could not be update").show();
        }
    }
}
