import javax.swing.*;
import java.awt.*;

public class MainClass {
    public static void main(String [] args) {
        SwingUtilities.invokeLater(MainClass::initUI);
    }

    private static void initUI() {
        JFrame mainFrame = new JFrame("Turtle Graphics");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        TurtleGraphics turtlePanel = new TurtleGraphics();
        mainFrame.add(turtlePanel, BorderLayout.CENTER);

        JTextField commandField = new JTextField(20);
        mainFrame.add(commandField, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.BLACK);

        JTextArea commandArea = new JTextArea(10, 25);
        commandArea.setEditable(false);
        commandArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(commandArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        turtlePanel.setCommandArea(commandArea);

        Mybuttons buttonHandler = new Mybuttons(turtlePanel);
        JPanel buttonPanel = buttonHandler.createButtonPanel();

        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(scrollPane);

        mainFrame.add(rightPanel, BorderLayout.EAST);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }


}
