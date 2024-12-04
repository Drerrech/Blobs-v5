package cells;

import blobs.Blob;

public class Cell {
    public Blob parentBlob;
    public String CommunicationType = "none"; // none, input, output
    public int id;
    public int type;
    public double mass; // kg
    public double maxHealth; // idk
    public double health; // idk
    public double dragCoefficient; // idk lol just [0, 1]
    public double energyCost;
    public double[] relativePosition = new double[3]; // orientation included (radians, counterclockwise)
    
    public double distanceToBlobCenter;
    
    Cell(Blob parentBlob, double mass, double maxHealth, double dragCoefficient, double energyCost, double[] relativePosition) {
        this.parentBlob = parentBlob;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.dragCoefficient = dragCoefficient;
        this.energyCost = energyCost;
        this.relativePosition = relativePosition.clone();
    }

    public double getMass() {
        return this.mass;
    }

     public void update(double deltaTime) {}
}
