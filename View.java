// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4


import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

// View Class
public class View extends JPanel
{
	// Tree size
	public static final int TREE_W = 75;
	public static final int TREE_H = 100;

	// Scroll position
	private int scrollPosX = 0;
	private int scrollPosY = 0;
	
	private final Model model;	// Game state reference

	// Constructor
	public View(Model m)
	{
		this.model = m;	// Stores the model reference
	}

	// Camera helpers
	public int getScrollPosX()
	{
		return scrollPosX;
	}

	public int getScrollPosY()
	{
		return scrollPosY;
	}

	// Scrolls to other rooms (dxRooms & dyRooms are -1, 0, or 1 to move directions)
	public void scrollBy(int dxRooms, int dyRooms)
	{
		int targetX = scrollPosX + dxRooms * Game.VIEW_W;
		int targetY = scrollPosY + dyRooms * Game.VIEW_H;

		// Prevents from going outside map
		if (targetX < 0) 
			targetX = 0;
		if (targetX > Game.WORLD_W - Game.VIEW_W) 
			targetX = Game.WORLD_W - Game.VIEW_W;
		if (targetY < 0) 
			targetY = 0;
		if (targetY > Game.WORLD_H - Game.VIEW_H) 
			targetY = Game.WORLD_H - Game.VIEW_H;

		scrollPosX = targetX;
		scrollPosY = targetY;
	}

	// Draws Image
	@Override
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(72, 152, 72));
		g.fillRect(0, 0, getWidth(), getHeight());

		// Draws all Trees by scroll
		for (int i = 0; i < model.getSpriteCount(); i++)
		{
			model.getSprite(i).draw(g, -scrollPosX, -scrollPosY);
		}


		// Show Edit Mode UI
		if (Controller.isEditMode())
		{
			// Box coordinates
			int boxX = 10, boxY = 10, boxW = 100, boxH = 100;

			// Background
			g.setColor(Controller.isAdding() ? Color.GREEN : Color.RED);
			g.fillRect(boxX, boxY, boxW, boxH); // Edit box

			// Shows sprite in box
			Sprite icon = model.getItem();
			if (icon != null)
			{
				int tx = 20, ty = 20;
				icon.draw(g, tx - icon.getX(), ty - icon.getY());
			}

			// Draw border
			g.setColor(Color.BLACK);
			g.drawRect(boxX, boxY, boxW, boxH);

		}

	}

}
