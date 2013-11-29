import javax.swing.JOptionPane;

/**
 * Provides GUI with the necessary data to acquire the next image in the study.  If there are 
 * no more images, the command stops execution before notifying the 
 * receiver.  This class also uses the GUI's current view to determine 
 * whether to move by increments of one or four.
 * 
 * @author piperchester
 */
public class QuitCommand implements ICommand {

	private CheckerGUI receiver;
	
	/**
	 * Single Arg constructor provides the receiver for the command
	 * @param receiver the recipient for this command's actions.
	 */
	public QuitCommand(CheckerGUI receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * Handles logic associated with the resign functionality.
	 */
	@Override
	public void execute() {
		
		JOptionPane.showMessageDialog( null, "Game has ended because: TEST " /* player name */, "Game Over", 
				JOptionPane.INFORMATION_MESSAGE );
		
		System.exit(0);

		// Update the receiver with the new values if need be
		// receiver.setImageIndexing(interval, index);
	}
}
