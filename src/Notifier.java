import java.awt.*;
import java.awt.event.*;

/**
 * Creates Notifier. Adds actionListener.
 */
public class Notifier extends Component {
 
	public final static String TIME_UPDATE = "time update";
    String ID;                       // The ID of this object
    ActionListener actionListener;   // registered actionlistener objects
   
    /**
     * Create a new instance of this class to notify other object
     *
     * @param ID the id of this object
     */
    public Notifier( String ID ) {
		this.ID = ID;
    }
   
    /**
     * Other objects register to be notified by this notifier. Generate
     * action listener for this event.
     *
     * @param listener the listener to be added
     */
    public void addActionListener( ActionListener listener ) {
        actionListener = AWTEventMulticaster.add( actionListener, listener );
    }

    /**
     * Timer calls this method to fire a Timer event. Generate
     * action based on the notifier.
     */
    public void generateActionPerformed() {
        actionListener.actionPerformed(new ActionEvent (this, ActionEvent.ACTION_PERFORMED, ID));
    }
}
