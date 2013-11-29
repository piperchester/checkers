import javax.swing.JOptionPane;

/**
 * Provides GUI with the necessary data to acquire the next image in the study.  If there are 
 * no more images, the command stops execution before notifying the 
 * receiver.  This class also uses the GUI's current view to determine 
 * whether to move by increments of one or four.
 * 
 * @author piperchester
 */
public class QuitCommand implements ICommand, IColleague {

	private CheckerGUI receiver;
	private String pName;
	private IMediator mediator;
	
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
		
		//mediator.Register(this);
		//this.requestPlayerName(mediator);
		
		JOptionPane.showMessageDialog( null, "Game has ended because: TEST " + pName, "Game Over", 
				JOptionPane.INFORMATION_MESSAGE );
		
		System.exit(0);

		// Update the receiver with the new values if need be
		//r receiver.setImageIndexing(interval, index);
	}

	@Override
	public void SendMessage(IMediator mediator, String message) {
		mediator.DistributeMessage(this, message);
		
	}

	@Override
	public void requestPlayerName(IMediator mediator) {
		mediator.sendPlayerName(this);
		
	}

	@Override
	public void ReceiveMessage(String message) {
		pName = message;
	}
}
