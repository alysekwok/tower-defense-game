package corn;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class WinScreen extends FXGLMenu {

    public WinScreen(){
        super(MenuType.MAIN_MENU);
        System.out.println("Nice") ;
        winScreen();
//        var button = new WinScreen.RestartButton("Restart", this:: fireNewGame);
    }

    public void winScreen() {
        getContentRoot().getChildren().setAll(new Pane());
        RestartButton restartButton = new WinScreen.RestartButton("Restart", this::fireNewGame);
        restartButton.setTranslateX(FXGL.getAppWidth() / 2 - 200 / 2);
        restartButton.setTranslateY(FXGL.getAppHeight() / 2 - 40 / 2 + 150);
        getContentRoot().getChildren().addAll(restartButton);
    }

    private static class RestartButton extends StackPane {
        public RestartButton(String restart, Runnable action) {
            var background = new Rectangle(200, 40);
            background.setStroke(Color.WHITE);
            var text = FXGL.getUIFactoryService().newText(restart, Color.WHITE, 18);
            background.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITE)
            );
            setOnMouseClicked(e -> action.run());
            getChildren().addAll(background, text);
        }
    }

}
