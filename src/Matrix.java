import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class Matrix extends JFrame {
    // Constants for the grid size
    private  int gridRows = 20;
    private  int gridCols = 20;

    // Components for the grid and control panel
    private JButton[][] cellButtons;
    private JButton happyFaceButton, xButton, goalButton, goButton, resetButton, undoButton;

    // Positions of Happy Face and Goal
    private int happyFaceRow = -1, happyFaceCol = -1;
    private int goalRow = -1, goalCol = -1;

    // Stack to manage undo operations
    private Stack<Action> actionStack = new Stack<>();

    // Input fields for custom rows and columns
    private JTextField rowInputField, colInputField;
    private JButton setSizeButton;

    private JPanel gridPanel;

//=========================================================================================================

    // Constructor: Initializes the GUI
    public Matrix() {
        setTitle("Custom Grid Matrix GUI");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel for custom rows and columns
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rowInputField = new JTextField(String.valueOf(gridRows),10); // Field for number of rows
        colInputField = new JTextField(String.valueOf(gridCols),10); // Field for number of columns

        setSizeButton = new JButton("Resize Grid");
        setSizeButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font
        setSizeButton.setBackground(new Color(0, 0, 0)); // Set background color
        setSizeButton.setForeground(Color.WHITE); // Set text color
        setSizeButton.setPreferredSize(new Dimension(150, 30));
        setSizeButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        setSizeButton.setFocusPainted(false); // Remove focus border
        setSizeButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand

        inputPanel.add(new JLabel("Rows:"));
        inputPanel.add(rowInputField);
        inputPanel.add(new JLabel("Cols:"));
        inputPanel.add(colInputField);
        inputPanel.add(setSizeButton);
        add(inputPanel, BorderLayout.NORTH);

        // Matrix panel
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridRows, gridCols));
        gridPanel.setBackground(new Color(255, 255, 255)); // Dark background for contrast

        cellButtons = new JButton[gridRows][gridCols]; // Initialize cellButtons array

        // Create buttons for the grid
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                JButton cellButton = new JButton();
                cellButton.setFont(new Font("", Font.BOLD, 30)); // Bold font
                cellButton.setBackground(null);
                cellButton.setForeground(Color.BLACK); // Black text
                cellButton.setFocusPainted(false); // Remove focus outline
                cellButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Thick border

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
        });
        happyFaceButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        happyFaceButton.setBackground(new Color(52, 82, 204)); // Set background color
        happyFaceButton.setForeground(Color.WHITE); // Set text color
        happyFaceButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        happyFaceButton.setFocusPainted(false); // Remove focus border
        happyFaceButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        controlPanel.add(happyFaceButton);

        // X Button
        xButton = new JButton("Set X");
        xButton.addActionListener(e -> enablePlacing("X"));
        xButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        xButton.setBackground(new Color(255, 50, 50)); // Set background color
        xButton.setForeground(Color.WHITE); // Set text color
        xButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        xButton.setFocusPainted(false); // Remove focus border
        xButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        controlPanel.add(xButton);

        // Goal Button
        goalButton = new JButton("Set Goal");
        goalButton.addActionListener(e -> {
            enablePlacing("🎯");
        });
        goalButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        goalButton.setBackground(new Color(255, 173, 50)); // Set background color
        goalButton.setForeground(Color.WHITE); // Set text color
        goalButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        goalButton.setFocusPainted(false); // Remove focus border
        goalButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        controlPanel.add(goalButton);

        // Go Button
        goButton = new JButton("Go");
        goButton.addActionListener(e -> startMovingToGoal());
        goButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        goButton.setBackground(new Color(70, 205, 35)); // Set background color
        goButton.setForeground(Color.WHITE); // Set text color
        goButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        goButton.setFocusPainted(false); // Remove focus border
        goButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        controlPanel.add(goButton);

        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        resetButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        resetButton.setBackground(new Color(0, 0, 0)); // Set background color
        resetButton.setForeground(Color.WHITE); // Set text color
        resetButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        resetButton.setFocusPainted(false); // Remove focus border
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        controlPanel.add(resetButton);

        // Undo Button
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undoLastAction());
        undoButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        undoButton.setBackground(new Color(0, 0, 0)); // Set background color
        undoButton.setForeground(Color.WHITE); // Set text color
        undoButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Set border
        undoButton.setFocusPainted(false); // Remove focus border
        undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        controlPanel.add(undoButton);

        // Resize grid action
        setSizeButton.addActionListener(new ResizeListener());

        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    //=========================================================================================================

    private void initializeMatrix() {
        gridPanel.removeAll();
        cellButtons = new JButton[gridRows][gridCols]; // Initialize cellButtons array

        // Create buttons for the grid
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                JButton cellButton = new JButton();
                cellButton.setFont(new Font("", Font.BOLD, 30)); // Bold font
                cellButton.setBackground(null);
                cellButton.setForeground(Color.BLACK); // Black text
                cellButton.setFocusPainted(false); // Remove focus outline
                cellButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Thick border

                cellButton.addActionListener(new CellClickListener(row, col));
                cellButtons[row][col] = cellButton;
                gridPanel.add(cellButton);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    //=========================================================================================================
    private class ResizeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int newRows = Integer.parseInt(rowInputField.getText());
                int newCols = Integer.parseInt(colInputField.getText());
                if (newRows > 0 && newRows <= 100 && newCols > 0 && newCols <= 100) {
                    gridRows = newRows;
                    gridCols = newCols;
                    gridPanel.setLayout(new GridLayout(gridRows, gridCols));
                    initializeMatrix();
                } else {
                    JOptionPane.showMessageDialog(
                            Matrix.this,
                            "Rows and columns must be between 1 and 100.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        Matrix.this,
                        "Please enter valid numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    //=========================================================================================================

    private void enablePlacing(String symbol) {
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                JButton cellButton = cellButtons[row][col];
                cellButton.setEnabled(true); // Enable the button
                cellButton.putClientProperty("symbol", symbol); // Store the symbol

                // Add action listener to update the button state when clicked
                cellButton.addActionListener(e -> {
                    String currentSymbol = (String) cellButton.getClientProperty("symbol");
                    if (currentSymbol.equals("X")) {
                        cellButton.setBackground(new Color(255, 50, 50)); // Change background to red for X
                        cellButton.setForeground(Color.WHITE); // Change foreground to white for X
                    } else if(currentSymbol.equals("😊")) {
                        cellButton.setBackground(new Color(52, 82, 204)); // Reset background for other symbols
                        cellButton.setForeground(Color.white); // Reset foreground for other symbols
                    } else if(currentSymbol.equals("🎯")) {
                        cellButton.setBackground(new Color(255, 173, 50));
                        cellButton.setForeground(Color.WHITE);
                    }
                    cellButton.setText(currentSymbol); // Set the text to the chosen symbol
                    cellButton.setEnabled(false); // Disable the button after placing the symbol
                });
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
        boolean[][] visited = new boolean[gridRows][gridCols];
        visited[happyFaceRow][happyFaceCol] = true;

        // To reconstruct the path
        int[][] parentRow = new int[gridRows][gridCols];
        int[][] parentCol = new int[gridRows][gridCols];
        for (int i = 0; i < gridRows; i++) {
            Arrays.fill(parentRow[i], -1);
            Arrays.fill(parentCol[i], -1);
        }

        // List to store the cells visited in BFS order
        List<int[]> bfsOrder = new ArrayList<>();
        bfsOrder.add(new int[]{happyFaceRow, happyFaceCol});

        boolean[] found = {false};

        // Separate thread for BFS processing
        new Thread(() -> {
            // BFS loop
            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int row = current[0];
                int col = current[1];

                // Check if goal is reached
                if (row == goalRow && col == goalCol) {
                    found[0] = true;
                    break;
                }

                // Explore neighbors
                for (int i = 0; i < 4; i++) {
                    int newRow = row + dr[i];
                    int newCol = col + dc[i];

                    // Check boundaries and if cell is unvisited and not an 'X'
                    if (newRow >= 0 && newRow < gridRows && newCol >= 0 && newCol < gridCols
                            && !visited[newRow][newCol]
                            && !"X".equals(cellButtons[newRow][newCol].getText())) {

                        queue.add(new int[]{newRow, newCol});
                        visited[newRow][newCol] = true;

                        // Add to BFS order for visualization
                        bfsOrder.add(new int[]{newRow, newCol});

                        // Track the parent for path reconstruction
                        parentRow[newRow][newCol] = row;
                        parentCol[newRow][newCol] = col;
                    }
                }
            }

            // Visualization loop
            for (int i = 0; i < bfsOrder.size(); i++) {
                int[] cell = bfsOrder.get(i);
                int row = cell[0];
                int col = cell[1];

                // Skip the start and goal cells
                if (!(row == happyFaceRow && col == happyFaceCol) &&
                        !(row == goalRow && col == goalCol)) {

                    int finalRow = row;
                    int finalCol = col;

                    SwingUtilities.invokeLater(() -> {
                        cellButtons[finalRow][finalCol].setBackground(Color.LIGHT_GRAY);
                    });

                    // Delay for slow motion
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Once visualization is complete, reconstruct the path if found
            SwingUtilities.invokeLater(() -> {
                if (found[0]) {
                    reconstructPath(parentRow, parentCol);
                } else {
                    JOptionPane.showMessageDialog(null, "No path found using BFS.");
                }
            });
        }).start();
    }


    //=========================================================================================================

    private void dfs() {
        // Check if the starting and goal positions are valid
        if (happyFaceRow == -1 || happyFaceCol == -1 || goalRow == -1 || goalCol == -1) {
            JOptionPane.showMessageDialog(this, "Place both the Happy Face 😊 and the Goal 🎯 before starting!");
            return;
        }

        // Create a 2D array to track visited cells
        boolean[][] visited = new boolean[gridRows][gridCols];

        // Create a stack to simulate the recursive behavior of Depth First Search (DFS)
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{happyFaceRow, happyFaceCol}); // Push the starting position (Happy Face) onto the stack

        // Arrays to keep track of the parent cell for each cell (used for path reconstruction)
        int[][] parentRow = new int[gridRows][gridCols];
        int[][] parentCol = new int[gridRows][gridCols];

        // Initialize parent arrays with -1, indicating no parent yet
        for (int i = 0; i < gridRows; i++) {
            Arrays.fill(parentRow[i], -1);
            Arrays.fill(parentCol[i], -1);
        }

        // List to store cells visited in DFS order
        List<int[]> dfsOrder = new ArrayList<>();
        dfsOrder.add(new int[]{happyFaceRow, happyFaceCol});

        boolean[] found = {false}; // Flag to check if the goal is reached

        // Separate thread for DFS execution
        new Thread(() -> {
            // Main DFS loop
            while (!stack.isEmpty()) {
                int[] current = stack.pop(); // Get the current cell from the stack
                int row = current[0];
                int col = current[1];

                // Skip this cell if it has already been visited
                if (visited[row][col]) continue;
                visited[row][col] = true; // Mark the current cell as visited

                // Add the cell to the DFS order for visualization
                dfsOrder.add(new int[]{row, col});

                // Check if the goal (🎯) is reached
                if (row == goalRow && col == goalCol) {
                    found[0] = true;
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
                    if (newRow >= 0 && newRow < gridRows && newCol >= 0 && newCol < gridCols
                            && !visited[newRow][newCol]
                            && !"X".equals(cellButtons[newRow][newCol].getText())) {
                        stack.push(new int[]{newRow, newCol}); // Add the neighbor to the stack
                        parentRow[newRow][newCol] = row; // Record the current cell as the parent of the neighbor
                        parentCol[newRow][newCol] = col;
                    }
                }
            }

            // Visualization loop
            for (int i = 0; i < dfsOrder.size(); i++) {
                int[] cell = dfsOrder.get(i);
                int row = cell[0];
                int col = cell[1];

                // Skip the start and goal cells
                if (!(row == happyFaceRow && col == happyFaceCol) &&
                        !(row == goalRow && col == goalCol)) {

                    int finalRow = row;
                    int finalCol = col;

                    SwingUtilities.invokeLater(() -> {
                        cellButtons[finalRow][finalCol].setBackground(Color.LIGHT_GRAY);
                    });

                    // Delay for slow motion
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Once visualization is complete, reconstruct the path if found
            SwingUtilities.invokeLater(() -> {
                if (found[0]) {
                    reconstructPath(parentRow, parentCol);
                } else {
                    JOptionPane.showMessageDialog(null, "No path found using DFS.");
                }
            });
        }).start();
    }


    //=========================================================================================================

    private void aStar() {
        // Check if the starting and goal positions are valid
        if (happyFaceRow == -1 || happyFaceCol == -1 || goalRow == -1 || goalCol == -1) {
            JOptionPane.showMessageDialog(this, "Place both the Happy Face 😊 and the Goal 🎯 before starting!");
            return;
        }

        // Priority Queue to store nodes, sorted by their cost (current cost + heuristic)
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(a.cost, b.cost));
        // Add the starting node (Happy Face) with a cost of 0
        pq.add(new Node(happyFaceRow, happyFaceCol, 0));

        // 2D array to track visited cells
        boolean[][] visited = new boolean[gridRows][gridCols];

        // Arrays to keep track of the parent of each cell (used for path reconstruction)
        int[][] parentRow = new int[gridRows][gridCols];
        int[][] parentCol = new int[gridRows][gridCols];
        for (int i = 0; i < gridRows; i++) {
            Arrays.fill(parentRow[i], -1); // Initialize with -1 (no parent)
            Arrays.fill(parentCol[i], -1);
        }

        // List to store the order of visited cells for visualization
        List<Node> visitedOrder = new ArrayList<>();

        // Separate thread for A* execution
        new Thread(() -> {
            boolean found = false;

            // Main A* loop
            while (!pq.isEmpty()) {
                // Get the node with the lowest cost from the priority queue
                Node current = pq.poll();
                int row = current.row;
                int col = current.col;

                // Skip this cell if it has already been visited
                if (visited[row][col]) continue;
                visited[row][col] = true; // Mark the current cell as visited

                // Add the cell to the visited order for visualization
                visitedOrder.add(current);

                // Check if the goal (🎯) has been reached
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
                    if (newRow >= 0 && newRow < gridRows && newCol >= 0 && newCol < gridCols
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

            // Visualization loop
            for (Node node : visitedOrder) {
                int row = node.row;
                int col = node.col;

                // Skip the start and goal cells
                if (!(row == happyFaceRow && col == happyFaceCol) &&
                        !(row == goalRow && col == goalCol)) {

                    int finalRow = row;
                    int finalCol = col;

                    SwingUtilities.invokeLater(() -> {
                        cellButtons[finalRow][finalCol].setBackground(Color.LIGHT_GRAY);
                    });

                    // Delay for slow motion
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Once visualization is complete, reconstruct the path if found
            boolean finalFound = found;
            SwingUtilities.invokeLater(() -> {
                if (finalFound) {
                    reconstructPath(parentRow, parentCol);
                } else {
                    JOptionPane.showMessageDialog(null, "No path found using A*.");
                }
            });
        }).start();
    }

    // Euclidean distance calculation
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
            // Skip coloring the goal cell
            if (!(row == goalRow && col == goalCol)) {
                // Highlight the current cell on the path with a green background
                cellButtons[row][col].setBackground(Color.GREEN);
            }

            // Move to the parent cell by retrieving its coordinates from the parent arrays
            int tempRow = parentRow[row][col];
            int tempCol = parentCol[row][col];
            row = tempRow;
            col = tempCol;

            // Increment the step count after moving to the parent
            stepCount++;
        }

        // Skip coloring the starting point (😊)
        if (!(row == happyFaceRow && col == happyFaceCol)) {
            cellButtons[happyFaceRow][happyFaceCol].setBackground(Color.GREEN);
        }

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
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
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
            Action lastAction = actionStack.pop();  // Pop the last action from the stack

            // Restore the previous state of the cell (text and button state)
            cellButtons[lastAction.row][lastAction.col].setText(lastAction.previousText);

            // Reset the button to its default state (normal form)
            cellButtons[lastAction.row][lastAction.col].setEnabled(true); // Re-enable the button
            cellButtons[lastAction.row][lastAction.col].setBackground(null); // Reset background to default
            cellButtons[lastAction.row][lastAction.col].setText(""); // Reset the text to default

            // Handle special cases for the Happy Face (😊) and Goal (🎯) symbols
            if (lastAction.previousText.equals("😊")) {
                happyFaceRow = -1; // Clear the Happy Face position
                happyFaceCol = -1;
                happyFaceButton.setEnabled(true); // Re-enable the Happy Face button
            } else if (lastAction.previousText.equals("🎯")) {
                goalRow = -1; // Clear the Goal position
                goalCol = -1;
                goalButton.setEnabled(true); // Re-enable the Goal button
            }
        } else {
            JOptionPane.showMessageDialog(this, "No actions to undo.");
        }
    }



    //=========================================================================================================

    private void resetPath() {
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                if (cellButtons[row][col].getBackground() == Color.GREEN || cellButtons[row][col].getBackground() == Color.LIGHT_GRAY) {
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