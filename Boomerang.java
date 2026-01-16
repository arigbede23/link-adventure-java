// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

// Boomerang Class
public class Boomerang extends Sprite
{
    // Velocity
    private int vx;
    private int vy;
    private boolean dead = false; // flag for removal

    private static BufferedImage[] images = null;


    // Constructor
    public Boomerang(int startX, int startY, int facing)
    {
        x = startX;
        y = startY;
        loadImages();

        // Bases the collision size from the first frame
        if (images != null && images[0] != null)
        {
            w = images[0].getWidth();
            h = images[0].getHeight();
        }
        else
        {
            w = 48;
            h = 48;
        }

        int speed = 12;
        switch (facing) 
        {
            case Link.DIR_LEFT:
                vx = -speed;
                vy = 0;
                break;
            
            case Link.DIR_RIGHT:
                vx = speed;
                vy = 0;
                break;   

            case Link.DIR_UP:
                vx = 0;
                vy = -speed;
                break;

            default:
                vx = 0;
                vy = speed;
                break;
        }
    }

    private static void loadImages()
    {
        if (images != null)
            return;
        images = new BufferedImage[4];
        String[] names = {"boomerang1.png", "boomerang2.png", "boomerang3.png", "boomerang4.png"};
        for (int i = 0; i < names.length; i++)
        {
            try
            {
                images[i] = ImageIO.read(new File("sprites/" + names[i]));
            }
            catch (Exception e)
            {
                images[i] = null;
            }
        }
    }

    public void kill()
    {
        dead = true;
    }

    @Override
    public boolean isBoomerang()
    {
        return true;
    }

    @Override
    public boolean update()
    {
        if (dead)
            return false;
        x += vx;
        y += vy;

        if (x + w < 0 || y + h < 0 || x > Game.WORLD_W || y > Game.WORLD_H)
            return false;

        return true;
    }

    @Override
    public void draw(Graphics g, int dx, int dy)
    {
        BufferedImage image = (images != null) ? images[(int)((System.currentTimeMillis()/100)%images.length)] : null;
        if (image != null)
            g.drawImage(image, x + dx, y + dy, w, h, null);
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

        ob.add("Sprite", "Boomerang");
        return ob;
    }

    @Override
    public String toString()
    {
        return "Boomerang (x,y)=(" + x + ", " + y + "), w=" + w + ", h=" + h;
    }
}
