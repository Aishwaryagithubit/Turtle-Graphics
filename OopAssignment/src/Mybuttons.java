import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.logging.Logger;

public class Mybuttons implements ActionListener {
    private static final Logger logger = Logger.getLogger(Mybuttons.class.getName());
    private final TurtleGraphics turtlePanel;

    public Mybuttons(TurtleGraphics turtlePanel) {
        this.turtlePanel = turtlePanel;
    }

    public JPanel createButtonPanel() {
        // Create a JPanel with centered flow layout
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Set the background color to black
        panel.setBackground(Color.BLACK);

        // Create buttons with labels, action commands, and listeners
        JButton greetButton = new JButton("Greet");
        greetButton.setActionCommand("Greet");
        greetButton.addActionListener(this);

        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(this);

        JButton loadButton = new JButton("Load");
        loadButton.setActionCommand("Load");
        loadButton.addActionListener(this);

        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand("Clear");
        clearButton.addActionListener(this);

        JButton helpButton = new JButton("Help");
        helpButton.setActionCommand("Help");
        helpButton.addActionListener(this);

        panel.add(greetButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(clearButton);
        panel.add(helpButton);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "Save" :
                saveDrawing();
                break;
            case "Load" :
                loadDrawing();
                break;
            case "Clear" :
                turtlePanel.clearWithConfirmation();
                break;
            case "Help" :
                showHelpDialog();
                break;
            case "Greet" :
                turtlePanel.setShowGreeting(true);
                break;
        }
    }

    public void saveDrawing() {
        BufferedImage image = new BufferedImage(
                turtlePanel.getWidth(),
                turtlePanel.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = image.createGraphics();
        turtlePanel.paintAll(g2d);
        g2d.dispose();

        try {
            File outputFile = new File("drawing.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Drawing saved as drawing.png");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(outputFile);
            }

            saveCommandsToFile();
            turtlePanel.setSaved(true);

            JOptionPane.showMessageDialog(null, "Drawing saved successfully!", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            logger.severe("Error saving drawing: " + ex.getMessage());
        }
    }

    private void saveCommandsToFile() {
        try {
            File commandFile = new File("commands.txt");
            PrintWriter writer = new PrintWriter(new FileWriter(commandFile));
            for (String command : turtlePanel.getCommandHistory()) {
                writer.println(command);
            }
            writer.close();
            System.out.println("Commands saved to commands.txt");
        } catch (IOException ex) {
            logger.severe("Error saving commands to file: " + ex.getMessage());
        }
    }


    public void loadDrawing() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Command File to Load");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            turtlePanel.clearWithConfirmation();

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    turtlePanel.processCommand(line);
                }
                System.out.println("Commands loaded and executed from: " + selectedFile.getName());
                turtlePanel.setSaved(false);

                // ** update command area **
                turtlePanel.updateCommandArea();

                JOptionPane.showMessageDialog(null, "Commands successfully loaded from " + selectedFile.getName());
            } catch (IOException ex) {
                logger.severe("Error loading commands: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error loading the command file.");
            }
        } else {
            System.out.println("Load operation cancelled.");
        }
    }


    private void showHelpDialog() {
        String helpMessage = """
            ******* Turtle Graphics Commands ******

            move       - Move forward
            reverse    - Move backward
            left       - Turn left by X degrees
            right      - Turn right by X degrees
            penup      - Lift the pen (no drawing)
            pendown    - Put the pen down (draw)
            clear      - Clear the canvas (with confirmation)
            reset      - Reset turtle to center
            red/green/blue/black/yellow - Change pen color
            penwidth   - Change pen size
            square     - Draw a square
            triangle   - Draw a triangle
            circle     - Draw a circle
            rainbow    - Draw multicolour spiral

            Example:
            pendown
            move 100
            right 90
            move 50
            """;

        JOptionPane.showMessageDialog(null, helpMessage, "Help - Command Reference", JOptionPane.INFORMATION_MESSAGE);
    }
}


