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
	
	/**
	 * Single Arg constructor provides the receiver for the command
	 * @param receiver the recipient for this command's actions.
	 */
	public DrawCommand(CheckerGUI receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * Handles logic associated with the draw functionality. If the user chooses
	 * yes to a draw, the game ends. Otherwise it continues.
	 */
	@Override
	public void execute() {
		int selected = JOptionPane.showConfirmDialog( null, /* get player name from Mediator */
		   	      " has requested a draw."
	       		      + "\n\nWill you agree to a draw?",
	       		      "Draw Offer",
	     	       	      JOptionPane.YES_NO_OPTION );
	
		if ( selected == JOptionPane.YES_OPTION ) {
			JOptionPane.showMessageDialog( null, "Both players have agreed to a draw. Game over!", 
					"Game Over", JOptionPane.INFORMATION_MESSAGE );
			System.exit( 0 );
		} else {
	    	JOptionPane.showMessageDialog( null, /*get player name from Mediator */ 
	    			" has declined the draw offer."
	                + "\n\nClick OK to continue the game.",
	            "Draw declined.",
	            JOptionPane.INFORMATION_MESSAGE );
		}
		
		// Update the receiver with the new values if need be
		// receiver.setImageIndexing(interval, index);
	}
}
