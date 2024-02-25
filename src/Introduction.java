import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Introduction extends JPanel implements ActionListener {
    Timer timer;
    ImageIcon background;
    int tick = -10;
    String text = "DinCraft Company";
    String shown_text;
    int s = 0;

    Introduction(){
        background = new ImageIcon("img/intro_bc.png");
        timer = new Timer(25, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background.getImage(), 0, 0, this);
        if (tick%5==0) {
            s++;
        }
        if (s<=text.length()) {
            shown_text = text.substring(0, s);
        }
        Util.drawCenteredString(g, shown_text, new Rectangle(500,300,0,0), new Font("Roboto", 0, 100), Color.BLACK);
        
        tick++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getGraphics()!=null) {
            update(getGraphics());
        }
    }
}
