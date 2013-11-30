

/**
 * Abstract class representing any piece that
 * knows about its color and possible moves and captures
 *
 * @author
 *
 */

import java.awt.*;

public abstract class Piece {
	
   private Color color; // the color of the piece

   /**
    * The constructor for a piece.
    * 
    * @param c - the color for this piece
    */
   public Piece( Color c ) {
	   color = c;
   }

   /**
    * The method which is abstract
    * 
    * @return the type of the piece
    */
   abstract int getType();
   
   /**
    * Returns piece color.
    * 
    * @return the color for this piece
    */
   public Color getColor() {
	   return color;
   }
}