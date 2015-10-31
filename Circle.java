import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class Circle extends JPanel implements ActionListener{
    private float DELTA = 0.01f;
    public ArrayList<Timer> fadeOutTimers = new ArrayList<Timer>(); //fadeOut
    public ArrayList<Timer> fadeInTimers = new ArrayList<Timer>(); //fadeIn
    private float alpha = 1f;
    private float fadeOutMinimum = 0f;

    public SudokuModel theModel;

    public String value;

    private boolean fadingOut = true;
    
    public Circle(SudokuModel theModel, String value){
	super();

	this.theModel = theModel;
	
	//this.setPreferredSize(new Dimension(256, 96));
        this.setOpaque(false); // don't know why
        //this.setBackground(Color.WHITE);

	this.value = value;
	
	JLabel textLabel = new JLabel();
	textLabel.setFont(new Font("Serif", Font.PLAIN, 30));
	//textLabel.setVerticalAlignment(SwingConstants.CENTER);
	//textLabel.setSize(new Dimension(50, 50));
	textLabel.setText("<html><div style=\"text-align: center; padding-top: 5px; padding-left: 2px;\"><font color='white'>"+ value +"</font></div></html>");
	add(textLabel);

	// remove strange top padding
	//setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	//setLayout(new GridBagLayout());
	//setMinimumSize(new Dimension(50, 50));
	//setBackground(Color.WHITE);
	//setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
	/*
	Box box = new Box(BoxLayout.Y_AXIS);
	box.setMinimumSize(new Dimension(50, 50));
	box.add(Box.createVerticalGlue());
	//textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	//textLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
	box.add(textLabel);
	box.add(Box.createVerticalGlue());	
	add(box);
	*/
	
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
	g2d.fillOval(0, 0, getWidth(), getHeight());
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
	fadingOut = true;
	fadeOutMinimum = 0f;
	//timer1.start(); KEEP IT DELETED	
    }

    public void fadeOutALittle(){
	fadeOutMinimum = 0.30f;
	//theModel.timers.add(timer);/////////////////////////////////
	//timer1.start(); KEEP IT DELETED
    }
    
    public void fadeIn(){
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
