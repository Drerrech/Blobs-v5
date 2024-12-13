package render;

import processing.core.PApplet;

public class Test extends PApplet{
	public static void main(String[] args){
		String[] processingArgs = {"Test"};
		Test mySketch = new Test();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
