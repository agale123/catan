package catanui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JFrame {
    
	/*    
    public static void main(String[] args) {
        Board m = new Board(new ClientGameBoard());
    }*/
    public int shrinkByY = 54 + 25;
    public int shrinkByX = 176;


    public Board(gamelogic.ClientGameBoard gameLogic) {
        super();
        
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setLayout(null);
        
        BoardObject.images.put(BoardObject.type.WHEAT, Toolkit.getDefaultToolkit().getImage("catanui/wheatcard.png"));
        BoardObject.images.put(BoardObject.type.WOOD, Toolkit.getDefaultToolkit().getImage("catanui/woodcard.png"));
        BoardObject.images.put(BoardObject.type.ORE, Toolkit.getDefaultToolkit().getImage("catanui/orecard.png"));
        BoardObject.images.put(BoardObject.type.SHEEP, Toolkit.getDefaultToolkit().getImage("catanui/sheepcard.png"));
        BoardObject.images.put(BoardObject.type.BRICK, Toolkit.getDefaultToolkit().getImage("catanui/brickcard.png"));
        BoardObject.images.put(BoardObject.type.DEV, Toolkit.getDefaultToolkit().getImage("catanui/devcard.png"));
	BoardObject.images.put(BoardObject.type.DICE, Toolkit.getDefaultToolkit().getImage("catanui/dice.png"));

	BoardObject.images.put(BoardObject.type.WHEATPORT, Toolkit.getDefaultToolkit().getImage("catanui/wheatport.png"));
        BoardObject.images.put(BoardObject.type.WOODPORT, Toolkit.getDefaultToolkit().getImage("catanui/woodport.png"));
        BoardObject.images.put(BoardObject.type.OREPORT, Toolkit.getDefaultToolkit().getImage("catanui/oreport.png"));
        BoardObject.images.put(BoardObject.type.SHEEPPORT, Toolkit.getDefaultToolkit().getImage("catanui/sheepport.png"));
        BoardObject.images.put(BoardObject.type.BRICKPORT, Toolkit.getDefaultToolkit().getImage("catanui/brickport.png"));


	BoardObject.coloredImages.put(BoardObject.type.SETTLEMENT, new Image[]{Toolkit.getDefaultToolkit().getImage("catanui/settlement_blue.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_red.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_green.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_cyan.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_yellow.png"),Toolkit.getDefaultToolkit().getImage("catanui/settlement_orange.png")});
	BoardObject.coloredImages.put(BoardObject.type.CITY, new Image[]{Toolkit.getDefaultToolkit().getImage("catanui/city_blue.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_red.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_green.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_cyan.png"),Toolkit.getDefaultToolkit().getImage("catanui/city_yellow.png"), Toolkit.getDefaultToolkit().getImage("catanui/city_orange.png")});
       
	MediaTracker tracker = new MediaTracker(this);
	tracker.addImage(BoardObject.images.get(BoardObject.type.DICE),0);
	tracker.addImage(BoardObject.images.get(BoardObject.type.DEV),0);
	for (int i=0;i<6;i++) {
	    tracker.addImage(BoardObject.coloredImages.get(BoardObject.type.SETTLEMENT)[i],0);
	    tracker.addImage(BoardObject.coloredImages.get(BoardObject.type.CITY)[i],0);
	}
	for (BoardObject.type t : BoardObject.cardtypes) {
		tracker.addImage(BoardObject.images.get(t),0);
	}
	for (BoardObject.type t : BoardObject.porttypes) {
		tracker.addImage(BoardObject.images.get(t),0);
	}

	SideBar sd = new SideBar(gameLogic);
	tracker.addImage(sd.tradeGraphic,0);
	tracker.addImage(sd.buildGraphic,0);
	tracker.addImage(sd.portsGraphic,0);
	try {
		tracker.waitForAll();
	} catch (InterruptedException ex) {
		System.out.println("Non-fatal error loading images.");
	}

	        
        sd.setSize(200,700-shrinkByY); 
        
        MapPanel mp = new MapPanel(gameLogic);
        mp.setSize(1000-shrinkByX, 650-shrinkByY);
        
        sd.mp = mp;
        mp.sb = sd;
        mp._x = 200;
        mp._h = 550;

        JTextField chat = new JTextField();
        chat.setBounds(210,760-shrinkByY,990-shrinkByX,30);

        ChatBar cb = new ChatBar(gameLogic,chat);
        cb.setSize(990-shrinkByX,200);
        
        chat.addActionListener(cb);
	chat.addKeyListener(cb);

        add(chat);
        
        mp.setBounds(200, 0, 1000-shrinkByX, 650-shrinkByY);
        sd.setBounds(0, 0, 200, 822-shrinkByY);
        cb.setBounds(200, 650-shrinkByY, 1000-shrinkByX, 200-shrinkByY);
        
        add(sd);
        add(cb);
        add(mp);


        //setSize(1200, 822); shrinkByX shrinkByY
	setSize(1200-shrinkByX,822-shrinkByY);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
