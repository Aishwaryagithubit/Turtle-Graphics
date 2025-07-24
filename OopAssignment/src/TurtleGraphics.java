// Import necessary classes and libraries
import uk.ac.leedsbeckett.oop.LBUGraphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.JOptionPane;


//TurtleGraphics class extending the LBUGraphics
public class TurtleGraphics extends LBUGraphics {
    private final List<String> commandHistory = new ArrayList<>();
    private boolean showGreeting = false;
    public boolean saved;
    public JTextArea commandArea;

    // Constructor
    public TurtleGraphics() {
        super();
    }


    public void setCommandArea(JTextArea commandArea) {
        this.commandArea = commandArea;
    }

    public List<String> getCommandHistory() {
        return commandHistory;
    }

    // Main method to process user commands
    @Override
    public void processCommand(String command) {
        // Add the command to the history list
        commandHistory.add(command);

        // If a command area is linked, append the command to it
        if (commandArea != null) {
            commandArea.append(command + "\n");
        }

        // Normalize command: lowercase and trim whitespace
        command = command.toLowerCase().trim();
        String[] parts = command.split("\\s+");

        switch (parts[0]) {
            case "about":
                about();
                break;
            case "pendown":
                drawOn();
                break;
            case "penup":
                drawOff();
                break;
            case "move":
                if (parts.length > 1 && !parts[1].isEmpty()) {
                    try {
                        int distance = Integer.parseInt(parts[1]);
                        forward(distance);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number for move command.");
                    }
                } else {
                    forward(100);
                }
                break;

            case "reverse":
                if (parts.length > 1) {
                    int distance = Integer.parseInt(parts[1]);
                    reverse(distance);
                } else {
                    reverse(100);
                }
                break;
            case "clear":
                clear();
                break;
            case "reset":
                reset();
                break;
            case "left":
                if (parts.length > 1) {
                    int angle = Integer.parseInt(parts[1]);
                    left(angle);
                } else {
                    left(90);
                }
                break;
            case "right":
                if (parts.length > 1) {
                    int angle = Integer.parseInt(parts[1]);
                    right(angle);
                } else {
                    right(90);
                }
                break;
            case "square":
                if (parts.length > 1) {
                    int length = Integer.parseInt(parts[1]);
                    drawSquare(length);
                }
                break;
            case "triangle":
                if (parts.length == 2) {
                    int length = Integer.parseInt(parts[1]);
                    drawTriangle(length, length, length);
                } else if (parts.length == 4) {
                    int a = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2]);
                    int c = Integer.parseInt(parts[3]);
                    if (isValidTriangle(a, b, c)) {
                        drawTriangle(a, b, c);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid triangle sides.");
                    }
                }
                break;

            case "circle":
                if (parts.length > 1) {
                    int radius = Integer.parseInt(parts[1]);
                    drawCircle(radius);
                }
                break;
            case "rainbow":
                drawRainbow();
                break;
            case "greet":
                showGreeting = true;
                repaint();
                break;
            case "history":
                JOptionPane.showMessageDialog(null, String.join("\n", commandHistory));
                break;
            case "pen":
                if (parts.length == 4) {
                    try {
                        int r = Integer.parseInt(parts[1]);
                        int g = Integer.parseInt(parts[2]);
                        int b = Integer.parseInt(parts[3]);

                        if (isValidRGB(r, g, b)) {
                            setPenColour(new Color(r, g, b));
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid RGB values: must be 0–255.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid RGB values: must be numbers.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usage: pen <red> <green> <blue>");
                }
                break;


            case "penwidth":
                if (parts.length > 1) {
                    int width = Integer.parseInt(parts[1]);
                    setStroke(width);
                }
                break;
            case "red":
            case "green":
            case "blue":
            case "yellow":
            case "orange":
            case "pink":
            case "purple":
            case "white":
                setColor(parts[0]);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown command: " + command);
        }
    }

    private void reverse(int distance) {
        right(180);
        forward(distance);
        left(180);
    }

    // Sets pen color based on color name string
    private void setColor(String colorName) {
        switch (colorName) {
            case "red":
                setPenColour(Color.RED);
                break;
            case "green":
                setPenColour(Color.GREEN);
                break;
            case "blue":
                setPenColour(Color.BLUE);
                break;
            case "yellow":
                setPenColour(Color.YELLOW);
                break;
            case "orange":
                setPenColour(Color.ORANGE);
                break;
            case "pink":
                setPenColour(Color.PINK);
                break;
            case "purple":
                setPenColour(new Color(128, 0, 128));
                break;
            case "white":
                setPenColour(Color.WHITE);
                break;
        }
    }

    // Checks if RGB values are valid (0–255)
    private boolean isValidRGB(int r, int g, int b) {
        return (r >= 0 && r <= 255) && (g >= 0 && g <= 255) && (b >= 0 && b <= 255);
    }

    // Draws a square by moving and turning 4 times
    private void drawSquare(int length) {
        for (int i = 0; i < 4; i++) {
            forward(length);
            right(90);
        }
    }

    // Checks if the triangle sides are valid
    private boolean isValidTriangle(int a, int b, int c) {
        return a + b > c && a + c > b && b + c > a;
    }

    // Draws a triangle
    private void drawTriangle(int a, int b, int c) {
        if (a == b && b == c) {
            // Equilateral triangle: fixed 120° turns
            for (int i = 0; i < 3; i++) {
                forward(a);
                right(120);
            }
        } else {
            // General triangle using Law of Cosines

            // Calculate internal angles
            double angleA = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2.0 * b * c)));
            double angleB = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2.0 * a * c)));
            double angleC = Math.toDegrees(Math.acos((a * a + b * b - c * c) / (2.0 * a * b)));

// Draw side a
            forward(a);
            right((int)(180 - angleC));

// Draw side b
            forward(b);
            right((int)(180 - angleA));

// Draw side c
            forward(c);
            right((int)(180 - angleB));

        }
    }

    // Draws a circle by small forward steps and 1° turns
    public void drawCircle(int radius) {
        drawOn();
        circle(radius);
    }


    // Draws a rainbow effect
    private void drawRainbow() {
        Color[] rainbowColors = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA };
        int steps = 100;
        double rainbowAngle = 6;
        int rainbowLength = 10;

        drawOn();  // ensure pen is down

        Timer timer = new Timer(50, null);
        final int[] i = {0};

        timer.addActionListener(_ -> {
            if (i[0] >= steps) {
                timer.stop();
                PenColour = Color.BLACK;
            } else {
                PenColour = rainbowColors[i[0] % rainbowColors.length];
                forward(rainbowLength + i[0] / 6);
                right((int) rainbowAngle);
                i[0]++;
            }
        });

        timer.start();
    }

    // Overrides the paintComponent to draw greeting
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (showGreeting) {
            g2d.setColor(Color.green);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.drawString("WELCOME TO MY TURTLE", getWidth() / 2 - 180, getHeight() / 2);
        }
    }


    // Clears the canvas and command history
    public void clearCanvas() {
        commandHistory.clear();
        if (commandArea != null) {
            commandArea.setText("");
        }
        repaint();
    }

    // Updates the command area with all past commands
    public void updateCommandArea() {
        if (commandArea != null) {
            commandArea.setText("");  // clear first
            for (String cmd : commandHistory) {
                commandArea.append(cmd + "\n");
            }
        }
    }

    // Toggles the greeting display and repaints
    public void setShowGreeting(boolean show) {
        this.showGreeting = show;
        repaint();
    }

    // Prompts user for confirmation before clearing
    public void clearWithConfirmation() {
        int confirm = JOptionPane.showConfirmDialog(
                null, "Are you sure you want to clear the canvas?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clearCanvas();
        }
    }


    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    // Override about()
    @Override
    public void about() {
        super.about();  // keep the original animation/dance behavior

        JOptionPane.showMessageDialog(null, "This TurtleGraphics made by Aishwarya Sah");
    }

}

