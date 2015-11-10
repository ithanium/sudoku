import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import java.awt.geom.Line2D;
//import java.awt.geom.Line2D;

public class SudokuEdges extends JPanel implements ActionListener {

    private float DELTA = 0.01f;
    public ArrayList<Timer> fadeOutTimers = new ArrayList<Timer>(); //fadeOut
    public ArrayList<Timer> fadeInTimers = new ArrayList<Timer>(); //fadeIn

    public Timer timerSolve;
    
    private float alpha = 1f;
    private float fadeOutMinimum = 0f;

    private boolean fadingOut = true;
    
    //private JLayeredPane layeredPane;

    public SudokuModel theModel;
    //public SudokuEdges theEdges; // 1st layer with edges between cells and values
    //public SudokuGrid theGrid; // 2nd layer with the SudokuCells and Circles

    JLabel statusLabel;
    public static JButton printButton;

    //public Line lines[];

    private int offset_y;
    private int distanceBetweenCells_y;

    private static final int SLEEP = 500; // THREAD SLEEP BETWEEN EDGE DRAWING
    public Color[][] edgeColors;

    public Color prevColor;

    public ArrayList<TripletIIB> moves = new ArrayList<TripletIIB>();
    
    public SudokuEdges(SudokuModel theModel){
	super();

	this.theModel = theModel;

	edgeColors  = new Color[20][20];

	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		//initially all edges are gray
		edgeColors[i][j] = Color.LIGHT_GRAY;
	    }
	}
	
	//lines = new Line[9*9 + 9+9];
	
	//System.out.println("SudokuEdges made");

	setPreferredSize(new Dimension(950, 550-4));
	setLayout(null);
	//setOpaque(false);

	//setBackground(Color.BLUE);
	
	statusLabel = new JLabel();
	statusLabel.setFont(new Font("Serif", Font.PLAIN, 14));
	statusLabel.setText("<html><div align=center>Please select a row, column<br> or block</div></html>");
	statusLabel.setSize(new Dimension(200, 50));
	add(statusLabel, 650, getPreferredSize().height/2 - 25, statusLabel.getWidth(), statusLabel.getHeight());
	/*
	printButton = new JButton("Print statement");
	printButton.addActionListener(new MouseListener());
	printButton.setBounds(600, 300, 200, 50);
	add(printButton);
	*/
    }

    public void setModel(SudokuModel theModel){
	this.theModel = theModel;
    }

    public void add (JComponent jc, int x, int y, int width, int height){
	jc.setLocation(x, y);
	jc.setBounds(new Rectangle(new Point(x, y), new Dimension(width, height)));
	add(jc);
	jc.setVisible(true);
	
	repaint(); ////// need this?
	
    }

    public void showWelcomeScreen(){
	statusLabel.setVisible(true);
    }

    public void hideWelcomeScreen(){
	setAlphaZero();
	statusLabel.setVisible(false);
    }
    
    private static class MouseListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {

	    // Handle open button action
	    if (e.getSource() == printButton) {
		System.out.println("edges print button");
	    }
	}
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
	super.paintComponent(grphcs);
	
	Graphics2D g2d = (Graphics2D) grphcs;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	/*
	//black
	Color prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));

	Color blueColor = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), (int)(alpha*255));
	Color redColor = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), (int)(alpha*255));
	Color greenColor = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), (int)(alpha*255));
	
	g2d.setColor(prevColor);
	*/
	offset_y = (getPreferredSize().height - 9 * 50)/2;
	distanceBetweenCells_y = (getPreferredSize().height - 9 * 50)/(9-1);

	if(statusLabel.isVisible()){
	    return;
	}

	////////// do not show any lines as we do not have any
	////////// row column or block selected
	
	if(theModel.theGrid.sudokuCells3Now[0] == null){
	    return;
	}

	/////// add the lines
	int x1 = 0;
	int y1 = 0;
	int x2 = 0;
	int y2 = 0;

	int u;
	int v;
	
	g2d.setStroke(new BasicStroke(1));

	for(int i =0; i<9; i++){
	    // from S to vars
	    x1 = 500;
	    y1 = (5-1) * 50 + (5-1)*distanceBetweenCells_y;
	    
	    x2 = 600;
	    y2 = (i) * 50 + (i)*distanceBetweenCells_y;

	    x1 += 25; // was 25 all four
	    x2 += 25;
	    y1 += 25;
	    y2 += 25;

	    u = 0;
	    v = i + 1;

	    if(drawColor(u, v) == Color.WHITE){
		//continue;
	    }
	    
	    g2d.setColor(drawColor(u, v));
	    g2d.draw(new Line2D.Float(x1, y1, x2, y2));
	}

	for(int i =0; i<9; i++){
	    // from values to T
	    x1 = 800;
	    y1 = (i) * 50 + (i)*distanceBetweenCells_y;

	    x2 = 900;
	    y2 = (5-1) * 50 + (5-1)*distanceBetweenCells_y;
	    
	    x1 += 25;
	    x2 += 25;
	    y1 += 25;
	    y2 += 25;

	    u = i + 9 + 1;
	    v = 19;

	    if(drawColor(u, v) == Color.WHITE){
		//continue;
	    }
		    
	    g2d.setColor(drawColor(u, v));
	    g2d.draw(new Line2D.Float(x1, y1, x2, y2));
	}
		
	for(int i=0; i<9; i++){
	    for(int j=0; j<9; j++){
		// from VAR i to VALUE j
	        int shown_x = theModel.theGrid.sudokuCells3Now[i].x;
		int shown_y = theModel.theGrid.sudokuCells3Now[i].y;
		
		if(!theModel.worldPeek().grid[shown_x][shown_y].hasValue(j + 1)){
		    continue;
		}
		
		x1 = 600;
		y1 = (i) * 50 + (i)*distanceBetweenCells_y;
		
		x2 = 800;
		y2 = (j) * 50 + (j)*distanceBetweenCells_y;
		
		x1 += 25;
		x2 += 25;
		y1 += 25;
		y2 += 25;

		u = i + 1;
		v = j + 9 + 1;

		if(drawColor(u, v) == Color.WHITE){
		    //continue;
		}
			    
		g2d.setColor(drawColor(u, v));
		//g2d.setColor(Color.BLACK);
		g2d.draw(new Line2D.Float(x1, y1, x2, y2));
	    }
	}
	/////// end adding the lines
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if(theModel.isAnimationPaused){
	    return;
	}
		
	Timer thisTimer = ((Timer)e.getSource());
	
	theModel.stopAllTimersOnDiffLevelComparedTo(thisTimer);

	if(thisTimer == timerSolve){
	    System.out.println("THIS TIMER IS SOLVER, CALL MODEL");

	    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
		protected Boolean doInBackground() throws Exception{
		    try{
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
				    theModel.solveFF(); 
				}
			    });
		    } catch (Exception e2){
			e2.printStackTrace();
		    }
		    
		    return true;
		}
	    };
	    
	    worker.execute();
	    /////////////////////////////////////////////////////////
	    
	    thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
	    theModel.removeTimer(thisTimer);
	    theModel.startNextTimers();
	}
	
	if(fadeOutTimers.contains(thisTimer)){
	    //System.out.println("Fadeing out timer");
	    alpha -= DELTA;
	    
	    if (alpha <= fadeOutMinimum) {
		alpha = fadeOutMinimum;

		fadeOutTimers.remove(thisTimer);
		
		thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
		theModel.removeTimer(thisTimer);
		theModel.startNextTimers();
	    }
	} else if(fadeInTimers.contains(thisTimer)){
	    //System.out.println("Fading in timer");
	    alpha += DELTA;
	    
	    if (alpha >= 1) {
		alpha = 1;

		fadeInTimers.remove(thisTimer);
		
		thisTimer.stop(); // STOP IT, START THE NEXT ONE IN QUEUE
		theModel.removeTimer(thisTimer);
		theModel.startNextTimers();
	    }
	}

	theModel.lastRunningTimer = thisTimer;
	
	repaint();
    }


    public void setAlphaZero(){
	//System.out.println("SET ALPHA ZERO");
	alpha = 0;

        repaint();
    }

    public void setAlphaOne(){
	//System.out.println("SET ALPHA ONE");
	alpha = 1;

        repaint();
    }
    
    public void setOpaqueOn(){
	//System.out.println("SET ALPHA ON");
	this.setOpaque(true);
    }

    public void setOpaqueOff(){
	//System.out.println("SET ALPHA OFF");
	this.setOpaque(false);
    }
    
    public void fadeOut(){
	//System.out.println("FADE OUT LINE");
	fadingOut = true;
	fadeOutMinimum = 0f;
	//timer1.start(); KEEP IT DELETED	
    }

    public void fadeOutALittle(){
	//System.out.println("FADE OUT A LITTLE");
	fadeOutMinimum = 0.30f;
	//theModel.timers.add(timer);/////////////////////////////////
	//timer1.start(); KEEP IT DELETED
    }
    
    public void fadeIn(){
	//System.out.println("line fading in");
	fadingOut = false;
        //timer2.start(); KEEP IT DELETED
    }

    public Timer getTimerFadeOut(){
	Timer timer1 = new Timer(10, null); //fadeOut
	timer1.setInitialDelay(100);
        timer1.addActionListener(this);
	timer1.setCoalesce(false);

	fadeOutTimers.add(timer1);
	
	return timer1;
    }

    public Timer getTimerFadeIn(){
	Timer timer2 = new Timer(10, null); //fadeOut
	timer2.setInitialDelay(100);
        timer2.addActionListener(this);
	timer2.setCoalesce(false);

	fadeInTimers.add(timer2);
	
	return timer2;	
    }

    public void drawPath(ArrayList<Integer> path){
	//applyDrawing();
	
	//System.out.print("Draw path: ");

	for(int i=0; i<path.size()-1; i++){
	    //System.out.print(path.get(i) + " ");
	    if(path.get(i) < path.get(i+1)){
		moves.add(new TripletIIB(path.get(i), path.get(i+1), Color.BLUE));
		//drawBlue(path.get(i), path.get(i+1));
	    } else {
		moves.add(new TripletIIB(path.get(i+1), path.get(i), Color.BLUE));
		//drawBlue(path.get(i+1), path.get(i));
	    }
	}

	//System.out.println();
    }

    public void drawPathNow(ArrayList<Integer> path){

	for(int i=0; i<path.size()-1; i++){
	    //System.out.print(path.get(i) + " ");
	    if(path.get(i) < path.get(i+1)){
		moves.add(new TripletIIB(path.get(i), path.get(i+1), Color.BLACK));
		//drawBlue(path.get(i), path.get(i+1));
		//drawBlack(path.get(i), path.get(i+1));
	    } else {
		moves.add(new TripletIIB(path.get(i+1), path.get(i), Color.BLACK));
		//drawBlue(path.get(i+1), path.get(i));
		//drawBlack(path.get(i+1), path.get(i));
	    }
	}
    }

    public void drawBlack(int u, int v){ // special as it doesn't repaint/sleep
	edgeColors[u][v] = Color.BLACK;
    }
    
    public void drawRed(int u, int v){
	//System.out.println("Draw red u:" + u + " v:" + v);
	edgeColors[u][v] = Color.RED;

	try{
	    repaint();
		
	    Thread.sleep(SLEEP);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

    public void drawGreen(int u, int v){
	//System.out.println("Draw green u:" + u + " v:" + v);
	edgeColors[u][v] = Color.GREEN;


	//System.out.println("Is drawGreen EDT? 1" + SwingUtilities.isEventDispatchThread());
	try{
	    repaint();
	    
	    Thread.sleep(SLEEP);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

    public void drawBlue(int u, int v){
	//System.out.println("Draw blue u:" + u + " v:" + v);
	edgeColors[u][v] = Color.BLUE;

	try{
	    repaint();
		
	    Thread.sleep(SLEEP);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }


    public void storeMoves(ArrayList<TripletIIB> triplets){
	for(TripletIIB t:triplets){
	    this.moves.add(t);
	}
    }

    public void drawMoves(){
	//System.out.println("Drawing moves");
	//System.out.println("Is drawing moves EDT? " + SwingUtilities.isEventDispatchThread());
	for(TripletIIB t:this.moves){
	    //System.out.println("Drawing move");
	    if(t.getC() == Color.GREEN){
		drawGreen(t.getA(), t.getB());
	    } else if(t.getC() == Color.RED){
		drawRed(t.getA(), t.getB());
	    } else if(t.getC() == Color.BLUE){
		if(prevColor != Color.BLUE){
		    applyDrawing();
		}
		
		drawBlue(t.getA(), t.getB());		
	    }

	    prevColor = t.getC();
	}
		    
	applyDrawing();
    }

    public void drawMovesNow(){
	for(TripletIIB t:this.moves){
	    //System.out.println("Drawing move");
	    if(t.getC() == Color.BLACK){
		drawBlack(t.getA(), t.getB());
	    }
	    
	    //prevColor = Color.BLUE; // TODO: try to remove this?
	}
		    
	//applyDrawing(); // TODO: try to remove this?

	moves = new ArrayList<TripletIIB>();

	try{
	    repaint();
		
	    Thread.sleep(SLEEP);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

    public Color drawColor(int u, int v){
	Color drawColor = null;

	Color invisibleColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(0*255));
		
	Color grayColor = new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), (int)(alpha*255));
		
	//black
	Color prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));

	Color blueColor = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), (int)(alpha*255));
	Color redColor = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), (int)(alpha*255));
	Color greenColor = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), (int)(alpha*255));	

	if(edgeColors[u][v] == Color.LIGHT_GRAY){
	    //System.out.println("Returning gray");
	    return grayColor;
	}

	if(edgeColors[u][v] == Color.BLACK){
	    //System.out.println("Returning black");
	    return prevColor;
	}

	if(edgeColors[u][v] == Color.BLUE){
	    //System.out.println("Returning blue");
	    return blueColor;
	}

	if(edgeColors[u][v] == Color.RED){
	    //System.out.println("Returning red");
	    return redColor;
	}

	if(edgeColors[u][v] == Color.GREEN){
	    //System.out.println("Returning green");
	    return greenColor;
	}

	if(edgeColors[u][v] == Color.WHITE){
	    //System.out.println("Returning invisible");
	    return invisibleColor;
	}
	
	return drawColor;
    }

    public void applyDrawing(){
	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		if(edgeColors[i][j] == Color.GREEN){
		    edgeColors[i][j] = Color.BLACK;
		}

		if(edgeColors[i][j] == Color.RED){
		    edgeColors[i][j] = Color.LIGHT_GRAY;
		}
	    }
	}

	try{
	    repaint();
		
	    Thread.sleep(SLEEP);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

    public void finishDrawing(){
	for(int i=0; i<20; i++){
	    for(int j=0; j<20; j++){
		if(j<10 || i>9 || edgeColors[i][j] == Color.LIGHT_GRAY){
		    edgeColors[i][j] = Color.WHITE;
		}
	    }
	}

	try{
	    repaint();
		
	    Thread.sleep(SLEEP);
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

    public Timer getTimerSolve(){
	Timer timer1 = new Timer(10, null);
	timer1.setInitialDelay(100);
        timer1.addActionListener(this);
	timer1.setCoalesce(false);

	timerSolve = timer1;
	
	return timerSolve;
    }
}
