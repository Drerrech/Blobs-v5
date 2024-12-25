package chunks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import blobs.Blob;
import worlds.World;

public class Chunk {
    public World parentWorld;
    public List<Blob> blobs = new ArrayList<>();
    public double[] globalPosition;
    public double passivePower; // power per second -> will be multiplied by deltaTime
    public double solarPower; // same deal as above
    

    public Chunk(World parentWorld, double[] globalPosition, double passivePower, double solarPower) {
        this.parentWorld = parentWorld;
        this.globalPosition = globalPosition.clone();
        this.passivePower = passivePower;
        this.solarPower = solarPower;
    }
    
    public void updateBlobs() {
    	// update blobs
        for (Blob blob : blobs) { // REMOVE LATER
        	//Temprorary velocity update
        	blob.netForce[0] += -0.1 * (this.globalPosition[0]-16*32) * parentWorld.deltaTime;
        	blob.netForce[1] += -0.1 * (this.globalPosition[1]-16*32) * parentWorld.deltaTime;
        }

        // first update everything except position
        for (Blob blob : blobs) {
            blob.updateGeneral(parentWorld.deltaTime);
        }

        // then update position
        for (Blob blob : blobs) {
            blob.updatePosition(parentWorld.deltaTime);
        }
    }
    
    public void cleanUpBlobs() {
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
            if (blob.globalPosition[0] < this.globalPosition[0] || blob.globalPosition[0] >= this.globalPosition[0] + this.parentWorld.chunkSize[0] || blob.globalPosition[1] < this.globalPosition[1] || blob.globalPosition[1] >= this.globalPosition[1] + this.parentWorld.chunkSize[1]) {
            	// transfer to the appropriate chunk
                int[] index = new int[2];
                blob.globalPosition[0] %= parentWorld.size[0];
                blob.globalPosition[1] %= parentWorld.size[1];
                index[0] = (int)(blob.globalPosition[0] / parentWorld.chunkSize[0]);
                index[1] = (int)(blob.globalPosition[1] / parentWorld.chunkSize[1]);
                
                // add to new chunk and update blob's parentChunk and position if it's looped
                parentWorld.chunks[index[0]][index[1]].blobs.add(blob); // don't forget to loop
                blob.parentChunk = parentWorld.chunks[index[0]][index[1]];
                blob.globalPosition[0] = (blob.globalPosition[0] + parentWorld.size[0]) % parentWorld.size[0];
                blob.globalPosition[1] = (blob.globalPosition[1] + parentWorld.size[1]) % parentWorld.size[1];
                
                // remove from this chunk
                iterator.remove();
            }
        }
    }
}
