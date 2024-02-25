import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Frame extends JFrame {
    JButton server, client;
    Menu menu;
    JTextField your_ipAdress_field, another_ipAdress_field, port_field;

    Frame(){
        windowConfig();
        intro();
        initUI();
        initButtons();
        menuUpdate();
    }

    public void windowConfig() {
        this.setBounds(100, 100, 1000, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Duel Tanks");
        this.setResizable(false);
    }

    public void intro() {
        Introduction intro = new Introduction();
        intro.setBounds(0, 0, 1000, 600);
        this.add(intro);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.remove(intro);
        intro = null;
    }

    public void initUI() {
        server = new JButton("Server");
        server.setFont(new Font("Arial", Font.PLAIN, 32));
        server.setBackground(Color.blue);
        server.setForeground(Color.white);
        server.setBounds(300, 400, 185, 40);

        client = new JButton("Client");
        client.setFont(new Font("Arial", Font.PLAIN, 32));
        client.setBackground(Color.blue);
        client.setForeground(Color.white);
        client.setBounds(515, 400, 185, 40);

        your_ipAdress_field = new JTextField("Your IP adress");
        your_ipAdress_field.setBackground(Color.BLUE);
        your_ipAdress_field.setForeground(Color.WHITE);
        your_ipAdress_field.setFont(new Font("Arial",0,32));
        your_ipAdress_field.setBounds(300, 250, 400, 40);

        another_ipAdress_field = new JTextField("Another IP adress");
        another_ipAdress_field.setBackground(Color.BLUE);
        another_ipAdress_field.setForeground(Color.WHITE);
        another_ipAdress_field.setFont(new Font("Arial",0,32));
        another_ipAdress_field.setBounds(300, 300, 400, 40);

        port_field = new JTextField("Port");
        port_field.setBackground(Color.BLUE);
        port_field.setForeground(Color.WHITE);
        port_field.setFont(new Font("Arial",0,32));
        port_field.setBounds(300, 350, 400, 40);

        menu = new Menu();
        menu.setBounds(0, 0, 1000, 600);
    }

    public void menuUpdate() {
        this.add(server);
        this.add(client);
        this.add(your_ipAdress_field);
        this.add(another_ipAdress_field);
        this.add(port_field);
        this.add(menu);

        this.update(getGraphics());
    }

    public void initButtons(){
        server.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.frame.remove(server);
                Main.frame.remove(client);
                Main.frame.remove(your_ipAdress_field);
                Main.frame.remove(another_ipAdress_field);
                Main.frame.remove(port_field);
                Main.frame.remove(menu);
                Game game = new Game(Integer.parseInt(port_field.getText()),your_ipAdress_field.getText(),another_ipAdress_field.getText());
                Main.frame.add(game);
                game.setVisible(true);
            }
        });

        client.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.frame.remove(server);
                Main.frame.remove(client);
                Main.frame.remove(your_ipAdress_field);
                Main.frame.remove(another_ipAdress_field);
                Main.frame.remove(port_field);
                Main.frame.remove(menu);
                Game2 game2 = new Game2(Integer.parseInt(port_field.getText()),your_ipAdress_field.getText(),another_ipAdress_field.getText());
                Main.frame.add(game2);
                game2.setVisible(true);
            }
        });
    }
}