// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


// Tree Class
public class Tree extends Sprite
{
    // Lazy load
    private static BufferedImage treeImage = null;
    
    // Loads tree image
    private static void loadImages()
    {
        if (treeImage != null)
            return;
        try
        {
            treeImage = ImageIO.read(new File("sprites/tree.png"));
        }
        catch (Exception e)
        {
            treeImage = null;
        }
    }

    // Constructor
    public Tree (int gridX, int gridY)
    {
        loadImages();
        x = gridX;
        y = gridY;
        w = 75;
        h = 100;
    }

    // Unmarshaling constructor from JSON
    public Tree(Json ob)
    {
        loadImages();
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
    }

    @Override
    public boolean isTree()
    {
        return true;
    }

    @Override
    public boolean isItem()
    {
        return true;
    }

    @Override
    public boolean update()
    {
        return true;
    }

    // Draws tree (camera offset)
    @Override
    public void draw(Graphics g, int dx, int dy)
    {
        if (treeImage != null)
        {
            g.drawImage(treeImage, x + dx, y + dy, w, h, null);
        }
        else
        {
            g.setColor(new Color(34,139,34));
            g.fillRect(x + dx, y + dy, w, h);
        }
    }

    @Override
    public boolean containsPoint(int px, int py)
    {
        return (px >= x) && (px < x + w) && (py >= y) && (py < y + h);
    }


    // Marshals tree to Json node
    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        return ob;
    }

    @Override
    public String toString()
    {
        return "Tree (x,y)=(" + x + ", " + y + "), w=" + w + ", h=" + h;
    }
}