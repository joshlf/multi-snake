import java.awt.Graphics;
import java.util.Random;

import core.*;
public class SnakeMain {
	public DirectionalController control;
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
		
		Init(mapWidth, mapHeight, 2);
		while (true) {
			initLevel();
			run();
		}
	}
	
	static int FPS, TPF;
	
	public static Snake[] snakes;
	public static int snakeCount;
	public static void Init(int width, int height, int snakeCount) {
		int tileWidth = 8;
		
		controller = new DirectionalController();
		GameFrame.Init(width, height, controller);
		Map.Init(width, height, 2, 2);
		
		SnakeMain.snakeCount = snakeCount;
		snakes = new Snake[snakeCount];
		for (int i = 0; i < snakeCount; i++) {
			snakes[i] = new Snake(0, 0, 10, i);
		}
		
		FPS = 5;
		TPF = 1000 / FPS;
	}
	
	static int frameCount;
	
	public static void initLevel() {
		
	}
	
	public static void run() {
		running = true;
		
		long t0 = System.currentTimeMillis();
		long t1;
		
		while (running) {
			for (Snake snake: snakes) {
				snake.update();
			}
			
			render(SnakeFrame.bufferG);
			
			t1 = System.currentTimeMillis();
			long timePassed = t1 - t0;
			t0 = t1;
			if (timePassed < TMPF) {
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
	
	public static void Collide(int x, int y, int idx, byte item) {
		switch item {
			case WALL:
			snakes[idx].Die();
			case SNAKE:
			snakes[idx].Die();
		}
	}
}
