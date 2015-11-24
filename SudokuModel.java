import java.io.*;
import java.util.*;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.Color;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.exception.ContradictionException;

public class SudokuModel {
    public SudokuGrid theGrid; // TODO update the name // GUI
    public static int SIZE;
    public static int SLEEP;
    public static int SLEEP_BETWEEN_STEPS = 3000;
    
    private static Stack<SudokuWorld> worldStack = new Stack<SudokuWorld>();

    private Solver solver = new Solver("sudoku");
    private IntVar[][] rows = VariableFactory.enumeratedMatrix("rows", 9, 9, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, solver); // TODO update the name

    ArrayList<ArrayList<Timer>> timers = new ArrayList<ArrayList<Timer>>();

    public Timer lastRunningTimer;
    public boolean isAnimationPaused = false;

    public ViewController viewController;

    public void SudokuModel(){
	worldStack = new Stack<SudokuWorld>();
    }

    public void worldPush(SudokuWorld world){
	worldStack.push(world);
	redraw();
    }

    public void worldPop(){
	worldStack.pop();
	redraw();
    }

    public SudokuWorld worldPeek(){
	return worldStack.peek();
    }

    // TODO Namechange
    // TODO SHOULD I STORE THE GUI in the SudokuWorld
    public void setGrid(SudokuGrid theGrid){
	this.theGrid = theGrid; // GUI
    }

    static boolean[][] differentThan (Var x, Var y){
	int n = x.domain.size();
	int m = y.domain.size();
	boolean[][] differentThan = new boolean[n][m];
	for (int i=0;i<n;i++)
	    for (int j=0;j<m;j++)
		differentThan[i][j] = i!=j;
	return differentThan;
    } // x != y

    public boolean propagate(){
	ArrayList<Constraint> constraints = worldStack.peek().constraints;
	
	boolean consistent = true;
	Stack<Constraint> S = new Stack<Constraint>();
	for (Constraint c : constraints){ // add all the constraints on the stack
	    //System.out.println("Push " + c.name);
	    S.push(c); c.flag = true;
	}
	
	while (consistent && !S.isEmpty()){
	    Constraint c = S.pop();
	    //System.out.println("Pop " + c.name);
	    c.flag = false;
	    if (c.revise()){
		redraw();
		consistent = c.v1.domain.cardinality() > 0;
		for (Constraint cv1 : c.v1.constraints){
		    if (!cv1.flag){
			//System.out.println("Push from var " + cv1.name);
			S.push(cv1); cv1.flag = true;
		    }
		}
	    }
	}
	
	return consistent;
    }

    public void scheduleSolveFF(){
	System.out.println("Is scheduleSolve EDT? " + SwingUtilities.isEventDispatchThread());	

	ArrayList<Timer> timers81 = new ArrayList<Timer>();

	Timer timerSolveGraph = viewController.theEdges.getTimerSolve();
	timers81.add(timerSolveGraph);
	timerSolveGraph.start();

	timers.add(timers81);
	
	if(timers.size() == 1){ // if this is the only timer, start it
	    // otherwise let another timer start this one when it finishes
	    //System.out.println("Fadeout model timers = 1, start level");
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	} else {
	    //System.out.println("Fade in blocked by other animation");
	}
	
    }

    public SudokuModel super_getThis(){
	return this;
    }

    public boolean solveFF(){
	//viewController.theEdges.DO_NOT_REFRESH = false; ////// !!!!!
	viewController.theEdges.setIgnoreRepaint(false);
	
	System.out.println("Solving using FF");
	System.out.println("Is solveFF EDT? " + SwingUtilities.isEventDispatchThread());
	/*
	try{
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
	*/
	int edges = 0;
	int n = 20; // we have 20 vertices
	int[][] A = new int[n][n];
	int[][] R = new int[n][n];
	
	for(int i=0; i<9; i++){
	    //edges from source to vars (where each var represents a sudoku cell)
	    edges++;
	    A[0][i+1] = 1;
	    R[0][i+1] = 1;
	}
	
	for(int i=0; i<9; i++){
	    //edges from values (9 circles with one number on them) to sink
	    edges++;
	    A[i + 9 + 1][19] = 1;
	    R[i + 9 + 1][19] = 1;
	}
	
	for(int i=0; i<9; i++){
	    for(int j=0; j< 9; j++){
		int row = 8; ////////////
		
		// from VAR i to VALUE j
		int shown_x = theGrid.sudokuCells3Now[i].x;
		int shown_y = theGrid.sudokuCells3Now[i].y;
		//int shown_x = row;
		//int shown_y = i;
		int capacity = 0;
		if(worldPeek().grid[shown_x][shown_y].hasValue(j + 1)){
		    capacity = 1;
		}
		
		edges++;
		A[i+1][j + 9 + 1] = capacity; ///////////// WAS 1 and it was working
		R[i+1][j + 9 + 1] = capacity;
	    }
	}

	System.out.println("Printing A");
	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		System.out.print(A[i][j] + " ");
	    }
	    System.out.println();
	}
	System.out.println("Ended printing A");

	
	System.out.println("Edges " + edges);
	FordFulkerson ff = new FordFulkerson(super_getThis(), A, R, 20);
	System.out.println("FF started");
	ff.run();
	System.out.println("Step 1 - FF finished");
	//viewController.theEdges.applyDrawing(); // apply last green/red
	
	viewController.theEdges.drawMoves();
	
	viewController.theEdges.finishDrawing(false); // hide unmatched paths
	viewController.theGrid.finishDrawing(); // hide S/T + S/T edges

	System.out.println("Printing R");
	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		System.out.print(R[i][j] + " ");
	    }
	    System.out.println();
	}
	System.out.println("Ended printing R");
	
	int newA[][] = new int[20][20];
	int newA2[][] = new int[18][18];

	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		if(i > 0 && i < 10 && A[i][j] == 1 && A[i][j] * R[j][i] == 1){
		    System.out.println("Edge i: "+(i)+" j:"+(j));
		    newA[i][j] = 1;
		}
	    }
	}

	System.out.println("printing newA");

       	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		System.out.print(newA[i][j] + " ");
	    }
	    System.out.println();
	}

	System.out.println("finished printing newA");

	for(int i=0; i<18; i++){
	    for(int j=0; j<18; j++){
		newA2[i][j] = A[i+1][j+1];
	    }
	}
	
	for(int i=0; i<18; i++){
	    for(int j=0; j<18; j++){
		if(newA[i+1][j+1] == 1){
		    newA2[i][j] = 0;
		    newA2[j][i] = 1;
		}
	    }
	}
	/*
	//PRESENTATION EXAMPLE
	for(int i=0; i<18; i++){
	    for(int j=0; j<18; j++){
		newA2[i][j] = 0;
		// wasted right to left
		if(i == 0 && j == 16) newA2[i][j] = 1;
		if(i == 1 && j == 10) newA2[i][j] = 1;
		if(i == 2 && j == 11) newA2[i][j] = 1;
		if(i == 3 && j == 10) newA2[i][j] = 1;
		if(i == 3 && j == 13) newA2[i][j] = 1;
		if(i == 4 && j == 12) newA2[i][j] = 1;
		if(i == 4 && j == 13) newA2[i][j] = 1;
		if(i == 5 && j == 12) newA2[i][j] = 1;
		if(i == 5 && j == 14) newA2[i][j] = 1;
		if(i == 6 && j == 10) newA2[i][j] = 1;
		if(i == 6 && j == 17) newA2[i][j] = 1;
		if(i == 7 && j == 11) newA2[i][j] = 1;
		if(i == 7 && j == 15) newA2[i][j] = 1;
		if(i == 8 && j == 10) newA2[i][j] = 1;
		if(i == 8 && j == 11) newA2[i][j] = 1;
		if(i == 8 && j == 13) newA2[i][j] = 1;
		if(i == 8 && j == 16) newA2[i][j] = 1;
		
		// assignments left to right
		if(i == 9 && j == 0) newA2[i][j] = 1;
		if(i == 10 && j == 2) newA2[i][j] = 1;
		if(i == 11 && j == 1) newA2[i][j] = 1;
		if(i == 12 && j == 3) newA2[i][j] = 1;
		if(i == 13 && j == 5) newA2[i][j] = 1;
		if(i == 14 && j == 4) newA2[i][j] = 1;
		if(i == 15 && j == 6) newA2[i][j] = 1;
		if(i == 16 && j == 7) newA2[i][j] = 1;
		if(i == 17 && j == 8) newA2[i][j] = 1;
	    }
	}
	*/
	System.out.println("Printing step 2 arrow flows after maximum matching\n");
	
	for(int i=0; i<18; i++){
	    System.out.print(i+": ");
	    for(int j=0; j<18; j++){
		if(newA2[i][j] == 1){
		    System.out.print(j + " ");
		    //System.out.print(newA2[i][j] + " ");
		} else {
		    //System.out.print("0 ");
		}
	    }
	    System.out.println();
	}

       	System.out.println("\nStep 2 finished");

	try{
	    Thread.sleep(SLEEP_BETWEEN_STEPS);
	} catch (Exception e){
	    e.printStackTrace();
	}
	
	viewController.theEdges.makeAllEdgesGray();
	
	Tarjan tarjan = new Tarjan(this, newA2, 18);
	ArrayList<ArrayList<Integer>> components = tarjan.run();
	/*
	try{
	    Thread.sleep(SLEEP_BETWEEN_STEPS);
	} catch (Exception e){
	    e.printStackTrace();
	}
	*/
	System.out.println("Step 3 - Tarjan finished");

	int newA3[][] = new int[18][18];
	
	for(int u=0; u<18; u++){
	    for(int v=0; v<18; v++){
		if(newA2[u][v] == 0){
		    continue;
		}
		
		int start_component_id = tarjan.id(u);
		int finish_component_id = tarjan.id(v);
		
		//System.out.println("u: " + u + " v: " + v + " sC: " + start_component_id + " fC: " + finish_component_id);

		if(start_component_id != finish_component_id){
		    System.out.println("Considering u: " + u + " v: " + v + " sC: " + start_component_id + " fC: " + finish_component_id);
		    newA3[u][v] = 1;
		}
	    }
	}
		
	try{
	    SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			/////////////////
			for(int u=0; u<18; u++){
			    for(int v=0; v<18; v++){
				if(newA3[u][v] == 0){
				    continue;
				}
				
				if(u<9){
				    //System.out.println("Delete u: " + u + " v:" + v);
				    
				    int shown_x = theGrid.sudokuCells3Before[u].x;
				    int shown_y = theGrid.sudokuCells3Before[u].y;
				    System.out.println("REDDD");
				    viewController.theEdges.edgeColors[u+1][v+1] = Color.RED;
				    
				} else if(u>8){
				    //System.out.println("Assign v: " + v + " u:" + u); // switch
				    
				    int shown_x = theGrid.sudokuCells3Before[v].x;
				    int shown_y = theGrid.sudokuCells3Before[v].y;
				    System.out.println("BLUUU");
				    viewController.theEdges.edgeColors[v+1][u+1] = Color.BLUE;
				    
				}
			    }
			}
			
			viewController.theEdges.repaint();
			/////////////////
		    }
		});
	} catch (Exception e){
	    e.printStackTrace();
	}
	
	//viewController.theEdges.DO_NOT_REFRESH = true; ////// !!!!!
	//viewController.theEdges.setIgnoreRepaint(true);
	
	try{
	    Thread.sleep(SLEEP_BETWEEN_STEPS);
	} catch (Exception e){
	    e.printStackTrace();
	}

	try{
	    SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			////////////////
			for(int u=0; u<18; u++){
			    for(int v=0; v<18; v++){
				if(newA3[u][v] == 0){
				    continue;
				}
				
				if(u<9){
				    System.out.println("Delete u: " + u + " v:" + v);
				    
				    int shown_x = theGrid.sudokuCells3Before[u].x;
				    int shown_y = theGrid.sudokuCells3Before[u].y;
				    
				    // delete from sudoku
				    worldPeek().grid[shown_x][shown_y].eliminateValue(v - (9-1));
				    
				    // TODO: in one call
				    theGrid.sudokuCells3Now[u].setValuesLabel(theGrid.sudokuCells3Now[u].formatPossibleValues());
				    theGrid.sudokuCells3Before[u].setValuesLabel(theGrid.sudokuCells3Before[u].formatPossibleValues());
				    
				} else if(u>8){
				    System.out.println("Assign v: " + v + " u:" + u); // switch
				    
				    int shown_x = theGrid.sudokuCells3Before[v].x;
				    int shown_y = theGrid.sudokuCells3Before[v].y;
				    
				    // delete from sudoku
				    worldPeek().grid[shown_x][shown_y].setValue(u - (9-1));
				    
				    // TODO: in one call
				    theGrid.sudokuCells3Now[v].setValuesLabel(theGrid.sudokuCells3Now[v].formatPossibleValues());
				    theGrid.sudokuCells3Before[v].setValuesLabel(theGrid.sudokuCells3Before[v].formatPossibleValues());
				    
				}
			    }
			}
			
			// WANT TO REMOVE THE RED EDGES? THEN UNCOMMENT
			//viewController.theEdges.repaint(); // VERY INTERESTING, KEEP THIS COMMENT
			
			//viewController.theEdges.makeAllEdgesGray();
			////////////////
		    }
		});
	} catch (Exception e){
	    e.printStackTrace();
	}

	
		
	
	System.out.println("Step 4 finished");

	//propagate();
	
	// END
	/*
	  }});
	  } catch (Exception e4){
	  e4.printStackTrace();
	  }
	*/
	return true; // not important yet
    }
    
    public boolean solveUsingBacktracking(SudokuWorld sw){
	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++){
		if (sw.grid[i][j].getValue() != -1) {
		    continue;
		}
		
		for (int num = 1; num <= SIZE; num++) {
		    SudokuWorld newWorld = new SudokuWorld(worldStack.peek());
		    newWorld.grid[i][j].setValue(num);
		    worldPush(newWorld);
		    
		    if (propagate()){
			if (solveUsingBacktracking(newWorld)) {
			    return true;
			} else {
			    worldPop();
			}
		    } else {
			worldPop();
		    }
		}
		
		return false;
	    }
	}
	
	return true;
    }

    public void solveUsingChoco3() {
	redraw();
	postConstraintsChoco3(solver);
	redraw();

	solver.findSolution();
	saveChoco3ToModel();
    }

    public void saveChoco3ToModel(){
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		IntVar[] row = rows[i];
		IntVar col = row[j];

		// delete all known values and set them again
		// with the data from choco3
		worldStack.peek().grid[i][j].eliminateAll();
		
		for(int k=0; k<SIZE; k++){
		    if(col.contains(k+1)){
			worldStack.peek().grid[i][j].addValue(k+1);
		    }
		}
	    }
	}
    }

    public void postConstraints(){
	SudokuWorld world =  worldStack.peek();
	
	// Not equal constraint between each case of a row
      	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		for (int k = j; k < SIZE; k++) {
		    if (k != j) {
			Constraint c = new Constraint(world.grid[i][j], differentThan(world.grid[i][j], world.grid[i][k]), world.grid[i][k], ""+i+""+j+"!="+i+""+k);
			world.constraints.add(c);

			Constraint c1 = new Constraint(world.grid[i][k], differentThan(world.grid[i][k], world.grid[i][j]), world.grid[i][j], ""+i+""+k+"!="+i+""+j);
			world.constraints.add(c1);
		    }
		}
	    }
	}
    
	// Not equal constraint between each case of a column
      	for (int j = 0; j < SIZE; j++) {
	    for (int i = 0; i < SIZE; i++) {
		for (int k = 0; k < SIZE; k++) {
		    if (k != i) {
			Constraint c = new Constraint(world.grid[i][j], differentThan(world.grid[i][j], world.grid[k][j]), world.grid[k][j], ""+i+""+j+"!="+k+""+j);
			world.constraints.add(c);

			Constraint c1 = new Constraint(world.grid[k][j], differentThan(world.grid[k][j], world.grid[i][j]), world.grid[i][j], ""+k+""+j+"!="+i+""+j);
			world.constraints.add(c1);
		    }
		}
	    }
	}
	
	// Not equal constraint between each case of a sub region
	for (int ci = 0; ci < SIZE; ci += 3) {
	    for (int cj = 0; cj < SIZE; cj += 3) {
		// Extraction of disequality of a sub region
             	for (int i = ci; i < ci + 3; i++) {
		    for (int j = cj; j < cj + 3; j++) {
			for (int k = ci; k < ci + 3; k++) {
			    for (int l = cj; l < cj + 3; l++) {
				if (k != i || l != j) {
				    Constraint c = new Constraint(world.grid[i][j], differentThan(world.grid[i][j], world.grid[k][l]), world.grid[k][l], ""+i+""+j+"!="+k+""+l);
				    world.constraints.add(c);

				    Constraint c1 = new Constraint(world.grid[k][l], differentThan(world.grid[k][l], world.grid[i][j]), world.grid[i][j], ""+k+""+l+"!="+i+""+j);
				    world.constraints.add(c1);
				}
			    }
			}
		    }
		}
	    }
	}
    }

    public void postConstraintsChoco3(Solver solver){
	int index = 0;
	
	// Row constraints
	for (int i = 0; i < SIZE; i++) {
	    solver.post(ICF.alldifferent(rows[i],"AC"));
	}
	
	// Column constraints
	IntVar[] columnVars = new IntVar[9];
	index = 0;
	
	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		columnVars[index] = rows[j][i];
		index++;
	    }
	    solver.post(ICF.alldifferent(columnVars,"AC"));
	    columnVars = new IntVar[9];
	    index = 0;
	}
	
	// Block constraints
	IntVar[] blockVars = new IntVar[9];
	index = 0;
	
	for(int i=0; i<SIZE; i+=3){
	    for(int j=0; j<SIZE; j+=3){
		for(int k=i; k<i+3; k++){
		    for(int l=j; l<j+3; l++){
			blockVars[index] = rows[k][l];
			index++;
		    }
		}
		solver.post(ICF.alldifferent(blockVars, "AC"));
		blockVars = new IntVar[9];
		index = 0;
	    }
	}
	
    	// Populate choco3 with the given values
	for(int i=0; i<SIZE; i++){
	    for(int j=0; j<SIZE; j++){
		if(worldPeek().grid[i][j].getValue() != -1){
		    try{
			rows[i][j].instantiateTo(worldPeek().grid[i][j].getValue(),null);
		    } catch (Exception e){
			e.printStackTrace();
		    }
		    
		    //solver.post(ICF.arithm(rows[i][j],"=", VF.fixed(i+""+j, worldPeek().grid[i][j].getValue(), solver)));
		    
		    // WHY ISN'T THIS WORKING?
		    //rows[i][j] = VF.fixed(i+"fixed"+j, worldPeek().grid[i][j].getValue(), solver);

		}
	    }
	}
    }

    public SudokuWorld readFromFile(String fileName) throws FileNotFoundException {
	SudokuWorld world = new SudokuWorld();
	
	Scanner sc = new Scanner(new FileReader(fileName));
	
 	for(int i = 0; i < 9; i++) {
	    if (sc.hasNextLine()) {
		for (int j = 0; j < 9; j++) {
		    if (sc.hasNextInt()) {
			int value = sc.nextInt();
			if(value != 0){
			    world.grid[i][j].setValue(value);
			    world.grid[i][j].setName(i+""+j);
			}
		    }
		}
	    }
	}

	sc.close();

	worldPush(world);
	redraw();

	postConstraints(); // TODO do we want this?
	
	return world;
    }

    public void redraw(){
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		ArrayList<Integer> possibleValues = worldPeek().grid[i][j].getPossibleValues();
		this.theGrid.sudokuCells[i][j].possibleValues = possibleValues;
	    }
	}

	// Separate loop so we can just redraw everything after knowing the values
	
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		// TODO do it in one call in SudokuCell
		this.theGrid.sudokuCells[i][j].setValuesLabel(this.theGrid.sudokuCells[i][j].formatPossibleValues());
		if(this.theGrid.sudokuCells2[i][j] != null){
		    this.theGrid.sudokuCells2[i][j].setValuesLabel(this.theGrid.sudokuCells2[i][j].formatPossibleValues());
		}
	    }
	}

	///////////////////////////////////////
	try {
	    Thread.sleep(SLEEP);
	} catch(InterruptedException ex) {
	    Thread.currentThread().interrupt();
	}
	///////////////////////////////////////
    }

    public void fadeOutAllExceptRow(int row){
	ArrayList<Timer> timers81 = new ArrayList<Timer>();

	viewController.hideWelcomeScreen();
	
	// wait for it to finish
	for(int i=0; i<9; i++){
	    if(i == row){
		// skip the row (it was an exception from fading out)
		continue;
	    }
	    for(int j=0; j<9; j++){
		//System.out.println("Is model EDT " + SwingUtilities.isEventDispatchThread());
		Timer timerFadeOut = theGrid.sudokuCells[i][j].getTimerFadeOut();
		timers81.add(timerFadeOut); //timer 1 = fadeout
		theGrid.sudokuCells[i][j].fadeOutALittle();
		timerFadeOut.start();
	    }
	}

	//////// adding the circles
	for(int i = 0; i <11; i++){
	    Timer timerFadeOut = theGrid.valueCircles[i].getTimerFadeOut();
	    timers81.add(timerFadeOut);
	    theGrid.valueCircles[i].fadeOut();
	    timerFadeOut.start();
	}
	//////// end adding the circles

	/////// adding lines
	Timer timerFadeOut = viewController.theEdges.getTimerFadeOut();
	timers81.add(timerFadeOut);
	viewController.theEdges.fadeOut();
	timerFadeOut.start();
	/////// end adding the lines

	timers.add(timers81);
	
	if(timers.size() == 1){ // if this is the only timer, start it
	    // otherwise let another timer start this one when it finishes
	    //System.out.println("Fadeout model timers = 1, start level");
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	} else {
	    // System.out.println("Fadeout blocked by other animation");
	}
    }

        public void fadeOutAllExceptColumn(int column){
	ArrayList<Timer> timers81 = new ArrayList<Timer>();

	viewController.hideWelcomeScreen();
	
	// wait for it to finish
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		if(j == column){
		    // skip the column (it was an exception from fading out)
		    continue;
		}
		//System.out.println("Is model EDT " + SwingUtilities.isEventDispatchThread());
		Timer timerFadeOut = theGrid.sudokuCells[i][j].getTimerFadeOut();
		timers81.add(timerFadeOut); //timer 1 = fadeout
		theGrid.sudokuCells[i][j].fadeOutALittle();
		timerFadeOut.start();
	    }
	}

	//////// adding the circles
	for(int i = 0; i <11; i++){
	    Timer timerFadeOut = theGrid.valueCircles[i].getTimerFadeOut();
	    timers81.add(timerFadeOut);
	    theGrid.valueCircles[i].fadeOut();
	    timerFadeOut.start();
	}
	//////// end adding the circles

	/////// adding lines
	Timer timerFadeOut = viewController.theEdges.getTimerFadeOut();
	timers81.add(timerFadeOut);
	viewController.theEdges.fadeOut();
	timerFadeOut.start();
	/////// end adding the lines
	
	timers.add(timers81);
	
	if(timers.size() == 1){ // if this is the only timer, start it
	    // otherwise let another timer start this one when it finishes
	    //System.out.println("Fadeout model timers = 1, start level");
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	} else {
	    // System.out.println("Fadeout blocked by other animation");
	}
    }

    public void fadeOutAllExceptBlock(int block){
	ArrayList<Timer> timers81 = new ArrayList<Timer>();

	viewController.hideWelcomeScreen();

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
	
	// wait for it to finish
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		if((y<=i && i<y+3) && (x<=j && j<x+3)){
		    // skip the block (it was an exception from fading out)
		    continue;
		}
		//System.out.println("Is model EDT " + SwingUtilities.isEventDispatchThread());
		Timer timerFadeOut = theGrid.sudokuCells[i][j].getTimerFadeOut();
		timers81.add(timerFadeOut); //timer 1 = fadeout
		theGrid.sudokuCells[i][j].fadeOutALittle();
		timerFadeOut.start();
	    }
	}

	//////// adding the circles
	for(int i = 0; i <11; i++){
	    Timer timerFadeOut = theGrid.valueCircles[i].getTimerFadeOut();
	    timers81.add(timerFadeOut);
	    theGrid.valueCircles[i].fadeOut();
	    timerFadeOut.start();
	}
	//////// end adding the circles

	/////// adding lines
	Timer timerFadeOut = viewController.theEdges.getTimerFadeOut();
	timers81.add(timerFadeOut);
	viewController.theEdges.fadeOut();
	timerFadeOut.start();
	/////// end adding the lines

	timers.add(timers81);
	
	if(timers.size() == 1){ // if this is the only timer, start it
	    // otherwise let another timer start this one when it finishes
	    //System.out.println("Fadeout model timers = 1, start level");
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	} else {
	    // System.out.println("Fadeout blocked by other animation");
	}
    }    

    public void fadeIn(){
	ArrayList<Timer> timers81 = new ArrayList<Timer>();
	
	// wait for it to finish
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		//System.out.println("Is model EDT " + SwingUtilities.isEventDispatchThread());
		Timer timerFadeIn = theGrid.sudokuCells[i][j].getTimerFadeIn();
		timers81.add(timerFadeIn); //timer 2 = fadein
		theGrid.sudokuCells[i][j].fadeIn();
		timerFadeIn.start();
	    }
	}

	//////// adding the circles
	for(int i = 0; i <11; i++){
	    Timer timerFadeIn = theGrid.valueCircles[i].getTimerFadeIn();
	    timers81.add(timerFadeIn);
	    theGrid.valueCircles[i].fadeIn();
	    timerFadeIn.start();
	}
	//////// end adding the circles
	
	/////// adding lines
	Timer timerFadeIn = viewController.theEdges.getTimerFadeIn();
	timers81.add(timerFadeIn);
	viewController.theEdges.fadeIn();
	timerFadeIn.start();
	/////// end adding the lines

	timers.add(timers81);
	
	if(timers.size() == 1){ // if this is the only timer, start it
	    // otherwise let another timer start this one when it finishes
	    //System.out.println("Fadeout model timers = 1, start level");
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	} else {
	    //System.out.println("Fade in blocked by other animation");
	}
    }

    public void fadeInGraph(){ ///////// COMASEAZA ASTA
	ArrayList<Timer> timers81 = new ArrayList<Timer>();
	/*
	// wait for it to finish
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		//System.out.println("Is model EDT " + SwingUtilities.isEventDispatchThread());
		Timer timerFadeIn = theGrid.sudokuCells[i][j].getTimerFadeIn();
		timers81.add(timerFadeIn); //timer 2 = fadein
		theGrid.sudokuCells[i][j].fadeIn();
		timerFadeIn.start();
	    }
	}
	*/
	//////// adding the circles
	for(int i = 0; i <11; i++){
	    Timer timerFadeIn = theGrid.valueCircles[i].getTimerFadeIn();
	    timers81.add(timerFadeIn);
	    theGrid.valueCircles[i].fadeIn();
	    timerFadeIn.start();
	}
	//////// end adding the circles
	
	/////// adding lines
	Timer timerFadeIn = viewController.theEdges.getTimerFadeIn();
	timers81.add(timerFadeIn);
	viewController.theEdges.fadeIn();
	timerFadeIn.start();
	/////// end adding the lines

	timers.add(timers81);
	
	if(timers.size() == 1){ // if this is the only timer, start it
	    // otherwise let another timer start this one when it finishes
	    //System.out.println("Fadeout model timers = 1, start level");
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	} else {
	    //System.out.println("Fade in blocked by other animation");
	}
    }
    
    // NOTE: USES WORLDPEEK() ; DO YOU WANT OLDER ONES?
    public ArrayList<Integer> getPossibleValues(int i, int j){
	ArrayList<Integer> possibleValues = new ArrayList<Integer>();
	if(worldPeek() != null){
	    if(worldPeek().grid[i][j] != null){
		possibleValues = worldPeek().grid[i][j].getPossibleValues();
	    }
	}
	
	return possibleValues;
    }

    public void stopAllTimersOnDiffLevelComparedTo(Timer t){
	for(ArrayList<Timer> sameTimeTimers:timers){
	    if(!sameTimeTimers.contains(t)){
		for(Timer t2:sameTimeTimers){
		    t2.stop();
		}
	    }
	}
    }

    public void removeTimer(Timer t){
	for(ArrayList<Timer> sameTimeTimers:timers){
	    if(sameTimeTimers.contains(t)){
		sameTimeTimers.remove(t);
	    }
	}
    }

    public void startNextTimers(){
	// First, clear the empty levels

	ArrayList<ArrayList<Timer>> valuesToRemove = new ArrayList<ArrayList<Timer>>();
	
	Iterator<ArrayList<Timer>> it = timers.iterator();
	while (it.hasNext()) {
	    ArrayList<Timer> sameTimeTimers = it.next(); 
	    if (sameTimeTimers.isEmpty()) {
		//it.remove();
		//timers.remove(sameTimeTimers);
		valuesToRemove.add(sameTimeTimers);
	    }
	}

	//System.out.println("Inside start next timers, timers.size():" + timers.size());
	timers.removeAll(valuesToRemove);
	if(valuesToRemove.size() == 0){
	    //System.out.println("Return");
	    // we have removed a fading cell for example, but others still exist
	    // i.e. the level still has something
	    return;
	}
	//System.out.println("After removal timers.size():" + timers.size());
	/*
	for(ArrayList sameTimeTimers:timers){
	    if(sameTimeTimers.isEmpty()){
		timers.remove(sameTimeTimers);
	    }
	}
	*/
	if(!timers.isEmpty()){
	    for(Timer t:timers.get(0)){
		t.start();
	    }
	}
    }

    public void pauseAnimation(){
	isAnimationPaused = true;
	// Pause the concurrentTimers that contain the last running timer
	System.out.println("Pause");
	for(ArrayList<Timer> concurrentTimers:timers){
	    if(concurrentTimers.contains(lastRunningTimer)){
		for(Timer t:concurrentTimers){
		    t.stop();
		}
	    }
	}
    }

    public void playAnimation(){
	isAnimationPaused = false;
	// Play the concurrentTimers that contain the last running timer that was paused
	System.out.println("Play");
	for(ArrayList<Timer> concurrentTimers:timers){
	    if(concurrentTimers.contains(lastRunningTimer)){
		for(Timer t:concurrentTimers){
		    t.start();
		}
	    }
	}
    }

    public void moveColumn(int column, boolean movingToTheRight){
	theGrid.moveColumn(column, movingToTheRight); // i don't like this
    }

    public void moveRow(int row, boolean movingToTheRight){
	theGrid.moveRow(row, movingToTheRight); // i don't like this
    }

    public void moveBlock(int block, boolean movingToTheRight){
	theGrid.moveBlock(block, movingToTheRight); // i don't like this
    }

    public void setViewController(ViewController vc){
	this.viewController = vc;
    }

    public void moveLeft(){
	if(theGrid.lastSelectionWasRow){
	    theGrid.moveRow(theGrid.lastSelectionNumber, false);
	}

	if(theGrid.lastSelectionWasColumn){
	    theGrid.moveColumn(theGrid.lastSelectionNumber, false);
	}
	
	if(theGrid.lastSelectionWasBlock){
	    theGrid.moveBlock(theGrid.lastSelectionNumber, false);
	}

	viewController.theEdges.edgeColors  = new Color[20][20];

	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		//initially all edges are gray
		viewController.theEdges.edgeColors[i][j] = Color.LIGHT_GRAY;
	    }
	}

	for(int i=1; i<10; i++){
	    // initially all circles have a black background with white text
	    theGrid.valueCircles[i].setCircleColor(Color.BLACK);
	    theGrid.valueCircles[i].setFontColor(Color.WHITE);
	}

	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		// initially all vars have no background with black text
		theGrid.sudokuCells2[i][j].setBackground(null);
		theGrid.sudokuCells2[i][j].setFontColor(Color.BLACK);
	    }
	}
	
	viewController.theEdges.moves = new ArrayList<TripletIIB>();
	this.timers = new ArrayList<ArrayList<Timer>>();
	theGrid.removedCellGoingToTheLeft = new int[9];
    }

    public boolean propagateOnAssignment(int shown_x, int shown_y){
	/*
	ArrayList<Constraint> constraints = worldPeek().grid[shown_x][shown_y].constraints; // participating constaints
	
	boolean consistent = true;
	Stack<Constraint> S = new Stack<Constraint>();
	for (Constraint c : constraints){ // add all the constraints on the stack
	    //System.out.println("Push " + c.name);
	    S.push(c); c.flag = true;
	}
	
	while (consistent && !S.isEmpty()){
	    Constraint c = S.pop();
	    //System.out.println("Pop " + c.name);
	    c.flag = false;
	    if (c.revise()){
		redraw();
		consistent = c.v1.domain.cardinality() > 0;
		///////
		for (Constraint cv1 : c.v1.constraints){
		    if (!cv1.flag){
			//System.out.println("Push from var " + cv1.name);
			S.push(cv1); cv1.flag = true;
		    }
		}
		///////
	    }
	}
	
	return consistent;
	*/
	
	return propagate();
    }
}
