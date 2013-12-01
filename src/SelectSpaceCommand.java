/**
 * Selecting space. Invoked in CheckerGUI.
 * 
 * @author piperchester
 */
public class SelectSpaceCommand implements ICommand, IColleague {
	
	private GameplayMediator mediator;
	private CheckerGUI receiver;
	private int startSpace;
	private int endSpace;
	private int space;
	private Player passivePlayer;
	private Player activePlayer;

	/**
	 * Single Arg constructor provides the receiver for the command
	 * @param receiver the recipient for this command's actions.
	 */
	public SelectSpaceCommand(CheckerGUI receiver, GameplayMediator mediator, int space) {
		this.receiver = receiver;
		this.mediator = mediator;
		this.space = space;
		mediator.Register(this);
		
		startSpace = 99;
		endSpace = 99;
	}
	
	/**
	 * Handles logic associated with the draw functionality. If the user chooses
	 * yes to a draw, the game ends. Otherwise it continues.
	 */
	@Override
	public void execute() {
		// When button is clicked, take info from the GUI
		
		// TODO: Not sure how if we can keep startSpace and endSpace, 
		// originally private var from Facade.
    	if( startSpace == 99 ){
    		startSpace = space;
    	} else if( startSpace != 99 && endSpace == 99 ){
    		if( space == startSpace ){
    			// Viewed as un-selecting the space selected
    			startSpace = 99; // Set startSpace to predetermined unselected value
    		} else {
    			endSpace = space;
    			
    			// TODO: Not sure what to do with Mediator here 
				// make sure startSpace and endSpace are defined
		    	this.requestActive(mediator);
		    	this.requestPassive(mediator);
    			
    			// TODO: Get activePlayer from Mediator?
				if( startSpace != 99 && endSpace!= 99 ){
				    // Takes the information of a move and calls makeMove() in a localplayer
				   // boolean result = activePlayer.makeMove( startSpace, endSpace );
				}
				
				// Reset startSpace and endSpace to 99
				startSpace = 99;
				endSpace   = 99;
    		}
    	}

    	// TODO: Not sure what to do about this. Maybe get it from Mediator?
//		if ( actionListener != null ) {
//		    actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, UPDATE));
//		} 

		// Update the receiver with the new values if need be
		// receiver.setImageIndexing(interval, index);
	}

	@Override
	public void SendMessage(IMediator mediator, String message) {
		//Intentionally left blank
		
	}

	@Override
	public void requestPlayerName(IMediator mediator) {
		//Intentionally left blank
		
	}

	@Override
	public void ReceiveMessage(String message) {
		//Intentionally left blank
		
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
