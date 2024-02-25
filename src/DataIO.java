import java.io.Serializable;
import java.util.List;

public class DataIO implements Serializable {
    int pX, pY, p2X, p2Y;
    int pAngle,p2Angle;
    int p2Cooldown;
    int p1Score, p2Score;
    int pHealth, p2Health, pMana, p2Mana;
    List<Bullet> bullets;

    DataIO(int pX, int pY, int p2X, int p2Y){
        this.pX = pX;
        this.pY = pY;
        this.p2X = p2X;
        this.p2Y = p2Y;
        this.bullets = Game.bullets;
        this.pAngle = Player.angle;
        this.p2Angle = Player2.angle;
        this.pHealth = Math.round(Player.health);
        this.p2Health = Math.round(Player2.health);
        this.pMana = Math.round(Player.mana);
        this.p2Mana = Math.round(Player2.mana);
        this.p2Cooldown = Player2.cooldown;
        this.p1Score = Game.p1Score;
        this.p2Score = Game.p2Score;
    }
}