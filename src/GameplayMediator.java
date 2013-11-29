import java.util.ArrayList;

/**
 * The concrete mediator, contains all of the functionality needed
 * @author Chuck
 *
 */

public class GameplayMediator implements IMediator {

	private ArrayList<IColleague> colleagueList = new ArrayList<IColleague>();	
	
	private Player activePlayer;
	
	private Player passivePlayer;
	
	@Override
	public void DistributeMessage(IColleague sender, String message) {
		int i = 0;
		System.out.println(colleagueList.toString());
		while(i <= colleagueList.size() - 1){
			System.out.println(colleagueList.get(i).toString());
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
	
	@Override
	public void sendPlayerName(IColleague sender){
		sender.ReceiveMessage(activePlayer.getName());
	}

	public Player getActivePlayer() {
		return activePlayer;
	}
	
	@Override
	public void setActivePlayer(Player activePlayer) {
		this.activePlayer = activePlayer;
	}

	public Player getPassivePlayer() {
		return passivePlayer;
	}

	@Override
	public void setPassivePlayer(Player passivePlayer) {
		this.passivePlayer = passivePlayer;
	}
	

}