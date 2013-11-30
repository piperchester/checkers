

import java.awt.*;
import java.awt.event.*;

/**
 * An interface between the GUI and the kernel classes in a checkers game.
 * 
 * @author
 */

public class Facade extends Component implements IColleague{

    public static int LOCALGAME  = 10000;
    public static int HOSTGAME   = 20000;
    public static int CLIENTGAME = 30000;

    public static String UPDATE       = "update";
    public static String playerSwitch = "switch";
    public static String ID           = "facade";
    
    public Driver      theDriver;
    public Board       theBoard;
    public Player      passivePlayer;
    public Player      activePlayer;		
    
    private int startSpace = 99; // Starting space for current move
    private int endSpace   = 99; // Ending space for current move
    
    // The numbers associated with the timer
    private int timer       = 999;
    private int warningTime = 999;
    private IMediator mediator;
    private ActionListener actionListener;
      
    /**
     * Constructor for the facade.  Initializes the data members.
     * 
     * @param newBoard  Board  object Facade will manipulate.
     * @param newDriver Driver object that will communicate 
     *                  with the Facade.
     */
    public Facade( Board newBoard, Driver newDriver ){
    	theBoard = newBoard;
    	theDriver = newDriver;
    	mediator = theDriver.getMediator(); 
    	mediator.Register(this);
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
    }
    
    /**
     * Return int indicating which player's turn it is.
     * ( e.g. 1 for player 1 )
     */
    public int whosTurn(){
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
    	return activePlayer.getNumber();
    }
    
    /**
     * Set which player's turn it is.
     * 
     * @param active  The active player
     * @param passive The passive player
     */
    public void setPlayerModes( Player active, Player passive ){
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
    	activePlayer = active;
    	passivePlayer = passive;
	
		if ( actionListener != null ) {
		    actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, UPDATE));
		}
    }
    
    /**
     *
     * This method should be called to select a space on the board, 
     * either as the starting point or the ending point for a move.  
     * The Facade will interpret this selection and send a move on to 
     * the kernel when two spaces have been selected.
     *
     * @param space an int indicating which space to move to, 
     *              according to the standard checkers numbering 
     *              scheme, left to right and top to bottom.
     */
    public void selectSpace( int space ){  
	
	// When button is clicked, take info from the GUI
    	if( startSpace == 99 ){
    		startSpace = space;
    	} else if( startSpace != 99 && endSpace == 99 ){
    		if( space == startSpace ){
    			// Viewed as un-selecting the space selected
    			startSpace = 99; // Set startSpace to predetermined unselected value
    		} else {
    			endSpace = space;
    			makeLocalMove();
    		}
    	}
	
		if ( actionListener != null ) {
		    actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, UPDATE));
		}  
    }
    
    /**
     * Send a move on to the kernel, i.e. call makeMove() in 
     * the LocalPlayer and inform it whose turn it is. startSpace
     * and endSpace must be defined.
     */
    private void makeLocalMove(){
		//make sure startSpace and endSpace are defined
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
		if( startSpace != 99 && endSpace!= 99 ){
		    // Takes the information of a move and calls makeMove() in a localplayer
		    boolean result = activePlayer.makeMove( startSpace, endSpace );
		}
		
		// Reset startSpace and endSpace to 99
		startSpace = 99;
		endSpace   = 99;
    }
    
    
    /**
     * Given a player number, returns the name associated 
     * with that number. 
     * 
     * @param  playerNum the number of a player
     * @return string    the name associated with playerNum
     */
    public String getPlayerName( int playerNum ){
    	this.requestActive(mediator);
    	this.requestPassive(mediator);
		try{
		    // Checks to see that playerNum is valid
		    if( playerNum == 1 || playerNum == 2 ){
		    	return activePlayer.getNumber() == playerNum ? activePlayer.getName() : passivePlayer.getName();
		    }		   
		}catch( Exception e ){  // Throws exception on illegal player name
		    System.out.println( e.getMessage() );
		}
		
		return null;
    }
    
    /**
     * Tell kernel to associate given name with acceptable given player number.
     *
     * @param playerNum the number of a player
     * @param name      the name that player should be given
     *
     */
    public void setPlayerName( int playerNum, String name ){
    	theDriver.setPlayerName( playerNum, name );
    }
    
    
    /**
     * Tell kernel to set a time limit for each turn.  The time 
     * limit, i.e. the amount of time a player has during his turn 
     * before he is given a time warning, is specified by the parameter 
     * called time, in minutes.
     * 
     * @param time the time limit for each turn, in seconds.
     *
     * @pre   10 <= time <= 300.
     */
    public void setTimer( int time, int warning ) throws Exception{
		// Checks to see that time is in between the necessary frame
		// Sets time(class variable) to time(param value)
		if( ( time == -1 ) || ( ( time >= 10 || time <= 300 ) 
					&& ( warning >= 10 || warning <= 300 ) ) ){
		    
		    timer       = time;
		    warningTime = warning;
		    theDriver.setTimer( time, warning );
		
		} else {
		    throw new Exception( "Invalid timer settings" );
		}	   
    }
    
    /**
     * Display to local players that game has ended with 
     * the message provided.
     * 
     * @param message
     */
    public void showEndGame( String message ){
    	theDriver.endGame( message );
    }
    
    /**
     * Set the game mode: a local game or a network game by calling
     * the Driver's setGameMode. 
     * 
     * @param the mode of the game
     * 
     */
    public void setGameMode( int mode ) throws Exception{
       	if( mode == LOCALGAME || mode == HOSTGAME || mode == CLIENTGAME ){
       		theDriver.setGameMode( mode );
       	} else {
       		throw new Exception( "Invalid Game Mode" );
       	}
    }
    
    /**
     * Returns timer value: how long each player gets to take a turn.
     * 
     * @return the amount of time each player has for a turn 
     * 
     */
    public int getTimer(){
    	return timer != 999 ? timer : 0;
    }
    
    /**
     * Returns the amount of time chosen for a warning that a player is 
     * near the end of his/her turn.
     * 
     * @return the amount of warning time a player has
     * 
     * @pre there has been a timer set for the current game  
     */
    public int getTimerWarning(){
    	return warningTime != 999 ? warningTime : -1;
    }
   
    /**
     * Adds an action listener to the facade
     */
    public void addActionListener( ActionListener a ){
    	actionListener = AWTEventMulticaster.add( actionListener, a );
    }
    
    /**
     * Returns the board so GUI can go through and update itself.
     * 
     * @return a Board object which is the state of the board
     * 
     */
    public Board stateOfBoard(){
    	return theBoard;
    }
    
    /**
     * Call the driver and begin the game.
     */
    public void startGame(){
    	theDriver.startGame();
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