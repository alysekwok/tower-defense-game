package corntest;

import corn.TowerDefenseApp;
import corn.TowerDefenseFactory;
import corn.tower.TowerType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import com.almasb.fxgl.app.GameSettings;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TowerDefenseTest {

    @BeforeEach
    void setUp() {
        HashMap<String, Object> map = new HashMap<String, Object>();
    }

    @org.junit.jupiter.api.Test
    public void testSpawnEnemy1() {
        var spawnEnemy1 = new TowerDefenseFactory();
        //var hp = new HealthIntComponent(3);
        //spawnEnemy1.initGame();
        //shows mem addy, check count of enemies
        assertThat(spawnEnemy1, is(true)); //not true, shld be number
    }

    @org.junit.jupiter.api.Test
    void testGameOver() {
        //fix initialization error and everything shld be okay
        //bool
        var start = new TowerDefenseApp();
        start.initGame();
        assertEquals(start, true);
    }

    @org.junit.jupiter.api.Test
    void testMainMenuEnabled() {
        var start = new TowerDefenseApp();
        GameSettings settings = new GameSettings();
        //boolean value, can use assertEquals
        start.initSettings(settings);
        //assertEquals(start., true);
    }

    @org.junit.jupiter.api.Test
    void testMon() {
        var start = new TowerDefenseApp();
        assertEquals(start.getMon(), 0);

    }

    @org.junit.jupiter.api.Test
    void testLevelEnemies() {
        var start = new TowerDefenseApp();
        assertEquals(start.getLevelEnemies(), 25);
    }

    @org.junit.jupiter.api.Test
    void testTotalEnemies() {
        var start = new TowerDefenseApp();
        assertEquals(start.getTotalEnemies(), 25);
    }

    @org.junit.jupiter.api.Test
    void testEnemySpawnPoint() {
        var start = new TowerDefenseApp();
        assertEquals(start.getEnemySpawnPoint(), new Point2D(0, 100));
    }

    @org.junit.jupiter.api.Test
    void testValues() {
        var start = new TowerDefenseApp();
        assertEquals(start.getValues().get("money"), 100);
    }

    @org.junit.jupiter.api.Test
    void testMaxEnemies() {
        var start = new TowerDefenseApp();
        start.initGameVars(new HashMap<String, Object>() {
        });
        assertEquals(start.getMaxEnemies(), 5);
    }

    @org.junit.jupiter.api.Test
    void testSelectedColor() {
        var start = new TowerDefenseApp();
        assertEquals(start.getSelectedColor(), Color.BLACK);
    }

    @org.junit.jupiter.api.Test
    void testSelectedIndex() {
        var start = new TowerDefenseApp();
        assertEquals(start.getSelectedIndex(), 1);
    }

    @org.junit.jupiter.api.Test
    void testSelectedType() {
        var start = new TowerDefenseApp();
        assertEquals(start.getSelectedType(), TowerType.FARMER);
    }

    @org.junit.jupiter.api.Test
    void testSelectedText() {
        var start = new TowerDefenseApp();
        assertEquals(start.getSelectedText(), null);
    }

}