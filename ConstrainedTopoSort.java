// Jonathon Sauers
// jo046326
// COP 3503, Fall 2017
// ConstrainedTopoSort.java

import java.util.*;
import java.io.*;

public class ConstrainedTopoSort
{
  boolean [][] matrix;

  // Determines if a given graph has a topological sort in which
  // some vertex, x, will come before another given vertex, y.
  public ConstrainedTopoSort(String filename) throws IOException
  {
    Scanner in = new Scanner(new File(filename));
    int N = in.nextInt();

    // Set the dimensions for the 2D adjacency matrix.
    matrix = new boolean[N][N];

    // Fills the matrix.
    for(int i = 0; i < N; i++)
    {
      int temp = in.nextInt();

			for(int j = 0; j < temp; j++)
      {
        int value = in.nextInt();
				matrix[i][value - 1] = true;
      }
    }

    in.close();
  }

  // Only used to verify that there is a topological sort for
  // a given graph.
  public boolean checkForTopoSort()
  {
  	int [] incoming = new int[matrix.length];
  	int cnt = 0;

  	// Count the number of incoming edges incident to each vertex.
  	for (int i = 0; i < matrix.length; i++)
  		for (int j = 0; j < matrix.length; j++)
  			incoming[j] += (matrix[i][j] ? 1 : 0);

  	Queue<Integer> q = new ArrayDeque<Integer>();

  	// Any vertex with zero incoming edges is ready to be visited, so add it to
  	// the queue.
  	for (int i = 0; i < matrix.length; i++)
  		if (incoming[i] == 0)
  			q.add(i);

  	while (!q.isEmpty())
  	{
  		// Pull a vertex out of the queue and add it to the topological sort.
  		int node = q.remove();

  		++cnt;

  		// All vertices we can reach via an edge from the current vertex should
  		// have their incoming edge counts decremented. If one of these hits
  		// zero, add it to the queue, as it's ready to be included in our
  		// topological sort.
  		for (int i = 0; i < matrix.length; i++)
  			if (matrix[node][i] && --incoming[i] == 0)
  				q.add(i);
  	}

  	// There is a cycle. i.e. no topological sort
  	if (cnt != matrix.length)
  		return false;

    // There is a valid topological sort.
    return true;
  }

  // Checks the ordering of x and y to determine if a graph has a
  // constrained topological sort.
  public boolean hasConstrainedTopoSort(int x, int y)
  {
    // If there is no valid topological sort, then this input graph fails.
    if(checkForTopoSort() == false) return false;

    Queue<Integer> q = new ArrayDeque<Integer>();
    HashSet<Integer> set = new HashSet<>();
    boolean [] visited = new boolean[matrix.length];

    // We start at vertex y. If we see vertex x when permforming a BFS,
    // then there is no constrained topo sort.
    // y is added to the queue, followed by all of the vertices y can get to.
    q.add(y - 1);
    set.add(y - 1);
    visited[y - 1] = true;

    // While y still has vertices it can get to, continue.
    while (!q.isEmpty())
    {
      // Remove a node from the queue and process it.
      int node = q.remove();

      // Add all neighbors of 'node' to the queue (as long as they haven't
      // been visited already).
      for (int i = 0; i < matrix.length; i++)
        if (matrix[node][i] && !visited[i])
        {
          visited[i] = true;
          q.add(i);
          set.add(i);
        }
    }

    // If we can get to x from y, this is a fail state.
    if(set.contains(x - 1))
      return false;

    // Otherwise, x will come before y.
    return true;
  }

  // How difficult I found this assignment.
  public static double difficultyRating()
  {
    return 2.5;
  }

  // How many hours I spent on this assignment.
  public static double hoursSpent()
  {
    return 5.0;
  }
}
