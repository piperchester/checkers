import javax.swing.JOptionPane;

/**
 * Tells the user has requested a draw. Alerts both
 * players and the kernel that one person has offered a draw and
 * class offerDraw() on both players.
 * 
 * @author piperchester
 */
public class DrawCommand implements ICommand {

	private CheckerGUI receiver;
	private Player drawingPlayer;
	private Player acceptingPlayer;
	
	/**
	 * Single Arg constructor provides the receiver for the command
	 * @param receiver the recipient for this command's actions.
	 */
	public DrawCommand(CheckerGUI receiver, Player drawingPlayer) {
		this.receiver = receiver;
		this.drawingPlayer = drawingPlayer;
	}
	
	/**
	 * Handles logic associated with the resign functionality.
	 */
	@Override
	public void execute() {
		int selected = JOptionPane.showConfirmDialog( null, drawingPlayer.getName()
		   	      + " has requested a draw."
	       		      + "\n\nWill you agree to a"
	      		      + " draw?",
	       		      "Draw offer",
	     	       	      JOptionPane.YES_NO_OPTION );
	
		if ( selected == JOptionPane.YES_OPTION ) {
		    // theDriver.endInDraw( player );
			
			JOptionPane.showMessageDialog( null,
		               "Game has ended because: " /* get string from Mediator */,
		       	       "Game Over",
			       JOptionPane.INFORMATION_MESSAGE );
			
			System.exit( 0 );
			
		} else {
		    // theDriver.declineDraw
			
	    	JOptionPane.showMessageDialog( null, /*get player name from Mediator */ 
	    			" has declined the draw offer."
	                + "\n\nClick OK to continue the game.",
	            "Draw declined",
	            JOptionPane.INFORMATION_MESSAGE );
		}
		
		
		System.exit(0);

		// Update the receiver with the new values if need be
		// receiver.setImageIndexing(interval, index);
	}
}
