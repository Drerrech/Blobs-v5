package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import worlds.World;

public class Render extends JPanel {
    public static double[] cameraPosition = {512, 512};
    public static double[] cameraSize = {1200, 1200};
    private static JFrame frame;
    final private World world;
    
    public Render(World world) {
        this.world = world;
        frame = new JFrame("Simulation");
        
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);
        
        initFrame();
    }
    
    private void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Calculate camera bounds
        double minX = cameraPosition[0] - cameraSize[0]/2; // global position
        double maxX = cameraPosition[0] + cameraSize[0]/2;
        double minY = cameraPosition[1] - cameraSize[1]/2;
        double maxY = cameraPosition[1] + cameraSize[1]/2;
        
        // Draw main viewport border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
        
        // Iterate through chunks
        for (int i = 0; i < world.numChunks[0]; i++) {
            for (int j = 0; j < world.numChunks[1]; j++) {
                // Get chunk position
                double chunkX = i * world.chunkSize[0];
                double chunkY = j * world.chunkSize[1];
                
                // Check if chunk is in view
                if (chunkX + world.chunkSize[0] >= minX && 
                    chunkX <= maxX &&
                    chunkY + world.chunkSize[1] >= minY && 
                    chunkY <= maxY) {
                    
                    // Draw chunk border
                    g2d.setColor(Color.GRAY);
                    g2d.drawRect(
                        (int)((chunkX - minX) * getWidth()/cameraSize[0]),
                        (int)((chunkY - minY) * getHeight()/cameraSize[1]),
                        (int)(world.chunkSize[0] * getWidth()/cameraSize[0]),
                        (int)(world.chunkSize[1] * getHeight()/cameraSize[1])
                    );
                    
                    // TODO: Add actual chunk rendering logic here
                }
            }
        }
    }
    
    public void refresh() {
        repaint();
    }
}
