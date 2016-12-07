/**
 * FractalApp.java -- Based on the canonical "main" program, SwingApp,
 *                  for a Swing application, 
 * 
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class FractalApp extends JFrame 
{
   static int frameWidth = 800;
   static int frameHeight = 700;
   
   public FractalApp( String title, String[] args ) 
   {
      super( title );
      this.setSize( frameWidth, frameHeight );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 
      FractalGUI appGUI = new FractalGUI( frameWidth - 50, frameHeight );
      
      this.add( appGUI );
      this.setVisible( true );
   }

   //------------------------------- main -------------------------------
   public static void main( String [] args ) 
   {
      FractalApp app = new FractalApp( "FractalApp demo", args );
   }
}