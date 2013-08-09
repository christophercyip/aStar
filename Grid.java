package aStarPathfinder;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Grid
{
  ///// Fields /////
	static Node[][] grid;
	static Node start;
	static Node end;
	static int size = 25;
	static int metric = 0;
	static int pathCost;
	static double scale = 10.0;
	
	public static void main(String[] args)
	{
		grid = new Node[size][size];
		initialize();
		randomizeCosts();
		
		// Run the pathfinder
		setAdjacent();
		start = grid[0][0];
		end = grid[size-1][size-1];
		draw();
	}
	
	public static void initialize()
	{
		for (int a = 0; a < size; a++)
			for (int b = 0; b < size; b++)
				grid[a][b] = new Node(a, b, 0);
	}
	
	public static void randomizeCosts()
	{
		Random r = new Random();
		for (int a = 0; a < size; a++)
			for (int b = 0; b < size; b++)
				grid[a][b].nodeCost = 10 + r.nextInt(90);
	}
	
	public static void setArray(int[][] costs)
	{
		for (int a = 0; a < size; a++)
			for (int b = 0; b < size; b++)
				grid[a][b].nodeCost = costs[a][b];
	}
	
	public static void setAdjacent()
	{
		for (int a = 0; a < size; a++)
		{
			for (int b = 0; b < size; b++)
			{
				switch (metric)
				{
					case 0:
						if (a != 0)
							grid[a][b].adjacent.add(grid[a - 1][b]);
						if (a != size - 1)
							grid[a][b].adjacent.add(grid[a + 1][b]);
						if (b != 0)
							grid[a][b].adjacent.add(grid[a][b - 1]);
						if (b != size - 1)
							grid[a][b].adjacent.add(grid[a][b + 1]);
						break;
					case 1:
						if (a != 0)
						{
							grid[a][b].adjacent.add(grid[a - 1][b]);
							if (b != 0)
								grid[a][b].adjacent.add(grid[a - 1][b - 1]);
							if (b != size - 1)
								grid[a][b].adjacent.add(grid[a - 1][b + 1]);
							
						}
						if (a != size - 1)
						{
							grid[a][b].adjacent.add(grid[a + 1][b]);
							if (b != 0)
								grid[a][b].adjacent.add(grid[a + 1][b - 1]);
							if (b != size - 1)
								grid[a][b].adjacent.add(grid[a + 1][b + 1]);
						}
						if (b != 0)
							grid[a][b].adjacent.add(grid[a][b - 1]);
						if (b != size - 1)
							grid[a][b].adjacent.add(grid[a][b + 1]);
						break;
				}
			}
		}
	}
	
	public static void draw()
	{
		// Set up the frame
		Screen g = new Screen(size);
		Displayer nodes = new Displayer();
		Pikachu p = new Pikachu(nodes);
		g.addMouseMotionListener(p);
		g.addMouseListener(p);
		g.add(nodes);
		g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Loop
		Pikachu.clickFlag = true;
		while (true)
		{
			if (Pikachu.clickFlag)
				calculatePath();
			else
				g.repaint();
		}
	}
	
	public static void calculatePath()
	{
		// Make the grid into an array
		Node[] nodes = new Node[size * size];
		for (int a = 0; a < size; a++)
			for (int b = 0; b < size; b++)
				nodes[size * a + b] = grid[a][b];
		
		// Initialize node array
		start.last = start;
		for (int i = 0; i < nodes.length; i++)
		{
			nodes[i].visited = false;
			nodes[i].heuristic = 1000000000 + getDistance(start, nodes[i]);
			nodes[i].totalCost = 0;
			nodes[i].last = null;
		}
		sort(nodes);
		
		// Run through all nodes
		while (!end.visited)
		{
			int nodeIndex = 0;
			for (; nodes[nodeIndex].visited; nodeIndex++);
			Iterator<Node> i = nodes[nodeIndex].adjacent.iterator();
			while (i.hasNext())
			{
				Node nextNode = i.next();
				double newHeuristic = nodes[nodeIndex].totalCost + nextNode.nodeCost + getDistance(nextNode, end);
				if (newHeuristic < nextNode.heuristic)
				{
					nextNode.totalCost = nodes[nodeIndex].totalCost + nextNode.nodeCost;
					nextNode.heuristic = newHeuristic;
					nextNode.last = nodes[nodeIndex];
				}
			}
			nodes[nodeIndex].visited = true;
			sort(nodes);
		}
		
		pathCost = end.totalCost + start.nodeCost;
		Pikachu.clickFlag = false;
	}
	
	// Sort a Node array by heuristic via Comb Sort
	public static void sort(Node[] a)
	{
		int length = a.length;
		int interval = (int) (length / 1.3);
		while (interval != 0)
		{
			int swaps = 0;
			for (int i = 0; i < length - interval; i++)
			{
				int shift = i + interval;
				if (a[i].heuristic > a[shift].heuristic)
				{
					Node x = a[i];
					a[i] = a[shift];
					a[shift] = x;
					swaps++;
				}
			}
			if (swaps == 0)
				interval = (int) (interval / 1.3);
		}
	}
	
	/*  0 Cardinal directions
	 *  1 Cardinal + diagonals
	 */
	public static double getDistance(Node a, Node b)
	{
		double distance;
		switch (metric)
		{
			case 0:
				distance = Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
				break;
			case 1:
				int x = Math.abs(a.x - b.x);
				int y = Math.abs(a.y - b.y);
				distance = 0.4142135623 * Math.min(x, y) + Math.max(x, y);
				break;
			default:
				distance = 0;
				break;
		}
		return scale * distance;
	}
}

class Node
{
	///// Fields /////
	int x, y;
	int nodeCost;
	int totalCost;
	double heuristic;
	boolean visited;
	Node last;
	LinkedList<Node> adjacent;
	
	protected Node(int x, int y, int cost)
	{
		this.x = x;
		this.y = y;
		nodeCost = cost;
		last = null;
		adjacent = new LinkedList<Node>();
	}
	
	protected boolean equals(Node n)
	{
		return x == n.x && y == n.y;
	}
}

class Pikachu extends MouseAdapter
{
	static boolean clickFlag;
	Displayer map;
	
	public Pikachu(Displayer g)
	{
		map = g;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		clickFlag = true;
		int x = e.getX() - 10;
		int y = e.getY() - 30;
		int boxX = clamp(0, x / Screen.size, Grid.size - 1);
		int boxY = clamp(0, y / Screen.size, Grid.size - 1);
		if (x < Grid.size * Screen.size && y < Grid.size * Screen.size)
			Grid.grid[boxX][boxY].nodeCost++;
		if (x > 0 && x < 130 && y > Grid.size * Screen.size && y < Grid.size * Screen.size + 30)
		{
			switch ((y - Grid.size * Screen.size) / 15)
			{
				case 0:
					Grid.randomizeCosts();
					break;
				case 1:
					Random r = new Random();
					Grid.start = Grid.grid[r.nextInt(Grid.size)][r.nextInt(Grid.size)];
					Grid.end = Grid.grid[r.nextInt(Grid.size)][r.nextInt(Grid.size)];
					Grid.calculatePath();
					break;
			}
		}
	}
	
	// Restrict a int value between two boundaries
	public static int clamp(int low, int n, int high)
	{
		if (n < low)
			return low;
		else if (n > high)
			return high;
		else
			return n;
	}
}
