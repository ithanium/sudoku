import java.util.*;
import java.awt.Color;

public class FordFulkerson {

    int[][] A;         // adjacency matrix, will be used only for displaying output
    int[][] R;         //  residual graph
    int n;             // order of graph
    int[] pred;        // predecessor of a vertex, describes augmented path
    boolean[] visited; // visited?, used in bfs
    Queue<Integer> Q;  // for bfs
    boolean trace;     // want a trace?

    SudokuModel theModel;
    ArrayList<TripletIIB> moveSteps;

    boolean finishedGreedyMatch = false;
    int lastVarVisited = 0;

    int skippedTwoGreensCount = 0;
    
    FordFulkerson (SudokuModel theModel, int[][] A, int[][] R, int n){
	this.theModel = theModel;

	this.A = A;
	this.R = R;
	this.n = n;

	pred = new int[n];
	visited = new boolean[n];
	Q = new LinkedList<Integer>();
    } 

    boolean bfs(){
	Q.clear();
	Arrays.fill(pred,-1);
	Arrays.fill(visited,false);
	Q.add(0);
	visited[0] = true;
	
	while (!Q.isEmpty()){
	    int v = Q.remove();

	    if (v == n-1){
		return true; // path from source (0) to sink (n-1) found
	    }
	    
	    for (int w=0;w<n;w++){
		if (!visited[w] && R[v][w] > 0){
		    pred[w] = v;
		    Q.add(w);
		    visited[w] = true;
		}
	    }
	}
	
	return false;	
    }
    //
    // using breadth first search, find an augmenting path from source to sink
    // - returns true if augmenting path found
    // - augmenting path is traced backwards from sink to source via predecessor
    //   links in array pred
    //

    int minCost(){
	int minCost = Integer.MAX_VALUE;
	int v = n-1;
	while (pred[v] != -1){minCost = Math.min(minCost,R[pred[v]][v]); v = pred[v];}
	return minCost;
    }	
    //
    // return the smallest residual flow in the augmenting path found by bfs
    //

    void updateR(){
	int f = minCost();
	int v = n-1;

	moveSteps = new ArrayList<TripletIIB>();
	
	while (pred[v] != -1){
	    int u = pred[v];

	    // BEGIN GREEDY MATCH CODE
	    if(u>0 && u<10){
		if((u < lastVarVisited && !finishedGreedyMatch) || u == 9){
		    finishedGreedyMatch = true;
		    System.out.println("FINISHED GREEDY");
		    theModel.viewController.theEdges.drawMovesNow();
		    if(u!=9){
			//theModel.viewController.theEdges.drawPath(displayPath());
		    }
		} else {
		    lastVarVisited = u;
		    System.out.println("lastVarVisited: " + lastVarVisited);
		}
	    }
	    System.out.println("u: " + u);
	    // END GREEDY MATCH CODE
	    
	    R[u][v] = R[u][v] - f; // residual capacity
	    R[v][u] = R[v][u] + f; // augmented flow
		    
	    if(u<v){
		if(finishedGreedyMatch && skippedTwoGreensCount>1){
		    System.out.println("Draw u:"+u + " v:"+v);
		    moveSteps.add(new TripletIIB(u, v, Color.GREEN));
		} else {
		    System.out.println("Skip draw u:"+u + " v:"+v);
		}
	    } else {
		if(finishedGreedyMatch){
		    moveSteps.add(new TripletIIB(v, u, Color.RED));
		}
	    }

	    if(finishedGreedyMatch){
		skippedTwoGreensCount++;
	    }
	    
	    v = u;
	}

	Collections.reverse(moveSteps);
	theModel.viewController.theEdges.storeMoves(moveSteps);
    }
    //
    // having computed an augmenting path via bfs update flows and residues
    //

    void fordFulkerson(){
	while (bfs()){
	    if (trace){
		if(finishedGreedyMatch){
		    theModel.viewController.theEdges.drawPath(displayPath()); //////////
		} else {
		    theModel.viewController.theEdges.drawPathNow(displayPath()); //////////		    
		}
		//System.out.println(" cost: "+ minCost());
	    }

	    updateR();

	    if (trace){
		//display();
		//System.out.println();
	    }
	}
    }
    //
    // while there is an augmenting path (bfs is true and path is in the array pred) 
    // update the residual graph
    //

    void display(){
	for (int i=0;i<n;i++){
	    System.out.print(i +": ");
	    for (int j=0;j<n;j++) System.out.print(R[i][j] + " ");
	    System.out.println();
	}
    }
    //
    // display residual graph R
    //

    void displayFlows(){
	System.out.println("flows");
	for (int i=0;i<n;i++){
	    System.out.print(i +": ");
	    for (int j=0;j<n;j++)
		System.out.print((A[i][j] * R[j][i]) + " ");
	    System.out.println();
	}
    }
    //
    // if there is an edge A[u][v] then its flow is R[v][u]
    //

    ArrayList<Integer> displayPath(){
        ArrayList<Integer> path = new ArrayList<Integer>();
	
	int i = n-1;
	while (i != -1){
	    //System.out.print(i + " ");
	    path.add(i);
	    i = pred[i];
	}

	Collections.reverse(path);

	return path;
    }
    //
    // display augmenting path held in pred array
    //
    
    public void run() {
	this.trace = true; ////////
	//this.display();
	//System.out.println();
	this.fordFulkerson();
	//this.displayFlows();

	for(int i=0; i<n; i++){
	    for(int j=0; j<n; j++){
		if(i > 0 && i < 10 && A[i][j] == 1){
		    if(A[i][j] * R[j][i] == 1){
			//System.out.println("Edge between i:" + i + " j:" + j);
		    }
		}
	    }
	}
    }
}
