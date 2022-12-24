package controllers.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

public class Pathfinder {
    public static void main(String[] args) {
        Pathfinder pathfinder = new Pathfinder();
        ArrayList<Integer> path = pathfinder.nodePath(3, 20);

        String[] directions = pathfinder.pathDirections(path);

        System.out.println("\nDirections:");
        for (String direction : directions) {
            System.out.println(direction);
        }
    }

    private Node[] nodes;

    public Pathfinder() {
        this.nodes = MapSetter.setMap();
    }

    private ArrayList<Integer> nodePath(int startingPoint, int endPoint) {
        System.out.println("Going from point\t" + startingPoint);
        System.out.println("To point\t\t\t" + endPoint);

        // Breadth first search vanuit het startpunt
        int[] prev = solve(startingPoint);

        // Maak het pad vanuit het startpunt naar het eindpunt
        return reconstructPath(startingPoint, endPoint, prev);
    }

    private int[] solve(int startingPoint) {
        ArrayList<Integer> queue = new ArrayList<>();

        boolean[] visited = new boolean[this.nodes.length];
        visited[startingPoint] = true;

        int[] prev = new int[this.nodes.length];

        queue.add(startingPoint);
        while (!queue.isEmpty()) {
            int node = queue.get(0);
            queue.remove(0);

            int[] neigbours = nodes[node].getNeigbours();
            for (int neigbour : neigbours) {
                if (!visited[neigbour]) {
                    queue.add(neigbour);
                    visited[neigbour] = true;
                    prev[neigbour] = node;
                }
            }
        }

        return prev;
    }

    private ArrayList<Integer> reconstructPath(int startingPoint, int endPoint, int[] prev) {
        ArrayList<Integer> path = new ArrayList<>();

        // Vanuit het eindpunt terug kijken (path is achterstevoren)
        for (int at = endPoint; at != 0; at = prev[at]) {
            path.add(at);
        }

        Collections.reverse(path);

        // Als er geen pad mogelijk is return een lege arraylist
        if (path.get(0) != startingPoint) {
            System.out.println("kan niet");
            return new ArrayList<>();
        }

        return path;
    }

    // Gaat ervan uit dat de bot al de juiste richting op kijkt
    private String[] pathDirections(ArrayList<Integer> path) {

        int[] directionCodes = new int[path.size() - 2];

        for (int i = 1; i < path.size() - 1; i++) {
            int[] neighbours = this.nodes[path.get(i)].getNeigbours();
            int origin = 0;
            int target = 0;

            for (int j = 0; j < 4; j++) {
                if (neighbours[j] == path.get(i - 1)) {
                    origin = j;
                }
                if (neighbours[j] == path.get(i + 1)) {
                    target = j;
                }
            }

            int front = (origin + 2) % 4;
            int direction = target - front ;

            directionCodes[i - 1] = direction;
//            System.out.println("Node:\t" + path.get(i) + "\tOrigin:\t" + origin + "\tTarget:\t" + target + "\tDirection:\t" + direction);
        }


        return translateDirections(directionCodes);
    }

    private String[] translateDirections(int[] directionCodes) {
        String[] pathDirections = new String[directionCodes.length];
        String directionName = "";
        for (int i = 0; i < directionCodes.length; i++) {
            switch (directionCodes[i]) {
                case -1:
                    directionName = "Left";
                    break;
                case 0:
                    directionName = "Straight";
                    break;
                case 1:
                    directionName = "Right";
                    break;
            }
            pathDirections[i] = directionName;
        }

        return pathDirections;
    }
}
