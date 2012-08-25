package core;

import java.awt.Graphics;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Font;

import java.util.Random;

import snake.Map;

import java.io.*;

public abstract class Renderer{
	//Shape/Color info
	public static final Color COLOR_RANDOM = null;
	public static final byte SHAPE_RANDOM = -1, SHAPE_SQUARE = 0, SHAPE_CIRCLE = 1, SHAPE_TRIANGLE = 2, SHAPE_STAR = 3, SHAPES = 3;
	byte[] shapes;
	Color[] colors;
	int elementsUsed;
	int[][][] shapePoints;

	//vital renderer info
	int width, height;
	int tileSize;

	//everything is buffered, the image buffer is then drawn to screen.
	public BufferedImage buffer;
	int[] imgInt;
	public Graphics g;

	public Renderer(int width, int height, int tileSize, Color[] colors, byte[] shapes, int elementsUsed){
		this.width = width;
		this.height = height;
	    	this.tileSize = tileSize;
		buffer = new BufferedImage(width * tileSize, height * tileSize, BufferedImage.TYPE_INT_RGB);
		imgInt = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
		g = buffer.getGraphics();
		this.colors = colors;
		this.shapes = shapes;
		this.elementsUsed = elementsUsed;
		if(elementsUsed == 0){
			randomize(127);
		}
		for(int i = 0; i < elementsUsed; i++){
			if(colors[i] == null) colors[i] = getRandomColor();
			if(shapes[i] == SHAPE_RANDOM) shapes[i] = getRandomShape();
		}
		//initialize shapes.
		shapePoints = new int[4][2][];
		shapePoints[SHAPE_TRIANGLE][0] = new int[3];
		shapePoints[SHAPE_TRIANGLE][1] = new int[3];
		//x
		shapePoints[SHAPE_TRIANGLE][0][0] = 0;
		shapePoints[SHAPE_TRIANGLE][0][1] = tileSize / 2;
		shapePoints[SHAPE_TRIANGLE][0][2] = tileSize;
		shapePoints[SHAPE_TRIANGLE][1][0] = tileSize;
		shapePoints[SHAPE_TRIANGLE][1][1] = 0;
		shapePoints[SHAPE_TRIANGLE][1][2] = tileSize;

	}
	public void effect(){
		//randomize(127);
	}
	public void randomize(int elementsUsed){
		if(elementsUsed > this.elementsUsed){
			colors = new Color[elementsUsed];
			shapes = new byte[elementsUsed];
		}
		//else we can use the existing buffers.
		for(int i = 0; i < elementsUsed; i++){
			colors[i] = getRandomColor();
			shapes[i] = getRandomShape();
		}
	}
	public abstract void clear(int frame);
	public void margin(int right, int top, int left, int bottom, int opacity){
		g.setPaintMode();
		g.setColor(new Color(0,0,0,opacity));
		g.fillRect(0, 0, width * tileSize, top * tileSize);//top
		g.fillRect(0, top * tileSize, left * tileSize, (height - top - bottom) * tileSize);//left
		g.fillRect(0, (height - top) * tileSize, width * tileSize, bottom * tileSize); //bottom
		g.fillRect((width - right) * tileSize, top * tileSize, right * tileSize, (height - top - bottom) * tileSize); //right
		/*
		int sWidth = width * tileSize;
		int sHeight = height * tileSize;
		for(int x = 0; x <= 16; x++){
			for(int y = 0; y < sHeight; y++){
				int i = 20 - x;
				int j = x + sWidth * y;	
				imgInt[j] = 
				((((imgInt[j] >> 16) * 4 / i)) << 16) | 				((((imgInt[j] & 0xff00) >> 8) * 4 / i) << 8) |
				((imgInt[j] & 0xff) * 4 / i);
				
				i = 4 + x;
				j = x + sWidth * y + sWidth - 17;
				imgInt[j] = 
				((((imgInt[j] >> 16) * 4 / i)) << 16) | 				((((imgInt[j] & 0xff00) >> 8) * 4 / i) << 8) |
				((imgInt[j] & 0xff) * 4 / i);

			}
		}
		*/
	}
	public abstract void drawElement(int x, int y, int frame, byte elementType);
	int[][] tempShape = new int[2][10];
	protected void drawShape(int x, int y, byte shape){
		x *= tileSize;
		y *= tileSize;
		switch(shape){
			case SHAPE_SQUARE:
				g.fillRect(x, y, tileSize, tileSize);
			break;
			case SHAPE_CIRCLE:
				g.fillOval(x, y, tileSize, tileSize);
			break;
			default:
				int l = shapePoints[shape][0].length;
				for(int i = 0; i < l; i++){
					tempShape[0][i] = shapePoints[shape][0][i] + x;
					tempShape[1][i] = shapePoints[shape][1][i] + y;
					//System.out.print("(" + tempShape[0][i] + ", " + tempShape[1][i] + "), ");
				}
				//System.out.println("");
				g.setColor(Color.black);
				g.fillPolygon(tempShape[0], tempShape[1], l);
			break;
		}
	}
/*
	public void renderStream(InputStream in){
		in = new DataInputStream(in);
		try{
			while(in.hasData()){
				drawElement(in.readShort(), in.readShort(), in.readByte());
			}		
		}	
	}
*/
	public void renderMap(byte[][] map, int frame, int offsetX, int offsetY){
		int xM = Math.min(map.length, width);
		int yM = Math.min(map[0].length, height);
		for(int x = 0; x < xM; x++)
			for(int y = 0; y < yM; y++)
				if(map[x][y] > 0 && map[x][y] < Map.SNAKE_BASE) drawElement(x + offsetX, y + offsetY, frame, map[x][y]);
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
	public static Random rand = new Random();
	public static Color getRandomColor(){
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}
	public static byte getRandomShape(){
		return (byte)rand.nextInt(SHAPES);
	}
	
	public void blitText(Color c, String text){
		if(c != null) g.setColor(c);
		g.setFont(new Font("Sans-Serif", Font.BOLD, 15));
		g.drawString(text, 15, 15);
	}
}
