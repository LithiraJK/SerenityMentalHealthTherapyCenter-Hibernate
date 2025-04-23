package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.BOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistAvailabilityBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistAvailabilityDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.TherapistDto;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapistAvailabilityTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class TherapistAvailabilityController implements Initializable {

    @FXML
    private TextField availabilityIdTxt;

    @FXML
    private TableColumn<TherapistAvailabilityTM, LocalDate> availableDateCol;

    @FXML
    private DatePicker availableDateTxt;

    @FXML
    private TableColumn<TherapistAvailabilityTM, String> availableIdCol;

    @FXML
    private TableColumn<TherapistAvailabilityTM, String> availableStatusCol;

    @FXML
    private TableColumn<TherapistAvailabilityTM, LocalTime> startTimeCol;

    @FXML
    private TextField startTimeTxt;

    @FXML
    private TextField endTimeTxt;

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private ChoiceBox<String> statusTxt;

    @FXML
    private TableView<TherapistAvailabilityTM> therapistAvailabilityTable;

    @FXML
    private TableColumn<TherapistAvailabilityTM, String> therapistIdCol;

    @FXML
    private TextField therapistIdTxt;

    @FXML
    private TextField therapistNameTxt;

    @FXML
    private Button therapistSearchBtn;

    @FXML
    private TableColumn<TherapistAvailabilityTM, LocalTime> endTimeCol;

    @FXML
    private Button updateButton;

    @FXML
    private AnchorPane bodyPane;

    private final TherapistAvailabilityBO availabilityBO = (TherapistAvailabilityBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST_AVAILABILITY);
    private final TherapistBO therapistBO = (TherapistBO) BOFactory.getInstance().getBO(BOFactory.BOType.THERAPIST);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        statusTxt.getItems().addAll("Available", "Not Available");
        statusTxt.setValue("Available");
        initializeColumns();
        refreshPage();
    }

    private void initializeColumns() {
        availableIdCol.setCellValueFactory(new PropertyValueFactory<>("availabilityId"));
        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        availableDateCol.setCellValueFactory(new PropertyValueFactory<>("availableDate"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        availableStatusCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
    }

    private void refreshPage() {
        availabilityIdTxt.clear();
        therapistIdTxt.clear();
        therapistNameTxt.clear();
        availableDateTxt.setValue(null);
        statusTxt.setValue("Available");

        availabilityIdTxt.setText(availabilityBO.getNextPK());
        refreshTable();
    }

    private void refreshTable() {
        ArrayList<TherapistAvailabilityDto> availabilityDtos = availabilityBO.getAllAvailabilities();
        ObservableList<TherapistAvailabilityTM> items = FXCollections.observableArrayList();

        for (TherapistAvailabilityDto dto : availabilityDtos) {
            items.add(new TherapistAvailabilityTM(
                    dto.getAvailabilityId(),
                    dto.getTherapistId(),
                    dto.getAvailableDate(),
                    dto.getStartTime(),
                    dto.getEndTime(),
                    dto.isAvailable()
            ));
        }

        therapistAvailabilityTable.setItems(items);
    }

    @FXML
    void save(ActionEvent event) {
        TherapistAvailabilityDto dto = getDtoFromFields();
        if (dto != null && availabilityBO.saveAvailability(dto)) {
            showAlert("Success", "Availability saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save availability", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void update(ActionEvent event) {
        TherapistAvailabilityDto dto = getDtoFromFields();
        if (dto != null && availabilityBO.updateAvailability(dto)) {
            showAlert("Success", "Availability updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update availability", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String id = availabilityIdTxt.getText();
        if (availabilityBO.deleteAvailability(id)) {
            showAlert("Success", "Availability deleted successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to delete availability", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String therapistName = searchTxt.getText().trim();
        if (therapistName.isEmpty()) {
            showAlert("Input Error", "Enter a therapist name to search", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }

        ArrayList<TherapistAvailabilityDto> results = availabilityBO.findByTherapist(therapistName);
        ObservableList<TherapistAvailabilityTM> list = FXCollections.observableArrayList();

        for (TherapistAvailabilityDto dto : results) {
            list.add(new TherapistAvailabilityTM(
                    dto.getAvailabilityId(),
                    dto.getTherapistId(),
                    dto.getAvailableDate(),
                    dto.getStartTime(),
                    dto.getEndTime(),
                    dto.isAvailable()
            ));
        }

        if (list.isEmpty()) {
            showAlert("Not Found", "No availability found", Alert.AlertType.WARNING);
            therapistAvailabilityTable.setItems(FXCollections.emptyObservableList());
        } else {
            therapistAvailabilityTable.setItems(list);
        }
    }

    @FXML
    void therapistSearch(ActionEvent event) {
        String name = therapistNameTxt.getText().trim();
        ArrayList<TherapistDto> therapists = therapistBO.findByTherapistName(name);

        if (therapists.isEmpty()) {
            showAlert("Not Found", "Therapist not found", Alert.AlertType.WARNING);
            return;
        }

        TherapistDto therapist = therapists.getFirst();

        therapistIdTxt.setText(therapist.getTherapistId());
        therapistNameTxt.setText(therapist.getName());
    }

    @FXML
    void tableClick(MouseEvent event) {
        TherapistAvailabilityTM selected = therapistAvailabilityTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availabilityIdTxt.setText(selected.getAvailabilityId());
            therapistIdTxt.setText(selected.getTherapistId());
            therapistNameTxt.setText(therapistBO.findByTherapistId(selected.getTherapistId()).getName());
            availableDateTxt.setValue(selected.getAvailableDate());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            startTimeTxt.setText(selected.getStartTime().format(formatter));
            endTimeTxt.setText(selected.getEndTime().format(formatter));
            statusTxt.setValue(selected.isAvailable() ? "Available" : "Not Available");
        }
    }

    private TherapistAvailabilityDto getDtoFromFields() {
        try {
            String timeInput = startTimeTxt.getText().trim();
            String durationInput = endTimeTxt.getText().trim();

            if (timeInput.isEmpty() || durationInput.isEmpty()) {
                showAlert("Input Error", "Time and duration fields cannot be empty.", Alert.AlertType.WARNING);
                return null;
            }

            LocalTime startTime;
            LocalTime endTime;
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("hh:mm a")
                        .toFormatter(Locale.ENGLISH);

                startTime = LocalTime.parse(startTimeTxt.getText().trim(), formatter);
                endTime = LocalTime.parse(endTimeTxt.getText().trim(), formatter);
            } catch (Exception e) {
                showAlert("Input Error", "Invalid time format. Please use format like 09:00 AM.", Alert.AlertType.WARNING);
                return null;
            }
            if (startTime.isAfter(endTime)) {
                showAlert("Input Error", "Start time cannot be after end time.", Alert.AlertType.WARNING);
                return null;
            }

            if (availableDateTxt.getValue() == null) {
                showAlert("Input Error", "Please select a date.", Alert.AlertType.WARNING);
                return null;
            }

            if (therapistIdTxt.getText().trim().isEmpty()) {
                showAlert("Input Error", "Therapist ID cannot be empty.", Alert.AlertType.WARNING);
                return null;
            }

            return new TherapistAvailabilityDto(
                    availabilityIdTxt.getText(),
                    therapistIdTxt.getText(),
                    availableDateTxt.getValue(),
                    startTime,
                    endTime,
                    null, // availableSlots
                    statusTxt.getValue().equals("Available")
            );

        } catch (Exception e) {
            showAlert("Unexpected Error", "Something went wrong: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }





    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
