package Final;



import com.sun.opengl.util.*;
import java.awt.*;
import javax.media.opengl.*;
import javax.swing.*;

public class RunForOnePlayerWithMouseEasy extends JFrame {

    public static void main(String[] args) {
        new RunForOnePlayerWithMouseEasy();
    }


        static String lol ="on";
        
    public RunForOnePlayerWithMouseEasy() {
        GLCanvas glcanvas;
        Animator animator;
        OnePlayersMouseEasy listener = new OnePlayersMouseEasy();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        
        glcanvas.addMouseMotionListener(listener);
        glcanvas.addKeyListener(listener);
        
        listener.setCanvas(glcanvas);
        
        add(glcanvas, BorderLayout.CENTER);
        
        animator = new FPSAnimator(60);
        animator.add(glcanvas);
//        int x=lol(1);
//        
//        if(x==1){
            animator.start();
//        }
//        else{
//                    animator.stop();
//        }
         
        setTitle("Air Hockey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
    
//    public static int lol(int x){
//        
//        return x;
//    
//    }
    
    
    
}
