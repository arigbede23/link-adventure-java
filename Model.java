// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4


import java.util.ArrayList;	// Imports ArrayList
import java.util.Iterator;	// Iterator for safe removal

// Model Class
public class Model
{
	private ArrayList<Sprite> sprites = new ArrayList<>(); // Holds all object in the game
	private Link link; // Holds Link

	private final ArrayList<Sprite> itemsICanAdd = new ArrayList<>();
	private int itemNum = 0;

	// Constructor
	public Model()
	{
		
		// Automatically loads map
		try
		{
			loadFromFile("map.json");
			System.out.println("Map loaded!");
		}
		catch (Exception ex)
		{
			System.err.println("Error. Couldn't load");
			ex.printStackTrace();
		}

		// Link's starting spot
		link = new Link(200, 400);
		sprites.add(link);

		// Items that the user is allowed to add, remove, and toggle through
		itemsICanAdd.add(new Tree(0, 0));
		itemsICanAdd.add(new TreasureChest(0, 0));
	}

	// Capturres prev. position at beginning of frame
	public void beginFrame()
	{
		if (link != null)
			link.beginFrame();
	}

	// Allows Link to throw a boomerang from the middle
	public void throwFrom(Link L)
	{
		if (L == null)
			return;
		int currentX = L.getX() + L.getW()/2;
		int currentY = L.getY() + L.getH()/2;
		sprites.add(new Boomerang(currentX, currentY, L.getDirection()));
	}


	// Update Method
	public void update()
	{
		// Allows each sprite to handle its own
		for (Iterator<Sprite> it = sprites.iterator(); it.hasNext(); )
		{
			Sprite s = it.next();
			if (!s.update())
			{
				it.remove();
				continue;
			}
		}

		// Sprite Colliion Handling
		for (int i = 0; i < sprites.size(); i++)
		{
			Sprite a = sprites.get(i);
			for (int j = i + 1; j < sprites.size(); j++)
			{
				Sprite b = sprites.get(j);
				if (!Sprite.collides(a, b))
					continue;

				// Link vs Item (Tree or Closed Chest)
					if (a.isLink() && b.isItem())
					((Link)a).pushBack(b);
				else if (b.isLink() && a.isItem())
					((Link)b).pushBack(a);

				// Link vs Chest (Handles opening and collecting)
				if (a.isLink() && b.isChest())
				{
					TreasureChest c = (TreasureChest)b;
					if (!c.isOpen())
					{
						((Link)a).pushBack(c);
						c.open();
					}
					else if (c.canCollect())
					{
						c.collect();
					}
				}
				else if (b.isLink() && a.isChest())
				{
					TreasureChest c = (TreasureChest)a;
					if (!c.isOpen())
					{
						((Link)b).pushBack(c);
						c.open();
					}
					else if (c.canCollect())
					{
						c.collect();
					}
				}

				// Boomerang vs item or chest
				if (a.isBoomerang() && (b.isItem() || b.isChest()))
				{
					if (b.isItem())
					{
						TreasureChest c = (TreasureChest)b;
						if (!c.isOpen())
							c.open();
						else
							c.collect();
					}
					((Boomerang)a).kill();
				}
				else if (b.isBoomerang() && (a.isItem() || a.isChest()))
				{
					if (a.isChest())
					{
						TreasureChest c = (TreasureChest)a;
						if (!c.isOpen())
							c.open();
						else
							c.collect();
					}
					((Boomerang)b).kill();
				}
			}
		}


		// Keeps Link inside the game world
		if (link != null)
		{
			int nx = link.getX();
			int ny = link.getY();
			if (nx < 0)
				nx = 0;
			if (ny < 0)
				ny = 0;
			if (nx > Game.WORLD_W - link.getW())
				nx = Game.WORLD_W - link.getW();
			if (ny > Game.WORLD_H - link.getH())
				ny = Game.WORLD_H - link.getH();
			link.setX(nx);
			link.setY(ny);
		}
		
	}

	// Show current item in the view
	public Sprite getItem()
	{
		return itemsICanAdd.get(itemNum);
	}

	// Cycles over to the next item when it edit box is clicked
	public void cycleItem()
	{
		if (itemsICanAdd.isEmpty())
			return;
		itemNum = (itemNum + 1) % itemsICanAdd.size();
	}


	// Allows user to add sprite/item to the map
	public void addSpriteAt(int gridX, int gridY, int w, int h)
	{
		Sprite sel = itemsICanAdd.get(itemNum);

		if (sel.isTree())
		{
			if (!hasTreeAt(gridX, gridY))
			{
				Tree t = new Tree(gridX, gridY);
				t.setW(w);
				t.setH(h);
				sprites.add(t);
			}
		}
		else if (sel.isChest())
		{
			TreasureChest c = new TreasureChest(gridX, gridY);

			for (Sprite s : sprites)
				if (Sprite.collides(c, s))
					return;
				sprites.add(c);
		}
	}

	// Allows user to remove sprite from the map
	public void removeItemAtPoint(int px, int py)
	{
		Sprite sel = itemsICanAdd.get(itemNum);

		for (int i = sprites.size () - 1; i >= 0; i--)
		{
			Sprite s = sprites.get(i);
			if (!s.containsPoint(px, py))
			{
				continue;
			}
			if ((sel.isTree() && s.isTree()) || (sel.isChest() && s.isChest()))
			{
				sprites.remove(i);
				return;
			}
		}
	}

	// Checks if tree is already placed in grid
	public boolean hasTreeAt(int gridX, int gridY)
	{
		for (Sprite s : sprites)
		{
			if (s.isTree() && s.getX() == gridX && s.getY() == gridY)
				return true;
		}
		return false;
	}

	// Checks if tree is already placed in grid
	public boolean hasChestAt(int gridX, int gridY)
	{
		for (Sprite s : sprites)
		{
			if (s.isChest() && s.getX() == gridX && s.getY() == gridY)
				return true;
		}
		return false;
	}

	// Clears sprites/items from the map
	public void clearItems()
	{
		for (Iterator<Sprite> it = sprites.iterator(); it.hasNext(); )
		{
			Sprite s = it.next();
			if (s.isTree() || s.isChest())
				it.remove();
		}
	}
		

	public Link getLink()
	{
		return link;
	}

	// Number of trees stored in ArrayList
	public int getSpriteCount()
	{
		return sprites.size();
	}

	// Gets a specific tree out of ArrayList
	public Sprite getSprite(int i)
	{
		return sprites.get(i);
	}


	// Marshals model to Json node
	public Json marshal()
	{
		Json ob = Json.newObject();
		Json trees = Json.newList();
		Json chests = Json.newList();
		ob.add("trees", trees);
		ob.add("chests", chests);

		for (Sprite s : sprites)
		{
			if (s.isTree())
				trees.add(s.marshal());
			else if (s.isChest())
				chests.add(s.marshal());
		}
		return ob;
	}

	// Unmarshal into model
	public void unmarshal(Json ob)
	{
		for (java.util.Iterator<Sprite> it = sprites.iterator(); it.hasNext(); )
		{
			Sprite s = it.next();
			if (s.isTree() || s.isChest())
				it.remove();
		}

		// Trees unmarshalling
		Json tList = null;
		try
		{
			tList = ob.get("trees");
		}
		catch (Exception ignore)
		{

		}
		if (tList != null)
		{
			for (int i = 0; i < tList.size(); i++)
			{
				sprites.add(new Tree(tList.get(i)));
			}
		}

		// Chests unmarshalling
		Json cList = null;
		try
		{
			cList = ob.get("chests");
		}
		catch (Exception ignore)
		{

		}
		if (cList != null)
		{
			for (int i = 0; i < cList.size(); i++)
			{
				sprites.add(new TreasureChest(cList.get(i)));
			}
		}
		
	}

	// Load from file and unmarshal
	public void loadFromFile(String filename) throws Exception
	{
		Json data = Json.load(filename);
		unmarshal(data);
	}

}