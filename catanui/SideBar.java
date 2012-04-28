/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import gamelogic.*;

/**
 *
 * @author jfedor
 */
public class SideBar extends JPanel implements MouseListener, MouseMotionListener {
    
    private int _width = 200;
    private int _height = 700;
    
    private final int ENTRANCEY = _height*2/3-50;
    
    private ArrayList<Card> _cards;
    private ArrayList<BoardObject> _handObjects;
    private HashMap<Integer,Exchanger> _exchangers;
    
    public BoardObject _up;
    
    public MapPanel mp;
    
    public Robot r;
    
    public enum Cards {}
    
    private int CurrDisplay = 0; // building

	private ClientGameBoard gameLogic;

	private int[] GOTOTRADECOORD = {0,400,100,50};
	private Image tradeGraphic = Toolkit.getDefaultToolkit().getImage("catanui/tradebutton.png");
	private Image buildGraphic = Toolkit.getDefaultToolkit().getImage("catanui/buildbutton.png");
	private int[] GOTOBUILDCOORD = {100,400,100,50};
    
    public SideBar(ClientGameBoard gl) {
        
        _cards = new ArrayList<Card>();
        _cards.add(new Card(15,481,BoardObject.type.BRICK));
        _cards.add(new Card(28,496,BoardObject.type.WOOD));
        _cards.add(new Card(40,511,BoardObject.type.ORE));
        _cards.add(new Card(140,511,BoardObject.type.SHEEP));
        
        _cards.add(new Card(136,599,BoardObject.type.BRICK));
        _cards.add(new Card(96,599,BoardObject.type.WOOD));
        _cards.add(new Card(139,486,BoardObject.type.WHEAT));
        
        _up = null;
        
	gameLogic = gl;
	gameLogic._sideBar = this;

        _exchangers = new HashMap<Integer,Exchanger>();
        //_exchangers.add(new Exchanger(1,10,100,new BoardObject.type[]
        //        {BoardObject.type.WOOD,BoardObject.type.WOOD},new BoardObject.type[]{BoardObject.type.ORE},5));
        
        _exchangers.put(1,new Exchanger(0,10,175,new BoardObject.type[]
                {BoardObject.type.WOOD,BoardObject.type.BRICK},new BoardObject.type[]{BoardObject.type.ROAD},1));
        
        _exchangers.put(2,new Exchanger(0,10,250,new BoardObject.type[]
                {BoardObject.type.WHEAT,BoardObject.type.SHEEP,BoardObject.type.ORE},new BoardObject.type[]{BoardObject.type.DEV},2));
                
        _exchangers.put(0,new Exchanger(0,10,100,new BoardObject.type[]
                {BoardObject.type.WHEAT,BoardObject.type.SHEEP,BoardObject.type.WOOD,BoardObject.type.BRICK},new BoardObject.type[]{BoardObject.type.SETTLEMENT},0));
        
        _exchangers.put(3,new Exchanger(0,10,325,new BoardObject.type[]
                {BoardObject.type.WHEAT,BoardObject.type.WHEAT,BoardObject.type.ORE,BoardObject.type.ORE,BoardObject.type.ORE},new BoardObject.type[]{BoardObject.type.CITY},3));  
        _handObjects = new ArrayList<BoardObject>();
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        
    }
   
   public void activateExchanger(int id) {
		_exchangers.get(id).switchOutB();
   }

    public class Exchanger implements java.io.Serializable {
        public int _x;
        public int _y;
        private int WIDTH = 180;
        private int HEIGHT = 55;
        
        private BoardObject.type[] ins;
        public BoardObject.type[] outs;
        
        public int _where;
		private int _tradeID;
        
        public Exchanger(int where, int x, int y, BoardObject.type[] in, BoardObject.type[] out, int id) {
            //practical max of two ins or outs
            _where = where;
            _x = x; _y = y; ins = in; outs = out;
			_tradeID = id;
        }

	
        public void paint(Graphics g) {
            
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(_x, _y, WIDTH, HEIGHT);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(_x, _y, WIDTH, HEIGHT);
            
            ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(0.5)));
            g.setColor(Color.LIGHT_GRAY);
            
            if (ins.length == 1) {
                Card i1 = new Card(_x+30+16,_y+5,ins[0]);
                i1.paint(g);
            }
            else if (ins.length == 2) {
                Card i1 = new Card(_x+8,_y+5,ins[0]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+16,_y+5,ins[1]);
                i1.paint(g);
            }
            else if (ins.length == 3) {
                Card i1 = new Card(_x+8,_y+5,ins[0]);
                i1.paint(g);
                i1 = new Card(_x+i1._w-4,_y+5,ins[1]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+16,_y+5,ins[2]);
                i1.paint(g);
            }
            else if (ins.length == 4) {
                Card i1 = new Card(_x+8,_y+5,ins[0]);
                i1.paint(g);
                i1 = new Card(_x+i1._w-9,_y+5,ins[1]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+4,_y+5,ins[2]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+20,_y+5,ins[3]);
                i1.paint(g);
            }
            else if (ins.length == 5) {
                 Card i1 = new Card(_x+8,_y+5,ins[0]);
                i1.paint(g);
                i1 = new Card(_x+i1._w-8,_y+5,ins[1]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+2,_y+5,ins[2]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+12,_y+5,ins[3]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+22,_y+5,ins[4]);
                i1.paint(g);
                
            }
            if (outs.length == 1) {
                if (outs[0] == BoardObject.type.SETTLEMENT) {
                    Settlement i = new Settlement(_x+WIDTH-30-44,_y+5);
                    i.paint(g);
                }
                else if (outs[0] == BoardObject.type.ROAD) {
                    Road i = new Road(_x+WIDTH-30-44,_y+5);
                    i.paint(g);
                }
                else {
                    Card i1 = new Card(_x+WIDTH-30-44,_y+5,outs[0]);
                    i1.paint(g);
                } 
            }
            else {
                Card i1 = new Card(_x+WIDTH-37,_y+5,outs[0]);
                i1.paint(g);
                i1 = new Card(_x+WIDTH-i1._w-44,_y+5,outs[1]);
                i1.paint(g);
            }
            
            ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1));
            
            g.setColor(Color.BLACK);
            g.drawLine(_x+WIDTH/2+2, _y+HEIGHT/2-5, _x+WIDTH/2+7, _y+HEIGHT/2);
            g.drawLine(_x+WIDTH/2-8, _y+HEIGHT/2, _x+WIDTH/2+7, _y+HEIGHT/2);
            g.drawLine(_x+WIDTH/2+2, _y+HEIGHT/2+5, _x+WIDTH/2+7, _y+HEIGHT/2);
            
        }
        
        public ArrayList<Card> cardsIn(ArrayList<Card> cards) {
            
            ArrayList<Card> ret = new ArrayList<Card>();
            
            for (Card c : cards) {
                if (inside(c.getX(),c.getY(),c._w,c._h,_x-2,_y-2,WIDTH+4,HEIGHT+4))
                    ret.add(c);
            }
            
            return ret;
        }
        
        public ArrayList<Card> checkFull(ArrayList<Card> cards) {
            ArrayList<Card> rm = new ArrayList<Card>();
            ArrayList<Card> cs = cardsIn(cards);
            int[] sofar = new int[ins.length];
            for (int i=0;i<sofar.length;i++) sofar[i] = 0; //set blank
            
            for (Card c : cs) {
                for (int i=0;i<ins.length;i++) {
                    if (sofar[i] == 0 && ins[i]==c.mytype) {
                        sofar[i] = 1;
                        rm.add(c);
                        break;
                    }
                }
            }
            for (int i=0;i<sofar.length;i++) 
                if (sofar[i] == 0)
                    return null;
            return rm;
        }
        
	public void switchOut(ArrayList<Card> rm) {
		if (outs[0] == BoardObject.type.SETTLEMENT) {

			gameLogic.writeBuySettlement(new Pair(new Pair(ins,outs),_tradeID));
			}
		else if (outs[0] == BoardObject.type.CITY)
			gameLogic.writeBuyCity(new Pair(new Pair(ins,outs),_tradeID));
		else if (outs[0] == BoardObject.type.ROAD)
			gameLogic.writeBuyRoad(new Pair(new Pair(ins,outs),_tradeID));
		else if (outs[0] == BoardObject.type.DEV)
			gameLogic.writeBuyDev(new Pair(new Pair(ins,outs),_tradeID));
		else 
			gameLogic.writeDoTrade(this, _tradeID);
	}

        public void switchOutB() {
            ArrayList<Card> sw = checkFull(_cards);
                
            if (sw != null) {
	        for (Card c : sw)
	            _cards.remove(c);
	        
	        if (outs.length == 1) {
	            if (outs[0] == BoardObject.type.SETTLEMENT) {
	                Settlement i = new Settlement(_x+WIDTH-30-44,_y+5);
	                _handObjects.add(i);
	            }
	            else if (outs[0] == BoardObject.type.ROAD) {
	                Road i = new Road(_x+WIDTH-30-44,_y+5);
	                _handObjects.add(i);
	            }
	            else {
	                Card i1 = new Card(_x+WIDTH-30-44,_y+5,outs[0]);
	                _cards.add(i1);
	            }
	        }
	        else {
	            Card i1 = new Card(_x+WIDTH-37,_y+5,outs[0]);
	            _cards.add(i1);
	            i1 = new Card(_x+WIDTH-i1._w-44,_y+5,outs[1]);
	            _cards.add(i1);
	        }
	        
	        repaint();
            }
		else {
			System.out.println("Error: cards have disappeared since request to exchange");
		}
        }
        
    }
    
    @Override
    public void paint(Graphics g) {
        
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, _width, _height);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(5, _height*2/3, _width-5, _height*2/3);
        g.setColor(new Color(200,200,200));
        g.fillRect(5,_height*2/3+5, _width - 10, _height*1/3-10);
        
        
        for (Integer e : _exchangers.keySet()) {
			Exchanger e1 = _exchangers.get(e);
            if (e1._where == CurrDisplay)
                e1.paint(g);
        }

		g.drawImage(tradeGraphic, GOTOTRADECOORD[0],GOTOTRADECOORD[1],GOTOTRADECOORD[2],GOTOTRADECOORD[3],  null);
		g.drawImage(buildGraphic, GOTOBUILDCOORD[0],GOTOBUILDCOORD[1],GOTOBUILDCOORD[2],GOTOBUILDCOORD[3],  null);

        for (Card c : _cards)
            c.paint(g);
        for (BoardObject o : _handObjects)
            o.paint(g);
        
		        

        if (_up != null)
            _up.paint(g);
    }

    
    public void addCard(BoardObject.type type) {
        
        _cards.add(new Card(getNextEntranceX(),ENTRANCEY,type));
        
    }
    
    private int getNextEntranceX() {
        
        int count = 0;
        for (int i=0;i<_cards.size();i++) {
            
            if (_cards.get(i).getY() == ENTRANCEY)
                count++;
            
        }
        
        return 20 + count*15;
        
    }
    
    
    @Override
    public void mouseClicked(MouseEvent me) {
        //addCard(BoardObject.type.WHEAT);
	if (collides(me.getX(),me.getY(),2,2,GOTOTRADECOORD[0],GOTOTRADECOORD[1],GOTOTRADECOORD[2],GOTOTRADECOORD[3]))
		CurrDisplay = 1;
	else if (collides(me.getX(),me.getY(),2,2,GOTOBUILDCOORD[0],GOTOBUILDCOORD[1],GOTOBUILDCOORD[2],GOTOBUILDCOORD[3]))
		CurrDisplay = 0;

	if (me.getButton() == MouseEvent.BUTTON3) {
		Card c;
		for (int i=_cards.size()-1;i>=0;i--) {
		    c = _cards.get(i);
		    if (collides(c._x,c._y,c._w,c._h,me.getX(),me.getY(),3,3) && c.getType()==BoardObject.type.DEV) {
			    gameLogic.useDevCard();
		            gameLogic._chatBar.addLine("You use the development card and are blessed with 1 wood and 1 brick.");
			    addCard(BoardObject.type.WOOD);
			    addCard(BoardObject.type.BRICK);
			    _cards.remove(c);
			    break;
		    }
		}
	}

        repaint();

    }

    @Override
    public void mousePressed(MouseEvent me) {
        for (BoardObject o : _handObjects) {
            if (collides(o.getX(),o.getY(),o.getW(),o.getH(),me.getX(),me.getY(),3,3)) {
                    _up = _handObjects.remove(_handObjects.indexOf(o));
                    return;
            }
        }
        Card c;
        for (int i=_cards.size()-1;i>=0;i--) {
            c = _cards.get(i);
            if (collides(c._x,c._y,c._w,c._h,me.getX(),me.getY(),3,3)) {
                    _up = _cards.remove(_cards.indexOf(c));
                    return;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        ArrayList<Card> sw;
        if (_up != null) {
            if (Card.class.isInstance(_up))
                _cards.add((Card)_up);
            else {
                _handObjects.add(_up);
            }
            _up = null;
        }
        for (Integer e : _exchangers.keySet()) {
			Exchanger e1 = _exchangers.get(e);
            sw = e1.checkFull(_cards);
                
            if (sw != null) {
                e1.switchOut(sw);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (_up != null && !Card.class.isInstance(_up) && (me.getY() < mp._h)) {
            mp.setUp(_up,me.getX(),me.getY());
            
            _up = null;
        } 
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (_up != null) {
            if (Card.class.isInstance(_up)) {
                _up.setX(Math.min(_width-_up.getW()-5, me.getX()-_up.getW()/2));
                _up.setY(me.getY()-_up.getH()/2);
            }
            else {
                _up.setX(Math.min(_width-_up.getW()-5, me.getX()-_up.getW()/2));
                _up.setY(me.getY()-_up.getH()/2);
                //if (me.getX() > _width) {
                //    mp._up = _up;
                //    _up = null;
                //}
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {

    }
    
    public boolean inside(int ax,int ay,int aw,int ah,int bx,int by,int bw,int bh) {
        
        return ((ax > bx && ax < bx+bw) && (ay > by && ay < by + bh) && 
                (ax + aw > bx && ax + aw < bx + bw) && (ay+ah > by && ay+ah < by+bh));

    }
    
    public static boolean collides(int ax,int ay,int aw,int ah,int bx,int by,int bw,int bh) {
	    
	    boolean incol = false;
	    boolean inrow = false;
	    
	    if (ax >= bx && ax <= bx+bw)
	        incol = true;
	    if (ax+aw >= bx && ax+aw <= bx+bw)
	        incol = true;
	    if (ay >= by && ay <= by+bh)
	        inrow = true;
	    if (ay+ah >= by && ay+ah <= by+bh)
	        inrow = true;

	    
	    if (bx >= ax && bx <= ax+aw)
	        incol = true;
	    if (bx+bw >= ax && bx+bw <= ax+aw)
	        incol = true;
	    if (by >= ay && by <= ay+ah)
	        inrow = true;
	    if (by+bh >= ay && by+bh <= ay+ah)
	        inrow = true;
	    
	    if (inrow && incol)
	        return true;
	    return false;

	}
    
    public void setUp(BoardObject u, int x, int y) {
         try {
            r = new Robot();
        } catch (AWTException ex) {
        }
        _up = u;
        _up.setX(_width-u.getW()-5);
	r.mouseMove(x-10,y);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
    }
}
