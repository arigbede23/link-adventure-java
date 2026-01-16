// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// Controller Class
public class Controller implements KeyListener, MouseListener, MouseMotionListener
{
	private boolean keepGoing; // Returns to Game loop
	private View view; // References the View
	private Model model; // References the model

	// Movement keys
	private boolean moveLeft;
	private boolean moveRight;
	private boolean moveUp;
	private boolean moveDown;

	// Edit/Add Mode
	private static boolean editMode = false;
	private static boolean addMode = true;

	// Constructor 
	public Controller(Model m)
	{
		this.model = m;
		this.keepGoing = true;
	}

	public void setView (View v)
	{
		this.view = v;
	}

	// Draws Edit UI
	public static boolean isEditMode()
	{
		//System.out.println("getting edit mode");
		return editMode;
	}

	public static boolean isAdding()
	{
		return addMode;
	}

	// Update Method
	public boolean update()
	{
		// Handles movements
		Link link = model.getLink();
		if (link != null)
		{
			int dx = 0;
			int dy = 0;

			// Step size per frame
			int step = (int)Math.round(link.getSpeed());
			if (step < 1) step = 1;

			if (moveLeft)
				dx -= step;
			if (moveRight)
				dx += step;
			if (moveUp)
				dy -= step;
			if (moveDown)
				dy += step;

			// Handles animation (Ensures correct movement and frame facing)
			boolean isMoving = (dx != 0 || dy != 0);
			link.setMoving(isMoving);
			if (dx > 0)
				link.setDirection(Link.DIR_RIGHT);
			else if (dx < 0)
				link.setDirection(Link.DIR_LEFT);
			else if (dy > 0)
				link.setDirection(Link.DIR_DOWN);
			else if (dy < 0)
				link.setDirection(Link.DIR_UP);
			
			// Handles room transitions
			if (isMoving)
			{
				// Applies movement
				link.setX(link.getX() + dx);
				link.setY(link.getY() + dy);

				// Converts world coordinates to screen coordinates
				int screenX = link.getX() - view.getScrollPosX();
				int screenY = link.getY() - view.getScrollPosY();

				// Room to the right
				if (screenX + link.getW() >= Game.VIEW_W)
				{
					// Try scrolling one room to the right
					int oldScrollX = view.getScrollPosX();
					view.scrollBy(1, 0); // Go to room to the rifht
					if (view.getScrollPosX() != oldScrollX)
					{
						// Enters new room (Left doorway)
						link.setX(view.getScrollPosX() + 2);
					}
					else
					{
						// Clamps to right side of current view
						link.setX(view.getScrollPosX() + Game.VIEW_W - link.getW() - 2);
					}	
				}

				// Room to the left
				if (screenX < 0)
				{
					int oldScrollX = view.getScrollPosX();
					view.scrollBy(-1, 0); // Go to room to the left
					if (view.getScrollPosX() != oldScrollX)
					{
						// Enters new room (Right doorway)
						link.setX(view.getScrollPosX() + Game.VIEW_W - link.getW() - 2);
					}
					else
					{
						link.setX(view.getScrollPosX() + 2);
					}
				}

				// Room going downwards
				if (screenY + link.getH() >= Game.VIEW_H)
				{
					int oldScrollY = view.getScrollPosY();
					view.scrollBy(0, 1); // Go to room that's below
					if (view.getScrollPosY() != oldScrollY)
					{
						// Enters new room (Top doorway)
						link.setY(view.getScrollPosY() + 2);
					}
					else
					{
						link.setY(view.getScrollPosY() + Game.VIEW_H - link.getH() - 2);
					}
				}

				// Room going upwards
				if (screenY < 0)
				{
					int oldScrollY = view.getScrollPosY();
					view.scrollBy(0, -1); // Go to room that's above
					if (view.getScrollPosY() != oldScrollY)
					{
						// Enters new room (Bottom doorway)
						link.setY(view.getScrollPosY() + Game.VIEW_H - link.getH() - 2);
					}
					else
					{
						link.setY(view.getScrollPosY() + 2);
					}
				}

			}
		}

		// Keeps game running
		return keepGoing;
	}

	// Adds/removes trees from grid when clicked
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (!editMode) return;	// Ensures user can't edit unless in editMode 

		// Converts screen coordinates to world coordinates
		int screenX = e.getX();
		int screenY = e.getY();
		int worldX = screenX + view.getScrollPosX();
		int worldY = screenY + view.getScrollPosY();

		// Box coordinates
		int boxX = 10;
		int boxY = 10;
		int boxW = 100;
		int boxH = 100;

		// Cycles through sprites
		if (screenX >= boxX && screenX <= boxX + boxW && screenY >= boxY && screenY <= boxY + boxH)
		{
			model.cycleItem();
			return;
		}

		// Either adds or removes tree
		if (addMode)
		{
			Sprite choice = model.getItem();
			if (choice.isTree())
			{
				// Snaps to grid
				int gridX = Math.floorDiv(worldX, View.TREE_W) * View.TREE_W;
				int gridY = Math.floorDiv(worldY, View.TREE_H) * View.TREE_H;
				model.addSpriteAt(gridX, gridY, View.TREE_W, View.TREE_H);
			}
			else
			{
				model.addSpriteAt(worldX, worldY, 0, 0);
			}
		} 
		else
		{
			model.removeItemAtPoint(worldX, worldY);
		}

	}

	// Adds/removes trees from grid when dragged
	public void mouseDragged(MouseEvent e)
	{
		if (!editMode || !addMode)
		{
			return;
		}

		int worldX = e.getX() + view.getScrollPosX();
		int worldY = e.getY() + view.getScrollPosY();

		Sprite choice = model.getItem();
		if (choice.isTree())
		{
			// Snaps to grid
			int gridX = Math.floorDiv(worldX, View.TREE_W) * View.TREE_W;
			int gridY = Math.floorDiv(worldY, View.TREE_H) * View.TREE_H;
			model.addSpriteAt(gridX, gridY, View.TREE_W, View.TREE_H);
		}
		else
		{
			model.addSpriteAt(worldX, worldY, 0, 0);
		}
		
	}

	// Empty Methods
	public void mouseReleased(MouseEvent e) 
	{

	}

	public void mouseClicked(MouseEvent e) 
	{

	}

	public void mouseEntered(MouseEvent e) 
	{

	}

	public void mouseExited(MouseEvent e) 
	{

	}

	public void mouseMoved(MouseEvent e) 
	{

	}

	// Handles Key Movements 
	public void keyPressed(KeyEvent e)
	{
		char ch = Character.toLowerCase(e.getKeyChar());
		int code = e.getKeyCode();

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE || ch == 'q')
			System.exit(0);

		if (code == KeyEvent.VK_LEFT)
			moveLeft = true;
		if (code == KeyEvent.VK_RIGHT)
			moveRight = true;
		if (code == KeyEvent.VK_UP)
			moveUp = true;
		if (code == KeyEvent.VK_DOWN)
			moveDown = true;
		
		if (code == KeyEvent.VK_SPACE)
			model.throwFrom(model.getLink());	
		try
		{
			switch (ch) 
			{
				case 'e':	// Toggel edit mode
					editMode = !editMode;
					if (editMode)
					addMode = true;
					System.out.println("Edit mode: " + editMode);
					break;
			
				case 'a':	// Add
					if (editMode)
					addMode = true;
					break;

				case 'r':	// Remove
					if (editMode)
					addMode = false;
					break;

				case 'c':	// Clears sprites
					if (editMode)
					model.clearItems();
					break;
				
				case 's':	// Save to JSON
				{
					Json node = model.marshal();
					node.save("map.json");
					System.out.println("Saved to map.json!");
					break;
				}

				case 'l':	// Load from JSON
				{
					model.loadFromFile("map.json");
					System.out.println("Loaded from map.json!");
					break;
				}

				// Room switching
				/* 
				case '4':	// Moves Left
				if (view != null)
				view.scrollBy(-1, 0);	
				break;

				case '6':	// Moves Right
				if (view != null)
				view.scrollBy(1, 0);
				break;

				case '8':	// Moves Up
				if (view != null)
				view.scrollBy(0, -1);
				break;

				case '2':	// Moves Down
				if (view != null)
				view.scrollBy(0, 1);
				break;
				*/
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}


	}
		
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_LEFT)
			moveLeft = false;
		if (code == KeyEvent.VK_RIGHT)
			moveRight = false;
		if (code == KeyEvent.VK_UP)
			moveUp = false;
		if (code == KeyEvent.VK_DOWN)
			moveDown = false;
	}

	public void keyTyped(KeyEvent e)
	{    }

}
