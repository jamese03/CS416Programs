/**
 * RegionManager - creates the regions; distributes new objects to them
 *        This should be a static class or Singleton pattern?
 *  
 * 
 * @author rdb
 * November 30, 2010
 */
import java.util.*;
import java.awt.*;

public class RegionManager 
{
   //-------------- class variables ------------------------------
   public static RegionManager rm;
   public static boolean report = false;
   public static boolean synchronize = false;
   public static int     sleepDelay = 100;  // milliseconds
   
   //--------------- instance variables ---------------------------
   // Needs to know the overall space, 
   //     the n x m partitioning of the space
   //     the spatial bounds of each region
   //     the regions.
   private int         width;
   private int         height;
   private int         rows;
   private int         cols;
   private Region[][]  regions;
   public  HashMap<Point, GOset> regionGOs;
   private int         rWidth;   // width of each region
   private int         rHeight;  // height of each region
   private int         objCount = 0;

   //--------- magic constants
   private Color[] colors = { Color.PINK, Color.BLUE, Color.GREEN,
      Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.GRAY };
   //----------------- constructor --------------------------------
   /** 
    * w x h spatial region
    * partitioned into rr regions per row and cr regions per column
    * 
    * Create array that is 2 larger in both directions, the true
    * regions will be in the range
    *     r = 1,...,rows
    *     c = 1,...,cols
    * The borders will be null references; this is convenient for
    * getting neighbors, the real border regions will have valid
    * index offsets into the array, but will get a null reference back.
    */
   private RegionManager( int w, int h, int rr, int cr )
   {
      width   = w;
      height  = h;
      rows    = rr;
      cols    = cr;
      rWidth  = w / cols;
      rHeight = h / rows;
      init();
   }
   //-------------------- init() ---------------------------------------------
   /**
    * define an start up the threads
    */
   private void init()
   {
      
      regions = new Region[ rows + 2 ][ cols + 2 ];
      regionGOs = new HashMap<Point,GOset>();
      
      for ( int r = 0; r < regions.length; r++ )
      {
         for ( int c = 0; c < regions[ 0 ].length; c++ )
         {
            regionGOs.put( new Point( r, c ), null );
         }
      }
      
      // now create the "real" regions
      int xLoc, yLoc;   // origin of each region
      int colIndex = 0;
      for ( int r = 1; r < regions.length - 1; r++ )
      {
         yLoc = ( r - 1 ) * rHeight;
         for ( int c = 1; c < regions[ 0 ].length - 1; c++ )
         {
            xLoc = ( c - 1 ) * rWidth;
            regionGOs.put( new Point( r, c ), new GOset());
            regions[ r ][ c ] = new Region( r, c, xLoc, yLoc, rWidth, rHeight,
                                  regionGOs.get( new Point( r, c )));
            regions[ r ][ c ].setColor( colors[ colIndex ] );
            colIndex = ++colIndex % colors.length;
            regions[ r ][ c ].start();
         }
      } 
      // Now set up all neighbor references
      for ( int r = 1; r < regions.length - 1; r++ )
      {
         for ( int c = 1; c < regions[ 0 ].length - 1; c++ )
         {
            regions[ r ][ c ].setNeighbors( // N, E, S, W
                              regionGOs.get( new Point( r - 1, c     )), // N 
                              regionGOs.get( new Point( r    , c + 1 )), // E 
                              regionGOs.get( new Point( r + 1, c     )), // S
                              regionGOs.get( new Point( r,     c - 1 ))  // W
                             );
         }
      }    
   }
   //-------------------- restart() ------------------------------------------
   /**
    * empty all GameObjects from all regions, terminate all threads and re-init
    */
   public void restart()
   {
      terminate();
      init();
   }
   //-------------------- terminate() ------------------------------------------
   /**
    * empty all GameObjects from all regions, terminate all threads
    */
   public void terminate()
   {
      for ( int r = 1; r < regions.length - 1; r++ )
         for ( int c = 1; c < regions[ 0 ].length - 1; c++ )
         {
            regions[ r ][ c ].terminate();
            try {  regions[ r ][ c ].join(); } 
            catch ( InterruptedException ie )
            { System.err.println( "RegionManager thread interrupted" ); }
         }
   }
   //--------------------- getRM() ------------------------------------------
   /**
    * return reference to the 1 object of this type that is allowed to exist.
    * This is called a Singleton design pattern.
    */
   public static RegionManager getRM()
   {
      return rm;
   }
   //----------------------- create( int, int, int ,int ) -------------------
   /**
    * There is no public constructor; you can only construct a RegionManager
    * through this method -- it won't create another.
    */
   public static RegionManager create( int w, int h, int r, int c )
   {
      if ( rm == null )
         rm = new RegionManager( w, h, r, c );
      else
         System.err.println( "RegionManager is a Singleton. Can't create 2" );
      return rm;
   }
   //-------------------- checkThreads --------------------------------------
   /**
    * A debugging tool to make some superficial tests on the status of the
    * threads.
    */
   public static void checkThreads()
   {
      int goCount = 0;
      int liveCount = 0;
      int threadCount = rm.rows * rm.cols;
      System.err.println( "Checking " + rm.objCount );
      for ( int r = 1; r < rm.regions.length - 1; r++ )
      {
         for ( int c = 1; c < rm.regions[ 0 ].length - 1; c++ )
         {
            if ( rm.regions[ r ][ c ].isAlive() )
               liveCount++;
         }
      }
      if ( liveCount != threadCount )
         System.err.println( "Live mismatch: " + liveCount + " " + threadCount );
      System.err.println( "Live count: " + liveCount );
      
   }
   //-------------------- add( GameObject ) -------------------------
   /**
    * Determine which region(s) should get the component.
    *    We just use the center of the component to determine
    *    the one region we'll put it in.
    */ 
   public void add( GameObject go )
   {
      Point center = go.getCenter();
      int row = center.y / rHeight + 1; 
      int col = center.x / rWidth + 1;
      Point index = new Point( row , col );

      GOset goset = regionGOs.get( index );
      if ( goset == null )
      {
         System.out.println( "Null goset for " + index + " @ " + center );
      }
      else
      {
         goset.add( go );
         objCount++;
      }
   }
   //----------------- getRegion( GameObject ) ----------------------------
   /**
    * find which region the GameObject is in; return tge Region or null
    */
   public Region getRegion( GameObject go )
   {
      for ( int r = 1; r < regions.length - 1; r++ )
      {
         for ( int c = 1; c < regions[ 0 ].length - 1; c++ )
         {
            Point p = new Point( r, c );
            GOset rGOset = regionGOs.get( p );
            if ( rGOset == null )
               System.out.println( "XXX: " + r + " " + c );
            else if ( rGOset.contains( go ))
               return regions[ r ][ c ];
         }
      }
      return null;
   }
}