/** Boggle Program
 * 
 * creates a boggle board as defined by a "dice" file that specifies
 *    the size of the board and the letters on the dice for that board.
 * 
 * Usage     java Boggle [-b] [-pn] [-sn] [-mn] [dictionary] [diceFile]
 * 
 *         -b says to run the program in batch mode -- no dialog boxes
 *            all output (and input) to/from standard input/output
 *         -pn make the length of the prefix hash table n, default is 4
 *         -sn make the seed for the random number generator n, default is 0
 *         -mn make the maximum word size n, default is 8
 * 
 *         dictionary: the file of words; 1 word per line; you can create
 *                     your own to help debugging; default: opted3to8.txt
 *         diceFile: File that defines the size of the boggle board and
 *                   the letters on the sides of the dice to be randomly put
 *                   into that board. Default: boggle4x4.txt
 *         The "switches" can come in any order, all must precede the
 *            file arguments, if they are there. You may use a "-" for the
 *            dictionary and/or dicefile arguments to use the default
 * 
 * @author jb
 * Edited by rdb
 * 
 */

import java.io.File;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Boggle
{
   //--------------------- class variables -------------------------
   //-- parameters changeable from command line
   static String     dictionaryFile = "opted3to8.txt";
   static String     diceFile       = "boggle4x4.txt";
   static boolean    batch          = false;
   static int        seed           = 1;
   
   //----
   static Dictionary dictionary = null;
   static Random     randomNum  = null;
   
   //--------------------- instance variables -------------------------
   private int               xsize;
   private int               ysize;
   private ArrayList<Die>    dice;
   private ArrayList<String> letters;  // all letters in all dice
   
   private BoggleBoard       bb;
   private Scanner           input = null; // in batch for reading commands
   
   //------------------- constructor --------------------------------
   public Boggle()
   {
      randomNum = new Random( seed );
      
      dictionary = new Dictionary( dictionaryFile );
      createDice();
      
      if ( batch )
         input = new Scanner( System.in );
      
      throwDice();
            
      bb = new BoggleBoard( letters, xsize, ysize );
      while ( bb != null )
      {
         String msg = "Board\n" + bb;
         
         //report( msg );   // you may comment this out if you want.
         
         long start = System.currentTimeMillis();
         
         String solution = bb.findWords();
         int    numWords = bb.getWordCount();

         float secs = ( System.currentTimeMillis() - start ) / 1000.0f;
         
         String timeMsg = "-----------Time: " + secs + " seconds " +
                          "  Prefix: " + Dictionary.prefixLength + 
                          "  MaxLen: " + Dictionary.maxWordLength + "\n\n";
         msg = "-------- " + numWords + " words found\n";

         reportSolution( msg + solution + timeMsg );
      }
   }
   //------------------- reportSolution( String ) --------------------------
   /**
    * report the resuts of the solution; check for another game and 
    *   possible changes to parameters.
    */
   private void reportSolution( String results )
   {
      String msg = bb.toString() + results;
      String prompt = "To terminate, enter an empty line\n\n"
         + "p<int>: reruns the same board with specifed prefix length\n"
         + "s<int>: defines a new random number seed, and a new board\n"
         + "m<int>: defines a new max word length, and evaluates this board\n"
         + "n:      creates a new board\n\n"
         + "else will re-evaluate previous board\n";
      
      String command;
      if ( batch )
      {
         report( msg );
         command = input.nextLine();
      }
      else
      {
         command = JOptionPane.showInputDialog( null, msg + prompt );
      }
      //------ process command
      if ( command == null )
         bb = null;
      else
      {
         command = command.trim();
         if ( command.length() == 0 )
            bb = null;
         else if ( command.startsWith( "m" ))  // change max word len, redo.
         {
            Dictionary.maxWordLength = toInt( command.substring( 1 ), 
                                            Dictionary.maxWordLength );
         }
         else if ( command.startsWith( "p" ))  // change prefix size, new Dict.
         {
            Dictionary.prefixLength = toInt( command.substring( 1 ), 
                                            Dictionary.prefixLength );
            dictionary = new Dictionary( dictionaryFile );
         }
         else if ( command.startsWith( "s" ))  // change seed, new board
         {
            seed = toInt( command.substring( 1 ), seed );
            createDice();
            throwDice();
            bb = new BoggleBoard( letters, xsize, ysize );
         }
         else if ( command.startsWith( "n" ))  // get a new board
         {
            throwDice();
            bb = new BoggleBoard( letters, xsize, ysize );
         }
         else
         {
            System.err.println( "Invalid command; re-doing previous board" );
         }
      }
   }
   //------------------- createDice --------------------------------
   /**
    * generate the dice with letters on them
    */
   private void createDice(  )
   {
      letters = new ArrayList<String>();
      dice = new ArrayList<Die>();
      
      Scanner tempdice = null;

      try //open the file
      {
         tempdice = new Scanner( new File( diceFile ) );
      }      
      catch ( Exception e )
      {
         System.err.println( "Unable to open dice file: " + diceFile );
         System.err.println( e + ":" + e.getMessage() );
         System.exit( 5 );
      }
      
      try //read in the dice file
      {
         if ( tempdice.hasNextInt() )
         {
            xsize = tempdice.nextInt();
            ysize = tempdice.nextInt();
            tempdice.nextLine();
         }
         while ( tempdice.hasNext() )
         {
            String tempword = tempdice.nextLine().toLowerCase();
            dice.add( new Die( tempword ));
         }
      }
      catch ( Exception e )
      {
         System.err.println( "Unable to read dice file: " + diceFile );
         System.err.println( e );
         System.exit( 7 );
      }
      
      if ( dice.size() != xsize * ysize )
      {
         String[] err = { "Dice layout and number of dice strings inconsistent",
                          " Layout: " + xsize + " x " + ysize,
                          "   Need " + ( xsize * ysize ) + " words.",
                          "   Have " + dice.size() + " word." };
         for ( int i = 0; i < err.length; i++ )          
            System.err.println( err[ i ] );         
         System.exit( 8 );
      }
   }
   //------------------- throwDice --------------------------------
   /**
    * generate the dice with letters on them
    */
   private void throwDice(  )
   {
      letters = new ArrayList<String>();
      int[] diceOrder = new int[ dice.size() ];
      ArrayList<Die> copyDice = new ArrayList<Die>();
      for( Die die: dice )
         copyDice.add( die );
      // now choose random dice for the spots
      while( copyDice.size() > 0 )
      {
         int d = randomNum.nextInt( copyDice.size() );
         Die die = copyDice.get( d );
         letters.add( die.roll() );
         copyDice.remove( d );
      }
   }
   //------------------ report( String ) -------------------------------
   /**
    * Report a message: To standard out if in batch, to a dialog box if not
    */
   public static void report( String msg )
   {
      if ( batch )
         System.out.println( msg );
      else
         JOptionPane.showMessageDialog( null, msg );
   }
   
   //---------------------- main ----------------------------------------
   /**
    * Command line arguments all optional, order required
    *       -b diceFile dictionaryFile
    *   -b means run in batch: no popup dialogs
    *   diceFile: default is boggle4x4.txt, "-" also means use default
    *   dictionaryFile: default is opted3to8.txt, "-" also means use default
    */    
   public static void main( String [] args )
   {
      int next = 0;
      //-------- check for switches ----------------
      while ( next < args.length && args[ next ].startsWith( "-" )
                  && ! args[ next ].equals( "-" ) )
      {
         //-------- check for batch switch ----------------
         if ( args[ next ].equals( "-b" ))
            batch = true;
         //-------- check for prefix switch ----------------
         else if ( args[ next ].startsWith( "-p" ))
         {
            String val = args[ next ].substring( 2 );
            // convert rest of string to int, assign to Dictionary.prefixLen,
            //   defaultl value is existing value of Dictionary.prefixLen
            Dictionary.prefixLength = toInt( val, Dictionary.prefixLength );
         }
         //-------- check for seed switch
         else if ( args[ next ].startsWith( "-s" ))
         {
            String val = args[ next ].substring( 2 );
            seed = toInt( val, seed );
         }
         //-------- check for min word length switch
         else if ( args[ next ].startsWith( "-m" ))
         {
            String val = args[ next ].substring( 2 );
            Dictionary.minWordLength = toInt( val, Dictionary.minWordLength );
         }
         else
            System.err.println( "Error: Invalid switch: " + args[ next ] );
         next++;
      }
      if ( next < args.length ) // check for dictionary file arg
      {
         if ( !args[ next ].equals( "-" ))
            dictionaryFile = args[ next ];
         next++;
      }
      if ( next < args.length ) // check for dice file arg
      {
         if ( !args[ next ].equals( "-" ))
            diceFile = args[ next ];
         next++;
      }
      Boggle b = new Boggle();
   }
   //-------------------- toInt( String, int ) -----------------------
   /**
    * convert a string to an int; if String is not a valid integer,
    * write a message to stderr and return the passed in default value
    */
   public static int toInt( String input, int defaultVal )
   {
      try
      {
         // The Integer class has a class method that converts a String
         //   to an int -- if it is a valid representation of an  integer.
         return Integer.parseInt( input );
      }
      catch ( NumberFormatException nfe )
      {
         // If the string is not a valid representation of an integer
         // (such as 4Qd3), Integer.parseInt throws a "NumberFormatException".
         // This code catches the exception, gives an error message
         // and uses the default value.
         if ( input.length() > 0 && ! input.equals( "-" ) )  
             System.err.println( "toInt Error: Not a valid integer: " + input
                            + ". Using default value: "+ defaultVal );
      }  
      return defaultVal;
   }
}