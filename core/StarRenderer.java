package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.Random;

public class StarRenderer extends Renderer{
	public StarRenderer(int width, int height, int tileSize, Color[] colors, byte[] shapes, int elementsUsed){
		super(width, height, tileSize, colors, shapes, elementsUsed);
	}
	public void clear(int frame){
		int xSize = width * tileSize;
		int ySize = height * tileSize;
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				imgInt[x + y * xSize] = (x + frame) % 0x100 + (y + frame) * 0x100 % 0x10000 + (x + y + frame) % 255 * 0x10000;
			}
		}
	}
	public void drawElement(int x, int y, int frame, byte elementType){
		Graphics2D g2 = (Graphics2D) g;
		g2.setXORMode(colors[elementType]);
		drawShape(x, y, shapes[elementType]);
	}
}
