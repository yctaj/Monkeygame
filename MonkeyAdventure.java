import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.Timer;


public class MonkeyAdventure {
    public static JFrame mazeFrame = new JFrame("Maze");
    public static JFrame frame = new JFrame("Monkey Island: The Forbidden Temple");
    public static Set<Character> completedTrials = new HashSet<>();
    public static int bananaCount = 0;
    public static int moveCount = 0;
    public static int secondsPlayed = 0;

    public static void main(String[] args) {
        showMainMenu();
    }

    public static void showMainMenu() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon("start.png").getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton startGame = new JButton("Start Adventure");
        startGame.addActionListener(e -> {
            bananaCount = 0;
            moveCount = 0;
            secondsPlayed = 0;
            completedTrials.clear();
            showMaze(false);
        });

        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> System.exit(0));

        buttonPanel.add(startGame);
        buttonPanel.add(quit);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    public static void showMaze(boolean finalMaze) {
        mazeFrame.dispose();
        mazeFrame = new JFrame("Maze");
        mazeFrame.setBackground(Color.black);
        mazeFrame.setSize(1180, 895);
        mazeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        MazePanel mazePanel = new MazePanel(finalMaze);
        mazeFrame.add(mazePanel);
        mazeFrame.setVisible(true);
        mazePanel.requestFocusInWindow();
    }

    static class MazePanel extends JPanel implements KeyListener, ActionListener {
        private final int cellSize = 65;
        private char[][] maze;
        private int playerX = 0, playerY = 0;
        private int monsterX = 1, monsterY = 1;
        private final boolean isFinal;
        private Timer gameTimer;

        public MazePanel(boolean isFinalMaze) {
            this.isFinal = isFinalMaze;
            setFocusable(true);
            addKeyListener(this);
            maze = createMaze(isFinal);
            gameTimer = new javax.swing.Timer(1000, this);
            gameTimer.start();

            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    if (maze[y][x] == 'S') {
                        playerX = x;
                        playerY = y;
                    }
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            MonkeyAdventure.secondsPlayed++;
            repaint();
        }

        private char[][] createMaze(boolean finalMaze) {
            char[][] m = new char[][] {
                { '#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#' },
                { '#','1',' ','B',' ',' ',' ',' ','L',' ',' ',' ',' ',' ',' ',' ','B',' ','#',' ',' ',' ','B',' ',' ',' ',' ','4','#' },
                { '#',' ','#','#','L','#','#',' ','#',' ','#','#','#','#','#','#','#',' ','#',' ','#','#','#',' ','L',' ','#','#','#' },
                { '#',' ','#',' ',' ',' ','#',' ','#',' ','#',' ','B',' ',' ','#',' ',' ','L',' ',' ',' ',' ',' ','#',' ',' ','#','#' },
                { '#',' ','#',' ','#','B','L',' ','#',' ','#',' ','#',' ','#','#',' ','#','#','#',' ','#',' ','#','#',' ','#',' ','#' },
                { '#',' ','#',' ','#',' ','#',' ','L',' ',' ',' ','#',' ',' ',' ',' ',' ',' ','#',' ','#',' ',' ','#',' ',' ',' ','#' },
                { '#','B',' ',' ','#',' ','#',' ','#','L','#','#','#','#','#','#','#',' ','#',' ','#','#',' ','#','#','#','B','#','#' },
                { '#',' ','#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ',' ','B',' ',' ',' ',' ',' ',' ',' ','#' },
                { '#',' ','#','#','#','#','#','L','#','#','#','#','#','#','#',' ','#','#','#','#','#','#','#','#','#','#',' ','#','#' },
                { '#',' ',' ','B',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','O',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ','#' },
                { '#',' ','#',' ','#','#','#','#','#','#','#','#','#',' ','#','#','#','#','#','#','#','#','#','#',' ','#',' ','#','#' },
                { '#',' ','#',' ','#',' ',' ',' ',' ',' ','B',' ','#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ',' ',' ','#' },
                { '#',' ','#',' ','#',' ','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#',' ','L',' ',' ','L','#',' ','#' },
                { '#',' ','#',' ','#',' ','#',' ',' ',' ','#',' ',' ','B',' ',' ','#',' ',' ',' ',' ',' ',' ','#',' ',' ',' ','#','#' },
                { '#',' ','#',' ','#',' ','#',' ','#',' ','L',' ','B',' ','B',' ','#',' ','#',' ','#',' ','L','#','#',' ',' ','B','#' },
                { '#',' ','#',' ','#',' ','#',' ','#',' ',' ',' ',' ','B',' ',' ','#',' ','#',' ','#',' ',' ',' ','#',' ','#',' ','#' },
                { '#',' ','#',' ','#',' ',' ',' ','#','#','#','#','#',' ','#','#','#',' ','#','L','#','#','#',' ','#',' ','L',' ','#' },
                { '#',' ','#',' ','#',' ','#','B',' ',' ',' ',' ','O',' ','L',' ',' ',' ',' ',' ',' ',' ','L',' ','#','B','#',' ','#' },
                { '#',' ','#',' ','#',' ','#','#','#','#','#','#','#',' ','#','#','#','#','#','L','#','#','#',' ','#',' ','L','3','#' },
                { '#','2',' ','B',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','L','#' },
                { '#','#','#','#','#','#','#','#','#','#','#','#','#','S','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#' }
            };
            if (finalMaze) m[0][14] = 'W';
            return m;
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
             for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    char tile = maze[y][x];
                    //if (x <= playerX+1&& y <= playerY +1 && x>= playerX-1 && y>= playerY-1){
                    if (tile == '#') 
                        g.drawImage(new ImageIcon("wall.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                        

                    else if (tile == 'L') g.drawImage(new ImageIcon("lava.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                    else if (tile == 'B') g.drawImage(new ImageIcon("banana.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                    else if (tile == 'O') g.drawImage(new ImageIcon("hole.png").getImage(),x *cellSize,y *cellSize,cellSize,cellSize,this);
                    else if (tile == 'W') g.drawImage(new ImageIcon("door.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                    else if (tile == '1' || tile == '2' || tile == '3' || tile == '4') {
                        if (!completedTrials.contains(tile))
                            g.drawImage(new ImageIcon("door.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                        else
                            g.drawImage(new ImageIcon("floor.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                    }
                    else g.drawImage(new ImageIcon("floor.png").getImage(), x * cellSize, y * cellSize, cellSize, cellSize, this);
                    }
                    //else { 
                    //        g.drawImage(new ImageIcon ("black.png").getImage(), x*cellSize, y*cellSize,cellSize,cellSize, this);
                    //}
                //}
            }
            g.drawImage(new ImageIcon("monkey.png").getImage(), playerX * cellSize + 5, playerY * cellSize + 5, 30, 30, this);
            g.drawImage(new ImageIcon("monster.png").getImage(), monsterX * cellSize + 5, monsterY * cellSize + 5, 30, 30, this);
            g.setColor(Color.YELLOW);
            g.drawString("Bananas: " + MonkeyAdventure.bananaCount, 10, 20);
            g.drawString("Moves: " + MonkeyAdventure.moveCount, 10, 35);
            g.drawString("Time: " + MonkeyAdventure.secondsPlayed + "s", 10, 50);
        }

        private void moveMonster() {
            if (playerX > monsterX && maze[monsterY][monsterX + 1] != '#' && playerX > monsterX && maze[monsterY][monsterX + 1] != 'O' ) monsterX++;
            else if (playerX < monsterX && maze[monsterY][monsterX - 1] != '#' && playerX < monsterX && maze[monsterY][monsterX - 1] != 'O') monsterX--;
            else if (playerY > monsterY && maze[monsterY + 1][monsterX] != '#' && playerY > monsterY && maze[monsterY + 1][monsterX] != 'O') monsterY++;
            else if (playerY < monsterY && maze[monsterY - 1][monsterX] != '#' && playerY < monsterY && maze[monsterY - 1][monsterX] != 'O' ) monsterY--;
        }

        public void keyPressed(KeyEvent e) {
            int dx = 0, dy = 0;
           // int[][] directions = {
           //     {-1, -1}, {-1, 0}, {-1, 1},
           //     { 0, -1},          { 0, 1},
           //     { 1, -1}, { 1, 0}, { 1, 1}
           // };
            // After player position updates and BEFORE repaint()
            
        
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: dy = -1; break;
                case KeyEvent.VK_DOWN: dy = 1; break;
                case KeyEvent.VK_LEFT: dx = -1; break;
                case KeyEvent.VK_RIGHT: dx = 1; break;
                case KeyEvent.VK_W: dy = -1; break;
                case KeyEvent.VK_S: dy = 1; break;
                case KeyEvent.VK_A: dx= -1; break;
                case KeyEvent.VK_D: dx = 1; break;
            }

            int newX = playerX + dx;
            int newY = playerY + dy;

                        // Play sound for all adjacent tiles

//for (int[] dir : directions) {
//    int nx = playerX + dir[1];
//    int ny = playerY + dir[0];
//    if (nx >= 0 && ny >= 0 && ny < maze.length && nx < maze[0].length) {
//        char tile = maze[ny][nx];
//        switch (tile) {
//            case '#': playSound("wall.wav");
//             try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();} 
//                    break;
//            case 'L': playSound("lava.wav"); 
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();} break;
//            case 'B': playSound("banana.wav"); try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();} break;
//            case 'O': playSound("O.wav");try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();} break;
//            case '1': case '2': case '3': case '4': case 'W':
//                playSound("door.wav"); 
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();} 
//                    break;
//            default: playSound("floor.wav");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();} break;
//
//        }
   // }
//}
            if (newX >= 0 && newY >= 0 && newX < maze[0].length && newY < maze.length && maze[newY][newX] != '#') {
                playerX = newX;
                playerY = newY;
                MonkeyAdventure.moveCount++;
                moveMonster();

                if (playerX == monsterX && playerY == monsterY) {
                    gameTimer.stop();
                    showGameOver();
                    SwingUtilities.getWindowAncestor(this).dispose();
                    return;
                }

                char current = maze[playerY][playerX];
                if (current == 'B') {
                    MonkeyAdventure.bananaCount++;
                    maze[playerY][playerX] = ' ';
                }

                if ("1234".indexOf(current) >= 0 && !completedTrials.contains(current)) {
                    completedTrials.add(current);
                    maze[playerY][playerX] = ' '; // Mark door as used (turn to floor)
                    Set<Character> requiredTrials = new HashSet<>(Arrays.asList('1', '2', '3', '4'));
                    if (completedTrials.containsAll(requiredTrials)) {
                        JOptionPane.showMessageDialog(this, "All trials complete! Proceed to the final door.");
                        gameTimer.stop();
                        showMaze(true);
                        SwingUtilities.getWindowAncestor(this).dispose();
                    } else {
                        if (current == '1') playTrial("mika1.png", "Follow the sound pattern", "Walk straight across quickly", "You make it safely.", "Trap collapses.");
                        if (current == '2') playTrial("mika2.png", "Feel air flow", "Watch shadows", "You find a hidden passage.", "You get zapped.");
                        if (current == '3') playTrial("mika3.png", "Tiptoe edge", "Rush middle", "Silent footwork succeeds.", "Stone falls on you!");
                        if (current == '4') playTrial("mika4.png", "Whistle rhythm", "Clap twice", "Final door opens!", "Posion blow dart shoots out of the wall!");
                    }
                } else if (current == 'L') {
                    gameTimer.stop();
                    showGameOver();
                    SwingUtilities.getWindowAncestor(this).dispose();
                } else if (current == 'W') {
                    gameTimer.stop();
                    showWinScreen();
                    SwingUtilities.getWindowAncestor(this).dispose();
                }
                // After player position updates and BEFORE repaint()


                repaint();
            }
        }

        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }

 public static void playTrial(String imageName, String optionAText, String optionBText, String resultA, String resultB) {
        JFrame trialFrame = new JFrame("Trial");
        trialFrame.setSize(800, 600);
        trialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon(imageName).getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton buttonA = new JButton("A");
        JButton buttonB = new JButton("B");

        buttonA.setToolTipText(optionAText);
        buttonB.setToolTipText(optionBText);

        buttonA.addActionListener(e -> {
            JOptionPane.showMessageDialog(trialFrame, resultA);
            trialFrame.dispose();
        });

        buttonB.addActionListener(e -> {
            JOptionPane.showMessageDialog(trialFrame, resultB);
            showGameOver();
            trialFrame.dispose();
        });

        buttonPanel.add(buttonA);
        buttonPanel.add(buttonB);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        trialFrame.setContentPane(backgroundPanel);
        trialFrame.setVisible(true);
    }

    public static void showGameOver() {
        for (Frame f : Frame.getFrames()) f.dispose();

        JFrame deadFrame = new JFrame("Game Over");
        deadFrame.setSize(800, 600);
        deadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            Image bg = new ImageIcon("dead.png").getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        JButton quitButton = new JButton("Quit");
        JButton mainMenuButton = new JButton("Main Menu");

        quitButton.addActionListener(e -> System.exit(0));
        mainMenuButton.addActionListener(e -> {
            deadFrame.dispose();
            completedTrials.clear();
            bananaCount = 0;
            showMainMenu();
        });

        panel.add(mainMenuButton);
        panel.add(quitButton);

        deadFrame.setContentPane(panel);
        deadFrame.setVisible(true);
    }

    public static void showWinScreen() {
        JFrame winFrame = new JFrame("You Win!");
        winFrame.setSize(800, 600);
        winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int secondsPlayedcap = secondsPlayed;
        if (secondsPlayedcap>1000){
            secondsPlayedcap = 1000;
        }
        int moveCountcap = moveCount;
        if (moveCount>500) {
            moveCountcap = 500;
        }
        int score = Math.max(0, 1500 - moveCountcap - secondsPlayedcap + bananaCount * 30);

        JPanel panel = new JPanel() {
            Image bg = new ImageIcon("win.png").getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Final Score: " + score, 20, 30);
                g.setFont(new Font("Arial", Font.BOLD,18));
                g.drawString("Moves: " + moveCount, 20, 55);
                g.drawString("Time: " + secondsPlayed + "s", 20, 80);
                g.drawString("Bananas: " + bananaCount, 20, 105);
            }
        };

        winFrame.setContentPane(panel);
        winFrame.setVisible(true);
    }

    private static void playSound(String filename) {
        new Thread(() -> {
            try {
                File soundFile = new File(filename);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    

}



//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import javax.swing.Timer;
//
//public class MonkeyAdventure {
//    public static JFrame mazeFrame = new JFrame("Maze");
//    public static JFrame frame = new JFrame("Monkey Island: The Forbidden Temple");
//    public static Set<Character> completedTrials = new HashSet<>();
//    public static int bananaCount = 0;
//    public static int moveCount = 0;
//    public static int secondsPlayed = 0;
//
//    public static void main(String[] args) {
//        showMainMenu();
//    }
//
//    public static void showMainMenu() {
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//
//        JPanel backgroundPanel = new JPanel() {
//            Image bg = new ImageIcon("start.png").getImage();
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
//            }
//        };
//        backgroundPanel.setLayout(new BorderLayout());
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setOpaque(false);
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
//
//        JButton startGame = new JButton("Start Adventure");
//        startGame.addActionListener(e -> {
//            bananaCount = 0;
//            moveCount = 0;
//            secondsPlayed = 0;
//            completedTrials.clear();
//            showMaze(false);
//        });
//
//        JButton quit = new JButton("Quit");
//        quit.addActionListener(e -> System.exit(0));
//
//        buttonPanel.add(startGame);
//        buttonPanel.add(quit);
//
//        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
//        frame.setContentPane(backgroundPanel);
//        frame.setVisible(true);
//    }
//
//    public static void showMaze(boolean finalMaze) {
//        mazeFrame.dispose();
//        mazeFrame = new JFrame("Maze");
//        mazeFrame.setSize(1180, 930);
//        mazeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        MazePanel mazePanel = new MazePanel(finalMaze);
//        mazeFrame.add(mazePanel);
//        mazeFrame.setVisible(true);
//        mazePanel.requestFocusInWindow();
//    }
//
//    static class MazePanel extends JPanel implements KeyListener, ActionListener {
//        private char[][] maze;
//        private int playerX = 1, playerY = 1;
//        private int monsterX = 1, monsterY = 1;
//        private final boolean isFinal;
//        private Timer gameTimer;
//
//        public MazePanel(boolean isFinalMaze) {
//            this.isFinal = isFinalMaze;
//            setFocusable(true);
//            addKeyListener(this);
//            maze = createMaze(isFinal);
//
//            gameTimer = new Timer(1000, this);
//            gameTimer.start();
//
//            for (int y = 0; y < maze.length; y++) {
//                for (int x = 0; x < maze[y].length; x++) {
//                    if (maze[y][x] == 'S') {
//                        playerX = x;
//                        playerY = y;
//                    }
//                }
//            }
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            MonkeyAdventure.secondsPlayed++;
//            repaint();
//        }
//
//        private char[][] createMaze(boolean finalMaze) {
//            char[][] m = {
//                "###########################".toCharArray(),
//                "#1 B   L     B #   B     4#".toCharArray(),
//                "# ##L## # ## #    ## ##L #".toCharArray(),
//                "# L   # # B   #  L     # #".toCharArray(),
//                "# #BL # # # ## ## # # #  #".toCharArray(),
//                "# # # L   #         # #  #".toCharArray(),
//                "#B  # # #L###### # ##B#  #".toCharArray(),
//                "# #           #   B      #".toCharArray(),
//                "# ######L###### ######## #".toCharArray(),
//                "#  B         #           #".toCharArray(),
//                "# #L######## ##########  #".toCharArray(),
//                "# #     B #              #".toCharArray(),
//                "# # #L###### ####### L # #".toCharArray(),
//                "# #L    #  B   #         #".toCharArray(),
//                "# # # L B B #  # # L##  B#".toCharArray(),
//                "# # #     B    # #   # # #".toCharArray(),
//                "# #   #### #  ## #L### #L#".toCharArray(),
//                "# #B     # L       L #B# #".toCharArray(),
//                "# L ###### #####L### # L3#".toCharArray(),
//                "#2 B                    L#".toCharArray(),
//                "###############S##########".toCharArray()
//            };
//            if (finalMaze) m[0][14] = 'W';
//            return m;
//        }
//
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            int rows = maze.length;
//            int cols = maze[0].length;
//            int cellWidth = getWidth() / cols;
//            int cellHeight = getHeight() / rows;
//
//            for (int y = 0; y < rows; y++) {
//                for (int x = 0; x < cols; x++) {
//                    char tile = maze[y][x];
//                    Image img;
//                if (tile == '#') {
//                    img = new ImageIcon("wall.png").getImage();
//                } else if (tile == 'L') {
//                    img = new ImageIcon("lava.png").getImage();
//                } else if (tile == 'B') {
//                    img = new ImageIcon("banana.png").getImage();
//                } else if (tile == 'W') {
//                    img = new ImageIcon("door.png").getImage();
//                } else if (tile == '1' || tile == '2' || tile == '3' || tile == '4') {
//                    if (completedTrials.contains(tile)) {
//                        img = new ImageIcon("floor.png").getImage();
//                    } else {
//                        img = new ImageIcon("door.png").getImage();
//                    }
//                } else {
//                    img = new ImageIcon("floor.png").getImage();
//                }
//                
//                    g.drawImage(img, x * cellWidth, y * cellHeight, cellWidth, cellHeight, this);
//                }
//            }
//
//            g.drawImage(new ImageIcon("monkey.png").getImage(), playerX * cellWidth, playerY * cellHeight, cellWidth, cellHeight, this);
//            g.drawImage(new ImageIcon("monster.png").getImage(), monsterX * cellWidth, monsterY * cellHeight, cellWidth, cellHeight, this);
//
//            g.setColor(Color.YELLOW);
//            g.drawString("Bananas: " + bananaCount, 10, 20);
//            g.drawString("Moves: " + moveCount, 10, 35);
//            g.drawString("Time: " + secondsPlayed + "s", 10, 50);
//        }
//
//        private void moveMonster() {
//            if (playerX > monsterX && maze[monsterY][monsterX + 1] != '#') monsterX++;
//            else if (playerX < monsterX && maze[monsterY][monsterX - 1] != '#') monsterX--;
//            else if (playerY > monsterY && maze[monsterY + 1][monsterX] != '#') monsterY++;
//            else if (playerY < monsterY && maze[monsterY - 1][monsterX] != '#') monsterY--;
//        }
//
//        public void keyPressed(KeyEvent e) {
//            int dx = 0, dy = 0;
//            switch (e.getKeyCode()) {
//                case KeyEvent.VK_UP:
//                    dy = -1;
//                    break;
//                case KeyEvent.VK_DOWN:
//                    dy = 1;
//                    break;
//                case KeyEvent.VK_LEFT:
//                    dx = -1;
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    dx = 1;
//                    break;
//            }
//            
//
//            int newX = playerX + dx;
//            int newY = playerY + dy;
//            if (newX >= 0 && newY >= 0 && newX < maze[0].length && newY < maze.length && maze[newY][newX] != '#') {
//                playerX = newX;
//                playerY = newY;
//                moveCount++;
//                moveMonster();
//
//                if (playerX == monsterX && playerY == monsterY || maze[playerY][playerX] == 'L') {
//                    gameTimer.stop();
//                    showGameOver();
//                    SwingUtilities.getWindowAncestor(this).dispose();
//                    return;
//                }
//
//                char current = maze[playerY][playerX];
//                if (current == 'B') {
//                    bananaCount++;
//                    maze[playerY][playerX] = ' ';
//                } else if ("1234".indexOf(current) >= 0 && !completedTrials.contains(current)) {
//                    completedTrials.add(current);
//                    maze[playerY][playerX] = ' ';
//                    Set<Character> requiredTrials = new HashSet<>(Arrays.asList('1', '2', '3', '4'));
//if (completedTrials.containsAll(requiredTrials)) {
//    JOptionPane.showMessageDialog(this, "All trials complete! Proceed to the final door.");
//    gameTimer.stop();
//    showMaze(true);
//    SwingUtilities.getWindowAncestor(this).dispose();
//} else {
//    if (current == '1') playTrial("mika1.png", "Follow the sound pattern", "Walk straight across quickly", "You make it safely.", "Trap collapse");
//    if (current == '2') playTrial("mika2.png", "Feel air flow", "Watch shadows", "You find a hidden passage.", "You get zapped.");
//    if (current == '3') playTrial("mika3.png", "Tiptoe edge", "Rush middle", "Silent footwork succeeds.", "Stone falls on you!");
//    if (current == '4') playTrial("mika4.png", "Whistle rhythm", "Clap twice", "Final door opens!", "Posion blow dart shoots out of the wall!");
//}
//                    
//                    
//                    
//                    
//                    
//                    
//                    
//                } else if (current == 'W') {
//                    gameTimer.stop();
//                    showWinScreen();
//                    SwingUtilities.getWindowAncestor(this).dispose();
//                }
//                repaint();
//            }
//        }
//
//        public void keyReleased(KeyEvent e) {}
//        public void keyTyped(KeyEvent e) {}
//    }
//
//    public static void playTrial(String imageName, String optionAText, String optionBText, String resultA, String resultB) {
//        JFrame trialFrame = new JFrame("Trial");
//        trialFrame.setSize(800, 600);
//        trialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel backgroundPanel = new JPanel() {
//            Image bg = new ImageIcon(imageName).getImage();
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
//            }
//        };
//        backgroundPanel.setLayout(new BorderLayout());
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setOpaque(false);
//        buttonPanel.setLayout(new FlowLayout());
//
//        JButton buttonA = new JButton("A");
//        JButton buttonB = new JButton("B");
//
//        buttonA.setToolTipText(optionAText);
//        buttonB.setToolTipText(optionBText);
//
//        buttonA.addActionListener(e -> {
//            JOptionPane.showMessageDialog(trialFrame, resultA);
//            trialFrame.dispose();
//        });
//
//        buttonB.addActionListener(e -> {
//            JOptionPane.showMessageDialog(trialFrame, resultB);
//            showGameOver();
//            trialFrame.dispose();
//        });
//
//        buttonPanel.add(buttonA);
//        buttonPanel.add(buttonB);
//        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        trialFrame.setContentPane(backgroundPanel);
//        trialFrame.setVisible(true);
//    }
//
//    public static void showGameOver() {
//        for (Frame f : Frame.getFrames()) f.dispose();
//
//        JFrame deadFrame = new JFrame("Game Over");
//        deadFrame.setSize(800, 600);
//        deadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel() {
//            Image bg = new ImageIcon("dead.png").getImage();
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
//            }
//        };
//        panel.setLayout(new FlowLayout());
//
//        JButton quitButton = new JButton("Quit");
//        JButton mainMenuButton = new JButton("Main Menu");
//
//        quitButton.addActionListener(e -> System.exit(0));
//        mainMenuButton.addActionListener(e -> {
//            deadFrame.dispose();
//            completedTrials.clear();
//            bananaCount = 0;
//            showMainMenu();
//        });
//
//        panel.add(mainMenuButton);
//        panel.add(quitButton);
//
//        deadFrame.setContentPane(panel);
//        deadFrame.setVisible(true);
//    }
//
//    public static void showWinScreen() {
//        JFrame winFrame = new JFrame("You Win!");
//        winFrame.setSize(800, 600);
//        winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        int score = Math.max(0, 1000 - moveCount - secondsPlayed + bananaCount * 30);
//
//        JPanel panel = new JPanel() {
//            Image bg = new ImageIcon("win.png").getImage();
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
//                g.setColor(Color.YELLOW);
//                g.setFont(new Font("Arial", Font.BOLD, 18));
//                g.drawString("Final Score: " + score, 20, 30);
//                g.drawString("Moves: " + moveCount, 20, 55);
//                g.drawString("Time: " + secondsPlayed + "s", 20, 80);
//                g.drawString("Bananas: " + bananaCount, 20, 105);
//            }
//        };
//
//        winFrame.setContentPane(panel);
//        winFrame.setVisible(true);
//    }
//}
//