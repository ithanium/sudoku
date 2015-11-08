import java.util.*;
import java.awt.Color;

public class Tarjan {
    
    SudokuModel theModel;
    int[][] A;         // adjacency matrix, will be used only for displaying output
    int n;             // order of graph
    int pre = 0;
    int count = 0;

    Stack<Integer> S;  // stack for algo
    boolean[] stacked; // visited?, used in bfs
    
    int[] id, low;

    Tarjan (SudokuModel theModel, int[][] A, int n){
	this.theModel = theModel;

	this.A = A;
	this.n = n; // 18 vertices = 9 + 9

	S = new Stack<Integer>();
	stacked = new boolean[n];
	
	id = new int[n];
	low = new int[n];

	for (int v = 0; v < n; v++) {
            if(!stacked[v]){
		dfs(v);
	    }
        }
    } 

    void dfs(int u){
	stacked[u] = true;

	low[u] = pre;
	pre = pre + 1;
	int min = low[u];

	//System.out.println("low["+u+"]: " + min);
	
	S.push(u);
	//System.out.println("Stacked: " + u);
	//System.out.print("A["+u+"]: ");
	
	for(int i=0; i<A[u].length; i++){
	    int w = A[u][i];
	    //System.out.print(w + " VS " + i);

	    if(w == 1 && u!=i){ //// u!=i !!!!
		if(!stacked[i]){
		    //System.out.print("A["+u+"]"+i + " ");
		    dfs(i);
		} else if (u != w){ /// !!!!!!!! https://www.seas.gwu.edu/~simhaweb/alg/lectures/module7/module7.html
		    //System.out.println("u != w");
		}
		
		if(low[i] < min){
		    min = low[i];
		    //System.out.println("\nlow[" + i + "]: " + min);
		}
	    }
	}

	//System.out.println();
	
	if(min < low[u]){
	    //System.out.println("min " + min + " < low["+u+"] " + low[u]);
	    low[u] = min;
	    //System.out.println("\nlow[" + u + "]: " + low[u] + "(min) RETURN");
	    return;
	}
	
	int w;
	
	do{
	    w = S.pop();
	    //stacked[w] = false; /////////////// ?
	    //System.out.println("Popped: " + w + " when u: " + u);
	    id[w] = count;
	    low[w] = n;
	    //System.out.println("low[" + w + "]: " + n + " DO/WHILE");
	} while (w != u);
	
	count = count + 1;
	//System.out.println("Count now: " + count);
    
    }

    // Returns the number of strongly connected components
    public int count() {
        return count;
    }
    
    // Checks if u and w are in the same component
    public boolean stronglyConnected(int u, int w) {
	return id[u] == id[w];
    }

    // Returns the id of the strongly connected component containing the vertex
    public int id(int v) {
        return id[v];
    }
    
    public ArrayList<ArrayList<Integer>> run() {
	// number of strongly connected components
        int scc_count = count();
        System.out.println(scc_count + " strongly connected components");

	ArrayList<ArrayList<Integer>> components = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < scc_count; i++) {
            components.add(new ArrayList<Integer>());
        }
	
        for (int v = 0; v < n; v++) {
            components.get(id[v]).add(v);
        }
	
	// Print the strongly connected components
        for (int i = 0; i < scc_count; i++) {
	    System.out.print("Component " + i + ": ");
	    
            for (int v : components.get(i)) {
                System.out.print(v + " ");
            }
	    
            System.out.println();
        }

	return components;
    }
    
}
