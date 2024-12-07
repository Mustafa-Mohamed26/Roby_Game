import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class Matrix extends JFrame {
    // Constants for the grid size
    private static final int GRID_SIZE = 20;

    // Components for the grid and control panel
    private JButton[][] cellButtons = new JButton[GRID_SIZE][GRID_SIZE];
    private JButton happyFaceButton, xButton, goalButton, goButton, resetButton, undoButton;

    // Positions of Happy Face and Goal
    private int happyFaceRow = -1, happyFaceCol = -1;
    private int goalRow = -1, goalCol = -1;

    // Stack to manage undo operations
    private Stack<Action> actionStack = new Stack<>();

    // Input fields for custom rows and columns
    private JTextField rowInputField, colInputField;
    private JButton setSizeButton;

    //=========================================================================================================

    // Constructor: Initializes the GUI
    public Matrix() {
        setTitle("20x20 Matrix GUI");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // Matrix panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        gridPanel.setBackground(new Color(255, 255, 255)); // Dark background for contrast


        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton cellButton = new JButton();

                // Customize button
                //cellButton.setFont(new Font("Arial", Font.BOLD, 16));                   // Bold font
                cellButton.setBackground(null);
                cellButton.setForeground(Color.black);                                    // White text
                cellButton.setFocusPainted(false);                                        // Remove focus outline
                cellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));                     // Hand cursor on hover
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Thick border

//                // Add hover effect
//                cellButton.addMouseListener(new java.awt.event.MouseAdapter() {
//                    @Override
//                    public void mouseEntered(java.awt.event.MouseEvent evt) {
//                        cellButton.setBackground(new Color(100, 149, 237)); // Light blue on hover
//                    }
//
//                    @Override
//                    public void mouseExited(java.awt.event.MouseEvent evt) {
//                        cellButton.setBackground(new Color(70, 130, 180)); // Restore original color
//                    }
//                });

                cellButton.addActionListener(new CellClickListener(row, col));
                cellButtons[row][col] = cellButton;
                gridPanel.add(cellButton);
            }
        }

        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 6));
        controlPanel.setPreferredSize(new Dimension(400, 40));

        // Happy Face Button
        happyFaceButton = new JButton("Character");
        happyFaceButton.addActionListener(e -> {
            enablePlacing("😊");
            //happyFaceButton.setEnabled(false);
        });
        // Customize the button
        happyFaceButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font
        happyFaceButton.setBackground(new Color(52, 82, 204));        // Set background color
        happyFaceButton.setForeground(Color.WHITE);                            // Set text color
        happyFaceButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,2)); // Set border
        happyFaceButton.setFocusPainted(false);                                // Remove focus border
        happyFaceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));             // Set cursor to hand
        controlPanel.add(happyFaceButton);

        // X Button
        xButton = new JButton("Set X");
        xButton.addActionListener(e -> enablePlacing("X"));
        // Customize the button
        xButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font
        xButton.setBackground(new Color(255, 50, 50));        // Set background color
        xButton.setForeground(Color.WHITE);                            // Set text color
        xButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,2)); // Set border
        xButton.setFocusPainted(false);                                // Remove focus border
        xButton.setCursor(new Cursor(Cursor.HAND_CURSOR));             // Set cursor to hand
        controlPanel.add(xButton);

        // Goal Button
        goalButton = new JButton("Set Goal");
        goalButton.addActionListener(e -> {
            enablePlacing("🎯");
            //goalButton.setEnabled(false);
        });
        // Customize the button
        goalButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font
        goalButton.setBackground(new Color(255, 173, 50));        // Set background color
        goalButton.setForeground(Color.WHITE);                            // Set text color
        goalButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,2)); // Set border
        goalButton.setFocusPainted(false);                                // Remove focus border
        goalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));             // Set cursor to hand
        controlPanel.add(goalButton);

        // Go Button
        goButton = new JButton("Go");
        goButton.addActionListener(e -> startMovingToGoal());
        // Customize the button
        goButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font
        goButton.setBackground(new Color(70, 205, 35));        // Set background color
        goButton.setForeground(Color.WHITE);                            // Set text color
        goButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,2)); // Set border
        goButton.setFocusPainted(false);                                // Remove focus border
        goButton.setCursor(new Cursor(Cursor.HAND_CURSOR));             // Set cursor to hand
        controlPanel.add(goButton);

        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        // Customize the button
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font
        resetButton.setBackground(new Color(0, 0, 0));        // Set background color
        resetButton.setForeground(Color.WHITE);                            // Set text color
        resetButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,2)); // Set border
        resetButton.setFocusPainted(false);                                // Remove focus border
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));             // Set cursor to hand
        controlPanel.add(resetButton);

        // Undo Button
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undoLastAction());
        // Customize the button
        undoButton.setFont(new Font("Arial", Font.BOLD, 16));  // Set font
        undoButton.setBackground(new Color(0, 0, 0));        // Set background color
        undoButton.setForeground(Color.WHITE);                            // Set text color
        undoButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,2)); // Set border
        undoButton.setFocusPainted(false);                                // Remove focus border
        undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));             // Set cursor to hand
        controlPanel.add(undoButton);

        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    //=========================================================================================================

    private void enablePlacing(String symbol) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cellButtons[row][col].setEnabled(true);
                cellButtons[row][col].putClientProperty("symbol", symbol);
            }
        }
    }

    //=========================================================================================================

//    private void startMovingToGoal() {
//        // Implement pathfinding logic here (e.g., BFS or DFS to avoid X blocks)
//        // This is a placeholder message for now
//        JOptionPane.showMessageDialog(this, "Pathfinding not yet implemented.");
//    }

    private void startMovingToGoal() {
        if (happyFaceRow == -1 || happyFaceCol == -1 || goalRow == -1 || goalCol == -1) {
            JOptionPane.showMessageDialog(this, "Place both the Happy Face 😊 and the Goal 🎯 before starting!");
            return;
        }

        // Clear any previous path
        resetPath();

        // Show algorithm selection dialog
        String[] options = {"BFS", "DFS", "A*"};
        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choose the algorithm to find the path:",
                "Algorithm Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == null) {
            JOptionPane.showMessageDialog(this, "No algorithm selected.");
            return;
        }

        switch (choice) {
            case "BFS":
                bfs();
                break;
            case "DFS":
                dfs();
                break;
            case "A*":
                aStar();
                break;
        }
    }

    //=========================================================================================================

    private void bfs() {
        // Check if the starting and goal positions are valid
        if (happyFaceRow == -1 || happyFaceCol == -1 || goalRow == -1 || goalCol == -1) {
            JOptionPane.showMessageDialog(this, "Place both the Happy Face 😊 and the Goal 🎯 before starting!");
            return;
        }

        // Directions for moving: right, down, left, up
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};

        // Queue for BFS: stores (row, col) pairs
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{happyFaceRow, happyFaceCol});

        // To track visited cells
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        visited[happyFaceRow][happyFaceCol] = true;

        // To reconstruct the path
        int[][] parentRow = new int[GRID_SIZE][GRID_SIZE];
        int[][] parentCol = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(parentRow[i], -1);
            Arrays.fill(parentCol[i], -1);
        }

        boolean found = false;

        // BFS loop
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            // Check if goal is reached
            if (row == goalRow && col == goalCol) {
                found = true;
                break;
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];

                // Check boundaries and if cell is unvisited and not an 'X'
                if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE
                        && !visited[newRow][newCol]
                        && !"X".equals(cellButtons[newRow][newCol].getText())) {

                    queue.add(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;

                    // Track the parent for path reconstruction
                    parentRow[newRow][newCol] = row;
                    parentCol[newRow][newCol] = col;
                }
            }
        }

        if (found) {
            reconstructPath(parentRow, parentCol);
        } else {
            JOptionPane.showMessageDialog(this, "No path found using BFS.");
        }
    }

    //=========================================================================================================

    private void dfs() {
        // Create a 2D array to track visited cells
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];

        // Create a stack to simulate the recursive behavior of Depth First Search (DFS)
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{happyFaceRow, happyFaceCol}); // Push the starting position (Happy Face) onto the stack

        // Arrays to keep track of the parent cell for each cell (used for path reconstruction)
        int[][] parentRow = new int[GRID_SIZE][GRID_SIZE];
        int[][] parentCol = new int[GRID_SIZE][GRID_SIZE];

        // Initialize parent arrays with -1, indicating no parent yet
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(parentRow[i], -1);
            Arrays.fill(parentCol[i], -1);
        }

        boolean found = false; // Flag to check if the goal is reached

        // Main DFS loop
        while (!stack.isEmpty()) {
            int[] current = stack.pop(); // Get the current cell from the stack
            int row = current[0];
            int col = current[1];

            // Skip this cell if it has already been visited
            if (visited[row][col]) continue;
            visited[row][col] = true; // Mark the current cell as visited

            // Check if the goal (🎯) is reached
            if (row == goalRow && col == goalCol) {
                found = true;
                break;
            }

            // Arrays representing possible movements: right, down, left, up
            int[] dr = {0, 1, 0, -1};
            int[] dc = {1, 0, -1, 0};

            // Explore the neighboring cells
            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];

                // Check if the neighbor is within bounds, unvisited, and not blocked ('X')
                if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE
                        && !visited[newRow][newCol]
                        && !"X".equals(cellButtons[newRow][newCol].getText())) {
                    stack.push(new int[]{newRow, newCol}); // Add the neighbor to the stack
                    parentRow[newRow][newCol] = row; // Record the current cell as the parent of the neighbor
                    parentCol[newRow][newCol] = col;
                }
            }
        }

        // If the goal is found, reconstruct the path; otherwise, show an error message
        if (found) reconstructPath(parentRow, parentCol);
        else JOptionPane.showMessageDialog(this, "No path found using DFS.");
    }

    //=========================================================================================================

    private void aStar() {
        // Priority Queue to store nodes, sorted by their cost (current cost + heuristic)
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(a.cost, b.cost));
        // Add the starting node (Happy Face) with a cost of 0
        pq.add(new Node(happyFaceRow, happyFaceCol, 0));

        // 2D array to track visited cells
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];

        // Arrays to keep track of the parent of each cell (used for path reconstruction)
        int[][] parentRow = new int[GRID_SIZE][GRID_SIZE];
        int[][] parentCol = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(parentRow[i], -1); // Initialize with -1 (no parent)
            Arrays.fill(parentCol[i], -1);
        }

        // Main A* loop
        while (!pq.isEmpty()) {
            // Get the node with the lowest cost from the priority queue
            Node current = pq.poll();
            int row = current.row;
            int col = current.col;

            // Skip this cell if it has already been visited
            if (visited[row][col]) continue;
            visited[row][col] = true; // Mark the current cell as visited

            // Check if the goal (🎯) has been reached
            if (row == goalRow && col == goalCol) {
                reconstructPath(parentRow, parentCol); // Reconstruct and display the path
                return; // End the function
            }

            // Arrays representing possible movements: right, down, left, up
            int[] dr = {0, 1, 0, -1};
            int[] dc = {1, 0, -1, 0};

            // Explore the neighboring cells
            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];

                // Check if the neighbor is within bounds, unvisited, and not blocked ('X')
                if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE
                        && !visited[newRow][newCol]
                        && !"X".equals(cellButtons[newRow][newCol].getText())) {
                    // Calculate the heuristic (straight-line distance to the goal)
                    double heuristic = euclideanDistance(newRow, newCol, goalRow, goalCol);
                    // Add the neighbor to the priority queue with updated cost
                    pq.add(new Node(newRow, newCol, current.cost + 1 + heuristic));
                    // Record the current cell as the parent of the neighbor
                    parentRow[newRow][newCol] = row;
                    parentCol[newRow][newCol] = col;
                }
            }
        }

        // If the queue is empty and the goal hasn't been reached, display an error message
        JOptionPane.showMessageDialog(this, "No path found using A*.");
    }


    private double euclideanDistance(int row1, int col1, int row2, int col2) {
        return Math.sqrt((row1 - row2) * (row1 - row2) + (col1 - col2) * (col1 - col2));
    }

    //=========================================================================================================

    private void reconstructPath(int[][] parentRow, int[][] parentCol) {
        // Start from the goal cell (🎯)
        int row = goalRow;
        int col = goalCol;
        int stepCount = 0; // Variable to count steps

        // Trace back from the goal to the start (😊) using the parent arrays
        while (!(row == happyFaceRow && col == happyFaceCol)) {
            // Highlight the current cell on the path with a green background
            cellButtons[row][col].setBackground(Color.GREEN);

            // Move to the parent cell by retrieving its coordinates from the parent arrays
            int tempRow = parentRow[row][col];
            int tempCol = parentCol[row][col];
            row = tempRow;
            col = tempCol;

            // Increment the step count after moving to the parent
            stepCount++;
        }

        // Highlight the starting point (😊)
        cellButtons[happyFaceRow][happyFaceCol].setBackground(Color.GREEN);

        // Display a success message with the number of steps
        JOptionPane.showMessageDialog(this, "Path found! 😊 reached 🎯 in " + stepCount + " steps.");
    }

    //=========================================================================================================

    private static class Node {
        int row, col;
        double cost;

        Node(int row, int col, double cost) {
            this.row = row;
            this.col = col;
            this.cost = cost;
        }
    }

    //=========================================================================================================


    private void resetBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cellButtons[row][col].setText(""); // Clear text
                cellButtons[row][col].setEnabled(true); // Enable button
                cellButtons[row][col].setBackground(null); // Reset background to default
            }
        }
        happyFaceButton.setEnabled(true); // Re-enable Happy Face button
        goalButton.setEnabled(true); // Re-enable Goal button
        happyFaceRow = happyFaceCol = goalRow = goalCol = -1; // Reset positions
        actionStack.clear(); // Clear undo stack
    }

    //=========================================================================================================

    private void undoLastAction() {
        if (!actionStack.isEmpty()) {
            Action lastAction = actionStack.pop();
            cellButtons[lastAction.row][lastAction.col].setText(lastAction.previousText);
            if (lastAction.previousText.equals("😊")) happyFaceButton.setEnabled(true);
            if (lastAction.previousText.equals("🎯")) goalButton.setEnabled(true);
        }
    }

    //=========================================================================================================

    private void resetPath() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cellButtons[row][col].getBackground() == Color.GREEN) {
                    cellButtons[row][col].setBackground(null); // Reset background to default
                }
            }
        }
    }

    //=========================================================================================================


    private class CellClickListener implements ActionListener {
        private final int row;
        private final int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String symbol = (String) button.getClientProperty("symbol");

            // Store previous text for undo functionality
            actionStack.push(new Action(row, col, button.getText()));

            button.setText(symbol);
            button.setEnabled(false);

            if (symbol.equals("😊")) {
                happyFaceRow = row;
                happyFaceCol = col;
            } else if (symbol.equals("🎯")) {
                goalRow = row;
                goalCol = col;
            }
        }
    }

    private class Action {
        int row, col;
        String previousText;

        Action(int row, int col, String previousText) {
            this.row = row;
            this.col = col;
            this.previousText = previousText;
        }
    }
}
