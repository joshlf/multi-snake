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
		this.moveSnake();
		Map.MoveTo(this.x[this.ptr], this.y[this.ptr], this);
	}
	
	private void moveSnake() {
		int oldPtr = this.ptr;
		this.ptr = (this.ptr + 1) % this.length;
		this.x[ptr] = (this.x[oldPtr] + dx) % Map.width;
		this.y[ptr] = (this.y[oldPtr] + dy) % Map.height;
	}
	
	private void eat() {
		int newLength = this.length + this.addFoodLength;
		int[] tmpX = new int[newLength];
		int[] tmpY = new int[newLength];
		
		for (int i = addFoodLength; i < newLength; i++) {
			tmpX[i] = this.x[(this.ptr + i - addFoodLength) % this.length];
			tmpY[i] = this.y[(this.ptr + i - addFoodLength) % this.length];
		}
		
		for (int i = addFoodLength; i < newLength; i++) {
			this.x[i] = tmpX[i];
			this.y[i] = tmpY[i];
		}
		
		int dx = this.x[newLength - 1] - this.x[newLength];
		int dy = this.y[newLength - 1] - this.y[newLength];
		
		this.x[0] = this.x[newLength] + dx;
		this.y[0] = this.y[newLength] + dy;
		
		for (int i = 1; i < addFoodLength; i++) {
			this.x[i] = this.x[i - 1] + dx;
			this.y[i] = this.y[i - 1] + dx;
		}
	}
	
	public void Collide(byte item) {
		switch item {
			case Map.FOOD:
			this.eat();
		}
	}
}