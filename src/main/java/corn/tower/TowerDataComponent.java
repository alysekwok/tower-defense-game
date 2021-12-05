package corn.tower;

import com.almasb.fxgl.entity.component.Component;

public class TowerDataComponent extends Component {


    private int hp;
    private int damage;
    private double attackDelay;
    private boolean upgrade;

    public TowerDataComponent(TowerType type, boolean upgrade) {
        if (type == TowerType.NINJA) {
            if (!upgrade) {
                hp = 2;
                damage = 1;
                attackDelay = 1.5;
            } else {
                hp = 3;
                damage = 3;
                attackDelay = 1;
            }

        }
        if (type == TowerType.BOMBER) {
            if (!upgrade) {
                hp = 8;
                damage = 3;
                attackDelay = 10;
            } else {
                hp = 10;
                damage = 5;
                attackDelay = 8;
            }

        }
        if (type == TowerType.FARMER) {
            if (!upgrade) {
                hp = 10;
                damage = 1;
                attackDelay = 5;
            } else {
                hp = 5;
                damage = 2;
                attackDelay = 3;
            }


        }
    }



    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setAttackDelay(double attackDelay) {
        this.attackDelay = attackDelay;
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
