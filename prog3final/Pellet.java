/* Pellet
 * James English jpj68
 * 
 * 
 * */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public class Pellet extends JEllipse
{
//--------------------- instance variables------------------------------  

  private double dx, dy;
  private int newX, newY;
  private Point p;

//--------------constructor-----------------------------------------------
  public Pellet( int x, int y, int speed, int angle )
  {
    setLocation( x, y );
    setSize( Targets.pelletSize, Targets.pelletSize);
    setColor(Color.RED);  
    dx = Math.cos(Math.toRadians(angle))*speed;
    dy = -Math.sin(Math.toRadians(angle))*speed;
  }
//---------------move method--------------------------------------------
 // moves the pellets when they are shot
  public void move()
  {
    newX = (int)Math.round(getXLocation() + dx);
    newY = (int)Math.round(getYLocation() + dy);
    setLocation( newX, newY ); 
    p = new Point( newX, newY);
  }


//-------------------get point method ----------------------------------
  public Point getPoint()
  {
    return p;
  }
// returns point of the bulllet


}