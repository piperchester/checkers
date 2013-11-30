import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.net.*;

/**
 * Main GUI class. Serves as the interface for the board.
 */
public class CheckerGUI extends JFrame implements ActionListener, IColleague{
   
    private static Facade theFacade; 
    private Vector<JButton> possibleSquares = new Vector<JButton>();
    
    private JButton[] squares = new JButton[65];  // Array that holds 64 buttons, serving as the board squares
    
    private int timeRemaining;
    private JLabel PlayerOnelabel;
    private JLabel playerTwoLabel;
    private JLabel timeRemainingLabel;
    private JLabel secondsLeftLabel;
    private JButton ResignButton;
    private JButton DrawButton;
    private JLabel warningLabel, whosTurnLabel;
    
    private GameplayMediator mediator;
    private Invoker invoker; // Invoker of commands for Command Pattern
    
    //the names and time left
    private static String playerOnesName="", playerTwosName="", timeLeft="";

    //the players
    private Player activePlayer, passivePlayer = null;
    
    /** 
     *
     * Constructor, creates the GUI and all its components
     *
     * @param facade the facade for the GUI to interact with
     * @param name1 the first players name
     * @param name2 the second players name
     *
     */
    public CheckerGUI( Facade facade, String name1, String name2, GameplayMediator mediator ) {

        super("Checkers");
        
        invoker = new Invoker();


        // long names mess up the way the GUI displays
        // this code shortens the name if it is too long
        String nameOne="", nameTwo="";
        if(name1.length() > 7 ){
            nameOne = name1.substring(0,7);
        }else{
            nameOne = name1;
        }
        
        if(name2.length() > 7 ){
            nameTwo = name2.substring(0,7);
        }else{
            nameTwo = name2;
        }
                
        playerOnesName = nameOne;
        playerTwosName = nameTwo;
        theFacade = facade;

        this.mediator = mediator;
        mediator.Register(this);
        
        this.requestActive(mediator);
        this.requestPassive(mediator);
        try{
        	theFacade.addActionListener(this);
        } catch( Exception e ){
            System.err.println( e.getMessage() );
        }

        
        try{
        	theFacade.addActionListener(this);
        } catch( Exception e ){
            System.err.println( e.getMessage() );
        }

        initComponents();
        pack();
        update();
    }
    
    /**
     * This method is called from within the constructor to
     * initialize the form. It initializes the components
     * adds the buttons to the Vector of squares and adds
     * an action listener to the components 
     *
     */
    private void initComponents() {
        
	this.setResizable( false );
	
    PlayerOnelabel = new JLabel();
    playerTwoLabel = new JLabel();
    whosTurnLabel = new JLabel();
    
    warningLabel = new JLabel( );
    timeRemainingLabel = new JLabel();
    secondsLeftLabel = new JLabel();

    ResignButton = new JButton();
    ResignButton.addActionListener( this );
	
    DrawButton = new JButton();
    DrawButton.addActionListener( this );
      
    //sets the layout and adds listener for closing window
    getContentPane().setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints;
    

    //add window listener
    addWindowListener(
    		new WindowAdapter() { public void windowClosing(WindowEvent evt) { 
    			exitForm(evt);
        }});
	
	
        // Initial Coords for Board
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; 
        gridBagConstraints.gridy = 1;
        int gridYCounter = 0;  // This counter will increment from 1 to 8
        
        // Loop that creates the buttons, adds them to the squares array
        // and to the possibleSquares Vector. Also sets appropriate dimensions,
        // color, ActionCommand, and the GridBag constraints.
    	for (int i = 0; i < 64; i++){
    		gridBagConstraints = new java.awt.GridBagConstraints();
    	
    		// Y counter starts at 1, and will increment seven times to reach 8
    		if (i % 8 == 0) {
    			gridYCounter++;
    		}
    		
    		gridBagConstraints.gridy = gridYCounter;
    		gridBagConstraints.gridx = (i % 8); // Loops from 0 -> 7
    		
    		squares[i] = new JButton();
    		possibleSquares.add(squares[i]);
    		squares[i].addActionListener(this);
    		
    		squares[i].setPreferredSize(new Dimension(80, 80));
    		squares[i].setActionCommand(Integer.toString(i));
    		
    		// As soon as the row increases, alternate the colors differently.
    		// Even row, even X values for white and vice versa.
    		if (gridBagConstraints.gridy % 2 == 0 && i % 2 == 0){
    			squares[i].setBackground(Color.white);
    		} else {
    			squares[i].setBackground(new Color(204, 2034, 153));
    		}
    		
    		getContentPane().add(squares[i], gridBagConstraints);
    	}
	
        
        PlayerOnelabel.setText("Player 1:     " + playerOnesName );
        PlayerOnelabel.setForeground( Color.black );
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        getContentPane().add(PlayerOnelabel, gridBagConstraints);
        
        playerTwoLabel.setText("Player 2:     " + playerTwosName );
        playerTwoLabel.setForeground( Color.black );
		
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        getContentPane().add(playerTwoLabel, gridBagConstraints);
        
        whosTurnLabel.setText("");
        whosTurnLabel.setForeground( new Color( 0, 100 , 0 ) );
        
        gridBagConstraints.gridx=8;
        gridBagConstraints.gridy=1;
        getContentPane().add(whosTurnLabel, gridBagConstraints );
        
        warningLabel.setText( "" );
        warningLabel.setForeground( Color.red );
		
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        getContentPane().add( warningLabel, gridBagConstraints );
        
        timeRemainingLabel.setText("Time Remaining:");
        timeRemainingLabel.setForeground( Color.black );
		
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        getContentPane().add(timeRemainingLabel, gridBagConstraints);
        
        secondsLeftLabel.setText( timeLeft + " sec.");
        secondsLeftLabel.setForeground( Color.black );
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        getContentPane().add(secondsLeftLabel, gridBagConstraints);
        
        ResignButton.setActionCommand("resign");
        ResignButton.setText("Resign");
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 7;
        getContentPane().add(ResignButton, gridBagConstraints);
        
        DrawButton.setActionCommand("draw");
        DrawButton.setText("Draw");
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        getContentPane().add(DrawButton, gridBagConstraints);
	
    }
    
    /** 
     * Exit the Application
     * @param the window event
     */
    private void exitForm(java.awt.event.WindowEvent evt) {
        // OLD: theFacade.pressQuit();
    	System.out.println("Testing");
    	invoker.invokeCommand(new QuitCommand(this, mediator));
    }

    /**
     * Takes care of input from users, handles any actions performed
     *
     * @param e  the event that has occured
     */
    
    public void actionPerformed( ActionEvent e ) {
	        
		try{
		    //if a square gets clicked
		    if( e.getActionCommand().equals(  "1" ) ||
			e.getActionCommand().equals(  "3" ) || 
			e.getActionCommand().equals(  "5" ) ||
			e.getActionCommand().equals(  "7" ) ||
			e.getActionCommand().equals(  "8" ) ||
			e.getActionCommand().equals( "10" ) ||
			e.getActionCommand().equals( "12" ) ||
			e.getActionCommand().equals( "14" ) ||
			e.getActionCommand().equals( "17" ) ||
			e.getActionCommand().equals( "19" ) ||
			e.getActionCommand().equals( "21" ) ||
			e.getActionCommand().equals( "23" ) ||
			e.getActionCommand().equals( "24" ) ||
			e.getActionCommand().equals( "26" ) ||
			e.getActionCommand().equals( "28" ) ||
			e.getActionCommand().equals( "30" ) ||
			e.getActionCommand().equals( "33" ) ||
			e.getActionCommand().equals( "35" ) ||
			e.getActionCommand().equals( "37" ) ||
			e.getActionCommand().equals( "39" ) ||
			e.getActionCommand().equals( "40" ) ||
			e.getActionCommand().equals( "42" ) ||
			e.getActionCommand().equals( "44" ) ||
			e.getActionCommand().equals( "46" ) ||
			e.getActionCommand().equals( "49" ) ||
			e.getActionCommand().equals( "51" ) ||
			e.getActionCommand().equals( "53" ) ||
			e.getActionCommand().equals( "55" ) ||
			e.getActionCommand().equals( "56" ) ||
			e.getActionCommand().equals( "58" ) ||
			e.getActionCommand().equals( "60" ) ||
			e.getActionCommand().equals( "62" ) ) {
			
			//call selectSpace with the button pressed
			theFacade.selectSpace(Integer.parseInt(e.getActionCommand()));
			
		    } else if(e.getActionCommand().equals("draw")){
		    	invoker.invokeCommand(new DrawCommand(this));
	
		    } else if(e.getActionCommand().equals("resign" )) {
		    	// OLD: theFacade.pressQuit();
		    	
		    	// Call invoker to execute from QuitCommand
		    	invoker.invokeCommand(new QuitCommand(this, mediator));	
		    
		    } else if(e.getActionCommand().equals("resign" )) {
		    	invoker.invokeCommand(new QuitCommand(this, mediator));	
			
		    //if the source came from the facade
		    }else if( e.getSource().equals( theFacade ) ) {
				//if its a player switch event
				if ( (e.getActionCommand()).equals(theFacade.playerSwitch) ) {
				    //set a new time
				    timeRemaining = theFacade.getTimer();
				    //if it is an update event
				} else if ( (e.getActionCommand()).equals(theFacade.UPDATE) ) {
				    //update the GUI
				    update();
				} else {
				    throw new Exception( "unknown message from facade" );
				}
		    }
		    //catch various Exceptions
		}catch( NumberFormatException excep ){
		    System.err.println(
			     "GUI exception: Error converting a string to a number" );
		}catch( NullPointerException exception ){
		    System.err.println( "GUI exception: Null pointerException "
					+ exception.getMessage() );
		    exception.printStackTrace();
		}catch( Exception except ){
		    System.err.println( "GUI exception: other: "
					+ except.getMessage() );
		    except.printStackTrace();
		}
	
    }
    

    /**
     * Updates the GUI reading the pieces in the board
     * Puts pieces in correct spaces, updates whos turn it is
     *
     * @param the board
     */
    private void update(){
		if( checkEndConditions() ){
		    theFacade.showEndGame(" ");
		}
		
		
		Board board = theFacade.stateOfBoard(); //the board to read information from
		
		JButton temp =  new JButton();  //a temp button to work with
		
		//go through the board
		for( int i = 1; i < board.sizeOf(); i++ ){
		    // if there is a piece there
		    if( board.occupied( i ) ){
			
				//check to see if color is blue
				if( board.colorAt( i ) == Color.blue ){
		
				    if((board.getPieceAt(i)).getType() == Board.SINGLE){
						
						temp = (JButton)possibleSquares.get(i); // show a blue single piece in that spot board
			
						//get the picture from the web
						try {
						    temp.setIcon(new ImageIcon(new URL("file:BlueSingle.gif")));
						} catch( MalformedURLException e ){
						    System.out.println(e.getMessage());
						}
				    } else if((board.getPieceAt(i)).getType() == Board.KING ){
				    	temp = (JButton)possibleSquares.get(i);  //show a blue king piece in that spot board
		
						//get the picture formt the web
						try{
						    temp.setIcon(new ImageIcon(new URL("file:BlueKing.gif")));
						} catch( Exception e ){
							
						}
				    }
		
				// White piece check         
				} else if( board.colorAt( i ) == Color.white ){
		
				    // Check Single VS Kinged
				    if((board.getPieceAt(i)).getType() == Board.SINGLE){
		
						temp = (JButton)possibleSquares.get(i);
			
						//get the picture from the web
						try{
						    temp.setIcon(new ImageIcon(new URL("file:WhiteSingle.gif")));
						}catch( Exception e ){
							
						}
					
					
				    }else if((board.getPieceAt(i)).getType() == Board.KING ){
			
						temp = (JButton)possibleSquares.get(i);
			
						//get the picture from the web
						try{
						    temp.setIcon(
						      new ImageIcon(new URL("file:WhiteKing.gif") ) );
						}catch( Exception e ){
							
						}
				    }
				}
		    }else {
				//show no picture
				temp = (JButton)possibleSquares.get(i);
				temp.setIcon( null );
		    }
		}
	
	//this code updates whose turn it is
	this.requestActive(mediator);
	this.requestPassive(mediator);
	if(activePlayer.getNumber() == 2 ){
	    playerTwoLabel.setForeground( Color.red );
	    PlayerOnelabel.setForeground(Color.black );
	    whosTurnLabel.setText( playerTwosName + "'s turn ");
	}else if(activePlayer.getNumber() == 1 ){
	    PlayerOnelabel.setForeground( Color.red );
	    playerTwoLabel.setForeground(Color.black );
	    whosTurnLabel.setText( playerOnesName + "'s turn" );
	}
	
}
    
    /**
     * Update the timer. If the time has run out but not in warning
     * time yet, display warning and count warning time. If the time
     * has run out and it was in warning time, quit game.
     *
     */
    public void updateTime() {            
		if (theFacade.getTimer() > 0) {
		    if (timeRemaining <= 0 && (warningLabel.getText()).equals("")) {
				timeRemaining = theFacade.getTimerWarning();
				warningLabel.setText( "Time is running out!!!" );
		    } else if (timeRemaining <= 0 && !(warningLabel.getText()).equals( "" )) {
		    	// OlD: theFacade.pressQuit();
		    	invoker.invokeCommand(new QuitCommand(this, mediator));
		    } else {
		    	timeRemaining--;
		    }
	                
		    secondsLeftLabel.setText(timeRemaining + " sec.");
	                    
		} else {
		    secondsLeftLabel.setText("*****");
		}
	}
    
    /**
     * Checks the ending condotions for the game
     * see if there a no pieces left
     *
     * @return the return value for the method
     *           true if the game should end
     *           false if game needs to continue 
     */
 
        public boolean checkEndConditions(){
           
	    //the return value
            boolean retVal = false;
            try{
		//the number of each piece left
		int whitesGone = 0 , bluesGone = 0;
		
		//the board to work with
		Board temp = theFacade.stateOfBoard();
		
		//go through all the spots on the board
		for( int i=1; i<temp.sizeOf(); i++ ){
		    //if there is a piece there
		    if( temp.occupied( i  ) ){
			//if its a blue piece there
			if( (temp.getPieceAt( i )).getColor() == Color.blue ){
			    // increment number of blues
			    bluesGone++;
			    //if the piece is white
			}else if( (temp.getPieceAt( i )).getColor()
				  == Color.white ){
			    //increment number of whites
			    whitesGone++;
			}
		    }
		}//end of for loop
		
		//if either of the number are 0
		if( whitesGone == 0 || bluesGone == 0 ){
		    retVal = true;
		}

            }catch( Exception e ){
                
                System.err.println( e.getMessage() );            
                
            }
            return retVal;
            
        }//checkEndConditions


	@Override
	public void SendMessage(IMediator mediator, String message) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void requestPlayerName(IMediator mediator) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void ReceiveMessage(String message) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void RecieveActivePlayer(Player p) {
		activePlayer = p;
		
	}


	@Override
	public void RecievePassivePlayer(Player p) {
		passivePlayer = p;
		
	}


	@Override
	public void requestPassive(IMediator mediator) {
		mediator.getPassivePlayer(this);
		
	}


	@Override
	public void requestActive(IMediator mediator) {
		mediator.getActivePlayer(this);
		
	}
}