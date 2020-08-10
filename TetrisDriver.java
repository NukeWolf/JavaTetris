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
    int timer1,timer2;
    int sel,hdown,hold;
    int rowcheck;
    boolean selm,colapse,colapse1,rowbool,holding;
    ArrayList<tetra> tet= new ArrayList<tetra>();
    ArrayList<Integer> tetque= new ArrayList<Integer>();
    Clip song;
    Clip blast;
    Clip rota;
    Clip set;
    public TetrisDriver()
    {
        con.setLayout(new FlowLayout());
        addKeyListener(this);
        hdown=15;
        sel = -1;
        hold=0;
        holding = true;
        rowcheck=0;
        colapse = false;
        colapse1 = false;
        rowbool=false;
        JOptionPane.showMessageDialog(null,"Welcome to Tetris! Use the left and right arrow keys to move left and right. Use the up arrow to rotate. Use the down arrow to speed up the block. Press space to hold certain blocks.");
        try{
            URL url = this.getClass().getClassLoader().getResource("assets/Tetris.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            song = AudioSystem.getClip();
            song.open(audioIn);
        }
        catch(Exception e){
            e.printStackTrace();

        }
        try{
            URL url = this.getClass().getClassLoader().getResource("assets/blast.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            blast = AudioSystem.getClip();
            blast.open(audioIn);
        }
        catch(Exception e){
            e.printStackTrace();

        }
        try{
            URL url = this.getClass().getClassLoader().getResource("assets/switch.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            rota = AudioSystem.getClip();
            rota.open(audioIn);
        }
        catch(Exception e){
            e.printStackTrace();

        }
        try{
            URL url = this.getClass().getClassLoader().getResource("assets/set.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            set = AudioSystem.getClip();
            set.open(audioIn);
        }
        catch(Exception e){
            e.printStackTrace();

        }
        for(int a = 0;a<=3;a++){
            tetque.add(tetque.size(),(int )(Math.random() * 7 + 1));

        }
        tet.add(tet.size(),new tetra(tetque.get(0)));

        tetque.remove(0);
        tetque.add(tetque.size(),(int )(Math.random() * 7 + 1));

        sel+=1;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.start();
    }

    public void run()
    {
        try{
            song.loop(20);
            while(true)
            {
                t.sleep(40);//Smaller number == faster, larger == slower
                timer1+=1;   

                if(timer1%hdown==0&& timer2==0){
                    tet.get(sel).down();
                }
                for(int a=0;a<tet.size();a++){
                    for(int b=0;b<=3;b++){
                        for(int c=0;c<=3;c++){
                            if (tet.get(a).getYar().get(b)==-2 ){
                                System.exit(0);
                            }

                        }
                    }
                }
                for(int a=0;a<tet.size();a++){
                    for(int b=0;b<=3;b++){
                        for(int c=0;c<=3;c++){
                            if(tet.get(a).getXar().get(b)==tet.get(sel).getXar().get(c) &&
                            tet.get(a).getYar().get(b)==tet.get(sel).getYar().get(c) && !(a == sel)){
                                tet.get(sel).up();
                            }

                        }
                    }
                }
                for(int a=0;a<tet.size();a++){
                    for(int b=0;b<=3;b++){
                        for(int c=0;c<=3;c++){
                            if(tet.get(a).getXar().get(b)==tet.get(sel).getXar().get(c) &&
                            tet.get(a).getYar().get(b)==tet.get(sel).getYar().get(c)+1 && !(a == sel) ||
                            tet.get(sel).getYar().get(c)==19){
                                colapse=true;

                            }

                        }
                    }
                }
                if(colapse){
                    timer2+=1;

                }
                if (timer2>=15){
                    for(int a=0;a<tet.size();a++){
                        for(int b=0;b<=3;b++){
                            for(int c=0;c<=3;c++){
                                if(tet.get(a).getXar().get(b)==tet.get(sel).getXar().get(c) &&
                                tet.get(a).getYar().get(b)==tet.get(sel).getYar().get(c)+1 && !(a == sel) ||
                                tet.get(sel).getYar().get(c)==19){
                                    colapse1=true;
                                }

                            }
                        }
                    }
                    timer2=0;
                    colapse = false;
                }
                if(colapse1){
                    tet.add(tet.size(),new tetra(tetque.get(0)));
                    tetque.remove(0);
                    tetque.add(tetque.size(),(int )(Math.random() * 7 + 1));
                    while(tetque.get(3)==tetque.get(2)){
                        tetque.remove(3);
                        tetque.add(tetque.size(),(int )(Math.random() * 7 + 1));
                    }
                    set.setFramePosition(0);
                    set.loop(0);
                    sel+=1;
                    timer2=0;
                    colapse = false;
                    colapse1=false;
                    timer1=0;
                    holding = true;
                    for(int c=0;c<=19;c++){
                        for(int d=0;d<=9;d++){
                            for(int a=0;a<tet.size();a++){    
                                for(int b=0;b<=3;b++){   
                                    if(tet.get(a).getXar().get(b)==d &&
                                    tet.get(a).getYar().get(b)==c){
                                        rowcheck+=1;
                                    }
                                }

                            }
                        }
                        if(rowcheck==10){
                            blast.setFramePosition(0);
                            blast.loop(0);
                            for(int a=0;a<tet.size();a++){    
                                for(int b=0;b<=3;b++){   
                                    if (tet.get(a).getYar().get(b)==c){
                                        tet.get(a).die(b);
                                    }
                                    else if (tet.get(a).getYar().get(b)<=c){
                                        tet.get(a).onedown(b);
                                    }

                                }

                            }

                        }
                        rowcheck=0;
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

        for(int a=0;a<350;a+=35)
        {   for(int b=0;b<700;b+=35)
            {
                g.setColor(Color.BLACK);
                g.drawRect(300+a,b+40,35,35);

            }
        }
        for(int a=0;a<tet.size();a++){
            tet.get(a).draw(g);
        }
        g.setColor(new Color(255,255,255));
        g.fillRect(670,60,120,120);
        g.setColor(Color.BLACK);
        g.drawRect(670,60,120,120);
        g.fillRect(690,180,80,300);
        g.setColor(new Color(200,105,255));
        g.fillRect(160,60,120,120);
        g.setColor(Color.BLACK);
        g.drawRect(160,60,120,120);
        //COMING NEXT SECTION
        if(tetque.get(0)==1)
        {
            Color c= new Color(148,0,211);
            g.setColor(c);
            g.fillRect(630+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+3*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+3*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+5*20,100+1*20,20,20);
        }
        else if(tetque.get(0)==2)
        {
            Color c=new Color(220,20,60);
            g.setColor(c);
            g.fillRect(630+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+5*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+5*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+3*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+3*20,100+1*20,20,20);
        }
        else if(tetque.get(0)==3)
        {
            Color c= new Color(50,205,50);
            g.setColor(c);
            g.fillRect(630+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+3*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+3*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+5*20,100+1*20,20,20);
        }
        else if(tetque.get(0)==4)
        {
            Color c= new Color(235,235,0);
            g.setColor(c);
            g.fillRect(630+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+5*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+5*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+5*20,100+1*20,20,20);
        }
        else if(tetque.get(0)==5)
        {
            Color c= new Color(0,191,255);
            g.setColor(c);
            g.fillRect(632+4*20,75+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(632+4*20,75+1*20,20,20);

            g.setColor(c);
            g.fillRect(632+4*20,75+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(632+4*20,75+0*20,20,20);

            g.setColor(c);
            g.fillRect(632+4*20,75+2*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(632+4*20,75+2*20,20,20);

            g.setColor(c);
            g.fillRect(632+4*20,75+3*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(632+4*20,75+3*20,20,20);
        }
        else if(tetque.get(0)==6)
        {
            Color c= new Color(0,0,205);
            g.setColor(c);
            g.fillRect(630+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+5*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(630+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(630+6*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(630+6*20,100+1*20,20,20);
        }
        else if(tetque.get(0)==7)
        {
            Color c= new Color(255,140,0);
            g.setColor(c);
            g.fillRect(650+3*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(650+3*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(650+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(650+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(650+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(650+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(650+2*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(650+2*20,100+1*20,20,20);
        }

        if(tetque.get(1)==1)
        {
            Color c= new Color(148,0,211);
            g.setColor(c);
            g.fillRect(657+4*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,220+1*15,15,15);
        }
        else if(tetque.get(1)==2)
        {
            Color c=new Color(220,20,60);
            g.setColor(c);
            g.fillRect(657+4*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,220+1*15,15,15);
        }
        else if(tetque.get(1)==3)
        {
            Color c= new Color(50,205,50);
            g.setColor(c);
            g.fillRect(657+4*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,220+1*15,15,15);
        }
        else if(tetque.get(1)==4)
        {
            Color c= new Color(235,235,0);
            g.setColor(c);
            g.fillRect(657+4*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,220+1*15,15,15);
        }
        else if(tetque.get(1)==5)
        {
            Color c= new Color(0,191,255);
            g.setColor(c);
            g.fillRect(657+4*15,190+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,190+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,190+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,190+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,190+2*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,190+2*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,190+3*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,190+3*15,15,15);
        }
        else if(tetque.get(1)==6)
        {
            Color c= new Color(0,0,205);
            g.setColor(c);
            g.fillRect(657+5*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+6*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+6*15,220+1*15,15,15);
        }
        else if(tetque.get(1)==7)
        {
            Color c= new Color(255,140,0);
            g.setColor(c);
            g.fillRect(677+3*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+3*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(677+4*15,220+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+4*15,220+0*15,15,15);

            g.setColor(c);
            g.fillRect(677+4*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+4*15,220+1*15,15,15);

            g.setColor(c);
            g.fillRect(677+2*15,220+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+2*15,220+1*15,15,15);
        }
        if(tetque.get(2)==1)
        {
            Color c= new Color(148,0,211);
            g.setColor(c);
            g.fillRect(657+4*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,270+1*15,15,15);
        }
        else if(tetque.get(2)==2)
        {
            Color c=new Color(220,20,60);
            g.setColor(c);
            g.fillRect(657+4*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,270+1*15,15,15);
        }
        else if(tetque.get(2)==3)
        {
            Color c= new Color(50,205,50);
            g.setColor(c);
            g.fillRect(657+4*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,270+1*15,15,15);
        }
        else if(tetque.get(2)==4)
        {
            Color c= new Color(235,235,0);
            g.setColor(c);
            g.fillRect(657+4*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,270+1*15,15,15);
        }
        else if(tetque.get(2)==5)
        {
            Color c= new Color(0,191,255);
            g.setColor(c);
            g.fillRect(657+4*15,280+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,280+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,280+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,280+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,280+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,280+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+6*15,280+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+6*15,280+1*15,15,15);
        }
        else if(tetque.get(2)==6)
        {
            Color c= new Color(0,0,205);
            g.setColor(c);
            g.fillRect(657+5*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+6*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+6*15,270+1*15,15,15);
        }
        else if(tetque.get(2)==7)
        {
            Color c= new Color(255,140,0);
            g.setColor(c);
            g.fillRect(677+3*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+3*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(677+4*15,270+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+4*15,270+0*15,15,15);

            g.setColor(c);
            g.fillRect(677+4*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+4*15,270+1*15,15,15);

            g.setColor(c);
            g.fillRect(677+2*15,270+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+2*15,270+1*15,15,15);
        }
        if(tetque.get(3)==1)
        {
            Color c= new Color(148,0,211);
            g.setColor(c);
            g.fillRect(657+4*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,330+1*15,15,15);
        }
        else if(tetque.get(3)==2)
        {
            Color c=new Color(220,20,60);
            g.setColor(c);
            g.fillRect(657+4*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,330+1*15,15,15);
        }
        else if(tetque.get(3)==3)
        {
            Color c= new Color(50,205,50);
            g.setColor(c);
            g.fillRect(657+4*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+3*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+3*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,330+1*15,15,15);
        }
        else if(tetque.get(3)==4)
        {
            Color c= new Color(235,235,0);
            g.setColor(c);
            g.fillRect(657+4*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+5*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,330+1*15,15,15);
        }
        else if(tetque.get(3)==5)
        {
            Color c= new Color(0,191,255);
            g.setColor(c);
            g.fillRect(657+4*15,340+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,340+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,340+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,340+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,340+2*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,340+2*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,340+3*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,340+3*15,15,15);
        }
        else if(tetque.get(3)==6)
        {
            Color c= new Color(0,0,205);
            g.setColor(c);
            g.fillRect(657+5*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+5*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(657+4*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+4*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(657+6*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(657+6*15,330+1*15,15,15);
        }
        else if(tetque.get(3)==7)
        {
            Color c= new Color(255,140,0);
            g.setColor(c);
            g.fillRect(677+3*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+3*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(677+4*15,330+0*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+4*15,330+0*15,15,15);

            g.setColor(c);
            g.fillRect(677+4*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+4*15,330+1*15,15,15);

            g.setColor(c);
            g.fillRect(677+2*15,330+1*15,15,15);
            g.setColor(Color.BLACK);
            g.drawRect(677+2*15,330+1*15,15,15);
        }

        if(hold==1)
        {
            Color c= new Color(148,0,211);
            g.setColor(c);
            g.fillRect(120+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+3*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+3*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+5*20,100+1*20,20,20);
        }
        else if(hold==2)
        {
            Color c=new Color(220,20,60);
            g.setColor(c);
            g.fillRect(120+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+5*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+5*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+3*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+3*20,100+1*20,20,20);
        }
        else if(hold==3)
        {
            Color c= new Color(50,205,50);
            g.setColor(c);
            g.fillRect(120+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+3*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+3*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+5*20,100+1*20,20,20);
        }
        else if(hold==4)
        {
            Color c= new Color(235,235,0);
            g.setColor(c);
            g.fillRect(120+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+5*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+5*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+5*20,100+1*20,20,20);
        }
        else if(hold==5)
        {
            Color c= new Color(0,191,255);
            g.setColor(c);
            g.fillRect(120+4*20,76+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,76+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,76+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,76+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,76+2*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,76+2*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,76+3*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,76+3*20,20,20);
        }
        else if(hold==6)
        {
            Color c= new Color(0,0,205);
            g.setColor(c);
            g.fillRect(120+5*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+5*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(120+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(120+6*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(120+6*20,100+1*20,20,20);
        }
        else if(hold==7)
        {
            Color c= new Color(255,140,0);
            g.setColor(c);
            g.fillRect(140+3*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(140+3*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(140+4*20,100+0*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(140+4*20,100+0*20,20,20);

            g.setColor(c);
            g.fillRect(140+4*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(140+4*20,100+1*20,20,20);

            g.setColor(c);
            g.fillRect(140+2*20,100+1*20,20,20);
            g.setColor(Color.BLACK);
            g.drawRect(140+2*20,100+1*20,20,20);
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

        if(k.getKeyCode()==40){

            hdown=15;
        }
    }

    public void keyPressed(KeyEvent k)
    {
        if(k.getKeyCode()==32){
            if(holding){
                holding = false;
                int b = hold;
                hold = tet.get(sel).geta1();
                for(int a=3;a>=0;a--)
                    tet.get(sel).die(a);
                if(b==0){
                    tet.add(tet.size(),new tetra(tetque.get(0)));
                    tetque.remove(0);
                    tetque.add(tetque.size(),(int )(Math.random() * 7 + 1));
                }
                else
                    tet.add(tet.size(),new tetra(b));
                sel+=1;
            }

        }
        if(k.getKeyCode()==38)
        {   
            for(int a=0;a<tet.size();a++){
                for(int b=0;b<=3;b++){
                    for(int c=1;c<=3;c++){
                        int xx=tet.get(sel).getXar().get(c);
                        int yy=tet.get(sel).getYar().get(c);
                        xx-=tet.get(sel).getXar().get(0);
                        yy-=tet.get(sel).getYar().get(0);

                        int xxx =tet.get(sel).getXar().get(0)-yy;
                        int yyy =tet.get(sel).getYar().get(0)+xx;

                        if(tet.get(a).getXar().get(b)==xxx &&
                        tet.get(a).getYar().get(b)==yyy && !(a == sel)|| yyy==20){
                            selm = false;
                        }
                        //Check if can move right
                        if(xxx==-1){
                            for(int aa=0;aa<tet.size();aa++){
                                for(int bb=0;bb<=3;bb++){
                                    for(int cc=0;cc<=3;cc++){
                                        if(tet.get(aa).getXar().get(bb)==tet.get(sel).getXar().get(cc)+1 &&
                                        tet.get(aa).getYar().get(bb)==tet.get(sel).getYar().get(cc) && !(aa == sel) || tet.get(sel).getRotation()){
                                            selm = false;
                                        }

                                    }
                                }
                            }
                            if(selm)
                                tet.get(sel).right();
                        }
                        //Check if it can move left.
                        if(xxx==10){
                            for(int aa=0;aa<tet.size();aa++){
                                for(int bb=0;bb<=3;bb++){
                                    for(int cc=0;cc<=3;cc++){
                                        if(tet.get(aa).getXar().get(bb)==tet.get(sel).getXar().get(cc)-1 &&
                                        tet.get(aa).getYar().get(bb)==tet.get(sel).getYar().get(cc) && !(aa == sel) || tet.get(sel).getRotation()){
                                            selm = false;
                                        }

                                    }
                                }
                            }
                            if(selm)
                                tet.get(sel).left();
                        }
                    }
                }
            }
            if(selm){
                rota.setFramePosition(0);
                rota.loop(0);
                tet.get(sel).rotate();
            }
            selm=true;
        }
        if(k.getKeyCode()==37){

            for(int a=0;a<tet.size();a++){
                for(int b=0;b<=3;b++){
                    for(int c=0;c<=3;c++){
                        if(tet.get(a).getXar().get(b)==tet.get(sel).getXar().get(c)-1 &&
                        tet.get(a).getYar().get(b)==tet.get(sel).getYar().get(c) && !(a == sel)){
                            selm = false;
                        }

                    }
                }
            }
            if(selm)
                tet.get(sel).left();

            selm=true;
        }
        if(k.getKeyCode()==39){
            for(int a=0;a<tet.size();a++){
                for(int b=0;b<=3;b++){
                    for(int c=0;c<=3;c++){
                        if(tet.get(a).getXar().get(b)==tet.get(sel).getXar().get(c)+1 &&
                        tet.get(a).getYar().get(b)==tet.get(sel).getYar().get(c) && !(a == sel)){
                            selm = false;
                        }

                    }
                }
            }
            if(selm)
                tet.get(sel).right();
            selm=true;
        }
        if(k.getKeyCode()==40){

            hdown=1;
        }
    }

    public void keyTyped(KeyEvent k)
    {

    }

}