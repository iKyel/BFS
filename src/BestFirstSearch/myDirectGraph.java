package BestFirstSearch;
import edu.princeton.cs.algs4.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class myDirectGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    private String[] vertexNames;
    private int[] vertexValues; // Giá trị của mỗi đỉnh
    
    public myDirectGraph(int V, String[] vertexNames, int[] vertexValues) {
        if (V < 0 || V != vertexNames.length || V != vertexValues.length)
            throw new IllegalArgumentException("Invalid number of vertices or vertexNames array length mismatch");
        this.V = V;
        this.E = 0;
        
        this.vertexNames = vertexNames.clone();
        this.vertexValues = vertexValues.clone();
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }       
    }
    
    // đọc đồ thị từ txt
    public myDirectGraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            this.vertexNames = new String[V];
            this.vertexValues = new int[V];
            adj = (Bag<Integer>[]) new Bag[V];
            for (int v = 0; v < V; v++) {
                vertexNames[v] = in.readString();
                vertexValues[v] = in.readInt();
                adj[v] = new Bag<Integer>();
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be non-negative");
            for (int i = 0; i < E; i++) {
                String v = in.readString();
                String w = in.readString();
                int value = in.readInt();
                addEdge(v, w);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }

    // đọc đồ thị
//    public myGraph(myGraph G) {
//        this.V = G.V();
//        this.E = G.E();
//        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
//
//        // update adjacency lists
//        adj = (Bag<Integer>[]) new Bag[V];
//        for (int v = 0; v < V; v++) {
//            adj[v] = new Bag<Integer>();
//        }
//
//        for (int v = 0; v < G.V(); v++) {
//            // reverse so that adjacency list is in same order as original
//            Stack<Integer> reverse = new Stack<Integer>();
//            for (int w : G.adj[v]) {
//                reverse.push(w);
//            }
//            for (int w : reverse) {
//                adj[v].add(w);
//            }
//        }
//    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);     
        E++;
        adj[v].add(w);
//        adj[w].add(v);
    }
    
    public void addEdge(String v, String w) {
        int indexV = indexOfVertex(v);
        int indexW = indexOfVertex(w);
        if (indexV == -1 || indexW == -1) {
            throw new IllegalArgumentException("Invalid vertex names: " + v + ", " + w);
        }
        addEdge(indexV, indexW);
    }
    
    int indexOfVertex(String vertexName) {
        for (int i = 0; i < V; i++) {
            if (vertexNames[i].equals(vertexName)) {
                return i;
            }
        }
        return -1;
    }
    
    public String[] getVertexNames() {
        return vertexNames.clone();
    }
    

    public int[] getVertexValues() {
        return vertexValues.clone();
    }
       
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
    
    private int findEdgeIndex(int v, int w) {
        for (int i : adj[v]) {
            if (i == w) {
                return i;
            }
        }
        return -1;
    }

//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append(V + " vertices, " + E + " edges " + NEWLINE);
//        for (int v = 0; v < V; v++) {
//            s.append(vertexNames[v] + ": ");
//            for (int w : adj[v]) {
//                s.append(vertexNames[w] + " ");
//            }
//            s.append(NEWLINE);
//        }
//        return s.toString();
//    }

    public static void main(String[] args) {
    	// Đọc dữ liệu từ đầu vào
        In in = new In(args[0]);
        int V = in.readInt(); // Số lượng đỉnh
        String[] vertexNames = new String[V];
        int[] vertexValues = new int[V];
        
        for (int i = 0; i < V; i++) {
            String name = in.readString();
            int value = in.readInt();
            vertexNames[i] = name;
            vertexValues[i] = value;
        }

        int E = in.readInt(); // Số lượng cạnh
        myDirectGraph G = new myDirectGraph(V, vertexNames, vertexValues);

        // Đọc thông tin về các cạnh và thêm vào đồ thị
        for (int i = 0; i < E; i++) {
            String v = in.readString();
            String w = in.readString();
            G.addEdge(v, w);
        }

     // In ra đồ thị theo định dạng yêu cầu
        for (int v = 0; v < V; v++) {
            
            for (int w : G.adj(v)) {
            	System.out.print(G.getVertexNames()[v] + "-" + G.getVertexValues()[v] + " ");
                System.out.print(G.getVertexNames()[w] + "-" + G.getVertexValues()[w] + " ");               
                System.out.print("\n");
            }
            System.out.println();
        }
    }

}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
