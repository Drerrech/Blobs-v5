package chunks;

import worlds.World;
import blobs.Blob;
import java.util.List;
import java.util.Iterator;

public class Chunk {
    public World parentWorld;
    public List<Blob> blobs;
    public double[] globalPosition;
    public double passivePower; // power per second -> will be multiplied by deltaTime
    public double solarPower; // same deal as above
    

    Chunk(World parentWorld, double[] globalPosition, double passivePower, double solarPower) {
        this.parentWorld = parentWorld;
        this.globalPosition = globalPosition.clone();
        this.passivePower = passivePower;
        this.solarPower = solarPower;
    }

    void update() {
        // update blobs
        for (Blob blob : blobs) {
            blob.update(parentWorld.deltaTime);
        }

        // check if any blobs are dead -> remove
        Iterator<Blob> iterator = blobs.iterator();
        while (iterator.hasNext()) {
            Blob blob = iterator.next();
            if (blob.isDead()) {
                iterator.remove();
            }
        }

        // check if any blobs are outside of the chunk -> transfer
        iterator = blobs.iterator();
        while (iterator.hasNext()) {
            Blob blob = iterator.next();
            if (blob.globalPosition[0] < this.globalPosition[0] || blob.globalPosition[0] > this.globalPosition[0] + this.size[0] || blob.globalPosition[1] < this.globalPosition[1] - this.size[1] || blob.globalPosition[1] > this.globalPosition[1]) {
                // transfer to the appropriate chunk
                int[] index = new int[2];
                index[0] = (int) (blob.globalPosition[0] / parentWorld.chunkSize[0]);
                index[1] = (int) (blob.globalPosition[1] / parentWorld.chunkSize[1]);

                // add to new chunk and update blob's parentChunk and position if it's looped
                parentWorld.chunks[index[0] % parentWorld.numChunks[0]][index[1] % parentWorld.numChunks[1]].blobs.add(blob); // don't forget to loop
                blob.parentChunk = parentWorld.chunks[index[0] % parentWorld.numChunks[0]][index[1] % parentWorld.numChunks[1]];
                blob.globalPosition[0] = blob.globalPosition[0] % parentWorld.size[0];
                blob.globalPosition[1] = blob.globalPosition[1] % parentWorld.size[1];

                // remove from this chunk
                iterator.remove();
            }
        }
    }
}
