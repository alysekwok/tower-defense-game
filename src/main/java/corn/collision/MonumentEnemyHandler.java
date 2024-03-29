package corn.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import corn.TowerDefenseType;
import corn.event.EnemyKilledEvent;
import corn.event.EnemyReachedGoalEvent;

public class MonumentEnemyHandler extends CollisionHandler {
    public MonumentEnemyHandler() {
        super(TowerDefenseType.ENEMY, TowerDefenseType.MONUMENT);
    }

    @Override
    protected void onCollisionBegin(Entity enemy, Entity monument) {
        FXGL.getEventBus().fireEvent(new EnemyKilledEvent(enemy));
        var hp = monument.getComponent(HealthIntComponent.class);
        hp.damage(1);


        if (hp.isZero()|| enemy.getComponents().get(enemy.getComponents().size() - 1).toString().equals("Boss")) {
            FXGL.getEventBus().fireEvent(new EnemyReachedGoalEvent(monument));
        }
    }
}
