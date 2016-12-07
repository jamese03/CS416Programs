/**
 * BoggleBoard.java - A skeleton class for implementing the board for
 *                    the game of Boggle
 * 
 * @author jb
 * Summer 2010
 * 
 * 02/19/11 rdb: minor formatting and style edits
 */

import java.io.*;
import java.util.*;

public class BoggleBoard
{
   //--------------------- instance variables -------------------------
   Tile[][]          board;
   int               nCols;
   int               nRows;
   ArrayList<String> letters;
   int _count = 0;
   int wordNum;

   //------------------ constructor -----------------------------------
   /**
    * Arguments: 
    *        lettersOnBoard: rows x cols letters to be shown on the board
    *        rows: number rows of letters
    *        cols: number columns of letters
    */
   BoggleBoard( ArrayList<String> lettersOnBoard, int rows, int cols )
   {
      letters = lettersOnBoard;     
      nCols = cols;
      nRows = rows;
      
      ////////////////////////////////////////////////////////////////
      // 1. Need to create the board and "populate" it with the letters
      //    passed in the ArrayList. Assign entries from letters by row!
      //    That is, do all of row 0, then row 1, etc.
      // 2. For each Tile in the board, need to create a list of its
      //    valid neighbors (in all 8 directions). Remember that tiles
      //    on the boundaries don't have 8 neighbors.
      /////////////////////////////////////////////////////////////////
      board = new Tile[rows][cols];
     
      for ( int i = 0; i < nRows; i++)
      {
        for ( int j = 0; j < nCols; j ++)
        {
          Tile tile = new Tile(i, j);
          tile.setLetter(letters.get(_count));  
          board[i][j] = tile;
          _count++;
        }
      }
      
      
   }
   //---------------------- getWordCount() -----------------------------
   /**
    * return the number of words found in the last solution.
    * if findWords has not yet been called, returns -1;
    */
   public int getWordCount()
   {
      /////////////////////////////////////////////////////////////////
      // return the number of words found in last call to findWords()
      //    or -1 if no call yet made
      /////////////////////////////////////////////////////////////////
      return wordNum; // total number of words 
      
   }
   
   //------------------------- findWords() -----------------------------
   /**
    * Find all the valid words in this board (based on current parameter
    *    settings).
    * As words are found, add them to a Java TreeSet object (which sorts
    *    them alphabetically for you) -- see Java API documentation.
    * Return the words in a single String, separated by commas with 
    *    10 words per line (except last line).
    * Most of the work is done by the private recursive method,
    *    findWords( String, Tile )
    */
   public String findWords()
   {
     //////////////////////////////////////////////////////////////////
     // For each tile in the board
     //    findWords( TreeSet, "", tile ) to find all words that start there
     // Convert the TreeSet into a single String with 10 words per line,
     //    separated by commas.
     // return this string.
     //////////////////////////////////////////////////////////////////
     TreeSet<String> allWords =  new TreeSet<String>();    // treestring of the found words
     
     for ( int i = 0; i < nRows; i++)
     {
       for ( int j = 0; j < nCols; j ++)
       {
         findWords(allWords, "", board[i][j]); 
       }
     }
     /* The next lines are so the words show up and can space out so it looks nice
      * 
      * */
     
     wordNum = allWords.size();
     int _counter = 0;
     String str = "";
     Iterator it = allWords.iterator();
     
     
     for ( int j = 0; j < allWords.size(); j++)
     {
       str += (it.next() + ", ");
       _counter++;    
       if ( _counter == 9) // if there are 9 in the row make a new line so it looks nice
       {       
         str += "\n";
         _counter = 0; // resets the counter to 0 so it will repeat the steps above
       }
     } 
     str = str.substring((0),str.length()-2) + " \n"; // the final return so it looks neat
     
     
     return str;
   }

   //---------------- findWords( TreeSet<String>, String, Tile ) -----------
   /**
    * Given a partial word ending at a neighbor of the tile passed in,
    *    add the tile's letter to the partial word, and check if it is a word
    *    and if it terminates the search along this path; recurse if not.
    */
   private void findWords( TreeSet<String> foundWords, String word, Tile t )
   {
      ///////////////////////////////////////////////////////////////////
      //  if tile has not been visited (on this path)
      //     set tile's status as visited
      //     add tile's letter to word
      //     lookup word using search method of Boggle.dictionary
      //     if it is a word
      //        add it to the TreeSet
      //     if it is a word or might be a word
      //        get neighbor tiles of this tile
      //        for each neighbor of this tile
      //           invoke findWords(...) recursively
      //     reset the tiles visited flag to false
      ////////////////////////////////////////////////////////////////////
     
    
     if (t.visited())
       return;

     String newWord = word + t.toString();  
     if (t.toString().equals("q"))
     {
       newWord += "u "; // sets q to qu for the game to work 
     }

     ArrayList<Tile> neighbors = neighborMake(t);
  
     int dict = Boggle.dictionary.search(newWord);

     if (dict >=1)
       foundWords.add(newWord); // adds the new word to the dictionary
     
     if (dict < 0) // avoids errors if dictionary is 0 it stops it.
       return;
 
     t.setVisited(); // sets it the word to visited 
     
     /*Stephen helped with the logic behind the recurison at this point. 
      * It goes through the neighbor tiles and calls the find words method to 
      * search for words on the board
      * */
     
     for ( int i = 0; i < neighbors.size(); i++)
     {
       findWords(foundWords, newWord, neighbors.get(i));           
     }
  
    
     t.clearVisit(); // clears the visit so it can re visit tiles after one word is found
     
   }

   
//----------------------make neighbor method------------------------------
   public ArrayList<Tile> neighborMake(Tile tile)
   {
     ArrayList<Tile> tempList = new ArrayList<Tile>(); // temp arraylist 
     int _r = tile.getRow(); // gets the rows 
     int _c = tile.getCol(); // gets the tiles 
 
     for ( int i = _r -1; i < _r + 2; i++)
     {
       for ( int j = _c -1; j < _c + 2; j++)
       {
         if( (i < 0) || (i > nRows - 1) || (j < 0) || (j > nCols -1) || (i == _r && j == _c) )
         { // does nothing for these cases 
         }
         
         else{  
           if (!board[i][j].visited())
             tempList.add(board[i][j]); 
              /*
            * if the board is not visited add them to the temp list
            * */
         }     
       }
     }

     return tempList;
   }
   
   
   
   //-------------------- toString() ---------------------------------------
   /**
    * convert the board to a String representation.
    */
   public String toString()
   {
      StringBuffer out = new StringBuffer();
      for ( int r = 0; r < nRows; r++ )
      {
         for ( int c = 0; c < nCols; c++ )
         {
            out.append( board[ r ][ c ] + "\t" );
            if ( board[ r ][ c ] .toString().length() == 1 )
               out.append( " " );        // add another blank for most letters
         }
         out.append( "\n" );
      }
      return out.toString();
   }
   //+++++++++++++++++++++++ main: invoke application ++++++++++++++++++++++
   public static void main( String [] args )
   {
      Boggle.main( args );
   }
}