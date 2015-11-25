import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class SudokuCellListener extends MouseAdapter {
    public SudokuCellListener(){
	
    }

    public void mouseClicked(MouseEvent e) {
	SudokuCell source = (SudokuCell) e.getSource();

	if(source.notInTheGrid == true){
	    //System.out.println("No action assigned to cells in the right part of the GUI");
	    return;
	}

	// misleading, replace with i and j

	if(source.x == 1 && source.y == 1){
	    //System.out.println("Selecting block 0");
	    selected(source.theModel, 0, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 1 && source.y == 4){
	    //System.out.println("Selecting block 1");
	    selected(source.theModel, 1, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 1 && source.y == 7){
	    //System.out.println("Selecting block 2");
	    selected(source.theModel, 2, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 4 && source.y == 1){
	    //System.out.println("Selecting block 3");
	    selected(source.theModel, 3, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 4 && source.y == 4){
	    //System.out.println("Selecting block 4");
	    selected(source.theModel, 4, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 4 && source.y == 7){
	    //System.out.println("Selecting block 5");
	    selected(source.theModel, 5, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 7 && source.y == 1){
	    //System.out.println("Selecting block 6");
	    selected(source.theModel, 6, 2); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(source.x == 7 && source.y == 4){
	    //System.out.println("Selecting block 7");
	    selected(source.theModel, 7, 2); // number + 0 = row, 1 = column, 2 = block

	    return;
	}

	if(source.x == 7 && source.y == 7){
	    //System.out.println("Selecting block 8");
	    selected(source.theModel, 8, 2); // number + 0 = row, 1 = column, 2 = block

	    return;
	}

	if(e.getPoint().getX() <= 3 || (source.x > 0 && source.y * 50 + e.getPoint().getY()<50)){
	    //System.out.println("Selecting row " + source.x);
	    selected(source.theModel, source.x, 0); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}

	if(e.getPoint().getY() <= 3 || (source.y > 0 && source.x * 50 + e.getPoint().getX()<50)){
	    //System.out.println("Selecting column " + source.y);
	    selected(source.theModel, source.y, 1); // number + 0 = row, 1 = column, 2 = block
	    
	    return;
	}
	
    }
    public void mousePressed(MouseEvent e) {
    
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void selected(SudokuModel theModel, int number, int selectionType){
	Thread t = new Thread(new Runnable() {
		
		Timer WAIT_FOR_TIMER = null;
		
		@Override
		public void run() {
		    try{
			SwingUtilities.invokeAndWait(new Runnable() {
				// THIS IS NOW THE EDT
				// CAN'T WAIT FOR TIMER HERE
				// AS TIMER ALSO RUNS ON EDT
				public void run() {
				    if(selectionType == 0){ //row
					//theModel.fadeOutAllExceptRow(number);
				
					WAIT_FOR_TIMER = theModel.moveRow(number, true);
		        
				    }
								    
				    if(selectionType == 1){ //column
					//theModel.fadeOutAllExceptColumn(number);
					theModel.moveColumn(number, true);
				    }
				    
				    if(selectionType == 2){ //block
					//theModel.fadeOutAllExceptBlock(number);
					theModel.moveBlock(number, true);
				    }

				    //theModel.viewController.theEdges.setAlphaOne();
				    //theModel.viewController.theEdges.repaint();
				}
			    });
		    } catch (Exception e2){
			e2.printStackTrace();
		    }
		    /*
		    // MAKE IT FOR UPPER LEVEL
		    synchronized (WAIT_FOR_TIMER) {
			try {
			    WAIT_FOR_TIMER.wait();
			} catch (InterruptedException ex) {
			}
		    }
		    */
		    System.out.println("Moving row finished");

		    try{
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
				    System.out.println("Fading started - fake");
				    if(theModel.SLEEP == 0 && theModel.SLEEP_BETWEEN_STEPS == 0){
					theModel.fadeInGraphNow();
					// UN WAIT ON THE LEVEL VA CHEMA
					// TREZIREA DE DOUA ORI PE ACEST
					// THREAD
					// SAU MAI BINE FACEM TREZIREA
					// DOAR CAND TERMINA NIVELUL
					//
					// DA
					//
					// TREZESTE DOAR CAND TERMINA NIVELUL
					//
					// CREAD CA ASTA SE FACE DIN
					// MODEL: START NEXT TIMERS
				    } else {
					theModel.fadeInGraph();
				    }
				    System.out.println("Fading ended - fake");
				    //theModel.scheduleSolveFF();
				}
			    });
		    } catch (Exception e2){
			e2.printStackTrace();
		    }

		    System.out.println("Finished  row");
		    
		}});
	t.start();
	
    }
}
