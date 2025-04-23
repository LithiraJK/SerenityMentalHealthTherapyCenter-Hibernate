package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.*;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TherapySessionTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.dto.tm.TimeSlotRowTM;
import lk.ijse.gdse.serenitymentalhealththerapycenter.util.ValidateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;


public class TherapySessionsController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<TherapySessionTM, Duration> durationCol;

    @FXML
    private TableColumn<TherapySessionTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private Button patientSearchButton;

    @FXML
    private Button paymentLoadButton;

    @FXML
    private TableColumn<TherapySessionTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private ChoiceBox<String> programNameTxt;

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
    private ChoiceBox<String> sessionDurationTxt;

    @FXML
    private TableColumn<TherapySessionTM, String> sessionIdCol;

    @FXML
    private TextField sessionIdTxt;

    @FXML
    private TableColumn<TherapySessionTM, String> sessionTimeCol;

    @FXML
    private TextField sessionTimeTxt;

    @FXML
    private TableColumn<TherapySessionTM, String> statusCol;

    @FXML
    private ChoiceBox<String> statusTxtChoice;

    @FXML
    private TableColumn<TherapySessionTM, String> therapistIdCol;

    @FXML
    private TextField therapistIdTxt;

    @FXML
    private ChoiceBox<String> therapistNameTxt;

    @FXML
    private TableView<TherapySessionTM> therapySessionTable;

    @FXML
    private Button updateButton;

    private final PatientBO patientBO = new PatientBOImpl();
    private final PatientProgramBO patientProgramBO = new PatientProgramBOImpl();
    private TherapistBO therapistBO = new TherapistBOImpl();
    private final TherapyProgramBO therapyProgramBO = new TherapyProgramBOImpl();
    private final TherapistProgramBO therapistProgramBO = new TherapistProgramBOImpl();

    private final TherapySessionBO therapySessionBO = new TherapySessionBOImpl();

    private final TherapistAvailabilityBO therapistAvailabiltyBO = new TherapistAvailabilityBOImpl();

    DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder().appendPattern("hh:mm a").toFormatter().withLocale(Locale.ENGLISH);


    public void initialize(URL location, ResourceBundle resources) {

        sessionDurationTxt.getItems().addAll("30 minutes", "1 hour", "1 and half hour", "2 hours");
        statusTxtChoice.getItems().addAll("Scheduled", "Completed", "Cancelled");

        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgramId"));
        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        sessionDateCol.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        sessionTimeCol.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTimeTable();

        loadAllSessions();

    }

    private void loadAllSessions() {
        List<TherapySessionDto> allSessions = therapySessionBO.getAll();
        List<TherapySessionTM> tmList = allSessions.stream().map(dto ->
                new TherapySessionTM(
                        dto.getSessionId(),
                        dto.getPatientId(),
                        dto.getTherapyProgramId(),
                        dto.getTherapistId(),
                        null, //dto.getAvailabilityId(),
                        dto.getSessionDate(),
                        dto.getSessionTime(),
                        Duration.ofMinutes(dto.getDuration()), // convert int to Duration
                        dto.getStatus()
                )
        ).collect(Collectors.toList());

        therapySessionTable.getItems().setAll(tmList);
        sessionIdTxt.setText(therapySessionBO.getNextSessionPK());
        saveButton.setDisable(false);
        updateButton.setDisable(true);
    }


    @FXML
    void save(ActionEvent event) {

        String sessionId = sessionIdTxt.getText().trim();
        String patientId = patientIdTxt.getText().trim();
        String programId = programIdTxt.getText().trim();
        String therapistId = therapistIdTxt.getText().trim();
        LocalDate sessionDate = sessionDateTxt.getValue();
        String sessionTimeStr = sessionTimeTxt.getText().trim();
        String status = statusTxtChoice.getValue();
        String sessionDurationChoice = sessionDurationTxt.getValue();

        // Validate required fields
        if (!ValidateUtil.areRequiredFields(sessionId, patientId, programId, therapistId, sessionTimeStr) ||
                sessionDate == null || sessionDurationChoice == null || status == null) {
            showAlert("Input Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
            return;
        }

        // Validate time format
        if (!ValidateUtil.isValidTime(sessionTimeStr)) {
            showAlert("Input Error", "Please enter a valid time format (e.g., 10:30 AM).", Alert.AlertType.ERROR);
            return;
        }

        // Parse the time after validation
        LocalTime sessionTime;
        try {
            sessionTime = LocalTime.parse(sessionTimeStr, timeFormatter);
        } catch (Exception e) {
            showAlert("Input Error", "Invalid time format. Please use format like 10:30 AM.", Alert.AlertType.ERROR);
            return;
        }

        // Validate IDs
        if (!ValidateUtil.isValidId(patientId, "PATIENT")) {
            showAlert("Input Error", "Invalid patient ID format. Should be P followed by 3 digits (e.g., P001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(therapistId, "THERAPIST")) {
            showAlert("Input Error", "Invalid therapist ID format. Should be T followed by 3 digits (e.g., T001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(programId, "THERAPY_PROGRAM")) {
            showAlert("Input Error", "Invalid program ID format. Should be TP followed by 3 digits (e.g., TP001).", Alert.AlertType.ERROR);
            return;
        }

        int sessionDuration = switch (sessionDurationChoice) {
            case "30 minutes" -> 30;
            case "1 hour" -> 60;
            case "1 and half hour" -> 90;
            case "2 hours" -> 120;
            default -> 0;
        };

        TherapySessionDto session = new TherapySessionDto(
                sessionId, patientId, programId, therapistId,
                null, sessionDate, sessionTime, sessionDuration, status
        );

        boolean saved = therapySessionBO.save(session);
        if (saved) {
            showAlert("Success", "Therapy session saved successfully!", Alert.AlertType.INFORMATION);
            loadAllSessions();
            clearTimeTable();
            clearForm();
        } else {
            showAlert("Error", "Failed to save therapy session.", Alert.AlertType.ERROR);
        }

        //        String therapistId = therapistNameTxt.getValue();
//        LocalDate date = sessionDateTxt.getValue();
//        String durationStr = sessionDurationTxt.getValue();
//        Duration sessionDuration = Duration.ofMinutes(Long.parseLong(durationStr));
//
//        boolean success = therapistAvailabiltyBO.bookTimeSlot(therapistId, date, startTime, sessionDuration);
//
//        if (success) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Time slot successfully booked!");
//            alert.showAndWait();
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to book the time slot. Please try again.");
//            alert.showAndWait();
//        }
//        boolean success = therapistAvailabiltyBO.bookTimeSlot("therapist001", LocalDate.of(2025, 4, 22), Duration.ofMinutes(60));


    }


    @FXML
    void update(ActionEvent event) {
        if (sessionIdTxt.getText() == null || sessionIdTxt.getText().isEmpty()) {
            showAlert("Warning", "Please select a session from the table to update.", Alert.AlertType.WARNING);
            return;
        }

        String sessionId = sessionIdTxt.getText().trim();
        String patientId = patientIdTxt.getText().trim();
        String programId = programIdTxt.getText().trim();
        String therapistId = therapistIdTxt.getText().trim();
        LocalDate sessionDate = sessionDateTxt.getValue();
        String sessionTimeStr = sessionTimeTxt.getText().trim();
        String status = statusTxtChoice.getValue();
        String sessionDurationChoice = sessionDurationTxt.getValue();

        // Validate required fields
        if (!ValidateUtil.areRequiredFields(sessionId, patientId, programId, therapistId, sessionTimeStr) ||
                sessionDate == null || sessionDurationChoice == null || status == null) {
            showAlert("Input Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
            return;
        }

        // Validate time format
        if (!ValidateUtil.isValidTime(sessionTimeStr)) {
            showAlert("Input Error", "Please enter a valid time format (e.g., 10:30 AM).", Alert.AlertType.ERROR);
            return;
        }

        // Parse the time after validation
        LocalTime sessionTime;
        try {
            sessionTime = LocalTime.parse(sessionTimeStr, timeFormatter);
        } catch (Exception e) {
            showAlert("Input Error", "Invalid time format. Please use format like 10:30 AM.", Alert.AlertType.ERROR);
            return;
        }

        // Validate IDs
        if (!ValidateUtil.isValidId(patientId, "PATIENT")) {
            showAlert("Input Error", "Invalid patient ID format. Should be P followed by 3 digits (e.g., P001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(therapistId, "THERAPIST")) {
            showAlert("Input Error", "Invalid therapist ID format. Should be T followed by 3 digits (e.g., T001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(programId, "THERAPY_PROGRAM")) {
            showAlert("Input Error", "Invalid program ID format. Should be TP followed by 3 digits (e.g., TP001).", Alert.AlertType.ERROR);
            return;
        }

        int sessionDuration = switch (sessionDurationChoice) {
            case "30 minutes" -> 30;
            case "1 hour" -> 60;
            case "1 and half hour" -> 90;
            case "2 hours" -> 120;
            default -> 0;
        };

        boolean isUpdated = therapySessionBO.update(
                new TherapySessionDto(sessionId, patientId, programId, therapistId, null, sessionDate, sessionTime, sessionDuration, status)
        );

        if (isUpdated) {
            showAlert("Success", "Session updated!", Alert.AlertType.INFORMATION);
            loadAllSessions();
            clearForm();
        } else {
            showAlert("Failed", "Failed to update session.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String sessionId = sessionIdTxt.getText();
        if (sessionId == null || sessionId.isEmpty()) {
            showAlert("Warning", "Please select a session to delete.", Alert.AlertType.WARNING);
            return;
        }

        boolean isDeleted = therapySessionBO.delete(sessionId);
        if (isDeleted) {
            showAlert("Success", "Session deleted!", Alert.AlertType.INFORMATION);
            loadAllSessions();
            clearForm();
        } else {
            showAlert("Failed", "Failed to delete session.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String name = searchTxt.getText().trim();
        if (name.isEmpty()) {
            showAlert("Input Error", "Enter a patient Name to search.", Alert.AlertType.WARNING);
            loadAllSessions();
            clearForm();
            return;
        }
        List<PatientDto> patients = patientBO.findByPatientName(name);
        searchTxt.setText(patients.get(0).getName());
        List<TherapySessionDto> sessions = therapySessionBO.findByPatientId(patients.get(0).getPatientId());
        therapySessionTable.getItems().clear();
        therapySessionTable.getItems().addAll(
                sessions.stream().map(dto ->
                        new TherapySessionTM(
                                dto.getSessionId(),
                                dto.getPatientId(),
                                dto.getTherapyProgramId(),
                                dto.getTherapistId(),
                                null, // or dto.getAvailabilityId()
                                dto.getSessionDate(),
                                dto.getSessionTime(),
                                Duration.ofMinutes(dto.getDuration()),
                                dto.getStatus()
                        )
                ).collect(Collectors.toList())
        );

        if (sessions.isEmpty()) {
            showAlert("No Results", "No therapy sessions found.", Alert.AlertType.INFORMATION);
        }
    }


    @FXML
    void tableClick(MouseEvent event) {
        TherapySessionTM selected = therapySessionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sessionIdTxt.setText(selected.getSessionId());
            patientIdTxt.setText(selected.getPatientId());
            programIdTxt.setText(selected.getTherapyProgramId());
            therapistIdTxt.setText(selected.getTherapistId());
            sessionDateTxt.setValue(selected.getSessionDate());
            sessionTimeTxt.setText(selected.getSessionTime().format(timeFormatter));

            String sessionDurationStr = switch ((int) selected.getDuration().toMinutes()) {
                case 30 -> "30 minutes";
                case 60 -> "1 hour";
                case 90 -> "1 and half hour";
                case 120 -> "2 hours";
                default -> "";
            };

            sessionDurationTxt.setValue(sessionDurationStr);
            statusTxtChoice.setValue(selected.getStatus());
            saveButton.setDisable(true);
            updateButton.setDisable(false);
        }
    }


    private void clearForm() {
        sessionIdTxt.clear();
        patientIdTxt.clear();
        patientNameTxt.clear();
        programIdTxt.clear();
        programNameTxt.getItems().clear();
        therapistIdTxt.clear();
        therapistNameTxt.getItems().clear();
        sessionDateTxt.setValue(null);
        sessionTimeTxt.clear();
        sessionDurationTxt.getSelectionModel().clearSelection();
        statusTxtChoice.getSelectionModel().clearSelection();
        searchTxt.clear();
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

        List<PatientProgramDto> programs = patientProgramBO.getProgramsByPatientId(patient.getPatientId());

        List<String> programNames = programs.stream()
                .map(PatientProgramDto::getProgramName)
                .collect(Collectors.toList());

        programNameTxt.getItems().clear(); // optional: clears old data if needed
        programIdTxt.clear();
        programNameTxt.getItems().addAll(programNames);

        programNameTxt.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchProgram();
            }
        });
    }

    void searchProgram() {
        String name = programNameTxt.getValue().trim();
        ArrayList<TherapyProgramDto> programs = therapyProgramBO.findTherapyProgramByName(name);

        if (programs.isEmpty()) {
            showAlert("Not Found", "Program not found", Alert.AlertType.WARNING);
            return;
        }

        TherapyProgramDto program = programs.getFirst();
        programIdTxt.setText(program.getProgramId());


        // Load therapists based on the selected program
        List<TherapistProgramDto> therapistsPrograms = therapistProgramBO.findByProgramName(programNameTxt.getValue().trim());

        List<String> therapistNames = therapistsPrograms.stream()
                .map(dto -> therapistBO.findByTherapistId(dto.getTherapistId()).getName())
                .collect(Collectors.toList());

        therapistNameTxt.getItems().clear();
        therapistIdTxt.clear();
        therapistNameTxt.getItems().addAll(therapistNames);

        therapistNameTxt.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchTherapist();
            }
        });
    }

    void searchTherapist() {
        String name = therapistNameTxt.getValue().trim();
        ArrayList<TherapistDto> therapists = therapistBO.findByTherapistName(name);

        if (therapists.isEmpty()) {
            showAlert("Not Found", "Therapist not found", Alert.AlertType.WARNING);
            return;
        }

        TherapistDto therapist = therapists.getFirst();
        therapistIdTxt.setText(therapist.getTherapistId());
        loadDataToTimeTable(therapist.getTherapistId());
    }

    @FXML
    void loadPaymentPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/payments-page.fxml"));
            Parent root = loader.load();

            PaymentsController controller = loader.getController();
            controller.setFromMainPage(true); // pass the boolean
            controller.configurePage();       // apply changes to the UI

            Stage stage = new Stage();
            stage.setTitle("Manage Payments");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private TableView<TimeSlotRowTM> timeSlotTable;
    @FXML
    private TableColumn<TimeSlotRowTM, String> timeTSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date1TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date2TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date3TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date4TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date5TSCol;

    private final List<LocalDate> nextFiveDates = new ArrayList<>();


    void loadTimeTable() {
        timeTSCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        date1TSCol.setCellValueFactory(new PropertyValueFactory<>("date1Status"));
        date2TSCol.setCellValueFactory(new PropertyValueFactory<>("date2Status"));
        date3TSCol.setCellValueFactory(new PropertyValueFactory<>("date3Status"));
        date4TSCol.setCellValueFactory(new PropertyValueFactory<>("date4Status"));
        date5TSCol.setCellValueFactory(new PropertyValueFactory<>("date5Status"));
    }

    public void loadDataToTimeTable(String therapistId) {
        nextFiveDates.clear();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            nextFiveDates.add(today.plusDays(i));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd");
        date1TSCol.setText(nextFiveDates.get(0).format(formatter));
        date2TSCol.setText(nextFiveDates.get(1).format(formatter));
        date3TSCol.setText(nextFiveDates.get(2).format(formatter));
        date4TSCol.setText(nextFiveDates.get(3).format(formatter));
        date5TSCol.setText(nextFiveDates.get(4).format(formatter));


        // Collect all time slots across 5 days
        Set<String> uniqueSlots = new TreeSet<>();
        Map<LocalDate, List<String>> slotMap = new HashMap<>();

        for (LocalDate date : nextFiveDates) {
            List<TherapistAvailabilityDto> availabilityList = therapistAvailabiltyBO.findByTherapistAndDate(therapistId, date);
            List<String> daySlots = new ArrayList<>();

            for (TherapistAvailabilityDto dto : availabilityList) {
                daySlots.addAll(dto.getAvailableSlots());
                uniqueSlots.addAll(dto.getAvailableSlots());
            }

            slotMap.put(date, daySlots);
        }

        // Build table rows
        List<TimeSlotRowTM> rows = new ArrayList<>();
        for (String slot : uniqueSlots) {
            rows.add(new TimeSlotRowTM(
                    slot,
                    slotMap.getOrDefault(nextFiveDates.get(0), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(1), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(2), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(3), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(4), List.of()).contains(slot) ? "✔" : ""
            ));
        }


        timeSlotTable.getItems().setAll(rows);
    }

    public void clearTimeTable() {
        timeSlotTable.getItems().clear(); // Clears all the rows in the table
    }



    @FXML
    void onclickTSTable(MouseEvent event) {
        if (event.getClickCount() == 2 && timeSlotTable.getSelectionModel().getSelectedItem() != null) {
            TimeSlotRowTM selected = timeSlotTable.getSelectionModel().getSelectedItem();
            String selectedTime = selected.getTimeSlot();

            System.out.println("Selected time slot: " + selectedTime);

            TablePosition<?, ?> pos = timeSlotTable.getSelectionModel().getSelectedCells().get(0);
            int columnIndex = pos.getColumn();

            if (columnIndex >= 1 && columnIndex <= 5) {
                LocalDate selectedDate = nextFiveDates.get(columnIndex - 1);
                System.out.println("Selected date: " + selectedDate);
            }
        }
    }


//    List<TherapistAvailabilityDto> availability = therapistAvailabiltyBO.findByTherapistAndDate("T002", LocalDate.of(2025, 4, 22));
//    List<String> slots = availability.get(0).getAvailableSlots();
//
//        System.out.println("Slots:");
//        for (String slot : slots) {
//        System.out.println(slot);
//    }

}
