//Things to add if time: Hold a brick,make sure you can slide stuff in.,

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
public class TetrisDriver extends JFrame implements Runnable, KeyListener//Other implementables
{
    Container con = getContentPane();
    Thread t = new Thread(this);
    // Constant Variables
    public static final int GRID_HEIGHT = 20;
    public static final int GRID_WIDTH = 10;
    public static final int TETRA_DROP_INTERVAL_DEFAULT = 15;
    int TETRA_DROP_INTERVAL;
    
    State state;
    int main_clock_count;
    boolean can_hold;
    
    ArrayList<block> tetra_all= new ArrayList<block>();
    ArrayList<tetra> tetra_queue= new ArrayList<tetra>();
    tetra selected_tetra, hold;
    
    Clip song;
    Clip blast;
    Clip rota;
    Clip set;
    
    public TetrisDriver()
    {
        con.setLayout(new FlowLayout());
        addKeyListener(this);
        
        state = State.DROPPING;
        can_hold = true;
        TETRA_DROP_INTERVAL = TETRA_DROP_INTERVAL_DEFAULT;

        JOptionPane.showMessageDialog(null,"Welcome to Tetris! Use the left and right arrow keys to move left and right. Use the up arrow to rotate. Use the down arrow to speed up the block. Press space to hold certain blocks.");
        
        // Load Sounds
        try{
            song = AudioSystem.getClip();
            song.open(loadAudioFile("assets/Tetris.wav"));
            blast = AudioSystem.getClip();
            blast.open(loadAudioFile("assets/blast.wav"));
            rota = AudioSystem.getClip();
            rota.open(loadAudioFile("assets/switch.wav"));
            set = AudioSystem.getClip();
            set.open(loadAudioFile("assets/set.wav"));
        }
        catch(Exception e){
            e.printStackTrace();

        }
        
        //Fill tetra queue
        for(int a = 0;a<5;a++){
            tetra_queue.add(new tetra());

        }
        selected_tetra = tetra_queue.remove(0);
        hold = null;
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.start();
    }

    public void run()
    {
        try{
            //Play background music
            song.loop(20);
            while(true)
            {
                t.sleep(40);//Smaller number == faster, larger == slower
                main_clock_count+=1;   
                
                
                // Check if game has ended.
                for(block tet : tetra_all){
                    // If any block is at -2, consider death.
                    if (tet.get_y() == -1){
                        System.exit(0);
                    }
                }
                
                
                //Delayed Clock Cycles 
                if(main_clock_count % TETRA_DROP_INTERVAL == 0){
                    
                    // Give a chance for player to adjust tetra piece before doing the final collapse;
                    if(state == State.PRE_COLLAPSE){
                        if(selected_tetra.can_move_down(tetra_all)){
                            state = State.DROPPING;
                        }
                        else{
                            state = State.COLLAPSE;
                        }
                    }
                    
                    // Drop the piece if it can move down and in the correct state.
                    if(state == State.DROPPING){
                    
                        if(selected_tetra.can_move_down(tetra_all)){
                            selected_tetra.down();
                        }
                            else{
                            state = State.PRE_COLLAPSE;
                        }
                        
                    }
                }
                
                
                

                if(state == State.COLLAPSE){
                    //Reset states
                    playSound(set);
                    can_hold = true;
                    state = State.DROPPING;
                    //Add selected tetra into environment blocks and chose new tetra.
                    for (block sel_block : selected_tetra.getBlocks()){
                        tetra_all.add(sel_block);
                    }
                    selected_tetra = tetra_queue.remove(0);
                    tetra_queue.add(new tetra());
                    
                    // TODO: PREVENT REPEATING BLOCKS
                    while(tetra_queue.get(3)==tetra_queue.get(2) && false){
                        //tetra_queue.remove(3);
                        //tetra_queue.add(tetra_queue.size(),(int )(Math.random() * 7 + 1));
                    }
                   
                    
                    //COLLAPSE BEHAVIOR
                    
                    // Count how many blocks are in each row.
                    int row_block_count[] = new int[GRID_HEIGHT];
                    for (block b : tetra_all){
                        row_block_count[b.get_y()] += 1;
                    }
                    
                    //Must do this from top to bottom for cascading effect.
                    for(int row=0;row<GRID_HEIGHT;row++){
                        // If a row is full
                        if(row_block_count[row] == GRID_WIDTH){
                            
                            playSound(blast);
                            ArrayList<block> removed_blocks = new ArrayList<block>();
                            // Either remove the block or move it down by one
                            for (block b : tetra_all){
                                if(b.get_y() == row){
                                    removed_blocks.add(b);
                                }
                                //Move down all blocks above the row.
                                else if (b.get_y() < row){
                                    b.move_y(1);
                                }
                            }
                            tetra_all.removeAll(removed_blocks);
                            
                        }
                    }

                }
                repaint();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void paint(Graphics gr)
    {
        Image i=createImage(getSize().width, getSize().height);
        Graphics2D g = (Graphics2D)i.getGraphics();
        g.setColor(new Color(0,95,127));
        g.fillRect(0,0,1000,800);
        g.setColor(new Color(0,60,90));
        g.fillRect(300,40,350,700);
        // TETRIS GRID
        for(int a=0;a<350;a+=35)
        {   for(int b=0;b<700;b+=35)
            {
                g.setColor(Color.BLACK);
                g.drawRect(300+a,b+40,35,35);

            }
        }
        //Draw environment Blocks
        for(block b : tetra_all){
            b.draw(g);
        }
        
        selected_tetra.draw(g);
        
        //GHOST PIECE
        tetra ghost = new tetra(selected_tetra,64);
        while(ghost.can_move_down(tetra_all)){
            ghost.down();
        }
        ghost.draw(g);
        
        
        g.setColor(new Color(255,255,255));
        g.fillRect(670,60,120,120);
        g.setColor(Color.BLACK);
        g.drawRect(670,60,120,120);
        g.fillRect(690,180,80,300);
        g.setColor(new Color(200,105,255));
        g.fillRect(160,60,120,120);
        g.setColor(Color.BLACK);
        g.drawRect(160,60,120,120);
        
        //Next in queue
        tetra_queue.get(0).drawQueue(g,630,100,20);
        //Rest of queue
        for(int a = 1; a < tetra_queue.size(); a++){
            tetra_queue.get(a).drawQueue(g,657,220 + 55 * (a-1),15);
        }
        
        
        if (hold != null){
            hold.drawQueue(g,120,100,20);
        }

        g.setColor(new Color(0,0,0));
        g.drawString("HOLD",205,170);
        g.drawString("NEXT",713,170);

        g.dispose();
        gr.drawImage(i, 0, 0, this);
    }

    public static void main(String[] args)
    {
        TetrisDriver frame = new TetrisDriver();
        frame.setSize(1000, 768);//determines size of screen
        frame.setVisible(true);
    }

    public void update(Graphics g)
    {
        paint(g);
    } 

    public void keyReleased(KeyEvent k)
    {
        //Reset hdown
        if(k.getKeyCode()==40){
            TETRA_DROP_INTERVAL=TETRA_DROP_INTERVAL_DEFAULT;
        }
    }

    public void keyPressed(KeyEvent k)
    {
        //KEY Space : HOLDING KEY
        if(k.getKeyCode()==16){
            if(can_hold){
                can_hold = false;
                if (hold == null){
                    hold = new tetra(selected_tetra.getId());
                    selected_tetra = tetra_queue.remove(0);
                    tetra_queue.add(new tetra()); 
                }
                else{
                    int hold_id = hold.getId();
                    hold = new tetra(selected_tetra.getId());
                    selected_tetra = new tetra(hold_id);
                }
            }

        }
        if(k.getKeyCode()==32){
            while(selected_tetra.can_move_down(tetra_all)){
                selected_tetra.down();
            }
            state = state.COLLAPSE;
        }
        
        // Up key : Rotate
        if(k.getKeyCode()==38)
        {   
            if(selected_tetra.rotate(tetra_all)){
                playSound(rota);
            }
        }
        // Left Key
        if(k.getKeyCode()==37){
            selected_tetra.left(tetra_all);
        }
        // Right Key
        if(k.getKeyCode()==39){
            selected_tetra.right(tetra_all);
        }
        //Down Key : Modify Interval
        if(k.getKeyCode()==40){
            TETRA_DROP_INTERVAL=1;
        }

    }

    public void keyTyped(KeyEvent k)
    {

    }
    
    public AudioInputStream loadAudioFile(String file){
        try{
            URL url = this.getClass().getClassLoader().getResource(file);
            return AudioSystem.getAudioInputStream(url);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void playSound(Clip sound){
        sound.setFramePosition(0);
        sound.loop(0);
    }
}