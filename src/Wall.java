import java.awt.Color;
import java.awt.Graphics;

public class Wall {
    int x;
    int y;

    Wall(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, int px, int py) {
        g.setColor(Color.darkGray);
        g.fillRect(x-px+475, y-py+275, 100, 100);
    }
    
    public boolean collision(int px, int py) {
        return ((py<y+100) && (py+50>y) && (px<x+100) && (px+50>x));
    }
    public boolean bulletCollision(float bx, float by) {
        return ((by<y+100) && (by+10>y) && (bx<x+100) && (bx+10>x));
    }
}