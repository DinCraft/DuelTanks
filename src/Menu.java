import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Menu extends JPanel {
    ImageIcon background;
    Timer timer;

    Menu(){
        init();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background.getImage(), 0, 0, this);
        Util.drawCenteredString(g, "Duel Tanks", new Rectangle(500,100,0,0), new Font("Comic Sans MS Bold",0,72), Color.BLACK);
    }

    public void init() {
        background = new ImageIcon("img/intro_bc.png");
        repaint();
    }
}