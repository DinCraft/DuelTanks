import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyEvent;

public class Game extends JPanel implements ActionListener, MouseMotionListener, KeyListener, MouseListener, MouseWheelListener {
    static int chosenBulletType = 0;
    static byte[] data;
    static List<Bullet> bullets = new ArrayList<>();
    static int p1Score = 0;
    static int p2Score = 0;
    Timer timer;
    int port;
    String your_ip = "";
	String another_ip = "";
    int x,y,mx,my;
    int b = 0;
    float tg;
    boolean isWPressed,isAPressed,isSPressed,isDPressed;
    List<Wall> walls = new ArrayList<>();
    ServerThread st;
    int initP1x, initP1y, initP2x, initP2y;

    Game(int port, String your_ip, String another_ip){
        this.port = port;
        this.your_ip = your_ip;
        this.another_ip = another_ip;
        Player.init();
        this.setBounds(0, 0, 1000, 600);
        loadMap();
        initPlayers();
        timer = new Timer(25, this);
        timer.start();
        st = new ServerThread();
        st.port = port;
        st.your_ip = your_ip;
        st.another_ip = another_ip;
        st.start();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addKeyListener(this);
		this.setFocusable(true);
        this.addMouseWheelListener(this);
		this.setFocusTraversalKeysEnabled(false);
    }

    public void initPlayers() {
        Player.health = 100;
        Player2.health = 100;
    }

    public void loadMap() {
        try(BufferedReader br = new BufferedReader(new FileReader("map.txt"))) {
            String line = br.readLine();
            int l = 0;
            while (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i)=='1') {
                        walls.add(new Wall(i*100, l*100));
                    }
                    if (line.charAt(i)=='2') {
                        initP1x = i*100;
                        initP1y = l*100;
                        Player.x = initP1x;
                        Player.y = initP1y;
                    }
                    if (line.charAt(i)=='3') {
                        initP2x = i*100;
                        initP2y = l*100;
                        Player2.x = initP2x;
                        Player2.y = initP2y;
                    }
                }
                line = br.readLine();
                l++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void backgroundUpdate(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, 1000, 600);
        for (int i = 0; i < walls.size(); i++) {
            walls.get(i).draw(g, Player.x, Player.y);
        }
    }

    public void moveP1() {
        if (isDPressed) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player.x+5,Player.y)){
                    col = true;
                }
            }
            if (!col) {
                Player.x+=5;
            }
        }
        if (isAPressed) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player.x-5,Player.y)){
                    col = true;
                }
            }
            if (!col) {
                Player.x-=5;
            }
        }
        if (isWPressed) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player.x,Player.y-5)){
                    col = true;
                }
            }
            if (!col) {
                Player.y-=5;
            }
        }
        if (isSPressed) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player.x,Player.y+5)){
                    col = true;
                }
            }
            if (!col) {
                Player.y+=5;
            }
        }
    }

    public void moveP2() {
        if (st.ci.moveDown) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player2.x,Player2.y+5)){
                    col = true;
                }
            }
            if (!col) {
                Player2.y+=5;
            }
        }
        if (st.ci.moveLeft) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player2.x-5,Player2.y)){
                    col = true;
                }
            }
            if (!col) {
                Player2.x-=5;
            }
        }
        if (st.ci.moveUp) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player2.x,Player2.y-5)){
                    col = true;
                }
            }
            if (!col) {
                Player2.y-=5;
            }
        }
        if (st.ci.moveRight) {
            boolean col = false;
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).collision(Player2.x+5,Player2.y)){
                    col = true;
                }
            }
            if (!col) {
                Player2.x+=5;
            }
        }
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(450, 0, 50, 50);
        g.setColor(Color.red);
        g.fillRect(500, 0, 50, 50);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", 0, 42));
        g.drawString(String.valueOf(p1Score), 465, 40);
        g.drawString(String.valueOf(p2Score), 515, 40);
    }

    public void resetPlayersStats() {
        Player.x = initP1x;
        Player.y = initP1y;
        Player.health = 100;
        Player2.x = initP2x;
        Player2.y = initP2y;
        Player2.health = 100;
    }

    public void bulletsUpdate(Graphics g) {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
            if (bullets.get(i).x+10>Player.x && bullets.get(i).y+10>Player.y && bullets.get(i).x<Player.x+50 && bullets.get(i).y<Player.y+50 && bullets.get(i).isBlueTeam==false) {
                if (Player.health>20) {
                    Player.health-=20;
                }else{
                    Player.health = 0;
                    p2Score+=1;
                    resetPlayersStats();
                }
                bullets.remove(i);
            }
            if (bullets.get(i).x+10>Player2.x && bullets.get(i).y+10>Player2.y && bullets.get(i).x<Player2.x+50 && bullets.get(i).y<Player2.y+50 && bullets.get(i).isBlueTeam==true) {
                if (Player2.health>20) {
                    Player2.health-=20;
                }else{
                    Player2.health = 0;
                    p1Score+=1;
                    resetPlayersStats();
                }
                bullets.remove(i);
            }
            //System.out.println("bx: "+bullets.get(i).x+" by: "+bullets.get(i).y+"; px: "+Player.x+" py: "+Player.y);
            for (int j = 0; j < walls.size(); j++) {
                if (walls.get(j).bulletCollision(bullets.get(i).x, bullets.get(i).y)) {
                    bullets.remove(i);
                    break;
                }
            }
        }
    }

    public void packData() {
        try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	ObjectOutputStream out = new ObjectOutputStream(bos);
        	out.writeObject(new DataIO(Player.x, Player.y, Player2.x, Player2.y));
        	out.flush();
        	data = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void paint(Graphics g) {
        backgroundUpdate(g);
        Player.update(g);
        Player2.update(g);
        //System.out.println(Player.x+" "+Player.y+"; "+Player2.x+" "+Player2.y);
        moveP1();
        if (st.ci!=null) {
            Player2.mana = st.ci.mana;
            moveP2();
            updatePlayer2();
            if (st.ci.shoot) {
                if (st.ci.type==0) {
                    bullets.add(new Bullet(Player2.x+20, Player2.y+20, (float)st.ci.vx*15, (float)st.ci.vy*15, "normal", false));
                }
                if (st.ci.type==1) {
                    bullets.add(new Bullet(Player2.x+20, Player2.y+20, (float)st.ci.vx*15, (float)st.ci.vy*15, "wavy1", false));
                    bullets.add(new Bullet(Player2.x+20, Player2.y+20, (float)st.ci.vx*15, (float)st.ci.vy*15, "wavy2", false));
                }
            }
        }
        bulletsUpdate(g);
        drawScore(g);
        packData();
    }

    public void updatePlayer2() {
        Player2.angle = st.ci.angle;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mx=e.getX();
        my=e.getY();
        x=mx-500;
        y=my-300;
        tg = (float)Math.atan2(y,x);
        tg = (float)(Math.toDegrees(tg));
        Player.angle = Math.round(tg)+90;
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
			isWPressed = true;
		}
        if (e.getKeyCode() == KeyEvent.VK_A){
			isAPressed = true;
		}
        if (e.getKeyCode() == KeyEvent.VK_S){
			isSPressed = true;
		}
        if (e.getKeyCode() == KeyEvent.VK_D){
			isDPressed = true;
		}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
			isWPressed = false;
		}
        if (e.getKeyCode() == KeyEvent.VK_A){
			isAPressed = false;
		}
        if (e.getKeyCode() == KeyEvent.VK_S){
			isSPressed = false;
		}
        if (e.getKeyCode() == KeyEvent.VK_D){
			isDPressed = false;
		}
    }

    
    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println(Math.round(tg)+180);
        double a = Math.toRadians(Math.round(tg)-180);
        double x = -Math.cos(a);
        double y = -Math.sin(a);
        //System.out.println(x+" "+y);
        if (Player.cooldown==0) {
            Player.cooldown = 20;
            if (chosenBulletType==0 && Player.real_mana>20) {
                Player.real_mana-=20;
                bullets.add(new Bullet(Player.x+20, Player.y+20, (float)x*15, (float)y*15,"normal",true));
            }
            if (chosenBulletType==1 && Player.real_mana>50) {
                Player.real_mana-=50;
                bullets.add(new Bullet(Player.x+20, Player.y+20, (float)x*15, (float)y*15,"wavy1",true));
                bullets.add(new Bullet(Player.x+20, Player.y+20, (float)x*15, (float)y*15,"wavy2",true));
            }
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println(e.getWheelRotation());
        if (e.getWheelRotation()==1) {
            if (chosenBulletType<1) {
                chosenBulletType+=1;
            }else{
                chosenBulletType=0;
            }
        }
        if (e.getWheelRotation()==-1) {
            if (chosenBulletType>0) {
                chosenBulletType-=1;
            }else{
                chosenBulletType=1;
            }
        }
    }
}