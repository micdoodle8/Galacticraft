package codechicken.lib.vec;

public class Rectangle4i
{
    public int x;
    public int y;
    public int w;
    public int h;
    
    public Rectangle4i()
    {
    }
    
    public Rectangle4i(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public Rectangle4i offset(int dx, int dy)
    {
        x+=dx;
        y+=dy;
        return this;
    }
    
    public Rectangle4i with(int px, int py)
    {
        if(x > px) x = px;
        if(y > py) y = py;
        if(x + w <= px) w = px-x+1;
        if(y + h <= py) h = py-y+1;
        return this;
    }
    
    public boolean contains(int px, int py)
    {
        return x <= px && px < x+w && y <= py && py < y+h;
    }
}
