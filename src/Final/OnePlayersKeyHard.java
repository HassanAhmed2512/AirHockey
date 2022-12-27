package Final;

import static Final.START_GAME.pla;
import static Final.sounds.playMusic;
import com.sun.opengl.util.GLUT;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;
import java.io.IOException;
import java.util.BitSet;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

public class OnePlayersKeyHard implements GLEventListener, KeyListener {

    GLUT g = new GLUT();
///////// For Dir/////////////////
    int UP_RIGHT = 315;
    int UP_LEFT = 45;
    int DOWN_LEFT = 135;
    int DOWN_RIGHT = 225;
    int UP = 0;
    int DOWN = 180;
    int LEFT = 90;
    int RIGHT = 270;
    int changing_angel_Player1 = UP;
    int changing_angel_Player2 = DOWN;
/////////////////////////For The Ball///////////////////////
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
    

    /////////////Frame + Players ////////////////////////
    int maxWidth = 100;
    int maxHeight = 100;
    int XforPlayer1 = 0, YforPlayer1 = -80, scoreplayer1;
    int XforPlayer2 = 0, YforPlayer2 = 80, scoreplayer2;
    private GLCanvas glc;

    ////////////////////////////
    String name = (String) JOptionPane.showInputDialog(null, "Enter Name", "Player 1");
    String textureNames[] = {"24.png", "4.png", "123.png", "Back.jpg"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    /*
     5 means gun in array pos
     x and y coordinate for gun 
     */
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
        handleKeyPress();

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
        if (XforPlayer2 > Xball ) {
            XforPlayer2--;
        }
        if (XforPlayer2 < Xball ) {
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
        if ((Xball > -30 && Xball < 30) && Yball <= -90) {
            reset();
            scoreplayer2++;
        }
        ///////////Bound For Second Goal////////////
        if ((Xball > -30 && Xball < 30) && Yball >= 90) {
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
        if (scoreplayer2 >= 7) {
            scoreplayer2 = 0;
            scoreplayer1 = 0;
            new Lost().setVisible(true);
            String filepath = "src\\Audio\\challenge-lose-By-Tuna.wav";
            playMusic(filepath);
            pla.clip.start();
            JOptionPane.showMessageDialog(null, " Loser " + name + " ;D");

        } else if (scoreplayer1 >= 7) {

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

    public void handleKeyPress() {

        if (isKeyPressed(VK_LEFT)) {
            if (XforPlayer1 > -maxWidth + 25) {
                XforPlayer1--;
            }
        }

        if (isKeyPressed(VK_RIGHT)) {
            if (XforPlayer1 < maxWidth - 25) {
                XforPlayer1++;
            }
        }

        if (isKeyPressed(VK_DOWN)) {
            if (YforPlayer1 > -(maxHeight / 4) - 60) {
                YforPlayer1--;
            }
        }

        if (isKeyPressed(VK_UP)) {
            if (YforPlayer1 < (maxHeight / 4) - 30) {
                YforPlayer1++;
            }
        }
        //Directions Player1
        if (isKeyPressed(VK_RIGHT) && isKeyPressed(VK_UP)) {
            changing_angel_Player1 = UP_RIGHT;
        } else if (isKeyPressed(VK_LEFT) && isKeyPressed(VK_UP)) {
            changing_angel_Player1 = UP_LEFT;
        } else if (isKeyPressed(VK_LEFT) && isKeyPressed(VK_DOWN)) {
            changing_angel_Player1 = DOWN_LEFT;
        } else if (isKeyPressed(VK_RIGHT) && isKeyPressed(VK_DOWN)) {
            changing_angel_Player1 = DOWN_RIGHT;
        } else if (isKeyPressed(VK_UP)) {
            changing_angel_Player1 = UP;
        } else if (isKeyPressed(VK_RIGHT)) {
            changing_angel_Player1 = RIGHT;
        } else if (isKeyPressed(VK_LEFT)) {
            changing_angel_Player1 = LEFT;
        } else if (isKeyPressed(VK_DOWN)) {
            changing_angel_Player1 = DOWN;
        }
//////////////////////////////////////////////////////////

    }

    void setCanvas(GLCanvas glcanvas) {
        this.glc = glcanvas;
    }
    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);

    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
