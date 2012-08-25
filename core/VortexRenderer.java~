package core;
//A SolidColorRenderer keeps track of the color and shape of each elementType, and renders it accordingly.
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.Random;

public class SolidColorRenderer extends Renderer{
	public static final byte SQUARE = 0, CIRCLE = 1;
	byte[] shapes;
	Color[] colors;
	int elementsUsed;
	public SolidColorRenderer(int width, int height, int tileSize, int elementsUsed){
		super(width, height, tileSize);
		shapes = new byte[127];
		colors = new Color[127];
		this.elementsUsed = elementsUsed;
	}

	public void randomizeColors(){
		Random rand = new Random();
		for(int i = 0; i < elementsUsed; i++){
			colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		}
	}

	public void clear(){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width * tileSize, height * tileSize);
	}
	public void drawElement(int x, int y, byte elementType){
		g.setColor(colors[elementType]);
		x *= tileSize;
		y *= tileSize;
		switch(shapes[elementType]){
			case SQUARE:
				g.fillRect(x, y, tileSize, tileSize);
			break;
			case CIRCLE:
				g.fillOval(x, y, tileSize, tileSize);
			break;
		}
	}
}
