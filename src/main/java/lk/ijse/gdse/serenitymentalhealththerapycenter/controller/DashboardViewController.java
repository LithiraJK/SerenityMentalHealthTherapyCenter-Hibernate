package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lk.ijse.gdse.serenitymentalhealththerapycenter.util.NavigationUtil;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardViewController implements Initializable {

    @FXML
    public ImageView usersPageBtn;

    @FXML
    public ImageView therapistsPageBtn;
    @FXML
    public ImageView therapyProgramPageBtn;
    @FXML
    private HBox adminOnlyButtonBox;

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private VBox commonButtonBox;

    @FXML
    private AnchorPane homeAnchorPane;

    @FXML
    private ImageView imgBookingsBtn;

    @FXML
    private ImageView imgBookingsBtn1;

    @FXML
    private ImageView imgSettingsBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private ImageView patientProgramPageBtn;

    @FXML
    private ImageView patientsPageBtn;

    @FXML
    private ImageView paymentsPageBtn;

    @FXML
    private ImageView therapySessionPageBtn;

    @FXML
    private ImageView welcomePageBtn;

    @FXML
    private Label lblDateTime;

    NavigationUtil navigate = new NavigationUtil();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navigate.navigateTo(bodyPane, "/view/welcome-page.fxml");
        startDateTimeUpdater();
    }

    private void startDateTimeUpdater() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd | hh:mm a");

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            lblDateTime.setText(now.format(formatter));
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void loadPatientProgramPage(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/patient-program-page.fxml");
    }

    @FXML
    void loadPaymentsPageBtn(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/payments-page.fxml");
    }

    @FXML
    void loadTherapySessionsPage(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/therapy-sessions-page.fxml");
    }

    @FXML
    void loadWelcomePage(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/welcome-page.fxml");
    }

    @FXML
    void logOut(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Logout Confirmation");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                navigate.navigateBack(homeAnchorPane, "/view/login-page.fxml");
            }
        });
    }

    @FXML
    void loadPatientsPage(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/patients-page.fxml");
    }

    @FXML
    public void loadUsersPage(MouseEvent mouseEvent) {
        navigate.navigateTo(bodyPane, "/view/users-page.fxml");
    }

    @FXML
    public void loadTherapyProgramsPage(MouseEvent mouseEvent) {
        navigate.navigateTo(bodyPane, "/view/therapy-programs-page.fxml");
    }

    @FXML
    public void loadTherapistsPage(MouseEvent mouseEvent) {
        navigate.navigateTo(bodyPane, "/view/therapists-page.fxml");
    }

    @FXML
    void onMouseEnterdBtn(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(15);
            glow.setHeight(15);
            glow.setRadius(15);
            icon.setEffect(glow);
        }
    }

    @FXML
    void onMouseExitedBtn(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);
        }
    }

    public void setUserRole(String role) {
        boolean isAdmin = "Admin".equalsIgnoreCase(role);
        adminOnlyButtonBox.setVisible(isAdmin);
        commonButtonBox.setVisible(true);

        System.out.println("Admin access: " + isAdmin);
        System.out.println("Admin buttons visible: " + adminOnlyButtonBox.isVisible());
        System.out.println("Common buttons visible: " + commonButtonBox.isVisible());
    }
}