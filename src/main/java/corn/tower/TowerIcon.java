package corn.tower;

import com.almasb.fxgl.texture.Texture;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TowerIcon extends Pane {

    public TowerIcon(Color color) {
        getChildren().add(new Rectangle(80, 80, color));

    }
}
