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
	
	@Override
	public void sendPlayerName(IColleague sender){
		sender.ReceiveMessage(activePlayer.getName());
	}

	
	@Override
	public void setActivePlayer(Player activePlayer) {
		this.activePlayer = activePlayer;
	}


	@Override
	public void setPassivePlayer(Player passivePlayer) {
		this.passivePlayer = passivePlayer;
	}

	@Override
	public void getPassivePlayer(IColleague sender) {
		sender.RecievePassivePlayer(passivePlayer);
		
	}

	@Override
	public void getActivePlayer(IColleague sender) {
		sender.RecieveActivePlayer(activePlayer);
	}

	@Override
	public void switchPlayers() {
		Player temp = null;
		temp = activePlayer;
		activePlayer = passivePlayer;
		passivePlayer = temp;
		
	}
	

}