package render;

import processing.core.PApplet;

import java.util.HashSet;

import worlds.World;
//import blobs.Blob;
//import cells.Cell;

public class Render extends PApplet {
	public double[] size = { 800, 800 };
	public int frameRate = 50;

	public double[] cameraPosition = { 512, 512 };
	public double[] cameraSize = { 1200, 1200 };
	public double moveSpeedMultiplier = 1E-48;
	public double deltaTime, prev;

	public World world;

	private HashSet<Character> keys = new HashSet<Character>();

	public void settings() {
		size((int) size[0], (int) size[1]);
	}

	public void setup() {
		frameRate(frameRate);
	}

	public void draw() {
		// call update
		update();

		// background
		background(0, 60, 60);

		// Calculate camera bounds
		double maxX = cameraPosition[0] + cameraSize[0] / 2;
		double minX = cameraPosition[0] - cameraSize[0] / 2;
		double minY = cameraPosition[1] - cameraSize[1] / 2;
		double maxY = cameraPosition[1] + cameraSize[1] / 2;

		// chunks and their blobs
		for (int i = 0; i < world.numChunks[0]; i++) {
			for (int j = 0; j < world.numChunks[1]; j++) {
				// Get chunk position
				double chunkX = i * world.chunkSize[0];
				double chunkY = j * world.chunkSize[1];

				// skip if not in frame
				if (!(chunkX + world.chunkSize[0] >= minX && chunkX <= maxX && chunkY + world.chunkSize[1] >= minY
						&& chunkY <= maxY))
					continue; // skips this chunk

				// draw chunk border
				fill(0, 0, 0, 0);
				stroke(0, 0, 0);
				strokeWeight(2);
				rect((int) ((chunkX - minX) * size[0] / cameraSize[0]),
						(int) ((chunkY - minY) * size[1] / cameraSize[1]),
						(int) (world.chunkSize[0] * size[0] / cameraSize[0]),
						(int) (world.chunkSize[1] * size[1] / cameraSize[1]));

				// draw blobs inside the chunk
//                if (world.chunks[i][j].blobs.isEmpty()) continue; // no blobs, our job here is done
//                for (Blob blob : world.chunks[i][j].blobs) {
//                    for (Cell cell : blob.cells) {
//                    	// later
//                    }
//                }
			}
		}

		// text
		fill(255, 255, 255);
		textSize(20);
		text("Camera position: " + String.format("%.2f", cameraPosition[0]) + ", "
				+ String.format("%.2f", cameraPosition[1]), 4, 24);
		text("Camera size: " + String.format("%.2f", cameraSize[0]) + ", " + String.format("%.2f", cameraSize[1]), 4,
				44);
		text("Render FPS: " + String.format("%.2f", 1 / deltaTime) + " | Delta time: "
				+ String.format("%.8f", deltaTime), 4, 64);
	}

	public void update() {
		deltaTime = (-prev + (prev = frameRateLastNanos)) / 1E9; // in seconds
		
		if (keys.contains('w')) {
			cameraPosition[1] -= deltaTime * moveSpeedMultiplier * cameraSize[1];
		}
		if (keys.contains('s')) {
			cameraPosition[1] += deltaTime * moveSpeedMultiplier * cameraSize[1];
		}
		if (keys.contains('a')) {
			cameraPosition[0] -= deltaTime * moveSpeedMultiplier * cameraSize[0];
		}
		if (keys.contains('d')) {
			cameraPosition[0] += deltaTime * moveSpeedMultiplier * cameraSize[0];
		}

		if (keys.contains('e')) {
			cameraSize[0] *= 1 - deltaTime * moveSpeedMultiplier;
			cameraSize[1] *= 1 - deltaTime * moveSpeedMultiplier;
		}
		if (keys.contains('q')) {
			cameraSize[0] *= 1 + deltaTime * moveSpeedMultiplier;
			cameraSize[1] *= 1 + deltaTime * moveSpeedMultiplier;
		}

		if (keys.contains('c')) {
			moveSpeedMultiplier = 2E0;
		} else
			moveSpeedMultiplier = 0.5E0;
		
		if (keys.contains('x')) {
			keys.clear();
		}
	}

	public void keyPressed() {
		keys.add(key);
	}

	public void keyReleased() {
		keys.remove(key);
	}
}
