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
    //private ArrayList<BoardObject> _objects;
    
    private int[] _mousedown;
    private int[] _display_offset = {500,0};
    
    private HashMap<CoordPair,Pair> vertexContents;
    private HashMap<Pair,Integer> roadContents;
    private HashMap<Pair,BoardObject.type> portContents;

    public BoardObject _up;
    
    public SideBar sb;
    
    public int _x;
    public int _h;

    private int hexleft;
    private int hextop;
    private int radius = 75;
    
    private int _dieRoll;
    private int[] twoDice;
    
    private String _gameOver = "";
    private float _currAlpha = (float)0.0;

    int intervalUp = (int)Math.ceil(radius*0.866);
    int[] intervalSide = new int[]{(int)(radius/2),radius};
    int rings;
    private final int[] DIE_DIST = {2,3,4,4,5,5,5,6,6,8,8,9,9,9,10,10,11,12};
    private BufferedImage diceImage;
    //Robot r;

    private ClientGameBoard gameLogic;
    
    public MapPanel(ClientGameBoard gl) {
        super();
	gameLogic = gl;
	gameLogic._mapPanel = this;
        _hexes = new ArrayList<Hex>();
        //_objects = new ArrayList<BoardObject>();
	vertexContents = new HashMap<CoordPair,Pair>();
	roadContents = new HashMap<Pair,Integer>();
	portContents = new HashMap<Pair,BoardObject.type>();
        
	diceImage = new BufferedImage(582, 98, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = diceImage.createGraphics();
    	g.drawImage(BoardObject.images.get(BoardObject.type.DICE), null, null);
	g.dispose();

        rings = gameLogic.getNumRings();
	
	hexleft = 100 - (int)(radius+(Math.floor(rings/2)*radius+Math.floor((rings-1)/2)*radius*2));
	if (rings%2==0) {
	    hexleft -= radius/2;
        }
	
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
		repaint();

	}

	public void updateEdgeContents(HashMap<Pair,Integer> newy) {
	
		roadContents = newy;
		repaint();

	}

	public void updatePortContents(HashMap<Pair,BoardObject.type> newy) {
	
		portContents = newy;
		repaint();

	}

	public void updateRoll(int i) {
		_dieRoll = i;
		twoDice = new int[]{(int)Math.max(Math.floor(Math.random()*Math.min(_dieRoll-1,5))+1,_dieRoll-6),1};
		twoDice[1] = _dieRoll - twoDice[0];
		repaint();
	}

	public void gameOver(String s) {
		_gameOver = s;
	}

    public void paint(Graphics graphics) {
        
        Graphics2D g = (Graphics2D) graphics;

        Image water = Toolkit.getDefaultToolkit().getImage("catanui/water.jpg");
        g.drawImage(water, 0, 0, this);

        for (Hex o : _hexes) {
            o.paint(g,_display_offset[0],_display_offset[1]);
        }

	g.translate(_display_offset[0],_display_offset[1]);
	for (Pair c : portContents.keySet()) {
		
		int lowx = hexleft+(((CoordPair)c.getA()).getX()-(((CoordPair)c.getA()).getX()%2))/2*intervalSide[0]+(((CoordPair)c.getA()).getX()-(((CoordPair)c.getA()).getX()%2))/2*intervalSide[1]+(((CoordPair)c.getA()).getX()%2)*intervalSide[0];
		int lowy = hextop+((CoordPair)c.getA()).getY()*intervalUp;
		int highx = hexleft+(((CoordPair)c.getB()).getX()-(((CoordPair)c.getB()).getX()%2))/2*intervalSide[0]+(((CoordPair)c.getB()).getX()-(((CoordPair)c.getB()).getX()%2))/2*intervalSide[1]+(((CoordPair)c.getB()).getX()%2)*intervalSide[0];
		int highy = hextop+((CoordPair)c.getB()).getY()*intervalUp;
		
		int dx = highx - lowx;
		int dy = highy - lowy;
		double rad = Math.atan((1.0)*dy/dx);
		
		if (dx < 0)
		    rad += Math.PI;

		g.translate(lowx,lowy);
		g.rotate(rad);
		g.drawImage(BoardObject.images.get(BoardObject.type2port.get(portContents.get(c))),0,-77,null);
		g.rotate(-rad);
		g.translate((-1)*lowx,(-1)*lowy);
	}
	g.translate((-1)*_display_offset[0],(-1)*_display_offset[1]);

	for (Pair c : roadContents.keySet()) {
		
		Road r = new Road(hexleft+(((CoordPair)c.getA()).getX()-(((CoordPair)c.getA()).getX()%2))/2*intervalSide[0]+(((CoordPair)c.getA()).getX()-(((CoordPair)c.getA()).getX()%2))/2*intervalSide[1]+(((CoordPair)c.getA()).getX()%2)*intervalSide[0],hextop+((CoordPair)c.getA()).getY()*intervalUp);

		r.setX2(hexleft+(((CoordPair)c.getB()).getX()-(((CoordPair)c.getB()).getX()%2))/2*intervalSide[0]+(((CoordPair)c.getB()).getX()-(((CoordPair)c.getB()).getX()%2))/2*intervalSide[1]+(((CoordPair)c.getB()).getX()%2)*intervalSide[0]);
		r.setY2(hextop+((CoordPair)c.getB()).getY()*intervalUp);

		r.setColor(roadContents.get(c));
		r.paint(g,_display_offset[0],_display_offset[1]);
	}

	for (CoordPair c : vertexContents.keySet()) {
		int newx = hexleft+((c._x-(c._x%2))/2*intervalSide[0]+(c._x-(c._x%2))/2*intervalSide[1]+(c._x%2)*intervalSide[0])-20;
	    int newy = hextop+c._y*intervalUp-20;

		if ((BoardObject.type)(vertexContents.get(c).getA()) == BoardObject.type.SETTLEMENT) {
			Settlement s = new Settlement(newx,newy,(Integer)(vertexContents.get(c).getB()));
			s.paint(g,_display_offset[0],_display_offset[1]);
		}
		else if ((BoardObject.type)(vertexContents.get(c).getA()) == BoardObject.type.CITY) {
			City s = new City(newx,newy,(Integer)(vertexContents.get(c).getB()));
			s.paint(g,_display_offset[0],_display_offset[1]);
		}
		else
			System.out.println("neither -_-");
	}

	

	g.setColor(Color.GRAY);
	g.fill(new Rectangle(0,0,110,60));
	g.setColor(Color.LIGHT_GRAY);
	g.fill(new Rectangle(3,3,104,56));
	if (_dieRoll > 0) {
	    
		BufferedImage r1img = diceImage.getSubimage((int)(Math.floor((twoDice[0]-1)*94.7)),0,95,94);
		g.drawImage(r1img,5,7,48,47,null);
		BufferedImage r2img = diceImage.getSubimage((int)(Math.floor((twoDice[1]-1)*94.7)),0,95,94);
		g.drawImage(r2img,55,7,48,47,null);
	}
        
        if (_up != null)
            _up.paint(g);
        
	if (!_gameOver.equals("") && _currAlpha < 1.0) {
	    g.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, _currAlpha));
	    g.setColor(Color.GRAY);
	    g.drawRect(0,0,1000,650);
	    g.setColor(Color.WHITE);
	    g.drawString(_gameOver,100,500);
	    _currAlpha += 0.01;
	    repaint();
	}
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

			else if ((_up.getType() == BoardObject.type.ROAD) && (((Road)_up).oneDown == true)) {

				gameLogic.writeBuildRoad(((Road)_up).mycoord[0],((Road)_up).mycoord[1],pos[0],pos[1]);
			}

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
            	_up = null;
				_mousedown = null;
			}


            
        }
        repaint();
    }
    
    private int[] setUpNearest(MouseEvent e) {
        
        int mousex = e.getX()-_display_offset[0];
        int mousey = e.getY()-_display_offset[1];
        
        int i = 0;
        int j;
                
        j = (int)Math.round((mousey-hextop)*1.0/intervalUp);
        
        
        if ((j%2+(rings-1)%2)==0 || (((j-1)%2+(rings)%2)==0)) {
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
        else {
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
