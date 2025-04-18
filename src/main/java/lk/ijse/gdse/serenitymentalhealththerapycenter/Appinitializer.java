package lk.ijse.gdse.serenitymentalhealththerapycenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.BOFactory;
import lk.ijse.gdse.serenitymentalhealththerapycenter.bo.custom.UserBO;
import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import org.hibernate.Session;

import java.util.Objects;

public class Appinitializer extends Application {

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login-view.fxml")))));
        userBO.initializeAdmin();
        stage.show();
        stage.resizableProperty().setValue(Boolean.FALSE);
        Session session = FactoryConfiguration.getInstance().getSession();
        session.close();
    }
}
