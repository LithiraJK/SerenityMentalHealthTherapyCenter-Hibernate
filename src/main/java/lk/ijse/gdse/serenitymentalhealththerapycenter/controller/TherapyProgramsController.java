package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.TherapyProgramBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapyProgramTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TherapyProgramsController implements Initializable {

    @FXML
    private TableColumn<TherapyProgramTM, String> programDurationCol;

    @FXML
    private TableColumn<TherapyProgramTM, String> programNameCol;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField durationTxt;

    @FXML
    private TextArea descriptionTxt;

    @FXML
    private TextField feeTxt;

    @FXML
    private TableColumn<TherapyProgramTM, String> patientEmailCol;

    @FXML
    private TableColumn<TherapyProgramTM, BigDecimal> programFeeCol;

    @FXML
    private TableColumn<TherapyProgramTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private TextField programNameTxt;

    @FXML
    private TableColumn<TherapyProgramTM, String> programDescriptionCol;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableView<TherapyProgramTM> therapyProgramsTable;

    @FXML
    private Button updateButton;

    @FXML
    private AnchorPane bodyPane;

    private final TherapyProgramBO therapyProgramBO = new TherapyProgramBOImpl();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        programIdCol.setCellValueFactory(new PropertyValueFactory<>("programId"));
        programNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        programDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        programFeeCol.setCellValueFactory(new PropertyValueFactory<>("fee"));
        programDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        refreshPage();
    }

    public void refreshPage() {
        programIdTxt.clear();
        programNameTxt.clear();
        durationTxt.clear();
        feeTxt.clear();
        descriptionTxt.clear();

        try {
            programIdTxt.setText(therapyProgramBO.getNextTherapyProgramPK());
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        ArrayList<TherapyProgramDto> programDtos = therapyProgramBO.getAllTherapyPrograms();
        if (programDtos != null && !programDtos.isEmpty()) {
            ObservableList<TherapyProgramTM> programTMS = FXCollections.observableArrayList();
            for (TherapyProgramDto dto : programDtos) {
                TherapyProgramTM program = new TherapyProgramTM(
                        dto.getProgramId(),
                        dto.getName(),
                        dto.getDuration(),
                        dto.getFee(),
                        dto.getDescription()
                );
                programTMS.add(program);
            }
            therapyProgramsTable.setItems(programTMS);
        } else {
            therapyProgramsTable.setItems(FXCollections.emptyObservableList());
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String programId = programIdTxt.getText();
        if (therapyProgramBO.deleteTherapyProgram(programId)) {
            showAlert("Success", "Program deleted successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to delete program", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void save(ActionEvent event) {
        try {
            BigDecimal fee = new BigDecimal(feeTxt.getText().trim());
            TherapyProgramDto programDto = new TherapyProgramDto(
                    programIdTxt.getText(),
                    programNameTxt.getText(),
                    durationTxt.getText(),
                    fee,
                    descriptionTxt.getText()
            );

            if (therapyProgramBO.saveTherapyProgram(programDto)) {
                showAlert("Success", "Program saved successfully", Alert.AlertType.INFORMATION);
                refreshPage();
            } else {
                showAlert("Error", "Failed to save program", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid fee", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String name = searchTxt.getText().trim();

        if (name.isEmpty()) {
            showAlert("Input Error", "Please enter a program name to search", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }

        ArrayList<TherapyProgramDto> programDtos = therapyProgramBO.findTherapyProgramByName(name);

        if (programDtos != null && !programDtos.isEmpty()) {
            ObservableList<TherapyProgramTM> programTMS = FXCollections.observableArrayList();
            for (TherapyProgramDto dto : programDtos) {
                TherapyProgramTM programTM = new TherapyProgramTM(
                        dto.getProgramId(),
                        dto.getName(),
                        dto.getDuration(),
                        dto.getFee(),
                        dto.getDescription()
                );
                programTMS.add(programTM);
            }

            therapyProgramsTable.setItems(programTMS);
        } else {
            showAlert("Not Found", "No program found with that name", Alert.AlertType.WARNING);
            therapyProgramsTable.setItems(FXCollections.emptyObservableList());
        }
    }

    @FXML
    void tableClick(MouseEvent event) {
        TherapyProgramTM selectedProgram = therapyProgramsTable.getSelectionModel().getSelectedItem();
        if (selectedProgram != null) {
            programIdTxt.setText(selectedProgram.getProgramId());
            programNameTxt.setText(selectedProgram.getName());
            durationTxt.setText(selectedProgram.getDuration());
            feeTxt.setText(selectedProgram.getFee().toString());
            descriptionTxt.setText(selectedProgram.getDescription());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            BigDecimal fee = new BigDecimal(feeTxt.getText().trim());
            TherapyProgramDto programDto = new TherapyProgramDto(
                    programIdTxt.getText(),
                    programNameTxt.getText(),
                    durationTxt.getText(),
                    fee,
                    descriptionTxt.getText()
            );

            if (therapyProgramBO.updateTherapyProgram(programDto)) {
                showAlert("Success", "Program updated successfully", Alert.AlertType.INFORMATION);
                refreshPage();
            } else {
                showAlert("Error", "Failed to update program", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid fee", Alert.AlertType.WARNING);
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
