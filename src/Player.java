

import java.awt.*;

import javax.swing.JOptionPane;
		   
/**
*  A class representation of the Player object.  This object
*  contains the methods needed when one of the users clicks
*  on one of the buttons within the GUI.
*
*  @author
*/

public class Player {
    
    // Instance of the rules class which will be used to
    // validate moves and check for game ending conditions
    // once a move has been made.
    protected Rules  theRules;	

    protected Driver theDriver;
    
    // Instance of the move class which will be
    // created when a user makes a move.
    protected Move   theMove;	
    
    protected int    playerNumber;
    protected String playerName;
    protected Color  playerColor;
    protected GameplayMediator mediator;
    protected int    type;
    
    /**
     * Create a new instance of a Player object to represent
     * one of the users.
     * 
     * @param num       The number of the player.
     * @param newRules  The rules used to validate moves.
     * @param newDriver Driver which will control this.     
     */
    public Player( int num, Rules newRules, Driver newDriver ){
	playerName   = null;
	playerColor  = null;
	playerNumber = num;
	theRules     = newRules;
	theDriver    = newDriver;
    }
    
    /**
     * Return the type of player.
     *
     * @return The type of player.
     */
    public int getType() {
	return type;
    }
    
    /**
     * Make an istance of a Move that was just made and pass it 
     * to theRules by calling its validateMove method.
     *
     * @param start The starting spot of the move.  The legal 
     *              squares on the checkers boardare numbered 
     *              from 1 to 32, left to right, top to bottom.
     * @param end   The ending spot of the move.  
     *
     * @return true If move was made, false otherwise
     */
    public boolean makeMove( int start, int end ){
	boolean retval = false;
	
	theMove = new Move(this, start, end );
	retval  = theRules.validateMove( theMove );
      
	return retval;
    }
    
    /**
     * This method is used for when a user has clicked on the 
     * "Quit" button on the GUI.  It handles exiting  the game.
     * 
     * @param the player who quit
     * @pre  game is in progress
     * @post message is sent to driver to end the game
     */
    public void endOfGame( String message ){
		JOptionPane.showMessageDialog( null,
	               "Game has ended because: "
	       	       + message,
	       	       "Game Over",
		       JOptionPane.INFORMATION_MESSAGE );
		
		System.exit( 0 );
    }
  
    /**
     * Returns the number for this player
     * 
     * @pre the player has a number
     * 
     * @return playerNumber
     */
    public int getNumber(){
	return playerNumber;
    }
    
    /**
     * Returns the players name
     * 
     * @pre the player has a name
     * 
     * @return the players name
     */
    public String getName(){
	return playerName;
    }
    
    /**
     * Sets the players name
     * 
     * @param the name to be set
     */
    public void setName( String name ){
	playerName = name;
    }
    
    /**
     * Return the color of this player
     * 
     * @return the color of this player
     */
    public Color getColor() {
	return playerColor;
    }
    
    /**
     * Set the color for this player.
     * 
     * @param newColor The new color for this player.
     */
    public void setColor( Color newColor ){
	playerColor = newColor;
    }
    
    /**
     * A string representation of this object.
     * 
     * @return a String representation of this object.
     */
    public String toString(){
        return ("Player.  name = " + playerName);
    }

    
}//Player.java
