package corn.event;
import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

public class EnemyReachedGoalEvent extends Event {

    private Entity monument;
    public static final EventType<EnemyReachedGoalEvent> ANY
            = new EventType<>(Event.ANY, "EnemyReachedGoalEvent");

    public EnemyReachedGoalEvent(Entity monument) {
        super(ANY);
        this.monument = monument;
    }

    public Entity getMonument() {
        return monument;
    }
}
