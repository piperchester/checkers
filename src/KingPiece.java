/**
 * KingPiece.java
 *
 * Version:
 *   $Id: KingPiece.java,v 1.1 2002/10/22 21:12:52 se362 Exp $
 *
 * Revisions:
 *   $Log: KingPiece.java,v $
 *   Revision 1.1  2002/10/22 21:12:52  se362
 *   Initial creation of case study
 *
 */
import java.awt.*;

/**
 * Represents a king piece.
 *
 */
public class KingPiece extends Piece {
   private static int KING = 1;
   private int type; 
  
   /**
    * This constructor creates a king piece object
    * 
    * @param kingColor - the color of this king piece
    */
   public KingPiece( Color kingColor ) {
	   super( kingColor ); 
	   type = KING;
   }
   
   /**
    * Returns the type of piece that this object is.
    * 
    * @return 1 for the king piece representation
    */
   public int getType() {
	   return type;
   }  
}