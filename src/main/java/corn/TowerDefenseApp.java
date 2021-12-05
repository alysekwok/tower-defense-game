package corn;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import corn.collision.BulletEnemyHandler;
import corn.collision.MonumentEnemyHandler;
import corn.components.MonumentComponent;
import corn.event.EnemyKilledEvent;
import corn.event.EnemyReachedGoalEvent;
import corn.tower.TowerDataComponent;
import corn.tower.TowerIcon;
import corn.tower.TowerType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private static int mon = 0;
    private static int levelEnemies = 25;
    private static int totalEnemies = 25;
    private static int enemiesKilled = 0;
    private static int bulletsShot = 0;
    private static int moneySpent = 0;
    private Point2D enemySpawnPoint = new Point2D(0, 100);
    private List<Point2D> waypoints = new ArrayList<>();
    public static final int WIDTH = 16 * 85;
    public static final int HEIGHT = 16 * 50;
    private Map<String, Object> values;
    private static final int MAX_ENEMIES = 5;
    private Color selectedColor = Color.BLACK; //tower data
    private int selectedIndex = 1;
    private TowerType selectedType = TowerType.FARMER;
    private String selectedText;
    private MonumentComponent monument = new MonumentComponent();
    private boolean upgradeBomber = false;
    private boolean upgradeFarmer = false;
    private boolean upgradeNinja = false;


    public List<Point2D> getWaypoints() {

        return new ArrayList<>(waypoints);
    }

    public static int getMon() {

        return mon;
    }

    public static int getLevelEnemies() {

        return levelEnemies;
    }

    public static int getTotalEnemies() {

        return totalEnemies;
    }

    public Point2D getEnemySpawnPoint() {

        return enemySpawnPoint;
    }

    public Map<String, Object> getValues() {

        return values;
    }

    public static int getMaxEnemies() {

        return MAX_ENEMIES;
    }

    public Color getSelectedColor() {

        return selectedColor;
    }

    public int getSelectedIndex() {

        return selectedIndex;
    }

    public TowerType getSelectedType() {
        return selectedType;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void incrementBullet() {
        bulletsShot++;
    }

    public void increaseMoneySpent(int amount) {
        moneySpent += amount;
    }

    public static int getKilledEnemies() {
        return 1;
    }

    public static int getShotBullets() {
        return 1;
    }

    public static int getSpentMoney() {
        return 1;
    }

    public boolean restart() {
        return true;
    }

    public boolean exit() {
        return true;
    }

    @Override
    public void initSettings(GameSettings settings) {
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
    public void initInput() {
        Input input = getInput();
        input.addAction(new UserAction("Place Tower") {
            private Rectangle2D worldBounds = new
                    Rectangle2D(0, 0, getAppWidth(), getAppHeight() - 100 - 40);
            private Rectangle2D mapBounds = new
                    Rectangle2D(0, 0, 16 * 65, getAppHeight() - 100 - 40);
            @Override
            protected void onActionBegin() {
                if (mapBounds.contains(input.getMousePositionWorld())) {
                    placeTower();
                }
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    public void initGameVars(Map<String, Object> vars) {
        vars.put("numEnemies", levelEnemies);
        vars.put("money", mon);
        values = vars;
    }

    @Override
    public void initGame() {
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


        // read from external data
        waypoints.addAll(Arrays.asList(
                new Point2D(970, 100),
                new Point2D(970, 565),
                new Point2D(130, 565),
                new Point2D(130, 710),
                new Point2D(900, 710)
        ));
    }

    public void startWave() {
        BooleanProperty enemiesLeft = new SimpleBooleanProperty();
        enemiesLeft.bind(getip("numEnemies").greaterThan(0));
        spawnMonument();
        getGameTimer().runAtIntervalWhile(this::spawnEnemy, Duration.seconds(1), enemiesLeft);
        getGameTimer().runAtIntervalWhile(this::spawnEnemy2, Duration.seconds(3), enemiesLeft);
        getGameTimer().runAtIntervalWhile(this::spawnEnemy3, Duration.seconds(2), enemiesLeft);
        getGameTimer().runOnceAfter(this::spawnBoss, Duration.seconds(14));
        getEventBus().addEventHandler(EnemyKilledEvent.ANY, this::onEnemyKilled);
        getEventBus().addEventHandler(EnemyReachedGoalEvent.ANY, e -> gameOver(false));
    }

    @Override
    public void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new MonumentEnemyHandler());
    }

    @Override
    public void initUI() {
        for (int i = 0; i < 3; i++) {
            int index = i + 1;
            Color[] colors = {Color.PURPLE, Color.LIGHTGREEN, Color.LIGHTPINK};
            Color color = colors[i];
            String[] names = {"Bomber", "Farmer", "Ninja"};
            String[] prices = {"Cost: 30", "Cost: 10", "Cost: 50"};
            TowerIcon icon = new TowerIcon(color);
            icon.setTranslateX(1150);
            icon.setTranslateY(120 + i * 100);
            icon.setOnMouseClicked(e -> {
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
                    showMessage("Corn Farmer protects its own."
                            + "Shoots down enemies with the most standard bullets at the most"
                            + "standard speed. Cost: 10");
                }
                if (selectedText.equals(names[2])) {
                    showMessage("Corn Ninja is trained"
                            + "and can shoot faster with higher grade bullets. Cost: 50");
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

        CornTDButton startButton = new CornTDButton("Start Combat", this::startWave);
        startButton.setTranslateX(100);
        startButton.setTranslateY(25);
        getGameScene().addUINode(startButton);

        SmallCornTDButton upgrade1 = new SmallCornTDButton("Upgrade",this::upgradeTower1 );
        upgrade1.setTranslateX(1250);
        upgrade1.setTranslateY(170);
        SmallCornTDButton upgrade2 = new SmallCornTDButton("Upgrade",this::upgradeTower2 );
        upgrade2.setTranslateX(1250);
        upgrade2.setTranslateY(270);
        SmallCornTDButton upgrade3 = new SmallCornTDButton("Upgrade",this::upgradeTower3 );
        upgrade3.setTranslateX(1250);
        upgrade3.setTranslateY(370);
        getGameScene().addUINodes(upgrade1, upgrade2, upgrade3);


    }

    private void spawnEnemy() {
        inc("numEnemies", -1);
        totalEnemies--;
        if (totalEnemies >= 0) {
            spawn("Enemy", enemySpawnPoint.getX(), enemySpawnPoint.getY());

        }
    }

    private void spawnEnemy2() {
        inc("numEnemies", -1);
        totalEnemies--;
        if (totalEnemies >= 0) {
            spawn("Enemy2", enemySpawnPoint.getX(), enemySpawnPoint.getY());

        }
    }

    private void spawnEnemy3() {
        inc("numEnemies", -1);
        totalEnemies--;
        if (totalEnemies >= 0) {
            spawn("Enemy3", enemySpawnPoint.getX(), enemySpawnPoint.getY());
        }
    }

    private void spawnBoss() {
        inc("numEnemies", -1);
        totalEnemies--;
        if (totalEnemies >= 0) {
            spawn("boss", enemySpawnPoint.getX(), enemySpawnPoint.getY());
        }
    }

    private void spawnMonument() {
        spawn("Monument", 900, 710);
    }

    private void placeTower() {
        if (selectedColor == Color.PURPLE && ((int) values.get("money") >= 30)) {
            if (!upgradeBomber) {
                values.replace("money", (int) values.get("money") - 30);
                spawn("TowerBomber",
                        new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                                .put("color", selectedColor)
                                // .put("type", selectedType)
                                .put("index", selectedIndex)
                );
            } else {
                values.replace("money", (int) values.get("money") - 30);
                spawn("UpgradeTowerBomber",
                        new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                                .put("color", selectedColor)
                                // .put("type", selectedType)
                                .put("index", selectedIndex)
                );

            }

        }
        if (selectedColor == Color.LIGHTGREEN && ((int) values.get("money") >= 10)) {
            if (!upgradeFarmer) {
                values.replace("money", (int) values.get("money") - 10);
                //System.out.println(values.get("money"));
                spawn("TowerFarmer",
                        new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                                .put("color", selectedColor)
                                // .put("type", selectedType)
                                .put("index", selectedIndex)
                );
            } else {
                values.replace("money", (int) values.get("money") - 10);
                //System.out.println(values.get("money"));
                spawn("UpgradeTowerFarmer",
                        new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                                .put("color", selectedColor)
                                // .put("type", selectedType)
                                .put("index", selectedIndex)
                );
            }

        }
        if (selectedColor == Color.LIGHTPINK && ((int) values.get("money") >= 50)) {
            if (!upgradeNinja) {
                values.replace("money", (int) values.get("money") - 50);
                //System.out.println(values.get("money"));
                spawn("TowerNinja",
                        new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                                .put("color", selectedColor)
                                // .put("type", selectedType)
                                .put("index", selectedIndex)
                );
            } else {
                values.replace("money", (int) values.get("money") - 50);
                //System.out.println(values.get("money"));
                spawn("UpgradeTowerNinja",
                        new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                                .put("color", selectedColor)
                                // .put("type", selectedType)
                                .put("index", selectedIndex)
                );
            }

        }
    }

    private void upgradeTower1() {
        values.replace("money", (int) values.get("money") - 10);
        upgradeBomber = true;

    }
    private void upgradeTower2() {
        values.replace("money", (int) values.get("money") - 10);
        upgradeFarmer = true;

    }
    private void upgradeTower3() {
        values.replace("money", (int) values.get("money") - 10);
        upgradeNinja = true;

    }



    private void onEnemyKilled(EnemyKilledEvent event) {
        Entity enemy = event.getEnemy();
        Point2D position = enemy.getPosition();

        Text xMark = getUIFactoryService().newText("X", Color.RED, 24);
        xMark.setTranslateX(position.getX());
        xMark.setTranslateY(position.getY() + 20);
        values.replace("money", (int) values.get("money") + 5);
        getGameScene().addGameView(new GameView(xMark, 1000));

        enemiesKilled++;
        levelEnemies--;

        if (levelEnemies == 0) {
            gameOver(true);
        }
    }
    
    private void gameOver(boolean won) {
        getGameController().pauseEngine();
        if (won) {
            showMessage("Congrats! You won!" + "\nStatistics:\nEnemies Killed: "
                    + enemiesKilled + "\nBullets Shot:"
                    + bulletsShot + "\nMoney Spent: "
                    + moneySpent);
        } else {
            showMessage("You lost. Better luck next time!" + "\nStatistics:\nEnemies Killed: "
                    + enemiesKilled + "\nBullets Shot:"
                    + bulletsShot + "\nMoney Spent: "
                    + moneySpent);
        }
        CornTDButton restart = new CornTDButton("Restart", getGameController()::gotoMainMenu);
        restart.setTranslateX(FXGL.getAppWidth() / 2);
        restart.setTranslateY(FXGL.getAppHeight() / 2 - 50);
        getGameScene().addUINode(restart);
        CornTDButton exit = new CornTDButton("Exit", getGameController()::exit);
        exit.setTranslateX(FXGL.getAppWidth() / 2);
        exit.setTranslateY(FXGL.getAppHeight() / 2 - 10);
        getGameScene().addUINode(exit);
    }
    public static void setDifficulty(int num) {
        if (num == 0) {
            mon = 100;
        } else if (num == 1) {
            mon = 80;
        } else {
            mon = 60;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    private static class CornTDButton extends StackPane {
        public CornTDButton(String name, Runnable action) {
            var rect = new Rectangle(200, 40);
            rect.setStroke(Color.WHITE);
            var text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 18);
            rect.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITE)
            );
            setOnMouseClicked(e -> action.run());
            getChildren().addAll(rect, text);
        }
    }

    private static class SmallCornTDButton extends StackPane {
        public SmallCornTDButton(String name, Runnable action) {
            var rect = new Rectangle(70, 20);
            rect.setStroke(Color.WHITE);
            var text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 10);
            rect.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITE)
            );
            setOnMouseClicked(e -> action.run());
            getChildren().addAll(rect, text);
        }
    }

}
