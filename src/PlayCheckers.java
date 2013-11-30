/**
 * PlayCheckers.java 
 *
 * Version:
 *   $Id: PlayCheckers.java,v 1.1 2002/10/22 21:12:53 se362 Exp $
 *   
 * Revisions:
 *   $Log: PlayCheckers.java,v $
 *   Revision 1.1  2002/10/22 21:12:53  se362
 *   Initial creation of case study
 */

/**
 *
 * The main class to run the game. It creates driver and first screen.
 *
 * @author
 *
 */
class PlayCheckers{
	
	/**
	* The main method to play checkers.
	* 
	*/
    public static void main( String args[] ){
    	IMediator mediator = new GameplayMediator();
		Driver theDriver = new Driver(mediator);
		InitialScreen second = new InitialScreen(theDriver,  theDriver.getMediator());
		second.show(); // Piper - Deprecated.
    }   
}