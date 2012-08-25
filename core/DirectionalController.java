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
    public int QUEUESIZE = 16;
    public int players;
    public int[][] keyCues;
    public int[] keyCuePos;

    public DirectionalController(int players){
	this.players = players;
	keyCuePos = new int[players];
	keyCues = new int[players][QUEUESIZE];
    }
//key events
    public void keyReset()
    {
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
		    if(keyCuePos[i] >= QUEUESIZE)
			System.out.println("Please stop pressing buttons.");
                    else 
			keyCues[i][keyCuePos[i]++] = j;
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
	keyCuePos[index]--;
	if(keyCuePos[index] < 0){
		keyCuePos[index] = 0;
		return NONE;
	}
	return keyCues[index][keyCuePos[index]];
    }
}
