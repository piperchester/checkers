import java.util.ArrayList;



public class GameplayMediator implements IMediator {

	private ArrayList<IColleague> colleagueList = new ArrayList<IColleague>();	
	
	@Override
	public void DistributeMessage(IColleague sender, String message) {
		int i = 0;
		System.out.println("recieved");
		while(i <= colleagueList.size()){
			if(colleagueList.get(i) != sender){
				colleagueList.get(i).ReceiveMessage(message);
			}
		}
		
	}

	@Override
	public void Register(IColleague colleague) {
		colleagueList.add(colleague);
		
	}

}
