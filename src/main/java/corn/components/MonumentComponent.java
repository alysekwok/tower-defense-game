
package corn.components;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import corn.TowerDefenseType;
import corn.Config;
import corn.tower.TowerType;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class MonumentComponent extends Component {
    private static int health;

    public MonumentComponent() {
        health = 10;
    }

    public int getHealth() {
        return health;
    }

    public void subtractHealth(int damage) {
        health -= damage;
    }

}