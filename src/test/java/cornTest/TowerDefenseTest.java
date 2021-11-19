package cornTest;

import com.almasb.fxgl.app.GameApplication;
import corn.TowerDefenseApp;
import corn.TowerDefenseFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import com.almasb.fxgl.app.GameSettings;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

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
        //boolean value, can use assertEquals
    }

}