package corn.tower;

import com.almasb.fxgl.entity.component.Component;

public class TowerDataComponent extends Component {
    private int hp = 10;
    private int damage = 1;
    private double attackDelay = 1.5;


    public int getHP() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public double getAttackDelay() {
        return attackDelay;
    }
}
