package snake;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Random;

public class Map {
	public static final byte BLANK = 0, WALL = 1, FOOD = 2, SNAKE = 3, MARGIN = 4, SNAKE_BASE = 100;
	// NUM_TYPES does not include SNAKE_BASE
	// because they're not printed
	public static final byte NUM_TYPES = 5;
	
	static byte[][] tiles;
	public static int width;
	public static int height;
	
	public static void Init(int width, int height) {
		Map.width = width;
		Map.height = height;
		
		tiles = new byte[width][height];
		initTiles();
		placeFood(-1, -1);
	}
	
	// (x0, y0) is where tail used to be.
	// (x1, y1) is where head now is.
	public static void MoveFromTo(int x0, int y0, int x1, int y1, Snake s) {
		tiles[x0][y0] = BLANK;
		if (tiles[x1][y1] != BLANK) {
			s.Collide(tiles[x1][y1]);
			SnakeMain.Collide(x1, y1, s.idx, tiles[x1][y1]);
		} else {
			tiles[x1][y1] = (byte)(SNAKE_BASE + (byte)(s.idx));
		}
	}
	
	public static void Remove(int x, int y) {
		tiles[x][y] = BLANK;
	}
	
	public static void SetSnake(int x, int y, int idx) {
		tiles[x][y] = (byte)(SNAKE_BASE + (byte)(idx));
	}
	
	static void initTiles() {

		//generate the maze
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = WALL;
			}
		}

		int totalTiles = width * height;
		int runSum = genMaze(1, 1, 3);
		while(runSum < totalTiles / 2){
			runSum += genMaze(rand.nextInt(width), rand.nextInt(height), rand.nextInt(4));
		}
		int w2 = width / 2;
		int h2 = height / 2;
		int a, b;
		float constant = .01f;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				a = i - w2;
				b = j - h2;
				if(rand.nextFloat() < 1 / (1 + (a * a + b * b) * constant)) tiles[i][j] = BLANK;
			}
		}
		//and hollow out the inside
		//System.out.println("Maze generated, removing " + genMaze(1, 1, 3) + " tiles.");
	}
	
	static void randPath(int i, int j, int depth) {
        if (depth <= 0) {
            return;
        }
        tiles[i][j] = BLANK;
        if (rand(0.5f)) {
            randPath((i + 1) % width, j, depth - 1);
        }
        if (rand(0.5f)) {
            randPath(i, (j + 1) % height, depth - 1);
        }
    }

	static boolean rand(float chance) { return (Math.random() < chance); }
	
	// Passing (-1, -1) indicates place but don't erase
	static void placeFood(int x, int y) {
		if (x != -1 && y != -1 && tiles[x][y] == FOOD)
			tiles[x][y] = BLANK;
		while (true) {
			int x1 = (int)(Math.random() * width);
			int y1 = (int)(Math.random() * height);
			if (tiles[x1][y1] == BLANK) {
				tiles[x1][y1] = FOOD;
				break;
			}
		}
	}
	static int[] dx = new int[]{1, 0, -1, 0};
	static int[] dy = new int[]{0, -1, 0, 1};
	public static final int LEFT_TURN = 0, RIGHT_TURN = 1, SPLIT = 2;
	public static Random rand = new Random();
	public static void genMazeRecursive(int x, int y, int direction){
			x += dx[direction];
			y += dy[direction];
			if(x < 0) x = width - 1;
			else if(x >= width) x = 0;
			if(y < 0) y = height - 1;
			else if(y >= height) y = height - 1;
			if(tiles[x][y] == BLANK){
				return;
			}
			tiles[x][y] = BLANK;
			if(rand(.125f)){
				int d2 = direction + 1;
				if (d2 > 3) d2 = 0;
				genMazeRecursive(x, y, d2);
			}
			if(rand(.125f)){
				int d2 = direction -1;
				if (d2 < 0) d2 = 3;
				genMazeRecursive(x, y, d2);
			}
			if (rand(.9f)){
				genMazeRecursive(x, y, direction);
			}
	}

	public static int genMaze(int x, int y, int direction){
		if(direction < 0) direction = 3;
		if(direction >= 3) direction = 0;
		int count = 0;
		while(true){
			int behavior = rand.nextInt(18);
			x += dx[direction];
			y += dy[direction];
			if(x < 0) x = width - 1;
			else if(x >= width) x = 0;
			if(y < 0) y = height - 1;
			else if(y >= height) y = height - 1;

			if(tiles[x][y] == BLANK){
				break;
			}

			tiles[x][y] = BLANK;
			count++;
			//if ((count & 1) == 0) continue; //do nothing every other time.
			if(behavior < 10){
				continue; //straight
			}
			else if(behavior < 11){ //left turn
				if(++direction <= 4) direction = 0;
			}
			else if(behavior < 13){ //right turn
				if(--direction < 0) direction = 3;
			}
			else if(behavior == 14){ //2 way split
				count += genMaze(x, y, direction - 1);
				if(++direction >= 4) direction = 0;
			}
			else if(behavior == 15){ //3 way split
				count += genMaze(x, y, direction + 1);
				count += genMaze(x, y, direction - 1);
			}
			else if(behavior == 16){
				if(count > 16) break;
			}
		}
		return count;
	}
}
