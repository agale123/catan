/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.*;


/**
 *
 * @author jfedor
 */
public class Hex {
    
    private double _x;
    private double _y;
    private int _radius;
    private BoardObject.type _type;
    private int _number;
    private String[] imageNames = new String[]{"catanui/sheep.png","catanui/forest.png","catanui/mountain.png","catanui/field.png","catanui/brick.png"};
    
    public Hex(double x, double y, int radius, BoardObject.type type, int number) {
        
        _x = x;
        _y = y;
        _radius = radius;
        _type = type;
        _number = number;
        
    } 
    
    public double getX() {return _x;}
    public double getY() {return _y;}
    public int getRadius() {return _radius;}

    
    public void paint(Graphics graphics, int x, int y) {
        Graphics2D g = (Graphics2D) graphics;
        
        g.setColor(new Color(0,0,0));
        
        Polygon mask = new Polygon(xHex(x),yHex(y),6);
        
        g.setClip(mask);
        String loc;
	switch (_type) {
		case BRICK: loc = imageNames[4]; break;
		case WHEAT: loc = imageNames[3]; break;
		case SHEEP: loc = imageNames[0]; break;
		case ORE: loc = imageNames[2]; break;
		default: loc = imageNames[1]; break;
	}

        Image texture = Toolkit.getDefaultToolkit().getImage(loc);
        g.drawImage(texture, ((int)_x-_radius*2+x), ((int)_y-_radius*2+y),  null);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g.setStroke(new BasicStroke(3));
        g.drawPolygon(mask);
        
        g.setClip(null);
        
        if (_number > 0) {
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(242,233,211));
            g.fillOval(((int)_x+x-_radius/4),((int)_y+y-_radius/4),_radius/2,_radius/2);
            g.setColor(new Color(59,56,49));
            g.drawOval(((int)_x+x-_radius/4),((int)_y+y-_radius/4),_radius/2,_radius/2);
            g.setFont(new Font("Arial",20,20));
            
            if (_number < 10)
                g.drawString(String.valueOf(_number), ((int)_x+x-4), ((int)_y+y+8));
            else
                g.drawString(String.valueOf(_number), ((int)_x+x-10), ((int)_y+y+8));
            
        }
        
    }
    
    public int[] xHex(int off) {
        int[] ret = new int[6];
        
        for (int i=0;i<6;i++)
            ret[i] = (int)(off + _x + (_radius * Math.cos(2 * Math.PI * i / 6)));
        
        return ret;
    }
    
    public int[] yHex(int off) {
        int[] ret = new int[6];
        
        for (int i=0;i<6;i++)
            ret[i] = (int)(off + _y + (_radius * Math.sin(2 * Math.PI * i / 6)));
        
        return ret;
    }
    
    public void clicked(int x, int y) {
        
    }
}
