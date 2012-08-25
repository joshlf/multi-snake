import java.util.Random;
import java.awt.Graphics;

public class Snake {
	private static final int MAX_LENGTH = 1600;
	
	// Player info & metadata
	public int score;
	public int idx;
	public int lives;
	
	// The physical snake
	private int[] x;
	private int[] y;
	private int length;
	private int ptr;
	private int dx, dy;
	private int addFoodLength;
	
	public Snake(int startX, int startY, int lives, int idx) {
		this.x = new int[MAX_LENGTH];
		this.y = new int[MAX_LENGTH];
		this.x[0] = startX;
		this.y[0] = startY;
		this.length = 1;
		this.ptr = length - 1;
		
		this.idx = idx;
		this.lives = lives;
		this.score = 0;
	}
	
	public void Update() {
		switch SnakeFrame.keyPressed(this.idx) {
			case SnakeFrame.UP:
			dx = 0;
			dy = -1;
			case SnakeFrame.LEFT:
			dx = -1;
			dy = 0;
			case SnakeFrame.DOWN:
			dx = 0;
			dy = 1;
			case SnakeFrame.RIGHT:
			dx = 1;
			dy = 0;
		}
		this.moveSnake()
		Map.MoveTo(this.x[this.ptr], this.y[this.ptr], this)
	}
	
	private void moveSnake() {
		int oldPtr = this.ptr;
		this.ptr = (this.ptr + 1) % this.length;
		this.x[ptr] = (this.x[oldPtr] + dx) % Map.width;
		this.y[ptr] = (this.y[oldPtr] + dy) % Map.height;
	}
}