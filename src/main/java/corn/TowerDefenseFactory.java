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
import corn.tower.TowerType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import com.almasb.fxgl.dsl.FXGL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class TowerDefenseFactory implements EntityFactory {
    @Spawns("Enemy")
    public Entity spawnEnemy(SpawnData data) throws FileNotFoundException {


        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.ENEMY)
                .viewWithBBox(new Circle(15, Color.LIGHTSALMON))
                .with(new CollidableComponent(true))
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("Tower")
    public Entity spawnTower(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .view(new Rectangle(40, 40, data.get("color")))
                // .view((String) data.get("type"))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent())
                .with(new TowerComponent())
                .build();
    }
/*
    @Spawns("TowerBomber")
    public Entity spawnTowerBomber(SpawnData data) throws FileNotFoundException {
        FileInputStream temp = new FileInputStream("./src/main/resources/assets/textures/bomber.PNG");
        ImageView image = new ImageView(new Image(temp));
        image.preserveRatioProperty();
        image.setFitWidth(70);
        image.setFitHeight(70);

        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .viewWithBBox((image))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent())
                .with(new TowerComponent(TowerType.BOMBER))
                .build();
    }

    @Spawns("TowerFarmer")
    public Entity spawnTowerFarmer(SpawnData data) throws FileNotFoundException {
        FileInputStream temp = new FileInputStream("./src/main/resources/assets/textures/farmer.PNG");
        ImageView image = new ImageView(new Image(temp));
        image.preserveRatioProperty();
        image.setFitWidth(70);
        image.setFitHeight(70);

        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .viewWithBBox((image))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent())
                .with(new TowerComponent(TowerType.FARMER))
                .build();
    }

     */

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
