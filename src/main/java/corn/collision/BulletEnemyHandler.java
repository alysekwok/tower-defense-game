package corn.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import corn.TowerDefenseType;
import corn.event.EnemyKilledEvent;

public class BulletEnemyHandler extends CollisionHandler {
    public BulletEnemyHandler() {
        super(TowerDefenseType.BULLET, TowerDefenseType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        bullet.removeFromWorld();
        // add hp/damage system
        var hp = enemy.getComponent(HealthIntComponent.class);
        hp.damage(1);

        if (hp.isZero()) {
            FXGL.inc("money", +5);
            FXGL.getEventBus().fireEvent(new EnemyKilledEvent(enemy));
            enemy.removeFromWorld();
        }
    }
}
