import java.util.*;
import java.awt.*;

/**
 *  The board. Holds a collection of pieces.
 *
 */
public class Board {

   private Piece pieces[];
   public static int SINGLE = 0;
   public static int KING = 1;
   final private int MAX_BOARD_SIZE = 64;

   /**
    * Creates a new board at the beginning of the game. Generates 24 piece
    * objects, 12 each color, and puts them in in the pieces array.
    */
   public Board() {
	   pieces = new Piece[MAX_BOARD_SIZE];

	   // Blue piece creation.
	   pieces[1] = new SinglePiece( Color.blue );
	   pieces[3] = new SinglePiece( Color.blue );
	   pieces[5] = new SinglePiece( Color.blue );
	   pieces[7] = new SinglePiece( Color.blue );
	   pieces[8] = new SinglePiece( Color.blue );
	   pieces[10] = new SinglePiece( Color.blue );
	   pieces[12] = new SinglePiece( Color.blue );
	   pieces[14] = new SinglePiece( Color.blue );
	   pieces[17] = new SinglePiece( Color.blue );
	   pieces[19] = new SinglePiece( Color.blue );
	   pieces[21] = new SinglePiece( Color.blue );
	   pieces[23] = new SinglePiece( Color.blue );

	   // White piece creation.
	   pieces[40] = new SinglePiece( Color.white );
	   pieces[42] = new SinglePiece( Color.white );
	   pieces[44] = new SinglePiece( Color.white );
	   pieces[46] = new SinglePiece( Color.white );
	   pieces[49] = new SinglePiece( Color.white );
	   pieces[51] = new SinglePiece( Color.white );
	   pieces[53] = new SinglePiece( Color.white );
	   pieces[55] = new SinglePiece( Color.white );
	   pieces[56] = new SinglePiece( Color.white );
	   pieces[58] = new SinglePiece( Color.white );
	   pieces[60] = new SinglePiece( Color.white );
	   pieces[62] = new SinglePiece( Color.white );
   }

   /**
    * Move the piece at the start position to the end position, if not occupied.
    * 
    * @param start - current location of the piece
    * @param end - the position where piece is moved
    * 
    * @return -1 if there is a piece in the end position, 0 otherwise.
    */
   public int movePiece(int start, int end) {
	   if (isOccupied(end)) {
	   	   return -1;
	   } else {
		   pieces[end] = pieces[start];
		   pieces[start] = null;
		   return 0;
	   }
   }

   /**
    * Checks if the space on the board contains a piece. If outside board
    * array bounds, return true.
    * 
    * @param space - the space that needs to be checked
    * 
    * @return true if space is occupied, false otherwise
    */
   public boolean isOccupied(int space) {
	   return ( space >= 1 && space <= 63 && pieces[space] == null ) ? false : true;
   }

   
   /**
    * Removes piece at provided position space. Set piece to null.
    * 
    * @param space - the positon of the piece to be removed
    */
   public void removePiece(int space) {
	   pieces[space] = null;
   }
   
   
   /**
    * Creates a king piece. Adds new king to array of pieces. 
    * 
    * @param space - the position at which the king piece is created 
    */
   public void kingPiece(int space) {
	   Color color = pieces[space].getColor();
	   Piece piece = new KingPiece( color );
	   pieces[space] = piece;   
   }
   
   
   /**
    * Returns the color of the piece at a certain space. If no piece
    * exists at space in board, return null.
    * 
    * @param space - the position of the piece on the board
    * 
    * @return the color of this piece if piece exists, else null.
    */
   public Color getPieceColorAtSpace(int space) {   
	   return isOccupied(space) ? pieces[space].getColor() : null;
   }
   

   /**
    * Returns the piece at the certain position.
    * 
    * @param space - the space of the piece
    * 
    * @return piece if it exists at that space.
    */
   public Piece getPieceAt(int space) {

	   Piece returnValue = new SinglePiece(Color.red);
	   
	   try{
	      if(isOccupied(space)) {
	   	   	   returnValue = pieces[space];
	   	   }
	   
	   } catch( ArrayIndexOutOfBoundsException e ) {
                          
	   } catch( NullPointerException e ) {
	   	
	   }
	   
      return returnValue;
   }
   
   /**
    * Returns if there is a piece of color on the board.
    * 
    * @param color - the color of the piece
    * 
    * @return true if a piece of color left on the board, else false
    */
   public boolean boardContainsPiece( Color color) {
	   for( int i = 1; i < pieces.length; i++ ) {
	   	   if( pieces[i] != null && pieces[i].getColor() == color ) {
   	   	   	   i = pieces.length;
   	   	   	   return true;
	   	   } 
	   }
	  
	   return false;
   }
   

   /**
    * Returns the size of the board.
    * 
    * @return the size of the board, always 64
    */
   public int sizeOf() {
       return MAX_BOARD_SIZE;
   }
   
   /**
    * Returns a vector containing all blue Pieces.
    * 
    * @return blue pieces on the board
    */
   public Vector<Piece> getBluePieces() {
   
      Vector<Piece> bluePieces = new Vector<Piece>();
      
      for (int i = 0; i < MAX_BOARD_SIZE; i++) {
          if (isOccupied(i)) {
              if (pieces[ i ].getColor() == Color.blue) {
                  bluePieces.addElement( pieces[ i ] );
              }
          }                 
      }
      
      return bluePieces; 
  }
 
 
   /**
    * Returns a vector containing all white Pieces.
    * 
    * @return white pieces on the board
    */
  public Vector<Piece> getWhitePieces() {
      Vector<Piece> whitePieces = new Vector<Piece>();
      
      for (int i = 0; i < MAX_BOARD_SIZE; i++) {
          if ( isOccupied(i) ) {
              if (pieces[i].getColor() == Color.white) {
                  whitePieces.addElement(pieces[i]);
              }
          }                 
      }
      
      return whitePieces;
  }
}