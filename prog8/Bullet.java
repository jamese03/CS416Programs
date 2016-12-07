/*
 * Bullet.java
 * 
 * Michael du Breuil
 * 
 * Bullets should be created near or at the start and reused throughout
 * the animation. Creating new copies every time one exits the screen is
 * lots of overhead.
 *
 * Bullets do not set their own color, it's managed by the brush that gets
 * passed to the paint method, allowing the field and quadtree to manage the
 * color. It also saves on calling the set color method every time, and makes
 * it easier to diagnose if the quadtree is working correctly
 * 
 * Summer 2010
 * 
 * Modified by rdb
 * April 2011
 * 
 * Edited by JB
 * Summer 2011
 *
 * Edited by KJ
 * April 2012
 */

import java.awt.*;
import java.awt.geom.*;

public class Bullet extends Ellipse2D.Float
{
   //------------------ class variables  ------------------------------
   private static int     size = 3;
   
   //------------------ instance variables  ------------------------------
   private float dx, dy;
   public boolean expired = false;
   private Point2D.Float position;
   // You will need to add your own variables
   public boolean gameOver = false;
   public Color _Color;
   
   //------------------------- constructor -------------------------------
   public Bullet( Game parent, Point2D.Float pos, float dx, float dy ) 
   {
      super( pos.x, pos.y, size, size );
      position = pos;
      this.dx = dx;
      this.dy = dy;
   }
   //--------------------------- restart ---------------------------------
   /**
    * Tell the bullet it has been restarted (that it is alive again.)
    */
   public void restart() 
   {
     
     setPosition((float)0,(float)285);
   }
//--------------------------move method-------------------------------------
   public void move() 
   {
    
     setPosition( position.x + dx, position.y + dy );
   }
//--------------------------------------get position--------------------------------
   public Point2D.Float getPosition() 
   {
      return position;
   }
//---------------------------set position-------------------------------------------
   public void setPosition( Point2D.Float pos ) 
   {
      this.setPosition( pos.x, pos.y );
   }
   public void setAngle(float x, float y)
   {
     dx = x;
     dy = y;
   }
   
   public void setColor()
   {
     //this.setColor(node.bullet.getColor());
     
   }
   
//-----------------------------------set postion with floats-----------------------------------   
   public void setPosition( float x, float y ) 
   {
      this.position.x = this.x = x;
      this.position.y = this.y = y;
   }
   
   public Rectangle getRect()
   {
     return new Rectangle( (int) position.x, (int) position.y, size, size);
   }
//---------------------------------draw method----------------------------------------------------------
   public void draw( Graphics g, Color color ) 
   {
      Color c = g.getColor();
      Graphics2D g2 = (Graphics2D) g;
      g.setColor(color);
     
      int ix = (int)position.x;
      int iy = (int)position.y;
      g2.draw( this );
      g.setColor(c);
   }
   public boolean offScreen()
   {
     return position.x > 512 || position.y > Game.height || position.y < 0;
   }
}
