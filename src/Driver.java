/**
 * Driver.java
 *
 * Version
 *    $Id: Driver.java,v 1.1 2002/10/22 21:12:52 se362 Exp $ 
 *
 * Revisions:
 *    $Log: Driver.java,v $
 *    Revision 1.1  2002/10/22 21:12:52  se362
 *    Initial creation of case study
 *
 */

import java.awt.*;
import java.net.*;
import javax.swing.*;

/**
 *
 * This class contains the main method to start the game, it 
 * creates all necessary classes as information is provided. Its 
 * functions include knowing whose turn it is, remembering multiple 
 * jumps, relaying end of game conditions and ending the game.
 *
 * @author
 *
 */

public class Driver {
    
    private Player  playerOne;
    private Player  playerTwo;
    private int     gameType;
    private Player  activePlayer;
    private Player  passivePlayer;
    private boolean runningTimer;
    private Timer   theTimer;
    private Facade  theFacade;
    private Rules   theRules;
    
    /**
     * Constructor
     *
     * Create the driver, which in turn creates the rest of 
     * the system.
     */
    public Driver(){    
		Board theBoard = new Board();
		theRules = new Rules( theBoard, this ); // Create the rules passing in the board
		theFacade = new Facade( theBoard, this ); // Create the facade and GUI
    }
    
    /**
     * Return the facade the GUI will talk to.
     *
     * @return A facade to talk to the GUI.
     */
    public Facade getFacade(){
    	return theFacade;
    }
    
    /**
     * Called after a move has been checked. 
     * Changes active player when a final successful jump has 
     * been made, resets the timer when appropriate, and tells 
     * the appropriate player whose turn it is to make a move.
     *
     * @param player The player whose turn it will now be
     * @param space  The space on the board from which a multiple 
     *               jump has to be made
     *
     * @pre  a players has made a move
     * @post a player has been told to make a move
     */
    public void endTurn( Player player, int space ){
	
		// Check to see if player passed in was the active player
		// If player passed in was active player, check for multiple
		// jump (space is none negative)
		if ( activePlayer == player ){
		    
		    // Inform the player that the move was not valid, or to make another jump
		    if ( space < 0 ){
			JOptionPane.showMessageDialog( null,
		       	       activePlayer.getName() + " made an illegal move",
	      	       	       "Invalid Move", JOptionPane.INFORMATION_MESSAGE );
		    } else {
			JOptionPane.showMessageDialog( null,
			       activePlayer.getName() + " please make" +
	       		       " another jump", "Multiple Jump Possible",
			       JOptionPane.INFORMATION_MESSAGE );
			
			// Get the GUI to update
			theFacade.setPlayerModes( activePlayer, passivePlayer );
			
			// If game is networked tell networked player to send the move
			if ( gameType == theFacade.HOSTGAME || gameType == theFacade.CLIENTGAME ) {
			    ( (NetworkPlayer) activePlayer ).sendMove();
				}
		    }
		} else if ( passivePlayer == player ) {
		    // If game is networked, tell networked player to send move
		    if ( gameType == theFacade.HOSTGAME || gameType == theFacade.CLIENTGAME ) {
				((NetworkPlayer)activePlayer).sendMove();
				((NetworkPlayer)activePlayer).waitForPlayer();
		    }
		    
		    // Inform the other player to make a move and
		    // tell facade to update any listening GUIs and reset the timer
		    
		    Player tempHold = activePlayer;
		    activePlayer    = passivePlayer;
		    passivePlayer   = tempHold;
		    
		    theFacade.setPlayerModes( activePlayer, passivePlayer );
		}
    }
    
    /**
     * Ends the game due to a draw, someone quitting, or a victory.
     *
     * @param message  the message to send to all players regarding the 
     *                 reason for ending the game
     *
     * @pre  the criteria for ending a game has been met, depending on why 
     *       the game ended
     * @post the game has been ended for both players and the game is ready 
     *       to exit
     */
    public void endGame( String message ){
		// Call endOfGame on both players with the given message
		playerOne.endOfGame( message );
		playerTwo.endOfGame( message );
		
		// When players have acknowledged the end of game 
		System.exit( 0 );
    }
    
    /**
     * Creates the correct players for a game.
     *
     * @param type the type of player to be created (0 - local, 1 - network)
     * @param name the name of the player
     * @param num  the player's number
     *
     * @pre   less than 2 players exist
     * @post  a player with correct name has been created  
     */
    public void createPlayer( int num, int type, String name ){
		Player temp = null;
	
		if ( type == Player.LOCALPLAYER ) {
		    temp = new LocalPlayer( num, theRules, this );
		    temp.setName( name );
		} else if ( type == Player.NETWORKPLAYER ) {
		    temp = new NetworkPlayer( num, theRules, this );
		    temp.setName( name );
		}
		
		// TODO: Ternary
		if ( num == 1 ) {
		    playerOne = temp;
		} else {
		    playerTwo = temp;
		}
    }
    
    /**
     * Set player name.
     * 
     * @param num  The player's number (1 or 2)
     * @param name The name to assign to the player.
     */
    public void setPlayerName( int num, String name ){
		if ( num == 1 ) {
		    playerOne.setName( name );
		} else {
		    playerTwo.setName( name );
		}
    }
    
    /** 
     * Set player color.
     *
     * @param num   The player's number (1 or 2)
     * @param color The color to assign to the player.
     */
    public void setPlayerColor( int num, Color color ) {
        if ( num == 1 ) {
            playerOne.setColor( color );
        } else {
            playerTwo.setColor( color );
        }
    }
    
    /**
     * Ends game in a draw, alerts both players that the draw has taken place
     *
     * @pre  both players have agreed to a draw
     * @post the game has ended and both players have been notified 
     *       of the draw
     */
    public void endInDraw( Player player ){
	   	// Calls endOfGame with a message that game ended in a draw.
		endGame( player.getName() + "'s draw offer was accepted. \n\n"
			 + "Game ended in a draw." );
    }

    /**
     * Called if a draw has been offered.
     * 
     * @param the player who offered the draw
     * 
     */    
    public void drawOffered( Player player ){
		if( player.getNumber() == playerOne.getNumber() ){
		    playerTwo.acceptDraw( player );
		}else if( player.getNumber() == playerTwo.getNumber() ){
		    playerOne.acceptDraw( player );
		}
    }
    
    /** 
     * The offer for a draw has been made.  This method declines
     * that offer, continuing the game.
     *
     * @param player The player declining the draw.
     */
    public void declineDraw( Player player ){
		if ( gameType == theFacade.LOCALGAME ) {
		    player.endInDeclineDraw( player );
		} else {
		    playerOne.endInDeclineDraw( player );
		    playerTwo.endInDeclineDraw( player );
		}
    }
    
    /**
     * Ends the game as a result of a player quitting, notifies each player.
     * 
     * @param the player who quit
     */
    public void endInQuit( Player player ){
	playerOne.endOfGame( player.getName() + " quit the game" );
	playerTwo.endOfGame( player.getName() + " quit the game" );
    }
    
    /**
     * This method creates the timer to be used, if one is desired 
     * to be used. It will also set the number of seconds for each 
     * turn.
     *
     * @param   time    : the number of seconds for each turn
     * @param   warning : whether or not a player will be warned 
     *                    that their turn is going to end
     *
     * @pre  It has been selected to use a timer in the game setup
     * @post The timer has been created and the appropriate time 
     *       restraints are in place
     */
    public void setTimer( int time, int warning ){
   	// If values are negative, set runningTimer to false
	// If they are positive, create Timer and notifier with the times
		if ( time < 0 ) {
		    runningTimer = false;
		} else {
		    runningTimer = true;
		    theTimer = new Timer();
		}
    }
        
    /**
     * Sets piece colors for each player.
     *
     * @pre the game has been started, and there are 2 players
     * @post each player has their colors
     */
    private void selectColors(){
	   	// Randomly select color for each player and call the 
		// setColor() method of each
		if ( Math.random() > .5 ) {
		    playerOne.setColor( Color.blue );
		    playerTwo.setColor( Color.white );
		} else {
		    playerOne.setColor( Color.white );
		    playerTwo.setColor( Color.blue );
		}
    }
    
    /**
     * Starts the game play. Lets first person move and so on.
     *
     * @pre  There are 2 players to play, all pregame conditions are in place 
     * @post The first person is able to make their first move
     */
    public void startGame(){
		selectColors();
	       
		if ( gameType == theFacade.HOSTGAME ) {
		    ( (NetworkPlayer)playerTwo).waitForConnect();
		    //( (NetworkPlayer)playerTwo).waitForConnect();
		} else if ( gameType == theFacade.CLIENTGAME ) {
		    //( (NetworkPlayer)playerOne).connectToHost();
		    ( (NetworkPlayer)playerOne).connectToHost();
		}
		
		// Tell player with the correct color to make a move
		if ( playerOne.getColor() == Color.white ) {
		    activePlayer  = playerOne;
		    passivePlayer = playerTwo;
		} else {
		    activePlayer  = playerTwo;
		    passivePlayer = playerOne;
		}
		
		theFacade.setPlayerModes( activePlayer, passivePlayer );
    }
    
    /**
     * Sets the host player will play against in case of 
     * a game over a network.
     *
     * @param  host the host of the game to be played
     *
     * @pre  There is a person to host the game, both players are 
     *       networkedPlayers
     * 
     * @post The players are connected to play
     */
    public void setHost( URL host ){
	   	// Call connectToHost in player two with the URL
		((NetworkPlayer)playerOne).setHost( host );
		((NetworkPlayer)playerTwo).setHost( host );
    }
    
    /**
     * Return the player whose turn it is not.
     *
     * @return the player whose turn it is not
     *
     * @pre there are 2 valid players and the game has started 
     * @post this method has not altered anything
     */
    public Player getOppositePlayer(){
    	return passivePlayer;    	// player whos getTurnStatus is false
    }
    
    /**
     * Whether the current game uses a timer
     *
     * @return true if a timer is being sed in the game, otherwise 
     *         false
     *
     * @pre the game has started 
     * @post this method has not altered anything
     */
    public boolean timerRunning(){
    	return runningTimer;
    }
    
   
    /**
     * Select the game type.
     *
     * @param mode the mode (0 local, 1 host, 2 client) of the game
     *
     * @pre  Players have not been created
     * @post Mode is set
     */
    public void setGameMode( int newMode ){
		// Set the value of mode
		gameType = newMode;
    }
    
    /**
     * Return the integer representing the type of game.
     *
     * @return the type of game
     *
     * @pre  Game has started
     * @post This method has changed nothing
     */
    public int getGameMode(){
    	return gameType;
    }
    
    /**
     * Return the notifier of the Timer
     *
     * @return the notifier for the Timer
     *
     * @pre  The game is running
     * @post This method has changed nothing
     */
    public Notifier getTimerNotifier(){
	   	// Return the timers notifier, by asking the timer for its notifier
		Notifier timer = null;
		
		if ( theTimer != null ) {
		    timer = theTimer.getNotifier();
		}
		
		return timer;
    }   
}