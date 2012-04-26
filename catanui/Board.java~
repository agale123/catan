package catanui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JFrame {
    
    
    public static void main(String[] args) {
        Board m = new Board();
    }
    
    private Board() {
        super();
        
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setLayout(null);
        
        SideBar sd = new SideBar();
        sd.setSize(200,700); 
        
        MapPanel mp = new MapPanel();
        mp.setSize(800, 550);
        
        sd.mp = mp;
        mp.sb = sd;
        mp._x = 200;
        mp._h = 550;
        
        ChatBar cb = new ChatBar();
        cb.setSize(800,150);
        
        JTextField chat = new JTextField();
        chat.setBounds(210,660,700,30);
        chat.addActionListener(cb);
        add(chat);
        
        mp.setBounds(200, 0, 800, 550);
        sd.setBounds(0, 0, 200, 700);
        cb.setBounds(200, 550, 800, 150);
        
        add(sd);
        add(cb);
        add(mp);
        
        
        BoardObject.images.put(BoardObject.type.WHEAT, Toolkit.getDefaultToolkit().getImage("wheatcard.png"));
        BoardObject.images.put(BoardObject.type.WOOD, Toolkit.getDefaultToolkit().getImage("woodcard.png"));
        BoardObject.images.put(BoardObject.type.ORE, Toolkit.getDefaultToolkit().getImage("orecard.png"));
        BoardObject.images.put(BoardObject.type.SHEEP, Toolkit.getDefaultToolkit().getImage("sheepcard.png"));
        BoardObject.images.put(BoardObject.type.BRICK, Toolkit.getDefaultToolkit().getImage("brickcard.png"));
        BoardObject.images.put(BoardObject.type.DEV, Toolkit.getDefaultToolkit().getImage("devcard.png"));

       
        setSize(1000, 722);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}