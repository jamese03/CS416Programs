/**
 * ThreadsApp.java - SwingApp for Animation lab
 * 
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class ThreadsApp extends JFrame 
{
   //----------------- Constructor -------------------------
   public ThreadsApp( String title ) 
   {
      super( title );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      this.add( new GamePanel( this ) );
      this.setVisible( true );
      this.pack();
   }
   
   //------------------ main -------------------------------
   public static void main( String [ ] args ) 
   {
      GamePanel.regionRows   = Utilities.getArg( args, 0, 3 );
      GamePanel.regionCols   = Utilities.getArg( args, 1, 5 );
      GamePanel.startObjects = Utilities.getArg( args, 2, 40 );
      ThreadsApp app = new ThreadsApp( "ThreadsApp" );
   }
}