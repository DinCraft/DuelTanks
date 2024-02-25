import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Bullet implements Serializable {
    float x;
    float y;
    float vx;
    float vy;
    float angle;
    float angle_const;
    boolean isIncreasing;
    String type;
    boolean isBlueTeam;

    Bullet(int x, int y, float vx, float vy, String type, boolean isBlueTeam){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.type = type;
        this.isBlueTeam = isBlueTeam;
        this.angle_const = (float)(Math.atan2(vy,vx)*(180/Math.PI)+180);
        if (type=="wavy1") {
            this.angle = -40;
            this.isIncreasing = false;
        }
        if (type=="wavy2") {
            this.angle = 40;
            this.isIncreasing = true;
        }
    }

    public void draw(Graphics g) {
        if (isBlueTeam) {
            g.setColor(Color.blue);
        }else{
            g.setColor(Color.red);
        }
        g.fillRect(Math.round(x-Player.x+475), Math.round(y-Player.y+275), 10, 10);
        move();
        if (type=="wavy1" || type=="wavy2") {
            changeAngle();
        }
    }

    public void move() {
        x+=vx;
        y+=vy;
    }

    public void changeAngle() {
        if (isIncreasing && angle>40) {
            isIncreasing = false;
        }
        if (!isIncreasing && angle<-40) {
            isIncreasing = true;
        }
        if (isIncreasing) {
            angle+=2;
        }else{
            angle-=2;
        }

        vx = (float)Math.cos((angle_const-180+angle)/(180/Math.PI))*10;
        vy = (float)Math.sin((angle_const-180+angle)/(180/Math.PI))*10;
    }
}