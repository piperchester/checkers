import java.awt.Color;

/**
 * Represents a piece that has not been kinged yet.
 */
public class SinglePiece extends Piece {

   private static int SINGLE_PIECE = 0; // this is a single type
   private int type; // the type of the piece
   
   /**
    * Creates a single piece checker object.
    * 
    * @param c - the color of this single piece
    */
   public SinglePiece(Color pieceColor) {
	    super(pieceColor);
		type = SINGLE_PIECE;
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