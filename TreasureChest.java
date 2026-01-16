// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


// TreasureChest Class (Chest becomes ruppee when collided with Link or Boomerang)
public class TreasureChest extends Sprite
{
    private static BufferedImage chestImage = null;
    private static BufferedImage rupeeImage = null;

    // Chest state
    private boolean opened = false; // False if chest isn't open; True when rupee is shown
    private int timer = 0; // frame counter while rupee is still showing

    // Time constants
    private static final int COLLECT_DELAY_FRAMES = 5; // Frames where Link can't collect rpee
    private static final int RUPEE_TIME_FRAMES = 40;    // Rupee disappears if not selected

    // Constructor
    public TreasureChest(int gridX, int gridY)
    {
        loadImages();
        x = gridX;
        y = gridY;
        BufferedImage ref = (chestImage != null ? chestImage : rupeeImage);
        w = (ref == null ? 48 : ref.getWidth());
        h = (ref == null ? 48 : ref.getHeight());
    }

    // Loads chest sprites to map from JSON
    public TreasureChest(Json ob)
    {
        loadImages();
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
        opened = false;
        timer = 0;
    }

    // Loads chest images
    private static void loadImages()
    {
        if (chestImage == null)
        {
            try
            {
                chestImage = ImageIO.read(new File("sprites/treasurechest.png"));
            }
            catch(Exception e)
            {
                chestImage = null;
            }
        }

        if (rupeeImage == null)
        {
            try
            {
                rupeeImage = ImageIO.read(new File("sprites/rupee.png"));
            }
            catch(Exception e)
            {
                rupeeImage = null;
            }
        }
    }

    @Override
    public boolean isChest()
    {
        return true;
    }

    @Override
    public boolean isItem()
    {
        return !opened;
    }

    // Helpers
    public boolean isOpen()
    {
        return opened;
    }

    public void open()
    {
        if (!opened)
        {
            opened = true;
            timer = 0;
        }
    }

    public boolean canCollect()
    {
        return opened && timer >= COLLECT_DELAY_FRAMES;
    }

    public void collect()
    {
        timer = COLLECT_DELAY_FRAMES + RUPEE_TIME_FRAMES + 1;
    }

    @Override
    public boolean update()
    {
        if (opened)
        {
            timer++;
            if (timer > COLLECT_DELAY_FRAMES + RUPEE_TIME_FRAMES)
                return false;
        }
        return true;
    }

    @Override
    public boolean containsPoint(int px, int py)
    {
        return (px >= x) && (px < x + w) && (py >= y) && (py < y + h);
    }

    @Override
    public void draw(Graphics g, int dx, int dy)
    {
        BufferedImage img = opened ? rupeeImage : chestImage;
        if (img != null)
            g.drawImage(img, x + dx, y + dy, w, h, null);
    }

    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("Sprite", "Chest");
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        return ob;
    }


    @Override
    public String toString()
    {
        return "Chest (x,y)=(" + x + ", " + y + "), w=" + w + ", h=" + h;
    }
}
