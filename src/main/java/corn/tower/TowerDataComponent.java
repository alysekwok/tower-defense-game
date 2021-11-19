package corn.tower;

import com.almasb.fxgl.entity.component.Component;

public class TowerDataComponent extends Component {
    private int hp;
    private int damage;
    private double attackDelay;

    public TowerDataComponent(TowerType type) {
        if (type == TowerType.NINJA) {
            hp = 2;
            damage = 1;
            attackDelay = 1.5;
        }
        if (type == TowerType.BOMBER) {
            hp = 8;
            damage = 3;
            attackDelay = 10;
        }
        if (type == TowerType.FARMER) {
            hp = 10;
            damage = 1;
            attackDelay = 5;
        }
    }


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
