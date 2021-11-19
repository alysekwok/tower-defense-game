package corn;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.tiled.TiledMap;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import corn.collision.BulletEnemyHandler;
import corn.collision.MonumentEnemyHandler;
import corn.components.MonumentComponent;
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
    private Map<String, Object> values;

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
            private Rectangle2D mapBounds = new Rectangle2D(0, 0, 16 * 65, getAppHeight() - 100 - 40);
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
        values = vars;
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new TowerDefenseFactory());
        // getAssetLoader().loadJSON("json/map-5.json", TiledMap.class);
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
        getPhysicsWorld().addCollisionHandler(new MonumentEnemyHandler());
    }

    // TODO: this should be tower data
    private Color selectedColor = Color.BLACK;
    private int selectedIndex = 1;
    private TowerType selectedType = TowerType.FARMER;
    private String selectedText;

    @Override
    protected void initUI() {
       // Rectangle uiBG = new Rectangle(getAppWidth(), 100);
        // uiBG.setTranslateY(500);

        // getGameScene().addUINode(uiBG);

        for (int i = 0; i < 3; i++) {
            int index = i + 1;
            Color[] colors = {Color.PURPLE, Color.LIGHTGREEN, Color.LIGHTPINK};
            Color color = colors[i];
            String[] names = {"Bomber", "Farmer", "Ninja"};
            String[] prices = {"Cost: 30", "Cost: 10", "Cost: 50"};
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
            Text desc = getUIFactoryService().newText(names[i], Color.BLACK, 20);
            String name = names[i];
            desc.setTranslateX(1250);
            desc.setTranslateY(140 + i * 100);
            desc.setOnMouseClicked(e -> {
                selectedText = name;
                if (selectedText.equals(names[0])) {
                    showMessage("Corn Bomber does more damage but attacks slower. Cost: 30");
                }
                if (selectedText.equals(names[1])) {
                    showMessage("Corn Farmer protects its own. Shoots down enemies with the most standard bullets at the most" +
                            "standard speed. Cost: 10");
                }
                if (selectedText.equals(names[2])) {
                    showMessage("Corn Ninja is trained and can shoot faster with higher grade bullets. Cost: 50");
                }


            });
            Text price = getUIFactoryService().newText(prices[i], Color.BLACK, 20);
            price.setTranslateX(1250);
            price.setTranslateY(160 + i * 100);

            getGameScene().addUINodes(icon, desc, price);
        }
        var cow = FXGL.getAssetLoader().loadTexture("cow.PNG", 130, 130);
        cow.setTranslateX(1150);
        cow.setTranslateY(600);
        var bomber = FXGL.getAssetLoader().loadTexture("bomber.PNG", 80, 80);
        bomber.setTranslateX(1150);
        bomber.setTranslateY(120);
        var farmer = FXGL.getAssetLoader().loadTexture("farmer.PNG", 80, 80);
        farmer.setTranslateX(1150);
        farmer.setTranslateY(220);
        var ninja = FXGL.getAssetLoader().loadTexture("ninja.PNG", 80, 80);
        ninja.setTranslateX(1150);
        ninja.setTranslateY(320);

        var brutus = FXGL.getAssetLoader().loadTexture("brutus.PNG", 85, 125);
        brutus.setTranslateX(950);
        brutus.setTranslateY(610);

        FXGL.getGameScene().addUINodes(cow, bomber, farmer, ninja, brutus);

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
        spawn("Monument", 900, 710);
    }

    private void placeTower() {
        if (selectedColor == Color.PURPLE && ((int)values.get("money") >= 30)) {
            //inc("money", -30);
            //System.out.println(values.get("money"));
            values.replace("money", (int)values.get("money")-30);
            System.out.println(values.get("money"));
            spawn("TowerBomber",
                    new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                            .put("color", selectedColor)
                            // .put("type", selectedType)
                            .put("index", selectedIndex)
            );
        }
        if (selectedColor == Color.LIGHTGREEN && ((int)values.get("money") >= 10)) {
            values.replace("money", (int)values.get("money")-10);
            //System.out.println(values.get("money"));
            spawn("TowerFarmer",
                    new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                            .put("color", selectedColor)
                            // .put("type", selectedType)
                            .put("index", selectedIndex)
            );
        }
        if (selectedColor == Color.LIGHTPINK && ((int)values.get("money") >= 50)) {
            values.replace("money", (int)values.get("money")-50);
            //System.out.println(values.get("money"));
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
        values.replace("money", (int)values.get("money")+5);
        //System.out.println(values.get("money"));
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
