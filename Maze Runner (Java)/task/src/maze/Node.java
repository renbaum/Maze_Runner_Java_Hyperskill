package maze;

import java.io.Serializable;
import java.util.Objects;

public class Node implements Serializable {
    private int row = 0;
    private int col = 0;
    private boolean visited = false;

    public Node(int row, int col){
        this.row = row;
        this.col = col;
    }

    public void setVisited(){
        this.visited = true;
    }

    public boolean isVisited(){
        return this.visited;
    }

    public int getRow(){
        return row;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getCol(){
        return col;
    }

    public void setCol(int col){
        this.col = col;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node node = (Node) obj;
        return row == node.row && col == node.col;
    }

    @Override
    public int hashCode(){
        return Objects.hash(row, col);
    }
}
