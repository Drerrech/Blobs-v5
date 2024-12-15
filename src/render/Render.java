package render;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashSet;

import worlds.World;
import chunks.Chunk;
import blobs.Blob;
import cells.Cell;

public class Render extends PApplet {
	public double[] size = { 800, 800 };
	public int frameRate = 50;

	public double[] cameraPosition = { 512, 512 };
	public double[] cameraSize = { 1200, 1200 };
	public double moveSpeedMultiplier = 1E-48;
	public double deltaTime, prev;

	public World world;

	private HashSet<Character> keys = new HashSet<Character>();

	PImage[] cellTextures = new PImage[5];
	int cellPixelSize = 32;

	public void settings() {
		size((int) size[0], (int) size[1]);
	}

	public void setup() {
		frameRate(frameRate);
		
		String pth = "resources/textures/cells_debug.png";
		PImage textures = loadImage(pth);
		for (int i = 0; i < 5; i++) {
			cellTextures[i] = textures.get(i*32, 0, 32, 32);
		}
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
                if (world.chunks[i][j].blobs.isEmpty()) continue; // no blobs, our job here is done
                
                for (Blob blob : world.chunks[i][j].blobs) {
                	pushMatrix(); // drawing as a group
        			translate((int)((blob.globalPosition[0] - minX) * size[0] / cameraSize[0]), (int)((blob.globalPosition[1] - minY) * size[1] / cameraSize[1]));
        			rotate(-(float)blob.globalPosition[2]);
        			
        			scale((float)(1 * size[0] / (cellPixelSize*cameraSize[0]))); // cells are 1 unit (and 32 pixels) and camera is square
        			
        			imageMode(CENTER); // no need to offset by -0.5 now
        			for (Cell cell : blob.cells) { // draw as if blob has not been displaced
        				pushMatrix();
        				translate((int)(cell.relativePosition[0] * cellPixelSize), (int)(cell.relativePosition[1] * cellPixelSize));
        				rotate(-(float)cell.relativePosition[2]); // it's clockwise, also for some reason no need to revert the rotation
        				
        				image(cellTextures[cell.type], 0, 0);
        				popMatrix();
        			}
        			
        			popMatrix();
        		}
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
