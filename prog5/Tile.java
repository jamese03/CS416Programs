/**
 * Tile for BoggleBoard -- skeleton
 * 
 * Knows its letter and its location on the board, but you
 *    may want to revise the the "letter" handling to 
 *    account for the fact that "q" is really "qu".
 * 
 * Needs to know about its visited status
 * and who its neighboring tiles are.
 * 
 * @author jb
 * Summer 2010
 * 
 * 02/19/11 rdb: Formatting and style edits
 */

import java.util.*;

public class Tile
{
   //---------------------- instance variables -------------------------
   private String          letter;
   private int             row, col;
   public Boolean isVisit = false;

   //------------------ constructor --------------------------------------
   public Tile( int r, int c )
   {
      row = r;
      col = c;
      letter = null;
   }
   //------------------ getCol() --------------------------------------
   /**
    * returns the col location
    */
   public int getCol()
   {
      return col;
   }
   //------------------------setLetter()-------------------------------
   /*set the letter
    * */
   public void setLetter(String l)
   {
     letter = l;
   }

   //------------------ getRow() --------------------------------------
   /**
    * returns the row location
    */ 
   public int getRow()
   {
      return row;
   }
//-----------------------says its visit----------------------------
   public Boolean visited()
   {
     return isVisit;    
   }
//---------------------setting the value------------   
   public void setVisited() 
   { 
     isVisit = true;
   }
   //----------clear visit---------------------
   public void clearVisit()
   {
     isVisit = false;
   }
   
   //--------------------------- toString() -------------------------
   public String toString()
   {
      return letter;
   }
}