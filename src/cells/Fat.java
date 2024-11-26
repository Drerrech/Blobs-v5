package cells;

import blobs.Blob;

public class Fat extends Cell {
    public Fat(Blob parentBlob, double[] relativePosition) {
        super(parentBlob, 1, 4, 0.9, 2, relativePosition); // parentBlob, mass, max health, drag coefficient, energy cost
    }
    
    @Override
    public void update(double deltaTime) {
        this.parentBlob.energy -= 0.2 * deltaTime;
    }
}
