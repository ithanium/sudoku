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

    //

    public static JMenuBar jMenuBar;
    
    public static JMenu fileMenu;
    public static JMenuItem openMenuItem;
    public static JMenuItem exitMenuItem;

    public static JMenu settingsMenu;
    public static JMenuItem setNormalSpeedMenuItem;
    public static JMenuItem setHighSpeedMenuItem;
    
    public static void showGUI(){
	JFrame mainFrame = new JFrame("sudo ku");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1250, 675);
        mainFrame.setLocationRelativeTo(null);

	jMenuBar = new javax.swing.JMenuBar();
	
        fileMenu = new javax.swing.JMenu("File");
        openMenuItem = new javax.swing.JMenuItem("Open");
	exitMenuItem = new javax.swing.JMenuItem("Exit");
	exitMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
	fileMenu.add(openMenuItem);
	fileMenu.add(exitMenuItem);

	settingsMenu = new javax.swing.JMenu("Settings");
        setNormalSpeedMenuItem = new javax.swing.JMenuItem("Set normal speed");
	setHighSpeedMenuItem = new javax.swing.JMenuItem("Set high speed");
	settingsMenu.add(setNormalSpeedMenuItem);
	settingsMenu.add(setHighSpeedMenuItem);

	jMenuBar.add(fileMenu);
	jMenuBar.add(settingsMenu);

	openMenuItem.addActionListener(new MouseListener());
        setNormalSpeedMenuItem.addActionListener(new MouseListener());
        setHighSpeedMenuItem.addActionListener(new MouseListener());
	
	mainFrame.setJMenuBar(jMenuBar);
	
	Box box = new Box(BoxLayout.Y_AXIS);
	
	JPanel theGridPlaceHolder = new JPanel();
	theGridPlaceHolder.setSize(1250,975); /////// CHECK THE SIZES
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

	solveFFButton.setEnabled(true); //// TODO
	unselectButton.setEnabled(false);
		
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
	    
	    // Handle set normal speed  menu item
	    if (e.getSource() == setNormalSpeedMenuItem) {
		System.out.println("Set normal speed  menu item clicked");

		theModel.SLEEP = 1;
		theModel.SLEEP_BETWEEN_STEPS = 3000;
		
		theModel.viewController.theGrid.DELTA = 5f; // was 1f, 5f, for movement

		for(int i=0; i<11; i++){
		    theModel.viewController.theGrid.valueCircles[i].DELTA = 0.01f; // forfade in out
		}

		theModel.viewController.theEdges.DELTA = 0.01f; // for fade out/fade in
		// TODO: SHOULD TWEAK THIS AS SLEEP 0 doesn't mean
		// no sleep at all
		theModel.viewController.theEdges.SLEEP = 500;  // between drawing
		theModel.viewController.theEdges.SLEEP_BETWEEN_STEPS = 3000;  // between drawing
	    }

	    // Handle set high speed menu item
	    if (e.getSource() == setHighSpeedMenuItem) {
		System.out.println("Set high speed menu item clicked");

		theModel.SLEEP = 0;
		theModel.SLEEP_BETWEEN_STEPS = 0;
		
		theModel.viewController.theGrid.DELTA = 1000f; // was 1f, 5f, for movement

		for(int i=0; i<11; i++){
		    theModel.viewController.theGrid.valueCircles[i].DELTA = 1f; // forfade in out
		}

		theModel.viewController.theEdges.DELTA = 1f; // for fade out/fade in
		// TODO: SHOULD TWEAK THIS AS SLEEP 0 doesn't mean
		// no sleep at all
		theModel.viewController.theEdges.SLEEP = 0;  // between drawing
		theModel.viewController.theEdges.SLEEP_BETWEEN_STEPS = 0;  // between drawing
	    }

	    // Handle open button action
	    if (e.getSource() == openFileButton || e.getSource() == openMenuItem) {

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

		solveFFButton.setEnabled(false);
		unselectButton.setEnabled(true);
		
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
		solveFFButton.setEnabled(true);
		unselectButton.setEnabled(false);
		
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
