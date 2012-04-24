/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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
    private ArrayList<Exchanger> _exchangers;
    
    public BoardObject _up;
    
    public MapPanel mp;
    
    public Robot r;
    
    public enum Cards {}
    
    public SideBar() {
        
        _cards = new ArrayList<Card>();
        _cards.add(new Card(15,481,BoardObject.type.BRICK));
        _cards.add(new Card(28,496,BoardObject.type.WOOD));
        _cards.add(new Card(40,511,BoardObject.type.ORE));
        
        _cards.add(new Card(136,599,BoardObject.type.BRICK));
        _cards.add(new Card(96,599,BoardObject.type.WOOD));
        _cards.add(new Card(139,486,BoardObject.type.WHEAT));
        
        _up = null;
        
        _exchangers = new ArrayList<Exchanger>();
        _exchangers.add(new Exchanger(10,100,new BoardObject.type[]
                {BoardObject.type.WOOD,BoardObject.type.WOOD},new BoardObject.type[]{BoardObject.type.ORE}));
        
        _exchangers.add(new Exchanger(10,200,new BoardObject.type[]
                {BoardObject.type.WOOD,BoardObject.type.WHEAT},new BoardObject.type[]{BoardObject.type.ROAD}));
        
        _exchangers.add(new Exchanger(10,300,new BoardObject.type[]
                {BoardObject.type.WHEAT,BoardObject.type.WHEAT},new BoardObject.type[]{BoardObject.type.SETTLEMENT}));
        
        
        _handObjects = new ArrayList<BoardObject>();
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        
    }
    
    public class Exchanger {
        public int _x;
        public int _y;
        private int WIDTH = 180;
        private int HEIGHT = 55;
        
        private Card.type[] ins;
        private Card.type[] outs;
        
        public Exchanger(int x, int y, Card.type[] in, Card.type[] out) {
            //practical max of two ins or outs
            _x = x; _y = y; ins = in; outs = out;
        }
        public void paint(Graphics g) {
            
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(_x, _y, WIDTH, HEIGHT);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(_x, _y, WIDTH, HEIGHT);
            
            ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(0.3)));
            
            if (ins.length == 1) {
                Card i1 = new Card(_x+30+16,_y+5,ins[0]);
                i1.paint(g);
            }
            else {
                Card i1 = new Card(_x+8,_y+5,ins[0]);
                i1.paint(g);
                i1 = new Card(_x+i1._w+16,_y+5,ins[1]);
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
                if (inside(c.getX(),c.getY(),c._w,c._h,_x,_y,WIDTH,HEIGHT))
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
            
            for (Card c : rm)
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
        
    }
    
    @Override
    public void paint(Graphics g) {
        
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, _width, _height);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(5, _height*2/3, _width-5, _height*2/3);
        g.setColor(new Color(200,200,200));
        g.fillRect(5,_height*2/3+5, _width - 10, _height*1/3-10);
        
        
        for (Exchanger e : _exchangers)
            e.paint(g);
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
        addCard(BoardObject.type.WHEAT  );
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
        for (Exchanger e : _exchangers) {
            sw = e.checkFull(_cards);
                
            if (sw != null) {
                e.switchOut(sw);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (_up != null && !Card.class.isInstance(_up) && (me.getY() < mp._h)) {
            mp.setUp(_up);
            
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
    
    public void setUp(BoardObject u) {
         try {
            r = new Robot();
        } catch (AWTException ex) {
        }
        _up = u;
        _up.setX(_width-u.getW()-5);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
    }
}
