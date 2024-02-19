package MonteCarloMini;

import java.util.concurrent.RecursiveTask;

public class SearchParallel  {
    private int id;
    private int pos_row, pos_col;
    private TerrainArea terrain;
    private int steps; //number of steps to end of search
	private boolean stopped;
    
    //// Constructor to initialize search parameters
    public SearchParallel(int id, int pos_row, int pos_col, TerrainArea terrain) {
        this.id = id;
        this.pos_row = pos_row;
        this.pos_col = pos_col;
        this.terrain = terrain;
        this.stopped = false;
    }

     // Perform the search to find the lowest point in the valley
    public int find_valley() {
        int height = Integer.MAX_VALUE;  // Initialize the height to maximum
        TerrainArea.Direction next = TerrainArea.Direction.STAY_HERE; //// Initialize next step direction

        while (terrain.visited(pos_row, pos_col) == 0) {
            height = terrain.get_height(pos_row, pos_col);  // Get height at current position
            terrain.mark_visited(pos_row, pos_col, id);    // Mark current position as visited
            next = terrain.next_step(pos_row, pos_col);    // Determine next step direction

             // Update position based on next step direction
            switch (next) {
                case STAY_HERE:
                    return height;  // Valley found, return current height
                case LEFT:
                    pos_row--;
                    break;
                case RIGHT:
                    pos_row = pos_row + 1;
                    break;
                case UP:
                    pos_col = pos_col - 1;
                    break;
                case DOWN:
                    pos_col = pos_col + 1;
                    break;
            }
        }
        // Return the lowest height found
        return height;
    }
        // Getters for search properties
    public int getID() {
		return id;
	}

	public int getPos_row() {
		return pos_row;
	}

	public int getPos_col() {
		return pos_col;
	}
    public int getSteps() {
		return steps;
	}
	public boolean isStopped() {
		return stopped;
	}

	
}
