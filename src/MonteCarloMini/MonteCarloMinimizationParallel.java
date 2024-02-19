package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MonteCarloMinimizationParallel extends RecursiveAction{
	 // Constants
    static final boolean DEBUG=false;
	//Sequential cutoff for the parallel algorithm in compute()
	private static int CUTOFF = 100;

	static int i;
	static long startTime = 0;
	static long endTime = 0;

	    // Variables for parallel computation
	 SearchParallel[] searches;
     int start;
	 int end;

     //terrain: object to store the heights and grid points visited by searches	 
    static private TerrainArea terrain; 
	static int rows, columns; //grid size
    static double xmin, xmax, ymin, ymax; //x and y terrain limits

	static double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!
	static int num_searches;		// Number of searches
    static int min=Integer.MAX_VALUE;
    static int local_min=Integer.MAX_VALUE;
    static int finder =-1;
   
	//constructor
    public MonteCarloMinimizationParallel(SearchParallel[] searches,int start , int end){
          this.searches = searches;
		  this.start = start;
		  this.end = end;
	}
	
		@Override
	protected void compute() { 
		// Perform local search in this segment
		if (end - start <= CUTOFF) {
            for (int i = start; i < end; i++) { //iterates through its assigned range of searches and finds the local minimum in each grid.
                int local_min = searches[i].find_valley();
                if ((!searches[i].isStopped()) && (local_min < min)) {
                    min = local_min;
                    finder = i; // Keep track of who found it
                }
                if (DEBUG) {
                    System.out.println("Search " + searches[i].getID() + " finished at " + local_min + " in " + searches[i].getSteps());
                }
            }
		} else {
			// Split the work and recursively invoke compute
		int split = (int) (start + end)/2;
        MonteCarloMinimizationParallel left = new MonteCarloMinimizationParallel(searches,start , split);
		MonteCarloMinimizationParallel right = new MonteCarloMinimizationParallel(searches, split, end);

		left.fork(); //The left subtask is forked using the fork method, 
		            //and then the right subtask is computed sequentially using the current thread
    		
		right.compute();
		left.join();
		//main thread waits for the left subtask to complete using join method
		// so that the results from both subtasks are combined correctly
	}
}

	
    
	//timers - note milliseconds
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static void tock(){
		endTime=System.currentTimeMillis(); 
	}
	
    public static void main(String[] args){
    	// Array of searches
    	Random rand = new Random();

        if (args.length!=7) {  
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}
    	/* Read argument values */

     	
    	rows =Integer.parseInt( args[0] )  ;
    	columns = Integer.parseInt( args[1] ) ;
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] )  ;
	

        if(DEBUG) {
    		/* Print arguments */
    		System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
    		System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
    		System.out.printf("Arguments, searches_density: %f\n", searches_density );
    		System.out.printf("\n");
    	}
    	
		// Initialize 
    	terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
		int num_searchescalc = (int) (rows * columns * searches_density);
    	SearchParallel[] searches= new SearchParallel [num_searches];

		// Generating random search positions and creating SearchParallel objects
    	for (int i=0;i<num_searches;i++) 
    		searches[i]=new SearchParallel(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
            // Create a new SearchParallel object with unique ID, random row and column positions, and the terrain


		tick();
		// Creating a new MonteCarloMinimizationParallel task for parallel computation
		MonteCarloMinimizationParallel para = new MonteCarloMinimizationParallel(searches,0,  num_searches);
        
		// Invoking the parallel computation, which triggers the recursive computation in compute() method
		ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(para);

        tock();

        if (DEBUG) {
            terrain.print_heights();
            terrain.print_visited();
        }

        System.out.printf("Run parameters\n");
        System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
        System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax);
        System.out.printf("\t Search density: %f (%d searches)\n", searches_density, num_searchescalc);
        System.out.printf("Time: %d ms\n", endTime - startTime);

        int tmp = terrain.getGrid_points_visited();
        System.out.printf("Grid points visited: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");
        tmp = terrain.getGrid_points_evaluated();
        System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");

        

        System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n", min, terrain.getXcoord(searches[finder].getPos_row()), terrain.getYcoord(searches[finder].getPos_col()));
	
    }

}
