/*
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

public class Square extends JPanel implements ActionListener{
    private float DELTA = 0.01f;
    private Timer timer = new Timer(10, null);
    private float alpha = 1f;

    private boolean fadingOut = true;
    
    public Square(){
	//this.setPreferredSize(new Dimension(256, 96));
        this.setOpaque(false); // don't know why
        //this.setBackground(Color.black);
        timer.setInitialDelay(100);
        timer.addActionListener(this);
	timer.setCoalesce(false);
	
	JLabel textLabel2 = new JLabel();
	textLabel2.setFont(new Font("Serif", Font.PLAIN, 14));
	//textLabel2.setVerticalAlignment(SwingConstants.CENTER);
	//textLabel2.setSize(new Dimension(50, 50));
	textLabel2.setText("<html><div style=\"text-align: center; padding-top: 3px; padding-left: 1px;\"><font color='BLACK'>1 2 3<br>4 5 6<br>7 8 9</font></div></html>");
	
	setLayout(new GridBagLayout());
	setMinimumSize(new Dimension(50, 50));
	//setBackground(Color.WHITE);

	add(textLabel2);
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
	super.paintComponent(grphcs);
	
	Graphics2D g2d = (Graphics2D) grphcs;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	
	int height = 50;
	int width = 50;
	
	Color prevColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), (int)(alpha*255));
	
	g2d.setColor(prevColor);
	
	//g2d.drawRect(0, 0, width-1, height-1);
	setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, prevColor));

	
    }
    
    @Override
    public Dimension getPreferredSize() {
	return new Dimension(50, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	//System.out.println(e.getSource());

	if(fadingOut){
	    alpha -= DELTA;
	} else {
	    alpha += DELTA;
	}
	
        if (alpha < 0) {
            alpha = 0;
            timer.stop();
        }

	if (alpha > 1) {
            alpha = 1;
            timer.stop();
        }
		
        repaint();
    }

    public void setAlphaZero(){
	alpha = 0;

        repaint();
    }
    
    public void fadeOut(){
	fadingOut = true;
        timer.start();
    }

    public void fadeIn(){
	fadingOut = false;
        timer.start();
    }

    public void setOpaqueOn(){
	this.setOpaque(true);
    }

    public void setOpaqueOff(){
	this.setOpaque(false);
    }
}
*/
