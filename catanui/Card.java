/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author jfedor
 */
public class Card implements BoardObject {
    
    public int _x;
    public int _y;
    public int _w = 30;
    public int _h = 45;
    public BoardObject.type mytype;
    private int _loc = -1;
    
    @Override
    public int getW() {
        return _w;
    }
    @Override
    public int getH() {
        return _h;
    }
    
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }
    @Override
    public void setX(int x) {
        _x = x;
    }

    @Override
    public void setY(int y) {
        _y = y;
    }

	public void setLoc(int l) {
		_loc = l;
	}

	public int getLoc() {
		return _loc;
	}

    public Card(int xa, int ya, BoardObject.type typea) {
        
        _x = xa;
        _y = ya;
        mytype = typea;
       
    }
    public BoardObject.type getType() {return mytype;}
    public void paint(Graphics g) {

        g.fillRect((_x), (_y), _w, _h);
        g.fillRect((_x), (_y), _w, _h);
        g.fillRect((_x), (_y), _w, _h);
        g.drawImage(images.get(mytype), (_x), (_y), _w, _h,  null);
        
    }
    
    public void paint(Graphics g, int dx, int dy){}
}
