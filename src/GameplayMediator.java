import java.util.ArrayList;

/**
 * The concrete mediator, contains all of the functionality needed
 * @author Chuck
 *
 */

public class GameplayMediator implements IMediator {

	private ArrayList<IColleague> colleagueList = new ArrayList<IColleague>();	
	
	@Override
	public void DistributeMessage(IColleague sender, String message) {
		int i = 0;
		while(i <= colleagueList.size() - 1){
			if(colleagueList.get(i) != sender){
				colleagueList.get(i).ReceiveMessage(message);
			}
			i++;
		}
		
	}

	@Override
	public void Register(IColleague colleague) {
		colleagueList.add(colleague);	
	}

}
