package blobs;

// TODO: add other cells
import cells.Cell;
import chunks.Chunk; // import cells.Leaf
import java.util.ArrayList;
import java.util.List;

public final class Blob {
    public Chunk parentChunk;
    public List<Cell> cells = new ArrayList<>();
    public double[][] brainMatrix;
    public double[] brainBias;

    public double mass;
    public double momentOfInertia;
    public double maxEnergy;
    public double energy;
    public double dragCoefficient;
    public double[] netForce = new double[3]; // rotation included
    public double[] velocity = new double[3]; // rotation included
    public double[] globalPosition = new double[3]; // rotation included

    // public Blob(genome of sorts) TODO: add genome + parentChunk at initialization | drug coef is the multiplication of all coefs of cells
    public Blob(Chunk parentChunk, List<Cell> cells, double[][] brainMatrix, double[] brainBias, double[] globalPosition) {
        this.parentChunk = parentChunk;
        this.cells = cells;
        this.brainMatrix = brainMatrix;
        this.brainBias = brainBias;
        this.globalPosition = globalPosition;
        calculateStats();
        // cells ids and parentBlob are given by parent
    }

    // void cellsFromJson(genome) implemet later, and add species tracking

    public final void calculateStats() {
        // for now max energy is just 100J per cell
        this.maxEnergy = this.cells.size() * 100;

        // calculate shift in center of mass (center of mass is 0 0)
        // and calculate mass
        this.mass = 0;
        double[] centerOfMass = new double[2];
        for (Cell cell : this.cells) {
            this.mass += cell.getMass();

            centerOfMass[0] += cell.getMass() * cell.relativePosition[0];
            centerOfMass[1] += cell.getMass() * cell.relativePosition[1];
        }
        centerOfMass[0] /= this.mass;
        centerOfMass[1] /= this.mass;

        // update cell relative positions and distances of cells to center of mass
        for (Cell cell : this.cells) {
            cell.relativePosition[0] -= centerOfMass[0];
            cell.relativePosition[1] -= centerOfMass[1];

            cell.distanceToBlobCenter = Math.sqrt(Math.pow(cell.relativePosition[0], 2) + Math.pow(cell.relativePosition[1], 2));
        }

        // calculate moment of inertia
        this.momentOfInertia = 0;
        for (Cell cell : this.cells) {
            this.momentOfInertia += cell.getMass() * Math.pow(cell.distanceToBlobCenter, 2);
        }
    }

    public void update(double deltaTime) {
        // update energy income
        this.energy += this.parentChunk.passivePower * deltaTime; // passive

        // reset netForce
        this.netForce = new double[3];

        // update cells
        for (Cell cell : this.cells) {
            cell.update(deltaTime);
        }

        // update velocity and position
        for(int i = 0; i < 3; i++) this.velocity[i] += this.netForce[i] / this.mass; // deltaTime was applied in cell/other updates
        // for(int i = 0; i < 3; i++) this.velocity[i] *= this.parentChunk.wind or smth

        for(int i = 0; i < 3; i++) this.velocity[i] *= this.dragCoefficient;

        for(int i = 0; i < 3; i++) this.globalPosition[i] += this.velocity[i];

    }

    public boolean isDead() {
       return false; // TODO: REMOVE
       
    //    if (this.energy <= 0 || this.cells.isEmpty()) {
    //         return true;
    //     }
    //     return false;
    }
}
