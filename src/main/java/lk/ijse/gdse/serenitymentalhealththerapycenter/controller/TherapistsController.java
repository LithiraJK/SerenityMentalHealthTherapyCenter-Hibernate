package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.TherapistBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.TherapistProgramBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.TherapyProgramBOImpl;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapyProgramDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapistProgramTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapistTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TherapistsController implements Initializable {


    @FXML
    private Button deleteButton;

    @FXML
    private Button therapistAvailabilityBtn;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableColumn<TherapistTM, String> therapistEmailCol;

    @FXML
    private TextField therapistEmailTxt;

    @FXML
    private TableColumn<TherapistTM, String> therapistIdCol;

    @FXML
    private TextField therapistIdTxt;

    @FXML
    private TableColumn<TherapistTM, String> therapistNameCol;

    @FXML
    private TextField therapistNameTxt;

    @FXML
    private TableColumn<TherapistTM, String> therapistPhoneCol;

    @FXML
    private TextField therapistPhoneTxt;

    @FXML
    private TableColumn<TherapistTM, String> therapistSpecsCol;

    @FXML
    private TextField therapistSpecsTxt;

    @FXML
    private TableView<TherapistTM> therapistsTable;

    @FXML
    private Button updateButton;

    @FXML
    private AnchorPane bodyPane;

    NavigationUtil navigate = new NavigationUtil();

    private final TherapistBO therapistBO = new TherapistBOImpl();

    @FXML
    void loadTherapistAvailabilityPage(ActionEvent event) {
        navigate.navigatePopup("/view/therapist-availability-page.fxml", "Manage Therapist Availability");
    }

    public void initialize(URL location, ResourceBundle resources) {

        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        therapistNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        therapistEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        therapistPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        therapistSpecsCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        loadTPTable();

        try {
            refreshPage();
        } catch (Exception e) {
            throw e;
        }
    }


    public void refreshPage() {
        therapistIdTxt.clear();
        therapistNameTxt.clear();
        therapistEmailTxt.clear();
        therapistPhoneTxt.clear();
        therapistSpecsTxt.clear();
        try {
            therapistIdTxt.setText(therapistBO.getNextTherapistPK());
            refreshTable();
        } catch (Exception e) {
            throw e;
        }
    }

    public void refreshTable() {
        ArrayList<TherapistDto> therapistDtos = therapistBO.getAllTherapists();
        if (therapistDtos != null && !therapistDtos.isEmpty()) {
            ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();
            for (TherapistDto therapistDto : therapistDtos) {
                TherapistTM therapist = new TherapistTM(
                        therapistDto.getTherapistId(),
                        therapistDto.getName(),
                        therapistDto.getEmail(),
                        therapistDto.getPhone(),
                        therapistDto.getSpecialization()
                );
                therapistTMS.add(therapist);
            }
            therapistsTable.setItems(therapistTMS);
        } else {
            therapistsTable.setItems(FXCollections.emptyObservableList());
        }
    }

    @FXML
    void tableClick(MouseEvent event) {
        TherapistTM selectedTherapist = therapistsTable.getSelectionModel().getSelectedItem();
        if (selectedTherapist != null) {
            therapistIdTxt.setText(selectedTherapist.getTherapistId());
            therapistNameTxt.setText(selectedTherapist.getName());
            therapistEmailTxt.setText(selectedTherapist.getEmail());
            therapistPhoneTxt.setText(selectedTherapist.getPhone());
            therapistSpecsTxt.setText(selectedTherapist.getSpecialization());
            loadTPTableData();
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String therapistId = therapistIdTxt.getText();
        if (therapistBO.deleteTherapist(therapistId)) {
            showAlert("Success", "Therapist deleted successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to delete therapist", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void save(ActionEvent event) {
        TherapistDto therapistDto = new TherapistDto(
                therapistIdTxt.getText(),
                therapistNameTxt.getText(),
                therapistEmailTxt.getText(),
                therapistPhoneTxt.getText(),
                therapistSpecsTxt.getText()
        );

        if (therapistBO.saveTherapist(therapistDto)) {
            showAlert("Success", "Therapist saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save therapist", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String name = searchTxt.getText().trim();

        if (name.isEmpty()) {
            showAlert("Input Error", "Please enter a name to search", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }

        ArrayList<TherapistDto> therapistDtos = therapistBO.findByTherapistName(name);

        if (therapistDtos != null && !therapistDtos.isEmpty()) {
            ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();

            for (TherapistDto therapistDto : therapistDtos) {
                TherapistTM therapistTM = new TherapistTM(
                        therapistDto.getTherapistId(),
                        therapistDto.getName(),
                        therapistDto.getEmail(),
                        therapistDto.getPhone(),
                        therapistDto.getSpecialization()
                );
                therapistTMS.add(therapistTM);
            }

            therapistsTable.setItems(therapistTMS);
        } else {
            showAlert("Not Found", "No therapist found with that name", Alert.AlertType.WARNING);
            therapistsTable.setItems(FXCollections.emptyObservableList());
        }
    }

    @FXML
    void update(ActionEvent event) {
        TherapistDto therapistDto = new TherapistDto(
                therapistIdTxt.getText(),
                therapistNameTxt.getText(),
                therapistEmailTxt.getText(),
                therapistPhoneTxt.getText(),
                therapistSpecsTxt.getText()
        );

        if (therapistBO.updateTherapist(therapistDto)) {
            showAlert("Success", "Therapist updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update therapist", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // ---------------------------------------------------------------------------------

    @FXML
    private Button deleteButtonTP;

    @FXML
    private Button getProgramIDButton;

    @FXML
    private TableColumn<TherapistProgramTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private TableColumn<TherapistProgramTM, String> programNameCol;

    @FXML
    private TextField programNameTxt;


    @FXML
    private Button saveButtonTP;

    @FXML
    private Button searchButtonTP;

    @FXML
    private TextField searchTxtTP;

    @FXML
    private TableView<TherapistProgramTM> therapistProgramTable;

    @FXML
    private Button updateButtonTP;

    TherapistProgramBO therapistProgramBO = new TherapistProgramBOImpl();
    TherapyProgramBO therapyProgramBO = new TherapyProgramBOImpl();

    private void loadTPTable() {
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgramId"));
        programNameCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgramName"));
    }


    @FXML
    void deleteTP(ActionEvent event) {
        String therapistId = therapistIdTxt.getText();
        String programId = programIdTxt.getText();

        boolean isDeleted = therapistProgramBO.deleteTherapistProgram(therapistId, programId);

        if (isDeleted) {
            showAlert("Success", "Therapist Program deleted successfully.");
            loadTPTableData();
        } else {
            showAlert("Error", "Failed to delete Therapist Program.");
        }
    }

    @FXML
    void getIdTP(ActionEvent event) {
        List<TherapyProgramDto> selected = therapyProgramBO.findTherapyProgramByName(programNameTxt.getText());

        if (selected != null && !selected.isEmpty()) {
            TherapyProgramDto firstMatch = selected.get(0);
            programIdTxt.setText(firstMatch.getProgramId());
            programNameTxt.setText(firstMatch.getName());
        } else {
            showAlert("Not Found", "No therapy program found with that name.");
        }
    }


    @FXML
    void saveTP(ActionEvent event) {
        String therapistId = therapistIdTxt.getText();
        String programId = programIdTxt.getText();

        boolean isSaved = therapistProgramBO.saveTherapistProgram(therapistId, programId);

        if (isSaved) {
            showAlert("Success", "Therapist Program saved successfully.");
            loadTPTableData();
        } else {
            showAlert("Error", "Failed to save Therapist Program.");
        }
    }

    @FXML
    void searchTP(ActionEvent event) {
        String name = searchTxtTP.getText();

        List<TherapistProgramDto> results = therapistProgramBO.findByProgramName(name);
        updateTPTableView(results);
    }

    @FXML
    void tableClickTP(MouseEvent event) {
        TherapistProgramTM selected = therapistProgramTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            programIdTxt.setText(selected.getTherapyProgramId());
            programNameTxt.setText(selected.getTherapyProgramName());
        }
    }

    @FXML
    void updateTP(ActionEvent event) {
        String therapistId = programIdTxt.getText();
        String programId = programNameTxt.getText();

        boolean isUpdated = therapistProgramBO.updateTherapistProgram(therapistId, programId);

        if (isUpdated) {
            showAlert("Success", "Therapist Program updated successfully.");
            loadTPTableData();
        } else {
            showAlert("Error", "Failed to update Therapist Program.");
        }
    }

    private void loadTPTableData() {
        List<TherapistProgramDto> programs = therapistProgramBO.getTherapistProgramsByTherapist(therapistIdTxt.getText());
        updateTPTableView(programs);
        programIdTxt.setText("");
        programNameTxt.setText("");
    }

    private void updateTPTableView(List<TherapistProgramDto> programs) {
        ObservableList<TherapistProgramTM> tableData = FXCollections.observableArrayList();

        for (TherapistProgramDto program : programs) {
            tableData.add(new TherapistProgramTM(program.getTherapyProgramId(), program.getTherapyProgramName()));
        }

        therapistProgramTable.setItems(tableData);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void switchSearchButtonFunction(KeyEvent event) {
        if (searchTxt.getText().isEmpty()) {
            searchButton.setText("Clear");
        } else {
            searchButton.setText("Search");
        }
    }

    @FXML
    void switchSearchButtonFunctionTP(KeyEvent event) {
        if (searchTxtTP.getText().isEmpty()) {
            searchButtonTP.setText("Clear");
        } else {
            searchButtonTP.setText("Search");
        }
    }

}
