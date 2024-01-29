import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CollegeMapDrawingWithBuildings extends JFrame {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 810;

    private Map<String, Point> buildings = new HashMap<>();
    private Map<String, Map<String, Integer>> connections = new HashMap<>();
    private List<String> shortestPath = new ArrayList<>();
    private int shortestPathCost = 0;

    private JLabel costLabel;

    public CollegeMapDrawingWithBuildings() {
        setTitle("College Map Drawing");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        Canvas canvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                for (Map.Entry<String, Point> entry : buildings.entrySet()) {
                    String buildingName = entry.getKey();
                    Point point = entry.getValue();
                    int x = point.x;
                    int y = point.y;
                    ImageIcon buildingIcon;
                    g.setColor(Color.BLACK);
                    if (buildingName.equals("i1")) {
                        buildingIcon = createResizedIcon("acd.jpeg", 1200, 700);
                        // buildingIcon = createResizedIcon("dot.jpg", 5, 5);
                    } else if (buildingName.equals("i2")) {

                        buildingIcon = createResizedIcon("Details.png", 300, 500);
                    } else if (buildingName.equals("i3")) {

                        buildingIcon = createResizedIcon("icon.jpg", 100, 100);
                    } else {
                        buildingIcon = createResizedIcon("dot.jpg", 5, 5);
                    }
                    buildingIcon.paintIcon(this, g, x, y);
                    g.drawString(buildingName, x + 10, y - 10);
                }

                g.setColor(Color.GREEN);
                Graphics2D g2d = (Graphics2D) g;
                float strokeWidth = 3.0f;
                g2d.setStroke(new BasicStroke(strokeWidth));

                g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                int totalCost = 0;
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    Point startPoint = buildings.get(shortestPath.get(i));
                    Point endPoint = buildings.get(shortestPath.get(i + 1));
                    int cost = connections.get(shortestPath.get(i)).get(shortestPath.get(i + 1));
                    totalCost += cost;
                    int centerX = (startPoint.x + endPoint.x) / 2;
                    int centerY = (startPoint.y + endPoint.y) / 2;
                    g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    g2d.drawString(Integer.toString(cost), centerX, centerY);
                }
                g.setColor(Color.BLACK);
                g.drawString("Total Cost: " + totalCost, 10, 20);
            }
        };
        canvas.setBackground(Color.WHITE);

        mainPanel.add(canvas, BorderLayout.CENTER);

        buildings.put("Gate1", new Point(110, 305));
        buildings.put("Gate2", new Point(115, 470));
        buildings.put("Gate3", new Point(130, 700));
        buildings.put("Gate4", new Point(990, 80));
        buildings.put("centr1", new Point(475, 530));
        buildings.put("centr2", new Point(445, 460));
        buildings.put("centr3", new Point(775, 320));
        buildings.put("p2", new Point(650, 315));
        buildings.put("p1", new Point(150, 300));
        buildings.put("ad2", new Point(150, 470));
        buildings.put("g1", new Point(210, 470));
        buildings.put("g2", new Point(420, 460));
        buildings.put("g3", new Point(210, 310));
        buildings.put("g4", new Point(420, 310));
        buildings.put("ar1", new Point(445, 314));
        buildings.put("ar2", new Point(690, 328));
        buildings.put("cat1", new Point(150, 679));
        buildings.put("ac1", new Point(535, 451));
        buildings.put("ac2", new Point(685, 370));
        buildings.put("m1", new Point(825, 285));
        buildings.put("m2", new Point(895, 193));
        buildings.put("b1", new Point(985, 107));
        buildings.put("i1", new Point(50, 20));
        buildings.put("i2", new Point(1200, 200));
        buildings.put("i3", new Point(1300, 50));
        JTextField startLocationField = new JTextField(10);
        JTextField endLocationField = new JTextField(10);
        JButton findPathButton = new JButton("Find Shortest Path");

        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startLocation = startLocationField.getText();
                String endLocation = endLocationField.getText();
                shortestPath = findShortestPath(startLocation, endLocation);
                shortestPathCost = calculatePathCost(shortestPath);
                costLabel.setText("Cost: " + shortestPathCost);
                canvas.repaint();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Start Location: "));
        inputPanel.add(startLocationField);
        inputPanel.add(new JLabel("End Location: "));
        inputPanel.add(endLocationField);
        inputPanel.add(findPathButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        costLabel = new JLabel("Cost: ");
        mainPanel.add(costLabel, BorderLayout.NORTH);

        addConnection("Gate1", "p1", 1);
        addConnection("p1", "g3", 1);
        addConnection("g3", "g4", 4);
        addConnection("g4", "ar1", 1);
        addConnection("ar1", "p2", 5);
        addConnection("p2", "centr3", 2);
        addConnection("m1", "m2", 3);
        addConnection("m2", "b1", 3);
        addConnection("b1", "Gate4", 1);
        addConnection("Gate1", "Gate2", 4);
        addConnection("Gate2", "Gate3", 4);
        addConnection("Gate2", "ad2", 1);
        addConnection("ad2", "g1", 1);
        addConnection("g1", "g2", 4);
        addConnection("g2", "centr2", 1);
        addConnection("centr2", "ac1", 1);
        addConnection("ac1", "ac2", 3);
        addConnection("ac2", "centr3", 1);
        addConnection("centr3", "m1", 1);
        addConnection("p2", "ar2", 1);
        addConnection("ar2", "ac2", 1);
        addConnection("Gate3", "cat1", 1);
        addConnection("cat1", "centr1", 5);
        addConnection("centr1", "centr2", 1);
        addConnection("g1", "g3", 4);
        addConnection("g1", "g4", 6);
        addConnection("g2", "g3", 6);
        addConnection("g2", "g4", 4);
        addConnection("ar1", "centr2", 4);
        addConnection("p1", "ad2", 4);
        addConnection("i1", "Gate1", 0);
        addConnection("Gate4", "i2", 0);
        addConnection("i2", "i3", 0);
        Container content = getContentPane();
        content.add(mainPanel);
    }

    private void addConnection(String startLocation, String endLocation, int distance) {
        connections.computeIfAbsent(startLocation, k -> new HashMap<>()).put(endLocation, distance);
        connections.computeIfAbsent(endLocation, k -> new HashMap<>()).put(startLocation, distance);
    }

    private List<String> findShortestPath(String startLocation, String endLocation) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousLocations = new HashMap<>();
        Set<String> unvisitedLocations = new HashSet<>(buildings.keySet());

        for (String location : buildings.keySet()) {
            distances.put(location, Integer.MAX_VALUE);
        }
        distances.put(startLocation, 0);

        while (!unvisitedLocations.isEmpty()) {
            String currentLocation = null;
            for (String location : unvisitedLocations) {
                if (currentLocation == null || distances.get(location) < distances.get(currentLocation)) {
                    currentLocation = location;
                }
            }

            unvisitedLocations.remove(currentLocation);

            for (Map.Entry<String, Integer> neighbor : connections.get(currentLocation).entrySet()) {
                int altDistance = distances.get(currentLocation) + neighbor.getValue();
                if (altDistance < distances.get(neighbor.getKey())) {
                    distances.put(neighbor.getKey(), altDistance);
                    previousLocations.put(neighbor.getKey(), currentLocation);
                }
            }
        }

        List<String> path = new ArrayList<>();
        String currentLocation = endLocation;
        while (currentLocation != null) {
            path.add(currentLocation);
            currentLocation = previousLocations.get(currentLocation);
        }
        Collections.reverse(path);

        return path;
    }

    private int calculatePathCost(List<String> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String currentLocation = path.get(i);
            String nextLocation = path.get(i + 1);
            cost += connections.get(currentLocation).get(nextLocation);
        }
        return cost;
    }

    private ImageIcon createResizedIcon(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image image = originalIcon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CollegeMapDrawingWithBuildings frame = new CollegeMapDrawingWithBuildings();
            frame.setVisible(true);
        });
    }
}
