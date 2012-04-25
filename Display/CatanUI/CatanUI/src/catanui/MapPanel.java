package catanui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private ArrayList<Hex> _hexes;
    private ArrayList<BoardObject> _objects;
    
    private int[] _mousedown;
    private int[] _display_offset = {300,0};
    
    public BoardObject _up;
    
    public SideBar sb;
    
    public int _x;
    public int _h;

    private int hexleft;
    private int hextop;
    private int radius = 75;
    
    private final int[] DIE_DIST = {2,3,4,4,5,5,5,6,6,8,8,9,9,9,10,10,11,12};
    
    Robot r;
    
    public MapPanel() {
        super();

        _hexes = new ArrayList<Hex>();
        _objects = new ArrayList<BoardObject>();
        int rings = 3;
        
        
        hexleft = 100-(radius+radius*((rings-1)%2)+(rings-((rings-1)%2))/2*3*radius);
        hextop = 300-(int)(radius*0.866 + (rings-1)*2*(radius * 0.866));
        
        int border = 1;
        
        int ring = 0;
        int currentDir = 5;
        int current = 0;
        int[][] directions = {{1,1},{0,2},{-1,1},{-1,-1},{0,-2},{1,-1}};

        int[][] HexCoordDirections = {{2,1},{0,2},{-2,1},{-2,-1},{0,-2},{2,-1}};

        Hex top = new Hex(100,300,radius, ((int)Math.floor(Math.random()*4)), DIE_DIST[(int)Math.floor(Math.random()*DIE_DIST.length)]);
        Hex curr = top;

        // column of hex: 1.5, 3.5, 5.5 etc
        // row of hex: in different rows, 1,2,3,4,5 etc

        _hexes.add(top);
        while (true) {
            if (current == ring) {
                currentDir++;
                current = 0;
            }
            if (currentDir > 5) {
                currentDir = 0;
                current = 0;
                ring++;
                if (ring < rings) {
                    top = new Hex(curr.getX(),
                        (curr.getY() - 2 * (Math.cos(Math.PI/6) * (curr.getRadius()+border))),
                        curr.getRadius(), ((int)Math.floor(Math.random()*4)),
                            DIE_DIST[(int)Math.floor(Math.random()*DIE_DIST.length)]);
                    
                    curr = top;
                }
                else
                    break;
            }
            
            curr = new Hex((curr.getX() + directions[currentDir][0]*(curr.getRadius()+border)*3/2),
                        (curr.getY() + directions[currentDir][1]*(Math.cos(Math.PI/6) * (curr.getRadius()+border))),
                        curr.getRadius(), ((int)Math.floor(Math.random()*4))
                    , (int)DIE_DIST[(int)Math.floor(Math.random()*DIE_DIST.length)]);
            _hexes.add(curr);
            
            current++;
        }
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    private void callObjectAtLocation(int x, int y) {
        for (Hex o : _hexes) {
            if (Math.sqrt((o.getX()-x)*(o.getX()-x) +
                          (o.getY()-y)*(o.getY()-y)) < o.getRadius()) {
                o.clicked(x,y);
                return;
            }
        }
        //System.out.println("Didn't click on anything...");
    }

    public void paint(Graphics graphics) {
        
        Graphics2D g = (Graphics2D) graphics;
        
        Image water = Toolkit.getDefaultToolkit().getImage("water.jpg");
        g.drawImage(water, 0, 0, this);
        //g.setColor(Color.WHITE);
        //g.fillRect(0, 0, 800, 550);

        for (Hex o : _hexes) {
            o.paint(g,_display_offset[0],_display_offset[1]);
        }
        for (BoardObject o : _objects) {
            o.paint(g,_display_offset[0],_display_offset[1]);
        }
        
        if (_up != null)
            _up.paint(g);
        
    }
    
    /**
     * MOUSE EVENTS!
     */
    
    @Override
    public void mouseClicked(MouseEvent e) {
           callObjectAtLocation(e.getX()-_display_offset[0],
                                    e.getY()-_display_offset[1]);
           repaint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {repaint();}
    @Override
    public void mouseExited(MouseEvent e) {
    
        if (_up != null && (e.getY() < _h-10)) {
                sb.setUp(_up);
                _up = null;
        }
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (_up == null)
            _mousedown = new int[]{e.getX(), e.getY()};
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
        if (_up != null) {
            int[] pos = setUpNearest(e);
            _objects.add(_up);

            _up = null;
        }
        _mousedown = null;
        repaint();
    }
    
    private int[] setUpNearest(MouseEvent e) {
        
        int mousex = _up.getX()-_display_offset[0];
        int mousey = _up.getY()-_display_offset[1];
        
        int intervalUp = (int)Math.ceil(radius*0.866);
        int[] intervalSide = new int[]{(int)(radius/2),radius};
        
        int i = 0;
        int j;
                
        j = (int)Math.round((mousey-hextop)*1.0/intervalUp);
        
        
        if ((j%2)==0) {
            int glob = 1;
            for (i=0;
                    (Math.floor((i+1)/2)*radius+Math.floor(i/2)*radius*2)<mousex-hexleft-radius/2;
                    i++) glob += (((i%2)==0) ? 1 : 3);
            
            double dx = (Math.floor((i+1)/2)*radius+Math.floor(i/2)*radius*2)-(mousex-hexleft-radius/2);
            
            System.out.println("0: "+i+" , "+dx);
            
            if ((i%2)==1 && dx > radius/2) {
                i = i - 1;
                glob -= 1;
            }
            else if ((i%2)==0 && dx > radius) {
                i = i - 1;
                glob -= 3;
            }
            
            i = glob;
            
        }
        else if ((j%2)==1) {
            int glob = 0;
            for (i=0;
                    (Math.floor((i+1)/2)*radius*2+Math.floor(i/2)*radius)<mousex-hexleft;
                    i++) glob += (((i%2)==0) ? 3 : 1);
            
            double dx = (Math.floor((i+1)/2)*radius*2+Math.floor(i/2)*radius)-(mousex-hexleft);
            
            System.out.println("1: "+i+" , "+dx);
            
            if ((i%2)==0 && dx > radius/2) {
                i = i - 1;
                glob -= 1;
            }
            else if ((i%2)==1 && dx > radius) {
                i = i - 1;
                glob -= 3;
            }
            
            i = glob;
            
        }
        

        
        _up.setX(hexleft+((i-(i%2))/2*intervalSide[0]+(i-(i%2))/2*intervalSide[1]+(i%2)*intervalSide[0])-_up.getW()/2);
        _up.setY(hextop+j*intervalUp-_up.getH()/2);
        
        return new int[]{i,j};
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
       
        if (_mousedown != null) {
           _display_offset[0] += e.getX()-_mousedown[0];
           _display_offset[1] += e.getY()-_mousedown[1];
           _mousedown = new int[]{e.getX(),e.getY()};
        }
        if (_up != null) {
                _up.setX(e.getX()-_up.getW()/2);
                _up.setY(e.getY()-_up.getH()/2);
        }

       repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
            
    }
    
    public void setUp(BoardObject u) {
        try {
            r = new Robot();
        } catch (AWTException ex) {
        }
        u.setX(5);
        _up = u;
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
    }
}
