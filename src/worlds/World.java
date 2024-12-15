package worlds;

import chunks.Chunk;

public class World {
    public double[] size; // height, width in meters
    public int[] numChunks; // rows, columns
    public double[] chunkSize;
    public double deltaTime;

    public Chunk[][] chunks;

    public World() { // preferred settings
        this.size = new double[] {1024, 1024}; // km x km world we'll see if it's too big
        this.numChunks = new int[] {64, 64}; // 1024/32 = 32x32 | chunk size is directly connected to the eye cells!!!
        this.chunkSize = new double[] {this.size[0] / this.numChunks[0], this.size[1] / this.numChunks[1]};
        this.deltaTime = 0.02; // if we had 50 frames per second this would look natural | make this value larger to speed up the simulation at a cost of accuracy

        this.chunks = new Chunk[this.numChunks[0]][this.numChunks[1]];

        createChunks();
    }

    private void createChunks() {
        for (int i = 0; i < this.numChunks[0]; i++) {
            for (int j = 0; j < this.numChunks[1]; j++) {
                this.chunks[i][j] = new Chunk(this, new double[] {i * this.chunkSize[0], j * this.chunkSize[1]}, 0, 0);
            }
        }
    }

    public void update() {
        for (int i = 0; i < this.numChunks[0]; i++) {
            for (int j = 0; j < this.numChunks[1]; j++) {
                this.chunks[i][j].update();
            }
        }
    }
}
