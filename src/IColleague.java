
/**
 * Main interface for the mediators colleagues
 * @author Chuck
 *
 */

public interface IColleague {
	
    void SendMessage(IMediator mediator, String message);
    
    void requestPlayerName(IMediator mediator);

    void ReceiveMessage(String message);
    
    void RecieveActivePlayer(Player p);
    
    void RecievePassivePlayer(Player p);
    
    void requestPassive(IMediator mediator);
    
    void requestActive(IMediator mediator);
    
}
