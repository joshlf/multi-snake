package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.Random;

public class VortexRenderer extends Renderer{
	public static final byte SQUARE = 0, CIRCLE = 1;
	byte[] shapes;
	Color[] colors;
	int elementsUsed;
	int[] imgInt;
	public VortexRenderer(int width, int height, int tileSize, int elementsUsed){
		super(width, height, tileSize);
		shapes = new byte[127];
		colors = new Color[127];
		this.elementsUsed = elementsUsed;
		imgInt = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
		randomizeColors();
	}

	public void randomizeColors(){
		Random rand = new Random();
		for(int i = 0; i < elementsUsed; i++){
			colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		}
	}

	int frame = 0;
	public void clear(){
		frame++;
//System.out.println(frame++);
		int xSize = width * tileSize;
		int ySize = height * tileSize;
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				imgInt[x + y * xSize] = (x + frame) % 0x100 + (y + frame) * 0x100 % 0x10000 + (x + y + frame) % 255 * 0x10000;
			}
		}
		//g.setColor(Color.BLACK);
		//g.fillRect(0, 0, width * tileSize, height * tileSize);
	}
	public void drawElement(int x, int y, int frame, byte elementType){
		Graphics2D g2 = (Graphics2D) g;
		g2.setXORMode(colors[elementType]);
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
