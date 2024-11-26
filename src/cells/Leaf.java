package cells;

import blobs.Blob;

public class Leaf extends Cell {
    public Leaf(Blob parentBlob, double[] relativePosition) {
        super(parentBlob, 2, 1, 0.6, 2, relativePosition); // parentBlob, mass, max health, drag coefficient, energy cost
    }
    
    @Override
    public void update(double deltaTime) {
        this.parentBlob.energy -= 0.25 * deltaTime;
        this.parentBlob.energy += this.parentBlob.parentChunk.solarPower * deltaTime;
    }
}
