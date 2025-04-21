package lk.ijse.gdse.serenitymentalhealththerapycenter.util;

import lk.ijse.gdse.serenitymentalhealththerapycenter.controller.DashboardViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationUtil {

    public void loginNavigation(String path, AnchorPane pane, String role){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane homePane = loader.load();

            DashboardViewController homeController = loader.getController();

            homeController.setUserRole(role);

            pane.getChildren().setAll(homePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateBack(AnchorPane currentPane, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane newPane = loader.load();

            Stage stage = (Stage) currentPane.getScene().getWindow();
            Scene scene = new Scene(newPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateTo(Pane basePane, String fxmlPath) {
        try {
            basePane.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            load.prefWidthProperty().bind(basePane.widthProperty());
            load.prefHeightProperty().bind(basePane.heightProperty());

            basePane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }


    public void navigatePopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane popupPane = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            Scene scene = new Scene(popupPane);
            popupStage.setScene(scene);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load popup!").show();
        }
    }



}
