// Name: Adedayo Arigbede
// Date: 10/09/25
// Assignment: Assignment 4

import java.awt.Graphics;

// Sprite Class
public abstract class Sprite
{
    // Shared state for Sprites
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    // Helpers
    public boolean isLink()
    {
        return false;
    }

    public boolean isTree()
    {
        return false;
    }

    public boolean isChest()
    {
        return false;
    }

    public boolean isBoomerang()
    {
        return false;
    }

    public boolean isItem()
    {
        return false;
    }

    // Polymorphic hooks
    public abstract boolean update(); // Updates frame; true = sprite stays
    public abstract void draw(Graphics g, int dx, int dy); // Draw based off camera
    public abstract Json marshal(); // Holds aspects of sprite (coordinates, name, etc.)

    // Collision handling for Sprites
    public static boolean collides(Sprite a, Sprite b)
    {
        int aRight = a.x + a.w, aBottom = a.y + a.h;
        int bRight = b.x + b.w, bBottom = b.y + b.h;
        if (aRight <= b.x)
            return false;
        if (a.x >= bRight)
            return false;
        if (aBottom <= b.y)
            return false;
        if (a.y >= bBottom)
            return false;
        return true; 
    }

    public boolean containsPoint(int px, int py)
    {
        return false;
    }

    // Getters
    public final int getX()
    {
        return x;
    }

    public final int getY()
    {
        return y;
    }

    public final int getW()
    {
        return w;
    }

    public final int getH()
    {
        return h;
    }

    // Setters
    public final void setX(int nx)
    {
        x = nx;
    }

    public final void setY(int ny)
    {
        y = ny;
    }

    public void setW(int nw)
    {
        w = nw;
    }

    public void setH(int nh)
    {
        h = nh;
    }




}