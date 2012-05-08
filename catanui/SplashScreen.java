/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
 
 
 /**
  * The Splash screen is the menu that will launch you into either the server or client
  */

package catanui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author agale
 */
public class SplashScreen extends JPanel{
    private boolean _host;
    private int _AIPlayers;
    private int _boardSize;
    private int _rollTimer;

    private String _hostname = "localhost";
    private String _port = "7777";
    private String _numCon = "1";
    private String _numAI = "0";
    private String _name = "Fred";
    private String _roll = "20";
    private String _error = "";
    
    private static JFrame _mainFrame;
    
    private server.Server _server;

    private int _screen;
    /*
     * 1 : First Splash Screen
     * 2 : Hosting Screen
     * 3 : Client Connect Screen
     * 4 : Error Screen
     * 5 : Instructions
     * 6 : Waiting for Connections
     * 7 : Connecting
     * 8 : Plot
     */

    public static void main(String[] args) {
        _mainFrame = new JFrame("Menu");
        _mainFrame.setSize(1000, 722);
        _mainFrame.setResizable(false);
        _mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SplashScreen s = new SplashScreen();
        _mainFrame.add(s);
        _mainFrame.setVisible(true);
    }

    public SplashScreen() {
        super();
        
        _screen = 1;
        repaint();
    }

	
	// Display the background and then the correct screen
    public void paintComponent(Graphics graphics) {
        JPanel j = displayBackground(graphics);

        switch(_screen) {
            case 1 :
                displayMain(j);
                break;
            case 2 :
                displayHost(j);
                break;
            case 3 :
                displayClient(j);
                break;
			case 4 :
				displayError(j, _error);
				break;
            case 5 :
                displayInstructions(j);
                break;
            case 6 :
                displayWaiting(j);
                break;
            case 8 :
				displayInstructions2(j);
				break;
            case 9 : 
				displayPlot(j);
				break;
            default:
                displayConnecting(j);
                break;

        }
		add(j);
        setVisible(true);
    }
    
    // reset the Default values
    private void resetDefaults() {
		_hostname = "localhost";
		_port = "7777";
		_numCon = "1";
		_numAI = "0";
		_name = "Fred";
		_roll = "20";
		_error = "";
    }
    
    // Draw the background image and main title
    private JPanel displayBackground(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		setLayout(null);
        Image water = Toolkit.getDefaultToolkit().getImage("catanui/title.jpeg");
        g.drawImage(water, 0, 0, 1000, 722, this);

        Color color = new Color(1,1,1,0.75f);
        g.setPaint(color);
        g.fill(new Rectangle(350,0,500,722));

        setLayout(null);
        setBounds(0,0,1000,722);
        JLabel title = new JLabel("Hexcraft");
        title.setOpaque(false);
        title.setBounds(450,10,800,40);
        title.setFont(new Font("SansSerif",Font.PLAIN, 30));

        JPanel j = new JPanel();
        j.setBounds(0,0,1000,722);
        j.setOpaque(false);
		j.setLayout(null);
        j.add(title);
        return j;
    }
    
    // Display the plot screen
    private void displayPlot(JPanel j) {
		try {
			BufferedImage plotpic = ImageIO.read(new File("catanui/plottext.png"));
			JLabel plottext = new JLabel(new ImageIcon( plotpic ));
			plottext.setBounds(375,-200,450,1000);
			add(plottext);
		}
		catch (java.io.IOException ex) {}

		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
    }
    
    // Display the trying to connect to server screen
    private void displayConnecting(JPanel j) {
		JLabel instr = new JLabel("Trying to connect to server...");
		instr.setFont(new Font("SansSerif",Font.PLAIN, 20));
		instr.setBounds(450,100,300,200);
		j.add(instr);

		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
    }
	
	// Display the waiting for incoming connections screen
	private void displayWaiting(JPanel j) {
		JLabel instr = new JLabel("<HTML>Waiting for incoming connections <BR />on port " + _port + "<HTML>");
		instr.setFont(new Font("SansSerif",Font.PLAIN, 20));
		instr.setBounds(450,100,400,100);
		j.add(instr);

		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
	}
	// Display the instructions screen
	private void displayInstructions(JPanel j) {
		try {
			BufferedImage plotpic = ImageIO.read(new File("catanui/instructions1.png"));
			JLabel plottext = new JLabel(new ImageIcon( plotpic ));
			plottext.setBounds(375,-200,450,1000);
			add(plottext);
		}
		catch (java.io.IOException ex) {}

		JLabel back = new JLabel("Continue");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginInstructions2();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
	}


	// Display the instructions2 screen
	private void displayInstructions2(JPanel j) {
		try {
			BufferedImage plotpic = ImageIO.read(new File("catanui/instructions2.png"));
			JLabel plottext = new JLabel(new ImageIcon( plotpic ));
			plottext.setBounds(375,-200,450,1000);
			add(plottext);
		}
		catch (java.io.IOException ex) {}

		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
	}
	
	// Display the server setup screen
    private void displayHost(JPanel j) {
		JLabel numConnections = new JLabel("Number of Connections");
		numConnections.setFont(new Font("SansSerif",Font.PLAIN, 20));
		numConnections.setBounds(450,160,250,40);
		j.add(numConnections);

		JTextField connections = new JTextField("1");
		connections.setBounds(450,200,200,40);
		connections.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._numCon = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});
		j.add(connections);


		JLabel numAI = new JLabel("Number of AI Players");
		numAI.setFont(new Font("SansSerif",Font.PLAIN, 20));
		numAI.setBounds(450,240,250,40);
		j.add(numAI);
		

		JTextField AIplayers = new JTextField("0");
		AIplayers.setBounds(450,280,200,40);
		AIplayers.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._numAI = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});
		j.add(AIplayers);                
		

		JLabel portText = new JLabel("Port number:");
		portText.setFont(new Font("SansSerif",Font.PLAIN, 20));
		portText.setBounds(450,320,200,40);
		j.add(portText);


		JTextField port = new JTextField("7777");
		port.setBounds(450,360,200,40);
		j.add(port);
		port.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._port = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});
		
		JLabel rollText = new JLabel("Roll interval:");
		rollText.setFont(new Font("SansSerif",Font.PLAIN, 20));
		rollText.setBounds(450,400,200,40);
		j.add(rollText);


		JTextField roll = new JTextField("20");
		roll.setBounds(450,440,200,40);
		j.add(roll);
		roll.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._roll = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});
		
		

		JLabel listen = new JLabel("Start Server");
		listen.setFont(new Font("SansSerif",Font.PLAIN, 20));
		listen.setBounds(450,480,250,40);
		listen.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if((_port != null)&& (_numCon != null || _numAI != null)) {
						SplashScreen.this.beginWaiting();
					}
				}
			});
		j.add(listen);

		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
    }
    
    // Display the main screen
	private void displayMain(JPanel j) {
		JLabel host = new JLabel("Host Game");
		host.setFont(new Font("SansSerif",Font.PLAIN, 30));
		host.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHost();
					}
				}
		);
		host.setOpaque(false);
		host.setBounds(450,310,200,40);
		j.add(host);

		JLabel client = new JLabel("Join Game");
		client.setFont(new Font("SansSerif",Font.PLAIN, 30));
		client.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginClient();
					}
				}
		);
		client.setOpaque(false);
		client.setBounds(450,360,200,40);
		j.add(client);

		JLabel instructions = new JLabel("Instructions");
		instructions.setFont(new Font("SansSerif",Font.PLAIN, 30));
		instructions.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginInstructions();
					}
				}
		);
		instructions.setOpaque(false);
		instructions.setBounds(450,470,200,40);
		j.add(instructions);
		
		JLabel plot = new JLabel("Plot");
		plot.setFont(new Font("SansSerif",Font.PLAIN, 30));
		plot.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginPlot();
					}
				}
		);
		plot.setOpaque(false);
		plot.setBounds(450,530,200,40);
		j.add(plot);
	}
	
	// Display the client settings screen
	private void displayClient(JPanel j) {
		JLabel portText = new JLabel("Port number:");
		portText.setFont(new Font("SansSerif",Font.PLAIN, 20));
		portText.setBounds(450,200,200,40);
		j.add(portText);


		JTextField port = new JTextField("7777");
		port.setBounds(450,240,200,40);
		j.add(port);
		port.addKeyListener(new MyKeyListener(_port));
		port.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._port = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});

		JLabel hostText = new JLabel("Host name:");
		hostText.setFont(new Font("SansSerif",Font.PLAIN, 20));
		hostText.setBounds(450,280,200,40);
		j.add(hostText);


		JTextField hostVal = new JTextField("localhost");
		hostVal.setBounds(450,320,200,40);
		j.add(hostVal);
		hostVal.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._hostname = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});
		
		JLabel yourName = new JLabel("Your name:");
		yourName.setFont(new Font("SansSerif",Font.PLAIN, 20));
		yourName.setBounds(450,360,200,40);
		j.add(yourName);


		JTextField name = new JTextField("Fred");
		name.setBounds(450,400,200,40);
		j.add(name);
		name.addKeyListener(
				new KeyListener() {
					public void keyReleased(KeyEvent k) {
						SplashScreen.this._name = ((JTextField) k.getSource()).getText();
					}
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
		});

		JLabel connect = new JLabel("Connect");
		connect.setFont(new Font("SansSerif",Font.PLAIN, 20));
		connect.setBounds(450,440,250,40);
		connect.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if((_port != null)&& (_hostname != null)) {
						SplashScreen.this.beginConnect();
					}
				}
			});
		j.add(connect);


		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
	}
	
	// Display the error screen
	private void displayError(JPanel j, String e) {
		JTextArea error = new JTextArea(e);
		error.setFont(new Font("SansSerif",Font.PLAIN, 15));
		error.setLineWrap(true);
		error.setWrapStyleWord(true);
		error.setOpaque(false);
		error.setBounds(365,150,490,400);
		j.add(error);
		
		JLabel back = new JLabel("Back");
		back.setFont(new Font("SansSerif",Font.PLAIN, 30));
		back.addMouseListener(
				new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						SplashScreen.this.beginHome();
					}
				}
		);
		back.setBounds(450,570,200,40);
		j.add(back);
	}
   
	// Changing screens menthods 
    private void beginHost() {
		resetDefaults();
       _screen = 2;
       this.removeAll();
       repaint();
    }

    private void beginClient() {
		resetDefaults();
        _screen = 3;
        this.removeAll();
        repaint();
    }

    private void beginInstructions() {
        _screen = 5;
        this.removeAll();
        repaint();
    }
    
    private void beginInstructions2() {
		_screen = 8;
        this.removeAll();
        repaint();
    }

	private void beginPlot() {
        _screen = 9;
        this.removeAll();
        repaint();
    }

    private void beginSettings() {
        _screen = 4;
        this.removeAll();
        repaint();
    }

    public void beginHome() {
        _screen = 1;
        this.removeAll();
        repaint();
    }
    
    public void beginError(String message) {
		_error = message;
		_screen = 4;
		this.removeAll();
		repaint();
    }
    
    
    // Begins a server
    private void beginWaiting() {
		try {
			int port = Integer.parseInt(_port);
			if (port < 1024 || port > 65535) {
				beginError("Port out of range! Is your friend telling you the wrong port? Silly him.");
				return;
			}
			int ai = Integer.parseInt(_numAI);
			if (ai < 0 || ai > 5) {
				beginError("You want to many or too few AI players. Shame on you, and please adjust your selected number to a reasonable number for a game with 6 max players.");;
				return;
			}
			int con = Integer.parseInt(_numCon);
			if (con < 0 || con + ai > 6) {
				beginError("Make sure that the number of people playing + AI players + yourself works out to be less than or equal to six!");
				return;
			}
			int rollInterval = Integer.parseInt(_roll);
			if (rollInterval < 1) {
				beginError("Hey, you can't set the roll interval to be "+rollInterval+"! Use your brain next time.");
				return;
			}
			_server = new server.Server(port, con, ai, this, rollInterval);
			_server.start();
			_screen = 6;
			this.removeAll();
			repaint();
        } catch (NumberFormatException e) {
				beginError("Port number, roll interval, and number of clients should be numbers, right? Yeah, pretty sure. *sigh*");
			
        } catch (IOException e) {
			beginError("Uh... we can't host on that port for some reason! Probably you have another process listening on that port. Try another?");
        } catch(Exception e) {
			e.printStackTrace();
        }
    }
    
    // Begins a client
    private void beginConnect() {
		try {
			int port = Integer.parseInt(_port);
			client.Client client = new client.Client(port, _hostname, _name, this);
			client.start();
			_screen = 7;
			this.removeAll();
			repaint();
        } catch (NumberFormatException e) {
			beginError("Invalid port value");
			
        } catch (IOException e) {
			beginError("Cannot connect to that server");
        } catch (ClassNotFoundException e) {
			beginError("Cannot connect to that server");
        } 
    }
    
    // Close the window
    public void close() {
		_mainFrame.dispose();
    }
    
    // Server enters loop where command line put is checked for exit command
    public void enterLoop() {
		_mainFrame.dispose();
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
				if(r.ready()) {
					String s = r.readLine();
					if(s.equals("exit")) {
						_server.kill();
						System.out.println("Good Bye");
						System.exit(0);
					} else {
						String[] split = s.split(" ");
						//TODO: Remove this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						if(split.length == 2 && split[0].equals("roll")) {
							_server.roll(Integer.parseInt(split[1]));
						}
					}
				}
			} catch (Exception e) {
			}
		}
    }
}
