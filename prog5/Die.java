/* Letter Die for BoggleBoard
 * 
 * Sides of die determined by input string
 * 
 * Input string is space delimited letters
 */

import java.util.*;

public class Die
{
   //--------------- instance variables -----------------------------
   protected ArrayList<String> letters;
   protected int               sides;

   //------------------- constructor -------------------------------
   public Die( String ls )
   {
      letters = new ArrayList<String>();
      Scanner ltemp = new Scanner( ls );
      
      while ( ltemp.hasNext() )
         letters.add( ltemp.next() );
      
      sides = letters.size();
   }

   //------------------- roll() ---------------------------------------
   public String roll()
   {
      int i = Boggle.randomNum.nextInt( sides );
      return letters.get( i );
   }   
}