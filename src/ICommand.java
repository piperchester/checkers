
/** 
 * Governs methods implemented in all concrete command
 * classes for the command pattern.
 * 
 * @author piperchester
 */
public interface ICommand {
	/**
	 * A method to carry out the command.
	 */
	public void execute();
}