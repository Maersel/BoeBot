package controllers.pathfinding;

import controllers.RouteOptions;

import java.util.ArrayList;
import java.util.Collections;

public class Pathfinder {
//    public static void main(String[] args) {
//        Pathfinder pathfinder = new Pathfinder();
//
//        int start = 3;
//        int end = 20;
//        RouteOptions[] directions = pathfinder.findPath(start, end);
//        pathfinder.printDirections(start, end, directions);
//
//        System.out.println("\n");
//
//        pathfinder.addObstacle(5);
//        pathfinder.addObstacle(2);
//        pathfinder.addObstacle(4);
//        directions = pathfinder.findPath(start, end);
//        pathfinder.printDirections(start, end, directions);
//    }

    private Node[] nodes;
    private ArrayList<Integer> obstacles;

    public Pathfinder() {
        this.nodes = MapSetter.setMap();
        this.obstacles = new ArrayList<>();
    }

    private RouteOptions[] findPath(int startingPoint, int endPoint) {
        ArrayList<Integer> path = this.nodePath(startingPoint, endPoint);

        return this.pathDirections(path);
    }

    private void addObstacle(int obstaclePoint) {
        if (!this.obstacles.contains(obstaclePoint))
            this.obstacles.add(obstaclePoint);
        for (Node node : nodes) {
            node.removeNodeFromNeigbour(obstaclePoint);
        }
    }

    private void resetMap() {
        this.nodes = MapSetter.setMap();
    }

    public ArrayList<Integer> nodePath(int startingPoint, int endPoint) {
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
            System.out.println("Route niet mogelijk");
            return new ArrayList<>();
        }

        return path;
    }

    // Gaat ervan uit dat de bot al de juiste richting op kijkt
    public RouteOptions[] pathDirections(ArrayList<Integer> path) {
        if (path.isEmpty()) return new RouteOptions[0];

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

    private RouteOptions[] translateDirections(int[] directionCodes) {
        RouteOptions[] pathDirections = new RouteOptions[directionCodes.length];
        RouteOptions direction = RouteOptions.NOTHING;
        for (int i = 0; i < directionCodes.length; i++) {
            switch (directionCodes[i]) {
                case -1:
                    direction = RouteOptions.LEFT;
                    break;
                case 0:
                    direction = RouteOptions.STRAIGHT;
                    break;
                case 1:
                    direction = RouteOptions.RIGHT;
                    break;
            }
            pathDirections[i] = direction;
        }

        return pathDirections;
    }

    private void printDirections(int startingPoint, int endPoint, RouteOptions[] route) {
        if (route.length == 0) return;

        System.out.println("Going from point\t" + startingPoint);
        System.out.println("To point\t\t\t" + endPoint);

        if (!this.obstacles.isEmpty()) {
            System.out.println("\nObstacles");
            for (Integer obstacle : this.obstacles) {
                System.out.println(obstacle);
            }
        }


        System.out.println("\nDirections");
        for (RouteOptions routeOptions : route) {
            System.out.println(routeOptions);
        }
    }
}
