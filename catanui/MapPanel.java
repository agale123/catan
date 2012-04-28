package catanui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import gamelogic.*;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private ArrayList<Hex> _hexes;
    private ArrayList<BoardObject> _objects;
    
    private int[] _mousedown;
    private int[] _display_offset = {300,0};
    
    private HashMap<CoordPair,Pair> vertexContents;
    private HashMap<Pair,Integer> roadContents;

    public BoardObject _up;
    
    public SideBar sb;
    
    public int _x;
    public int _h;

    private int hexleft;
    private int hextop;
    private int radius = 75;
    
    int intervalUp = (int)Math.ceil(radius*0.866);
    int[] intervalSide = new int[]{(int)(radius/2),radius};

    private final int[] DIE_DIST = {2,3,4,4,5,5,5,6,6,8,8,9,9,9,10,10,11,12};
    
    //Robot r;

    private ClientGameBoard gameLogic;
    
    public MapPanel(ClientGameBoard gl) {
        super();
	gameLogic = gl;
	gameLogic._mapPanel = this;
        _hexes = new ArrayList<Hex>();
        _objects = new ArrayList<BoardObject>();
	vertexContents = new HashMap<CoordPair,Pair>();
        
	/*try {
            r = new Robot();
        } catch (AWTException ex) {
	    System.out.println("Robot no work.");
        }*/
        int rings = gameLogic.getNumRings();
        
        hexleft = 100-(radius+radius*((rings-1)%2)+(rings-((rings-1)%2))/2*3*radius);
        hextop = 300-(int)(radius*0.866 + (rings-1)*2*(radius * 0.866));
        
        double border = 0.4;

	HashMap<Pair,Pair> hexData = gameLogic.getHexInfo(); // call the gamelogic

	Pair currCoord = gameLogic.getStartPoint();
	Pair topCoord = currCoord;

	int ring = 0;
	
        int currentDir = 5;
        int current = 0;
        int[][] directions = {{1,1},{0,2},{-1,1},{-1,-1},{0,-2},{1,-1}};

        int[][] HexCoordDirections = {{2,1},{0,2},{-2,1},{-2,-1},{0,-2},{2,-1}};

        Hex top = new Hex(100,300,radius, (BoardObject.type)(hexData.get(currCoord).getA()), (Integer)(hexData.get(currCoord).getB()));
        Hex curr = top;

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
		    topCoord = new Pair(currCoord.getA(),(Double)(currCoord.getB())-2);
		    currCoord = topCoord;

                    top = new Hex(curr.getX(),
                        (curr.getY() - 2 * (Math.cos(Math.PI/6) * (curr.getRadius()+border))),
                        curr.getRadius(), (BoardObject.type)(hexData.get(currCoord).getA()), (Integer)(hexData.get(currCoord).getB()));
                    curr = top;

                }
                else
                    break;
            }
            currCoord.setA((Object)((Double)(currCoord.getA())+HexCoordDirections[currentDir][0]));
	    currCoord.setB((Object)((Double)(currCoord.getB())+HexCoordDirections[currentDir][1]));

            curr = new Hex((curr.getX() + directions[currentDir][0]*(curr.getRadius()+border)*3/2),
                        (curr.getY() + directions[currentDir][1]*(Math.cos(Math.PI/6) * (curr.getRadius()+border))),
                        curr.getRadius(), (BoardObject.type)(hexData.get(currCoord).getA()), (Integer)(hexData.get(currCoord).getB()));
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
    }

	public void updateVertexContents(HashMap<CoordPair,Pair> newy) {
	
		vertexContents = newy;

	}

    public void paint(Graphics graphics) {
        
        Graphics2D g = (Graphics2D) graphics;
        
        Image water = Toolkit.getDefaultToolkit().getImage("catanui/water.jpg");
        g.drawImage(water, 0, 0, this);
        //g.setColor(Color.WHITE);
        //g.fillRect(0, 0, 800, 550);

        for (Hex o : _hexes) {
            o.paint(g,_display_offset[0],_display_offset[1]);
        }
        for (BoardObject o : _objects) {
            o.paint(g,_display_offset[0],_display_offset[1]);
        }
	for (CoordPair c : vertexContents.keySet()) {
		int newx = hexleft+((c._x-(c._x%2))/2*intervalSide[0]+(c._x-(c._x%2))/2*intervalSide[1]+(c._x%2)*intervalSide[0])-20;
        	int newy = hextop+c._y*intervalUp-20;
		g.fillRect(newx,newy,40,40);
		//vertexContents.get(c);
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
                sb.setUp(_up,e.getX(),e.getY());
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

			if (_up.getType() == BoardObject.type.SETTLEMENT)

				gameLogic.writeBuildSettlement(pos[0],pos[1]);

			else if (_up.getType() == BoardObject.type.CITY)

				gameLogic.writeBuildCity(pos[0],pos[1]);

			else if ((_up.getType() == BoardObject.type.ROAD) && (((Road)_up).oneDown == true))

				gameLogic.writeBuildRoad(((Road)_up).mycoord[0],((Road)_up).mycoord[1],pos[0],pos[1]);
		

			if (_up.getType() == BoardObject.type.ROAD) {
				if (((Road)_up).oneDown == false) {

					_up.setX(hexleft+((pos[0]-(pos[0]%2))/2*intervalSide[0]+(pos[0]-(pos[0]%2))/2*intervalSide[1]+(pos[0]%2)*intervalSide[0])+_display_offset[0]);
					_up.setY(hextop+pos[1]*intervalUp+_display_offset[1]);

					((Road)_up).oneDown = true;
					((Road)_up).mycoord = pos;
					_mousedown = null;
					e.consume();
				}
				else {
					((Road)_up).setX2(hexleft+((pos[0]-(pos[0]%2))/2*intervalSide[0]+(pos[0]-(pos[0]%2))/2*intervalSide[1]+(pos[0]%2)*intervalSide[0]));
					((Road)_up).setY2(hextop+pos[1]*intervalUp);
					((Road)_up).oneDown = true;
					_up = null;
					_mousedown = null;
				}
			}
			else {
				_up.setX(hexleft+((pos[0]-(pos[0]%2))/2*intervalSide[0]+(pos[0]-(pos[0]%2))/2*intervalSide[1]+(pos[0]%2)*intervalSide[0])-_up.getW()/2);
				_up.setY(hextop+pos[1]*intervalUp-_up.getH()/2);
				_objects.add(_up);
            	_up = null;
				_mousedown = null;
			}


            
        }
        repaint();
    }
    
    private int[] setUpNearest(MouseEvent e) {
        
        int mousex = _up.getX()-_display_offset[0];
        int mousey = _up.getY()-_display_offset[1];
        
        int i = 0;
        int j;
                
        j = (int)Math.round((mousey-hextop)*1.0/intervalUp);
        
        
        if ((j%2)==0) {
            int glob = 1;
            for (i=0;
                    (Math.floor((i+1)/2)*radius+Math.floor(i/2)*radius*2)<mousex-hexleft-radius/2;
                    i++) glob += (((i%2)==0) ? 1 : 3);
            
            double dx = (Math.floor((i+1)/2)*radius+Math.floor(i/2)*radius*2)-(mousex-hexleft-radius/2);
            

            
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
        
        return new int[]{i,j};
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
       
        if (_mousedown != null && _up == null) {
           _display_offset[0] += e.getX()-_mousedown[0];
           _display_offset[1] += e.getY()-_mousedown[1];
           _mousedown = new int[]{e.getX(),e.getY()};
        }
        if (_up != null) {
		if (_up.getType() == BoardObject.type.ROAD) {
			if (((Road)_up).oneDown == false) {
				_up.setX(e.getX());
				_up.setY(e.getY());
			}
			else {
				((Road)_up).setX2(e.getX());
				((Road)_up).setY2(e.getY());
			}
		}
		else {
                	_up.setX(e.getX()-_up.getW()/2);
               	 	_up.setY(e.getY()-_up.getH()/2);
		}
        }

       repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
            if (_up != null)
				mouseDragged(e);
    }
    
    public void setUp(BoardObject u, int x, int y) {

        u.setX(5);
        _up = u;
	
	/*r.mouseMove(x+10,y);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);*/
    }
}
