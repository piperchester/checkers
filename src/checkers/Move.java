package checkers;
/**
 * Move.java
 *
 * Version:
 *    $Id: Move.java,v 1.1 2002/10/22 21:12:52 se362 Exp $
 *
 * Revisions:
 *    $Log: Move.java,v $
 *    Revision 1.1  2002/10/22 21:12:52  se362
 *    Initial creation of case study
 *
 */

/**
 * An object representation of a move.
 *
 * @author
 */
 public class Move {
	 
	private int startingLocation;
	private int endingLocation;
	public Player thePlayer; // player that this move is intended for

     
	/**
	 * Create a move with the starting and ending locations.
	 *	
	 * @param startLoc The starting point of the move
	 * @param endLoc   The ending point of the move
	 * 
	 * @pre startLoc and endLoc are valid locations
	 */
	public Move( Player player, int startLoc, int endLoc ) {
		thePlayer = player;
		startingLocation = startLoc;
		endingLocation = endLoc;
	}

     
	/**
	 * Return the player who made the move.
	 * 
	 * @return the player who made this move
	 * 
	 */
	public Player getPlayer() {
		return thePlayer;
	}

     
	/**
	 * Return the starting location of this move.
	 *
	 * @return The starting point of the move.
 	 */
	public int startLocation() {
		return startingLocation;
	}

     
	/**
	 * Return the ending location of this move.
	 *
	 * @return The ending point of this location.
	 */
	public int endLocation() {
		return endingLocation;
	}    
}