package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.UserBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.impl.UserBOImpl;

import java.io.IOException;

public class LoginController {

    private final UserBO userBO = new UserBOImpl();

    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private Label lblFogotPassword;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXPasswordField loginPasswordText;

    @FXML
    private JFXTextField loginUsernameText;

    @FXML
    void loginValidate(ActionEvent event) {
        String username = loginUsernameText.getText();
        String password = loginPasswordText.getText();

        // Reset styles
        loginUsernameText.setStyle("-fx-border-color: transparent;");
        loginPasswordText.setStyle("-fx-border-color: transparent;");

        if (userBO.isUserExists(username, password)) {
            String role = userBO.getUserRole(username, password); // Retrieve user role
            loadDashboard("/view/dashboard-view.fxml", role);
        } else {
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Invalid username or password");
            alert.setContentText("Please check your credentials and try again.");
            alert.showAndWait();

            // Reset input fields
            loginUsernameText.clear();
            loginPasswordText.clear();
            loginUsernameText.setFocusColor(Paint.valueOf("red"));
            loginPasswordText.setFocusColor(Paint.valueOf("red"));
        }
    }



    private void loadDashboard(String fxmlFile, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) loginUsernameText.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));

            // Pass the role to the dashboard controller
            DashboardViewController controller = loader.getController();
            controller.setUserRole(role);

            // Debug message to verify role
            System.out.println("User logged in with role: " + role);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
