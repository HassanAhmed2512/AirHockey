package Final;

import static Final.START_GAME.pla;
import static Final.sounds.playMusic;
import com.sun.opengl.util.GLUT;
import java.awt.Component;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

public class OnePlayersMouseEasy implements GLEventListener, MouseMotionListener, KeyListener {

    GLUT g = new GLUT();
    String name = (String) JOptionPane.showInputDialog(null, "Enter Name", "Player 1");

    boolean moveRight = true;
    int UP = 0;
    int DOWN = 180;
    int changing_angel_Player1 = UP;
    int changing_angel_Player2 = DOWN;
/////////////////////////

////////////////////////
    long Timer = 3600;

    float X0ball = 0;
    float Y0ball = 0;
    float slope = 0;
    float Xball = X0ball;
    float Yball = Y0ball;
    boolean movingRight = true;
    boolean movingUp = true;
    boolean verticle = false;
    boolean up = false;
    boolean down = false;
    boolean play = false;
 

    ////////
    int maxWidth = 100;
    int maxHeight = 100;
    int XforPlayer1 = 0, YforPlayer1 = -80, scoreplayer1;
    int XforPlayer2 = 0, YforPlayer2 = 80, scoreplayer2;

    String textureNames[] = {"24.png", "4.png", "123.png", "Back.jpg"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    private GLCanvas glc;

    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("Assets" + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        gl.glLoadIdentity();
        gl.glOrtho(-maxWidth, maxWidth, -maxHeight, maxHeight, -1, 1);
    }

    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer

        DrowTheBallAndBack(gl, 0, 0, 100, 3); /////The BackGround

        //////FirstPlayer/////////
        DrawSpritePlayer1(gl, XforPlayer1, YforPlayer1, 1, 15, changing_angel_Player1);

        /////////////Computer////////////////
        DrawSpritePlayer1(gl, XforPlayer2, YforPlayer2, 0, 15, changing_angel_Player2);

        //////////////The win method Drow The Ball and check if it go to the 
        win(gl);
///////////////Here To show the Score ////
        gl.glRasterPos2i(-79, 0);
        g.glutBitmapString(8, Integer.toString((int) (Timer--)/60));
        gl.glRasterPos2i(-79, 90);
        g.glutBitmapString(5, "Computer");
        gl.glRasterPos2i(-79, 80);
        g.glutBitmapString(5, Integer.toString(scoreplayer2));
        gl.glRasterPos2i(-79, -80);
        g.glutBitmapString(5, name);
        gl.glRasterPos2i(-79, -70);
        g.glutBitmapString(5, Integer.toString(scoreplayer1));

    }

    void stupidAi() {
//        if (XforPlayer2 < 74 && moveRight) {
//            XforPlayer2++;
//
//            if (XforPlayer2 >= 74) {
//                moveRight = false;
//            }
//        } else {
//            if (!moveRight && XforPlayer2 > -75) {
//                XforPlayer2--;
//
//            }
//            if (XforPlayer2 <= -75) {
//                moveRight = true;
//            }
//
//        }
        if (XforPlayer2 > Xball + 30) {
            XforPlayer2--;
        }
        if (XforPlayer2 < Xball - 30) {
            XforPlayer2++;
        }
        if (Yball >= 25 && YforPlayer2 >= Yball && YforPlayer2 > 40) {
            YforPlayer2--;
        }
        if (Yball <= 0 && YforPlayer2 < 90) {
            YforPlayer2++;
        }
        if (Yball > YforPlayer2 && YforPlayer2 < 90) {
            YforPlayer2++;
        }

    }

    public void drawball(GL gl) {

        ///// player 1
        if ((int) Math.sqrt(Math.pow(Xball - XforPlayer1, 2) + Math.pow(Yball - YforPlayer1, 2)) <= 15) {
            X0ball = XforPlayer1;
            Y0ball = YforPlayer1;
            play = true;
            verticle = (Xball - XforPlayer1 == 0);
            if (verticle) {
                if (Y0ball > Yball) {
                    down = true;
                } else {
                    up = true;
                }
            } else {
                down = up = false;
            }
            slope = (Yball - YforPlayer1) / (Xball - XforPlayer1);
            if (YforPlayer1 > Yball && slope < 0) {
                movingUp = false;
                movingRight = true;
                Xball += 10;
            }
            if (YforPlayer1 > Yball && slope > 0) {
                movingUp = false;
                movingRight = false;
                Xball -= 10;
            }

            if (YforPlayer1 < Yball && slope < 0) {
                movingUp = true;
                movingRight = false;
                Xball -= 10;
            }

            if (YforPlayer1 < Yball && slope > 0) {
                movingUp = true;
                movingRight = true;
                Xball += 10;
            }
        }
///////// Computer
        if ((int) Math.sqrt(Math.pow(Xball - XforPlayer2, 2) + Math.pow(Yball - YforPlayer2, 2)) <= 15 && movingUp) {
            if (movingUp && movingRight) {
                movingUp = movingRight = false;
            } else if (movingUp && !movingRight) {
                movingRight = true;
                movingUp = false;
            } else if (!movingUp && movingRight) {
                movingUp = true;
                movingRight = false;
            } else if (!movingUp && !movingRight) {
                movingRight = true;
                movingUp = false;
            }
        }
///////// the movement For the ball
        if (play) {
            if (!verticle) {
                Yball = (slope * (Xball - X0ball) + Y0ball);
            }

            if (movingRight) {
                if (Xball < 80) {
                    Xball += 1;
                } else {
                    movingRight = false;
                    slope *= -1;
                    X0ball = Xball;
                    Y0ball = Yball;
                }
            }
            if (!movingRight) {
                if (Xball > -80) {
                    Xball -= 1;
                } else {
                    movingRight = true;
                    slope *= -1;
                    X0ball = Xball;
                    Y0ball = Yball;
                }
            }

            if (movingUp) {
                if (!(Yball < 90)) {
                    slope *= -1;
                    X0ball = Xball;
                    Y0ball = Yball;
                    movingUp = false;
                }
            }
            if (!movingUp) {
                if (!(Yball > -90)) {
                    slope *= -1;
                    X0ball = Xball;
                    Y0ball = Yball;
                    movingUp = true;
                }
            }

            if (down) {
                Yball--;
                if (Yball <= -100) {
                    up = true;
                }
                down = false;
            }

            if (up) {
                Yball++;
                if (Yball >= 100) {
                    down = true;
                }
                up = false;
            }
        }

        DrowTheBallAndBack(gl, Xball, Yball, 10, 2);
    }

    public void win(GL gl) {
        ///////////Bound For First Goal////////////
        if ((Xball > -30 && Xball < 30) && Yball <= -90 ) {
            reset();
            scoreplayer2++;
        }
        ///////////Bound For Second Goal////////////
        if ((Xball > -30 && Xball < 30) && Yball >= 90 ) {
            reset();
            scoreplayer1++;
        }

        /////////////////// The Final Score for Each one ///////
         if (Timer == 0 && scoreplayer2 >= scoreplayer1) {
            scoreplayer2 = 0;
            scoreplayer1 = 0;
            new Lost().setVisible(true);
            String filepath = "src\\Audio\\challenge-lose-By-Tuna.wav";
            playMusic(filepath);
            pla.clip.start();
            JOptionPane.showMessageDialog(null, " Loser " + name + " ;D");
        }
        if (Timer == 0 && scoreplayer2 < scoreplayer1) {
            scoreplayer2 = 0;
            scoreplayer1 = 0;
            new win().setVisible(true);
            String filepath = "src\\Audio\\win-By-Tuna.wav";
            playMusic(filepath);
            pla.clip.start();
            JOptionPane.showMessageDialog(null, " Good Job " + name + " :O");
        }
        if (scoreplayer2 >= 3) {
            scoreplayer2 = 0;
            scoreplayer1 = 0;
            new Lost().setVisible(true);
            String filepath = "src\\Audio\\challenge-lose-By-Tuna.wav";
            playMusic(filepath);
            pla.clip.start();
            JOptionPane.showMessageDialog(null, " Loser " + name + " ;D");

        } else if (scoreplayer1 >= 3) {

            scoreplayer2 = 0;
            scoreplayer1 = 0;

            new win().setVisible(true);
            String filepath = "src\\Audio\\win-By-Tuna.wav";
            playMusic(filepath);
            pla.clip.start();
            JOptionPane.showMessageDialog(null, " Good Job " + name + " :O");

        } 
        drawball(gl);
    }

    public void reset() {
        //////////////////Rest Every Thing/////////

        XforPlayer1 = 0;
        YforPlayer1 = -80;
        XforPlayer2 = 0;
        YforPlayer2 = 80;
        X0ball = 0;
        Y0ball = 0;
        slope = 0;
        Xball = X0ball;
        Yball = Y0ball;
        movingRight = true;
        movingUp = true;
        verticle = false;
        up = false;
        down = false;
        play = false;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrowTheBallAndBack(GL gl, float x, float y, int r, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled(r, r, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSpritePlayer1(GL gl, int x, int y, int index, float scale, int angle) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glScaled(scale, scale, 1);
        gl.glRotatef(angle, 0, 0, 1); //Dir
        //System.out.println(XforPlayer1 +" " + YforPlayer1);
        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
        stupidAi();

    }

    void setCanvas(GLCanvas glcanvas) {
        this.glc = glcanvas;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        //////////////X and Y for The Ai ////////////////
        XforPlayer1 = (int) (((x / width) * 200) - 100);
        YforPlayer1 = (int) (((y / height) * 200) - 50);
        ///////////// Here to make it in deafalt place /////////
        YforPlayer1 = 50 - YforPlayer1;
        ///////// To make it don't go out his height//////////////
        if (YforPlayer1 > 0) {
            YforPlayer1 = YforPlayer1 % 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
