package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;

public class DashboardViewController {
    @FXML
    public AnchorPane homeAnchor;

    @FXML
    private ImageView imgLogOutBtn;

    @FXML
    private ImageView imgHomeBtn;

    @FXML
    private ImageView imgPatientsBtn;

    @FXML
    private ImageView imgBookingsBtn;

    @FXML
    private ImageView imgProgramsBtn;

    @FXML
    private ImageView imgTherapistsBtn;

    @FXML
    private ImageView imgSettingsBtn;

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

    @FXML
    void navigateBookingPage(MouseEvent event) {
        loadPage("appointment-view.fxml");
    }

    @FXML
    void navigatePatientsPage(MouseEvent event) {
        loadPage("patient-view.fxml");
    }

    @FXML
    void navigateProgramsPage(MouseEvent event) {
        loadPage("theraphy-program-view.fxml");
    }

    @FXML
    void navigateSettingsPage(MouseEvent event) {
        loadPage("setting-view.fxml");
    }

    @FXML
    void navigateTherapistsPage(MouseEvent event) {
        loadPage("therapists-view.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + fxmlFile));
            AnchorPane newPane = loader.load();

            homeAnchor.getChildren().setAll(newPane);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load " + fxmlFile);
        }
    }

    public void navigateToLoginPage(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login-view.fxml")));
            Stage stage = (Stage) imgLogOutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
