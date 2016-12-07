/**
 * CardList.java - implements a LinkedList of Cards using the LinkedList.class
 * 
 * @author rdb
 * 10/02/10
 * 
 * Derived from CardList implemented by mlb
 */
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;

public class CardList implements CardGroup
{
    //--------------- instance variables ------------------------ 
    private int        _xLoc;         // display location for entire card list
    private int        _yLoc;
    private int        _xOffset = 0;  // x,y offsets for each card 
    private int        _yOffset = 0;  
    private JLabel     _base = null;  // label for displaying empty location
    private Container  _parent;       // parent Container
    private LinkedList<Card> _list;         // underlying list
    
    //--------------------- constructors -------------------------
    /**
     * construct a CardList to be displayed in parent at x, y 
     */ 
    public CardList( Container parent,  int x, int y )
    {
        _list = new LinkedList<Card>();
        _parent = parent;
        _xLoc = x;
        _yLoc = y;
        setLocation( _xLoc, _yLoc );
    }
    //--------------------------- head() -----------------------------------
    /**
     * return the Card in the head of the list
     */
    public Card head()
    {
       if ( _list.head() == null )
          return null;
       else
          return _list.head().data;
    }
    //--------------------------- tail() -----------------------------------
    /**
     * return the Card in the head of the list
     */
    public Card tail()
    {
       if ( _list.tail() == null )
          return null;
       else
          return _list.tail().data;
    }
    //----------------------- getList() -------------------------------------
    /**
     * return a reference to the underlying LinkedList 
     */
    public LinkedList<Card> getList()
    {
       return _list;
    }
    //--------------------- makeBaseIcon( String ) --------------------------
    /**
     * generate a baseIcon to represent the position of the CardList when
     * it is empty. 
     */
    public void makeBaseIcon( String text )
    {
        _base = new JLabel( text );
        _base.setSize( Card.width, Card.height );
        _base.setLocation( _xLoc - 4, _yLoc );
        _base.setBorder( new LineBorder( Color.BLACK, 1 ));
        _base.setOpaque(true);
        _base.setBackground( Color.LIGHT_GRAY );

        _parent.add( _base );  
    }
    
    //--------------------- addByRank( Card ) -------------------------------
    /**
     * add the card to the list in order based on the compareTo method of Card.
     */    
    public void addByRank( Card newCard )
    {
       _list.addInOrder( newCard );
    }

    //--------------------- addTail( Card ) -------------------------------
    /**
     * add the card to the end of the list.
     */    
    public void addTail( Card newCard )
    {
       _list.addTail( newCard );
    }

    //--------------------- remove( Card ) -------------------------------
    /**
     * add the card to the end of the list.
     */    
    public void remove( Card newCard )
    {
       _list.remove( newCard );
    }

    //--------------------- size() -------------------------------
    /**
     * return size of list
     */    
    public int size()
    {
       return _list.size();
    }
    
    //--------------------- get( int ) -------------------------------
    /**
     * return n-th Card in the list
     */    
    public Card get( int n )
    {
       Node<Card> node = _list.get( n );
       if ( node == null )
          return null;
       else
          return node.data;
    }
    
    //--------------------- clear() -------------------------------
    /**
     * remove all elements from the list
     */    
    public void clear()
    {
       _list.clear();
    }
    //----------------------- findNode( Card ) --------------------------
    /**
     * Find the node on the list that contains an object that compares
     * as equal to the parameter passed in. Return the Node.
     */   
    public Node<Card> findNode( Card card ) 
    {
       return _list.findNode( card );
    }
    //--------------------- setXOffset( int )--------------------------
    /**
     * set the offset in x for showing the edges of the cards in the stack
     */ 
    public void setXOffset( int m )
    {
        _xOffset = m;  
    }    
    
    //---------------------setYOffset( int )-----------------------
    /**
     * set the offset in y for showing the edges of the cards in the stack
     */ 
    public void setYOffset( int m )
    {
        _yOffset = m;  
    }
        
    //--------------------- displayCards( int, boolean ) -------------------
    /**
     * redisplay the first "num" cards in the list; show face if 2nd param is
     *    true, else show back
     */ 
    public void displayCards( int num, boolean face )
    {
        int size = size();
        if (  size == 0 )
            return;
        int show = Math.min( num, size );  // number to show
        
        if ( num < 0 )
            show = size;

        int xOff = _xOffset;
        int yOff = _yOffset;
        
        if ( num == 0 )
           xOff = yOff = 0;
        int count = 0;
        
        // place from front of list to end
        for ( int c = 0; c < size(); c++ )
        {
            Card card = get( c );  
            if ( card == null )
            {
               System.err.println( "CardList:displayCards null card: pos, size" 
                                     + c + ", " + size() );
            }
            else
            {
               if ( c > show - 1 || !face )
                  card.setFaceUp( false );
               else
                  card.setFaceUp( true );     
               card.setLocation( _xLoc + xOff * count,
                                _yLoc + yOff * count++ );  
               
               _parent.setComponentZOrder( card, 0 );  
            }
        }
    }
      
    //------------------- setLocation( int, int ) ----------------------
    /**
     * set the location of the list, which means changing the locations
     *   of all the cards in the list.
     */
    public void setLocation( int x, int y )
    {
        _xLoc = x;
        _yLoc = y;
        if ( _base != null )
           _base.setLocation( x, y );
        for ( int i = 0; i < this.size(); i++ )
            get( i ).setLocation( x + _xOffset * i, y + i  * _yOffset );
        
    }
    
    //------------------- getXLocation() --------------------------
    /**
     * return the x location for the display of this card stack
     */   
    public int getXLocation()
    {
        return _xLoc;
    }
    
    //------------------- getYLocation() --------------------------
    /**
     * return the y location for the display of this card stack
     */
    public int getYLocation()
    {
        return _yLoc;
    }
}