package corn.collision;

import com.almasb.fxgl.dsl.FXGL;
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

        // TODO: add HP/Damage system
        FXGL.getEventBus().fireEvent(new EnemyKilledEvent(enemy));
        enemy.removeFromWorld();
    }
}
