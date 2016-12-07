/** 
  * Utils.java
  * A simple class to provide a variety of useful utility functions as
  * static methods
  * 
  *        void   frame()
  *        void   sleep( int msecs )
  *        int    getArg( String[], int, int )
  *        String getFileName()
  * 
  * rdb
  * Spring 2011
  */

import javax.swing.*;
import java.io.*;

public  class Utils 
{
   //-------------------- class variables -----------------------
  private static int frameTime = 100;
  private static JFileChooser chooser = null;
 
  //---------------------- class methods ------------------------
  
  //---------------------- frame() ------------------------------
  /**
   * delay an appropriate time to get a friendly frame rate (100 ms)
   */
  public static void frame( ) 
  {
    sleep( frameTime );
  }
  
  //---------------------- sleep(int) ------------------------------
  /**
   * Put the program (thread, actually) to sleep for the specified milliseconds
   */
  public static void sleep( int milliseconds ) 
  {
      try
      {
        Thread.sleep( milliseconds );
      }
      catch ( java.lang.InterruptedException e ) 
      {  
         System.err.println( "sleep interrupted by whom? " + e.getMessage() );
      }
  }
  
    //---------------------- getArg( String[], int, int ) -----------------
    /**
     * This is a utility method for accessing one of the command line
     * string arguments and converting it to an int. It accepts the 
     * entire array of arguments in String form, an integer indicating 
     * which is to be converted to an int and a default value to be 
     * returned if this argument was not on the command line, or if 
     * the specified String is not a valid integer representation.
     */
    public static int getArg( String[ ] args, int which, int defaultVal )
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
            
            System.err.println( "Error: improper command line argument "                         
                    + which + " = " + args[ which ] 
                    + ".  It should be an integer; using default value: "
                    + defaultVal );
        }  
        return defaultVal;
    }
    //---------------------- getArg( String[], int, float ) -----------------
    /**
     * float version of getArg
     */
    public static float getArg( String[ ] args, int which, float defaultVal )
    {
        try
        {
            return Float.parseFloat( args[ which ] );
        }
        catch ( ArrayIndexOutOfBoundsException oob )
        {
        }
        catch ( NumberFormatException nfe )
        {
            System.err.println( "Error: improper command line argument "                         
                    + which + " = " + args[ which ] 
                    + ".  It should be an float; using default value: "
                    + defaultVal );
        }  
        return defaultVal;
    }
    //---------------------- getArg( String[], int, String ) -----------------
    /**
     * String version of getArg
     */
    public static String getArg( String[ ] args, int which, String defaultVal )
    {
        try
        {
            return args[ which ];
        }
        catch ( ArrayIndexOutOfBoundsException oob )
        {
        }
        return defaultVal;
    }
   //----------------------- toInt( String, int ) ----------------------------
   /**
    * Convert string to integer
    */
   public static int toInt( String str, int defaultVal )
   {
      String msg = null;
      try 
      {
         return Integer.parseInt( str );
      }
      catch ( NumberFormatException nfe )
      {
         msg = str + " is not a valid int. Using " + defaultVal;
      }
      catch ( NullPointerException npe )
      {
         msg = "Null string. Using " + defaultVal;
      }
      System.err.println( "***** ERROR: Utils.toInt: " + msg );
      return defaultVal;
   }
   //----------------------- toDouble( String, double ) ----------------------
   /**
    * Convert string to double
    */
   public static double toDouble( String str, double defaultVal )
   {
      String msg = null;
      try 
      {
         return Double.parseDouble( str );
      }
      catch ( NumberFormatException nfe )
      {
         msg = str + " is not a valid double. Using " + defaultVal;
      }
      catch ( NullPointerException npe )
      {
         msg = "Null string. Using " + defaultVal;
      }
      System.err.println( "***** ERROR: Utils.toDouble: " + msg );
      return defaultVal;
   }    
    //---------------------- String getFileName( String ) ---------------
    /**
     * Use a JFileChooser dialog to get a valid file name from a user.
     *   Will not return the name unless the file exists.
     * Returns null if no valid file selected.
     */
    public static String getFileName( String prompt )
    {
       String fileName = null;
       
       if ( chooser == null )
       {
          chooser = new JFileChooser();
          chooser.setCurrentDirectory( new File( "." ) );
       }
       chooser.setDialogTitle( prompt );
       
       int returnVal = chooser.showOpenDialog( null );
       while ( fileName == null && returnVal != JFileChooser.CANCEL_OPTION ) 
       {
          if ( returnVal == JFileChooser.APPROVE_OPTION )
          {
             File f = chooser.getSelectedFile();
             if ( f.isFile() )
                fileName = f.getPath();
             else
                returnVal = chooser.showOpenDialog( null );
          }
       }
       return fileName;
    }

    //------------------- File getFile( String ) -------------------------
    /**
     * Use a JFileChooser dialog to get a valid file name from a user.
     *   Will not return the name unless the file exists.
     * Returns null if no valid file selected.
     */
    public static File getFile( String prompt )
    {
       String fileName = null;
       File   file = null;
       
       if ( chooser == null )
       {
          chooser = new JFileChooser();
          chooser.setCurrentDirectory( new File( "." ) );
       }
       chooser.setDialogTitle( prompt );
       
       int returnVal = chooser.showOpenDialog( null );
       while ( fileName == null && returnVal != JFileChooser.CANCEL_OPTION ) 
       {
          if ( returnVal == JFileChooser.APPROVE_OPTION )
          {
             file = chooser.getSelectedFile();
             if ( file.isFile() )
                fileName = file.getPath();
             else
                returnVal = chooser.showOpenDialog( null );
          }
       }
       return file;
    }

}