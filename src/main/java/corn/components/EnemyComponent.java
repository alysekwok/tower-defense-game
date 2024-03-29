package corn.components;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import corn.TowerDefenseApp;
import javafx.geometry.Point2D;

import java.util.List;

public class EnemyComponent extends Component {
    private List<Point2D> waypoints;
    private Point2D nextWaypoint;
    private Entity monument;

    private double speed;
    private int hp;

    @Override
    public void onAdded() {
        waypoints = ((TowerDefenseApp) FXGL.getApp()).getWaypoints();

        nextWaypoint = waypoints.remove(0);
    }

    @Override
    public void onUpdate(double tpf) {
        speed = tpf * 60 * 2;

        Point2D velocity = nextWaypoint.subtract(entity.getPosition())
                .normalize()
                .multiply(speed);

        entity.translate(velocity);

        if (nextWaypoint.distance(entity.getPosition()) < speed) {
            entity.setPosition(nextWaypoint);

            if (!waypoints.isEmpty()) {
                nextWaypoint = waypoints.remove(0);
            } else {
                entity.removeFromWorld();
            }
        }
    }

}
