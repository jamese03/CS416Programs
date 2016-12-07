/**
 * CardGroup -- encapsulates shared features of CardStack and CardList.
 *             mainly used to facilitate display.
 */
public interface CardGroup
{
   public Card get( int i );     // get the i-th card
   public int  size();           // number Cards in the group
   public int  getXLocation();   // return x location
   public int  getYLocation();   // return y location
}