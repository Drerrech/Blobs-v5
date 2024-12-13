package render;

import processing.core.PApplet;

public class Test extends PApplet{
	
	public void settings(){
		size(500, 500);
	}
	
	public void setup() {
		frameRate(4);
	}
	
	public void draw(){
//		double delta = (-prev + (prev = frameRateLastNanos))/1e6d;
		System.out.println(frameRateLastNanos);
		background(152,190,100);
		ellipse(mouseX, mouseY, 50, 50);
	}
	
	public void mousePressed(){
		background(64);
	}
}