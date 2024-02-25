import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

public class Game2 extends JPanel implements ActionListener, MouseListener, KeyListener, MouseWheelListener, MouseMotionListener {
    static boolean isWPressed,isAPressed,isSPressed,isDPressed;
    static BufferedImage normalBullet, normalBulletActive, wavyBullet, wavyBulletActive;
    static int chosenBulletType = 0;
    static int angle;
    static byte[] data;
    static int mana = 0;
    static float real_mana = 0;
    int x,y,mx,my;
    float tg;
    Timer timer;
    ClientThread ct;
    int port;
    String your_ip = "";
	String another_ip = "";
    List<Wall> walls = new ArrayList<>();
    BufferedImage gun;
    int cooldown = 0;
    static boolean shoot = false;
    static double vx;
    static double vy;

    Game2(int port, String your_ip, String another_ip){
        this.setBounds(0, 0, 1000, 600);
        this.port = port;
        this.your_ip = your_ip;
        this.another_ip = another_ip;
        loadMap();
        timer = new Timer(25, this);
        timer.start();
        ct = new ClientThread();
        ct.port = port;
        ct.your_ip = your_ip;
        ct.another_ip = another_ip;
        ct.start();
        init();
        
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addKeyListener(this);
		this.setFocusable(true);
        this.addMouseWheelListener(this);
		this.setFocusTraversalKeysEnabled(false);
    }

    public void drawBullets(Graphics g) {
        for (int i = 0; i < ct.dio.bullets.size(); i++) {
            if (ct.dio.bullets.get(i).isBlueTeam) {
                g.setColor(Color.BLUE);
            }else{
                g.setColor(Color.RED);
            }
            g.fillRect(Math.round(ct.dio.bullets.get(i).x-ct.dio.p2X+475), Math.round(ct.dio.bullets.get(i).y-ct.dio.p2Y+275), 10, 10);
        }
    }

    public void drawP2(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            gun = ImageIO.read(new File("img/img.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.setColor(Color.red);
        g2.fillRect(475, 275, 50, 50);
        gun = rotateImage(gun,false);
        g2.drawImage(gun, null, 475, 275);

        g.setColor(new Color(0, 122, 12));
        g.fillRect(475, 330, 50, 7);
        g.setColor(new Color(100, 166, 17));
        g.fillRect(476, 331, Math.round(ct.dio.p2Health*48/100), 5);

        g.setColor(new Color(46, 64, 183));
        g.fillRect(475, 340, 50, 7);
        g.setColor(new Color(86, 109, 255));
        g.fillRect(476, 341, Math.round(mana*48/100), 5);

        if (chosenBulletType==0) {
            g2.drawImage(normalBulletActive, null, 900, 500);
            g2.drawImage(wavyBullet, null, 900, 450);
        }
        if (chosenBulletType==1) {
            g2.drawImage(wavyBulletActive, null, 900, 450);
            g2.drawImage(normalBullet, null, 900, 500);
        }
    }
    public void drawP1(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            gun = ImageIO.read(new File("img/img.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.setColor(Color.blue);
        g2.fillRect(ct.dio.pX-ct.dio.p2X+475, ct.dio.pY-ct.dio.p2Y+275, 50, 50);
        gun = rotateImage(gun,true);
        g2.drawImage(gun, null, ct.dio.pX-ct.dio.p2X+475, ct.dio.pY-ct.dio.p2Y+275);

        g.setColor(new Color(0, 122, 12));
        g.fillRect(ct.dio.pX-ct.dio.p2X+475, ct.dio.pY-ct.dio.p2Y+330, 50, 7);
        g.setColor(new Color(100, 166, 17));
        g.fillRect(ct.dio.pX-ct.dio.p2X+476, ct.dio.pY-ct.dio.p2Y+331, Math.round(ct.dio.pHealth*48/100), 5);

        g.setColor(new Color(46, 64, 183));
        g.fillRect(ct.dio.pX-ct.dio.p2X+475, ct.dio.pY-ct.dio.p2Y+340, 50, 7);
        g.setColor(new Color(86, 109, 255));
        g.fillRect(ct.dio.pX-ct.dio.p2X+476, ct.dio.pY-ct.dio.p2Y+341, Math.round(ct.dio.pMana*48/100), 5);
    }

    public BufferedImage rotateImage(BufferedImage imageToRotate, boolean isP1) {
        int widthOfImage = imageToRotate.getWidth();
        int heightOfImage = imageToRotate.getHeight();
        int typeOfImage = imageToRotate.getType();

        BufferedImage newImageFromBuffer = new BufferedImage(widthOfImage, heightOfImage, typeOfImage);

        Graphics2D graphics2D = newImageFromBuffer.createGraphics();

        if (isP1) {
            graphics2D.rotate(Math.toRadians(ct.dio.pAngle), widthOfImage / 2, heightOfImage / 2);
        }else{
            graphics2D.rotate(Math.toRadians(ct.dio.p2Angle), widthOfImage / 2, heightOfImage / 2);
        }
        
        graphics2D.drawImage(imageToRotate, null, 0, 0);

        return newImageFromBuffer;
    }

    public void packData() {
        try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	ObjectOutputStream out = new ObjectOutputStream(bos);
        	out.writeObject(new ClientInfo());
        	out.flush();
        	data = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void init(){
        try {
            normalBullet = ImageIO.read(new File("img/normal.png"));
            normalBulletActive = ImageIO.read(new File("img/normal2.png"));
            wavyBullet = ImageIO.read(new File("img/wavy.png"));
            wavyBulletActive = ImageIO.read(new File("img/wavy2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backgroundUpdate(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, 1000, 600);
        for (int i = 0; i < walls.size(); i++) {
            walls.get(i).draw(g,ct.dio.p2X,ct.dio.p2Y);
        }
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(450, 0, 50, 50);
        g.setColor(Color.red);
        g.fillRect(500, 0, 50, 50);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", 0, 42));
        g.drawString(String.valueOf(ct.dio.p1Score), 465, 40);
        g.drawString(String.valueOf(ct.dio.p2Score), 515, 40);
    }

    @Override
    public void paint(Graphics g) {
        if (ct.dio!=null) {
            backgroundUpdate(g);
            drawP1(g);
            drawP2(g);
            drawBullets(g);
            drawScore(g);
        }
        manaUpdate();
        cooldown();
        packData();
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
                }
                line = br.readLine();
                l++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        mx=e.getX();
        my=e.getY();
        x=mx-500;
        y=my-300;
        tg = (float)Math.atan2(y,x);
        tg = (float)(Math.toDegrees(tg));
        angle = Math.round(tg+90);
    }
    
    @Override public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
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

    public void cooldown() {
        if (cooldown>0) {
            cooldown--;
        }
    }

    public void manaUpdate() {
        if (real_mana<100) {
            real_mana+=0.2;
        }
        mana=Math.round(real_mana);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double a = Math.toRadians(Math.round(tg)-180);
        vx = -Math.cos(a);
        vy = -Math.sin(a);
        if (cooldown==0) {
            cooldown=20;
            if (chosenBulletType==0 && real_mana>=20) {
                real_mana-=20;
                shoot = true;
            }else if (chosenBulletType==1 && real_mana>=50) {
                real_mana-=50;
                shoot = true;
            }
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}