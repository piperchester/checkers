

/**
 *  Timer runs on the separate thread, during simulation tells
 *  facade to update the warning and normal times 
 *
 *  @invariant all variables have valid values
 *
 *  @author
 */

public class Timer extends Thread {

    private static int INTERVAL = 100;    
    private int      interval;
    private Notifier notifier;
    
    /**
     * Creates a new timer.
     */    
    public Timer(){
		notifier = new Notifier( notifier.TIME_UPDATE );
		interval = INTERVAL;
    }
    
    /**
     * Creates a timer with an interval.
     *
     * @param inter - the new interval
     */
    public Timer( int inter ){
		notifier = new Notifier( notifier.TIME_UPDATE );
		interval = inter;
    }
    
    /**
     * Executes when thread has been started.
     * 
     */
    public void run() {
		// Start the timer thread, notify the facade every interval
		while ( true ) {
		    try {
		    	sleep( interval );
		    }
		    catch ( InterruptedException e ) {
		    	System.err.println( "The timer malfunctioned." );
		    }
		    notifier.generateActionPerformed();
		}
    }
    
    /**
     * Get the notifier.
     * 
     * @return Notifier
     * @roseuid 3C5AE4FD00C1
     */
    public Notifier getNotifier(){
    	return notifier;
    }   
}
