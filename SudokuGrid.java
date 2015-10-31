import javax.swing.*;
import java.awt.*;
//import java.util.*;
import java.awt.event.*;

import java.util.ArrayList;

class SudokuGrid extends JPanel implements ActionListener{
    private  float DELTA = 1f;
    //private  Timer timer = new Timer(5, null);
    public ArrayList<Timer> timersToTheRight = new ArrayList<Timer>(); // to the right
    public ArrayList<Timer> timersToTheLeft = new ArrayList<Timer>(); // to the left
    private float alpha = 1f;
    private float fadeOutMinimum = 0f;

    private int square_x = 0;
    private int square_y = 0;

    private int offset_y;
    private int distanceBetweenCells_y;
    
    public SudokuCell sudokuCells[][]; // the grid made of 81 cells
    public SudokuCell sudokuCells2[][]; // the grid made of 81 cells ready to be moved
    public SudokuCell sudokuCells3Before[]; // only references to the initial position of current 9 moving cells
    public SudokuCell sudokuCells3Now[]; // only references to the current 9 moving cells
    public SudokuCell sudokuCells3BeforeLeft[]; // only references to the initial position of current 9 moving cells
    public SudokuCell sudokuCells3NowLeft[]; // only references to the current 9 moving cells
    //public SudokuCell sudokuCells3After[]; // only references to the final position of current 9 moving cells   
    public GridBagConstraints c;

    public SudokuModel theModel;

    private boolean movingToTheRight = true;

    private static int removedCellGoingToTheLeft[];

    public Circle valueCircles[];
    
    public SudokuGrid(SudokuModel model, int rows, int columns){
	//super(new GridBagLayout());
	super(null);
	//System.out.println("SudokuGrid made");
	setModel(model);
	
	this.setLayout(null);

	//setBackground(new Color(0,0,0,0));
	//this.setBackground(Color.RED);
	// 4 is because I want the distanceBetweenCells_y to be in line with last
	this.setPreferredSize(new Dimension(950, 550-4)); ///!!!! not minimum sau size
        //this.setOpaque(false); // don't know why
	//this.setVisible(true);
	
        //this.setBackground(Color.black);
	/*
        timer.setInitialDelay(10);
        timer.addActionListener(this);
	timer.setCoalesce(false);
	*/
	sudokuCells = new SudokuCell[rows][columns];
	sudokuCells2 = new SudokuCell[rows][columns]; //////////////
	sudokuCells3Before = new SudokuCell[rows];
	sudokuCells3Now = new SudokuCell[rows];
	//sudokuCells3After = new SudokuCell[rows];

	sudokuCells3BeforeLeft = new SudokuCell[rows];
	sudokuCells3NowLeft = new SudokuCell[rows];

	removedCellGoingToTheLeft = new int[rows];

	valueCircles = new Circle[rows + 2];
	
	c = new GridBagConstraints();

	offset_y = (getPreferredSize().height - 9 * 50)/2;
	distanceBetweenCells_y = (getPreferredSize().height - 9 * 50)/(9-1);
	//System.out.println("Distance " + distanceBetweenCells_y);
	for(int i=0; i<rows; i++){
	    for(int j=0; j<columns;j++){
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = j; // was
		c.gridy = i; // the opposite

		sudokuCells[i][j] = new SudokuCell(i,j);
		sudokuCells[i][j].addMouseListener(new SudokuCellListener());
		//System.out.println("Grid settings the model to cell " + this.theModel);
		sudokuCells[i][j].setModel(this.theModel);

		//////
		sudokuCells2[i][j] = new SudokuCell(i,j);
		sudokuCells2[i][j].addMouseListener(new SudokuCellListener());
		//System.out.println("Grid settings the model to cell " + this.theModel);
		sudokuCells2[i][j].setModel(this.theModel);
		sudokuCells2[i][j].setVisible(false);
		
		//add(sudokuCells[i][j], c); // ,c
		//if(i == 0 && j== 0){
		add(sudokuCells[i][j], square_x, square_y + offset_y);
		    
		    //}

		//System.out.println("CELL i=" + i + " j=" + j + " x: " + sudokuCells[i][j].getX()+" y: " + sudokuCells[i][j].getY() + " bounds_x: " + sudokuCells[i][j].getBounds().x + " bounds_y: " + sudokuCells[i][j].getBounds().y + " bounds_width: " + sudokuCells[i][j].getBounds().width + " bounds_height: " + sudokuCells[i][j].getBounds().height + "\n");
				
		if(j == 0){
		    //square_x = (int)sudokuCells[i][j].getX();
		    //square_y = sudokuCells[i][j].getY();
		}

		square_x +=50;
	    }

	    square_x = 0;
	    square_y += 50;
	}

	square_x = 0; // reset to default
	square_y = offset_y; // reset to default
	
	//this.repaint(); //after adding everything, because of absolut locations, repaint

	// exterior border
	//setBorder(BorderFactory.createLineBorder(Color.black));
	//setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.BLACK)); ///////
	//System.out.println("Is sudoku grid EDT " + SwingUtilities.isEventDispatchThread());


	for(int i=0; i<11; i++){
	    String value = Integer.toString(i);
	    int circle_x = 800;
	    int circle_y = (i-1) * 50 + (i-1)*distanceBetweenCells_y;
	    if(i == 0){
		value = "S";
		circle_x = 500;
		circle_y = (5-1) * 50 + (5-1)*distanceBetweenCells_y;
	    } else if(i == 10){
		value = "T";
		circle_x = 900;
		circle_y = (5-1) * 50 + (5-1)*distanceBetweenCells_y;
	    }
	    valueCircles[i] = new Circle(theModel, value);
	    valueCircles[i].setAlphaZero();
	    add(valueCircles[i], circle_x, circle_y);
	}

    }
    
    public void add (JPanel jp, int x, int y){
	jp.setLocation(x, y);
	jp.setBounds(new Rectangle(new Point(x, y), new Dimension(50, 50)));
	add(jp);
	jp.setVisible(true);
	repaint();
	
    }

    public void setModel(SudokuModel model){
	this.theModel = model;
    }

    public void moveColumn(int column, boolean movingToTheRight){
	abandonProgressOnTheRight();
	theModel.viewController.hideWelcomeScreen();

	this.movingToTheRight = movingToTheRight;

	Timer thisTimer = null;
	
	if(movingToTheRight == true){ // INDENT
	    System.out.println("called moving COLUMN to the right");

	    thisTimer = getTimerMovingRight();
	    for(int i=0; i<9; i++){
		sudokuCells3Before[i] = sudokuCells[i][column];
		sudokuCells3Now[i] = sudokuCells2[i][column];
     
	    }
	
	    for(int i=0; i<9; i++){
		sudokuCells3Now[i].setAlphaZero();
		sudokuCells3Now[i].setNoBoldBorder();
		sudokuCells3Now[i].setNotInTheGrid();

		add(sudokuCells3Now[i], sudokuCells[i][column].getX(), sudokuCells[i][column].getY());

	    }
	} // INDENT
	else if(movingToTheRight == false){
	    System.out.println("called moving COLUMN to the left");

	    thisTimer = getTimerMovingLeft();
	    
	    for(int i=0; i<9; i++){
		sudokuCells3BeforeLeft[i] = sudokuCells[i][column];
		sudokuCells3NowLeft[i] = sudokuCells2[i][column];
		
	    }
	    
	    for(int i=0; i<9; i++){
		//sudokuCells3Now[i].setAlphaZero();
		sudokuCells3NowLeft[i].setNoBoldBorder();
		sudokuCells3NowLeft[i].setNotInTheGrid();
		
		//add(sudokuCells3Now[i], sudokuCells[i][column].getX(), sudokuCells[i][column].getY());

	    }
	    
	}


	
	ArrayList<Timer> timers1 = new ArrayList<Timer>();
	timers1.add(thisTimer);
	theModel.timers.add(timers1);
	if(theModel.timers.size() == 1){
	    // otherwise let another timer start this one when it finishes
	    System.out.println("Move column model timers = 1, start level");
	    for(Timer t:theModel.timers.get(0)){
		t.start();
	    }
	} else {
	    System.out.println("Move column blocked by other animation");
	}
	
    }

    public void moveRow(int row, boolean movingToTheRight){
	abandonProgressOnTheRight();
	theModel.viewController.hideWelcomeScreen();

	this.movingToTheRight = movingToTheRight;

	Timer thisTimer = null;
	
	if(movingToTheRight == true){ // INDENT
	    System.out.println("called moving ROW to the right");

	    thisTimer = getTimerMovingRight();
	    for(int i=0; i<9; i++){
		sudokuCells3Before[i] = sudokuCells[row][i];
		sudokuCells3Now[i] = sudokuCells2[row][i];
     
	    }
	
	    for(int i=0; i<9; i++){
		sudokuCells3Now[i].setAlphaZero();
		sudokuCells3Now[i].setNoBoldBorder();
		sudokuCells3Now[i].setNotInTheGrid();

		add(sudokuCells3Now[i], sudokuCells[row][i].getX(), sudokuCells[row][i].getY());

	    }
	} // INDENT
	else if(movingToTheRight == false){
	    System.out.println("called moving ROW to the left");

	    thisTimer = getTimerMovingLeft();
	    
	    for(int i=0; i<9; i++){
		sudokuCells3BeforeLeft[i] = sudokuCells[row][i];
		sudokuCells3NowLeft[i] = sudokuCells2[row][i];
		
	    }
	    
	    for(int i=0; i<9; i++){
		//sudokuCells3Now[i].setAlphaZero();
		sudokuCells3NowLeft[i].setNoBoldBorder();
		sudokuCells3NowLeft[i].setNotInTheGrid();
		
		//add(sudokuCells3NowLeft[i], sudokuCells[row][i].getX(), sudokuCells[row][i].getY());

	    }
	    
	}

	ArrayList<Timer> timers1 = new ArrayList<Timer>();
	timers1.add(thisTimer);
	theModel.timers.add(timers1);
	if(theModel.timers.size() == 1){
	    // otherwise let another timer start this one when it finishes
	    System.out.println("Move ROW model timers = 1, start level");
	    for(Timer t:theModel.timers.get(0)){
		t.start();
	    }
	} else {
	    System.out.println("Move ROW blocked by other animation");
	}
    }

    public void moveBlock(int block, boolean movingToTheRight){
	int x = 0;
	int y = 0;

	if(block == 0){
	    x = 0; y = 0;
	}

	if(block == 1){
	    x = 3; y = 0;
	}

	if(block == 2){
	    x = 6; y = 0;
	}

	if(block == 3){
	    x = 0; y = 3;
	}

	if(block == 4){
	    x = 3; y = 3;
	}

	if(block == 5){
	    x = 6; y = 3;
	}
	
	if(block == 6){
	    x = 0; y = 6;
	}

	if(block == 7){
	    x = 3; y = 6;
	}

	if(block == 8){
	    x = 6; y = 6;
	}

	abandonProgressOnTheRight();
	theModel.viewController.hideWelcomeScreen();

	this.movingToTheRight = movingToTheRight;

	Timer thisTimer = null;
	
	if(movingToTheRight == true){ // INDENT
	    System.out.println("called moving BLOCK to the right");

	    thisTimer = getTimerMovingRight();
	    for(int i=y, k=0; i<y+3; i++){
		for(int j=x; j<x+3 && k<9; j++, k++){
		    sudokuCells3Before[k] = sudokuCells[i][j];
		    sudokuCells3Now[k] = sudokuCells2[i][j];
		    
		    sudokuCells3Now[k].setAlphaZero();
		    sudokuCells3Now[k].setNoBoldBorder();
		    sudokuCells3Now[k].setNotInTheGrid();
		    
		    add(sudokuCells3Now[k], sudokuCells[i][j].getX(), sudokuCells[i][j].getY());
		}
	    }
	} // INDENT
	else if(movingToTheRight == false){
	    System.out.println("called moving ROW to the left");

	    thisTimer = getTimerMovingLeft();
	    
	    for(int i=y, k=0; i<y+3; i++){
		for(int j=x; j<x+3 && k<9; j++, k++){
		    sudokuCells3BeforeLeft[k] = sudokuCells[i][j];
		    sudokuCells3NowLeft[k] = sudokuCells2[i][j];
		    
		    //sudokuCells3Now[k].setAlphaZero();
		    sudokuCells3NowLeft[k].setNoBoldBorder();
		    sudokuCells3NowLeft[k].setNotInTheGrid();
		    
		    //add(sudokuCells3Now[k], sudokuCells[i][j].getX(), sudokuCells[i][j].getY());
		}
	    }
	}
	
	ArrayList<Timer> timers1 = new ArrayList<Timer>();
	timers1.add(thisTimer);
	theModel.timers.add(timers1);
	if(theModel.timers.size() == 1){
	    // otherwise let another timer start this one when it finishes
	    System.out.println("Move (BLOCK) model timers = 1, start level");
	    for(Timer t:theModel.timers.get(0)){
		t.start();
	    }
	} else {
	    System.out.println("Move (BLOCK) blocked by other animation");
	}
	    
    }
    
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
	if(theModel.isAnimationPaused){
	    return;
	}
	
	Timer thisTimer = ((Timer)e.getSource());

	theModel.stopAllTimersOnDiffLevelComparedTo(thisTimer);
	
	theModel.lastRunningTimer = thisTimer;
	
	for(int i=0; i<9; i++){
	    sudokuCells3Now[i].setAlphaOne(); /////////////// !!!!! only call once
	    /*
	    float x1 = sudokuCells3Before[i].getX(); //from
	    float x2 = 600; //to
	    
	    float y1 = sudokuCells3Before[i].getY(); //from
	    float y2 = i * 50 + i * distanceBetweenCells_y; //to

	    float m = (y2-y1)/(x2-x1);
	    float x = 0; // fake value, see below
	    */

	    float x1,x2,y1,y2,m,x;
	    x1 = x2 = y1 = y2 = m = x = 0;
	    if(timersToTheRight.contains(thisTimer)){
		//System.out.println("Event right");
		x1 = sudokuCells3Before[i].getX(); //from
		x2 = 600; //to
		
		y1 = sudokuCells3Before[i].getY(); //from
		y2 = i * 50 + i * distanceBetweenCells_y; //to
		
		m = (y2-y1)/(x2-x1);
		
		x = sudokuCells3Now[i].getX() + DELTA;

		if(x >= 450){
		    sudokuCells3Now[i].setOpaque(true);
		}
		
		//System.out.println("X + DELTA" + x);
		if(x >= 600){
		    x = 600;
		    
		    boolean didAllFinish = true;
		    for(int i2=0; i2<9; i2++){
			if(sudokuCells3Now[i2].getX() < 600){
			    didAllFinish = false;
			}
		    }
		    
		    if(didAllFinish){
			timersToTheRight.remove(thisTimer);
			
			thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
			theModel.removeTimer(thisTimer);
			//System.out.println("Move column finished, wake up next timers");
			theModel.startNextTimers();
		    }
		}
	    } else if(timersToTheLeft.contains(thisTimer)) {
		//System.out.println("Event left");

		setAlphaZeroEdgesCircles();

		x1 = sudokuCells3BeforeLeft[i].getX(); //from
		x2 = 600; //to
		
		y1 = sudokuCells3BeforeLeft[i].getY(); //from
		y2 = i * 50 + i * distanceBetweenCells_y; //to
		
		m = (y2-y1)/(x2-x1);
		/*
		float animation_offset_x = 0;
		
	        if(600 - sudokuCells3NowLeft[i].getX() < 50){
		    // separate a little in the beginning
		    animation_offset_x = DELTA * i * 50.5f;
		}
		*/
		if(x <= 450){
		    sudokuCells3Now[i].setOpaque(false);
		}
		
	        if(i > 0 && 600 - sudokuCells3NowLeft[i-1].getX() < 60){
		    break;
		}
		x = sudokuCells3NowLeft[i].getX() - DELTA /*- animation_offset_x*/;
		//System.out.println("X - DELTA" + x);
		
		if(x < sudokuCells3BeforeLeft[i].getX()){
		    x = sudokuCells3BeforeLeft[i].getX();

		    remove(sudokuCells3NowLeft[i]);
		    removedCellGoingToTheLeft[i] = 1;
		    int count = 0;
		    for(int i3=0; i3<9; i3++){
			if(removedCellGoingToTheLeft[i3] == 1){
			    count++;
			}
		    }
		    
		    if(count == 9){
			//System.out.println("COUNT IS 9");
			timersToTheLeft.remove(thisTimer);
					
			thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
			theModel.removeTimer(thisTimer);
			//System.out.println("Move column finished, wake up next timers");
			theModel.startNextTimers();
		    }
		    /*
		    boolean didAllFinish = true;
		    for(int i2=0; i2<9; i2++){
			if(sudokuCells3NowLeft[i2].getX() > sudokuCells3BeforeLeft[i].getX()){
			    didAllFinish = false;
			}
		    }
		    didAllFinish = false;
		    if(didAllFinish){
			for(int i3=0; i3<9; i3++){
			    remove(sudokuCells3NowLeft[i3]);
			    // set to null?
			}
			
			timersToTheLeft.remove(thisTimer);
					
			thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
			theModel.removeTimer(thisTimer);
			//System.out.println("Move column finished, wake up next timers");
			theModel.startNextTimers();
		    }
		    */
		}
	    }

	    /*
	    if(x >= 600){
		x = 600;

		boolean didAllFinish = true;
		for(int i2=0; i2<9; i2++){
		    if(sudokuCells3Now[i2].getX() < 600){
			didAllFinish = false;
		    }
		}

		if(didAllFinish){
		    thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
		    theModel.removeTimer(thisTimer);
		    //System.out.println("Move column finished, wake up next timers");
		    theModel.startNextTimers();
		}
		
	    }
	    */
	    float b = y1 - m*x1;
	    float y = m * x + b;
	    
	    if(timersToTheRight.contains(thisTimer)){
		//System.out.println("Calling set location x:" + (int)x+ " y:"+y);
		sudokuCells3Now[i].setLocation((int)x, (int)y);
		sudokuCells3Now[i].repaint();
	    } else if(timersToTheLeft.contains(thisTimer)){
		//System.out.println("Calling set location x:" + (int)x+ " y:"+y);
		sudokuCells3NowLeft[i].setLocation((int)x, (int)y);
		sudokuCells3NowLeft[i].repaint();
	    }
	}
	    
	repaint(); // try to remove this
    }

    public void abandonProgressOnTheRight(){
	/*
	for(int i=0; i<9; i++){
	    // check to see if there was something on the right of the screen
	    // if so, remove it
	    if(sudokuCells3Now[i] != null){
		remove(sudokuCells3Now[i]);
		sudokuCells3Before[i] = null;
		sudokuCells3Now[i] = null;
	    }
	}
	*/
	// reshow label on the right

	// stop all current playing animations !!!!! ()

	// SHOULD I LOOP AND STOP THEM?
	//theModel.timers.clear(); ///////////////////////////
    }

    public void setAlphaZeroEdgesCircles(){
	//theModel.viewController.theEdges.setAlphaZero();
	theModel.viewController.theEdges.setVisible(false);
	
	for(int i = 0; i < 11; i++){
	    valueCircles[i].setAlphaZero();
	}
    }

    public Timer getTimerMovingRight(){
	Timer timer1 = new Timer(10, null); //moving right
	timer1.setInitialDelay(100);
        timer1.addActionListener(this);
	timer1.setCoalesce(false);

	timersToTheRight.add(timer1);
	
	return timer1;
    }

    public Timer getTimerMovingLeft(){
	Timer timer2 = new Timer(10, null); //moving left
	timer2.setInitialDelay(100);
        timer2.addActionListener(this);
	timer2.setCoalesce(false);

	timersToTheLeft.add(timer2);
	
	return timer2;	
    }
}
	     
