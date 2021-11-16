package corn.event;

import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

public class EnemyKilledEvent extends Event {
    public static final EventType<EnemyKilledEvent> ANY
            = new EventType<>(Event.ANY, "ENEMY_KILLED");

    private Entity enemy;

    /**
     * @return killed enemy
     */
    public Entity getEnemy() {
        return enemy;
    }

    public EnemyKilledEvent(Entity enemy) {
        super(ANY);
        this.enemy = enemy;
    }

}
