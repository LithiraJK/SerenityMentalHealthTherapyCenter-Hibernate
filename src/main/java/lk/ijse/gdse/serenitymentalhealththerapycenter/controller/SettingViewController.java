package lk.ijse.gdse.serenitymentalhealththerapycenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class SettingViewController {
    @FXML
    private CheckBox checkBoxUserName;

    @FXML
    private JFXPasswordField txtCurrentPassword;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private JFXPasswordField txtConformNewPassword;

    @FXML
    private JFXTextField txtNewUserName;

    @FXML
    private JFXPasswordField txtUPassword;

    @FXML
    private Label lblUserName;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtConformPassword;

    @FXML
    private JFXButton btnAddUser;

    @FXML
    void btnAddUserOnAction(ActionEvent event) {

    }

    @FXML
    void btnSubmitUserOnAction(ActionEvent event) {

    }

}
