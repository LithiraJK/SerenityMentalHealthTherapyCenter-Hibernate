package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.PatientBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistAvailabiltyBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapySessionTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TimeSlotRow;
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
import java.util.*;

public class TherapySessionsController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<TherapySessionTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private Button patientSearchButton;

    @FXML
    private TableColumn<TherapySessionTM, String> programIdCol;

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
    private TableColumn<TherapySessionTM, LocalDate> sessionDateCol;

    @FXML
    private DatePicker sessionDateTxt;

    @FXML
    private TableColumn<TherapySessionTM, String> sessionIdCol;

    @FXML
    private TextField sessionIdTxt;

    @FXML
    private TableColumn<TherapySessionTM, LocalTime> sessionTimeCol;

    @FXML
    private TableColumn<TherapySessionTM, String> statusCol;

    @FXML
    private ChoiceBox<String> statusTxtChoice;

    @FXML
    private TableColumn<TherapySessionTM, String> therapistIdCol;

    @FXML
    private TextField therapistIdTxt;

    @FXML
    private TextField therapistNameTxt;

    @FXML
    private Button therapistSearchButton;

    @FXML
    private TableView<TherapySessionTM> therapySessionTable;

    @FXML
    private TableColumn<TherapySessionTM, Integer> durationCol;

    @FXML
    private TextField sessionDurationTxt;

    @FXML
    private TextField sessionTimeTxt;

    @FXML
    private Button updateButton;

    @FXML
    private AnchorPane bodyPane;

    private final TherapySessionBOImpl therapySessionBO = new TherapySessionBOImpl();
    private final TherapistAvailabiltyBO availabilityBO = new TherapistAvailabiltyBOImpl();
    private final TherapistBO therapistBO = new TherapistBOImpl();
    private final PatientBO patientBO = new PatientBOImpl();
    private final TherapyProgramBO programBO = new TherapyProgramBOImpl();

    public void initialize(URL location, ResourceBundle resources){

        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("programId"));
        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        sessionDateCol.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        sessionTimeCol.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusTxtChoice.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));

        refreshPage();

    }

    private void refreshPage() {
        sessionIdTxt.setText(therapySessionBO.getNextSessionPK());
        patientIdTxt.clear(); patientNameTxt.clear();
        programIdTxt.clear(); programNameTxt.clear();
        therapistIdTxt.clear(); therapistNameTxt.clear();
        sessionDateTxt.setValue(null);
        sessionTimeTxt.clear(); sessionDurationTxt.clear();
        statusTxtChoice.setValue("Scheduled");
        refreshTable();
    }

    private void refreshTable() {
        List<TherapySessionDto> sessionList = therapySessionBO.getAllSessions();
        ObservableList<TherapySessionTM> sessionTMs = FXCollections.observableArrayList();
        for (TherapySessionDto dto : sessionList) {
            sessionTMs.add(new TherapySessionTM(
                    dto.getSessionId(), dto.getPatientId(), dto.getTherapyProgramId(),
                    dto.getTherapistId(), dto.getAvailabilityId(), dto.getSessionDate(),
                    dto.getSessionTime(), dto.getDuration(), dto.getStatus()
            ));
        }
        therapySessionTable.setItems(sessionTMs);
    }


    @FXML
    void delete(ActionEvent event) {
        String sessionId = sessionIdTxt.getText();
        if (therapySessionBO.deleteSession(sessionId)) {
            showAlert("Success", "Session deleted successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to delete session", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void save(ActionEvent event) {
        try {
            TherapySessionDto dto = collectSessionData();
            boolean splitSuccess = availabilityBO.splitAvailabilitySlot(
                    dto.getTherapistId(), dto.getSessionDate(), dto.getSessionTime(), dto.getDuration()
            );
            if (!splitSuccess) {
                showAlert("Error", "Invalid slot: cannot split availability", Alert.AlertType.WARNING);
                return;
            }
            if (therapySessionBO.saveSession(dto)) {
                showAlert("Success", "Session saved", Alert.AlertType.INFORMATION);
                refreshPage();
            } else {
                showAlert("Error", "Save failed", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Exception", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
//        String searchTerm = searchTxt.getText();
//        List<TherapySessionDto> sessions = therapySessionBO.findByPatientName(searchTerm);
//        if (sessions != null && !sessions.isEmpty()) {
//            ObservableList<TherapySessionTM> items = FXCollections.observableArrayList();
//            for (TherapySessionDto dto : sessions) {
//                items.add(new TherapySessionTM(
//                        dto.getSessionId(), dto.getPatientId(), dto.getProgramId(),
//                        dto.getTherapistId(), dto.getSessionDate(), dto.getSessionTime(),
//                        dto.getDuration(), dto.getStatus()
//                ));
//            }
//            therapySessionTable.setItems(items);
//        } else {
//            showAlert("Not Found", "No sessions found for the given patient name", Alert.AlertType.WARNING);
//        }
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

    private String selectedAvailabilityId;  // class-level variable to store availabilityId

    private TherapySessionDto collectSessionData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        LocalTime time = LocalTime.parse(sessionTimeTxt.getText().trim(), formatter);

        return new TherapySessionDto(
                sessionIdTxt.getText(),
                patientIdTxt.getText(),
                programIdTxt.getText(),
                therapistIdTxt.getText(),
                null,
                sessionDateTxt.getValue(),
                time,
                Integer.parseInt(sessionDurationTxt.getText()),
                statusTxtChoice.getValue()
        );
    }


    @FXML
    void tableClick() {
        TherapySessionTM selected = therapySessionTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        sessionIdTxt.setText(selected.getSessionId());
        patientIdTxt.setText(selected.getPatientId());
        programIdTxt.setText(selected.getTherapyProgramId());
        therapistIdTxt.setText(selected.getTherapistId());
        sessionDateTxt.setValue(selected.getSessionDate());
        sessionTimeTxt.setText(selected.getSessionTime().toString());
        sessionDurationTxt.setText(String.valueOf(selected.getDuration()));
        statusTxtChoice.setValue(selected.getStatus());
    }

    @FXML
    void searchTherapist(ActionEvent event) {
        String name = therapistNameTxt.getText().trim();
        ArrayList<TherapistDto> therapists = therapistBO.findByTherapistName(name);

        if (therapists.isEmpty()) {
            showAlert("Not Found", "Therapist not found", Alert.AlertType.WARNING);
            return;
        }

        TherapistDto therapist = therapists.getFirst();

        therapistIdTxt.setText(therapist.getTherapistId());
        therapistNameTxt.setText(therapist.getName());
        loadAvailabilityTable(therapistIdTxt.getText());
    }

    @FXML
    void update(ActionEvent event) {
        try {
            TherapySessionDto dto = collectSessionData();
            if (therapySessionBO.updateSession(dto)) {
                showAlert("Success", "Session updated", Alert.AlertType.INFORMATION);
                refreshPage();
            } else {
                showAlert("Error", "Update failed", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Exception", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    // timetable for availability

    @FXML
    private TableView<TimeSlotRow> timeSlotTable;

    @FXML
    private TableColumn<TimeSlotRow, String> timeTSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date1TSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date2TSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date3TSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date4TSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date5TSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date6TSCol;

    @FXML
    private TableColumn<TimeSlotRow, String> date7TSCol;


    public void initialize() {
        // Load the availability table for a given therapist (e.g., "T001")
        loadAvailabilityTable("T001");
    }

    public void loadAvailabilityTable(String therapistId) {
        // Fetch availability slots for the therapist
        List<TherapistAvailabilityDto> slots = availabilityBO.findAvailableSlotsByTherapist(therapistId);

        // Group availabilities by date.
        // For each date, we keep the list of TherapistAvailability objects.
        Map<LocalDate, List<TherapistAvailabilityDto>> availabilityByDate = new TreeMap<>();
        for (TherapistAvailabilityDto slot : slots) {
            availabilityByDate
                    .computeIfAbsent(slot.getAvailableDate(), d -> new ArrayList<>())
                    .add(slot);
        }

        // Determine the overall time range from the minimum start time to the maximum end time (across all dates).
        LocalTime minTime = LocalTime.MAX;
        LocalTime maxTime = LocalTime.MIN;
        for (List<TherapistAvailabilityDto> list : availabilityByDate.values()) {
            for (TherapistAvailabilityDto slot : list) {
                LocalTime start = slot.getStartTime();
                LocalTime end = slot.getEndTime();
                if (start.isBefore(minTime)) {
                    minTime = start;
                }
                if (end.isAfter(maxTime)) {
                    maxTime = end;
                }
            }
        }

        // If for some reason no availabilities were found, use a default range.
        if (minTime.equals(LocalTime.MAX) || maxTime.equals(LocalTime.MIN)) {
            minTime = LocalTime.of(8, 0);
            maxTime = LocalTime.of(18, 0);
        }

        // Generate time slots (e.g., every 15 minutes) from minTime to maxTime.
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime t = minTime;
        while (!t.isAfter(maxTime)) {
            timeSlots.add(t);
            t = t.plusMinutes(15);
        }

        // Build the set of dates from the keys of the availabilityByDate map.
        List<LocalDate> dates = new ArrayList<>(availabilityByDate.keySet());
        // (Assume there are at most 7 dates; otherwise, you need to manage extra columns.)

        // Configure the fixed "Time" column.
        timeTSCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Set up the date columns dynamically.
        List<TableColumn<TimeSlotRow, String>> dateColumns = Arrays.asList(
                date1TSCol, date2TSCol, date3TSCol, date4TSCol, date5TSCol, date6TSCol, date7TSCol
        );
        for (int i = 0; i < dates.size() && i < dateColumns.size(); i++) {
            LocalDate date = dates.get(i);
            TableColumn<TimeSlotRow, String> col = dateColumns.get(i);
            // Set the header text to the date string
            col.setText(date.toString());
            // For each row, bind the availability value for this date.
            int index = i;  // final variable for use in lambda
            col.setCellValueFactory(cellData -> cellData.getValue().availabilityProperty(date));
        }

        // Create an ObservableList for the table rows.
        ObservableList<TimeSlotRow> rows = FXCollections.observableArrayList();

        // Format for displaying times (e.g., "HH:mm")
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // For each generated time slot, create a row.
        for (LocalTime slotTime : timeSlots) {
            String timeText = slotTime.format(timeFormatter);
            TimeSlotRow row = new TimeSlotRow(timeText, new HashSet<>(dates));
            // For each date, check all availability periods on that date.
            for (LocalDate date : dates) {
                List<TherapistAvailabilityDto> availabilities = availabilityByDate.get(date);
                boolean isAvailable = false;
                if (availabilities != null) {
                    for (TherapistAvailabilityDto availability : availabilities) {
                        LocalTime start = availability.getStartTime();
                        LocalTime end = availability.getEndTime();
                        // Check if slotTime lies in [start, end)
                        if (!slotTime.isBefore(start) && slotTime.isBefore(end)) {
                            isAvailable = true;
                            break;
                        }
                    }
                }
                row.setAvailability(date, isAvailable ? "✔️" : "");
            }
            rows.add(row);
        }

        // Set the items for the table.
        timeSlotTable.setItems(rows);
    }


    @FXML
    void onclickTSTable(MouseEvent event) {
//        TimeSlotRow selectedRow = timeSlotTable.getSelectionModel().getSelectedItem();
//        if (selectedRow == null) return;
//
//        TablePosition pos = timeSlotTable.getSelectionModel().getSelectedCells().get(0);
//        int colIndex = pos.getColumn();
//
//        // Get corresponding TableColumn
//        TableColumn col = timeSlotTable.getColumns().get(colIndex);
//
//        // Get the date from column header
//        sessionTimeTxt.setText(selectedRow.timeProperty().getValue());
//
//        String dateStr = col.getText();
//        LocalDate selectedDate = LocalDate.parse(dateStr);
//        LocalTime selectedTime = LocalTime.parse(sessionTimeTxt.getText());
//
//        // Check if slot is actually available
//        String status = (String) col.getCellObservableValue(selectedRow).getValue();
//        if (!"✔️".equals(status)) {
//            showAlert("Invalid Slot", "This time slot is not available", Alert.AlertType.WARNING);
//            return;
//        }
//
//        // Set fields in form
//        sessionDateTxt.setValue(selectedDate);
//        sessionTimeTxt.setText(selectedTime.toString());
//
//        // Set the selected availability ID
//        selectedRow.setAvailabilityId("SomeUniqueAvailabilityId"); // Use the appropriate ID for the selected row
    }


}