package corn;

import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import corn.components.EnemyComponent;
import corn.components.TowerComponent;
import corn.tower.TowerDataComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class TowerDefenseFactory implements EntityFactory {
    @Spawns("Enemy")
    public Entity spawnEnemy(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.ENEMY)
                .viewWithBBox(new Rectangle(40, 40, Color.RED))
                .with(new CollidableComponent(true))
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("Tower")
    public Entity spawnTower(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .view(new Rectangle(40, 40, data.get("color")))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent())
                .with(new TowerComponent())
                .build();
    }

    @Spawns("Bullet")
    public Entity spawnBullet(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.BULLET)
                .viewWithBBox(new Rectangle(15, 5, Color.DARKGREY))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }
}
