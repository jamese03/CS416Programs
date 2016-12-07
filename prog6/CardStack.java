/**
 * CardStack -- a stack of cards
 * 
 * Built on Stack.
 * 
 * @author rdb
 * March 8, 2009
 * 
 * mlb 10/09
 */
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CardStack extends Stack<Card> implements CardGroup
{
    //--------------- instance variables ------------------------ 
    private int    _xLoc;
    private int    _yLoc;
    private int    _xOffset = 0;
    private int    _yOffset = 0;
    private JLabel _base;
    private JPanel _parent;
    
    //--------------------- constructors -------------------------
    /**
     * construct a CardStack to be displayed in parent at x, y 
     */ 
    public CardStack( JPanel parent,  int x, int y )
    {
        super(); 
        
        _parent = parent;
        _xLoc = x;
        _yLoc = y;
        setLocation( _xLoc, _yLoc );

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
    
    //------------------ push ---------------------------------
    /**
     * push a card onto the stack
     */ 
    public Card push( Card c )
    {
        super.push( c );
        return c;
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
     * redisplay the top "num" cards in the stack; show face if 2nd param is
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
        
        int count = 0;
        
        // place from bottom of stack
        for ( int c = size( ) - 1; c >= 0; c-- )
        {
            Card card = get( c );  
            if ( c > show - 1 || !face )
                card.setFaceUp( false );
            else
                card.setFaceUp( true );     
            card.setLocation( _xLoc + _xOffset * count,
                              _yLoc + _yOffset * count++ );  
            //////////////////////////////////////////////////////////////
            // Need to invoke the setComponentZOrder method of our _parent
            //   container in order to ensure that the cards are displayed 
            //   so that the top of the stack is displayed last.
            // The 1st parameter to setComponentZOrder is the component
            //   whose order is being specified (the Card object in our case).
            // The 2nd parameter specifies the "Z" index of the component 
            //   where large Z values are painted first -- therefore 
            //      low index Z components paint OVER high index ones.
            // Components with the same index are painted in the order
            //   in which their Z index has been specified.
            // Hence, the "later" ones draw over "earlier" ones.
            //
            // We assign the Z index of the Cards from the bottom of the stack 
            //   to the top and we give them all the same Z-index. Hence the top 
            //   of the stack is drawn last. The index parameter must be 
            //   between 0 and N-1, where N is the number of components in the
            //   container. The easiest index to use is 0, as long as there
            //   are no other components that need to be drawn over the 
            //   cards in this stack. This is the case for this problem.
            //
            ////////////////////////////////////////////////////////////////
            _parent.setComponentZOrder( card, 0 );  
            
        }
    }
    
    //------------------- get( int ) -------------------------
    /**
     * returns the i-th entry for display purposes; we think of entry 0
     * as being the top of the stack, but the implementation of the java
     * Stack class (probably built on top of ArrayList), puts the top of
     * the Stack as element stack.size() - 1. We invert the index based
     * on this knowledge. This one way that we can re-use an existing class
     * whose semantics is not exactly what we want.
     */
    public Card get( int i )
    {
        return super.get( size() - i - 1 );
    }
    
    //------------------- setLocation( int, int ) ----------------------
    /**
     * set the location of the stack, which means changing the locations
     *   of all the cards in the stack.
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
