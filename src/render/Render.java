package render;

import blobs.Blob;
import cells.Cell;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import worlds.World;

public class Render extends JPanel {
    public static double[] cameraPosition = {512, 512};
    public static double[] cameraSize = {1200, 1200};
    private static JFrame frame;
    final private World world;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private double moveSpeedMultiplier = 0.00001;
    private LocalTime currentTime = LocalTime.now();
    private double deltaTime;
    private BufferedImage[] cellTextures = new BufferedImage[5];

    public Render(World world) {
        this.world = world;
        frame = new JFrame("Simulation");
        
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);
        
        initFrame();
        
        // Add key listener for camera movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });
        setFocusable(true);
        requestFocusInWindow(); // Request focus for key events

        // load textures
        // cells
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("resources/textures/cells_debug.png"));
            for (int i = 0; i < 5; i++) {
                cellTextures[i] = spriteSheet.getSubimage(0, 0,  32, 32);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
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
        
        // background
        setBackground(new Color(0, 60, 60));
        
        // chunks and their blobs
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
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(
                        (int)((chunkX - minX) * getWidth()/cameraSize[0]),
                        (int)((chunkY - minY) * getHeight()/cameraSize[1]),
                        (int)(world.chunkSize[0] * getWidth()/cameraSize[0]),
                        (int)(world.chunkSize[1] * getHeight()/cameraSize[1])
                    );

                    // draw blobs in this chunk
                    if (!world.chunks[i][j].blobs.isEmpty()) {
                        for (Blob blob : world.chunks[i][j].blobs) {
                            // draw cells
                            for (Cell cell : blob.cells) {
                                double cellGlobalPosition[] = {blob.globalPosition[0] + cell.relativePosition[0], blob.globalPosition[1] + cell.relativePosition[1], blob.globalPosition[2] + cell.relativePosition[2]};
                                double[] screenPosition = {(cellGlobalPosition[0] - (cameraPosition[0] - cameraSize[0]/2))/cameraSize[0] * getWidth(), (cellGlobalPosition[1] - (cameraPosition[1] - cameraSize[1]/2))/cameraSize[1] * getHeight(), cellGlobalPosition[2]};
                                
                                AffineTransform tx = AffineTransform.getRotateInstance(0, screenPosition[0], screenPosition[1]);
                                double scale = 1.0/32.0 * getWidth() / cameraSize[0];
                                AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);
                                tx.concatenate(scaleTransform);
                                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                                // Draw the rotated cell texture
                                BufferedImage cellTexture = cellTextures[cell.type];
                                g2d.drawImage(op.filter(cellTexture, null), (int)screenPosition[0], (int)screenPosition[1], null);
                            }
                        }
                    }
                }
            }
        }

        // text: camera position, size
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Camera position: " + String.format("%.2f", cameraPosition[0]) + ", " + String.format("%.2f", cameraPosition[1]), 4, 24);
        g2d.drawString("Camera size: " + String.format("%.2f", cameraSize[0]) + ", " + String.format("%.2f", cameraSize[1]), 4, 44);
        g2d.drawString("Render FPS: " + String.format("%.2f", 1/deltaTime) + " | Delta time: " + String.format("%.8f", deltaTime), 4, 64);
    }
    
    public void update() {
        deltaTime = (LocalTime.now().toNanoOfDay() - currentTime.toNanoOfDay()) / 1E9;
        currentTime = LocalTime.now();

        // Check which keys are held down and update camera position
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            cameraPosition[1] -= deltaTime * moveSpeedMultiplier * cameraSize[1];
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            cameraPosition[1] += deltaTime * moveSpeedMultiplier * cameraSize[1];
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            cameraPosition[0] -= deltaTime * moveSpeedMultiplier * cameraSize[0];
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            cameraPosition[0] += deltaTime * moveSpeedMultiplier * cameraSize[0];
        }
        
        if (pressedKeys.contains(KeyEvent.VK_E)) {
            cameraSize[0] *= 1 - deltaTime * moveSpeedMultiplier;
            cameraSize[1] *= 1 - deltaTime * moveSpeedMultiplier;
        }
        if (pressedKeys.contains(KeyEvent.VK_Q)) {
            cameraSize[0] *= 1 + deltaTime * moveSpeedMultiplier;
            cameraSize[1] *= 1 + deltaTime * moveSpeedMultiplier;
        }

        if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
            moveSpeedMultiplier = 2E0;
        } else moveSpeedMultiplier = 0.5E0;
    }
}
