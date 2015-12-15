import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuCellListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
	SudokuCell source = (SudokuCell) e.getSource();
	SudokuModel theModel = source.theModel;

	Thread t = new Thread(new Runnable() {
		@Override
		public void run() {
		    if(source.notInTheGrid == true){
			//System.out.println("No action assigned to cells in the right part of the GUI");
			return;
		    }

		    System.out.println("Clicked a clickable cell");
	
		    // misleading, replace with i and j

		    if(source.x == 1 && source.y == 1){
			//System.out.println("Selecting block 0");
			theModel.select(0, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 1 && source.y == 4){
			//System.out.println("Selecting block 1");
			theModel.select(1, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 1 && source.y == 7){
			//System.out.println("Selecting block 2");
			theModel.select(2, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 4 && source.y == 1){
			//System.out.println("Selecting block 3");
			theModel.select(3, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 4 && source.y == 4){
			//System.out.println("Selecting block 4");
			theModel.select(4, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 4 && source.y == 7){
			//System.out.println("Selecting block 5");
			theModel.select(5, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 7 && source.y == 1){
			//System.out.println("Selecting block 6");
			theModel.select(6, 2); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(source.x == 7 && source.y == 4){
			//System.out.println("Selecting block 7");
			theModel.select(7, 2); // number + 0 = row, 1 = column, 2 = block

			return;
		    }

		    if(source.x == 7 && source.y == 7){
			//System.out.println("Selecting block 8");
			theModel.select(8, 2); // number + 0 = row, 1 = column, 2 = block

			return;
		    }

		    if(e.getPoint().getX() <= 3 || (source.x > 0 && source.y * 50 + e.getPoint().getY()<50)){
			//System.out.println("Selecting row " + source.x);
			theModel.select(source.x, 0); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		    if(e.getPoint().getY() <= 3 || (source.y > 0 && source.x * 50 + e.getPoint().getX()<50)){
			//System.out.println("Selecting column " + source.y);
			theModel.select(source.y, 1); // number + 0 = row, 1 = column, 2 = block
	    
			return;
		    }

		}			
	    });
	t.start();
    }
}
