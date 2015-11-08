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
    public static JButton unselectButton;
    
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
	//theGridPlaceHolder.add(openFileButton);

	backtrackButton = new JButton("Backtrack Solve");
	backtrackButton.addActionListener(new MouseListener());
	//theGridPlaceHolder.add(backtrackButton);

	nextButton = new JButton("Start");
	nextButton.addActionListener(new MouseListener());
	//theGridPlaceHolder.add(nextButton);

	pauseButton = new JButton("Pause");
	pauseButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(pauseButton);

	playButton = new JButton("Play");
	playButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(playButton);
	
	choco3Button = new JButton("Choco3 Solve");
	choco3Button.addActionListener(new MouseListener());
	//theGridPlaceHolder.add(choco3Button);
	
	printButton = new JButton("Print statement");
	printButton.addActionListener(new MouseListener());
	//theGridPlaceHolder.add(printButton);

	solveFFButton = new JButton("AllDifferent");
	solveFFButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(solveFFButton);

	unselectButton = new JButton("Unselect row/column/block");
	unselectButton.addActionListener(new MouseListener());
	theGridPlaceHolder.add(unselectButton);

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
					    //theModel.moveRow(8, false);
					    //theModel.fadeOutAllExceptRow(8);
					    //theModel.solve();
					    //theModel.moveBlock(7, false); ///// EWW it's in gridview
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }
			     
			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    //theModel.moveColumn(0, false);
					    //theModel.moveRow(8, false);
					    //theModel.fadeOutAllExceptRow(8);
					    //theModel.moveBlock(7, false); ///// EWW it's in gridview
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }

			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    //theModel.moveColumn(0, false);
					    //theModel.moveRow(8, false);
					    //theModel.fadeOutAllExceptRow(8);
					    //theModel.moveBlock(7, false); ///// EWW it's in gridview
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }

			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    //theModel.fadeIn(); ///// EWW it's in gridview
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
		/*
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
			protected Boolean doInBackground() throws Exception{
			    theModel.solve();
			    return true;
			}
		    };
		worker.execute();
		*/
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
				    protected Boolean doInBackground() throws Exception{
					//System.out.println("Is solve EDT?" + SwingUtilities.isEventDispatchThread());
					theModel.solveFF();
					return true;
				    }
				};
			    worker.execute();
			}});
		
		th.start();
	    }

	    // Handle solve button
	    if (e.getSource() == unselectButton){
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
				    protected Boolean doInBackground() throws Exception{
					//System.out.println("Is solve EDT?" + SwingUtilities.isEventDispatchThread());
					theModel.moveLeft();
					return true;
				    }
				};
			    worker.execute();
			}});
		
		th.start();
	    }
	}
    }
    
}
