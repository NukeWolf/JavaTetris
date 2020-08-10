import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
public class tetra
{
    private ArrayList<Integer> x= new ArrayList<Integer>();
    private ArrayList<Integer> y= new ArrayList<Integer>();
    private boolean move,rot;
    private int yv,val;
    private Color c;
    public tetra(int a1)
    {
        yv=0;
        val = a1;
        rot=true;
        if(a1==0){
         x.add(x.size(),4000);
            y.add(y.size(),1);
            //Top block
            x.add(x.size(),424);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),243);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),2425);
            y.add(y.size(),1);
        }
        if(a1==1){
            //T-SHAPE
            //Central Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //Top block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),3);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),5);
            y.add(y.size(),1);
            c= new Color(148,0,211);
        }
        else if(a1==2){
            //S-SHAPE
            //Central Block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Top block
            x.add(x.size(),5);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),3);
            y.add(y.size(),1);
            c=new Color(220,20,60);
        }
        else if(a1==3){
            //BACKWARDS-S-SHAPE
            //Central Block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Top block
            x.add(x.size(),3);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),5);
            y.add(y.size(),1);
            c= new Color(50,205,50);
        }
        else if(a1==4){
            //BLOCK-SHAPE
            //Central Block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Top block
            x.add(x.size(),5);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),5);
            y.add(y.size(),1);
            c= new Color(235,235,0);
            rot=false;
        }
        else if(a1==5){
            //LINE-SHAPE
            //Central Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //Top block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),4);
            y.add(y.size(),2);
            //rIGHT bLOCK
            x.add(x.size(),4);
            y.add(y.size(),3);
            c= new Color(0,191,255);
            
        }
        else  if(a1==6){
            //L SHAPED-SHAPE
            //Central Block
            x.add(x.size(),5);
            y.add(y.size(),1);
            //Top block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),6);
            y.add(y.size(),1);
            c= new Color(0,0,205);
            
        }
        else  if(a1==7){
            //Backwards-L SHAPED-SHAPE
            //Central Block
            x.add(x.size(),3);
            y.add(y.size(),1);
            //Top block
            x.add(x.size(),4);
            y.add(y.size(),0);
            //Left Block
            x.add(x.size(),4);
            y.add(y.size(),1);
            //rIGHT bLOCK
            x.add(x.size(),2);
            y.add(y.size(),1);
            c= new Color(255,140,0);
            
        }
        
        move = true;
    }

    public void draw(Graphics2D g)

    {  
        g.setColor(Color.RED);
        for(int a=0;a<=3;a++){
            g.setColor(c);
            g.fillRect(300+x.get(a)*35,40+y.get(a)*35,35,35);
            g.setColor(Color.BLACK);
            g.drawRect(300+x.get(a)*35,40+y.get(a)*35,35,35);
        }

    }

    public void rotate(){
        if(rot){
            for(int a=1;a<=3;a++){
                int xx=x.get(a);
                int yy=y.get(a);
                xx-=x.get(0);
                yy-=y.get(0);
                x.remove(a);
                y.remove(a);
                x.add(a,x.get(0)-yy);
                y.add(a,y.get(0)+xx);
               
            }
        }
    }

    public void down(){
        for(int a=0;a<=3;a++){
            int yy=y.get(a);
            y.remove(a);
            y.add(a,yy+1+yv);
        }

    }
    public void up(){
        for(int a=0;a<=3;a++){
            int yy=y.get(a);
            y.remove(a);
            y.add(a,yy-1+yv);
        }
    }

    public void left(){
        for(int a=0;a<=3;a++){
            if(x.get(a)==0){
                move=false;
            }

        }
        if(move ==true){
            for(int a=0;a<=3;a++)
            {
                int xx=x.get(a);
                x.remove(a);
                x.add(a,xx-1);

            }
        }
        move = true;
    }

    public void right(){
        for(int a=0;a<=3;a++){
            if(x.get(a)==9){
                move=false;
            }

        }
        if(move==true){
            for(int a=0;a<=3;a++)
            {
                int xx=x.get(a);
                x.remove(a);
                x.add(a,xx+1);

            }
        }
        move  = true;
    }

    public ArrayList<Integer> getXar(){
        return x;
    }

    public ArrayList<Integer> getYar(){
        return y;
    }
    public Integer geta1(){
        return val;
    }

    public void die(int a){
        x.remove(a);
        x.add(a,200);
    }

    public void onedown(int a){

        int yy=y.get(a);
        y.remove(a);
        y.add(a,yy+1);

    }
    public boolean getRotation(){
        return !rot;
    }
}
