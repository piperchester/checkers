import java.util.ArrayList;

/**
 * Main interface for communicating with all 
 * colleague objects
 * 
 * @author Chuck
 *
 */

public interface IMediator {

    void DistributeMessage(IColleague sender, String message);

    void Register(IColleague colleague);
	
}
