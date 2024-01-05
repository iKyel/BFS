import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private final int s; // source vertex

    public BreadthFirstPaths(myGraph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(myGraph G, int s) {
        Queue<Integer> queue = new LinkedList<>();
        marked[s] = true;
        queue.add(s);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    marked[w] = true;
                    queue.add(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;

        LinkedList<Integer> path = new LinkedList<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.addFirst(x);
        }
        path.addFirst(s);

        return path;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        myGraph G = new myGraph(in);

        char sourceVertex = 'A'; // Replace with the desired source vertex
        int sourceIndex = -1;

        String[] vertexNames = G.getVertexNames();
        for (int i = 0; i < vertexNames.length; i++) {
            if (vertexNames[i].charAt(0) == sourceVertex) {
                sourceIndex = i;
                break;
            }
        }

        if (sourceIndex == -1) {
            StdOut.println("Invalid source vertex: " + sourceVertex);
            return;
        }

        BreadthFirstPaths bfs = new BreadthFirstPaths(G, sourceIndex);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%c to %c:  ", sourceVertex, vertexNames[v].charAt(0));
                for (int x : bfs.pathTo(v)) {
                    StdOut.print(vertexNames[x] + " ");
                }
                StdOut.println();
            } else {
                StdOut.printf("%c to %c:  not connected\n", sourceVertex, vertexNames[v].charAt(0));
            }
        }
    }
}