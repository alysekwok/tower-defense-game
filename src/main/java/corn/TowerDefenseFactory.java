package corn;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.ui.ProgressBar;
import corn.components.*;
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

    @Spawns("TowerBomber")
    public Entity spawnTowerBomber(SpawnData data) throws FileNotFoundException {
        FileInputStream temp1 = new FileInputStream("./src/main/resources/assets/textures/bomber.PNG");
        ImageView image1 = new ImageView(new Image(temp1));
        image1.preserveRatioProperty();
        image1.setFitWidth(70);
        image1.setFitHeight(70);

        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .viewWithBBox((image1))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent(TowerType.BOMBER))
                .with(new TowerComponent(TowerType.BOMBER))
                .build();
    }

    @Spawns("TowerFarmer")
    public Entity spawnTowerFarmer(SpawnData data) throws FileNotFoundException {
        FileInputStream temp2 = new FileInputStream("./src/main/resources/assets/textures/farmer.PNG");
        ImageView image2 = new ImageView(new Image(temp2));
        image2.preserveRatioProperty();
        image2.setFitWidth(70);
        image2.setFitHeight(70);

        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .viewWithBBox((image2))
                .with(new CollidableComponent(true))
                .with(new TowerDataComponent(TowerType.FARMER))
                .with(new TowerComponent(TowerType.FARMER))
                .build();
    }

    @Spawns("TowerNinja")
    public Entity spawnTowerNinja(SpawnData data) throws FileNotFoundException {
        FileInputStream temp3 = new FileInputStream("./src/main/resources/assets/textures/ninja.PNG");
        ImageView image3 = new ImageView(new Image(temp3));
        image3.preserveRatioProperty();
        image3.setFitWidth(70);
        image3.setFitHeight(70);

        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.TOWER)
                .viewWithBBox((image3))
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

    @Spawns("Monument")
    public Entity spawnMonument(SpawnData data) throws FileNotFoundException {
        var hp = new HealthIntComponent(10);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(10);
        hpView.setWidth(50);
        hpView.setTranslateY(20);
        hpView.setTranslateX(50);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return FXGL.entityBuilder(data)
                .type(TowerDefenseType.ENEMY)
                .viewWithBBox(new Circle(20, Color.ROSYBROWN))
                .with(new CollidableComponent(true))
                .view(hpView)
                .with(hp)
                .with(new MonumentComponent())
                .build();
    }

/*
    @Spawns("TowerBomber")
    public Entity spawnTowerBomber(SpawnData data) {
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

     */


}
