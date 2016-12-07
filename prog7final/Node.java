
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;

public class Node 
{
  public CardStack one, two;
  public ArrayList<Node> kids; 
  public int depth, score;
  public int bestKid = -1;
  public Node parent;
  
//-------------------------------Constructor----------------------------------- 
  
  public Node()
  {
    
  }
//----------------------helper move method-----------------
  public void move()
  {
    if(two !=null && one!=null)
    two.push(one.pop());
    
  }
//---------------------------------helper undo method--------------------------  
  public void undo()
  {
    if(one !=null && two !=null)
    one.push(two.pop());
    
  }
  
}