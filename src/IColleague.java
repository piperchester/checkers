
public interface IColleague {
	
    void SendMessage(IMediator mediator, String message);

    void ReceiveMessage(String message);
}
