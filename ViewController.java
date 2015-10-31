import javax.swing.*;
import java.awt.*;

public class ViewController extends JLayeredPane { // or JLayerPane?

    public SudokuModel theModel;
    public SudokuEdges theEdges; // 1st layer with edges between cells and values
    public SudokuGrid theGrid; // 2nd layer with the SudokuCells and Circles

    public static final int SIZE = 9; // make this common accross
    
    public ViewController(SudokuModel theModel){
	super();

	this.theModel = theModel;
	
	//System.out.println("View controller made");

	setPreferredSize(new Dimension(950, 550-4));
	//this.layeredPane.setBackground(Color.ORANGE);
	//this.layeredPane.setLayout(new LayeredPaneLayout(layeredPane));
	setLayout(new BorderLayout());
	
	theEdges = new SudokuEdges(theModel);
	theEdges.setPreferredSize(new Dimension(950, 550-4));
	
	theGrid = new SudokuGrid(theModel, SIZE, SIZE);
	//theEdges.setPreferredSize(new Dimension(950, 550));

	theModel.setGrid(theGrid);

	theModel.setViewController(this);
	
	//this.layeredPane.add(theEdges, new Integer(0));
	//this.layeredPane.add(theGrid, new Integer(1));
	/*
	theEdges.setSize(950, 550-4);
	theGrid.setSize(950, 550-4);

	theEdges.setBounds(0, 0, 950, 550-4);
	theGrid.setBounds(0, 0, 950, 550-4);
	*/
	theEdges.setOpaque(false);
	theGrid.setOpaque(false);
	add(theEdges, 0, 0, 0, 950, 550-4);
	add(theGrid, 0, 0, 1, 950, 550-4);
	//this.layeredPane.validate();
    }

    public void setModel(SudokuModel theModel){
	this.theModel = theModel;
    }

    public void add (JComponent jc, int x, int y, int z, int width, int height){
	jc.setLocation(x, y);
	//jc.setSize(new Dimension(width, height));
	jc.setBounds(new Rectangle(new Point(x, y), new Dimension(width, height)));
	setLayer(jc, new Integer(z));
	add(jc);
	jc.setVisible(true);
	
	//repaint(); ////// need this?
	
    }

    public void showWelcomeScreen(){
	theEdges.showWelcomeScreen();
    }

    public void hideWelcomeScreen(){
	theEdges.hideWelcomeScreen();
    }
}
