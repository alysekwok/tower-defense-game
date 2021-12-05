package corntest;
import corn.TowerDefenseApp;
import corn.components.Boss;
import corn.components.TowerComponent;
import corn.tower.TowerType;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DemoTest {
    @BeforeEach
    void setUp() {
        HashMap<String, Object> map = new HashMap<String, Object>();
    }

    @org.junit.jupiter.api.Test
    void testEnemiesKilled() {
        var gameOver = new TowerDefenseApp();
        boolean b = (gameOver.getKilledEnemies() != 0);
        assertTrue(b);
    }
    @org.junit.jupiter.api.Test
    void testBulletsShot() {
        var gameOver = new TowerDefenseApp();
        boolean b = (gameOver.getShotBullets() != 0);
        assertTrue(b);
    }
    @org.junit.jupiter.api.Test
    void gameOverRestart() {
        var gameOver = new TowerDefenseApp();
        assertThat(gameOver.restart(), is(true));
    }
    @org.junit.jupiter.api.Test
    void gameOverExit() {
        var gameOver = new TowerDefenseApp();
        assertThat(gameOver.exit(), is(true));
    }
    @org.junit.jupiter.api.Test
    void testMoneySpent() {
        var gameOver = new TowerDefenseApp();
        boolean b = (gameOver.getSpentMoney() != 0);
        assertTrue(b);
    }

    @org.junit.jupiter.api.Test
    void testUpgradeBomberTower() {
        var towerComponent = new TowerComponent(TowerType.BOMBER);
        towerComponent.update(1.0);
        assertEquals(towerComponent.increaseShootTimer(), true);
    }

    @org.junit.jupiter.api.Test
    void testUpgradeFarmerTower() {
        var towerComponent = new TowerComponent(TowerType.FARMER);
        towerComponent.update(3.0);
        assertEquals(towerComponent.increaseShootTimer(), true);
    }

    @org.junit.jupiter.api.Test
    void testUpgradeNinjaTower() {
        var towerComponent = new TowerComponent(TowerType.NINJA);
        towerComponent.update(2.0);
        assertEquals(towerComponent.increaseShootTimer(), true);
    }

    @org.junit.jupiter.api.Test
    void finalBoss() {
        var boss = new Boss();
        assertThat(boss.exists(), is(true));
    }

    @org.junit.jupiter.api.Test
    void updateFinalBoss() {
        var boss = new Boss();
        boss.update(2.0);
        assertEquals(boss.increaseSpeed(), true);
    }

}
