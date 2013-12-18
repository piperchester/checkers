/**
 *  Timer runs on the separate thread, during simulation tells
 *  facade to update the warning and normal times 
 *
 *  @invariant all variables have valid values
 *
 *  @author
 */
public class Timer extends Thread {

    private static int SET_INTERVAL = 100;    
    private int      interval;
    private Notifier notifier;
    
    /**
     * Creates a new timer.
     */    
    public Timer(){
		notifier = new Notifier(Notifier.TIME_UPDATE);
		interval = SET_INTERVAL;
    }
    
    /**
     * Creates a timer with an interval.
     *
     * @param inter - the new interval
     */
    public Timer(int interval){
		notifier = new Notifier(Notifier.TIME_UPDATE);
		this.interval = interval;
    }
    
    /**
     * Executes when thread has been started.
     * 
     */
    public void run() {
		// Start the timer thread, notify the facade every interval
		while (true) {
		    try {
		    	sleep(interval);
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
     */
    public Notifier getNotifier(){
    	return notifier;
    }   
}
