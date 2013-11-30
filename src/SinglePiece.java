

import java.awt.*;
import java.util.*;

/**
 * Represents a piece that has not been kinged yet.
 *
 */
public class SinglePiece extends Piece {

   private static int SINGLE = 0; // this is a single type
   private int type; // the type of the piece
   
   /**
    * Creates a single piece checker object.
    * 
    * @param c - the color of this single piece
    * @param pc - the possible captures of this single piece
    * @param pm - the possible moves of this single piece
    */
   public SinglePiece( Color c  ) {
	    super( c );
		type = SINGLE;
   }
   
   /**
    * Returns that the type of the checker is single.
    * 
    * @return type which is 0 for single
    */
   public int getType() {
	   return type;
   }  
}