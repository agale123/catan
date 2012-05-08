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
	private int _height = 822;

	private final int ENTRANCEY = _height*2/3-50;

	private ArrayList<Card> _cards;
	private ArrayList<BoardObject> _handObjects;
	private Hashtable<Integer,Exchanger> _exchangers;

	public BoardObject _up;

	public MapPanel mp;

	public Robot r;

	private int CurrDisplay = 0; // building

	private ClientGameBoard gameLogic;

	private int[] GOTOTRADECOORD = {2,300,65,50};
	public Image tradeGraphic = Toolkit.getDefaultToolkit().getImage("catanui/tradebutton.png");
	public Image buildGraphic = Toolkit.getDefaultToolkit().getImage("catanui/buildbutton.png");
	public Image portsGraphic = Toolkit.getDefaultToolkit().getImage("catanui/portsbutton.png");
	private int[] GOTOBUILDCOORD = {133,300,65,50};
	private int[] GOTOPORTSCOORD = {68,300,65,50};

	public SideBar(ClientGameBoard gl) {

		_cards = new ArrayList<Card>();

		_up = null;

		gameLogic = gl;
		gameLogic._sideBar = this;

		_exchangers = new Hashtable<Integer,Exchanger>();
		//_exchangers.add(new Exchanger(1,10,100,new BoardObject.type[]
		//        {BoardObject.type.WOOD,BoardObject.type.WOOD},new BoardObject.type[]{BoardObject.type.ORE},5));

		_exchangers.put(1,new Exchanger(0,10,75,new BoardObject.type[]
					{BoardObject.type.WOOD,BoardObject.type.BRICK},new BoardObject.type[]{BoardObject.type.ROAD},1));

		_exchangers.put(2,new Exchanger(0,10,150,new BoardObject.type[]
					{BoardObject.type.WHEAT,BoardObject.type.SHEEP,BoardObject.type.ORE},new BoardObject.type[]{BoardObject.type.DEV},2));

		_exchangers.put(0,new Exchanger(0,10,10,new BoardObject.type[]
					{BoardObject.type.WHEAT,BoardObject.type.SHEEP,BoardObject.type.WOOD,BoardObject.type.BRICK},new BoardObject.type[]{BoardObject.type.SETTLEMENT},0));

		_exchangers.put(3,new Exchanger(0,10,225,new BoardObject.type[]
					{BoardObject.type.WHEAT,BoardObject.type.WHEAT,BoardObject.type.ORE,BoardObject.type.ORE,BoardObject.type.ORE},new BoardObject.type[]{BoardObject.type.CITY},3));

		_handObjects = new ArrayList<BoardObject>();

		addMouseListener(this);
		addMouseMotionListener(this);


	}

	public void removeTrade(int i) {
		_exchangers.remove(new Integer(i));
		repaint();
	}

	public void signalNewTrade(gamelogic.Trade t) {
		synchronized (_exchangers) {
			_exchangers.put((Integer) t.getTradeID(),
					new Exchanger(1,10,-1,
						t.getIns(),
						t.getOuts(),
						t.getTradeID()));
		}
		repaint();
	}

	public void addPort(BoardObject.type t) {
		synchronized (_exchangers) {
			_exchangers.put(100 + BoardObject.cardtypes.indexOf(t),
					new PortExchanger(10,-1,
						100 + BoardObject.cardtypes.indexOf(t),
						t));
		}
		repaint();
	}

	public void activateExchanger(int id, boolean b) {
		Exchanger ex = _exchangers.get(id);
		if(ex != null) {
			ex.switchOutB(b);
		}
	}

	public class PortExchanger extends Exchanger {

		int upto = 0;

		public PortExchanger(int x, int y, int id, BoardObject.type t) {

			super(2,x,y,new BoardObject.type[]{t,t},new BoardObject.type[]{BoardObject.type.WOOD},id);


		}

		public void onClick(int a, int b) {
			outs[0] = BoardObject.cardtypes.get(upto);
			upto++;
			upto = upto%5;
			repaint();
		}

	}

	public class TradeExchanger extends Exchanger {

		int upto = 0;

		public TradeExchanger(int x, int y, int id) {

			super(1,x,y,new BoardObject.type[2],new BoardObject.type[]{BoardObject.type.WOOD},id);

		}

		public void onClick(int a, int b) {
		    if (!done) {
			outs[0] = BoardObject.cardtypes.get(upto);
			upto++;
			upto = upto%5;
			repaint();

			gameLogic.writeProposeTrade(ins,outs,_tradeID);
		    }
		}

		public void refreshcontents(Iterator<Map.Entry<Integer, Exchanger>> it) {
			
			if (done == false) {
				ins[0] = null;ins[1] = null;
				ArrayList<Card> crds = cardsIn(_cards);
				for (int i = 0;i<Math.min(crds.size(),2);i++){
					ins[i] = crds.get(i).getType();
				}
			}
			if ((ins[0] == null) && (ins[1] == null) || (done && cardsIn(_cards).size()==0))
				synchronized (_exchangers) {
					if (!done) {
					  gameLogic.writeRemoveTrade(_tradeID);
					}
					it.remove();
				}
			else if (done == false)
				gameLogic.writeProposeTrade(ins,outs,_tradeID);

			repaint();
		}
	}

	public class Exchanger implements java.io.Serializable {
		public int _x;
		public int _y;
		public int WIDTH = 180;
		public int HEIGHT = 55;

		protected BoardObject.type[] ins;
		public BoardObject.type[] outs;

		public int _where;
		protected int _tradeID;

		public boolean done;

		public Exchanger(int where, int x, int y, BoardObject.type[] in, BoardObject.type[] out, int id) {
			//practical max of two ins or outs
			_where = where;
			_x = x; _y = y; ins = in; outs = out;
			_tradeID = id;
			done = false;
		}

		public int getID() {
			return _tradeID;
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
					Settlement i = new Settlement(_x+WIDTH-30-44,_y+5,gameLogic._playerNum);
					i.paint(g);
				}
				else if (outs[0] == BoardObject.type.ROAD) {
					Road i = new Road(_x+WIDTH-30-44,_y+25);
					i.setColor(gameLogic._playerNum);
					i.paint(g);
				}
				else if (outs[0] == BoardObject.type.CITY) {
					City i = new City(_x+WIDTH-30-44,_y+5,gameLogic._playerNum);
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
			synchronized(cards) {
				for (Card c : cards) {
					if (inside(c.getX(),c.getY(),c._w,c._h,_x-2,_y-2,WIDTH+4,HEIGHT+4))
						ret.add(c);
				}
			}

			return ret;
		}

		public ArrayList<Card> checkFull(ArrayList<Card> cards) {
			ArrayList<Card> rm = new ArrayList<Card>();
			ArrayList<Card> cs = cardsIn(cards);
			int[] sofar = new int[ins.length];
			for (int i=0;i<sofar.length;i++) {
				if (ins[i]==null)
					sofar[i] = 1;
				else
					sofar[i] = 0; //set blank
			}

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
				gameLogic.writeBuySettlement(ins,outs,_tradeID);
			}
			else if (outs[0] == BoardObject.type.CITY)
				gameLogic.writeBuyCity(ins,outs,_tradeID);
			else if (outs[0] == BoardObject.type.ROAD) {
				gameLogic.writeBuyRoad(ins,outs,_tradeID);
			}
			else if (outs[0] == BoardObject.type.DEV)
				gameLogic.writeBuyDev(ins,outs,_tradeID);
			else if (this.getClass().equals(PortExchanger.class))
				gameLogic.exchangePort(ins,outs,_tradeID);
			else  {
				gameLogic.writeDoTrade(ins,outs,_tradeID);
			}

		}

		public void switchOutB(boolean free) {
			synchronized(_cards) {
				ArrayList<Card> sw = checkFull(_cards);
				if (sw != null)
					for (Card c : sw)
						_cards.remove(c);
				if (sw !=null || free) {
					if (outs.length == 1) {
						synchronized (_handObjects) {
							if (outs[0] == BoardObject.type.SETTLEMENT) {
								Settlement i = new Settlement(_x+WIDTH-30-44,_y+5, gameLogic._playerNum);
								_handObjects.add(i);
							}
							else if (outs[0] == BoardObject.type.ROAD) {
								Road i = new Road(_x+WIDTH-30-44,_y+25);
								i.setColor(gameLogic._playerNum);
								_handObjects.add(i);
							}
							else if (outs[0] == BoardObject.type.CITY) {
								City i = new City(_x+WIDTH-30-44,_y+5, gameLogic._playerNum);
								_handObjects.add(i);
							}
							else {
								Card i1 = new Card(_x+WIDTH-30-44,_y+5,outs[0]);
								i1.setLoc(_where);
								_cards.add(i1);
							}
						}
					}

					else {
						if (outs[0] != null) {
							Card i1 = new Card(_x+WIDTH-37,_y+5,outs[0]);
							i1.setLoc(_where);
							_cards.add(i1);
						}
						if (outs[1] != null) {
							Card i2 = new Card(_x+WIDTH-30-44,_y+5,outs[1]);
							i2.setLoc(_where);
							_cards.add(i2);
						}
					}
					if (getID() > 800 || getID() == 0)
						done = true;
					repaint();
				}
				else {
					System.out.println("Error: cards have disappeared since request to exchange");
					if (_up.getType().equals(ins[0]) || _up.getType().equals(ins[1]))
						_up = null;
				}
			}
		}

	}

	@Override
		public synchronized void paint(Graphics g) {

			g.setColor(Color.GRAY);
			g.fillRect(0, 0, _width, _height);
			g.setColor(Color.DARK_GRAY);
			g.drawLine(5, _height*2/3, _width-5, _height*2/3);
			g.setColor(new Color(200,200,200));
			g.fillRect(5,_height*2/3+5, _width - 10, _height*1/3-10);

			synchronized (_exchangers) {
				int currY = 50;
				ArrayList<Integer> curr = new ArrayList<Integer>();
				for (Integer e : _exchangers.keySet()) {
				      Exchanger e1 = _exchangers.get(e);
				      if (e1._y != -1 && CurrDisplay == e1._where)
					    curr.add(e1._y);
				}
				for (Integer e : _exchangers.keySet()) {
					Exchanger e1 = _exchangers.get(e);
					if (e1._where == CurrDisplay) {
						if (e1._y == -1) {
							do { currY += 65; }
							while (curr.contains(currY));
							if (currY < _height*2/3 - 40) {
							    e1._y = currY;
							    curr.add(currY);
							    e1.paint(g);
							}
						}
						else
							e1.paint(g);
					}
				}
			}

			g.drawImage(tradeGraphic, GOTOTRADECOORD[0],GOTOTRADECOORD[1],GOTOTRADECOORD[2],GOTOTRADECOORD[3],  null);
			g.drawImage(buildGraphic, GOTOBUILDCOORD[0],GOTOBUILDCOORD[1],GOTOBUILDCOORD[2],GOTOBUILDCOORD[3],  null);
			g.drawImage(portsGraphic, GOTOPORTSCOORD[0],GOTOPORTSCOORD[1],GOTOPORTSCOORD[2],GOTOPORTSCOORD[3],  null);

			synchronized (_cards) {	
				for (Card c : _cards) {
					if (c.getLoc() == CurrDisplay || c.getLoc() == -1)
						c.paint(g);
				}
			}

			synchronized (_handObjects) {
				for (BoardObject o : _handObjects)
					if (o.getLoc() == CurrDisplay || o.getLoc() == -1)
						o.paint(g);
			}



			if (_up != null)
				_up.paint(g);
		}


	public void addCard(BoardObject.type type) {
		synchronized(_cards) {
			_cards.add(new Card(getNextEntranceX(),ENTRANCEY,type));
		}
		repaint();
	}

	public void removeAllCards(BoardObject.type type) {
		synchronized(_cards) {
			Iterator iter = _cards.iterator();
			while (iter.hasNext()) {
				if (((Card)iter.next()).getType() == type)
					iter.remove();
			}
		}
		repaint();
	}

	private int getNextEntranceX() {
		synchronized (_cards) {
			boolean found = false;
			for (int ij=20;ij<_width;ij+=15) {
				for (int i=0;i<_cards.size();i++) {

					if (_cards.get(i).getY() == ENTRANCEY && _cards.get(i).getX() == ij) {
						found = true;
						break;
					}
				}
				if (found == false)
					return ij;
				found = false;
			}
		}
		return 20 + (int)Math.floor(Math.random()*6)*15;
	}


	@Override
		public void mouseClicked(MouseEvent me) {
			//addCard(BoardObject.type.WHEAT);
			if (collides(me.getX(),me.getY(),2,2,GOTOTRADECOORD[0],GOTOTRADECOORD[1],GOTOTRADECOORD[2],GOTOTRADECOORD[3])) {
				if (CurrDisplay == 1) {
					int randid = (int)Math.floor(Math.random()*6819203+1000);
					synchronized (_exchangers) {
						_exchangers.put(randid,new TradeExchanger(10,30,randid));
					}
				}
				CurrDisplay = 1;
			}
			else if (collides(me.getX(),me.getY(),2,2,GOTOBUILDCOORD[0],GOTOBUILDCOORD[1],GOTOBUILDCOORD[2],GOTOBUILDCOORD[3]))
				CurrDisplay = 0;
			else if (collides(me.getX(),me.getY(),2,2,GOTOPORTSCOORD[0],GOTOPORTSCOORD[1],GOTOPORTSCOORD[2],GOTOPORTSCOORD[3]))
				CurrDisplay = 2;
			else {
				synchronized (_exchangers) {
					for (Integer e1 : _exchangers.keySet()) {
						Exchanger e = _exchangers.get(e1);
						if (e.getClass() == TradeExchanger.class && CurrDisplay == 1) {
							if (collides(me.getX(),me.getY(),3,3,e._x,e._y,e.WIDTH,e.HEIGHT))
								((TradeExchanger)e).onClick(me.getX(),me.getY());
						}
						if (e.getClass() == PortExchanger.class && CurrDisplay == 2) {
							if (collides(me.getX(),me.getY(),3,3,e._x,e._y,e.WIDTH,e.HEIGHT))
								((PortExchanger)e).onClick(me.getX(),me.getY());
						}
					}
				}
			}

			synchronized (_cards) {
				if (me.getButton() == MouseEvent.BUTTON3) {
					Card c;
					for (int i=_cards.size()-1;i>=0;i--) {
						c = _cards.get(i);
						if (collides(c._x,c._y,c._w,c._h,me.getX(),me.getY(),3,3) && c.getType()==BoardObject.type.DEV && (c.getLoc() == CurrDisplay || c.getLoc() == -1)) {
							gameLogic.useDevCard();

							_cards.remove(c);
							break;
						}
					}
				}
			}

			repaint();

		}

	@Override
		public void mousePressed(MouseEvent me) {
			synchronized (_handObjects) {
				Iterator<BoardObject> iter = _handObjects.iterator();
				while (iter.hasNext()) {
					BoardObject o = iter.next();
					if (collides(o.getX(),o.getY(),o.getW(),o.getH(),me.getX(),me.getY(),3,3) && (o.getLoc() == CurrDisplay || o.getLoc() == -1)) {
						_up = o;
						iter.remove();
						return;
					}
				}
			}

			Card c;
			synchronized (_cards) {
				for (int i=_cards.size()-1;i>=0;i--) {
					c = _cards.get(i);
					if (collides(c._x,c._y,c._w,c._h,me.getX(),me.getY(),3,3) && (c.getLoc() == CurrDisplay || c.getLoc() == -1)) {
						_up = _cards.remove(_cards.indexOf(c));
						return;
					}
				}
			}
		}

	@Override
		public void mouseReleased(MouseEvent me) {
			ArrayList<Card> sw;
			synchronized (_cards) {
				synchronized (_exchangers) {
					if (_up != null) {
						if (Card.class.isInstance(_up))
							_cards.add((Card)_up);
						else {
							_handObjects.add(_up);
						}
						_up = null;
					}
					Iterator<Map.Entry<Integer, Exchanger>> i = _exchangers.entrySet().iterator();
					while(i.hasNext()) {
						Integer e = i.next().getKey();
						//for (Integer e : _exchangers.keySet()) {
						Exchanger e1 = _exchangers.get(e);
						if (e1.getClass() == TradeExchanger.class)
							((TradeExchanger)e1).refreshcontents(i);
						else if ((e1.getID() > 800) && (e1.cardsIn(_cards).size()==0) && e1.done == true) {
							// _exchangers.remove(e);
							i.remove();
							repaint();
						}
						else {

							sw = e1.checkFull(_cards);

							if (sw != null) {
								e1.switchOut(sw);
							}
						}
						}
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
							_up.setX(Math.max(Math.min(_width-_up.getW()-5, me.getX()-_up.getW()/2),0));
							_up.setY(Math.max(Math.min(me.getY()-_up.getH()/2,_height-_up.getH()-5),0));

						}
						else {
							_up.setX(Math.max(Math.min(_width-_up.getW()-5, me.getX()-_up.getW()/2),0));
							_up.setY(Math.max(Math.min(me.getY()-_up.getH()/2,_height-_up.getH()-5),0));
						}
						if (me.getY() < 300)
							_up.setLoc(CurrDisplay);
						else
							_up.setLoc(-1);
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
				/* try {
				   r = new Robot();
				   } catch (AWTException ex) {
				   }*/
				_up = u;
				_up.setX(_width-u.getW()-5);
				/*r.mouseMove(x-10,y);
				  r.mouseRelease(InputEvent.BUTTON1_MASK);
				  r.mousePress(InputEvent.BUTTON1_MASK);*/
			}
		}
