package aStarPathfinder;
import java.awt.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class Displayer extends JComponent
{
  public void paintComponent(Graphics g)
	{
		// Delay to let the path be calculated
		try
		{	Thread.sleep(10);	}
		catch (InterruptedException e)
		{}
		
		// Draw path
		if (Grid.end.visited)
		{
			g.setColor(Color.red);
			Node currNode = Grid.end, lastNode = Grid.end.last;
			while (!currNode.equals(Grid.start))
			{
				g.drawLine(Screen.size * currNode.x + Screen.size / 2, Screen.size * currNode.y + Screen.size / 2,
						Screen.size * lastNode.x + Screen.size / 2, Screen.size * lastNode.y + Screen.size / 2);
				lastNode = lastNode.last;
				currNode = currNode.last;
			}
		}
		
		// Draw boxes and costs
		g.setColor(Color.black);
		for (int a = 0; a < Grid.size; a++)
		{
			for (int b = 0; b < Grid.size; b++)
			{
				g.drawRect(Screen.size * a, Screen.size * b, Screen.size, Screen.size);
				g.drawString("" + Grid.grid[a][b].nodeCost, Screen.size * a + 3, Screen.size * b + 15);
			}
		}
		
		// Color start and end nodes
		for (int a = 0; a < Grid.size; a++)
		{
			for (int b = 0; b < Grid.size; b++)
			{
				if (Grid.start.equals(Grid.grid[a][b]))
					g.setColor(Color.green);
				if (Grid.end.equals(Grid.grid[a][b]))
					g.setColor(Color.orange);
				if (Grid.start.equals(Grid.grid[a][b]) || Grid.end.equals(Grid.grid[a][b]))
				g.drawRect(Screen.size * a, Screen.size * b, Screen.size, Screen.size);
			}
		}
		g.setColor(Color.black);
		
		// Draw path cost and "option" boxes
		g.drawString("Total path cost: " + Grid.pathCost, 133, Screen.size * Grid.size + 12);
		g.drawRect(0, Screen.size * Grid.size, 130, 15);
		g.drawString("Randomize costs", 1, Screen.size * Grid.size + 12);
		g.drawRect(0, Screen.size * Grid.size + 15, 130, 15);
		g.drawString("Randomize endpoints", 1, Screen.size * Grid.size + 27);
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
