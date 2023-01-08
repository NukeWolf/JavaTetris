import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
/**
 * Write a description of class block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class block
{
    // instance variables - replace the example below with your own
    private int x_cord;
    private int y_cord;
    private final int block_px_size = 35;
    private Color color;
    /**
     * Constructor for objects of class block
     */
    public block(int x, int y)
    {
        // initialise instance variables
        x_cord = x;
        y_cord = y;
    }
    public block(block b)
    {
        // initialise instance variables
        x_cord = b.get_x();
        y_cord = b.get_y();
        color = b.getColor();
    }

    public int get_x()
    {
        return x_cord;
    }
    public int get_y()
    {
        return y_cord;
    }
    
    public void setColor (Color c){
        color = c;
    }
    public Color getColor (){
        return color;
    }
    
    public void draw(Graphics2D g)
    {  
        g.setColor(Color.RED);
        g.setColor(color);
        
        int sz = block_px_size;
        g.fillRect(300+x_cord*sz,40+y_cord*sz,sz,sz);
        g.setColor(Color.BLACK);
        g.drawRect(300+x_cord*sz,40+y_cord*sz,sz,sz);
    }
     public void drawQueue(Graphics2D g,int offset_x, int offset_y, int blocksz)
    {  
        g.setColor(Color.RED);
        g.setColor(color);

        int sz = blocksz;
        g.fillRect(offset_x+x_cord*sz,offset_y+y_cord*sz,sz,sz);
        g.setColor(Color.BLACK);
        g.drawRect(offset_x+x_cord*sz,offset_y+y_cord*sz,sz,sz);
    }
    
    public void rotate_around_block(block center){
        int center_x = center.get_x();
        int center_y = center.get_y();
        int current_x = x_cord;
        int current_y = y_cord;
        
        //Rotation Equation
        x_cord = center_x - (current_y - center_y);
        y_cord = center_y + (current_x - center_x);
    
    }
    
    //Move Horizontallly along x axis. Increment by xv
    public void move_x(int xv){
        x_cord += xv;
    }
    //Move Vertically along y axis. Increment by yv
    public void move_y(int yv){
        y_cord += yv;
    }
    
    public boolean is_adjacent_to_blocks(ArrayList<block> all_blocks, int offset_x, int offset_y){
        for (block compared : all_blocks){
            if(this.is_adjacent_to_block(compared,offset_x,offset_y)){
                return true;
            }
        }
        return false;
    }
    
    public boolean is_adjacent_to_block(block compared, int offset_x, int offset_y){
        return (compared.get_x() == x_cord + offset_x && compared.get_y() == y_cord + offset_y);
    }
    

    
}
