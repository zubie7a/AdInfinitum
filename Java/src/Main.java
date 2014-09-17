import java.awt.*   ;
import javax.swing.*;

public class Main extends JFrame{
	
  /*Now, shall we initiate the Survival Strategy?*/
	
  private static final long serialVersionUID = 1L; //no idea what's this for, but Eclipse likes it when I put it
  
  public Panel panel          ; //JPanel containing the interfacing elements between the human and the application
  public Graph graph          ; //JPanel containing the visualization of the simulation taking place
  public OtherPanel otherPanel; //JPanel containing the information for the simulation's running time
  
  public Main(){
      this.setTitle("Ad Infinitum")               ;
      this.setSize(300,428)                       ;
      this.setResizable(false)                    ;
      setLayout(new BorderLayout())               ;
      graph = new Graph(this)                     ;
      panel = new Panel(this)                     ;
      otherPanel = new OtherPanel(this)           ;
      this.add(panel, BorderLayout.NORTH)         ;
      this.add(graph, BorderLayout.CENTER)        ;
      this.add(otherPanel, BorderLayout.SOUTH)    ;
      this.setFocusable(true)                     ;
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
  
  public static void main(String[] args) {
	  Main main = new Main(); 
	  main.setVisible(true) ;
  }
  
  public void itBegins(int x){
	  graph.reGraph(x);
  }
  
  public void updateTime(int time){
	  otherPanel.currentTime(time);
  }
  
}