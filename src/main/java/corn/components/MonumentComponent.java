
package corn.components;
import com.almasb.fxgl.entity.component.Component;

public class MonumentComponent extends Component {
    private static int health;

    public MonumentComponent() {
        health = 10;
    }

    public int getHealth() {
        return health;
    }

    public void subtractHealth(int damage) {
        health -= damage;
    }

}