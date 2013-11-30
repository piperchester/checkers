
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
import javax.swing.*;

/**
 * Contains the main method to start the game. 
 * Creates all necessary classes as information is provided. Its 
 * functions include knowing whose turn it is, remembering multiple 
 * jumps, relaying end of game conditions and ending the game.
 */
public class Driver implements IColleague{
    
    private Player  playerOne;
    private Player  playerTwo;
    private int     gameType;
    private Player  activePlayer;
    private Player  passivePlayer;
    private boolean isTimerRunning;
    private Timer   theTimer;
    private Facade  theFacade;
    private Rules   theRules;
    private IMediator mediator;
    
    /**
     * Constructor
     *
     * Create the driver, which in turn creates the rest of 
     * the system.
     */
    public Driver(IMediator mediator){    
    	this.mediator = mediator;
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
    
    
    public void setMediator(IMediator m){
    	mediator = m;
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
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
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
				
			    // Inform the other player to make a move and tell facade to 
				// update any listening GUIs and reset the timer
			    
				mediator.switchPlayers();
				this.requestActive(mediator);
				this.requestPassive(mediator);
				
			    theFacade.setPlayerModes();
		    }
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
    public void createPlayer( int num, String name ){
		Player temp = null;

		temp = new Player( num, theRules, this );
		temp.setName( name );
		
		
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
		    isTimerRunning = false;
		} else {
		    isTimerRunning = true;
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

		// Tell player with the correct color to make a move
		if ( playerOne.getColor() == Color.white ) {
		    mediator.setActivePlayer(playerOne);
		    mediator.setPassivePlayer(playerTwo);
		} else {
		    mediator.setActivePlayer(playerTwo);
		    mediator.setPassivePlayer(playerOne);
		}
		
		theFacade.setPlayerModes();
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
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
    	return passivePlayer;    	// player whos getTurnStatus is false
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
     * Return the notifier of the Timer by asking the timer for its notifier.
     *
     * @return the notifier for the Timer
     *
     * @pre  The game is running
     * @post This method has changed nothing
     */
    public Notifier getTimerNotifier(){
		Notifier timer = null;
		
		if ( theTimer != null ) {
		    timer = theTimer.getNotifier();
		}
		
		return timer;
    }   
    
    public IMediator getMediator(){
    	return mediator;
    }

	@Override
	public void SendMessage(IMediator mediator, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestPlayerName(IMediator mediator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ReceiveMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RecieveActivePlayer(Player p) {
		activePlayer = p;
		
	}

	@Override
	public void RecievePassivePlayer(Player p) {
		passivePlayer = p;
		
	}

	@Override
	public void requestPassive(IMediator mediator) {
		mediator.getPassivePlayer(this);
		
	}

	@Override
	public void requestActive(IMediator mediator) {
		mediator.getActivePlayer(this);
		
	}
}