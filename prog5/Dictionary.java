/* Dictionary class
 * 
 * Stores a hashset of all the words from the dictionary
 * 
 * Given a prefix length of n
 *    stores a hashset of all possible starting n letter combinations
 *    if the hashset does not have the n letter combination
 *    then no words start with those n letters
 *    n is defined by a package static variable, prefixLength
 * 
 * @author jb
 */

import java.io.*;
import java.util.*;

public class Dictionary
{
   static int   prefixLength = 3;
   static int   maxWordLength = 0; // longest word in dictionary
   static int   minWordLength  = 3;

   //------------------- instance variables ---------------------
   protected HashSet<String> words;
   protected HashSet<String> prefix;
   
 
   //------------------- constructors ---------------------------
   public Dictionary( String fileName )
   {
      
      words  = new HashSet<String>();
      prefix = new HashSet<String>();
      readWords( fileName );
      System.out.println( "Words read: " + words.size() );
      System.out.println( "Prefixes:   " + prefix.size() );
   }

   //----------------- readWords ---------------------------
   /**
    * Open the dictionary file
    * Reads the words into the hashsets
    */
   protected void readWords( String fileName )
   {
      BufferedReader tempwords = null;
      
      try //open the dictionary file
      {
         tempwords = new BufferedReader( new FileReader( fileName ) );
      }      
      catch ( Exception e )
      {
         System.err.println( "Open failed for dictionary file: " + fileName );
         System.err.println( e );
         System.exit( 1 );
      }
      
      try 
      { 
         // Read in the dictionary file to populate hashsets
         // Each line of the file is a separate word
         String word = null;
         while ( ( word = tempwords.readLine()) != null )
         {
            word = word.toLowerCase();
            int len = word.length();
            if ( len > maxWordLength )
               maxWordLength = len;
            if ( len >= minWordLength )
               words.add( word );
            if ( len >= prefixLength )
            {
               String start = word.substring( 0, prefixLength );
               if ( !prefix.contains( start ))
                  prefix.add( start );
            }
         }
      }
      catch ( IOException e )
      {
         System.err.println( "Read failed on: " + fileName );
         System.err.println( e.getMessage() );
         System.exit( 2 );
      }      
   }
   //-------------------- search( String ) ------------------------------
   /**
    * Method to search this dictionary for specified word
    * 
    * if the initial letters of the search string can form a word
    * it will search for the word
    * 
    * Returns:
    * 1 -  if the string is is a word
    * 0 -  if the initial letters at the start can form a word, 
    *      but the string is not a word
    * -1 - if the initial letters at the start cannot form a word
    */
   public int search( String word )
   {
      int result = 0;   // unknown is default
      int len = word.length();
      if ( len > maxWordLength )
         result = -1;   // cannot be a word
      else if ( len < minWordLength && len < prefixLength )
         result = 0;    // could be start of a word
      else if ( words.contains( word ) )
         result = 1;      
      else if ( prefixLength <= word.length() )
      {
         String thisPrefix = word.substring( 0, prefixLength );
         if ( !prefix.contains( word.substring( 0, prefixLength )))
            result = -1;
      }
      return result;
   }

   //------------------- main -----------------------------------
   /**
    *  a convenience testing method to call Boggle.main
    */
   public static void main (String args[] )
   {
      Boggle.main( args );
   }   
}
