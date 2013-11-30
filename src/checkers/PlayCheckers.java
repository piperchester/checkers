package checkers;
/**
 * The main class to run the game. Creates driver and first screen.
 *
 */
class PlayCheckers{
	
	/**
	* The main method to play checkers. Instantiate
	* IMediator, Driver, and InitialScreen.
	*/
	public static void main( String args[] ){
    	IMediator mediator = new GameplayMediator();
		Driver theDriver = new Driver(mediator);
		InitialScreen second = new InitialScreen(theDriver,  theDriver.getMediator());
		second.setVisible(true);
    }   
}