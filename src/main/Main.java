package main;

import render.Render;
import worlds.World;

public class Main {
    public static void main(String[] args) {
        // create world
        World world = new World();

        // create render
        Render render = new Render(world);

        // start simulation loop
        while (true) {
            world.update();

            render.refresh();
            // TODO: sleep for deltaTime
        }
    }
}