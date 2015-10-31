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

import java.awt.geom.Line2D;

public class Line extends JPanel implements ActionListener{
    private float DELTA = 0.01f;
    public ArrayList<Timer> fadeOutTimers = new ArrayList<Timer>(); //fadeOut
    public ArrayList<Timer> fadeInTimers = new ArrayList<Timer>(); //fadeIn
    private float alpha = 1f;
    private float fadeOutMinimum = 0f;

    public SudokuModel theModel;

    private boolean fadingOut = true;

    public int x1, x2, y1, y2;


    
    public Line(SudokuModel theModel, int x1, int y1, int x2, int y2){
	super();

	this.theModel = theModel;
	
        this.setOpaque(false); // don't know why
	this.setVisible(true);
	//this.setLayout(null);

	this.x1 = x1;
	this.x2 = x2;
	this.y1 = y1;
	this.y2 = y2;
	
	//g2d.setStroke(new BasicStroke(1));
	//g2d.draw(new Line2D.Float(x1, y1, x2, y2));
	/*
	JLabel jb = new JLabel();
	jb.setText("line");
	jb.setSize(50, 50);
	jb.setBounds(0, 0, 50, 50);
	jb.setLocation(0, 0);
	add(jb);
	*/
	setBackground(Color.BLUE);
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
	super.paintComponent(grphcs);
	
	Graphics2D g2d = (Graphics2D) grphcs;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

	alpha = 1f; //////////////
	Color prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));
	
	g2d.setColor(prevColor);
	g2d.setStroke(new BasicStroke(1));
	g2d.draw(new Line2D.Float(x1, y1, x2, y2));
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
	System.out.println("SET ALPHA ZERO");
	alpha = 0;

        repaint();
    }

    public void setAlphaOne(){
	System.out.println("SET ALPHA ONE");
	alpha = 1;

        repaint();
    }
    
    public void setOpaqueOn(){
	System.out.println("SET ALPHA ON");
	this.setOpaque(true);
    }

    public void setOpaqueOff(){
	System.out.println("SET ALPHA OFF");
	this.setOpaque(false);
    }
    
    public void fadeOut(){
	System.out.println("FADE OUT LINE");
	fadingOut = true;
	fadeOutMinimum = 0f;
	//timer1.start(); KEEP IT DELETED	
    }

    public void fadeOutALittle(){
	System.out.println("FADE OUT A LITTLE");
	fadeOutMinimum = 0.30f;
	//theModel.timers.add(timer);/////////////////////////////////
	//timer1.start(); KEEP IT DELETED
    }
    
    public void fadeIn(){
	System.out.println("line fading in");
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
