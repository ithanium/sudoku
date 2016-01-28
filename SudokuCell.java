import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SudokuCell extends JPanel implements ActionListener{

    int i, j;  // the place on the grid
    int value; // the value, if known
    public SudokuModel theModel;

    private static final int SIZE = 50; // TODO change name
    
    public float DELTA = 0.01f;
    public ArrayList<Timer> fadeOutTimers = new ArrayList<Timer>();
    public ArrayList<Timer> fadeInTimers = new ArrayList<Timer>();
    private float alpha = 1f;
    private float fadeOutMinimum = 0f;
    
    private JLabel valuesLabel = new JLabel();
    private boolean noBoldBorder = false;
    public boolean notInTheGrid = false;
    public Color fontColor = Color.BLACK;

    public int fontSize = 20;
    public Font theFont = new Font("Serif", Font.PLAIN, fontSize);
        
    public SudokuCell(int i, int j){
	super();

	//this.setPreferredSize(new Dimension(256, 96));
        this.setOpaque(false); // don't know why
        //this.setBackground(Color.black);
	/*
        timer1.setInitialDelay(100);
        timer1.addActionListener(this);
	timer1.setCoalesce(false);

	timer2.setInitialDelay(100);
        timer2.addActionListener(this);
	timer2.setCoalesce(false);
	*/
	
	valuesLabel.setFont(theFont);
	valuesLabel.setForeground(fontColor);
	//valuesLabel.setSize(SIZE, SIZE);
	valuesLabel.setSize(new Dimension(SIZE, SIZE));
	//valuesLabel.setVerticalAlignment(SwingConstants.CENTER);
	//valuesLabel.setSize(new Dimension(50, 50));
	//valuesLabel.setText("<html><div style=\"text-align: center; padding-top: 3px; padding-left: 1px;\"><font color='BLACK'>1 2 3<br>4 5 6<br>7 8 9</font></div></html>");
	
	setLayout(new GridBagLayout());
	setPreferredSize(new Dimension(50, 50));

	add(valuesLabel);

	this.i = i;
	this.j = j;
    }

    public void setValuesLabel(String valuesText){
	//SwingUtilities.invokeLater(new Runnable() {
	//	public void run() {
		    //System.out.println("TEST X0");
		    //System.out.println("Should be true " + SwingUtilities.isEventDispatchThread());
		    valuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    valuesLabel.setVerticalAlignment(SwingConstants.CENTER);
		    
		    if(valuesText.length() <= 1){
			// Single digit

			if(valuesText.compareTo("0") != 0){
			    valuesLabel.setText(valuesText);
			} else {
			    valuesLabel.setText(" ");
			}

			// TODO DISSERTATION
			// NUMBERS ARE HARDCODED BECAUSE
			// FONT FORMATING RETURNS SAME THING ON ALL PLATFORMS
			// EVEN THOUGH FONTS LOOK DIFFERENT
			fontSize = 20;
			//fontSize = theModel.getMaxFittingFontSize("9", theFont, valuesLabel, 50, 50);

			valuesLabel.setFont(new Font("Serif", Font.PLAIN, fontSize));

			//valuesLabel.repaint();
		    } else {
			// More choices available
			valuesLabel.setText(valuesText);
			
			fontSize = 14;
			//fontSize = theModel.getMaxFittingFontSize("1 2 3", theFont, valuesLabel, 50, 50);
			//System.out.println("fontSize: " + fontSize);
			
			valuesLabel.setFont(new Font("Serif", Font.PLAIN, fontSize));
			//valuesLabel.repaint();
			
		    }
		    //		}
    //  });

    }

    public String formatPossibleValues(){
	// first refresh the values
	//System.out.println(theModel);
	ArrayList<Integer> possibleValues = theModel.getPossibleValues(i, j);
	
	if(possibleValues.size() == 1){
	    return Integer.toString(possibleValues.get(0));
	}

	StringBuilder sb = new StringBuilder();
	//sb.append("<html><div style=\"text-align: center;\">");
	sb.append("<html><div style=\"text-align: center; padding-top: 3px; padding-left: 4px;\">");
	for(int i=1; i<=9; i++){
	    if(possibleValues.contains(i)){
		sb.append(i + "&nbsp;");
	    } else {
		sb.append("&nbsp;&nbsp;&nbsp;");
	    }
	    
	    if(i%3 == 0){
		sb.append("<br>");
	    }
	}
	sb.append("</div></html>");

	return sb.toString();
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

	int top = 2;
	int left = 2;
	int bottom = 2;
	int right = 2;

	if(noBoldBorder == false){
	    // sub-grids border
	    if(this.i % 3 == 0){top = 4;}
	    if(this.j % 3 == 0){left = 4;}
	    if(this.i % 3 == 2){bottom = 4;}
	    if(this.j % 3 == 2){right = 4;}

	    // outside border
	    if(this.i % 9 == 0){top = 8;}
	    if(this.j % 9 == 0){left = 8;}
	    if(this.i % 9 == 8){bottom = 8;}
	    if(this.j % 9 == 8){right = 8;}
	}	 

	//THIS DIDN'T WORK ON WINDOWS
	//KEEP THIS TO WRITE IN DISSERTATION
	//TODO:
	//setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, prevColor));

	g2d.setStroke(new BasicStroke(top));
	g2d.drawLine(0, 0, 50, 0); //windows: 49
	g2d.setStroke(new BasicStroke(left));
	g2d.drawLine(0, 0, 0, 50);//windows: 49
	g2d.setStroke(new BasicStroke(bottom));
	g2d.drawLine(0, 50, 50, 50);//windows: 49
	g2d.setStroke(new BasicStroke(right));
	g2d.drawLine(50, 0, 50, 50);//here leave it like this
    }

    @Override
    public Dimension getPreferredSize() {
	return new Dimension(50, 50);
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
	alpha = 0;

        repaint();
    }

    public void setAlphaOne(){
	alpha = 1;

        repaint();
    }
    
    public void setOpaqueOn(){
	this.setOpaque(true);
    }

    public void setOpaqueOff(){
	this.setOpaque(false);
    }
    
    public void fadeOut(){
	fadeOutMinimum = 0f;
	//timer1.start(); KEEP IT DELETED	
    }

    public void fadeOutALittle(){
	fadeOutMinimum = 0.30f;
	//theModel.timers.add(timer);/////////////////////////////////
	//timer1.start(); KEEP IT DELETED
    }
    
    public void fadeIn(){
    }

    public void setModel(SudokuModel model){
	this.theModel = model;
    }

    public void setNoBoldBorder(){
	noBoldBorder = true;
	//Color prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));
	//setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, prevColor));
	//repaint();
    }

    public void setNotInTheGrid(){

	// this means this cell is in the right of the screen
	// use this in order to assign new click actions
	// in sudoku cell listener
	notInTheGrid = true;
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

    public void setFontColor(Color color){
	this.fontColor = color;

	//System.out.println("SudokuCell setting font color: " + colorName);
	valuesLabel.setForeground(fontColor);
	// TODO: in one call
	setValuesLabel(formatPossibleValues());
	//valuesLabel.repaint();
	//repaint();
    }
}
