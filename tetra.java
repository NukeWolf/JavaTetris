import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
public class tetra
{
    private block blocks[];
    private boolean can_rotate;
    private int tetra_block;
    private Color c;
    //Random Tetra
    public tetra(){
        tetra_block = (int)(Math.random() * 7 + 1);
        can_rotate=true;
        blocks = new block[4];
        setupTetraFromId(tetra_block);
        //Setup Colors
        for (block b :blocks){
            b.setColor(c);
        }

        
    }
    //Specific Block
    public tetra(int tetra_id)
    {
        tetra_block = tetra_id;
        can_rotate=true;
        blocks = new block[4];
        setupTetraFromId(tetra_block);
        //Setup Colors
        for (block b :blocks){
            b.setColor(c);
        }
    }
    
    public tetra(tetra tet){
        tetra_block = tet.getId();
        can_rotate = tet.can_rotate;
        c = tet.c;
        blocks = tet.createBlocksCopy();
        for (block b :blocks){
            b.setColor(c);
        }
    }
    public tetra(tetra tet,int transparency){
        tetra_block = tet.getId();
        can_rotate = tet.can_rotate;
        c = new Color(tet.c.getRed(),tet.c.getGreen(),tet.c.getBlue(),128);
        blocks = tet.createBlocksCopy();
        for (block b :blocks){
            b.setColor(c);
        }
    }
    
    public block[] createBlocksCopy(){
        block new_blocks[] = new block[4];
        for (int a = 0; a < 4; a++){
            new_blocks[a] = new block(blocks[a].get_x(),blocks[a].get_y());
        }
        return new_blocks;
    }

    public void draw(Graphics2D g)
    {  
        g.setColor(Color.RED);
        for(int a=0;a<=3;a++){
            blocks[a].draw(g);
        }

    }
    public void drawQueue(Graphics2D g, int offset_x, int offset_y, int blocksz)
    {  
        g.setColor(Color.RED);
        if(tetra_block == 7){
            offset_x += 20;
        }
        for(int a=0;a<=3;a++){
            blocks[a].drawQueue(g,offset_x,offset_y,blocksz);
        }

    }

    public boolean rotate(ArrayList<block> all_blocks){
        if(!can_rotate){
            return false;
        }
        tetra copy = new tetra(this);
        
        //Setup test rotation to simulate what would happen.
        block center = blocks[0];
        block test_blocks[] = copy.getBlocks();
        for(int a=1;a<=3;a++){
            test_blocks[a].rotate_around_block(center);
        }
        
        // If any tetra is inside another block, don't rotate;
        for (block compared : test_blocks){
             if (compared.is_adjacent_to_blocks(all_blocks,0,0)){
                 return false;
                }
        }
        
        //If in the leftmost wall
        if (copy.check_block_in_column(-1)){
            for (block compared : test_blocks){
             if (compared.is_adjacent_to_blocks(all_blocks,0,1)){
                 return false;
                }
            }
            for(int a=0;a<=3;a++)
            {
              blocks[a].move_x(1);
            }
        }
        
        //If in the rightmost wall
        if (copy.check_block_in_column(TetrisDriver.GRID_WIDTH)){
            for (block compared : test_blocks){
             if (compared.is_adjacent_to_blocks(all_blocks,0,-1)){
                 return false;
                }
            }
            for(int a=0;a<=3;a++)
            {
              blocks[a].move_x(-1);
            }
        }
        blocks = test_blocks.clone();
        
        return true;
    }

    public void down(){
        for(int a=0;a<=3;a++){
            blocks[a].move_y(1);
        }

    }

    public boolean left(ArrayList<block> all_blocks){
        //If theres not a block in the left most column, move. Otherwise do nothing.
        if (!can_move_left(all_blocks)){
            return false;
        }
        for(int a=0;a<=3;a++)
        {
          blocks[a].move_x(-1);
        }
        return true;
    }

    public boolean right(ArrayList<block> all_blocks){
        //If theres not a block in the right most column, move. Otherwise do nothing.
        if (!can_move_right(all_blocks)){
            return false;
        }
        for(int a=0;a<=3;a++)
        {
          blocks[a].move_x(1);
        }
        return true;
    }
    public block[] getBlocks(){
        return blocks;
    }
    
    public Integer getId(){
        return tetra_block;
    }

    
    public boolean check_block_in_column(int col_ind){
        for (int a=0;a<=3;a++){
            if(blocks[a].get_x() == col_ind)
                return true;
        }
        return false;
    }
    
    public boolean check_block_in_row(int row_ind){
        for (int a=0;a<=3;a++){
            if(blocks[a].get_y() == row_ind)
                return true;
        }
        return false;
    }
    
    public boolean can_move_down(ArrayList<block> all_blocks){
        // If tetra is all the way at the bottom of grid, it doesn't move.
        if(check_block_in_row(TetrisDriver.GRID_HEIGHT - 1)){
            return false;
        }
        // If any block on the current tetra is on top of any other block.
        for (block compared : blocks){
             if (compared.is_adjacent_to_blocks(all_blocks,0,1)){
                 return false;
                }
        }
        return true;
    }
    public boolean can_move_right(ArrayList<block> all_blocks){
        // If tetra is all the way at the right of grid, it doesn't move.
        if(check_block_in_column(TetrisDriver.GRID_WIDTH - 1)){
            return false;
        }
        // If any block on the current tetra is to the right of any other block.
        for (block compared : blocks){
             if (compared.is_adjacent_to_blocks(all_blocks,1,0)){
                 return false;
                }
        }
        return true;
    }
    public boolean can_move_left(ArrayList<block> all_blocks){
        // If tetra is all the way at the left of grid, it doesn't move.
        if(check_block_in_column(0)){
            return false;
        }
        // If any block on the current tetra is to the left of any other block.
        for (block compared : blocks){
             if (compared.is_adjacent_to_blocks(all_blocks,-1,0)){
                 return false;
                }
        }
        return true;
    }

    
    public void setupTetraFromId(int tetra_id){
        switch(tetra_id){
            case 1: 
                //T-SHAPE
                //Central Block
                blocks[0] = new block(4,1);
                //Top block
                blocks[1] = new block(4,0);
                //Left Block
                blocks[2] = new block(3,1);
                //Right Block
                blocks[3] = new block(5,1);
                c= new Color(148,0,211);
                break;
            case 2:
                //S-SHAPE
                //Central Block
                blocks[0] = new block(4,0);
                //Top block
                blocks[1] = new block(5,0);
                //Left Block
                blocks[2] = new block(4,1);
                //rIGHT bLOCK
                blocks[3] = new block(3,1);
                c = new Color(220,20,60);
                break;
            case 3:
                //BACKWARDS-S-SHAPE
                //Central Block
                blocks[0] = new block(4,0);
                //Top block
                blocks[1] = new block(3,0);
                //Left Block
                blocks[2] = new block(4,1);
                //rIGHT bLOCK
                blocks[3] = new block(5,1);
                c= new Color(50,205,50);
                break;
            case 4:
                //BACKWARDS-S-SHAPE
                //Central Block
                blocks[0] = new block(4,0);
                //Top block
                blocks[1] = new block(5,0);
                //Left Block
                blocks[2] = new block(4,1);
                //rIGHT bLOCK
                blocks[3] = new block(5,1);
                c= new Color(235,235,0);
                can_rotate=false;
                break;
            case 5:
                //LINE-SHAPE
                 blocks[0] = new block(4,1);
                //Top block
                blocks[1] = new block(4,0);
                //Left Block
                blocks[2] = new block(4,2);
                //rIGHT bLOCK
                blocks[3] = new block(4,3);
                c= new Color(0,191,255);
                break;
            case 6:
                //L SHAPED-SHAPE
                blocks[0] = new block(5,1);
                //Top block
                blocks[1] = new block(4,0);
                //Left Block
                blocks[2] = new block(4,1);
                //rIGHT bLOCK
                blocks[3] = new block(6,1);
                c= new Color(0,0,205);
            
            case 7:
                //Backwards-L SHAPED-SHAPE
                blocks[0] = new block(3,1);
                //Top block
                blocks[1] = new block(4,0);
                //Left Block
                blocks[2] = new block(4,1);
                //rIGHT bLOCK
                blocks[3] = new block(2,1);
                c= new Color(255,140,0);
                break;
            }
    }
}
