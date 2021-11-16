package corn;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import corn.collision.BulletEnemyHandler;
import corn.event.EnemyKilledEvent;
import corn.event.EnemyReachedGoalEvent;
import corn.tower.TowerIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TowerDefenseApp extends GameApplication {
    private int levelEnemies = 10;
    private Point2D enemySpawnPoint = new Point2D(50, 0);
    private List<Point2D> waypoints = new ArrayList<>();

    public List<Point2D> getWaypoints() {
        return new ArrayList<>(waypoints);
    }


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("CornTD");
        settings.setVersion("1.0");
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setIntroEnabled(false);
        settings.setProfilingEnabled(false);
        settings.setCloseConfirmation(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Place Tower") {
            private Rectangle2D worldBounds = new Rectangle2D(0, 0, getAppWidth(), getAppHeight() - 100 - 40);

            @Override
            protected void onActionBegin() {
                if (worldBounds.contains(input.getMousePositionWorld())) {
                    placeTower();
                }
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("numEnemies", levelEnemies);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new TowerDefenseFactory());

        // TODO: read this from external level data
        waypoints.addAll(Arrays.asList(
                new Point2D(700, 0),
                new Point2D(700, 300),
                new Point2D(50, 300),
                new Point2D(50, 450),
                new Point2D(700, 500)
        ));

        BooleanProperty enemiesLeft = new SimpleBooleanProperty();
        enemiesLeft.bind(getip("numEnemies").greaterThan(0));

        getGameTimer().runAtIntervalWhile(this::spawnEnemy, Duration.seconds(1), enemiesLeft);

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

    @Override
    protected void initUI() {
        Rectangle uiBG = new Rectangle(getAppWidth(), 100);
        uiBG.setTranslateY(500);

        getGameScene().addUINode(uiBG);

        for (int i = 0; i < 4; i++) {
            int index = i + 1;

            Color color = FXGLMath.randomColor();
            TowerIcon icon = new TowerIcon(color);
            icon.setTranslateX(10 + i * 100);
            icon.setTranslateY(500);
            icon.setOnMouseClicked(e -> {
                selectedColor = color;
                selectedIndex = index;
            });

            getGameScene().addUINode(icon);
        }
    }

    private void spawnEnemy() {
        inc("numEnemies", -1);

        spawn("Enemy", enemySpawnPoint.getX(), enemySpawnPoint.getY());
    }

    private void placeTower() {
        spawn("Tower",
                new SpawnData(getInput().getMouseXWorld(), getInput().getMouseYWorld())
                        .put("color", selectedColor)
                        .put("index", selectedIndex)
        );
    }

    private void onEnemyKilled(EnemyKilledEvent event) {
        levelEnemies--;

        if (levelEnemies == 0) {
            gameOver(true);
        }

        Entity enemy = event.getEnemy();
        Point2D position = enemy.getPosition();

        Text xMark = getUIFactoryService().newText("X", Color.RED, 24);
        xMark.setTranslateX(position.getX());
        xMark.setTranslateY(position.getY() + 20);

        getGameScene().addGameView(new GameView(xMark, 1000));
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
