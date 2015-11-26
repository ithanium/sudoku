import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;

import java.awt.Color;
import java.awt.*;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.List;

import java.util.Hashtable;
import java.util.Dictionary;
import java.util.ArrayList;

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

    public static JButton nextStepButton;    

    public static JButton showDemoButton;
    
    public static JButton solveFFButton;
    public static JButton unselectButton;

    //

    public static JMenuBar jMenuBar;
    
    public static JMenu fileMenu;
    public static JMenuItem openMenuItem;
    public static JMenuItem exitMenuItem;

    public static JMenu settingsMenu;
    public static JMenuItem solveInStepsTrueMenuItem;
    public static JMenuItem solveInStepsFalseMenuItem;
    public static JMenuItem setNormalSpeedMenuItem;
    public static JMenuItem setHighSpeedMenuItem;
    
    public static JLabel currentStepStatusLabel;

    //

    public static JSlider speedSlider;

    public Sudoku(){
	
    }
    
    public static void showGUI(){
	JFrame mainFrame = new JFrame("sudo ku");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1250, 725);
        mainFrame.setLocationRelativeTo(null);
	
	mainFrame.setResizable(false);

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
        solveInStepsTrueMenuItem = new javax.swing.JMenuItem("Solve in steps");
	solveInStepsFalseMenuItem = new javax.swing.JMenuItem("Do not solve in steps");
        setNormalSpeedMenuItem = new javax.swing.JMenuItem("Set normal speed");
	setHighSpeedMenuItem = new javax.swing.JMenuItem("Set high speed");

	settingsMenu.add(solveInStepsTrueMenuItem);
	settingsMenu.add(solveInStepsFalseMenuItem);       
	settingsMenu.add(setNormalSpeedMenuItem);
	settingsMenu.add(setHighSpeedMenuItem);

	jMenuBar.add(fileMenu);
	jMenuBar.add(settingsMenu);

	openMenuItem.addActionListener(new MouseListener());
        solveInStepsTrueMenuItem.addActionListener(new MouseListener());
        solveInStepsFalseMenuItem.addActionListener(new MouseListener());
        setNormalSpeedMenuItem.addActionListener(new MouseListener());
        setHighSpeedMenuItem.addActionListener(new MouseListener());
	
	mainFrame.setJMenuBar(jMenuBar);
	
	//Box box = new Box(BoxLayout.Y_AXIS);
	
	JPanel theGridPlaceHolder = new JPanel();
	theGridPlaceHolder.setSize(1250, 725); /////// CHECK THE SIZES
	//theGridPlaceHolder.setBackground(Color.WHITE);////////////////////////
	//SudokuGrid theGrid = new SudokuGrid(theModel, SIZE, SIZE);
	//theGrid.setModel(theModel);
	ViewController vc = new ViewController(theModel);
	//vc.setModel(theModel);
	
	//theGridPlaceHolder.add(theGrid);
	theGridPlaceHolder.add(vc);
	//box.add(Box.createVerticalGlue());
	//box.add(theGridPlaceHolder);
	//box.add(Box.createVerticalGlue());
	
	//mainFrame.add(box);
	mainFrame.add(theGridPlaceHolder);

	JPanel buttonHolders = new JPanel();
	buttonHolders.setPreferredSize(new Dimension(1265, 35));

	openFileButton = new JButton("Open");
	openFileButton.addActionListener(new MouseListener());
	buttonHolders.add(openFileButton);

	backtrackButton = new JButton("Backtrack Solve");
	backtrackButton.addActionListener(new MouseListener());
	//buttonHolders.add(backtrackButton);

	nextButton = new JButton("Start");
	nextButton.addActionListener(new MouseListener());
	//buttonHolders.add(nextButton);

	pauseButton = new JButton("Pause");
	pauseButton.addActionListener(new MouseListener());
	buttonHolders.add(pauseButton);

	playButton = new JButton("Play");
	playButton.addActionListener(new MouseListener());
	buttonHolders.add(playButton);
	
	choco3Button = new JButton("Choco3 Solve");
	choco3Button.addActionListener(new MouseListener());
	//buttonHolders.add(choco3Button);
	
	printButton = new JButton("Print statement");
	printButton.addActionListener(new MouseListener());
	//buttonHolders.add(printButton);

	nextStepButton = new JButton("Next step");
	nextStepButton.addActionListener(new MouseListener());
	buttonHolders.add(nextStepButton);
	    
	solveFFButton = new JButton("AllDifferent");
	solveFFButton.addActionListener(new MouseListener());
	buttonHolders.add(solveFFButton);

	unselectButton = new JButton("Unselect row/column/block");
	unselectButton.addActionListener(new MouseListener());
	buttonHolders.add(unselectButton);
	
	showDemoButton = new JButton("Show demo");
	showDemoButton.addActionListener(new MouseListener());
	buttonHolders.add(showDemoButton);
	    
    	currentStepStatusLabel = new JLabel();
	currentStepStatusLabel.setPreferredSize(new Dimension(1215, 25));
	currentStepStatusLabel.setFont(new Font("Serif", Font.PLAIN, 14));
	currentStepStatusLabel.setForeground(Color.BLACK);
	currentStepStatusLabel.setVerticalAlignment(SwingConstants.BOTTOM);
	currentStepStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
	currentStepStatusLabel.setText("Please select a row, column or block");
	theGridPlaceHolder.add(currentStepStatusLabel);
	
	theGridPlaceHolder.add(buttonHolders);
	
	speedSlider = new JSlider(0, 100);
	speedSlider.setPreferredSize(new Dimension(1215, 40));
	speedSlider.addChangeListener(new MyChangeListener());
	speedSlider.setSnapToTicks(true);
	speedSlider.setMajorTickSpacing(10);
	speedSlider.setMinorTickSpacing(1);
	speedSlider.setPaintTicks(true);

	Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
	labelTable.put(new Integer(0), new JLabel("Slow"));
	labelTable.put(new Integer(100), new JLabel("Fast"));

	speedSlider.setLabelTable(labelTable);
	
	speedSlider.setPaintLabels(true);

	theGridPlaceHolder.add(speedSlider);

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
		    //System.out.println("Should be false " + SwingUtilities.isEventDispatchThread());
		    
		    // Begin trick to load the initial file
		    File workingDirectory = new File(System.getProperty("user.dir"));
		    
		    try {
			// easy file
			//theModel.readFromFile(workingDirectory.getAbsolutePath() + "/herald20061222E.txt");
			// hard file
			//theModel.readFromFile(workingDirectory.getAbsolutePath() + "/herald20061222H.txt");
			// demo file
			theModel.readFromFile(workingDirectory.getAbsolutePath() + "/lockedset.txt");
		    } catch (FileNotFoundException ex) {
			ex.printStackTrace();
		    }
		    // End trick to load the initial file
		}
	    };

	try{
	    SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			//new Sudoku();
			showGUI();
		    }
		});
	} catch (Exception e){
	    e.printStackTrace();
	}
	
	r.run();	
    }

    public static void setAnimationSpeed(int speed){
	if(speed <= 50){
	    System.out.println("Set normal speed  menu item clicked");

	    for(int i=0; i<9; i++){
		for(int j=0; j<9; j++){
		    theModel.viewController.theGrid.sudokuCells[i][j].DELTA = 0.01f;
		}
	    }

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
	} else {
	    System.out.println("Set high speed menu item clicked");

	    for(int i=0; i<9; i++){
		for(int j=0; j<9; j++){
		    theModel.viewController.theGrid.sudokuCells[i][j].DELTA = 1f;
		}
	    }
	    
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
    }
    
    // make sudoku.java implement changelistener and actionlistener
    // and just write the methods
    // with this as a listener
    
    private static class MyChangeListener implements ChangeListener{
	
	public void stateChanged(ChangeEvent e) {
	    JSlider slider = (JSlider) e.getSource();
	    int sliderValue = slider.getValue();

	    setAnimationSpeed(sliderValue);
	}
    }
    
    private static class MouseListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {

	    // Handle set normal speed  menu item
	    if (e.getSource() == solveInStepsTrueMenuItem) {
		System.out.println("solveInStepsTrueMenuItem");
	    }

	    // Handle set high speed menu item
	    if (e.getSource() == solveInStepsFalseMenuItem) {
		System.out.println("solveInStepsFalseMenuItem");
	    }

	    // Handle set normal speed  menu item
	    if (e.getSource() == setNormalSpeedMenuItem) {
		setAnimationSpeed(0);
	    }

	    // Handle set high speed menu item
	    if (e.getSource() == setHighSpeedMenuItem) {
		setAnimationSpeed(100);
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

	     // Handle next step button
	    if (e.getSource() == nextStepButton) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    try{
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
					    System.out.println("Next step button pressed");
					    //theModel.fadeOutAllExceptColumn(0);
					    //theModel.fadeOutAllExceptRow(8);
					    //theModel.fadeOutAllExceptBlock(7); //// ew it's in sudokucell
					}
				    });
			    } catch (Exception e){
				e.printStackTrace();
			    }
			}});

		t.start();
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
	    
	    // Handle unselect button
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

	    // Handle showDemoButton button
	    if (e.getSource() == showDemoButton){
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    
			    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
				    ArrayList<Timer> sameLevelTimers;
				    
				    protected Boolean doInBackground() throws Exception{
					System.out.println("Started running demo");
					
					solveFFButton.setEnabled(false);
					unselectButton.setEnabled(false);
					showDemoButton.setEnabled(false);

					// sequence of steps to show the demo
					theModel.selected(0, 1); // 1st column
					System.out.println("Should start solveFF");
					theModel.solveFF();
					theModel.moveLeft(); // NEW NAME: unselect!

					theModel.selected(4, 0); // 5th row
					theModel.solveFF();
					theModel.moveLeft();

					//
					// make buttons selectable/unselectable
					//
					
					theModel.selected(5, 0); // 6th row
					theModel.solveFF();
					theModel.moveLeft();
					
					theModel.selected(3, 2); // 4th block
					theModel.solveFF();
					theModel.moveLeft();
					
					theModel.selected(0, 1); // 1st column
					theModel.solveFF();
					//theModel.moveLeft();

					theModel.fadeInGridNow();

					unselectButton.setEnabled(true);

					System.out.println("Finished running demo");
					
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
