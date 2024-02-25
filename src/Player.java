import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
    static BufferedImage gun, normalBullet, normalBulletActive, wavyBullet, wavyBulletActive;
    static int x;
    static int y;
    static float health;
    static int mana = 0;
    static float real_mana = 0;
    static int cooldown = 0;
    static int angle = 0;

    static void init(){
        try {
            normalBullet = ImageIO.read(new File("img/normal.png"));
            normalBulletActive = ImageIO.read(new File("img/normal2.png"));
            wavyBullet = ImageIO.read(new File("img/wavy.png"));
            wavyBulletActive = ImageIO.read(new File("img/wavy2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void update(Graphics g){
        try {
            gun = ImageIO.read(new File("img/img.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        draw(g);
        drawHealth(g);
        drawMana(g);
        cooldown();
        manaUpdate();
    }

    static void manaUpdate(){
        if (real_mana<100) {
            real_mana+=0.2;
        }
        mana=Math.round(real_mana);
    }

    static void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
        g2.fillRect(475, 275, 50, 50);
        gun = rotateImage(gun);
        g2.drawImage(gun, null, 475, 275);
        if (Game.chosenBulletType==0) {
            g2.drawImage(normalBulletActive, null, 900, 500);
            g2.drawImage(wavyBullet, null, 900, 450);
        }
        if (Game.chosenBulletType==1) {
            g2.drawImage(wavyBulletActive, null, 900, 450);
            g2.drawImage(normalBullet, null, 900, 500);
        }
        
    }

    static void cooldown(){
        if (cooldown>0) {
            cooldown--;
        }
    }

    static void drawHealth(Graphics g){
        g.setColor(new Color(0, 122, 12));
        g.fillRect(475, 330, 50, 7);
        g.setColor(new Color(100, 166, 17));
        g.fillRect(476, 331, Math.round(health*48/100), 5);
    }
    static void drawMana(Graphics g){
        g.setColor(new Color(46, 64, 183));
        g.fillRect(475, 340, 50, 7);
        g.setColor(new Color(86, 109, 255));
        g.fillRect(476, 341, Math.round(mana*48/100), 5);
    }

    static BufferedImage rotateImage(BufferedImage imageToRotate) {
        int widthOfImage = imageToRotate.getWidth();
        int heightOfImage = imageToRotate.getHeight();
        int typeOfImage = imageToRotate.getType();

        BufferedImage newImageFromBuffer = new BufferedImage(widthOfImage, heightOfImage, typeOfImage);

        Graphics2D graphics2D = newImageFromBuffer.createGraphics();

        graphics2D.rotate(Math.toRadians(angle), widthOfImage / 2, heightOfImage / 2);
        graphics2D.drawImage(imageToRotate, null, 0, 0);

        return newImageFromBuffer;
    }
}