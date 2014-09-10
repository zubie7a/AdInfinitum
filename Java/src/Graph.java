import javax.swing.*                     ;
import java.awt.Color                    ;
import java.awt.Graphics                 ;

public class Graph extends JComponent{
	
	private static final long serialVersionUID = 1L; //no idea what's this for, but Eclipse likes it when I put it
	public int UP, RIGHT, DOWN, LEFT               ; //set of variables regarding the four directions for each cell's neighbors
	public volatile int lucky                      ; //the integer 'lucky' controls the state of the 'chaos' value, the possibility of survival
	                                                 //it is 'volatile' because that will allow for different threads to access it on the go
	public int X,Y,luck,xyz                        ; //the integers 'X' and 'Y' contains the dimensions of the space. the integer 'luck' contains
	                                                 //the default survival percentage which is transferred to 'lucky' in case the 'chaos' is enabled
	                                                 //the integer 'xyz' contains the amount of colors desired for the simulation.
	public String names[]                          ; //the String array 'names' contain the name of each color for 'population display' purposes
	public boolean t[][]                           ; //the boolean space 't' contains booleans regarding whether a cell is 'active' or 'dormant'
	                                                 //at the beginning of the simulation, there is only one 'active' cell. when that cell starts
	                                                 //interacting with other cells, those cells become 'active' cells. the most accurate example
	                                                 //to represent this is considering the first 'active' cell as a 'patient zero' which starts
	                                                 //spreading a disease to its neighbors. 											
	public int space[][]                           ; //the 'space' grid is the space which contains all the cells, its dimensions are Y*X 
	public int p[][]                               ; //the 'p' grid is a temporal grid in which the state changes of each iteration are stored
	public Main main                               ;	
	public Color col                               ;
	public int spread                              ; //the integer 'spread' contains the spread percentage of the 'active' status in cells
	public Graph(Main main){
		  xyz = 9    ;	//initial number of colors to use by default
		  X = Y = 150;	//default dimension of the 'space' grid
		  luck = 5   ;  //default chance of survival in case 'chaos' is enabled
		  spread = 3 ;  //default chance of spread of the 'active' status in cells
		  names = new String[9];
		  names[0] = "RED:                    ";
		  names[1] = "DARK MAGENTA: "          ;
		  names[2] = "MAGENTA:           "     ;
		  names[3] = "BLUE:                   ";
		  names[4] = "LIGHTER BLUE:     "      ;
		  names[5] = "CYAN:                  " ;
		  names[6] = "GREEN:                 " ;
		  names[7] = "YELLOW:               "  ;
		  names[8] = "ORANGE:              "   ;
		  
		  space = new int[Y][X];
		  t = new boolean[Y][X];
		  p = new int[Y][X]    ;
	  	  
		  t[(int)(Math.random()*Y)][(int)(Math.random()*X)]=true; //the first 'active' or 'infected' cell is determined by random values 
																  //ranging between [0,Y) and [0,X)
		  
		  reGraph(xyz);      //the method 'reGraph' is called, using the default 'xyz' value, '9', to paint a initial grid of all 9 possible
		                     //colors. Its done purely for artistic purposes, because I think its somehow 'beautiful' to have that 'debris'
		                     //stage with all those colors at the launch of the program.
		  setVisible(true);
		  this.main = main;		
    }
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		for(int y=0; y<Y; ++y){
			  for(int x=0; x<X; ++x){
				  if(space[y][x]==1){
					  col = Color.RED                      ;
				  }
				  if(space[y][x]==2){
					  col = Color.MAGENTA.darker()         ;
				  }
				  if(space[y][x]==3){
					  col = Color.MAGENTA.darker().darker();
				  }
				  if(space[y][x]==4){
					  col = Color.BLUE.darker()            ;
				  }
				  if(space[y][x]==5){
					  col = Color.BLUE                     ;
				  }
				  if(space[y][x]==6){
					  col = Color.CYAN                     ;
				  }
				  if(space[y][x]==7){
					  col = Color.GREEN                    ;
				  }
				  if(space[y][x]==8){
					  col = Color.YELLOW                   ;
				  }
				  if(space[y][x]==9){
					  col = Color.ORANGE                   ;
				  }
				  g.setColor(col)		  ;
				  g.fillRect(2*x, 2*y,2,2); //the default dimension of a 'cell' is 2 square pixels. if you want to have bigger cells and maintain
				                            //aspect ratio, you can set the values to g.fillRect(3*x, 3*y,3,3) which paints cells of 3 square pixels
				                            //and change X and Y above to 100. if you want smaller cell sizes and maintain aspect ratio, you can set
				                            //the values to g.fillRect(x,y,1,1) which paints cells of 1 square pixel, and change the X and Y above to 300.
				                            //thought I don't recommend the last because at least in my computer it lags quite a lot (it goes from having
				                            //22500 cells (150*150 grid) to 90000 cells (300*300 grid), and thats on a i5 with 4GB of RAM!
			}
		}	
	}
	public void destinationOfFate(){  //this is the method that is constantly called by the 'worker' thread as long as the 'stopper' boolean is set to false
		for(int m=0;m<Y;++m){
			for(int n=0;n<X;++n){
				UP=m-1   ;	
				RIGHT=n+1;
				DOWN=m+1 ;
				LEFT=n-1 ;
		
				if(UP<0)      UP=0     ; //if one of the neighbor values exceeds the limits, those are set back to the limit
				if(RIGHT>X-1) RIGHT=X-1;
				if(DOWN>Y-1)  DOWN=Y-1 ;
				if(LEFT<0)    LEFT=0   ;
				colorMethod(UP,RIGHT,DOWN,LEFT,m,n); //this method uses the four neighbors and m,n which is the y,x position of the cell we're working on
			}
		}	
        for(int m=0;m<Y;++m){
			for(int n=0;n<X;++n){
				p[m][n]=space[m][n]; //after one iteration of going through the entire 'space' grid updating the states, everything is stored in the 'p'
				                     //temporal grid, waiting for the next iteration
			}
		}
        paintImmediately(0,0,2000,2000); //the paint method is immediately called after each iteration
	}
	
	void colorMethod(int UP, int RIGHT, int DOWN, int LEFT, int m, int n){
		/*During quite a time this was called 'functionIHate', paradoxically I should like it, because previously I was doing this manually for each color value*/

		if(t[UP][n]==false && t[m][RIGHT]==false && t[DOWN][n]==false && t[m][LEFT]==false){
			return; //this function exits suddenly if none of the neighboring cells is 'active' as defined by its corresponding boolean in the 't' grid
		}
		int k = space[m][n]; //the integer 'k' will temporarily hold the value of the cell we are working on
		int j = k+1 	   ; //the integer 'j' will temporarily hold the value that 'defeats' the current cell when hey 'engage' or have an 'encounter'
		                     //obeying the rule that a cell with state 'k' will be 'defeated' by a cell with state 'k+1' except for the cell with the
		                     //last state possible or 'xyz' which will be defeated by the cell of state '1'.
		
		if(k == xyz){        //the border case is that the current cell hold the value of the last status possible, in that case, this cell will be 'weak'
		                     //to the cell of status '1'
			if(p[UP][n]==1||p[m][RIGHT]==1||p[DOWN][n]==1||p[m][LEFT]==1){//if at least one of the neighbors has status '1' an 'encounter' begins.  
				if((int)(Math.random()*10)+1>lucky){                  //if the integer 'lucky' is set to '0', the 'weak' cell will always be defeated
                                                                                      //otherwise, it will have a chance of survival (which by default is the value of
                                                                                      //the integer 'luck' or '5', meaning 50% of the times nothing could happen.
					space[m][n]=1   ;
					t[m][n]=true    ;                             //as there was a change if this condition was entered, meaning there was a change, 
                                                                                      //the current cell's respective position in the boolean grid 't' is now marked as 'active'.
				}
			}
		}
		else{
			if(p[UP][n]==j||p[m][RIGHT]==j||p[DOWN][n]==j||p[m][LEFT]==j){//if at least one of the neighbors has status 'k+1' an 'encounter' begins.  
				if((int)(Math.random()*10)+1>lucky){                  //if the integer 'lucky' is set to '0', the 'weak' cell will always be defeated
                                                                                      //otherwise, it will have a chance of survival (which by default is the value of
				                                                      //the integer 'luck' or '5', meaning 50% of the times nothing could happen.
					space[m][n]=j   ;
					t[m][n]=true    ;                             //as there was a change if this condition was entered, meaning there was a change, 
                                                                                      //the current cell's respective position in the boolean grid 't' is now marked as 'active'.
				}
			}
		}
	}
	
	void reGraph(int val){ //this is the method that 'restarts' the 'space' grid with new random states between '1' and the integer 'val' which was parsed from the
	                       //'tex' textbox as the desired amount of states or colors to use in the new simulation.
		xyz = val;     //the integer 'xyz' is updated to reflect the change brought by the integer 'val'
		for(int y=0; y<Y; ++y){
			  for(int x=0; x<X; ++x){
				  space[y][x] = (int)(Math.random()*val + 1); //the 'space' grid is redrawn with values between '1' and the integer 'val'
				      t[y][x] = false;                        //all of the booleans in the 't' grid are reseted to false, meaning all are inactive
			  }
		}
	    t[(int)(Math.random()*Y)][(int)(Math.random()*X)]=true; //and then one single boolean or the 'patient zero' is set to true, randomly at [0,Y) and [0,X)
		paintImmediately(0,0,2000,2000);
	}
	
	void count(){               //this method counts the amount of cells for each state or color in the 'space' grid
		int cnt[] = new int[9];
		String res = "";        //everything is concatenated into this String
		for(int z=0; z<9; ++z){
			cnt[z]=0;
		}
		for(int y=0; y<Y; ++y){
			for(int x=0; x<X; ++x){
				cnt[space[y][x]-1]++;
			}
		}
		for(int z=0; z<xyz;++z){ //it goes up to the value of the integer 'xyz' so the output message will only show values for the colors or states currently on the grid
			res += names[z] + cnt[z] + '\n';
		}
		JOptionPane.showMessageDialog(this, res); //a message dialog is shown with the result. while this window is open, the simulation will be paused. once its closed, it
                                                          //will resume operations as normal. this might be useful for those people crazy about statistics and the dominance of a 
                                                          //color on the simulation on a given time.
	}
	
}
	