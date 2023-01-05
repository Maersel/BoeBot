package controllers.pathfinding;

public class Node {
    private int id;
    private int[] neigbours;

    public Node(int id, int[] neigbours) {
        this.id = id;
        this.neigbours = neigbours;
    }

    public int getId() {
        return id;
    }

    public int[] getNeigbours() {
        return neigbours;
    }

    public void removeNodeFromNeigbour(int node) {
        for (int i = 0; i < 4; i++) {
            if (this.neigbours[i] == node) {
                this.neigbours[i] = 0;
            }
        }
    }
}
