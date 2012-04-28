package catanui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JFrame {
    
	/*    
    public static void main(String[] args) {
        Board m = new Board(new ClientGameBoard());
    }*/
    
    public Board(gamelogic.ClientGameBoard gameLogic) {
        super();
        
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setLayout(null);
        
        SideBar sd = new SideBar(gameLogic);
        sd.setSize(200,700); 
        
        MapPanel mp = new MapPanel(gameLogic);
        mp.setSize(800, 550);
        
        sd.mp = mp;
        mp.sb = sd;
        mp._x = 200;
        mp._h = 550;
        
        ChatBar cb = new ChatBar(gameLogic);
        cb.setSize(800,150);
        
        JTextField chat = new JTextField();
        chat.setBounds(210,660,700,30);
        chat.addActionListener(cb);

		/*JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);

		BoundedRangeModel brm = chat.getVerticalVisibility();
		scrollBar.setModel(brm);
		add(scrollBar);*/

        add(chat);

		cb._textfield = chat;
        
        mp.setBounds(200, 0, 800, 550);
        sd.setBounds(0, 0, 200, 700);
        cb.setBounds(200, 550, 800, 150);
        
        add(sd);
        add(cb);
        add(mp);
        
        
        BoardObject.images.put(BoardObject.type.WHEAT, Toolkit.getDefaultToolkit().getImage("catanui/wheatcard.png"));
        BoardObject.images.put(BoardObject.type.WOOD, Toolkit.getDefaultToolkit().getImage("catanui/woodcard.png"));
        BoardObject.images.put(BoardObject.type.ORE, Toolkit.getDefaultToolkit().getImage("catanui/orecard.png"));
        BoardObject.images.put(BoardObject.type.SHEEP, Toolkit.getDefaultToolkit().getImage("catanui/sheepcard.png"));
        BoardObject.images.put(BoardObject.type.BRICK, Toolkit.getDefaultToolkit().getImage("catanui/brickcard.png"));
        BoardObject.images.put(BoardObject.type.DEV, Toolkit.getDefaultToolkit().getImage("catanui/devcard.png"));


		BoardObject.coloredImages.put(BoardObject.type.SETTLEMENT, new Image[]{Toolkit.getDefaultToolkit().getImage("catanui/settlement_blue.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_red.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_green.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_cyan.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_yellow.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_orange.png")});

		BoardObject.coloredImages.put(BoardObject.type.CITY, new Image[]{Toolkit.getDefaultToolkit().getImage("catanui/city_blue.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_red.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_green.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_cyan.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_yellow.png"), Toolkit.getDefaultToolkit().getImage("catanui/city_orange.png")});
       
        setSize(1000, 722);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
