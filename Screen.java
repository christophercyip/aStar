package aStarPathfinder;
import javax.swing.*;
@SuppressWarnings("serial")
public class Screen extends JFrame
{
  ///// Fields /////
	static int size = 25;
	
	public Screen(int n)
	{
		setVisible(true);
		setSize(Math.max(250, size*n + 20), size*n + 80);
	}
}
