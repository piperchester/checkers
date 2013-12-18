import java.util.*;
import java.awt.*;

/**
 * This class is used to check the validity of the moves 
 * made by the players.  It also checks to see if the conditions 
 * for the end of the game have been met.
 * 
 */
public class Rules {
     
    private Board theBoard; 
    private Driver theDriver; 
    private Move currentMove; 
    private int adjacentSpots[] = { -9, -7, 7, 9 }; 
    private int secondSpots[] = { -18, -14, 14, 18 }; //  Adjacent to adjacent spots
    private int middle = 0;  // The space of a piece that gets jumped
    private Vector<Integer> leftWallPieces = new Vector<Integer>(); // Positions of the left
    private Vector<Integer> rightWallPieces = new Vector<Integer>(); // Positions of the right
    
    /**
     * The constructor for the Rules class.
     *
     * @param board - the checker board.
     * @param driver - the main driver of the program.
     */
    public Rules( Board board, Driver driver ) {
		theBoard = board;
		theDriver = driver;
		fillWallVector();
		
    }
    
    /**
     *  This method checks to see if the move that was just made
     *  was valid and returns a boolean indicating that.
     *
     *  @param move  The move that is to be validated.
     * 
     *  @return Boolean indicating if it was valid.
     * 
     *  @pre a player has made a move
     *  @post the player knows if the move has legal
     */
    public boolean validateMove( Move move ) {
        
		currentMove = move;	
		boolean isValid = false;
	
        try {
		    
		    boolean anotherMove = false;  // If there is another move that must be made
		    boolean gameOver = false;    
		    Player player = currentMove.getPlayer(); // Current player.
		    Player oppositePlayer = theDriver.getOppositePlayer(); 
		    int start = currentMove.startLocation(); // Start of piece.
		    int end = currentMove.endLocation();  // Attempted end location
		                                             // of the piece.
		    int pieceType = theBoard.getPieceAt( start ).getType();// Type of
		                                                      // the piece.
		    // Contains any possible moves if the piece is on the wall.
	        Vector wallMoves = new Vector();
		    Vector<Integer> tempVec = new Vector<Integer>();
		    Vector<Integer> startVec = new Vector<Integer>();
		    //Vector possibleJumps = new Vector();
		    Vector possibleJumps = checkForPossibleJumps( start, pieceType,
								  player );
			
			// For each piece of the current color, see if there are forced jumps.
			for ( int count = 1; count < 64; count++ ) {
				if ( theBoard.isOccupied( count ) ) {
					if ( theBoard.getPieceAt( count ).getColor() == player.getColor() ) {
						tempVec = checkForPossibleJumps( count, pieceType, player );
						if ( !tempVec.isEmpty() ) {
							startVec.addElement( new Integer( count ) );
							possibleJumps.addAll( tempVec );
						}
					}
				}
												
			}
		
	    
	    // Only proceed if player is trying to move one of his own pieces
													  
		if ( !theBoard.getPieceColorAtSpace( start ).equals(
			        theDriver.getOppositePlayer().getColor() ) ) {
		// If there is a possible jump it must be made so the end 
		// position must match one of the possible jumps.
		if ( startVec.contains( new Integer( start ) ) ) {     
		    possibleJumps = checkForPossibleJumps( start, pieceType, 
							   player );
		    if ( possibleJumps.contains( new Integer( end ) ) ) {
			// Move the piece
			theBoard.movePiece( start, end ); 
			// Remove the jumped piece
			theBoard.removePiece( middle );
			middle = 0;		
			// And if there is a possible multiple jump.
			anotherMove = checkForOtherPossibleJump( end, pieceType,
								 player );
			// If there is another jump to make, next turn will be
			// current player's and he must move last piece moved.
			if ( anotherMove ) {
			    theDriver.endTurn( player, end );
			}
			// Otherwise, next turn should be the opposite player's.
			else {
			    theDriver.endTurn( oppositePlayer, 0 );
			}
			isValid = true;
		    } // There is no required jump.
		}
		// Otherwise the player is free to make any move that is legal.
		else if ( possibleJumps.isEmpty() ) {
                    
		    // If the piece starts on a wall and it's end position is 
		    // valid then the move is legal.
		    if ( leftWallPieces.contains( new Integer( start ) ) ||
			 rightWallPieces.contains( new Integer( start ) ) ) {
			wallMoves.addAll( wallPieceMoves( start, false, 
							  pieceType, player ) );
			if ( wallMoves.contains( new Integer( end ) ) ) {
			    isValid = true;
			}
		    }
		    
		    // If the end position is not occupied then validate move.
		    if ( !theBoard.isOccupied( end ) ) { 
			isValid = validateRegularMove( start, end, player );
		    }
                
		    // If move was OK check end conditions. If game was not won,
		    // end turn.
		    if ( isValid ) {
                        
			// If a move was made, see if the piece should be kinged
			kingCheck( end, pieceType );
                        gameOver = checkEndCond();
			if ( gameOver ) {
			    theDriver.endGame( player.getName() +  
					       " won the game." );
			} else {			    
			    theBoard.movePiece( start, end );
			    theDriver.endTurn( oppositePlayer, 0 );   
			}
			
		    } 
                } // Move is either valid or not. 
	    } // end if piece on start space is the correct color
	    
	    // If the move was not valid, tell the player. 
	    if ( !isValid ) {
		theDriver.endTurn( player, -1 );
	    }
            kingCheck(end, pieceType);
        } catch ( Exception e ) { }
	
	return isValid;
	
    }
    
    /**
     *  Used after a move has been completed to check to see
     *  if the conditions have been met for ending the game.
     *  a boolean is returned indicating whether or not those
     *  conditions have been met.
     *
     *  @return retval true indicating the game is to end.
     * 
     *  @pre a capture was successful
     */
    private boolean checkEndCond() {
    	return doesPassivePlayerHavePieces() ? true : false;
    }
    
    /**
     *  Will check if there are any more pieces left for the player 
     *  whose turn it is not.
     * 
     *  @return true if the other player has no more pieces.
     *
     *  @pre A capture was successful.
     */ 
    private boolean doesPassivePlayerHavePieces() {
	
    	Player oppositePlayer = theDriver.getOppositePlayer();
	
		// If the board does not have any pieces of the opposite player,
		// the current player has captured all pieces and won.
		if ( !theBoard.boardContainsPiece( oppositePlayer.getColor() ) ) {
		    return true;
		} else {
			return false;
		}
	
    }
    
    /**
     *  Will check the board for any jumps that are open to the current 
     *  player. If there are any possible jumps the valid end positions 
     *  will be added to the vector.
     *
     *  @param piecePosition - start position of piece. 
     *  @param pieceType     - type of piece.
     *
     *  @return possibleJumps which contains end positions of possible jumps.
     */	
    private Vector checkForPossibleJumps( int piecePosition, int pieceType, 
					  Player aPlayer ) {
	
	Vector possibleJumps = new Vector();
	boolean adjacentSpace = false;
	boolean endSpace = false;
	
	Player player = aPlayer; // The current player.
        Piece aPiece = new SinglePiece( player.getColor());
	int i = 0;
        int loop = 0;
	
	// Get available moves if the piece is on a wall.
	possibleJumps.addAll( wallPieceMoves( piecePosition,
					      true, pieceType, player ) );
	
	// If the piece is a king.
	if ( pieceType == Board.KING_PIECE_TYPE) {       
	    
	    // King is on top wall.
            if ( piecePosition <= 7 ) {
                i = 2;
                loop = adjacentSpots.length;
            } 
	    // King is on bottom wall.
	    else if ( piecePosition >= 56 ) {
                i = 0;
                loop = 2;
            } else {
                i = 0;
                loop = adjacentSpots.length;
            }
	    
	    // Check to see if piece is adjacent to piece of opposite color.
	    // If there are, add possible end locations to vector.
	    for ( ; i < loop; i++ ) {
		if ( !leftWallPieces.contains( new Integer( piecePosition +
						      adjacentSpots[i] ) ) && 
		     !rightWallPieces.contains( new Integer( piecePosition +
						      adjacentSpots[i] ) ) ) {
		    adjacentSpace = theBoard.isOccupied( piecePosition + 
						       adjacentSpots[i] );
		    aPiece = theBoard.getPieceAt( piecePosition +
						  adjacentSpots[i] );
		    
		    if ( adjacentSpace && 
			 ( aPiece.getColor() != player.getColor() ) ) {
		    // Check space diagonal to the piece to see if it is empty.
			endSpace = theBoard.isOccupied( piecePosition +
						      secondSpots[i] );
			/// If the space is empty, the jump is valid and it is 
			// added to vector of possible jumps.
			if ( !endSpace ) {
			    possibleJumps.addElement( new Integer( piecePosition + secondSpots[i] ) );
			    // If the player has selected to make this jump,
			    // let other methods access the piece to be jumped
			    if ( currentMove.endLocation() == piecePosition + 
				 secondSpots[i] ) {
				middle = piecePosition + adjacentSpots[i];   
			    }
			}			 	    
		    }	
		    adjacentSpace = false;	
		} // end for           
	    } // end if (the piece is not a king).
	}
	// If piece is white...        
	else if ( player.getColor() == Color.white ) {      
	    
	    
	    for ( int j = 0; j <= 1; j++ ) {
		if ( !leftWallPieces.contains( new Integer( piecePosition +
						       adjacentSpots[j] ) ) && 
		     !rightWallPieces.contains( new Integer( piecePosition +
						       adjacentSpots[j] ) ) ) {
		    // Check to see if there is a piece this piece can jump over.
		    adjacentSpace = theBoard.isOccupied( piecePosition + 
						       adjacentSpots[j] );
		    // Get that piece
                    
                    if( adjacentSpace ){
		    aPiece = theBoard.getPieceAt( piecePosition +
						  adjacentSpots[j] );
		    //player = aPlayer;
                    }
		    // Make sure the piece is the right color
		    if ( adjacentSpace && aPiece.getColor() !=
			 player.getColor() ) {
			// Check space diagonal to piece to see if it is empty.
			endSpace = theBoard.isOccupied( piecePosition + 
						      secondSpots[j] );
			// If space is empty, there is a jump that must be made.
			if ( !endSpace ) {
			    possibleJumps.addElement( new Integer( piecePosition
							    + secondSpots[j] ) );
			}
			// If the player has selected to make this jump,
			// let other methods access the piece to be jumped
			if ( currentMove.endLocation() == piecePosition 
			                                 + secondSpots[j] ) {
			    middle = piecePosition + adjacentSpots[j];
			}   
		    }
		    adjacentSpace = false;
		} // end for
	    }
	}
	// else the Color is blue and can only move down.
	else {
	    for ( int k = 2; k < adjacentSpots.length; k++ ) {
		if ( !leftWallPieces.contains( new Integer( piecePosition + 
						        adjacentSpots[k] ) ) && 
		     !rightWallPieces.contains( new Integer( piecePosition +
							 adjacentSpots[k] ) ) ) {
		    adjacentSpace = theBoard.isOccupied( piecePosition +
						       adjacentSpots[k] );
		    aPiece = theBoard.getPieceAt( piecePosition +
						     adjacentSpots[k] ); 
		    
		    if ( adjacentSpace && ( aPiece.getColor() !=
					    player.getColor() ) ) {
			// Check space diagonal to piece to see if it is empty.
			endSpace = theBoard.isOccupied( piecePosition + 
						      secondSpots[k] );
			// If space is empty, a multi jump that must be made.
			if ( !endSpace ) {
			    possibleJumps.addElement( new Integer( piecePosition
							   + secondSpots[k] ) );
			    if ( currentMove.endLocation() == piecePosition +
				 secondSpots[k] ) {
				middle = piecePosition + adjacentSpots[k];   
			    }
			}			    
		    }
		    adjacentSpace = false;
		} // end for
	    }
	}
	
	return possibleJumps;
	
    }
    
    /**
     *   After a piece has made a jump, check to see if another jump can
     *   be made with the same piece.
     *
     *  @param piecePosition - start position of the piece.
     *  @param pieceType     - the type of the piece.
     *
     *  @return retval true if there is a nother jump that must be made.
     *
     */   
    private boolean checkForOtherPossibleJump( int piecePosition, int pieceType, Player aPlayer ) {
	
    	boolean isJumpable = false;
	
    	try{
	    
	    boolean adjacentSpace = false;
	    Vector<Integer> possibleJumps = new Vector<Integer>();
	    Piece aPiece;
	    Player player = aPlayer;
	    boolean endSpace = false;
	    
	    // Get available moves if the piece is on a wall.
	    possibleJumps.addAll( wallPieceMoves( piecePosition, 
						  true, pieceType, player ) );
	    
	    // If the piece is a king.
	    if (pieceType == Board.KING_PIECE_TYPE) {
		
		// Check to see if piece is adjacent to piece of opposite color.
		// If there are, add possible end locations to vector.
		for ( int i = 0; i <= adjacentSpots.length; i++ ) {
		    if ( !leftWallPieces.contains( new Integer( piecePosition +
							 adjacentSpots[i] ) ) && 
			 !rightWallPieces.contains( new Integer( piecePosition +
							 adjacentSpots[i] ) ) ) {
			
			adjacentSpace = theBoard.isOccupied( piecePosition +
							   adjacentSpots[i] );
			aPiece = theBoard.getPieceAt( piecePosition +
						      adjacentSpots[i] );
			
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    
			    // Check space diagonal to piece to see if its empty.
			    endSpace = theBoard.isOccupied( piecePosition + 
							  secondSpots[i] );
			    
			    // If space is empty, a multiple jump must be made.
			    if ( !endSpace ) {
			    	isJumpable = true;
				}			 
			}
			
			adjacentSpace = false;
		} 
		}
	    } 
	    // Else the piece is regular.
	    else {
		// If it is white the player can only move up.
		if ( player.getColor() == Color.white ) {
		    for ( int j = 0; j <= 1; j++ ) {
		    	if ( !leftWallPieces.contains( new Integer( 
					piecePosition + adjacentSpots[j] ) ) && 
			     !rightWallPieces.contains( new Integer(
					piecePosition + adjacentSpots[j] ) ) ) {
			    adjacentSpace = theBoard.isOccupied( piecePosition +
							    adjacentSpots[j] );
			    aPiece = theBoard.getPieceAt( piecePosition +
							  adjacentSpots[j] );
			    player = currentMove.getPlayer();
			    if ( adjacentSpace && ( aPiece.getColor() != 
						    player.getColor() ) ) {
				// Check space diagonal to the piece to see if
				// it is empty.
				endSpace = theBoard.isOccupied( piecePosition +
							      secondSpots[j] );
				// If the space is empty, there is a 
				// multiple jump that must be made.
				if ( !endSpace ) {
				    isJumpable = true;
				}			 
			    }
			    adjacentSpace = false;
			} // end for
		    }
		} 
		// else the Color is blue and can only move down.
		else {
		    for ( int k = 2; k <= adjacentSpots.length; k++ ) {
			if ( !leftWallPieces.contains( new Integer(
					piecePosition + adjacentSpots[k] ) ) && 
			     !rightWallPieces.contains( new Integer( 
					piecePosition + adjacentSpots[k] ) ) ) {
			    int a = piecePosition + adjacentSpots[k];
			    adjacentSpace = theBoard.isOccupied( a );
			    aPiece = theBoard.getPieceAt( piecePosition +
							  adjacentSpots[k] );
			    
			    player = currentMove.getPlayer(); 
			    if ( adjacentSpace && ( aPiece.getColor() !=
						    player.getColor() ) ) {
				// Check the space diagonal to the piece to see 
				// if it is empty.
				endSpace = theBoard.isOccupied( piecePosition +
							      secondSpots[k] );
				
				// If the space is empty, there is a multiple
				// jump that must be made.
				if ( !endSpace ) {
				    isJumpable = true;
				}			 
			    }
			    adjacentSpace = false;
			} 
		    }
		}
	    }
	    
        } catch( Exception e ) { }
	
    	return isJumpable;
	
    }
    
    /*
     * Validate a regular, non jumping move.
     *
     * @param piecePosition - the starting piece position.
     * @param end - the end piece position.
     *
     * @return true if the move is valid.
     */
    private boolean validateRegularMove( int piecePosition, int end, 
					 Player aPlayer ) {
	
	boolean retval = false;
	Player player = aPlayer;

	// If piece is a king
	if ( theBoard.getPieceAt( piecePosition ).getType() == Board.KING_PIECE_TYPE ) {
	    //Check if piece is on wall. If it is it's movement is restricted.
	    if ( leftWallPieces.contains( new Integer( piecePosition ) ) ||
                 rightWallPieces.contains( new Integer( piecePosition ) )) {
		// Check if end position is valid.
		if ( end == piecePosition + 7 || end == piecePosition - 7 ) {
		    retval = true;
		}
		// Piece is not on left or right wall.
	    } else {
		// Check if end position is valid.
		if ( end == piecePosition - 7 || end == piecePosition - 9 ||
		     end == piecePosition + 7 || end == piecePosition + 9 ) {
		    retval = true;
		}
	    }
	} // end if (piece is king).
	
	// If the piece is regular piece then it can only move in one
	// direction and has at most 2 possible moves.
	else {
	    // If the piece is white it may only move up (down the array).
	    // If it is on the wall it can only move to one spot.
	    if ( player.getColor() == Color.white &&
		 ( leftWallPieces.contains( new Integer( piecePosition ) ) ||
                   rightWallPieces.contains( new Integer( piecePosition ) ) ) &&
		 ( end == piecePosition - 9 ) ) {
		
		retval = true;
	    } 
	    // Otherwise the piece can move to one of two places.
	    else if ( player.getColor() == Color.white && 
		    ( end == piecePosition - 7 || end == piecePosition - 9 ) ) {
		
		retval = true;
	    }	
	    // Otherwise the player is blue and can only move down (up array).
	    // If the player is on the wall it can only move to one spot.
	    else if ( player.getColor() == Color.blue && 
		      ( leftWallPieces.contains( new Integer( piecePosition ) )
		 || rightWallPieces.contains( new Integer( piecePosition ) ) ) &&
		      ( end == piecePosition + 9 ) ) { 
		
		retval = true;
	    }
	    // The piece is free to move to one of two spots down the board.
	    else if ( player.getColor() == Color.blue &&
		    ( end == piecePosition + 7 || end == piecePosition + 9 ) ) {
		retval = true;
	    }
	} 
	
	return retval;
	
    }
    
    /*
     *  Fill the vectors that contain wall pieces.
     *
     */
    private void fillWallVector() {
		rightWallPieces.addElement( new Integer( 7 ) );
		leftWallPieces.addElement( new Integer( 8 ) );
		rightWallPieces.addElement( new Integer( 23 ) );
		leftWallPieces.addElement( new Integer( 24 ) );
		rightWallPieces.addElement( new Integer( 39 ) );
		leftWallPieces.addElement( new Integer( 40 ) );
		rightWallPieces.addElement( new Integer( 55 ) );
		leftWallPieces.addElement( new Integer( 56 ) );
    }
    
    /*
     *  This method will check the available moves for pieces if the piece
     *  is on the left or right walls. 
     *
     *  @param piecePosition - the starting position of the piece.
     *  @param jump          - true if the piece is making a jump.
     *  @param pieceType     - type of piece.
     *
     *  @return moves, a vector of end positions for the piece.
     */
    private Vector<Integer> wallPieceMoves( int piecePosition, boolean isJumping, int pieceType , Player aPlayer ) {
	
    	Vector<Integer> moves = new Vector<Integer>();
	
        try{
	    
	    boolean adjacentSpace = false;
	    Piece aPiece;
	    Player player = aPlayer;
	    boolean endSpace = false;
	    
	    if ( pieceType == Board.KING_PIECE_TYPE ) {
		if ( leftWallPieces.contains( new Integer( piecePosition ) ) ) {
		    if ( isJumping ) {
			adjacentSpace = theBoard.isOccupied( piecePosition - 7 );
			aPiece = theBoard.getPieceAt( piecePosition - 7 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = theBoard.isOccupied( piecePosition - 14 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition
							       - 14 ) );
			    }
			}
			adjacentSpace = theBoard.isOccupied( piecePosition + 9 );
			aPiece = theBoard.getPieceAt( piecePosition + 9 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to see if
			    // it is empty.
			    endSpace = theBoard.isOccupied( piecePosition + 18 );
			    // If the space is empty, there is a multiple 
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(new Integer( piecePosition
							      + 18 ) );
			    }
			}
		    } 
		    // Otherwise check for a regular move, not a jump.
		    else {
			adjacentSpace = theBoard.isOccupied( piecePosition - 7 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition - 7 ) );
			}
			adjacentSpace = theBoard.isOccupied( piecePosition + 9 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition + 9 ) );
			}
		    }
		}
		// If the piece is on the right wall.
                if ( rightWallPieces.contains( new Integer( piecePosition ) ) ) {
	            if ( isJumping ) {
			adjacentSpace = theBoard.isOccupied( piecePosition - 9 );
			aPiece = theBoard.getPieceAt( piecePosition - 9 );
			if ( adjacentSpace && ( aPiece.getColor() != 
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to 
			    // see if it is empty.
			    endSpace = theBoard.isOccupied( piecePosition - 18 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition
							       - 18 ) );
			    }
			}
			adjacentSpace = theBoard.isOccupied( piecePosition + 7 );
			aPiece = theBoard.getPieceAt( piecePosition + 7 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = theBoard.isOccupied( piecePosition + 14 );
			    // If the space is empty, there is a multiple 
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition
							       + 14 ) );
			    }
			}
                    }
		    else {
                        // Otherwise check for a regular move, not a jump.
			adjacentSpace = theBoard.isOccupied( piecePosition - 9 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition - 9 ) );
			}
			adjacentSpace = theBoard.isOccupied( piecePosition + 7 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition + 7 ) );
			}
                    }
                    
	        } // end if the piece is on the right wall.
	    }	
	    
	    // The piece is not a king.  If its color is white...
	    else if ( player.getColor() == Color.white ) {
		if ( leftWallPieces.contains( new Integer( piecePosition ) ) ) {
		    if ( isJumping ) {
			adjacentSpace = theBoard.isOccupied( piecePosition - 7 );
			aPiece = theBoard.getPieceAt( piecePosition - 7 );
			if ( adjacentSpace && ( aPiece.getColor() != 
						player.getColor() ) ) {
			    // Check space diagonal to piece to see if
			    // it is empty.
			    endSpace = theBoard.isOccupied( piecePosition - 14 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition
							       - 14 ) );
			    }
			}
		    } 
		    // Otherwise check for a regular move, not a jump.
		    else {
			adjacentSpace = theBoard.isOccupied( piecePosition - 7 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition - 7 ) );
			}
		    }
		}
		if ( rightWallPieces.contains( new Integer( piecePosition ) ) ) {
		    if ( isJumping ) {
			adjacentSpace = theBoard.isOccupied( piecePosition - 9 );
			aPiece = theBoard.getPieceAt( piecePosition - 9 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = theBoard.isOccupied( piecePosition - 18 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition
							       - 18 ) );
			    }
			}
                        // Regular move, not a jump.
		    } else {
			adjacentSpace = theBoard.isOccupied( piecePosition - 9 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition - 9 ) );
			}
		    }
		} // end if the piece is on the right wall.
	    } // end if the piece is white.
	    // The piece must be blue.
	    else { 
		if ( leftWallPieces.contains( new Integer( piecePosition ) ) ) {
		    if ( isJumping ) {
			adjacentSpace = theBoard.isOccupied( piecePosition + 9 );
			aPiece = theBoard.getPieceAt( piecePosition + 9 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to 
			    // see if it is empty.
			    endSpace = theBoard.isOccupied( piecePosition + 18 );
			    // If the space is empty, there is a
			    // multiple jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition 
							       + 18 ) );
			    }
			}
		    } 
		    // Otherwise check for a regular move, not a jump.
		    else {
			adjacentSpace = theBoard.isOccupied(  piecePosition + 9 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition + 9 ) );
			}
		    }
		}
		if ( rightWallPieces.contains( new Integer( piecePosition ) ) ) {
		    if ( isJumping ) {
			adjacentSpace = theBoard.isOccupied( piecePosition + 7 );
			aPiece = theBoard.getPieceAt( piecePosition + 7 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = theBoard.isOccupied( piecePosition + 14 );
			    // If the space is empty, there is a multiple 
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement( new Integer( piecePosition
							       + 14 ) );
			    }
			}
		    } else {
			adjacentSpace = theBoard.isOccupied( piecePosition + 7 );
			if ( !adjacentSpace ) {
			    moves.addElement( new Integer( piecePosition + 7 ) );
			}
		    }
		} // end if the piece is on the right wall.
	    }    
	    
        } catch( Exception e ) {}

        return moves;
   
    }
    
    /*
     * Will check if a single piece needs to be kinged.
     *
     * @param end - the piece position.
     * @param pieceType - the type of piece it is.
     *
     * @return true if the piece needs to be kinged.
     */
    private boolean kingCheck(int piecePosition, int pieceType) {
	    if ( pieceType == Board.SINGLE_PIECE_TYPE ) {
	    	if ( currentMove.getPlayer().getColor() == Color.white ) {
	    		if ( piecePosition == 1 || piecePosition == 3 || piecePosition == 5 || piecePosition == 7 ) {
	    			theBoard.kingPiece(piecePosition);
	    			return true;
	    		}
	    	} else {
	    		if ( piecePosition == 56 || piecePosition == 58 || piecePosition == 60 || piecePosition == 62 ) {
					theBoard.kingPiece(piecePosition);
					return true;
	    		}
	    	}
	    }
	    
	    return false;
    }    
}
