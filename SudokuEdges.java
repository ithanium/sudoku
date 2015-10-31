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
    
    public SudokuEdges(SudokuModel theModel){
	super();

	this.theModel = theModel;
	
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
	System.out.println("Added line x:" + jc.getX() + " y:" + jc.getY() + " width:" + jc.getSize().width + " height:" + jc.getSize().height);
	
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

	Color prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));
	
	g2d.setColor(prevColor);
	
	offset_y = (getPreferredSize().height - 9 * 50)/2;
	distanceBetweenCells_y = (getPreferredSize().height - 9 * 50)/(9-1);

	if(statusLabel.isVisible()){
	    return;
	}
	
	/////// add the lines
	int x1 = 0;
	int y1 = 0;
	int x2 = 0;
	int y2 = 0;

	g2d.setStroke(new BasicStroke(1));
	
	for(int i =0; i<9; i++){
	    // from S
	    x1 = 500;
	    y1 = (5-1) * 50 + (5-1)*distanceBetweenCells_y;
	    
	    x2 = 600;
	    y2 = (i) * 50 + (i)*distanceBetweenCells_y;

	    x1 += 25;
	    x2 += 25;
	    y1 += 25;
	    y2 += 25;
	
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
		
		g2d.draw(new Line2D.Float(x1, y1, x2, y2));
	    }
	}
	
	for(int i =0; i<9; i++){
	    // to T
	    x1 = 800;
	    y1 = (i) * 50 + (i)*distanceBetweenCells_y;

	    x2 = 900;
	    y2 = (5-1) * 50 + (5-1)*distanceBetweenCells_y;
	    
	    x1 += 25;
	    x2 += 25;
	    y1 += 25;
	    y2 += 25;
	    
	    g2d.draw(new Line2D.Float(x1, y1, x2, y2));
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
}
