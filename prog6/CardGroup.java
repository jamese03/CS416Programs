/**
 * CardGroup -- encapsulates shared features of card collections without
 *              imposing a particular semantics.
 *             mainly used to facilitate display.
 */
public interface CardGroup
{
   public Card get( int i );         // get the i-th card; the definition of
                                     // what i means is up to the implementation
   public int  size();               // number Cards in the group
   public int  getXLocation();       // return x location
   public int  getYLocation();       // return y location
   public void setXOffset( int dx ); // the x,y offset for displaying the group
   public void setYOffset( int dy ); //  0,0 is on top of each other
}