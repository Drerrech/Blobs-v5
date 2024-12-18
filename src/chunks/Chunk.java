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
        for (Blob blob : blobs) {
        	//Temprorary velocity update
//        	blob.velocity[1] = -32 * parentWorld.deltaTime;
        	blob.netForce[0] += -0.001 * (this.globalPosition[0]-16*32) * parentWorld.deltaTime;
        	blob.netForce[1] += -0.001 * (this.globalPosition[1]-16*32) * parentWorld.deltaTime;
        	
            blob.update(parentWorld.deltaTime);
        }
        // TODO: figure out how to first update the stats and only then the position (and other stuff) of blob, similar to a cellular automaton
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
                index[0] = (int)(blob.globalPosition[0] / parentWorld.chunkSize[0] + parentWorld.chunkSize[0]) % parentWorld.numChunks[0];
                index[1] = (int)(blob.globalPosition[1] / parentWorld.chunkSize[1] + parentWorld.chunkSize[1]) % parentWorld.numChunks[1];
                
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
