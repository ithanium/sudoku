import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

import java.awt.Color;
import java.awt.*;

import java.util.List;

public class Sudoku{
    public static final int SIZE = 9;
    public static final int SLEEP = 1;
        
    public static SudokuModel theModel;

    public static JButton openFileButton;
    public static JButton backtrackButton;
    public static JButton nextButton;
    public static JButton pauseButton;
    public static JButton playButton;
    public static JButton choco3Button;
    public static JButton printButton;
    public static JButton solveFFButton;
    
    public static void showGUI(){
	JFrame mainFrame = new JFrame("sudo ku");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 675);
        mainFrame.setLocationRelativeTo(null);

	Box box = new Box(BoxLayout.Y_AXIS);
	
	JPanel theGridPlaceHolder = new JPanel();
	theGridPlaceHolder.setSize(1000,675); /////// CHECK THE SIZES
	//theGridPlaceHolder.setBackground(Color.WHITE);////////////////////////
	//SudokuGrid theGrid = new SudokuGrid(theModel, SIZE, SIZE);
	//theGrid.setModel(theModel);
	ViewController vc = new ViewController(theModel);
	//vc.setModel(theModel);
	
	//theGridPlaceHolder.add(theGrid);
	theGridPlaceHolder.add(vc);
	box.add(Box.createVerticalGlue());
	box.add(theGridPlaceHolder);
	box.add(Box.createVerticalGlue());
	
	mainFrame.add(box);

	openFileButton = new JButton("Open");
	openFileButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(openFileButton);

	backtrackButton = new JButton("Backtrack Solve");
	backtrackButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(backtrackButton);

	nextButton = new JButton("Start");
	nextButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(nextButton);

	pauseButton = new JButton("Pause");
	pauseButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(pauseButton);

	playButton = new JButton("Play");
	playButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(playButton);
	
	choco3Button = new JButton("Choco3 Solve");
	choco3Button.addActionListener(new MouseListener());
	theGridPlaceHolder.add(choco3Button);
	
	printButton = new JButton("Print statement");
	printButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(printButton);

	solveFFButton = new JButton("Solve FF");
	solveFFButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(solveFFButton);
	
	//theModel.setGrid(theGrid);
	
	mainFrame.setVisible(true);
	//System.out.println("TEST0");
	//System.out.println("Should be true " + SwingUtilities.isEventDispatchThread());
    }
    
    public static void main(String args[]){
	theModel = new SudokuModel();
	theModel.SIZE = SIZE;
	theModel.SLEEP = SLEEP;
	
	Runnable r = new Runnable() {
		public void run() {
		    //System.out.println("TEST1");
		    //System.out.println("Should be false " + SwingUtilities.isEventDispatchThread());
		    
		    // Begin trick to load the initial file
		    File workingDirectory = new File(System.getProperty("user.dir"));
		    
		    try {
			// easy file
			//theModel.readFromFile(workingDirectory.getAbsolutePath() + "/herald20061222E.txt");
			// hard file
			theModel.readFromFile(workingDirectory.getAbsolutePath() + "/herald20061222H.txt");
		    } catch (FileNotFoundException ex) {
			ex.printStackTrace();
		    }
		    // End trick to load the initial file
		}
	    };

	try{
	    SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			showGUI();
		    }
		});
	} catch (Exception e){
	    e.printStackTrace();
	}
	
	r.run();	
    }

    private static class MouseListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {

	    // Handle open button action
	    if (e.getSource() == openFileButton) {

		JFileChooser fc = new JFileChooser();
        
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
        
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();

		    try {
			theModel.readFromFile(file.getAbsolutePath());
		    } catch (FileNotFoundException ex) {
			ex.printStackTrace();
		    }
		} else {
		    System.out.println("Open command canceled by the user");
		}
	    }

	    // Handle backtrack solve button
	    if (e.getSource() == backtrackButton) {
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
			protected Boolean doInBackground() throws Exception{
			    theModel.solveUsingBacktracking(theModel.worldPeek());
			    theModel.redraw();
			    return true;
			}
		    };
		worker.execute();
	    }

	    // Handle next move button
	    if (e.getSource() == nextButton) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    //theModel.fadeOutAllExceptColumn(0);
					    //theModel.fadeOutAllExceptRow(8);
					    //theModel.fadeOutAllExceptBlock(7); //// ew it's in sudokucell
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }
			    
			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    //theModel.moveColumn(0, true);
					    theModel.moveRow(8, true);
					    //theModel.moveBlock(7, true); ///// EWW it's in gridview
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }

			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    theModel.fadeIn(); ///// EWW it's in gridview
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }

			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    //theModel.moveColumn(0, false);
					    //theModel.moveRow(0, false);
					    //theModel.moveBlock(7, false); ///// EWW it's in gridview
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }

			   
			}
			
		    });
		t.start();
		
	    }

	    // Handle pause button
	    if (e.getSource() == pauseButton) {
		theModel.pauseAnimation();
	    }

	    // Handle play button
	    if (e.getSource() == playButton) {
		theModel.playAnimation();
	    }
	    
	    // Handle solve choco3 button
	    if (e.getSource() == choco3Button) {
		// GOOD !! otherwise
		// error
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
			protected Boolean doInBackground() throws Exception{
			    theModel.solveUsingChoco3();
			    theModel.redraw();
			    return true;
			}
		    };
		worker.execute();
	    }

	    // Handle print button
	    if (e.getSource() == printButton) {
		System.out.println("Printing button");
	    }

	    // Handle solve button
	    if (e.getSource() == solveFFButton){
		  try{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
					    Object nodes[] = new Object[20];

					    nodes[0] = theModel.theGrid.valueCircles[0];
					    nodes[19] = theModel.theGrid.valueCircles[10];
					    
					    Graph graph = new Graph();

					    graph.addNode(nodes[0]);
					    graph.addNode(nodes[19]);

					    int edges = 0;

					    int row = 8;
					    
					    for(int i=0; i<9; i++){
						// add 9 vars for row
						nodes[i+1] = theModel.worldPeek().grid[row][i];
						graph.addNode(nodes[i+1]);
					    }
					    
					    for(int i=1; i<10; i++){
						// add 9 values for row
						nodes[i+8+1] = theModel.theGrid.valueCircles[i];
						graph.addNode(nodes[i+8+1]);
					    }
					    
					    for(int i=0; i<9; i++){
						for(int j=0; j<9; j++){
						    // from VAR i to VALUE j
						    //int shown_x = theModel.theGrid.sudokuCells3Now[i].x;
						    //int shown_y = theModel.theGrid.sudokuCells3Now[i].y;
						    int shown_x = row;
						    int shown_y = i;
						    
						    if(!theModel.worldPeek().grid[shown_x][shown_y].hasValue(j + 1)){
							continue;
						    }

						    edges++;
						    graph.addEdge(nodes[i + 1], nodes[j+9 + 1]);
						}
					    }
					    
					    
					    //need to be greedy?
					    /*
					    for(int i=0; i<9; i++){
						for(int j=0; j<9; j++){
						    List<Edge> edgeList = graph.edgesFrom(nodes[j+9+1]);

						    int shown_x = row;
						    int shown_y = i;
						    
						    if(theModel.worldPeek().grid[shown_x][shown_y].hasValue(j + 1) && edgeList.size() == 0){
							edges++;
							graph.addEdge(nodes[i + 1], nodes[j+9+1]);
							System.out.println("ADDED EDGE from:" + (i+1) + " to:"+(j+9+1));
							//i += 1;
							break;
						    }
						}
					    }
					    */
					    for(int i=0; i<9; i++){
						//edges left source
						edges++;
						graph.addEdge(nodes[0], nodes[i+1]);
					    }

					    for(int i=0; i<9; i++){
						//edges right target
						edges++;
						graph.addEdge(nodes[19], nodes[i+10]);
					    }

					    /*
					    for(int i=0; i<20; i++){
						System.out.println("Node" + i + " " + nodes[i]);
					    }
					    */

					    int total = 0;
					    for (List<Edge> edgeList : graph.graph.values()) {
						total += edgeList.size();
					    }

					    System.out.println("Visual edges:"+edges + " in graph:"+total);
					    
					    FordFulkerson ff = new FordFulkerson(graph);
					    double value = ff.maxFlow(nodes[0], nodes[19]);
					    System.out.println("HOLY ANSWER " + value);
					}});
			    } catch (Exception e4){
				e4.printStackTrace();
			    }
	    }
	}
    }
    
}
