package corn;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.tiled.TiledMap;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import corn.collision.BulletEnemyHandler;
import corn.event.EnemyKilledEvent;
import corn.event.EnemyReachedGoalEvent;
import corn.tower.TowerIcon;
import corn.tower.TowerType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Orientation;


import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TowerDefenseApp extends GameApplication {
    private int levelEnemies = 25;
    private Point2D enemySpawnPoint = new Point2D(0, 100);
    private List<Point2D> waypoints = new ArrayList<>();
    public static final int WIDTH = 16 * 85;
    public static final int HEIGHT = 16 * 50;

    public List<Point2D> getWaypoints() {
        return new ArrayList<>(waypoints);
    }


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("CornTD");
        settings.setVersion("1.0");
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setIntroEnabled(false);
        settings.setProfilingEnabled(false);
        settings.setCloseConfirmation(false);
        settings.setMainMenuEnabled(true);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        Input input = getInput();
        input.addAction(new UserAction("Place Tower") {
            private Rectangle2D worldBounds = new Rectangle2D(0, 0, getAppWidth(), getAppHeight() - 100 - 40);
            private Rectangle2D mapBounds = new Rectangle2D(0, 0, 16 * 70, getAppHeight() - 100 - 40);
            @Override
            protected void onActionBegin() {
                if (mapBounds.contains(input.getMousePositionWorld())) {
                    placeTower();
                }
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("numEnemies", levelEnemies);
        vars.put("money", 100);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new TowerDefenseFactory());
        //getAssetLoader().loadJSON("json/map-5.json", TiledMap.class);
        // setLevelFromMap("map-5.json");
        // setLevelFromMap("map-5.tmx");
        // getGameWorld().setLevel(level);
        // FXGL.setLevelFromMap("map-5.tmx");
        Texture t = getAssetLoader().loadTexture("map-menu.png");

        entityBuilder()
                .view(new ScrollingBackgroundView(t.superTexture(t, HorizontalDirection.RIGHT),
                        Orientation.HORIZONTAL))
                .buildAndAttach();

        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight());


        // TODO: read this from external level data
        waypoints.addAll(Arrays.asList(
                new Point2D(970, 100),
                new Point2D(970, 565),
                new Point2D(130, 565),
                new Point2D(130, 710),
                new Point2D(900, 710)
        ));

        BooleanProperty enemiesLeft = new SimpleBooleanProperty();
        enemiesLeft.bind(getip("numEnemies").greaterThan(0));
        spawnMonument();
        getGameTimer().runAtIntervalWhile(this::spawnEnemy, Duration.seconds(1), enemiesLeft);
        getGameTimer().runAtIntervalWhile(this::spawnEnemy2, Duration.seconds(3), enemiesLeft);
        getGameTimer().runAtIntervalWhile(this::spawnEnemy3, Duration.seconds(2), enemiesLeft);
        getEventBus().addEventHandler(EnemyKilledEvent.ANY, this::onEnemyKilled);
        getEventBus().addEventHandler(EnemyReachedGoalEvent.ANY, e -> gameOver(false));

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
    }

    // TODO: this should be tower data
    private Color selectedColor = Color.BLACK;
    private int selectedIndex = 1;
    private TowerType selectedType = TowerType.FARMER;

    @Override
    protected void initUI() {
       // Rectangle uiBG = new Rectangle(getAppWidth(), 100);
        // uiBG.setTranslateY(500);

        // getGameScene().addUINode(uiBG);

        for (int i = 0; i < 3; i++) {
            int index = i + 1;
            Color[] colors = {Color.PURPLE, Color.LIGHTGREEN, Color.LIGHTPINK};
            Color color = colors[i];
            // TowerType[] towerTypes = {TowerType.FARMER, TowerType.COW, TowerType.NINJA, TowerType.BOMBER};
            // TowerType type = towerTypes[i];
            TowerIcon icon = new TowerIcon(color);
            icon.setTranslateX(1150);
            icon.setTranslateY(120 + i * 100);
            icon.setOnMouseClicked(e -> {
                // selectedType = type;
                selectedColor = color;
                selectedIndex = index;
            });
            getGameScene().addUINode(icon);
        }
        var monument = FXGL.getAssetLoader().loadTexture("cow.PNG", 130, 130);
        monument.setTranslateX(1150);
        monument.setTranslateY(600);

        FXGL.getGameScene().addUINode(monument);

        Text towerTitle = getUIFactoryService().newText("TOWERS", Color.BLACK, 35);
        towerTitle.setTranslateX(1150);
        towerTitle.setTranslateY(50);
        getGameScene().addUINode(towerTitle);

        Text moneyLabel = getUIFactoryService().newText("Money: ", Color.BLACK, 25);
        Text moneyValue = getUIFactoryService().newText("", Color.BLACK, 25);
        moneyLabel.setTranslateX(1160);
        moneyLabel.setTranslateY(80);
        moneyValue.setTranslateX(1250);
        moneyValue.setTranslateY(80);
        moneyValue.textProperty().bind(getWorldProperties().intProperty("money").asString());
        getGameScene().addUINodes(moneyLabel, moneyValue);
    }

    private void spawnEnemy() {
        inc("numEnemies", -1);
        spawn("Enemy", enemySpawnPoint.getX(), enemySpawnPoint.getY());
    }
    private void spawnEnemy2() {
        inc("numEnemies", -1);
        spawn("Enemy2", enemySpawnPoint.getX(), enemySpawnPoint.getY());
    }

    private void spawnEnemy3() {
        inc("numEnemies", -1);
        spawn("Enemy3", enemySpawnPoint.getX(), enemySpawnPoint.getY());
    }

    private void spawnMonument() {
        spawn("Monument", 990, 650);
    }

    private void placeTower() {
        if (selectedColor == Color.PURPLE) {
            spawn("TowerBomber",
                    new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                            .put("color", selectedColor)
                            // .put("type", selectedType)
                            .put("index", selectedIndex)
            );
        }
        if (selectedColor == Color.LIGHTGREEN) {
            spawn("TowerFarmer",
                    new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                            .put("color", selectedColor)
                            // .put("type", selectedType)
                            .put("index", selectedIndex)
            );
        }
        if (selectedColor == Color.LIGHTPINK) {
            spawn("TowerNinja",
                    new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                            .put("color", selectedColor)
                            // .put("type", selectedType)
                            .put("index", selectedIndex)
            );
        }
    }

    private void onEnemyKilled(EnemyKilledEvent event) {

        Entity enemy = event.getEnemy();
        Point2D position = enemy.getPosition();

        Text xMark = getUIFactoryService().newText("X", Color.RED, 24);
        xMark.setTranslateX(position.getX());
        xMark.setTranslateY(position.getY() + 20);
        getGameScene().addGameView(new GameView(xMark, 1000));

        levelEnemies--;

        if (levelEnemies == 0) {
            gameOver(true);
        }
    }

    private void gameOver(boolean won) {
        if (won) {
            showMessage("Congrats! You won", getGameController()::exit);
        } else {
            showMessage("lol u lost", getGameController()::exit);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
