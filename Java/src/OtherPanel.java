
import javax.swing.*       ;
import javax.swing.border.*;
import java.awt.Color      ;
import java.awt.GridLayout ;


public class OtherPanel extends JPanel{
	
	private static final long serialVersionUID = 1L; //no idea what's this for, but Eclipse likes it when I put it
	public JTextField timeTex;	//'timeTex' is a text field that will display the time of the current simulation in milliseconds
	public JLabel elapsedTime;  //'elapsedTime' is a label that will reflect the purpose of the 'timeTex' text field it neighbors. 
	public Main main;
	
    public OtherPanel(Main main){
		setLayout(new GridLayout(1, 2));
		TitledBorder borde = BorderFactory.createTitledBorder("");
		borde.setTitleColor(Color.white);
		borde.setBorder(BorderFactory.createLineBorder(Color.magenta.darker()));
		setBorder(borde);
		
		timeTex = new JTextField();
		timeTex.setEditable(true) ;
		timeTex.setText("")       ;
        
		elapsedTime = new JLabel();
		elapsedTime.setForeground(Color.WHITE);
		elapsedTime.setText("Elapsed Time (ms): ");
        
		add(elapsedTime);
		add(timeTex);
		
		this.setBackground(Color.DARK_GRAY);
		this.main = main                   ;
	}
	void currentTime(int time){
		timeTex.setText(Integer.toString(time));
	}
}

