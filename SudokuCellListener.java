import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuCellListener extends MouseAdapter {
    public SudokuCellListener(){
	
    }

    public void mouseClicked(MouseEvent e) {
	SudokuCell source = (SudokuCell) e.getSource();

	if(source.notInTheGrid == true){
	    System.out.println("No action assigned to cells in the right part of the GUI");
	    return;
	}

	// misleading, replace with i and j

	if(source.x == 1 && source.y == 1){
	    System.out.println("Selecting block 0");
	    source.theModel.moveBlock(0, true);
	    return;
	}

	if(source.x == 1 && source.y == 4){
	    System.out.println("Selecting block 1");
	    source.theModel.moveBlock(1, true);
	    return;
	}

	if(source.x == 1 && source.y == 7){
	    System.out.println("Selecting block 2");
	    source.theModel.moveBlock(2, true);
	    return;
	}

	if(source.x == 4 && source.y == 1){
	    System.out.println("Selecting block 3");
	    source.theModel.moveBlock(3, true);
	    return;
	}

	if(source.x == 4 && source.y == 4){
	    System.out.println("Selecting block 4");
	    source.theModel.moveBlock(4, true);
	    return;
	}

	if(source.x == 4 && source.y == 7){
	    System.out.println("Selecting block 5");
	    source.theModel.moveBlock(5, true);
	    return;
	}

	if(source.x == 7 && source.y == 1){
	    System.out.println("Selecting block 6");
	    source.theModel.moveBlock(6, true);
	    return;
	}

	if(source.x == 7 && source.y == 4){
	    System.out.println("Selecting block 7");
	    source.theModel.moveBlock(7, true);
	    return;
	}

	if(source.x == 7 && source.y == 7){
	    System.out.println("Selecting block 8");
	    source.theModel.moveBlock(8, true);
	    return;
	}

	if(e.getPoint().getX() <= 3 || (source.x > 0 && source.y * 50 + e.getPoint().getY()<50)){
	    System.out.println("Selecting row " + source.x);
	    source.theModel.moveRow(source.x, true);
	    return;
	}

	if(e.getPoint().getY() <= 3 || (source.y > 0 && source.x * 50 + e.getPoint().getX()<50)){
	    System.out.println("Selecting column " + source.y);
	    source.theModel.moveColumn(source.y, true);
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
}
