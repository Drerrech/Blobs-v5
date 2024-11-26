package worlds;

import chunks.Chunk;

public class World {
    public double[] size; // height, width in meters
    public int[] numChunks; // rows, columns
    public double[] chunkSize;
    public double deltaTime;

    public Chunk[][] chunks;

    World(double[] size, int[] numChunks, double deltaTime) {
        this.size = size.clone();
        this.numChunks = numChunks.clone();
        this.deltaTime = deltaTime;

        this.chunks = new Chunk[this.numChunks[0]][this.numChunks[1]]; // move to a method createChunks() or smth

        //TODO: create chunks (as method)
    }

    World() { // preferred settings
        this.size = new double[] {1024, 1024}; // km x km world we'll see if it's too big
        this.numChunks = new int[] {32, 32}; // 1024/32 = 32x32 | chunk size is directly connected to the eye cells!!!
        this.chunkSize = new double[] {this.size[0] / this.numChunks[0], this.size[1] / this.numChunks[1]};
        this.deltaTime = 0.02; // if we had 50 frames per second this would look natural | make this value larger to speed up the simulation at a cost of accuracy

        this.chunks = new Chunk[this.numChunks[0]][this.numChunks[1]];

        //TODO: create chunks (as method)
    }
}
