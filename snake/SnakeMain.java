package snake;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.util.Random;


import core.*;

public class SnakeMain {
	public static DirectionalController controller;
	public static Renderer renderer;
	public static int offsetX;
	public static int offsetY;

	static int FPS, TPF;
	
	public static Snake[] snakes;
	public static int snakeCount;
	private static boolean running = true;
	public static void main(String[] args) {
		int mapWidth = 100;
		int mapHeight = 75;
		
		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals("-w")) {
				mapWidth = Integer.parseInt(args[++i]);
			} else if (args[i].equals("-h")) {
				mapHeight = Integer.parseInt(args[++i]);
			}
		}
		
		Init(mapWidth, mapHeight, 3, 3, 2);
		while (true) {
			initLevel();
			run();
		}
	}
	
	
	public static void Init(int width, int height, int xMarg, int yMarg, int snakeCount) {
		SnakeMain.offsetX = xMarg;
		SnakeMain.offsetY = yMarg;
		
		int tileWidth = 8;
		int[][] controls = new int[snakeCount][4];
		
		controls[0] = new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN};
		if (snakeCount > 1)
			controls[1] = new int[]{KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S};
		if (snakeCount > 2)
			controls[2] = new int[]{KeyEvent.VK_L, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K};
		
		controller = new DirectionalController(controls);
		// controller = new DirectionalController(new int[][] {
		// 		new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN}, 
		// 		new int[]{KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S},
		// 		new int[]{KeyEvent.VK_L, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K}
		// 	    });
		GameFrame.Init((width + xMarg * 2) * tileWidth, (height + yMarg * 2) * tileWidth, controller);

		byte[] shapes = new byte[Map.NUM_TYPES];
		Color[] colors = new Color[Map.NUM_TYPES];
		
		for (int i = 0; i < shapes.length; i++) {
			shapes[i] = Renderer.SHAPE_RANDOM;
		}
		for (int i = 0; i < colors.length; i++) {
			colors[i] = Renderer.COLOR_RANDOM;
		}
		
		shapes[Map.WALL] = Renderer.SHAPE_SQUARE;
		shapes[Map.SNAKE] = Renderer.SHAPE_SQUARE;
		shapes[Map.FOOD] = Renderer.SHAPE_SQUARE;
		
		renderer = new StarRenderer(width + xMarg * 2, height + yMarg * 2, tileWidth, colors, shapes, shapes.length);
		//renderer = new SolidColorRenderer(width, height, tileWidth, 127);

		Map.Init(width, height);
		
		SnakeMain.snakeCount = snakeCount;
		snakes = new Snake[snakeCount];
		for (int i = 0; i < snakeCount; i++) {
			snakes[i] = new Snake(i, i, 10, 0.5f, 10, i);
		}
		
		FPS = 20;
		TPF = 1000 / FPS;
	}
	
	static int frameCount;
	
	public static void initLevel() {
		
	}
	
	public static void run() {
		running = true;
		
		long t0 = System.currentTimeMillis();
		long t1;
		
	controller.keyReset();
		while (running) {
			for (Snake snake: snakes) {
				snake.Update();
			}
			controller.keyReset();
			render();

			t1 = System.currentTimeMillis();
			long timePassed = t1 - t0;
			t0 = t1;
			frameCount++;
			if (timePassed < TPF) {
				try {
					Thread.sleep(TPF - timePassed);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				t0 += TPF - timePassed;
			}
		}
	}

	public static void render(){
		renderer.clear(frameCount);
		for(Snake snake : snakes) {
			snake.render(renderer, frameCount);
		}
		renderer.renderMap(Map.tiles, frameCount, offsetX, offsetY);
		
		String scoreStr = "";
		
		for (int i = 0; i < snakes.length; i++) {
			scoreStr += "P" + i + ": " + snakes[i].score + "/";
			if (snakes[i].dead)
				scoreStr += "X ";
			else
				scoreStr += snakes[i].lives + " ";
		}
		
		renderer.blitText(null, scoreStr);
		GameFrame.render(renderer.buffer); //render to screen
	}
	
	public static void Collide(int x, int y, int idx, byte item) {
		if (item == Map.WALL) {
			snakes[idx].Die();
		} else if (item == Map.FOOD) {
			Map.placeFood(x, y);
		} else if (item >= Map.SNAKE_BASE) {
			snakes[idx].Die();
			int other = item - Map.SNAKE_BASE;
			if (snakes[other].x[0] == x && snakes[other].y[0] == y)
				snakes[other].Die();
		}
	}
	
	//clean up and kill the game
	public static void kill(){
		System.exit(0);
	}
}
