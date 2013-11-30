package checkers;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

/**
 * Tells the user has requested a draw. Alerts both
 * players and the kernel that one person has offered a draw and
 * class offerDraw() on both players.
 * 
 * @author piperchester
 */
public class SelectSpaceCommand implements ICommand {
	private CheckerGUI receiver;
	
	/**
	 * Single Arg constructor provides the receiver for the command
	 * @param receiver the recipient for this command's actions.
	 */
	public SelectSpaceCommand(CheckerGUI receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * Handles logic associated with the draw functionality. If the user chooses
	 * yes to a draw, the game ends. Otherwise it continues.
	 */
	@Override
	public void execute() {
		// When button is clicked, take info from the GUI
//    	if( startSpace == 99 ){
//    		startSpace = space;
//    	} else if( startSpace != 99 && endSpace == 99 ){
//    		if( space == startSpace ){
//    			// Viewed as un-selecting the space selected
//    			startSpace = 99; // Set startSpace to predetermined unselected value
//    		} else {
//    			endSpace = space;
//    			makeLocalMove();
//    		}
//    	}
//	
//		if ( actionListener != null ) {
//		    actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, UPDATE));
//		} 
//		
		// Update the receiver with the new values if need be
		// receiver.setImageIndexing(interval, index);
	}
}
