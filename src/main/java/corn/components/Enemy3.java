package corn.components;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import corn.TowerDefenseApp;
import javafx.geometry.Point2D;

import java.util.List;

public class Enemy3 extends Component {
    private List<Point2D> waypoints;
    private Point2D nextWaypoint;
    private MonumentComponent monument = new MonumentComponent();

    private double speed;

    @Override
    public void onAdded() {
        waypoints = ((TowerDefenseApp) FXGL.getApp()).getWaypoints();

        nextWaypoint = waypoints.remove(0);
    }

    @Override
    public void onUpdate(double tpf) {
        speed = tpf * 60 * 1.5;

        Point2D velocity = nextWaypoint.subtract(entity.getPosition())
                .normalize()
                .multiply(speed);

        entity.translate(velocity);

        if (nextWaypoint.distance(entity.getPosition()) < speed) {
            entity.setPosition(nextWaypoint);

            if (!waypoints.isEmpty()) {
                nextWaypoint = waypoints.remove(0);
            }
        }
    }

}
