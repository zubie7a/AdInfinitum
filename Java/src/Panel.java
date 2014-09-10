
import javax.swing.*                ;
import javax.swing.border.*         ;
import java.awt.Color               ;
import java.awt.GridLayout          ;
import java.awt.event.ActionEvent   ;
import java.awt.event.ActionListener;


public class Panel extends JPanel implements ActionListener, Runnable{

	private static final long serialVersionUID = 1L; //no idea what's this for, but Eclipse likes it when I put it
	public final static String CHAOS = "CHAOS";//'CHAOS' is a string containing the action command for the check box 'survival'->'Chaos'
	public final static String EXEC = "EXEC"  ;//'EXEC'  is a string containing the action command for the button 'run'->'Execute'
	public final static String STOP = "STOP"  ;//'STOP'  is a string containing the action command for the button 'stop'->'Pause/Resume'
	public final static String POPL = "POPL"  ;//'POPL'  is a string containing the action command for the button 'pop'->'Population'
	public volatile boolean stopper           ;//'stopper' is a boolean regarding whether the simulation should be running or not
	public JCheckBox survival                 ;//'survival' is a checkbox that determines if the simulation should take a chaotic behavior or not
	public boolean running                    ;//'running' is a boolean regarding whether the simulation has been started or not
	public JTextField tex                     ;//'tex' is a textfield in which the user inputs the desired value (1-9) for the amount of available colors 
	public Thread worker                      ;//'worker' is the thread in which the simulation runs, using a thread allows the use of other commands, such
                                                 //as changing the 'survival' checkbox while the simulation runs, pausing the simulation, or checking the population
	public JButton stop                       ;//'stop' is the button that pauses or resumes the simulation, its label changes according to its state
	public JButton run                        ;//'run' is the button that reads the desired amount of colors to use, then runs the simulation
	public JButton pop                        ;//'pop' is the button that pauses the simulation, displays the population for each color, and then resumes again
	public JLabel colr                        ;//'colr' is the label regarding the 'tex' textfield, an indicator of what the textfield is for
	public Main main;	
	public int temp                           ;//'temp' is an integer which holds temporarily the integer value attempted to be parsed from the 'tex' textfield
	public int time                           ;//'time' is an integer which holds the elapsed time since start of the current simulation
	
	public Panel(Main main){
		running = false          ;//from the beginning there is nothing running
		stopper = true           ;//stopper is set to 'true'...
		worker = new Thread(this);//...because the thread will be initialized but with 'stopper' as true, it will do nothing (for now)
		worker.start()           ;//the thread is started only once. the control for turning the simulation 'on' or 'off' will be the boolean 'stopper'
		time = 0                 ;
    	
		setLayout(new GridLayout(2, 4))                                        ;
		TitledBorder borde = BorderFactory.createTitledBorder("")              ;
		borde.setTitleColor(Color.white)                                       ;
		borde.setBorder(BorderFactory.createLineBorder(Color.magenta.darker()));
		setBorder(borde)                                                       ;
        
		survival = new JCheckBox("Chaos")  ;
		survival.setForeground(Color.white);
		survival.setActionCommand(CHAOS)   ;//the checkbox 'survival' will have a action command set to it, and this class as its action listener, so the program will detect 
		survival.addActionListener(this)   ;//changes on its selected state, to enable or disable the 'chaotic' factor, instead of checking if its selected on each iteration.
        
		tex = new JTextField();
		tex.setEditable(true) ;
		tex.setText("")       ;
        
		colr = new JLabel()            ;
		colr.setText("N¼ of Colors")   ;
		colr.setForeground(Color.WHITE);
        
		pop = new JButton("Population");
		pop.setActionCommand(POPL)     ;	
		pop.addActionListener(this)    ;
        
		run = new JButton("Execute");
		run.setActionCommand(EXEC)  ;
		run.addActionListener(this) ;
        
		stop = new JButton("Pause") ;
		stop.setActionCommand(STOP) ;
		stop.addActionListener(this);
        
		add(colr)    ;
		add(tex)     ;
		add(run)     ;
		add(survival);
		add(stop)    ;
		add(pop)     ;
                
		this.setBackground(Color.DARK_GRAY);
		this.main = main                   ;
	}
	
	public void delayah(){
		try{
			Thread.sleep(1); //this is a method for waiting 1 millisecond...
			time++;
			main.updateTime(time);
		}
		catch(InterruptedException e){
		}
	}
	
	public void run(){
		while(true){	
			while(!stopper){
				delayah(); //...which is called on each iteration before calling a method to update the state of the running grid
				main.graph.destinationOfFate();
			}
		}
	}

    public void actionPerformed(ActionEvent evento){ 
    	
        if(evento.getActionCommand() == "EXEC"){
        	running = true;  //the simulation is now running, from now on it will always be truth
        	stopper = true;  //the stopper is temporarily set to true, just in case the user wants to run a new simulation
        	
        	if(tex.getText().length()!=0){
        		temp = Integer.parseInt(tex.getText()); //this tries to read an integer from the 'tex' textfield
        	}
        	else{
        		temp = 0; //in case the 'tex' textfield is empty, by default the value will be set to zero
        	}
        	
        	if(temp<=9 && temp>=1){
				time = 0           ;//the integer 'time' is reseted to '0' as part of the new simulation
				main.itBegins(temp);//if its a valid value between 9 and 1, it calls the method at 'main' that sends this value 
				                    //to the graphic class, and generates a new grid based on the desired amount of colors
				stopper = false	   ;//the boolean 'stopper' is set to false, so the cycle in the thread will call the method that
                                                    //starts updating the state of each cell in the grid according to the rules
        	}
        	else{
        		JOptionPane.showMessageDialog(this,"Enter a number between 1 and 9");
        	}
        }
        
        if(evento.getActionCommand() == "STOP" && running==true){//if the simulation hasn't been run at least once, this button does nothing
        	if(stop.getText()=="Pause"){    //if the current state of the button shows 'pause' it means its running
        		stopper = true        ; //the boolean 'stopper' its set to true so the simulation 'pauses'
        		stop.setText("Resume"); //and then the name of the button changes to 'resume'
        	}
        	else{
        		stopper = false      ;//if the current state shows 'resume', the boolean 'stopper' is set to 'false'
        		stop.setText("Pause");//so the simulation 'runs' again and the name of the button changes to 'pause'
        	}
        }
        
        if(evento.getActionCommand() == "CHAOS"){
        	if(survival.isSelected()){
        		main.graph.lucky = main.graph.luck; //this updates directly on the graphic class the state of the chaotic variable
                                                           //modifying the integer 'lucky' according to it, putting the value of 'luck', which 
                                                           //is the default rate of 'survival', which is 5, meaning 50%
        	}
        	else{
        		main.graph.lucky = 0; //if the checkbox is not selected, the chance of survival becomes 0, meaning 0%,
                                             //meaning the weak 'cell' will always be 'defeated' in a 'encounter'
        	}
        }
        
        if(evento.getActionCommand() == "POPL"){
        	stopper = true	                            ;//the boolean 'stopper' is set to 'true' so the simulation is temporarily paused from running
        	main.graph.count()                          ;//a method that counts the amount of cells belonging to the available colors is called
        	if(running==true && stop.getText()!="Resume"){//if the simulation was launched at least once, the boolean 'stopped' returns to its false state
        		stopper = false                     ;//because if you inquire about the population of the grid at program launch (which is of 9 possible states)
			                                             //the simulation would launch upon closing the message dialog showing the amount of cells for each color.
				                                         //also you can inquire about the population while the simulation is in the 'Paused' state. In case you checked
				                                         //the population, the simulation would leave the 'Paused' state without reflecting a change on the 'stop' button.
        	}
        }
    }
}