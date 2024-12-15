package main;

import processing.core.PApplet;

import java.util.ArrayList;

import render.Render;
import worlds.World;
import blobs.Blob;
import cells.Fat;
import cells.Leaf;

public class Main {
//	public static void main(String[] args) {
//		String[] processingArgs = { "MySketch" };
//		Test mySketch = new Test();
//		PApplet.runSketch(processingArgs, mySketch);
//		mySketch.draw();
//	}

	public static void main(String[] args) {
		// create world
		World world = new World();

		// test blob rendering
		for (int i = 0; i < world.numChunks[0]; i++) {
			for (int j = 0; j < world.numChunks[1]; j++) {
				Blob blob = new Blob(world.chunks[i][j], new ArrayList<>(), new double[0][0], new double[0], new double[]{i * world.chunkSize[0]+8, j * world.chunkSize[1], Math.random()*Math.PI*2});
				// add a fat cell
				blob.cells.add(new Fat(blob, new double[]{0, 0, 0}));
				blob.cells.add(new Fat(blob, new double[]{1, 1, Math.random()*Math.PI*2}));
				blob.calculateStats();

				world.chunks[i][j].blobs.add(blob);
			}
		}

		// create render
		String[] processingArgs = { "Blobs" };
		Render display = new Render();
		display.world = world; // important
		
		// start drawing
		PApplet.runSketch(processingArgs, display);

		// start simulation loop
		while (true) {
			world.update();
		}
	}
}