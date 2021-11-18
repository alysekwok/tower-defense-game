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

public class TowerComponent extends Component {
    private LocalTimer shootTimer;
    private TowerType type;
/*
    public TowerComponent(TowerType type) {
        this.type = type;
    }

 */

    @Override
    public void onAdded() {
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {

        if (shootTimer.elapsed(Duration.seconds(0.5))) {
            FXGL.getGameWorld()
                    .getClosestEntity(entity, e -> e.isType(TowerDefenseType.ENEMY))
                    .ifPresent(nearestEnemy -> {
                        shoot(nearestEnemy);
                        shootTimer.capture();
                    });
        }
    }

    private void shoot(Entity enemy) {
        Point2D position = getEntity().getPosition();

        Point2D direction = enemy.getPosition().subtract(position);

        Entity bullet = FXGL.spawn("Bullet", position);
        bullet.addComponent(new ProjectileComponent(direction, Config.BULLET_SPEED));
    }
}
