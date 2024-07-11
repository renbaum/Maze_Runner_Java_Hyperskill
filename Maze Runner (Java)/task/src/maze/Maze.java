package maze;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Maze implements Serializable {
    private int[][] maze;
    private int row = 0;
    private int col = 0;
    Graph<Node> graph;
    Set<Node> potential;
    Node startPoint = null;
    Node endPoint = null;
    

    public Maze(int row, int col){
        this.row = row;
        this.col = col;
        graph = new Graph<>();
        potential = new HashSet<>();

        maze = new int[this.row][this.col];
        for(int r = 0; r < this.row; r++){
            for(int c = 0; c < this.col; c++){
                setElement(r, c, 1);
            }
        }
    }

    public void print(){
        for(int i = 0; i<maze.length; i++){
            for (int j = 0; j < maze[i].length; j++) {
                printElement(maze[i][j]);
            }
            System.out.println();
        }
    }

    private void printElement(int element){
        switch (element){
            case 1:
                System.out.print("\u2588\u2588");
                break;
            case 0:
                System.out.print("  ");
                break;
            case 2:
                System.out.print("//");
                break;
        }
    }

    public void setElement(int row, int col, int value) {
        maze[row][col] = value;
    }

    public Node createRandomNode() {
        int r = (int) (Math.random() * (row -1)/2) * 2 + 1;
        int c = (int) (Math.random() * (col -1)/2) * 2 + 1;

        Node node = new Node(r, c);
        graph.addVertex(node);
        return node;
    }

    private void getPossibleFreeNeighbors(Node node) {
        int r = node.getRow();
        int c = node.getCol();
        Node top = new Node(r - 2, c);
        Node bottom = new Node(r + 2, c);
        Node left = new Node(r, c - 2);
        Node right = new Node(r, c + 2);

        if (isNodeValid(top) && !graph.existsVertex(top)) potential.add(top);
        if (isNodeValid(bottom) && !graph.existsVertex(bottom)) potential.add(bottom);
        if (isNodeValid(left) && !graph.existsVertex(left)) potential.add(left);
        if (isNodeValid(right) && !graph.existsVertex(right)) potential.add(right);
    }

    private List<Node> getPossibleNeighbors(Node node) {
        int r = node.getRow();
        int c = node.getCol();
        List<Node> neighbors = new ArrayList<>();
        Node top = new Node(r - 2, c);
        Node bottom = new Node(r + 2, c);
        Node left = new Node(r, c - 2);
        Node right = new Node(r, c + 2);

        if (graph.existsVertex(top)) neighbors.add(top);
        if (graph.existsVertex(bottom)) neighbors.add(bottom);
        if (graph.existsVertex(left)) neighbors.add(left);
        if (graph.existsVertex(right)) neighbors.add(right);
        return neighbors;
    }


    private boolean isNodeValid(Node node){
        if (node.getCol() < 1) return false;
        if (node.getCol() >= this.col - 1) return false;
        if(node.getRow() < 1) return false;
        if (node.getRow() >= this.row - 1) return false;
        return true;
    }

    public Node getNode(int row, int col){
        Node n = graph.getVertex(new Node(row, col));
        return n;
    }

    public void generate(){
        Node node = createRandomNode();
        getPossibleFreeNeighbors(node);
        Random rand = new Random();

        while (!potential.isEmpty()) {
            List<Node> list = new ArrayList<>(potential);

            Node nodePotential = list.get(rand.nextInt(list.size()));
            List<Node> neighbors = getPossibleNeighbors(nodePotential);
            Node randomNeighbor = neighbors.get(rand.nextInt(neighbors.size()));

            graph.addEdge(nodePotential, randomNeighbor);
            graph.addEdge(randomNeighbor, nodePotential);
            potential.remove(nodePotential);
            getPossibleFreeNeighbors(nodePotential);
        }
    }

    public void setEntryPoint(){
        List<Node> list = graph.getAllVertex().stream()
                .filter(n -> n.getRow() == 1)
                .collect(Collectors.toList());
        Random rand = new Random();
        Node node = list.get(rand.nextInt(list.size()));
        startPoint = new Node(node.getRow(), 0);
        graph.addEdge(startPoint, node);
        //maze[node.getRow()][0] = 0;
    }

    public void setExitPoint(){
        int maxCol = graph.getAllVertex().stream()
                .max(Comparator.comparingInt(n -> n.getCol())).get().getCol();
        List<Node> list = graph.getAllVertex().stream()
                .filter(n -> n.getCol() == maxCol)
                .collect(Collectors.toList());
        Random rand = new Random();
        Node node = list.get(rand.nextInt(list.size()));
        endPoint = new Node(node.getRow(), col-1);
        graph.addEdge(endPoint, node);
//        maze[node.getRow()][col - 1] = 0;
    }

    public void visualize(){
        List<Node> nodes = graph.getAllVertex();
        for(Node n : nodes){
            List<Node> neighbors = graph.getNeighbors(n);
            maze[n.getRow()][n.getCol()] = 0;
            for(Node nn : neighbors){
                maze[nn.getRow()][nn.getCol()] = 0;
                int dCol = (nn.getCol() - n.getCol()) / 2;
                int dRow = (nn.getRow() - n.getRow()) / 2;
                maze[n.getRow() + dRow][n.getCol() + dCol] = 0;
            }
        }
    }

    public void findPath() {
        visualize();
        if(!graph.findPath(startPoint, endPoint, null)){
            System.out.println("Sorry, aber ich kann nichts finden");
            return;
        }
        visualizePath();
        print();
    }

    private void visualizePath() {
        List<Node> nodes = graph.getAllVertex();
        for(Node n : nodes){
            List<Node> neighbors = graph.getNeighbors(n);
            maze[n.getRow()][n.getCol()] = 2;
            for(Node nn : neighbors){
                maze[nn.getRow()][nn.getCol()] = 2;
                int dCol = (nn.getCol() - n.getCol()) / 2;
                int dRow = (nn.getRow() - n.getRow()) / 2;
                maze[n.getRow() + dRow][n.getCol() + dCol] = 2;
            }
        }
    }

}
