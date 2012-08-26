package snake;

import java.util.Random;
import java.awt.Graphics;

import core.*;
public class Snake {
	private static final int MAX_LENGTH = 1600;
	
	// Player info & metadata
	public int score;
	public int idx;
	public int lives;
	public boolean dead;
	
	// The physical snake
 	public int[] x;
	public int[] y;
	private int length;
	private int ptr;
	private int dx, dy;
	private int addFoodLength;
	private float moveRate = .06125f;
	private float moveCtr;
	
	private int startX;
	private int startY;
	private int startLength;
	private int startDX;
	private int startDY;
	private int startLives;
	private float startMoveRate;
	
	public Snake(int startX, int startY, int addFoodLength, float moveRate, int lives, int idx) {
		this.x = new int[MAX_LENGTH];
		this.y = new int[MAX_LENGTH];
		this.startX = startX;
		this.startY = startY;
		this.startLives = lives;
		this.lives = this.startLives;
		this.idx = idx;
		this.score = 0;
		this.addFoodLength = addFoodLength;
		this.startMoveRate = moveRate;
		this.dead = false;
		
		init();
	}
	
	private void init() {
		this.x[0] = this.startX;
		this.y[0] = this.startY;
		this.moveRate = this.startMoveRate;
		this.length = 1;
		this.ptr = length - 1;

		for (int i = 0; i < this.length; i++) {
			Map.SetSnake(this.x[i], this.y[i], this.idx);
		}
	}
	
	public void Update() {
		if (this.dead)
			return;
		moveCtr += moveRate;
		while(moveCtr > 1){
			int dir = SnakeMain.controller.getKeyPressed(this.idx);
			moveCtr --;
			if (dir == DirectionalController.UP && dy != 1) {
				dx = 0;
				dy = -1;
			} else if (dir == DirectionalController.LEFT && dx != 1) {
				dx = -1;
				dy = 0;
			} else if (dir == DirectionalController.DOWN && dy != -1) {
				dx = 0;
				dy = 1;
			} else if (dir == DirectionalController.RIGHT && dx != -1) {
				dx = 1;
				dy = 0;
			}
			this.moveSnake();
		}
	}
	
	private void moveSnake() {
		// Find first "displayed" snake bit at tail
		int i;
		for (i = (this.ptr + 1) % this.length; this.x[i] == -1; i = (i + 1) % this.length) {}
		int oldX = this.x[i];
		int oldY = this.y[i];
		
		int oldPtr = this.ptr;
		this.ptr = (this.ptr + 1) % this.length;
		this.x[this.ptr] = (this.x[oldPtr] + dx + Map.width) % Map.width;
		this.y[this.ptr] = (this.y[oldPtr] + dy + Map.height) % Map.height;
		
		Map.MoveFromTo(oldX, oldY, this.x[this.ptr], this.y[this.ptr], this);
	}
	
	private void eat() {
		grow(this.addFoodLength);
		
		this.moveRate += 0.01f;
		this.score++;
	}
	
	private void grow(int addFoodLength){
		int newLength = this.length + addFoodLength;
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
		
		// Assume that negative values won't fuck shit up.
		// If it does, try impossibly large values.
		// The idea here is that these are never rendered
		// until this part of the area is written over.
		for (int i = 0; i < addFoodLength; i++) {
			this.x[i] = (this.y[i] = -1);
		}
		
		this.length = newLength;
		this.ptr = addFoodLength;
	}
	
	public void Collide(byte item) {
		switch (item) {
			case Map.FOOD:
			this.eat();
			break;
		}
	}
	
	public void Die() {
		for (int i = 0; i < x.length; i++) {
			if (this.x[i] != -1)
				Map.Remove(this.x[i], this.y[i]);
		}
		
		this.lives--;
		if (this.lives == 0)
			this.dead = true;
		else
			this.init();
	}
	
	public void render(Renderer renderer, int frameCount){
		if (this.dead)
			return;
		for(int i = 0; i < this.length; i++){
			// if (i == this.ptr)
				// renderer.drawElement(this.x[i], this.y[i], frameCount, (byte)(40 + this.idx));
			// else if (this.x[i] != -1)
			if (this.x[i] != -1)
				renderer.drawElement(this.x[i] + SnakeMain.offsetX, this.y[i] + SnakeMain.offsetY, frameCount, Map.SNAKE);
		}
	}
}
