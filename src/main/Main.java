package main;

import blobs.Blob;
import cells.Fat;
import java.util.ArrayList;
import render.Render;
import worlds.World;

public class Main {
    public static void main(String[] args) {
        // create world
        World world = new World();

        // test blob rendering
        for (int i = 0; i < world.numChunks[0]; i++) {
            for (int j = 0; j < world.numChunks[1]; j++) {
                Blob blob = new Blob(world.chunks[i][j], new ArrayList<>(), new double[0][0], new double[0], new double[]{i * world.chunkSize[0], j * world.chunkSize[1], 0.2});
                // add a fat cell
                blob.cells.add(new Fat(blob, new double[]{0, 0, 0}));
                blob.calculateStats();

                world.chunks[i][j].blobs.add(blob);
            }
        }

        // create render
        Render render = new Render(world);

        // start simulation loop
        while (true) {
            world.update();

            render.refresh();
            // TODO: sleep for deltaTime
        }
    }
}