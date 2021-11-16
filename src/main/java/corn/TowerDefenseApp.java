package corn;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TowerDefenseApp extends GameApplication {
    private IntegerProperty numEnemies;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("CornTD");
        settings.setVersion("1.0");
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {

    }

    @Override
    protected void initGame() {
        numEnemies = new SimpleIntegerProperty(10);



    }


}
