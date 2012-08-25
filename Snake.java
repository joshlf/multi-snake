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
	
	private int startX;
	private int startY;
	private int startLength;
	private int startDX;
	private int startDY;
	private int startLives;
	
	public Snake(int startX, int startY, int lives, int idx) {
		this.x = new int[MAX_LENGTH];
		this.y = new int[MAX_LENGTH];
		this.startX = startX;
		this.startY = startY;
		this.startLives = lives;
		this.idx = idx;
		
		this.Init();
	}
	
	private void init() {
		this.x[0] = this.startX;
		this.x[0] = this.startY;
		this.length = 1;
		this.ptr = length - 1;
		this.score = 0;
	}
	
	public void Update() {
		switch (Main.controller.getKeyPressed(this.idx)) {
			case Main.controller.UP:
			dx = 0;
			dy = -1;
			break;
			case Main.controller.LEFT:
			dx = -1;
			dy = 0;
			break;
			case Main.controller.DOWN:
			dx = 0;
			dy = 1;
			break;
			case Main.controller.RIGHT:
			dx = 1;
			dy = 0;
			break;
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
		
		for (int i = this.addFoodLength; i < newLength; i++) {
			tmpX[i] = this.x[(this.ptr + i - this.addFoodLength) % this.length];
			tmpY[i] = this.y[(this.ptr + i - this.addFoodLength) % this.length];
		}
		
		for (int i = this.addFoodLength; i < newLength; i++) {
			this.x[i] = tmpX[i];
			this.y[i] = tmpY[i];
		}
		
		int dx = this.x[newLength - 1] - this.x[newLength];
		int dy = this.y[newLength - 1] - this.y[newLength];
		
		this.x[0] = this.x[newLength] + dx;
		this.y[0] = this.y[newLength] + dy;
		
		for (int i = 1; i < this.addFoodLength; i++) {
			this.x[i] = this.x[i - 1] + dx;
			this.y[i] = this.y[i - 1] + dx;
		}
		
		this.length = newLength;
		this.ptr = this.addFoodLength;
	}
	
	public void Collide(byte item) {
		switch (item) {
			case Map.FOOD:
			this.eat();
			break;
		}
	}
	
	public void Die() {
		this.lives--;
		this.init();
	}
}
