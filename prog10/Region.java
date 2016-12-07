/**
 * Region - manages a rectangular region in the space 
 *          it knows about its neighbors
 *        it passes objects to neighbors
 */

import java.awt.*;
import java.util.*;

public class Region extends Thread
{
  //--------------------- instance variables -------------------------
  private int row;
  private int col;
  private int xLoc;
  private int yLoc;
  private int width;
  private int height;
  private Rectangle bounds;
  private boolean shuttingDown = false;
  
  private GOset myGOs;
  private GOset northGOs = null;
  private GOset eastGOs = null;
  private GOset southGOs = null;
  private GOset westGOs = null;
  
  private Color myColor = null;
  private int   waitTime = 50;
  private String name;
  
  //--------------------- constructor --------------------------------
  public Region( int r, int c, int x, int y, int w, int h, GOset gos )
  {
    name = "R" + row + "x" + col;
    row   = r;  col    = c;
    xLoc  = x;  yLoc   = y;
    width = w;  height = h; 
    myGOs = gos;
    bounds = new Rectangle( x, y, w, h ); // Rectangle good for testing
    // if a Point is inside it
  }
  //------------------ setColor( Color ) ---------------------------
  /**
   * This color is used to color all objects in this region
   */
  public void setColor( Color col )
  {
    myColor = col;
  }
  //--------------------- setNeighbors ----------------------------
  /** 
   * Initialize neighbor refrences; some may be null!
   */
  public void setNeighbors( GOset n, GOset e, GOset s, GOset w )
  {
    northGOs = n;
    eastGOs  = e;
    southGOs = s;
    westGOs  = w;
  }
  //---------------------- setFrameInterval( int ) ---------------------
  /**
   * Frame interval is approximage time in milliseconds for each frame
   * of the animation. We use it as a wait time in the run loop.
   */
  public void setFrameInterval( int millis ) 
  {
    waitTime = millis;
  }
  //-------------------- run() -------------------------------------
  public void run()
  {  
    while ( !shuttingDown )
    {
      //////////////////////////////////////////////////////////////
      // Add code here for checking if objects in this regions
      //    set are still in the region.
      //    if so, color them the regions color.
      //    if not, remove them from this region and pass them on to the
      //       correct neighbor.
      //    border regions won't have 4 neighbors so check for null.
      //       if neighbor is null, don't need to do anything, the
      //       object will soon bounce off the edge of the window
      //       and come back in to the region.
      // GameObjects have a method that returns their center Point.
      //    Rectangle objects have a method that checks if the Rectangle
      //    contains a Point. 
      ///////////////////////////////////////////////////////////////
      
      for(int i = 0; i < myGOs.size(); i++)
      {
        GameObject _gameObject = myGOs.get(i);
        int flag = 0;
        
        if(bounds.contains(_gameObject.getCenter()))
          _gameObject.setColor(myColor);
        
        else
        {
          myGOs.remove(_gameObject);
          
          int _right = (int)(bounds.getX() + bounds.getWidth());
          
          int __left = (int)(bounds.getX());
          
          int ___yTop = (int)(bounds.getY() + bounds.getHeight());
          
          int yBottom = (int)(bounds.getY());
          
          if(northGOs != null) {
            if(flag == 0 && _gameObject.getCenter().y <= yBottom) {
              flag = 1;
              
              _gameObject.setColor(Color.black);
              
              northGOs.add(_gameObject);
            }
          }
          
          if(southGOs != null){
            if(flag == 0 && _gameObject.getCenter().y >= ___yTop){
              
              flag = 1;
              
              _gameObject.setColor(Color.black);
              
              southGOs.add(_gameObject);
            }
          }
          
          if(eastGOs != null) {
            if(flag == 0 && _gameObject.getCenter().x >= _right){
              flag = 1;
              
              _gameObject.setColor(Color.black);
              
              eastGOs.add(_gameObject);
            }
          }
          
          if(westGOs != null){
            if(flag == 0 && _gameObject.getCenter().x <= __left){
              
              flag = 1;
              
              _gameObject.setColor(Color.black);
              
              westGOs.add(_gameObject);
            }
          }
          
          if(flag == 0) {
            myGOs.add(_gameObject);
            _gameObject.setColor(myColor);
          }
        }
      }
      
      try 
      {
        Thread.sleep( RegionManager.sleepDelay );
      }
      catch ( InterruptedException ie )
      {
        System.err.printf( "Region: %s interrupted\n", name );
      }           
    }
  }
  //------------------ terminate() ------------------------------------
  /**
   * Signal to shut down in this case we can shut down immediately
   */
  public void terminate()
  {
    shuttingDown = true;
  }
  //////////////////////////////////////////////////////////////////
  // This main is a good test for early debugging; not many regions,
  //    not many initial objects.
  // After awhile either use command line arguments or run from
  //    ThreadsApp
  //////////////////////////////////////////////////////////////////
  //++++++++++++++++++++ main +++++++++++++++++++++++++++++++++++++++
  public static void main( String[] args )
  {
    String[] defaultArgs = { "3", "5", "10" };
    if ( args.length == 0 )
      ThreadsApp.main( defaultArgs );
    else
      ThreadsApp.main( args );
  }      
}