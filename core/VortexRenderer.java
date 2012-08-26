package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Graphics;
import java.util.Random;

public class VortexRenderer extends Renderer{
	public VortexRenderer(int width, int height, int tileSize, Color[] colors, byte[] shapes, int elementsUsed){
		super(width, height, tileSize, colors, shapes, elementsUsed);
		initSine(256, 256 / 6);
	}

	int[][] coeffs = new int[][]{new int[]{1, 2, 3}, new int[]{4, 3, 2}, new int[]{3, 7, 5}, new int[]{11, 7, 13}, new int[]{17, 23, 19}};

	public void clear(int frame){
		int l = rand.nextInt(coeffs.length);
		int m = rand.nextInt(coeffs[0].length);
		coeffs[l][m] += rand.nextInt(5) - 2;
		if(coeffs[l][m] < 0) coeffs[l][m] = 0;
		int xSize = width * tileSize;
		int ySize = height * tileSize;
		int[] c = new int[3];
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				c[0] = c[1] = c[2] = 128;

				for(int i = 0; i < coeffs.length; i++){
					c[i / 2] += (int)(intSine(x * coeffs[i][0] + y * coeffs[i][1] + frame * coeffs[i][2])); 
				}
				imgInt[x + y * xSize] = c[0] << 16 | c[1] << 8 | c[2];
			}
		}
		//g.setColor(Color.BLACK);
		//g.fillRect(0, 0, width * tileSize, height * tileSize);
	}
	int[][] tempShape = new int[2][10];
	public void drawElement(int x, int y, int frame, byte elementType){
		Graphics2D g2 = (Graphics2D) g;
		g2.setXORMode(colors[elementType]);
		drawShape(x, y, shapes[elementType]);
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
