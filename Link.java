// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;

// Link Class
public class Link extends Sprite
{

    // Link previous position
    private int px;
    private int py;

    // Movement speed
    private double speed = 18.0;

    // Direction constants
    public static final int DIR_RIGHT = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_UP = 2;
    public static final int DIR_DOWN = 3;

    // Animation frames (44 frames; 11 per direction)
    private static final int TOTAL_FRAMES = 44;
    private static final int FRAMES_PER_DIR = 11;

    // Lazy load
    private static BufferedImage[] frames = null;

    // Animation
    private int frame = 1; // Goes bettween 11 frames per direction
    private int direction = DIR_RIGHT;  // Last direction faced
    private boolean moving = false;
    private int frameDelay = 1;
    private int frameCounter = 0;

    // Direction base 
    private static final int[] DIR_BASE = 
    {
        22, // Right (Frames 23-33)
        11, // Left (Frames 12-22)
        33, // Up (Frames 34-44)
        0 // Down (Frames 1-11)
    };

    // Constructor
    public Link(int startX, int startY)
    {
        // Position + Previous
        this.x = startX;
        this.y = startY;
        this.px = x;
        this.py = y;
        loadFrames();
        setSize();
    }

    // Lazy load frames
    private static void loadFrames()
    {
        if (frames != null)
            return;
        frames = new BufferedImage[TOTAL_FRAMES + 1];
        for (int i = 1; i <= TOTAL_FRAMES; i++)
        {
            try
            {
                frames[i] = ImageIO.read(new File("sprites/link" + i + ".png"));
            }
            catch (Exception e)
            {
                frames[i] = null;
            }
        }
    }

    // Sets size of sprite based on first frame
    public void setSize()
    {
        for (int i = 1; i <= TOTAL_FRAMES; i++)
        {
            if (frames[i] != null)
            {
                this.w = frames[i].getWidth();
                this.h = frames[i].getHeight();
                return;
            }
        }
        w = 40;
        h = 40;
    }

    // First frame called
    public void beginFrame()
    {
        px = x;
        py = y;
    }

    // Pushes Link out from the map item with the prev. postion
    public void pushBack(Sprite s)
    {
        // From left
        if (px + w <= s.getX())
        {
            x = s.getX() - w;
            return;
        }

        // From Right
        if (px >= s.getX() + s.getW())
        {
            x = s.getX() + s.getW();
            return;
        }

        // From Above
        if (py + h <= s.getY())
        {
            y = s.getY() - h;
            return;
        }

        // From below
        if (py >= s.getY() + s.getH())
        {
            y = s.getY() + s.getH();
            return;
        }
    }

    // Update method (Allows Link to cycle frames while moving)
    @Override
    public boolean update()
    {
        if (moving)
        {
            frameCounter++;
            if (frameCounter >= frameDelay)
            {
                frameCounter = 0;
                frame++;
                if (frame > FRAMES_PER_DIR) frame = 1;
            }
        }
        else
        {
            frame = 1;
        }
        return true; // Link remains valid
    }

    // Draw method
    @Override
    public void draw(Graphics g, int dx, int dy)
    {
        int base = DIR_BASE[direction];
        int index = base + frame; // Ensures that Link goes through all frames while moving
        BufferedImage img = (index >= 1 && index <= TOTAL_FRAMES) ? frames[index] : null;
         
        if (img != null)
            g.drawImage(img, x + dx, y + dy, w, h, null);
        else
        {
            g.setColor(Color.BLUE); // Draws box if image isn't found
            g.fillRect(x + dx, y + dy, w, h);
        }
    }

    // Marshals Link's state
    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("Sprite", "Link");
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

    @Override
    public boolean isLink()
    {
        return true;
    }

    public void setMoving(boolean m)
    {
        moving = m;
    }

    public void setDirection(int dir)
    {
        direction = dir;
    }

    public int getDirection()
    {
        return direction;
    }

    public double getSpeed()
    {
        return speed;
    }

    public void setSpeed(double s)
    {
        speed = s;
    }


    @Override
    public String toString()
    {
        return "Link(=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", speed=" + speed + ")";
    }
}

