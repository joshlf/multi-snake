package core;
import java.awt.event.*;
import snake.*;

public class DirectionalController implements KeyListener{
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3, NONE = 4;
    //keyMappings follows [player][key] indexing.
    public static int[][] keyMappings = new int[][] {
	new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN}, 
	new int[]{KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S}
    };
    public boolean[] downs = new boolean[keyMappings.length];
    public int players;
    public int[] keys;

    public DirectionalController(int players){
	this.players = players;
	keys = new int[players];
    }
//key events
    public void keyReset()
    {
        for(int i = 0; i < downs.length; i++) {
            downs[i] = false;
	    keys[i] = NONE;
        }
    }
    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e)
    {
        //keys[e.getKeyCode()];
    }

    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e)
    {
//#Pretend this is a hash table
        int key = e.getKeyCode();
        for(int i = 0; i < keyMappings.length; i++) {
            for(int j = 0; j < keyMappings[i].length; j++) {
                if(key == keyMappings[i][j]) {
                    if (!downs[i]) {
			//System.out.println("Direction " + j);
                        downs[i] = true;//downs[i] = 1;
                        keys[i] = j;
                    }
                    return;
                }
            }
        }
	if (key == KeyEvent.VK_ESCAPE){
		SnakeMain.kill();
	}
//#Stop pretending.
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e)
    {
        //int key = e.getKeyCode();
        //if (key > keys.length);
        //keys[key] = false;
    }

    public int getKeyPressed(int index){
	return keys[index];
    }
}
