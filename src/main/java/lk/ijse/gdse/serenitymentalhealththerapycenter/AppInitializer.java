package lk.ijse.gdse.serenitymentalhealththerapycenter;

import lk.ijse.gdse.serenitymentalhealththerapycenter.config.FactoryConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class AppInitializer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(AppInitializer.class.getResourceAsStream("/images/Leonardo_Phoenix_09_A_modern_and_elegant_logo_for_Serenity_Men_0.jpg")));
        stage.setTitle("SMHTC");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        initializeDatabase();

        launch();
    }

    public static void initializeDatabase(){
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        transaction.commit();
        session.close();
    }


}