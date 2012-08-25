package core;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;

import java.awt.event.KeyListener;

// SnakeFrame is the main loop of the game,
// handling logic and movement from
// frame to frame.
public class GameFrame
{
    int width, height;
    Frame frame;
    Graphics frameG;
    public GameFrame(int width, int height, KeyListener listener){
        frame = new Frame();
        frame.setSize(width + 10, height + 20);
        frame.setVisible(true);
        frame.addKeyListener(listener);
        frameG = frame.getGraphics();
    }

    //Graphics
    public void render(Image image)
    {
        // bufferG.drawImage(buffer, 5, 20, null);
        frame.getGraphics().drawImage(image, 5, 20, null);
        // frame.repaint();
    }
}
