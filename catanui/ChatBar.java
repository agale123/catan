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
public class ChatBar extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    
    int _width = 800;
    int _height = 150;
    int _smallheight = 118;
    
    LinkedList<String> text = new LinkedList<String>();
    
    public JTextField _textfield;

    public ClientGameBoard gameLogic;

    private boolean firstpaint = true;

    public ChatBar(ClientGameBoard gl) {
	gameLogic = gl;
        gameLogic._chatBar = this;  
        addMouseListener(this);
        addMouseMotionListener(this);
	
    }
    
    @Override
    public void paint(Graphics g) {
	if (firstpaint) {

        	g.setColor(Color.GRAY);
		g.fillRect(0, 0, _width, _height);
		
		g.setColor(new Color(200,200,200));
		g.fillRect(0, 5, _width - 5, _height - 10);
		
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("Arial", Font.BOLD, 15));

        }
	else {

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, _width, _smallheight);
		
		g.setColor(new Color(200,200,200));
		g.fillRect(0, 5, _width - 5, _smallheight - 10);

		g.fillRect(_height - 5, 5, _width - 5, 5);
		
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("Arial", Font.BOLD, 15));

	}

        int i = 0;
        while (i < text.size()) {
            
            g.drawString(text.get(text.size()-1-i), 10, (100-i*20));//110 - i*20);
            
            i++;
        }

	_textfield.repaint();

	firstpaint = false;
    }

    public void addLine(String s) {
        
        text.add(s);
        if (text.size() > 20)
            text.removeFirst();
        repaint();

	//String temp = _textfield.getText();
	//_textfield.setText(".");
	//_textfield.setText(temp);
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {
       
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
       
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	if (ae.getActionCommand().equals("/exit")) {
		gameLogic.exit();
		System.exit(0);
	}
	else if (ae.getActionCommand().equals("/cheat")) {
		gameLogic._sideBar.addCard(BoardObject.type.DEV);
		gameLogic._sideBar.addCard(BoardObject.type.WHEAT);
		gameLogic._sideBar.addCard(BoardObject.type.SHEEP);
		gameLogic._sideBar.addCard(BoardObject.type.ORE);
		gameLogic._sideBar.addCard(BoardObject.type.WOOD);
		gameLogic._sideBar.addCard(BoardObject.type.BRICK);
		gameLogic._sideBar.repaint();
		((JTextField)ae.getSource()).setText("");
		return;
	}
        addLine(gameLogic._name+": "+ae.getActionCommand());
	gameLogic.sendLine(gameLogic._name+": "+ae.getActionCommand());
        ((JTextField)ae.getSource()).setText("");
    }

}
