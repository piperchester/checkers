/**
 * Acts as an invoker in a command pattern.  This class handles 
 * the execution of the commands  used throughout the system. 
 * 
 * @author piperchester
 */
public class Invoker {
	
	/**
	 * A no args constructor
	 */
	public Invoker() {

	}
	
	/**
	 * A method for adding commands that should be executed
	 * @param command the command to be executed
	 */
	public void addCommand(ICommand command) {
		command.execute();
	}
}
