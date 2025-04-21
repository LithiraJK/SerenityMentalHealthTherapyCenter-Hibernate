package lk.ijse.gdse.serenitymentalhealththerapycenter.util;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class SetBackgroundUtil {
    public void setBackgroundImage(Pane pane, int width, int height) {
        Image backgroundImage = new Image(
                getClass().getResource("/images/rm222batch2-mind-03.jpg").toExternalForm()
        );
        BackgroundSize backgroundSize = new BackgroundSize(
                width, height, true, true, false, true
        );
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );
        pane.setBackground(new Background(bgImage));
    }
}
