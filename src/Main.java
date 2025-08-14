import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    private static Robot robot;
    private static boolean shiftPressed = false;

    public static void main(String[] args) throws Exception {
        robot = new Robot();

        JFrame frame = new JFrame("Pls work Mk5");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 400);
        frame.setAlwaysOnTop(true);
        frame.setFocusableWindowState(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        frame.add(mainPanel);

        String[][] keys = {
                {"1","2","3","4","5","6","7","8","9","0","-","="},
                {"Q","W","E","R","T","Y","U","I","O","P","Backspace"},
                {"A","S","D","F","G","H","J","K","L","Enter","/"},
                {"Shift","Z","X","C","V","B","N","M",",",".","X3"},
                {"Space"}
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3,3,3,3);

        for(int row=0; row<keys.length; row++){
            String[] rowKeys = keys[row];
            for(int col=0; col<rowKeys.length; col++){
                String key = rowKeys[col];
                JButton button = new JButton();
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBackground(Color.LIGHT_GRAY);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));

                // Set image icon with scaling
                setButtonIcon(button, key);

                // Hover effect
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(Color.GRAY); }
                    public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(Color.LIGHT_GRAY); }
                });

                button.addActionListener(e -> handleKey(key));

                gbc.gridx = col;
                gbc.gridy = row;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                if(key.equals("Space")) gbc.gridwidth = rowKeys.length; else gbc.gridwidth=1;

                mainPanel.add(button, gbc);
                if(key.equals("Space")) gbc.gridwidth=1;
            }
        }

        frame.setVisible(true);
    }

    private static void setButtonIcon(JButton button, String key) {
        java.net.URL imgURL = Main.class.getResource("/images/" + key + ".png");
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);

            // Set icon initially (no scaling yet)
            button.setIcon(originalIcon);

            // Update icon whenever button is resized
            button.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int w = button.getWidth();
                    int h = button.getHeight();
                    if (w <= 0 || h <= 0) return; // skip if not yet laid out
                    Image scaled = originalIcon.getImage().getScaledInstance(
                            w, h, Image.SCALE_SMOOTH
                    );
                    button.setIcon(new ImageIcon(scaled));
                }
            });
        } else {
            button.setText(key); // fallback text
            button.setFont(new Font("Arial", Font.BOLD, 16));
        }
    }

    private static void handleKey(String key){
        try{
            switch(key){
                case "Backspace":
                    robot.keyPress(KeyEvent.VK_BACK_SPACE);
                    robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                    break;
                case "Enter":
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
                case "Space":
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                    break;
                case "Shift":
                    shiftPressed = true; // just mark shift as pressed
                    return; // don’t type anything
                case "X3":
                    shiftPressed = true;
                    typeCharacterWithShift("X");
                    typeCharacterWithShift("3");
                    break;
                default:
                    typeCharacterWithShift(key);
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    private static void typeCharacterWithShift(String character) {
        for(char c : character.toCharArray()){
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if(keyCode == KeyEvent.CHAR_UNDEFINED) continue;

            // Press shift if needed
            if(shiftPressed) robot.keyPress(KeyEvent.VK_SHIFT);

            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);

            if(shiftPressed) robot.keyRelease(KeyEvent.VK_SHIFT);
        }
        // Reset shift if it’s a one-time press (like normal keyboards)
        shiftPressed = false;
    }
/// No longer needed it no work :/
/*
    private static void typeCharacter(String character){
        for(char c : character.toCharArray()){
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if(keyCode == KeyEvent.CHAR_UNDEFINED) continue;
            if(shiftPressed){
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(keyCode);
            }
            else {
                robot.keyPress(keyCode);
            }
            robot.keyRelease(keyCode);
        }
    }

 */


}
