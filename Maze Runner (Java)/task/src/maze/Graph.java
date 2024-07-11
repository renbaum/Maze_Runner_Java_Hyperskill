package maze;

import java.io.Serializable;
import java.util.*;

public class Graph<T> implements Serializable {
    private Map<T, List<T>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public T getVertex(T obj) {
        if (!adjList.containsKey(obj)) return null;
        List<T> nodes = (List<T>) adjList.keySet();
        for (T node : nodes) {
            if (node.equals(obj)) return node;
        }
        return null;
    }

    public List<T> getAllVertex() {
        Set<T> result = (Set<T>) adjList.keySet();
        return new ArrayList<>(result);
    }

    public List<T> getNeighbors(T vertex) {
        return adjList.get(vertex);
    }

    public boolean existsVertex(T obj) {
        return adjList.containsKey(obj);
    }

    private void addEdgeToNode(T source, T target){
        List<T> neighbors = adjList.get(source);
        if(neighbors==null) return;
        if(neighbors.contains(target)) return;
        neighbors.add(target);
    }

    public void addEdge(T source, T destination) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>());
        addEdgeToNode(source, destination);
        addEdgeToNode(destination, source);
    }

    public void removeVertex(T vertex) {
        adjList.values().forEach(e -> e.remove(vertex));
        adjList.remove(vertex);
    }

    public void removeEdge(T source, T destination) {
        List<T> srcList = adjList.get(source);
        List<T> destList = adjList.get(destination);
        if (srcList != null) srcList.remove(destination);
        if (destList != null) destList.remove(source);
    }

    public void DFS(T start) {
        Set<T> visited = new HashSet<>();
        Stack<T> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            T vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                System.out.print(vertex + " ");
                for (T neighbor : adjList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        System.out.println();
    }

    public void BFS(T start) {
        Set<T> visited = new HashSet<>();
        Queue<T> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            T vertex = queue.poll();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                System.out.print(vertex + " ");
                for (T neighbor : adjList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        System.out.println();
    }

    public boolean findPath(T start, T end, List<T> visited) {
        if(visited == null) visited = new ArrayList<>();

        if(visited.contains(start)) return false;
        visited.add(start);
        if(start.equals(end)) return true;

        List<T> list = new ArrayList<>(getNeighbors(start));
        boolean found = false;

        for (T neighbor : list) {
            if(visited.contains(neighbor)) continue;
            if(!findPath(neighbor, end, visited)){
                removeEdge(neighbor, end);
                removeVertex(neighbor);
            }else{
                found = true;
            }
        }
        return found;
    }
}
