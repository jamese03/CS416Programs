/*
 * QuadShoot.java
 * Michael du Breuil
 * 
 * Summer 2010
 * 
 * Edited by rdb, March 2011
 * Edited by JB Summer 2011
 */

//package FlowQuad;

import java.awt.*;
import javax.swing.*;

public class QuadShoot extends JFrame 
{
    public QuadShoot( String title, String fileName, int maxHits,
		      int maxBullets ) 
   {
      super( title );
      JPanel contentPanel = new JPanel( new BorderLayout());
     
      Game field = new Game( fileName, maxHits, maxBullets );
      
      // The interaction control panel is a separate window
      contentPanel.add( field, BorderLayout.CENTER );
      GUIPanel control = new GUIPanel( field );
      control.setLocation( 700, 0 );
      control.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      this.add( contentPanel );
     
      // The initial window size is good for a square vector field with sides
      // that are a power of 2, plus some extra in x and y (8 and 30) that 
      // appear to be about right for window pieces outside the draw area.
      this.setSize( new Dimension( Game.width  + 8, Game.height + 30 ));
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      //  make both windows visible
      this.setVisible( true );
      control.setVisible( true );
   }
   
   //-------------------------------- main -------------------------------
   public static void main( String[] args )
   {
      String fileName = Utils.getArg(args, 0, "targets1.txt");
      int maxHits = Utils.getArg(args, 1, 20);
      int maxBullets = Utils.getArg(args, 2, 50);
      
      QuadShoot vectorField = new QuadShoot( "Target Game", fileName,
					     maxHits, maxBullets );
   }
}
