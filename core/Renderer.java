package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;

import java.util.Random;

import snake.Map;

public abstract class Renderer{
	//Info used by most/all renderers
	public static final byte SQUARE = 0, CIRCLE = 1;
	byte[] shapes;
	Color[] colors;
	int elementsUsed;

	//vital renderer info
	int width, height;
	int tileSize;

	//everything is buffered, the image buffer is then drawn to screen.
	public BufferedImage buffer;
	public Graphics g;

	public Renderer(int width, int height, int tileSize, Color[] colors, byte[] shapes, int elementsUsed){
		this.width = width;
		this.height = height;
	    	this.tileSize = tileSize;
		buffer = new BufferedImage(width * tileSize, height * tileSize, BufferedImage.TYPE_INT_RGB);
		g = buffer.getGraphics();
		this.colors = colors;
		this.shapes = shapes;
		this.elementsUsed = elementsUsed;
		if(elementsUsed == 0){
			randomize(127);
		}
	}
	public void effect(){
		randomize(127);
	}
	public void randomize(int elementsUsed){
		Random rand = new Random();
		if(elementsUsed > this.elementsUsed){
			colors = new Color[elementsUsed];
			shapes = new byte[elementsUsed];
		}
		//else we can use the existing buffers.
		for(int i = 0; i < elementsUsed; i++){
			colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			shapes[i] = (byte)rand.nextInt(2);
		}
	}
	public abstract void clear(int frame);
	public abstract void drawElement(int x, int y, int frame, byte elementType);
	public void renderMap(byte[][] map, int frame){
		int xM = Math.min(map.length, width);
		int yM = Math.min(map[0].length, height);
		for(int x = 0; x < xM; x++)
			for(int y = 0; y < yM; y++)
				if(map[x][y] > 0 && map[x][y] != Map.SNAKE) drawElement(x, y, frame, map[x][y]);
	}
	//Color related items
	public void setElements(Color[] colors, byte[] shapes){
		if(colors != null) this.colors = colors;
		if(shapes != null) this.shapes = shapes;
		elementsUsed = Math.min(colors.length, shapes.length);
	}
	public void setColor(Color color, int index){
		if(index < elementsUsed) colors[index] = color;
		else System.err.printf("ERROR: Color index %d out of bounds (size %d).\n", index, elementsUsed);
	}
	public void setShape(Color color, int index){
		if(index < elementsUsed) colors[index] = color;
		else System.err.printf("ERROR: Shape index %d out of bounds (size %d).\n", index, elementsUsed);
	}
}
