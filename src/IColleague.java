/**
 * Main interface for the mediators colleagues
 * @author Chuck
 *
 */

public interface IColleague {
	
    void SendMessage(IMediator mediator, String message);

    void ReceiveMessage(String message);
}
