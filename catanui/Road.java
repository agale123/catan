/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author jfedor
 */
public class Road implements BoardObject {

    private int _x;
    private int _y;
    private int _x2;
    private int _y2;
    public int _length = 80;
    private int _h = 20;
    
    public Color c = Color.magenta;
    
    public Road(int x1, int y1) {
        _x = x1;
        _y = y1;
        _x2 = -1;
        _y2 = -1;
    }
    
    @Override
    public int getW() {
        return _length;
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

    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        
        g.setColor(c);
        g.setStroke(new BasicStroke(4));
        g.drawLine(_x, _y, _x+65, _y);
        g.setStroke(new BasicStroke(1));
        
    }
    
    @Override
    public void paint(Graphics g1, int dx, int dy) {
        Graphics2D g = (Graphics2D)g1;
        
        g.setColor(c);
        g.setStroke(new BasicStroke(4));
        g.drawLine(_x+dx, _y+dy, _x+75+dx, _y+dy);
        g.setStroke(new BasicStroke(1));
        
    }
}
