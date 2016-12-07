/**
 * GameObject -- an object that can bounce around the display 
 *    Could have subclasses:
 *              compute collisions with other objects
 *              explode on collision
 *              destroy smaller object
 *              absorb smaller object and grow until they explode 
 *              replicate on collision
 * 
 * @author rdb
 * 11/30/10
 */
import javax.swing.*;
import java.util.*;
import java.awt.*;

public class GameObject extends JComponent implements Animated
{
   //---------------- class variables ----------------------------------
   private static String    defaultPrefix = "noname";
   private static int       nameCounter   = 0;
   private static Container parent;
   private static Random    rng = new Random( 0 );
   
   //---------------- instance variables -------------------------------
   private JAreaShape theObj;
   private String     name;      // must be unique id 
   private Point      center;
   private boolean    dead = false;
   
   //---- animation variables
   private int        moveX;
   private int        moveY;
   
   
   //---------------- constructors --------------------------------------
   /**
    * App passes its own name for the GameObject in this constructor
    */   
   public GameObject( String id, Container pa, JAreaShape realObj )
   {
      name   = id;
      parent = pa;
      theObj = realObj;
      this.add( theObj );
   }
   /**
    *   Nameless constructor; we'll generate a unique name.
    */   
   public GameObject( Container ma, JAreaShape realObj )
   {
      name = defaultPrefix + String.valueOf( nameCounter++ );
      parent = ma;
      theObj = realObj;
      this.add( theObj );
    }

   //----------------- collision ------------------------------------
   public int collision( GameObject go )
   {
      return 0;
   }   
   //----------------- die ------------------------------------
   public void die()
   {
      dead = true;
      setAnimated( false );
      setSize( 3, 3 );
      //if ( getColor() != Color.RED )
         //setColor( Color.BLACK );
   }
   //----------------- isDead ------------------------------------
   public boolean isDead()
   {
      return dead;
   }

   //------------------ getCenter() ------------------------------------
   public Point getCenter()
   {
      if ( center == null )
         center = new Point( 0, 0 );
      center.x = getX() + getWidth() / 2       ;
      center.y = getY() + getHeight() / 2;
      
      return center;
      }
   //++++++++++++++++++ wheels-like convenience methods +++++++++++++++++++
   //----------------------- setLineColor( Color ) --------------------
   /**
    * setLineColor -- a wheels-like method; defaults to setting color
    */
   public void setLineColor( Color aColor ) 
   {
      theObj.setLineColor( aColor );
      theObj.repaint();
   }
   //----------------------- setFrameColor( Color ) --------------------
   /**
    * setFrameColor -- a wheels-like method; defaults to setting color
    */
   public void setFrameColor( Color aColor ) 
   {
      theObj.setFrameColor( aColor );
      theObj.repaint();
   }
   //----------------------- ( Color ) -----------------------
   /**
    * setColor -- a wheels-like method
    */
   public void setColor( Color aColor ) 
   {
      //System.out.println( "Settingn color " + aColor );
      theObj.setColor( aColor );
      theObj.repaint();
   }
   //------------------- setLocation( int, int ) -----------------------
   /**
    * setLocation( Point ) -- a wheels method, but it just calls parent.
    */
   public void setLocation( int x, int y ) 
   {
      super.setLocation( x, y );
   }
   //------------------- setLocation( Point ) -----------------------
   /**
    * setLocation( Point ) -- a wheels method, but it just calls parent.
    */
   public void setLocation( Point p ) 
   {
      setLocation( p.x, p.y );
   }
   //-------------------- setSize( int, int ) ---------------------------
   /**
    * the JComponent size will actually be set to be the passed in parameters
    */
   public void setSize( int w, int h )
   {
      super.setSize( w , h );
      theObj.setSize( w, h );
      this.repaint();
   }
   //++++++++++++++++++++++++ accessor methods +++++++++++++++++++++++++++++
   //----------------------- getColor() -----------------------
   /**
    * getColor -- a wheels method; defaults to line color
    */
   public Color getColor() 
   {
      return theObj.getColor();
   }
   //----------------------- getFrameColor() -----------------------
   /**
    * getFrameColor -- a wheels-like method; defaults to line color
    */
   public Color getFrameColor() 
   {
      return theObj.getFrameColor();
   }
   //----------------------- getLineColor() -----------------------
   /**
    * getLineColor -- a wheels-like method
    */
   public Color getLineColor() 
   {
      return theObj.getLineColor();
   }   
   //+++++++++++++++ methods used for animation +++++++++++++++++++++
   //---------------- setMove( int, int ) ---------------------------
   /**
    * set the incremental steps taken by each move.
    */
   public void setMove( int dx, int dy )
   {
      moveX = dx;
      moveY = dy;
   }
   //---------------- moveBy( int, int ) -----------------------------
   public void moveBy( int dx, int dy )
   {
      setLocation( getX() + dx, getY() + dy );
   }
   
   //++++++++++++++++++++++ Animated interface +++++++++++++++++++++++++
   private boolean _animated = true; // instance variable used in interface
   //---------------------- newFrame() ----------------------------------
   public boolean isAnimated()
   {
      return _animated;
   }
   //---------------------- setAnimated( boolean ) --------------------
   public void setAnimated( boolean onOff )
   {
      _animated = onOff;
   }
   //---------------------- newFrame() -------------------------------
   /**
    * invoked for each frame of animation; 
    * update the position of the ball, then check whether it has hit
    *   any of the 4 boundaries of the display panel; if it does,
    *   bounce it off the boundary, by revising the move parameters,
    *   but also force the ball to be entirely on screen; this last
    *   bit is necessary primarily when a ball is started far enough
    *   off screen that the next frame increment isn't enough to
    *   get its position on screen, so code will reverse step again,
    *   putting it further off, etc. 
    */
   public void newFrame() 
   {
      if ( _animated )
      {
         int nextX = (int)getX() + moveX;
         int nextY = (int)getY() + moveY;
         if ( nextX <= minX() ) 
         {
            moveX = - moveX;
            nextX = minX();   // force entire object on screen 
         }
         else if ( nextX >= maxX() ) 
         {
            moveX = - moveX;
            nextX = maxX();
         }
         
         if ( nextY <= minY() ) 
         {
            moveY = - moveY;
            nextY = minY();
         }
         else if ( nextY >= maxY() ) 
         {
            moveY = - moveY;
            nextY =  maxY();
         }
         setLocation( nextX, nextY );
      }
   }
   //+++++++++++++++ end Animated interface +++++++++++++++++++++++++++++++++++++
   //------------- methods to get boundaries of parent container --------------
   //------------- minX() ---------------------
   private int minX() 
   {
      return (int) parent.getX();
   }
   //------------- minY() ---------------------
   private int minY() 
   {
      return (int) parent.getY();
   }
   //------------- maxX() ---------------------
   private int maxX() 
   {
      return (int)( parent.getX() + parent.getWidth()
                       - this.getWidth() );
   }
   //-------------- maxY() ----------------------
   private int maxY() 
   {
      return (int)( parent.getY() + parent.getHeight()
                       - this.getHeight() );
   }
}