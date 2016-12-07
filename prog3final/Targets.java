/**
 * Targets.java -- Based on the canonical "main" program, SwingApp,
 *                  for a Swing application with GUI widgets
 *                  and a draw panel for graphics output.
 * 
 *
 * 1/31/09 rdb: 
 *      renamed MovePanel to GUI: it has expanded responsibilities
 * 9/8/10 rdb:
 *      revised to specify GUI panel size based on estimate of top frame size;
 *         these values could be determined precisely, but I just guess here
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class Targets extends JFrame 
{
   //----------------------- class variables ---------------------------
   //--- key application "constants" defined as "package" variables
   static int    numPellets;    // the number of pellets user is allowed
   static int    pelletSize;    // size of pellets
   static int    numTargets;    // the number of targets to be displayed
   static int    targetHeight;  // height of each target
   static int    targetSpeed;   // abs value of speed of target motion
   static int    framesInEachDir; // # frames targets should move in one 
                                  //   direction before reversing direction
   
   //--------------- constructor --------------------------
   public Targets( String title, String[] args ) 
   {
      super( title );
      int width = 500;
      int height = 600;
      int topFrameH = 20;  // estimate of the space used for top border frame
      
      this.setSize( width, height );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      ////////////////////////////////////////////////////
      // Add processing of command line arguments here.
      // For this application, all command line arguments
      //   are stored in package static variables of the 
      //   application class, Targets
      ////////////////////!////////////////////////////////
      
      // get command line arguments
      numTargets      = getArg( args, 0, 10 );  // # targets
      numPellets      = getArg( args, 1, 20 );  // # pellets
      pelletSize      = getArg( args, 2, 10 );
      targetHeight    = getArg( args, 3, 40 );
      targetSpeed     = getArg( args, 4, 5 );   // magnitude of target speed
      framesInEachDir = getArg( args, 5, 40 );  // # frames until reverse dir 
      
      GUI appGUI = new GUI( width, height - topFrameH );
      
      this.add( appGUI );
      this.setVisible( true );
   }

   //++++++++++++++++++++++ class methods ++++++++++++++++++++++++++++
   //------------- getArg( String[], int, int ) ----------------------
   /**
    * This is a utility method for accessing one of the command line
    * string arguments and converting it to an int. It accepts the 
    * entire array of arguments in String form, an integer indicating 
    * which is to be converted to an int and a default value to be 
    * returned if this argument was not on the command line, or if 
    * the specified String is not a valid integer representation.
    *
    * However, a single "-" in the field means to use the default value
    */
   private static int getArg( String[ ] args, int which, int defaultVal )
   {
      try
      {
         // The Integer class has a class method that will convert a String
         //   to an int -- if it is a valid representation of an  integer.
         return Integer.parseInt( args[ which ] );
      }
      catch ( ArrayIndexOutOfBoundsException oob )
      {
         // If there is no args[ which ] element, Java "throws" an exception.
         // This code "catches" the exception and handles it gracefull.
         // In this case, it is not an error. The parameter is optional
         // and there is a specified default value that is returned.
      }
      catch ( NumberFormatException nfe )
      {
         // If the string is not a valid representation of an integer
         // (such as 4Qd3), Integer.parseInt throws a "NumberFormatException".
         // Again, this code catches the exception, gives an error message
         // and uses the default value.
         if ( ! args[ which ].equals( "-" ) )  // "-" is not an error
             System.err.println( "Error: improper command line argument " 
                            + which + " = " + args[ which ] 
                            + ".  Should be an integer; using default value: "
                            + defaultVal );
      }  
      return defaultVal;
   }
   //------------------------------- main -------------------------------
   public static void main( String [] args ) 
   {
      Targets app = new Targets( "Targets", args );
   }
}
