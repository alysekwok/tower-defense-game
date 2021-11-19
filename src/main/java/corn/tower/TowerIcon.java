package corn.tower;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TowerIcon extends Pane {
    private TowerType type;
    public TowerIcon(Color color) {
        getChildren().add(new Rectangle(80, 80, color));
    }
}
