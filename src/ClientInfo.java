import java.io.Serializable;

public class ClientInfo implements Serializable {
    boolean moveLeft, moveRight, moveUp, moveDown;
    int angle;
    int mana;
    double vx,vy;
    boolean shoot;
    int type;

    ClientInfo(){
        this.moveLeft = Game2.isAPressed;
        this.moveRight = Game2.isDPressed;
        this.moveUp = Game2.isWPressed;
        this.moveDown = Game2.isSPressed;
        this.angle = Game2.angle;
        this.shoot = Game2.shoot;
        this.vx = Game2.vx;
        this.mana = Game2.mana;
        this.vy = Game2.vy;
        this.type = Game2.chosenBulletType;
        if (Game2.shoot==true) {
            Game2.shoot = false;
        }
    }
}