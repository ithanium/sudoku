import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import java.util.ArrayList;

public class SudokuCellListener extends MouseAdapter {
    public SudokuCellListener(){
	
    }

    public void mouseClicked(MouseEvent e) {
	SudokuCell source = (SudokuCell) e.getSource();
	SudokuModel theModel = source.theModel;

	SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
				    ArrayList<Timer> sameLevelTimers;
				    
				    protected Boolean doInBackground() throws Exception{
	if(source.notInTheGrid == true){
	    //System.out.println("No action assigned to cells in the right part of the GUI");
	    return true;
	}

	System.out.println("Clicked a clickable cell");
	
	// misleading, replace with i and j

	if(source.x == 1 && source.y == 1){
	    //System.out.println("Selecting block 0");
	    theModel.selected(0, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 1 && source.y == 4){
	    //System.out.println("Selecting block 1");
	    theModel.selected(1, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 1 && source.y == 7){
	    //System.out.println("Selecting block 2");
	    theModel.selected(2, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 4 && source.y == 1){
	    //System.out.println("Selecting block 3");
	    theModel.selected(3, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 4 && source.y == 4){
	    //System.out.println("Selecting block 4");
	    theModel.selected(4, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 4 && source.y == 7){
	    //System.out.println("Selecting block 5");
	    theModel.selected(5, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 7 && source.y == 1){
	    //System.out.println("Selecting block 6");
	    theModel.selected(6, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(source.x == 7 && source.y == 4){
	    //System.out.println("Selecting block 7");
	    theModel.selected(7, 2); // number + 0 = row, 1 = column, 2 = block

	    return true;
	}

	if(source.x == 7 && source.y == 7){
	    //System.out.println("Selecting block 8");
	    theModel.selected(8, 2); // number + 0 = row, 1 = column, 2 = block

	    return true;
	}

	if(e.getPoint().getX() <= 3 || (source.x > 0 && source.y * 50 + e.getPoint().getY()<50)){
	    //System.out.println("Selecting row " + source.x);
	    theModel.selected(source.x, 0); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	if(e.getPoint().getY() <= 3 || (source.y > 0 && source.x * 50 + e.getPoint().getX()<50)){
	    //System.out.println("Selecting column " + source.y);
	    theModel.selected(source.y, 1); // number + 0 = row, 1 = column, 2 = block
	    
	    return true;
	}

	return true;
	
				    }};
	worker.execute();

    }
    public void mousePressed(MouseEvent e) {
    
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    
}
