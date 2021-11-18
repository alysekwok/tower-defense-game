package corn.tower;

import com.almasb.fxgl.texture.Texture;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TowerIcon extends Pane {
    private TowerType type;


    public TowerIcon(Color color) {
        getChildren().add(new Rectangle(80, 80, color));
    }
/*
    public TowerIcon(TowerType type) throws FileNotFoundException {
        if (type == TowerType.FARMER) {
            FileInputStream temp = new FileInputStream("./src/main/resources/assets/textures/farmer.PNG");
            ImageView image = new ImageView(new Image(temp));
            getChildren().add(image);

        }
        if (type == TowerType.BOMBER) {
            FileInputStream temp = new FileInputStream("./src/main/resources/assets/textures/bomber.PNG");
            ImageView image = new ImageView(new Image(temp));
            getChildren().add(image);
        }
        if (type == TowerType.NINJA) {
            FileInputStream temp = new FileInputStream("./src/main/resources/assets/textures/ninja.PNG");
            ImageView image = new ImageView(new Image(temp));
            getChildren().add(image);
        }
 */



}
