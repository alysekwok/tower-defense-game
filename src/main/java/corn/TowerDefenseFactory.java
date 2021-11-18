package corn;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.ui.ProgressBar;
import corn.components.EnemyComponent;
import corn.components.enemy2;
import corn.components.TowerComponent;
import corn.components.enemy3;
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
        var hp = new HealthIntComponent(3);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(3);
        hpView.setWidth(50);
        hpView.setTranslateY(-30);
        hpView.setTranslateX(-25);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.ENEMY)
                .viewWithBBox(new Circle(15, Color.LIGHTSALMON))
                .view(hpView)
                .with(hp)
                .with(new CollidableComponent(true))
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("Enemy2")
    public Entity spawnEnemy2(SpawnData data) throws FileNotFoundException {
        var hp = new HealthIntComponent(2);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(2);
        hpView.setWidth(50);
        hpView.setTranslateY(-30);
        hpView.setTranslateX(-25);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.ENEMY)
                .viewWithBBox(new Circle(20, Color.AZURE))
                .with(new CollidableComponent(true))
                .view(hpView)
                .with(hp)
                .with(new enemy2())
                .build();
    }

    @Spawns("Enemy3")
    public Entity spawnEnemy3(SpawnData data) throws FileNotFoundException {
        var hp = new HealthIntComponent(2);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(2);
        hpView.setWidth(50);
        hpView.setTranslateY(-30);
        hpView.setTranslateX(-25);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.ENEMY)
                .viewWithBBox(new Circle(20, Color.CRIMSON))
                .with(new CollidableComponent(true))
                .view(hpView)
                .with(hp)
                .with(new enemy3())
                .build();
    }

    @Spawns("Tower")
    public Entity spawnTower(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .view(new Rectangle(40, 40, data.get("color")))
                // .view((String) data.get("type"))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent(TowerType.BOMBER))
                .with(new TowerComponent(TowerType.BOMBER))
                .build();
    }

    @Spawns("TowerFarmer")
    public Entity spawnTowerFarmer(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .view(new Rectangle(40, 40, data.get("color")))
                // .view((String) data.get("type"))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent(TowerType.FARMER))
                .with(new TowerComponent(TowerType.FARMER))
                .build();
    }

    @Spawns("TowerNinja")
    public Entity spawnTowerNinja(SpawnData data) {
        return entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .view(new Rectangle(40, 40, data.get("color")))
                // .view((String) data.get("type"))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent(TowerType.NINJA))
                .with(new TowerComponent(TowerType.NINJA))
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


}
