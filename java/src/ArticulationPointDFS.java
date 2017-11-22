import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import mooc.EdxIO;

public class ArticulationPointDFS
{
	// Contains the graph's list of vertexes.
	private Vertex[] graph;

	// Information about the graph (vertices, edges, and biconnected components).
	private int numVertices, numEdges, numBCC;

	// Used to assign DFS numbers to each vertex.
	private int dfsCounter;

	// Holds various lists.
	private ArrayList<Vertex> articulationPointList;
	private ArrayList<BiconnectedComponent> bccList;
	
	// The stack used to hold upcoming vertices for DFS.
	private Stack<Edge> visited;

	private SparseArray[] map;
	
	/**
	 * Create a new graph for articulation point search.
	 */
	public ArticulationPointDFS(EdxIO io)
	{
		// Get the number of vertices and edges from the first line.
	 	numVertices = io.nextInt();
	 	numEdges = io.nextInt();

	 	map = new SparseArray[numEdges];
	 	
		// Set up the basic graph.
		graph = new Vertex[numVertices];
		for(int i = 0; i < numVertices; i++)
		{
			graph[i] = new Vertex(i);
		}

		// Add each edge to the graph.
		for(int i = 0; i < numEdges; i++)
		{
			int origin = io.nextInt() - 1;
			int destination = io.nextInt() - 1;

			addEdge(origin, destination);
			
			if (map[origin] == null) {
				map[origin] = new SparseArray(numVertices);
			}
			
			map[origin].store(destination, i);
		}

		// Setup the other various lists and structures used to traverse
		// this graph.
		articulationPointList = new ArrayList<Vertex>();
		bccList = new ArrayList<BiconnectedComponent>();
		visited = new Stack<Edge>();
		dfsCounter = 1;
		numBCC = 1;
	}


	/**
	 * Add an edge to the list of edges in the graph.
	 */
	public void addEdge(int origin, int destination)
	{
		Vertex startNode = graph[origin];
		Vertex endNode = graph[destination];

		// Add the respective vertice to the edge list of the opposite vertice.
		startNode.eList.add(destination);
		endNode.eList.add(origin);
	}


	/**
	 * Perform recursive DFS starting at vertex.
	 * All information needed to find articulation points and BCCs will
	 * be stored and updated.
	 */
	public void doArticulationPointDFS(Vertex vertex)
	{
        if(vertex == null)
        {
            return;
        }

		// Set DFS info. of vertex.
		vertex.dfsNum = dfsCounter++;
		vertex.low = vertex.dfsNum;

		// Run through all unvisited, connected nodes.
		Iterator<Integer> iter = vertex.eList.iterator();

		while(iter.hasNext())
		{
            Integer nextVertexId = (Integer)iter.next();
			Vertex nextVertex = graph[nextVertexId];

            // if the nextVertex is unvisited...
            if(nextVertex.dfsNum == -1)
            {
                // The next vertex is one level higher than the current vertex.
                nextVertex.dfsLevel = vertex.dfsLevel + 1;
                vertex.numChildren += 1;

                // Add this edge to the stack.
                visited.push(new Edge(vertex, nextVertex));

                // Recursively perform DFS on the children nodes of the current vertex.
                doArticulationPointDFS(nextVertex);


                // The lowest DFS level of the current node is either its current value or the value of nextNode.
                vertex.low = Math.min(vertex.low, nextVertex.low);


                // Determine if any articulation points exist based on lowest value/dfs level.
                // We're at the root of the dfs tree...
                if(vertex.dfsNum == 1)
                {
                    if(vertex.numChildren >= 2)
                    {
                        if(!articulationPointList.contains(vertex))
                        {
                            articulationPointList.add(vertex);
                        }
                    }

                    retrieveBCCEdges(vertex, nextVertex);
                }

                // Otherwise, see if vertex is an articulation point separating nextVertex.
                else
                {
                    if(nextVertex.low >= vertex.dfsNum)
                    {
                        if(!articulationPointList.contains(vertex))
                        {
                            articulationPointList.add(vertex);
                        }

                        retrieveBCCEdges(vertex, nextVertex);
                    }
                }
            }

            // otherwise, if its dfs level is lower than the current vertex's...
            // therefore, (vertex, nextVertex) is a back edge and should be examined.
            else if(nextVertex.dfsLevel < vertex.dfsLevel - 1)
            {
                vertex.low = Math.min(vertex.low, nextVertex.dfsNum);
                visited.push(new Edge(vertex, nextVertex));
            }
        }
    }

    /**
     * The overloaded form of doArticulationPointDFS(), allowing the user to start calling DFS at the start node.
     */
    public void doArticulationPointDFS()
    {
        Vertex vertex;

        if(this.graph.length == 0)
        {
            vertex = null;
        }

        else
        {
            vertex = this.graph[0];
        }

        doArticulationPointDFS(vertex);
    }

    /**
     * Retrieve all edges in a biconnected component.  All edges in a BCC canbe traced back whenever an articulation
     * point is found (because DFS is a recursive algorithm using a global stack).
     */
    public void retrieveBCCEdges(Vertex articulationPoint, Vertex child)
    {
        BiconnectedComponent bcc = new BiconnectedComponent(numBCC++);

        Edge top = (Edge)visited.peek();

        // Until the top of the stack is (articulationPoint,child), trace back and build a list of all edges in the
        // bcc.
        while(!top.equal(articulationPoint, child))
        {
            Edge edge = (Edge)visited.pop();
            bcc.edgeList.add(edge);
            top = (Edge)visited.peek();
        }

        Edge edge = (Edge)visited.pop();
        bcc.edgeList.add(edge);

        bccList.add(bcc);
    }


    /**
     * Show all the results of DFS.
     */
    public void showResults(EdxIO io)
    {
//        System.out.println("Number of vertices: " + numVertices);
//        System.out.println("Number of edges: " + numEdges);
//        System.out.println("Number of articulation points: " + articulationPointList.size());

//        if(articulationPointList.size() > 0)
//        {
//            System.out.print("==> ");
//
//            Iterator<Vertex> iter = articulationPointList.iterator();
//            while(iter.hasNext())
//            {
//                Vertex vertex = (Vertex)iter.next();
//                System.out.print(vertex.id + " ");
//            }
//
//            System.out.println();
//        }

//        System.out.println("Number of biconnected components: " + bccList.size());
        io.println(bccList.size());

        if(bccList.isEmpty())
        {
            System.out.println("The graph is biconnected since no articulation points exist.");
        }

        else
        {
//            System.out.print("==> ");

            int[] o = new int[numEdges];
            
            for(int i = 0; i < bccList.size(); i++)
            {
                BiconnectedComponent bcc = (BiconnectedComponent)bccList.get(i);

//                System.out.print("\nComponent " + bcc.id + ": ");
                Iterator<Edge> iter = bcc.edgeList.iterator();
                while(iter.hasNext())
                {
                  Edge edge = (Edge)iter.next();
                	o[(int)map[edge.first.id].fetch(edge.second.id)] = i+1;
//                    System.out.print(edge + " ");
                }
            }

            for (int i = 0; i < o.length; i++) {
				io.print(o[i] + " ");
			}
        }
    }
    
    public static void main(String[] args) {
    	try (EdxIO io = EdxIO.create()) {

    		ArticulationPointDFS AP = new ArticulationPointDFS(io);
    		AP.doArticulationPointDFS();
    		AP.showResults(io);
    	}

	}
}

class Vertex
{
	int id;
	
	// the lowest tree level reachable from this verte// the lowest tree level reachable from this vertexx
	int low;		

	int dfsNum;
	// tree level of this vertex in DFS
	int dfsLevel;

	int numChildren;

	// list of edges involving this vertex
	LinkedList<Integer> eList;	

	// Create a vertex with given ID number.
	public Vertex(int id)
	{
		this.id = id;
		dfsNum = -1;					// Initially not involved in DFS.
		eList = new LinkedList<Integer>();	// list of adjacency edges is initially empty.
	}
}

class BiconnectedComponent
{
	int id;
	
	// Stores the list of edges in this BCC.
	LinkedList<Edge> edgeList;


	public BiconnectedComponent(int id)
	{
		this.id = id;
		edgeList = new LinkedList<Edge>();
	}
}

class Edge
{
	// Two vertices comprising this edge.
	Vertex first, second;	

	
	// Create a new edge with the given vertices.
	public Edge(Vertex first, Vertex second)
	{
		this.first = first;
		this.second = second;
	}

	// Return the string representation of the edge in the form of a pair of vertices.
	public String toString()
	{
		return "(" + first.id + ", " + second.id + ")";
	}

	// Check if two edges are equal by comparing the vertices.
	boolean equal(Vertex a, Vertex b)
	{
		return (a.id == first.id) && (b.id == second.id);
	}
}

class MyList
{
    private int index;
    private Object value;
    private MyList nextindex;
 
    public MyList(int index)
    {
        this.index = index;
        nextindex = null;
        value = null;
    }
 
    public MyList()
    {
        index = -1;
        value = null;
        nextindex = null;
    }
 
    public void store(int index, Object value)
    {
    	MyList current = this;
    	MyList previous = null;
 
    	MyList node = new MyList(index);
        node.value = value;
 
        while (current != null && current.index < index)
        {
            previous = current;
            current = current.nextindex;
        }
 
        if (current == null)
        {
            previous.nextindex = node;
        } else
        {
            if (current.index == index)
            {
                System.out.println("DUPLICATE INDEX");
                return;
            }
            previous.nextindex = node;
            node.nextindex = current;
        }
        return;
    }
 
    public Object fetch(int index)
    {
    	MyList current = this;
        Object value = null;
        while (current != null && current.index != index)
        {
            current = current.nextindex;
        }
        if (current != null)
        {
            value = current.value;
        } else
        {
            value = null;
        }
        return value;
    }
 
    public int elementCount()
    {
        int elementCount = 0;
        for (MyList current = this.nextindex; (current != null); current = current.nextindex)
        {
            elementCount++;
        }
        return elementCount;
    }
}
 
class SparseArray
{
    private MyList start;
    private int index;
 
    SparseArray(int index)
    {
        start = new MyList();
        this.index = index;
    }
 
    public void store(int index, Object value)
    {
        if (index >= 0 && index < this.index)
        {
            if (value != null)
                start.store(index, value);
        } else
        {
            System.out.println("INDEX OUT OF BOUNDS");
        }
    }
 
    public Object fetch(int index)
    {
        if (index >= 0 && index < this.index)
            return start.fetch(index);
        else 
        {
            System.out.println("INDEX OUT OF BOUNDS");
            return null;
        }
    }
 
    public int elementCount()
    {
        return start.elementCount();
    }
}