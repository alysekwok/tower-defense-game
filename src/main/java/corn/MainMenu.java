package corn;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.function.Predicate;

public class MainMenu extends FXGLMenu {

    public MainMenu() {
        super(MenuType.MAIN_MENU);
        startMenu();
    }

    public void startMenu() {
        getContentRoot().getChildren().setAll(new Pane());
        CornTDButton startButton = new CornTDButton("Play", this::configMenu);
        startButton.setTranslateX(FXGL.getAppWidth() / 2 - 200 / 2);
        startButton.setTranslateY(FXGL.getAppHeight() / 2 - 40 / 2 + 150);
        var logo = FXGL.getAssetLoader().loadTexture("logo.PNG", 500, 420);
        logo.setTranslateX(FXGL.getAppWidth() / 2 - 200 / 2 - 150);
        logo.setTranslateY(FXGL.getAppHeight() / 2 - 40 / 2 - 300);
        getContentRoot().getChildren().addAll(logo, startButton);
    }
    public void configMenu() {
        getContentRoot().getChildren().setAll(new Pane());

        FXGL.getDialogService().showInputBox("Enter your name: ", SANITIZE_NAME, name -> {
            //GameVars.setName(name);
        });

        ToggleGroup difficulty = new ToggleGroup();
        RadioButton easy = new RadioButton("easy");
        easy.setToggleGroup(difficulty);
        easy.setOnAction((ActionEvent action) -> TowerDefenseApp.setDifficulty(0));
        RadioButton medium = new RadioButton("medium");
        medium.setToggleGroup(difficulty);
        medium.setOnAction((ActionEvent action) -> TowerDefenseApp.setDifficulty(1));
        RadioButton hard = new RadioButton("hard");
        hard.setToggleGroup(difficulty);
        hard.setOnAction((ActionEvent action) -> TowerDefenseApp.setDifficulty(2));

        VBox gameDifficulty = new VBox();
        gameDifficulty.getChildren().addAll(easy, medium, hard);

        var startButton = new CornTDButton("Start", this::fireNewGame);

        VBox vbox = new VBox(50);
        vbox.getChildren().addAll(gameDifficulty, startButton);
        vbox.setTranslateX(FXGL.getAppWidth() / 2);
        vbox.setTranslateY(FXGL.getAppHeight() / 2 - 20);

        getContentRoot().getChildren().add(vbox);
    }

    private static final Predicate<String> SANITIZE_NAME = (str) -> str.matches("\\S+");

    private static class CornTDButton extends StackPane {
        public CornTDButton(String name, Runnable action) {
            var rect = new Rectangle(200, 40);
            rect.setStroke(Color.WHITE);
            var text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 18);
            rect.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITE)
            );
            setOnMouseClicked(e -> action.run());
            getChildren().addAll(rect, text);
        }
    }
}