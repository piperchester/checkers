/*
 * Firstscreen.java
 *
 *  * Version:
 *   $Id: Firstscreen.java,v 1.1 2002/10/22 21:12:52 se362 Exp $
 *
 * Revisions:
 *   $Log: Firstscreen.java,v $
 *   Revision 1.1  2002/10/22 21:12:52  se362
 *   Initial creation of case study
 *
 */
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Firstscreen extends JFrame implements ActionListener{

    Facade theFacade;
    Secondscreen next;
  
    // Variables declaration - do not modify
    private JRadioButton LocalGameButton;
    private JButton OKButton;
    private JButton CancelButton;
    private ButtonGroup gameModes;
    private IMediator mediator; 

    /** 
     * Creates new form Firstscreen
     *
     * @param facade a facade object for the GUI to interact with
     *     
     */
    public Firstscreen( Facade facade, IMediator mediator ) {
		super( "First screen" );
	        theFacade = facade;
	        this.mediator = mediator;
	        initComponents();
	        pack();
    }
    

    /** 
     * Initializes form from within constructor.
     */
    private void initComponents() {

        LocalGameButton = new JRadioButton();
        gameModes = new ButtonGroup();
        OKButton = new JButton();
        CancelButton = new JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        }
        );
        
		gameModes.add(LocalGameButton);
			
		LocalGameButton.setActionCommand("local");
	    LocalGameButton.setText("Local game");
	    LocalGameButton.addActionListener(this);
	    LocalGameButton.setSelected( true );
	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = 0;
	    getContentPane().add(LocalGameButton, gridBagConstraints1);
	    
	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = 1;
	    
	    gridBagConstraints1 = new java.awt.GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = 2;
	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 2;
	    gridBagConstraints1.gridy = 3;
	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = 3;
	    
	    OKButton.setText("OK");
	    OKButton.setActionCommand("ok");
	    OKButton.setName("button6");
	    OKButton.setBackground(new Color (212, 208, 200));
	    OKButton.setForeground(Color.black);
	    OKButton.addActionListener(this);
	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 2;
	    gridBagConstraints1.gridy = 5;
	    gridBagConstraints1.insets = new Insets(30, 0, 0, 0);
	    getContentPane().add(OKButton, gridBagConstraints1);
	    
	    CancelButton.setText("Cancel");
	    CancelButton.setActionCommand("cancel");
	    CancelButton.setName("button7");
	    CancelButton.setBackground(new Color (212, 208, 200));
	    CancelButton.setForeground(Color.black);
	    CancelButton.addActionListener(this);
	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 3;
	    gridBagConstraints1.gridy = 5;
	    gridBagConstraints1.insets = new Insets(30, 0, 0, 0);
	    getContentPane().add(CancelButton, gridBagConstraints1);
	    

	    
	    gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 2;
	    gridBagConstraints1.gridy = 4;

    }

	/**
	 * Checks the action command of all components and decides what needs to be done.
	 *
	 * @param e the event that has been fired
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed( ActionEvent e ){
		
	    try{
	    	if( ( e.getActionCommand() ).equals( "ok" ) ){
		    
		    //a temporary button to use for determining the game type
		    ButtonModel tempButton = gameModes.getSelection();
		    
		    //if check to see of the local radio button is selected
		    if( tempButton.getActionCommand().equals( "local" ) ){
			
			//set up a local game
			theFacade.setGameMode( theFacade.LOCALGAME );
			
			
			//hide the Firstscreen, make a Secondscreen and show it
			this.hide();
			//next = new Secondscreen( theFacade, this, theFacade.LOCALGAME, mediator );
			next.show();
			
			//if the host game button is selected
		    } 
			
		} else if( e.getActionCommand().equals( "cancel" ) ){
		    System.exit( 0 );
		} 
		
	    } catch( Exception x ) {
	    	System.err.println( x.getMessage() );
	    }
	}
}