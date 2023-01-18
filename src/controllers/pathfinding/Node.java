package controllers.pathfinding;

public class Node {
    private int id;
    private int[] neighbours;

    public Node(int id, int[] neighbours) {
        this.id = id;
        this.neighbours = neighbours;
    }

    public int getId() {
        return id;
    }

    public int[] getNeigbours() {
        return neighbours;
    }

    public void removeNodeFromNeigbour(int node) {
        for (int i = 0; i < 4; i++) {
            if (this.neighbours[i] == node) {
                this.neighbours[i] = 0;
            }
        }
    }
}
