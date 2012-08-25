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
		initSine(256, 256 / 6);
	}

	public void randomizeColors(){
		Random rand = new Random();
		for(int i = 0; i < elementsUsed; i++){
			colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		}
	}
	public void effect(){
		randomizeColors();
	}

	int frame = 0;
	
	int[][] coeffs = new int[][]{new int[]{1, 2, 3}, new int[]{4, 3, 2}, new int[]{3, 7, 5}, new int[]{11, 7, 13}, new int[]{17, 23, 19}};

	public void clear(){
		frame++;
//System.out.println(frame++);
		int xSize = width * tileSize;
		int ySize = height * tileSize;
		int[] c = new int[3];
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				c[0] = c[1] = c[2] = 128;

				for(int i = 0; i < coeffs.length; i++){
					c[i / 2] += (int)(intSine(x * coeffs[i][0]) + intSine(y * coeffs[i][1]) + intSine(frame * coeffs[i][2])); 
				}
				imgInt[x + y * xSize] = c[0] << 16 | c[1] << 8 | c[2];
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
public static final float B = (float)(4/Math.PI), C = (float)(-4/(Math.PI*Math.PI));
public static float sine(float x)
{
    float y = B * x + C * x * Math.abs(x);

/*    #ifdef EXTRA_PRECISION
    //  const float Q = 0.775;
        const float P = 0.225;

        y = P * (y * abs(y) - y) + y;   // Q * y + P * y * abs(y)
    #endif
*/
    return y;
}

	public int[] sVals;
	public int range;
	public int intSine(int in){
		return sVals[in % range];
	}
	public void initSine(int range, int domain){
		sVals = new int[range];
		domain /= 2;
		this.range = range;
		float constant = (float)(2 * Math.PI / range);
		for(int i = 0; i < range; i++){
			sVals[i] = domain + (int)(Math.sin(i * constant) * domain);
		}
	}
}
