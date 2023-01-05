package controllers.pathfinding;

import java.util.ArrayList;

public class MapSetter {
    public static Node[] setMap() {
        ArrayList<Node> nodes = new ArrayList<>();
        // 1e neighbour wijst naar het middelpunt van het diagram.
        // 2e, 3e en 4e neigbours gaan met de klok mee.
        // 0 is een eindpunt / geen neighbour

        // Crossovers
        // Inner circle
        nodes.add(new Node(0, new int[]{0, 0, 0, 0}));
        nodes.add(new Node(1, new int[]{13, 3, 4, 2}));
        nodes.add(new Node(2, new int[]{14, 1, 7, 15}));
        nodes.add(new Node(3, new int[]{17, 16, 10, 1}));
        // Outer circle
        nodes.add(new Node(4, new int[]{1, 12, 18, 5}));
        nodes.add(new Node(5, new int[]{27, 4, 19, 6}));
        nodes.add(new Node(6, new int[]{28, 5, 20, 7}));
        nodes.add(new Node(7, new int[]{2, 6, 21, 8}));
        nodes.add(new Node(8, new int[]{29, 7, 22, 9}));
        nodes.add(new Node(9, new int[]{30, 8, 23, 10}));
        nodes.add(new Node(10, new int[]{3, 9, 24, 11}));
        nodes.add(new Node(11, new int[]{31, 10, 25, 12}));
        nodes.add(new Node(12, new int[]{32, 11, 26, 4}));


        // Endpoints
        // Inner circle
        nodes.add(new Node(13, new int[]{0, 0, 1, 0}));
        nodes.add(new Node(14, new int[]{0, 0, 2, 0}));
        nodes.add(new Node(15, new int[]{0, 0, 2, 0}));
        nodes.add(new Node(16, new int[]{0, 0, 3, 0}));
        nodes.add(new Node(17, new int[]{0, 0, 3, 0}));
        // Outer circle
        nodes.add(new Node(18, new int[]{4, 0, 0, 0}));
        nodes.add(new Node(19, new int[]{5, 0, 0, 0}));
        nodes.add(new Node(20, new int[]{6, 0, 0, 0}));
        nodes.add(new Node(21, new int[]{7, 0, 0, 0}));
        nodes.add(new Node(22, new int[]{8, 0, 0, 0}));
        nodes.add(new Node(23, new int[]{9, 0, 0, 0}));
        nodes.add(new Node(24, new int[]{10, 0, 0, 0}));
        nodes.add(new Node(25, new int[]{11, 0, 0, 0}));
        nodes.add(new Node(26, new int[]{12, 0, 0, 0}));
        // Middle circle
        nodes.add(new Node(27, new int[]{0, 0, 5, 0}));
        nodes.add(new Node(28, new int[]{0, 0, 6, 0}));
        nodes.add(new Node(29, new int[]{0, 0, 8, 0}));
        nodes.add(new Node(30, new int[]{0, 0, 9, 0}));
        nodes.add(new Node(31, new int[]{0, 0, 11, 0}));
        nodes.add(new Node(32, new int[]{0, 0, 12, 0}));

        return nodes.toArray(new Node[nodes.size()]);
    }
}
