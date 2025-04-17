package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.BOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDTO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapyProgramTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TheraphyProgramViewController implements Initializable {
    @FXML
    private TableView<TherapyProgramTM> tblProgram;

    @FXML
    private TableColumn<TherapyProgramTM, String> colProgramID;

    @FXML
    private TableColumn<TherapyProgramTM, String> colProgramName;

    @FXML
    private TableColumn<TherapyProgramTM, String> colProgramDuration;

    @FXML
    private TableColumn<TherapyProgramTM, Double> colProgramFee;

    @FXML
    private JFXTextField txtProgramName;

    @FXML
    private JFXTextField txtProgramDuration;

    @FXML
    private JFXTextField txtProgramFee;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnSave;

    String id;

    private final TherapyProgramBO therapyProgramBO = (TherapyProgramBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPYPROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableListener();
        loadProgramData();
        setCellValueFactory();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private void loadProgramData() {
        try {
            List<TherapyProgramDTO> programs = therapyProgramBO.getAllPrograms();
            ObservableList<TherapyProgramTM> programList = FXCollections.observableArrayList();
            for (TherapyProgramDTO dto : programs) {
                programList.add(new TherapyProgramTM(
                        dto.getProgramId(),
                        dto.getProgramName(),
                        dto.getDuration(),
                        dto.getProgramFee()
                ));
            }
            tblProgram.setItems(programList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colProgramID.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colProgramDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colProgramFee.setCellValueFactory(new PropertyValueFactory<>("programFee"));
    }

    private void setTableListener() {
        tblProgram.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setProgramData(newValue);
            }
        });
    }

    private void setProgramData(TherapyProgramTM program) {
        txtProgramName.setText(program.getProgramName());
        txtProgramDuration.setText(program.getDuration());
        txtProgramFee.setText(String.valueOf(program.getProgramFee()));
        id = String.valueOf(program.getProgramId());
        btnDelete.setDisable(false);
        btnUpdate.setDisable(false);
    }

    @FXML
    void btnDeleteProgramOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean isDeleted = therapyProgramBO.deleteTherapyProgram(id);

        if (isDeleted) {
            loadProgramData();
            clearFields();
            new Alert(Alert.AlertType.CONFIRMATION, "Therapy Program Deleted").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "failed to delete therapy program").show();
        }
    }

    private void clearFields() {
        txtProgramName.clear();
        txtProgramDuration.clear();
        txtProgramFee.clear();
        id = null;
    }

    @FXML
    void btnSaveProgramOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String programName = txtProgramName.getText();
        String programDuration = txtProgramDuration.getText();
        String programFee = txtProgramFee.getText();

        TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO(programName, programDuration, programFee);

        boolean isSaved = therapyProgramBO.saveProgram(therapyProgramDTO);

        if (isSaved) {
            loadProgramData();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Program Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "program not saved").show();
        }
    }

    @FXML
    void btnUpdateProgramOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String programName = txtProgramName.getText();
        String programDuration = txtProgramDuration.getText();
        String programFee = txtProgramFee.getText();

        TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO(programName, programDuration, programFee);

        boolean isUpdated = therapyProgramBO.updateProgram(therapyProgramDTO);

        if (isUpdated) {
            loadProgramData();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Program Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "program not Updated").show();
        }
    }
}
