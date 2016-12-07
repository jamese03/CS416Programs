/*Colored Target
 * 
 * */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.Color;
import java.awt.*;
import java.awt.Dimension;
import java.util.*;

public class ColoredShotTarget extends JComponent
{
  //-----------------instance variables---------------
  private ArrayList<JRectangle> cTargets;
  private Color[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW,Color.GREEN, 
    Color.BLUE, Color.RED, Color.YELLOW,Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW};
  private final int height = 30, width = 10;
  private int mover= 10; 
  private int countFrame;
  //--------------constructor------------------------
  public ColoredShotTarget(int x, int y, JPanel jp )  
  {
    cTargets = new ArrayList<JRectangle>();
    
    for( int i = 0; i < Targets.numTargets; i++)
    {
      JRectangle jr = new JRectangle();
      jr.setColor(colors[i]);
      jr.setSize( width, Targets.targetHeight);
      jr.setLocation(x, (i*(15 + y + height)));
      cTargets.add(jr);
      jp.add(jr);
            
    }  
    
  }
  
//--------------------moving method----------------
  public void moving()
  {
    countFrame++; // for interactions arguments 
    if ( countFrame == Targets.framesInEachDir)
    {
      mover *=-1;
      countFrame = 0;
    }
    
    if (cTargets.size() == 0)
      return;
    
    
    for ( int j = 0; j < cTargets.size(); j++)
    {
      JRectangle r = cTargets.get(j); 
      r.setLocation(r.getXLocation(), r.getYLocation()+ mover);      
    }
    
    if(cTargets.get(cTargets.size()-1).getYLocation() >= 600 )
    {
      mover*= -1;
      countFrame = 0;
    }
    else if ( cTargets.get(0).getYLocation() <= 0)
    {
      mover *= -1;
      countFrame = 0;
    }
    repaint();
  }
  
//--------------------delete method for collision------------
  public void delete(JRectangle a)
  {
    cTargets.remove(a);
  }
//-----------------get array method ------------------
  // allows the GUI to get access to the individiual target in an array
  public ArrayList<JRectangle> getArrayList()
  {
    return cTargets;
  }
//----------------clear list--------
  public void clearList()
  {
    cTargets.clear(); // clears the list of targets 
  }
    
  
}