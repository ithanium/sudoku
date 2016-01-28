import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class Circle extends JPanel implements ActionListener{
    public float DELTA = 0.01f;
    public ArrayList<Timer> fadeOutTimers = new ArrayList<Timer>(); //fadeOut
    public ArrayList<Timer> fadeInTimers = new ArrayList<Timer>(); //fadeIn
    private float alpha = 1f;
    private float fadeOutMinimum = 0f;

    JLabel textLabel;

    public SudokuModel theModel;

    public String value;

    public Color fontColor = Color.WHITE;
    public Color circleColor = Color.BLACK;
    
    public Circle(SudokuModel theModel, String value){
	super();

	setSize(new Dimension(50, 50));
	
	this.theModel = theModel;
	
    this.setOpaque(false);
	
	this.value = value;
	
	textLabel = new JLabel();

	textLabel.setHorizontalAlignment(SwingConstants.CENTER);
	textLabel.setVerticalAlignment(SwingConstants.CENTER);

	textLabel.setSize(new Dimension(getWidth(), getHeight()));

	System.out.println(getWidth() + " " + getHeight());
	
	textLabel.setFont(new Font("Serif", Font.PLAIN, 30));
	textLabel.setForeground(fontColor);
	//3, 2 pentru mac la paddinguri
	textLabel.setText("<html><div style=\"text-align: center; padding-top: 0px; padding-left: 2px;\">"+ value +"</div></html>");
	add(textLabel);	
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
	super.paintComponent(grphcs);
	
	Graphics2D g2d = (Graphics2D) grphcs;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

	// for circle

	////////// do not show any lines as we do not have any
	////////// row column or block selected
	
	if(theModel.theGrid.sudokuCells3Now[0] == null){
	    textLabel.setVisible(false);
	    return;
	} else {
	    textLabel.setVisible(true);
	}

	Color prevColor = new Color(circleColor.getRed(), circleColor.getGreen(), circleColor.getBlue(), (int)(alpha*255));
	
	g2d.setColor(prevColor);
	
	g2d.fillOval(0, 0, getWidth(), getHeight());

	prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));
	g2d.setColor(prevColor);
	g2d.fillOval(0, 0, getWidth(), getHeight());

	prevColor = new Color(circleColor.getRed(), circleColor.getGreen(), circleColor.getBlue(), (int)(alpha*255));
	
	g2d.setColor(prevColor);
	
	g2d.fillOval(1, 1, getWidth()-2, getHeight()-2);
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

    public void setCircleColor(Color theColor){
	this.circleColor = theColor;

	repaint();
    }

    public void setFontColor(Color colorName){
	this.fontColor = colorName;
	textLabel.setForeground(fontColor);
    }
}
