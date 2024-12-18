package cells;

import blobs.Blob;

public class Fat extends Cell {
    public Fat(Blob parentBlob, double[] relativePosition) {
        super(parentBlob, 1, 4, 0.9, 2, relativePosition); // parentBlob, mass, max health, drag coefficient, energy cost
        this.type = 0;
        this.dragCoefficient = 0.2;
    }
    
    @Override
    public void update(double deltaTime) {
        this.parentBlob.energy -= 0.2 * deltaTime;
    }
}
