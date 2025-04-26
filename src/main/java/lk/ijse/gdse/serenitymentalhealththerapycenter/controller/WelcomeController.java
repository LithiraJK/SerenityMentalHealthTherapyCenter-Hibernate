package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.gdse.serenitymentalhealththerapycenter.util.NavigationUtil;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    NavigationUtil navigate = new NavigationUtil();

    @FXML
    private VBox addPatient;

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private VBox proccesPayment;

    @FXML
    private VBox sessions;

    @FXML
    void addPatientOnClicked(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/patients-page.fxml");

    }

    @FXML
    void proccesPaymentOnClicked(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/payments-page.fxml");

    }

    @FXML
    void sessionsOnClicked(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/therapy-sessions-page.fxml");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}

