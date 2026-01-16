// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4


import javax.swing.JFrame;
import java.awt.Toolkit;


// Game Class
public class Game extends JFrame
{
	// World size
	public static final int WORLD_W = 3500;
	public static final int WORLD_H = 3000;

	// View (window) size for each room
	public static final int VIEW_W = 800;
	public static final int VIEW_H = 600;
	
	/// Wires to MVC classes
	private boolean keepGoing;
	private Model model;	
	private View view;
	private Controller controller;

	// Constructor
	public Game()
	{
		// Local variables
		this.model = new Model();
		this.view = new View(this.model);
		this.controller = new Controller(this.model);
		this.controller.setView(this.view);

		// Connects input to view
		view.addMouseListener(controller);
		view.addMouseMotionListener(controller);
		view.addKeyListener(controller);
		view.setFocusable(true);

		// Game window
		this.setTitle("A4 - Link's Treasure Quest");
		this.setSize(VIEW_W, VIEW_H);
		this.getContentPane().add(this.view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		// Ensures keys go to the view
		view.requestFocusInWindow();
		this.keepGoing = true;

	}

	public void run()
	{
		do
		{	
			// Captures previous positions
			this.model.beginFrame();
			
			// Handles input and Link + map movement
			this.keepGoing = controller.update();

			// Model update
			this.model.update();

			this.view.repaint(); // This will indirectly call View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

		// Go to sleep for 50 milliseconds
			try
			{
				Thread.sleep(50);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		
		}
		while(this.keepGoing);
	}

	public static void main(String[] args)
	{
		Game g = new Game();
		g.run(); // Calls run method
	}
}
