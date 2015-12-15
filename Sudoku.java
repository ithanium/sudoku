import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Dictionary;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Sudoku{
    public static MutableBoolean DEBUG = new MutableBoolean(false);
    
    public final int SIZE = 9;
    public final int SLEEP = 1;
	
    public SudokuModel theModel;

    public JButton openFileButton;
    public JButton pauseButton;
    public JButton playButton;
    public JButton nextStepButton;
    public JButton allDifferentButton;
    public JButton deselectButton;
    public JButton showDemoButton;
    public JButton backtrackButton;
    public JButton choco3Button;

    public JMenuBar jMenuBar;
    
    public JMenu fileMenu;
    public JMenuItem openMenuItem;
    public JMenuItem exitMenuItem;

    public JMenu settingsMenu;
    public JMenuItem solveInStepsTrueMenuItem;
    public JMenuItem solveInStepsFalseMenuItem;
    public JMenuItem setNormalSpeedMenuItem;
    public JMenuItem setHighSpeedMenuItem;
    public JMenuItem setPropagateOnMenuItem;
    public JMenuItem setPropagateOffMenuItem;
    public JMenuItem setRunAllDifferentWhenSelectingOnMenuItem;
    public JMenuItem setRunAllDifferentWhenSelectingOffMenuItem;
    public JMenuItem setDeselectAfterAllDifferentOnMenuItem;
    public JMenuItem setDeselectAfterAllDifferentOffMenuItem;
    
    public JLabel currentStepStatusLabel;

    public JSlider speedSlider;

    private Sudoku(){
	theModel = new SudokuModel();
	theModel.referenceToMain = this;
	theModel.DEBUG = DEBUG;
	theModel.SIZE = SIZE;
	theModel.SLEEP = SLEEP;

	/*
	  try{
	  SwingUtilities.invokeAndWait(new Runnable() {
	  public void run() {
	*/
	showGUI();
	/*
	  }
	  });
	  } catch (Exception e){
	  e.printStackTrace();
	  }
	*/
			
	if(DEBUG.getValue()){System.out.println("GUI loaded");}
	
	File workingDirectory = new File(System.getProperty("user.dir"));
		    
	try {
	    // demo file
	    theModel.readFromFile(workingDirectory.getAbsolutePath() + "/puzzles/lockedset.txt");
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	}
    }
    
    public void showGUI(){

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
	setPropagateOnMenuItem = new javax.swing.JMenuItem("Set propagate on");
	setPropagateOffMenuItem = new javax.swing.JMenuItem("Set propagate off");
	setRunAllDifferentWhenSelectingOnMenuItem = new javax.swing.JMenuItem("Run allDifferent when selecting on");
	setRunAllDifferentWhenSelectingOffMenuItem = new javax.swing.JMenuItem("Run allDifferent when selecting off");
	setDeselectAfterAllDifferentOnMenuItem = new javax.swing.JMenuItem("Deselect row after allDifferent finishes on");
	setDeselectAfterAllDifferentOffMenuItem = new javax.swing.JMenuItem("Deselect row after allDifferent finishes off");
	
	settingsMenu.add(solveInStepsTrueMenuItem);
	settingsMenu.add(solveInStepsFalseMenuItem);
	settingsMenu.add(new JSeparator(JSeparator.HORIZONTAL));
	settingsMenu.add(setNormalSpeedMenuItem);
	settingsMenu.add(setHighSpeedMenuItem);
	settingsMenu.add(new JSeparator(JSeparator.HORIZONTAL));
	settingsMenu.add(setPropagateOnMenuItem);
	settingsMenu.add(setPropagateOffMenuItem);
	settingsMenu.add(new JSeparator(JSeparator.HORIZONTAL));
	settingsMenu.add(setRunAllDifferentWhenSelectingOnMenuItem);
	settingsMenu.add(setRunAllDifferentWhenSelectingOffMenuItem);
	settingsMenu.add(new JSeparator(JSeparator.HORIZONTAL));
	settingsMenu.add(setDeselectAfterAllDifferentOnMenuItem);
	settingsMenu.add(setDeselectAfterAllDifferentOffMenuItem);

	
	jMenuBar.add(fileMenu);
	jMenuBar.add(settingsMenu);

	openMenuItem.addActionListener(new MouseListener());
	solveInStepsTrueMenuItem.addActionListener(new MouseListener());
	solveInStepsFalseMenuItem.addActionListener(new MouseListener());
	setNormalSpeedMenuItem.addActionListener(new MouseListener());
	setHighSpeedMenuItem.addActionListener(new MouseListener());
	setPropagateOnMenuItem.addActionListener(new MouseListener());
	setPropagateOffMenuItem.addActionListener(new MouseListener());
	setRunAllDifferentWhenSelectingOnMenuItem.addActionListener(new MouseListener());
	setRunAllDifferentWhenSelectingOffMenuItem.addActionListener(new MouseListener());
	setDeselectAfterAllDifferentOnMenuItem.addActionListener(new MouseListener());
	setDeselectAfterAllDifferentOffMenuItem.addActionListener(new MouseListener());
	
	mainFrame.setJMenuBar(jMenuBar);
	
	JPanel theGridPlaceHolder = new JPanel();
	theGridPlaceHolder.setSize(1250, 725); /////// CHECK THE SIZES

	ViewController vc = new ViewController(theModel);
	
	theGridPlaceHolder.add(vc);
	mainFrame.add(theGridPlaceHolder);

	JPanel buttonHolders = new JPanel();
	buttonHolders.setPreferredSize(new Dimension(1265, 35));

	openFileButton = new JButton("Open");
	openFileButton.addActionListener(new MouseListener());
	buttonHolders.add(openFileButton);

	pauseButton = new JButton("Pause");
	pauseButton.addActionListener(new MouseListener());
	buttonHolders.add(pauseButton);

	playButton = new JButton("Play");
	playButton.addActionListener(new MouseListener());
	buttonHolders.add(playButton);

	nextStepButton = new JButton("Next step");
	nextStepButton.addActionListener(new MouseListener());
	buttonHolders.add(nextStepButton);
	    
	allDifferentButton = new JButton("AllDifferent");
	allDifferentButton.addActionListener(new MouseListener());
	buttonHolders.add(allDifferentButton);

	deselectButton = new JButton("Deselect row/column/block");
	deselectButton.addActionListener(new MouseListener());
	buttonHolders.add(deselectButton);
	
	showDemoButton = new JButton("Show demo");
	showDemoButton.addActionListener(new MouseListener());
	buttonHolders.add(showDemoButton);

	backtrackButton = new JButton("Backtrack Solve");
	backtrackButton.addActionListener(new MouseListener());
	buttonHolders.add(backtrackButton);

	choco3Button = new JButton("Choco3 Solve");
	choco3Button.addActionListener(new MouseListener());
	buttonHolders.add(choco3Button);
	    
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

	allDifferentButton.setEnabled(true); //// TODO
	deselectButton.setEnabled(false);
		
	mainFrame.setVisible(true);
    }
    
    public static void main(String args[]){
	if(args.length > 0){
	    if(args[0].compareTo("debug") == 0 || args[0].compareTo("DEBUG") == 0){
		DEBUG.setValue(true);
		if(DEBUG.getValue()){System.out.println("Debug enabled");}	
	    }
	}
		
	new Sudoku();
    }

    public void setAnimationSpeed(int speed){
	if(speed <= 50){
	    if(DEBUG.getValue()){System.out.println("Set normal speed  menu item clicked");}

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
	    if(DEBUG.getValue()){System.out.println("Set high speed menu item clicked");}

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
    
    private class MyChangeListener implements ChangeListener{
	
	public void stateChanged(ChangeEvent e) {
	    JSlider slider = (JSlider) e.getSource();
	    int sliderValue = slider.getValue();

	    setAnimationSpeed(sliderValue);
	}
    }
    
    private class MouseListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {

	    // Handle set normal speed  menu item
	    if (e.getSource() == solveInStepsTrueMenuItem) {
		if(DEBUG.getValue()){System.out.println("solveInSteps True MenuItem");}
		theModel.setSolveInSteps(true);
	    }

	    // Handle set high speed menu item
	    if (e.getSource() == solveInStepsFalseMenuItem) {
		if(DEBUG.getValue()){System.out.println("solveInSteps False MenuItem");}
		theModel.setSolveInSteps(false);
	    }

	    // Handle set normal speed  menu item
	    if (e.getSource() == setNormalSpeedMenuItem) {
		setAnimationSpeed(0);
	    }

	    // Handle set high speed menu item
	    if (e.getSource() == setHighSpeedMenuItem) {
		setAnimationSpeed(100);
	    }
	    
	    // Handle set propagate on menu item
	    if (e.getSource() == setPropagateOnMenuItem) {
		System.out.println("Propagate on");
		theModel.setPropagateAfterAllDifferent(true);
	    }

	    // Handle set propagate off menu item
	    if (e.getSource() == setPropagateOffMenuItem) {
		System.out.println("Propagate off");
		theModel.setPropagateAfterAllDifferent(false);
	    }

	    // Handle set run all different after making a selection on menu item
	    if (e.getSource() == setRunAllDifferentWhenSelectingOnMenuItem) {
		System.out.println("AllDifferent on selection on");
		theModel.setRunAllDifferentOnSelection(true);
	    }

	    // Handle set run all different after making a selection on menu item
	    if (e.getSource() == setRunAllDifferentWhenSelectingOffMenuItem) {
		System.out.println("AllDifferent on selection off");
		theModel.setRunAllDifferentOnSelection(false);
	    }

	    // Handle deselect after alldifferent finishies running on menu item
	    if (e.getSource() == setDeselectAfterAllDifferentOnMenuItem) {
		System.out.println("Deselect after alldifferent on");
		theModel.setDeselectAfterAllDifferent(true);
	    }

	    // Handle deselect after alldifferent finishies running off menu item
	    if (e.getSource() == setDeselectAfterAllDifferentOffMenuItem) {
		System.out.println("Deselect after alldifferent off");
		theModel.setDeselectAfterAllDifferent(false);
	    }
	    
	    // Handle open button action
	    if (e.getSource() == openFileButton || e.getSource() == openMenuItem) {

		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter(".txt - plain text files", "txt"));
	
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
		    if(DEBUG.getValue()){System.out.println("Open command canceled by the user");}
		}
	    }

	    // Handle backtrack solve button
	    if (e.getSource() == backtrackButton) {
		//allDifferentButton.setEnabled(false);
		//deselectButton.setEnabled(true);

		// escape from EDT
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.solveUsingBacktracking(theModel.worldPeek());
			    theModel.redraw();
			}			
		    });
		t.start();
	    }

	    // Handle next move button
	    if (e.getSource() == nextStepButton) {
		//allDifferentButton.setEnabled(false);
		//deselectButton.setEnabled(true);

		// escape from EDT
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.nextAnimation();
			}			
		    });
		t.start();
	    }

	    // Handle pause button
	    if (e.getSource() == pauseButton) {
		// escape from EDT
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.pauseAnimation();
			}			
		    });
		t.start();
	    }

	    // Handle play button
	    if (e.getSource() == playButton) {
		// escape from EDT
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.playAnimation();
			}			
		    });
		t.start();

	    }
	    
	    // Handle solve choco3 button
	    if (e.getSource() == choco3Button) {
		//allDifferentButton.setEnabled(false);
		//deselectButton.setEnabled(true);

		// escape from EDT
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.solveUsingChoco3();
			    theModel.redraw();
			}});
		
		th.start();
	    }

	    // Handle allDifferent button
	    if (e.getSource() == allDifferentButton){
		allDifferentButton.setEnabled(false);
		deselectButton.setEnabled(true);

		// escape from EDT
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.allDifferent();
			}});
		
		th.start();
	    }
	    
	    // Handle deselect button
	    if (e.getSource() == deselectButton){
		allDifferentButton.setEnabled(true);
		deselectButton.setEnabled(false);

		// escape from EDT
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    theModel.deselect();
			}});
		
		th.start();
	    }

	    // Handle showDemoButton button
	    if (e.getSource() == showDemoButton){

		//System.out.println("EDT? " + SwingUtilities.isEventDispatchThread() + " " + Thread.currentThread().getId() + " " + new Throwable().getStackTrace()[0].getClassName() + " " + new Throwable().getStackTrace()[0].getLineNumber());

		// escape from EDT
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			    if(DEBUG.getValue()){System.out.println("Started running demo");}

			    allDifferentButton.setEnabled(false);
			    deselectButton.setEnabled(false);
			    showDemoButton.setEnabled(false);
			    
			    // sequence of steps to show the demo
			    theModel.select(0, 1); // 1st, type 1 for column
			    theModel.allDifferent();
			    theModel.deselect();
			    
			    theModel.select(4, 0); // 5th, type 0 for row
			    theModel.allDifferent();
			    theModel.deselect();
			    
			    //
			    // make buttons selectable/deselectable
			    //
			    
			    theModel.select(5, 0); // 6th, type 0 for row
			    theModel.allDifferent();
			    theModel.deselect();
			    
			    theModel.select(3, 2); // 4th, type 2 for block
			    theModel.allDifferent();
			    theModel.deselect();
			    
			    theModel.select(0, 1); // 1st, type 1 for column
			    theModel.allDifferent();
			    //theModel.deselect();
			    
			    theModel.fadeInGridNow();
			    
			    deselectButton.setEnabled(true);
			    
			    if(DEBUG.getValue()){System.out.println("Finished running demo");}
			    
			}});
		
		th.start();
	    }
	}
    }
    
}
